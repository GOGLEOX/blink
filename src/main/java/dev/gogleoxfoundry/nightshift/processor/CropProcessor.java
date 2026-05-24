package dev.gogleoxfoundry.nightshift.processor;

import dev.gogleoxfoundry.nightshift.config.NightshiftConfig;
import dev.gogleoxfoundry.nightshift.util.AdvancementContext;
import dev.gogleoxfoundry.nightshift.util.LoadedChunkScanner;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.PitcherCropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

public final class CropProcessor implements NightshiftProcessor {
    private static final String ID = "crop";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public ProcessorResult process(AdvancementContext context) {
        if (!NightshiftConfig.ENABLE_CROPS.get()) {
            return ProcessorResult.none(ID);
        }

        int maxCrops = NightshiftConfig.maxCropOperations();
        int growthAttempts = NightshiftConfig.cropGrowthAttempts();
        if (maxCrops <= 0 || growthAttempts <= 0 || context.sleeperPositions().isEmpty()) {
            return ProcessorResult.none(ID);
        }

        List<LevelChunk> candidateChunks = LoadedChunkScanner.collectNearbyChunks(context, maxCrops);
        if (candidateChunks.isEmpty()) {
            return ProcessorResult.none(ID);
        }

        ServerLevel level = context.level();
        RandomSource random = context.random();
        Set<BlockPos> visitedCrops = new LinkedHashSet<>();
        int processedCrops = 0;
        int samplesPerChunk = Math.max(8, growthAttempts * 4);

        for (LevelChunk chunk : candidateChunks) {
            if (processedCrops >= maxCrops || context.budget().exhausted()) {
                break;
            }

            for (int sample = 0; sample < samplesPerChunk; sample++) {
                if (processedCrops >= maxCrops || context.budget().exhausted()) {
                    break;
                }

                BlockPos cropPos = findCandidateCropPos(chunk, context, random);
                if (cropPos == null || !visitedCrops.add(cropPos.immutable())) {
                    continue;
                }

                if (advanceCrop(level, cropPos, random, growthAttempts, context)) {
                    processedCrops++;
                }
            }
        }

        return new ProcessorResult(ID, processedCrops);
    }

    private static boolean advanceCrop(
        ServerLevel level,
        BlockPos pos,
        RandomSource random,
        int growthAttempts,
        AdvancementContext context
    ) {
        boolean advanced = false;

        for (int attempt = 0; attempt < growthAttempts; attempt++) {
            if (!context.budget().tryConsume()) {
                break;
            }

            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            if (!isSupportedCrop(block) || !state.isRandomlyTicking()) {
                break;
            }

            if (block instanceof CropBlock cropBlock) {
                cropBlock.randomTick(state, level, pos, random);
                advanced = true;
                continue;
            }

            if (block instanceof PitcherCropBlock pitcherCropBlock) {
                pitcherCropBlock.randomTick(state, level, pos, random);
                advanced = true;
                continue;
            }

            break;
        }

        return advanced;
    }

    private static BlockPos findCandidateCropPos(LevelChunk chunk, AdvancementContext context, RandomSource random) {
        int localX = random.nextInt(16);
        int localZ = random.nextInt(16);
        int topY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, localX, localZ) - 1;
        int minY = context.level().getMinBuildHeight();

        for (int offset = 0; offset < 3; offset++) {
            int y = topY - offset;
            if (y < minY) {
                break;
            }

            BlockPos pos = new BlockPos(chunk.getPos().getMinBlockX() + localX, y, chunk.getPos().getMinBlockZ() + localZ);
            BlockState state = context.level().getBlockState(pos);
            Block block = state.getBlock();
            if (!isSupportedCrop(block) || !state.isRandomlyTicking()) {
                continue;
            }

            if (block == Blocks.PITCHER_CROP) {
                BlockPos below = pos.below();
                if (context.level().getBlockState(below).is(Blocks.PITCHER_CROP)) {
                    return below;
                }
            }

            return pos;
        }

        return null;
    }

    private static boolean isSupportedCrop(Block block) {
        return block == Blocks.WHEAT
            || block == Blocks.CARROTS
            || block == Blocks.POTATOES
            || block == Blocks.BEETROOTS
            || block == Blocks.TORCHFLOWER_CROP
            || block == Blocks.PITCHER_CROP;
    }
}
