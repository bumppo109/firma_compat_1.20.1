package com.bumppo109.firma_compat.util.climate.models;

import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.TimeInvariantClimateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import sfiomn.legendarysurvivaloverhaul.common.integration.eclipticseasons.EclipticSeasonsModifier;

public class EclipticLSOClimateModel implements TimeInvariantClimateModel {

    public static EclipticLSOClimateModel INSTANCE = new EclipticLSOClimateModel();

    @Override
    public ClimateModelType type() {
        return ModClimateModels.ECLIPTIC_LSO_BASED.get();
    }

    @Override
    public float getTemperature(LevelReader level, BlockPos pos) {
        return VanillaClimateHelper.getTemperature(level, pos) + getLSOSeasonInfluence((Level) level, pos);
    }

    @Override
    public float getRainfall(LevelReader levelReader, BlockPos pos) {
        return EclipticClimateHelper.getRainfall(levelReader, pos);
    }

    public static float getLSOSeasonInfluence(Level level, BlockPos pos) {
        return new EclipticSeasonsModifier().getUncaughtWorldInfluence(level, pos);
    }
}
