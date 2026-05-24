package dev.gogleoxfoundry.nightshift;

import dev.gogleoxfoundry.nightshift.config.NightshiftConfig;
import dev.gogleoxfoundry.nightshift.event.SleepAdvanceHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Nightshift.MOD_ID)
public final class Nightshift {
    public static final String MOD_ID = "nightshift";

    public Nightshift() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NightshiftConfig.SPEC);
        MinecraftForge.EVENT_BUS.register(SleepAdvanceHandler.class);
    }
}
