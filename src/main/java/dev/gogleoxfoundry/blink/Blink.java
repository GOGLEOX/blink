package dev.gogleoxfoundry.blink;

import dev.gogleoxfoundry.blink.config.BlinkConfig;
import dev.gogleoxfoundry.blink.event.SleepAdvanceHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Blink.MOD_ID)
public final class Blink {
    public static final String MOD_ID = "blink";

    public Blink() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BlinkConfig.SPEC);
        MinecraftForge.EVENT_BUS.register(SleepAdvanceHandler.class);
    }
}
