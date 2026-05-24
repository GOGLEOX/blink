package dev.gogleoxfoundry.blink.processor;

import dev.gogleoxfoundry.blink.config.BlinkConfig;
import dev.gogleoxfoundry.blink.util.AdvancementContext;
import dev.gogleoxfoundry.blink.util.LoadedChunkScanner;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;

public final class FurnaceProcessor implements BlinkProcessor {
    private static final String ID = "furnace";

    @Override
    public String id() {
        return ID;
    }

    @Override
    public ProcessorResult process(AdvancementContext context) {
        if (!BlinkConfig.ENABLE_FURNACES.get()) {
            return ProcessorResult.none(ID);
        }

        int maxFurnaces = BlinkConfig.maxFurnaceOperations();
        int bonusTicks = BlinkConfig.furnaceBonusTicks();
        if (maxFurnaces <= 0 || bonusTicks <= 0 || context.sleeperPositions().isEmpty()) {
            return ProcessorResult.none(ID);
        }

        int processedFurnaces = 0;
        List<LevelChunk> candidateChunks = LoadedChunkScanner.collectNearbyChunks(context, maxFurnaces);

        for (LevelChunk chunk : candidateChunks) {
            if (processedFurnaces >= maxFurnaces || context.budget().exhausted()) {
                break;
            }

            for (BlockEntity blockEntity : chunk.getBlockEntities().values()) {
                if (processedFurnaces >= maxFurnaces || context.budget().exhausted()) {
                    break;
                }
                if (!(blockEntity instanceof AbstractFurnaceBlockEntity furnace)) {
                    continue;
                }

                BlockPos pos = furnace.getBlockPos();
                BlockState state = context.level().getBlockState(pos);

                boolean advanced = false;
                for (int tick = 0; tick < bonusTicks; tick++) {
                    if (!context.budget().tryConsume()) {
                        break;
                    }
                    AbstractFurnaceBlockEntity.serverTick(context.level(), pos, state, furnace);
                    advanced = true;
                }

                if (advanced) {
                    processedFurnaces++;
                }
            }
        }

        return new ProcessorResult(ID, processedFurnaces);
    }
}
