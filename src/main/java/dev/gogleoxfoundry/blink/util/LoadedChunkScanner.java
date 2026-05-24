package dev.gogleoxfoundry.blink.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

public final class LoadedChunkScanner {
    private LoadedChunkScanner() {
    }

    public static List<LevelChunk> collectNearbyChunks(AdvancementContext context, int maxChunks) {
        if (context == null) {
            return List.of();
        }

        int chunkLimit = Math.max(0, maxChunks);
        if (chunkLimit == 0 || context.sleeperPositions().isEmpty()) {
            return List.of();
        }

        ArrayList<LevelChunk> chunks = new ArrayList<>(Math.min(chunkLimit, 64));
        LinkedHashSet<ChunkPos> seen = new LinkedHashSet<>();
        int radius = Math.max(0, context.processingRadius());

        for (BlockPos sleeperPos : context.sleeperPositions()) {
            ChunkPos origin = new ChunkPos(sleeperPos);
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (chunks.size() >= chunkLimit) {
                        return chunks;
                    }

                    ChunkPos chunkPos = new ChunkPos(origin.x + dx, origin.z + dz);
                    if (!seen.add(chunkPos)) {
                        continue;
                    }

                    LevelChunk chunk = context.level().getChunkSource().getChunkNow(chunkPos.x, chunkPos.z);
                    if (chunk != null) {
                        chunks.add(chunk);
                    }
                }
            }
        }

        return chunks;
    }
}
