package com.bumppo109.firma_compat.worldgen;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, FirmaCompat.MODID);

    // Correct registration - pass the codec from the config
    public static final RegistryObject<Feature<ErosionFeatureConfig>> EROSION =
            FEATURES.register("erosion", () -> new ErosionFeature(ErosionFeatureConfig.CODEC));

    public static void register(IEventBus modEventBus) {
        FEATURES.register(modEventBus);
    }
}