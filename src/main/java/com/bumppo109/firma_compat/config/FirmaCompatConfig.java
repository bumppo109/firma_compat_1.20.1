package com.bumppo109.firma_compat.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class FirmaCompatConfig {

    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final Common COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        COMMON = new Common(builder);
        COMMON_CONFIG = builder.build();
    }

    public static class Common {
        public final ForgeConfigSpec.IntValue sheepWoolRegrowthDelayTicks;
        public final ForgeConfigSpec.IntValue cowMilkingDelayTicks;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("animals");

            sheepWoolRegrowthDelayTicks = builder
                    .comment("Ticks required after shearing before a sheep can regrow its wool.",
                            "20 ticks = 1 second. Default: 6000 ticks = 5 minutes.",
                            "Set to 0 to disable delay (vanilla behavior).")
                    .defineInRange("sheepWoolRegrowthDelayTicks", 6000, 0, Integer.MAX_VALUE);


            cowMilkingDelayTicks = builder
                    .comment("Ticks required after milking a cow before it can be milked again.",
                            "20 ticks = 1 second. Default: 1200 ticks = 1 minute.",
                            "Set to 0 to disable (vanilla behavior).")
                    .defineInRange("cowMilkingDelayTicks", 1200, 0, Integer.MAX_VALUE);

            builder.pop();
        }
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG, "firma_compat-common.toml");
    }
}
