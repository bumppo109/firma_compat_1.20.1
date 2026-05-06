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

        public final ForgeConfigSpec.DoubleValue tempLerpValue;
        public final ForgeConfigSpec.BooleanValue doTempLerp;
        public final ForgeConfigSpec.DoubleValue tempScale;
        public final ForgeConfigSpec.DoubleValue tempShift;
        public final ForgeConfigSpec.DoubleValue rainScale;

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

            builder.push("climate");

            tempLerpValue = builder
                    .comment("Lerp value to align biome temperatures more closely to their JSON temperature value: [0 - 1]",
                            "0 to disable and only return the temperature noise value.",
                            "Default: 0.4")
                    .defineInRange("tempLerpValue", 0.4, 0, 1);

            doTempLerp = builder
                    .comment("Whether or not to use the Lerp function to adjust climate temperature towards biome temperature value.",
                            "Default: true")
                    .define("doTempLerp", true);

            tempScale = builder
                    .comment("Value to scale temperature noise to climate temperature value",
                            "Default: 27.5")
                    .defineInRange("tempScale", 27.5, Double.MIN_VALUE, Double.MAX_VALUE);

            tempShift = builder
                    .comment("Value to shift temperature scale.",
                            "Default: 7.5")
                    .defineInRange("tempShift", 7.5, Double.MIN_VALUE, Double.MAX_VALUE);

            rainScale = builder
                    .comment("Value to scale vegetation noise to climate rain value",
                            "Default: 250")
                    .defineInRange("rainScale", 250.0, Double.MIN_VALUE, Double.MAX_VALUE);

            builder.pop();
        }
    }

    public static void register() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG, "firma_compat-common.toml");
    }
}
