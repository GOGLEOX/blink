package dev.gogleoxfoundry.nightshift.processor;

import dev.gogleoxfoundry.nightshift.config.NightshiftConfig;
import dev.gogleoxfoundry.nightshift.util.AdvancementContext;
import dev.gogleoxfoundry.nightshift.util.LoadedChunkScanner;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public final class CampfireProcessor implements NightshiftProcessor {
    private static final String ID = "campfire";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public ProcessorResult process(AdvancementContext context) {
        if (!NightshiftConfig.ENABLE_CAMPFIRES.get()) {
            return ProcessorResult.none(ID);
        }

        int maxCampfires = NightshiftConfig.maxCampfireOperations();
        int bonusTicks = NightshiftConfig.campfireBonusTicks();
        if (maxCampfires <= 0 || bonusTicks <= 0 || context.sleeperPositions().isEmpty()) {
            return ProcessorResult.none(ID);
        }

        int processedCampfires = 0;
        List<LevelChunk> candidateChunks = LoadedChunkScanner.collectNearbyChunks(context, maxCampfires);

        for (LevelChunk chunk : candidateChunks) {
            if (processedCampfires >= maxCampfires || context.budget().exhausted()) {
                break;
            }

            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (processedCampfires >= maxCampfires || context.budget().exhausted()) {
                    break;
                }
                if (!(blockEntity instanceof CampfireBlockEntity campfire)) {
                    continue;
                }
                if (isEmpty(campfire)) {
                    continue;
                }

                BlockPos pos = campfire.getBlockPos();
                BlockState state = context.level().getBlockState(pos);
                if (!state.hasProperty(CampfireBlock.LIT) || !state.getValue(CampfireBlock.LIT)) {
                    continue;
                }

                boolean advanced = false;
                for (int tick = 0; tick < bonusTicks; tick++) {
                    if (!context.budget().tryConsume()) {
                        break;
                    }
                    CampfireBlockEntity.cookTick(context.level(), pos, state, campfire);
                    advanced = true;
                }

                if (advanced) {
                    processedCampfires++;
                }
            }
        }

        return new ProcessorResult(ID, processedCampfires);
    }

    private static boolean isEmpty(CampfireBlockEntity campfire) {
        for (ItemStack stack : campfire.getItems()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
