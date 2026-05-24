package dev.gogleoxfoundry.blink.event;

import dev.gogleoxfoundry.blink.config.BlinkConfig;
import dev.gogleoxfoundry.blink.processor.BlinkProcessor;
import dev.gogleoxfoundry.blink.processor.BlinkProcessorRegistry;
import dev.gogleoxfoundry.blink.processor.ProcessorResult;
import dev.gogleoxfoundry.blink.util.AdvancementContext;
import dev.gogleoxfoundry.blink.util.BlinkLog;
import dev.gogleoxfoundry.blink.util.PerformanceBudget;
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
        if (!BlinkConfig.ENABLE_BLINK.get()) {
            return;
        }
        if (BlinkConfig.maxTotalOperationsPerSleep() <= 0) {
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
        boolean debugLogging = BlinkConfig.DEBUG_LOGGING.get();
        AdvancementContext context = new AdvancementContext(
            level,
            sleeperPositions,
            skippedTime,
            RandomSource.create(level.getSeed() ^ event.getNewTime()),
            BlinkConfig.processingRadius(),
            new PerformanceBudget(BlinkConfig.maxTotalOperationsPerSleep())
        );

        for (BlinkProcessor processor : BlinkProcessorRegistry.all()) {
            if (context.budget().exhausted()) {
                break;
            }

            try {
                ProcessorResult result = processor.process(context);
                if (result == null) {
                    BlinkLog.LOGGER.error("Blink processor {} returned null result", processor.id());
                    continue;
                }
                if (debugLogging) {
                    BlinkLog.LOGGER.debug(
                        "Blink processor {} completed with reported={} budgetUsed={} budgetRemaining={}",
                        result.processorId(),
                        result.operationsUsed(),
                        context.budget().operationsUsed(),
                        context.budget().remainingOperations()
                    );
                }
            } catch (Throwable throwable) {
                BlinkLog.LOGGER.error("Blink processor {} failed", processor.id(), throwable);
            }
        }
    }
}
