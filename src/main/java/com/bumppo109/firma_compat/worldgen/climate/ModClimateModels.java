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

    public static final Supplier<ClimateModelType> LSO_BASED =
            register("lso_based", LSOClimateModel::new);

    public static final Supplier<ClimateModelType> ECLIPTIC_LSO_BASED =
            register("ecliptic_lso_based", EclipticLSOClimateModel::new);

    public static void registerEclipticModel() {
        ECLIPTIC_BASED.get();
    }
    public static void registerLsoModel() {
        LSO_BASED.get();
    }
    public static void registerEclipticLsoModel() {
        ECLIPTIC_LSO_BASED.get();
    }

    private static Supplier<ClimateModelType> register(String id, Supplier<ClimateModel> modelSupplier) {
        return Lazy.of(() -> Climate.register(Helpers.identifier(id), modelSupplier));
    }
}