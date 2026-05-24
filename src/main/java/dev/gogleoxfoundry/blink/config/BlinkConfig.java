package dev.gogleoxfoundry.blink.config;

import dev.gogleoxfoundry.blink.Blink;
import dev.gogleoxfoundry.blink.util.BlinkLog;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Blink.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class BlinkConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_BLINK;
    public static final ForgeConfigSpec.IntValue PROCESSING_RADIUS;
    public static final ForgeConfigSpec.IntValue MAX_TOTAL_OPERATIONS_PER_SLEEP;
    public static final ForgeConfigSpec.BooleanValue ENABLE_FURNACES;
    public static final ForgeConfigSpec.IntValue MAX_FURNACE_OPERATIONS;
    public static final ForgeConfigSpec.IntValue FURNACE_BONUS_TICKS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_CAMPFIRES;
    public static final ForgeConfigSpec.IntValue MAX_CAMPFIRE_OPERATIONS;
    public static final ForgeConfigSpec.IntValue CAMPFIRE_BONUS_TICKS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_CROPS;
    public static final ForgeConfigSpec.IntValue MAX_CROP_OPERATIONS;
    public static final ForgeConfigSpec.IntValue CROP_GROWTH_ATTEMPTS;
    public static final ForgeConfigSpec.BooleanValue DEBUG_LOGGING;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        ENABLE_BLINK = builder
            .comment("Enables Blink sleep processing.")
            .define("enableBlink", true);
        PROCESSING_RADIUS = builder
            .comment("Chunk radius around sleepers to consider.")
            .defineInRange("processingRadius", 2, 0, 8);
        MAX_TOTAL_OPERATIONS_PER_SLEEP = builder
            .comment("Hard cap on all Blink operations per sleep.")
            .defineInRange("maxTotalOperationsPerSleep", 128, 0, 4096);
        ENABLE_FURNACES = builder
            .comment("Allows furnace bonus progress.")
            .define("enableFurnaces", true);
        MAX_FURNACE_OPERATIONS = builder
            .comment("Max furnaces processed per sleep.")
            .defineInRange("maxFurnaceOperations", 16, 0, 512);
        FURNACE_BONUS_TICKS = builder
            .comment("Bonus burn or cook ticks per furnace.")
            .defineInRange("furnaceBonusTicks", 200, 0, 1200);
        ENABLE_CAMPFIRES = builder
            .comment("Allows campfire bonus progress.")
            .define("enableCampfires", true);
        MAX_CAMPFIRE_OPERATIONS = builder
            .comment("Max campfires processed per sleep.")
            .defineInRange("maxCampfireOperations", 16, 0, 512);
        CAMPFIRE_BONUS_TICKS = builder
            .comment("Bonus cook ticks per campfire.")
            .defineInRange("campfireBonusTicks", 40, 0, 1200);
        ENABLE_CROPS = builder
            .comment("Allows limited crop growth attempts.")
            .define("enableCrops", true);
        MAX_CROP_OPERATIONS = builder
            .comment("Max crop targets processed per sleep.")
            .defineInRange("maxCropOperations", 32, 0, 1024);
        CROP_GROWTH_ATTEMPTS = builder
            .comment("Growth attempts per selected crop.")
            .defineInRange("cropGrowthAttempts", 1, 0, 16);
        DEBUG_LOGGING = builder
            .comment("Enables Blink debug logging.")
            .define("debugLogging", false);

        SPEC = builder.build();
    }

    private BlinkConfig() {
    }

    public static int processingRadius() {
        return Math.max(0, PROCESSING_RADIUS.get());
    }

    public static int maxTotalOperationsPerSleep() {
        return Math.max(0, MAX_TOTAL_OPERATIONS_PER_SLEEP.get());
    }

    public static int maxFurnaceOperations() {
        return Math.max(0, MAX_FURNACE_OPERATIONS.get());
    }

    public static int furnaceBonusTicks() {
        return Math.max(0, FURNACE_BONUS_TICKS.get());
    }

    public static int maxCampfireOperations() {
        return Math.max(0, MAX_CAMPFIRE_OPERATIONS.get());
    }

    public static int campfireBonusTicks() {
        return Math.max(0, CAMPFIRE_BONUS_TICKS.get());
    }

    public static int maxCropOperations() {
        return Math.max(0, MAX_CROP_OPERATIONS.get());
    }

    public static int cropGrowthAttempts() {
        return Math.max(0, CROP_GROWTH_ATTEMPTS.get());
    }

    @SubscribeEvent
    public static void onConfigLoading(ModConfigEvent.Loading event) {
        handleConfigEvent(event.getConfig(), "loaded");
    }

    @SubscribeEvent
    public static void onConfigReloading(ModConfigEvent.Reloading event) {
        handleConfigEvent(event.getConfig(), "reloaded");
    }

    private static void handleConfigEvent(ModConfig config, String phase) {
        if (config.getSpec() != SPEC) {
            return;
        }

        // Read all live values through accessors so any clamped runtime use stays aligned.
        processingRadius();
        maxTotalOperationsPerSleep();
        maxFurnaceOperations();
        furnaceBonusTicks();
        maxCampfireOperations();
        campfireBonusTicks();
        maxCropOperations();
        cropGrowthAttempts();

        if (DEBUG_LOGGING.get()) {
            BlinkLog.LOGGER.debug(
                "Blink config {}: enableBlink={}, radius={}, maxOps={}, furnaces={}({}/{}), campfires={}({}/{}), crops={}({}/{})",
                phase,
                ENABLE_BLINK.get(),
                processingRadius(),
                maxTotalOperationsPerSleep(),
                ENABLE_FURNACES.get(),
                maxFurnaceOperations(),
                furnaceBonusTicks(),
                ENABLE_CAMPFIRES.get(),
                maxCampfireOperations(),
                campfireBonusTicks(),
                ENABLE_CROPS.get(),
                maxCropOperations(),
                cropGrowthAttempts()
            );
        }
    }
}
