package com.bumppo109.firma_compat.worldgen.climate;

import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public final class ModClimateModels {

    public static final Supplier<ClimateModelType> ECLIPTIC_BASED =
            register("ecliptic_based", EclipticSeasonsClimateModel::new);

    public static void registerClimateModels() {
        ECLIPTIC_BASED.get();
    }

    private static Supplier<ClimateModelType> register(String id, Supplier<ClimateModel> modelSupplier) {
        return Lazy.of(() -> Climate.register(Helpers.identifier(id), modelSupplier));
    }
}