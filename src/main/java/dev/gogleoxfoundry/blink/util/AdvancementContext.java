package dev.gogleoxfoundry.blink.util;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

public record AdvancementContext(
    ServerLevel level,
    List<BlockPos> sleeperPositions,
    long skippedTime,
    RandomSource random,
    int processingRadius,
    PerformanceBudget budget
) {
    public AdvancementContext {
        if (level == null) {
            throw new IllegalArgumentException("level must not be null");
        }
        if (sleeperPositions == null) {
            throw new IllegalArgumentException("sleeperPositions must not be null");
        }
        if (random == null) {
            throw new IllegalArgumentException("random must not be null");
        }
        if (budget == null) {
            throw new IllegalArgumentException("budget must not be null");
        }

        sleeperPositions = List.copyOf(sleeperPositions);
        skippedTime = Math.max(0L, skippedTime);
        processingRadius = Math.max(0, processingRadius);
    }
}
