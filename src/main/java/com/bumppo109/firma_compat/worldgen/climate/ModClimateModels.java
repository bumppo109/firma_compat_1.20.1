package com.bumppo109.firma_compat.worldgen.climate;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public final class ModClimateModels {

    public static final Supplier<ClimateModelType> SMOOTHED_BIOME_BASED =
            register("smoothed_biome_based", SmoothedBiomeClimateModel::new);

    public static void registerClimateModels() {
        SMOOTHED_BIOME_BASED.get();
    }

    private static Supplier<ClimateModelType> register(String id, Supplier<ClimateModel> modelSupplier) {
        return Lazy.of(() -> Climate.register(Helpers.identifier(id), modelSupplier));
    }
}