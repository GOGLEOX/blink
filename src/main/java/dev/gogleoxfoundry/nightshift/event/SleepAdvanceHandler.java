package dev.gogleoxfoundry.nightshift.event;

import dev.gogleoxfoundry.nightshift.config.NightshiftConfig;
import dev.gogleoxfoundry.nightshift.processor.NightshiftProcessor;
import dev.gogleoxfoundry.nightshift.processor.NightshiftProcessorRegistry;
import dev.gogleoxfoundry.nightshift.processor.ProcessorResult;
import dev.gogleoxfoundry.nightshift.util.AdvancementContext;
import dev.gogleoxfoundry.nightshift.util.NightshiftLog;
import dev.gogleoxfoundry.nightshift.util.PerformanceBudget;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class SleepAdvanceHandler {
    private SleepAdvanceHandler() {
    }

    @SubscribeEvent
    public static void onSleepFinished(SleepFinishedTimeEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (!NightshiftConfig.ENABLE_NIGHTSHIFT.get()) {
            return;
        }
        if (NightshiftConfig.maxTotalOperationsPerSleep() <= 0) {
            return;
        }

        ArrayList<BlockPos> sleeperPositions = new ArrayList<>();
        for (ServerPlayer player : level.players()) {
            if (player != null && player.isSleepingLongEnough()) {
                sleeperPositions.add(player.blockPosition());
            }
        }
        if (sleeperPositions.isEmpty()) {
            return;
        }

        long skippedTime = Math.max(0L, event.getNewTime() - level.getDayTime());
        boolean debugLogging = NightshiftConfig.DEBUG_LOGGING.get();
        AdvancementContext context = new AdvancementContext(
            level,
            sleeperPositions,
            skippedTime,
            RandomSource.create(level.getSeed() ^ event.getNewTime()),
            NightshiftConfig.processingRadius(),
            new PerformanceBudget(NightshiftConfig.maxTotalOperationsPerSleep())
        );

        for (NightshiftProcessor processor : NightshiftProcessorRegistry.all()) {
            if (context.budget().exhausted()) {
                break;
            }

            try {
                ProcessorResult result = processor.process(context);
                if (result == null) {
                    NightshiftLog.LOGGER.error("Nightshift processor {} returned null result", processor.id());
                    continue;
                }
                if (debugLogging) {
                    NightshiftLog.LOGGER.debug(
                        "Nightshift processor {} completed with reported={} budgetUsed={} budgetRemaining={}",
                        result.processorId(),
                        result.operationsUsed(),
                        context.budget().operationsUsed(),
                        context.budget().remainingOperations()
                    );
                }
            } catch (Throwable throwable) {
                NightshiftLog.LOGGER.error("Nightshift processor {} failed", processor.id(), throwable);
            }
        }
    }
}
