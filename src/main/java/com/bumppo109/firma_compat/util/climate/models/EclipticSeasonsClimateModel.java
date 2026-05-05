package com.bumppo109.firma_compat.util.climate.models;

import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.TimeInvariantClimateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public class EclipticSeasonsClimateModel implements TimeInvariantClimateModel {

    public static final EclipticSeasonsClimateModel INSTANCE = new EclipticSeasonsClimateModel();

    @Override
    public ClimateModelType type() {
        return ModClimateModels.ECLIPTIC_BASED.get();
    }

    @Override
    public float getTemperature(LevelReader levelReader, BlockPos pos) {
        return EclipticClimateHelper.getTemperature(levelReader, pos);
    }

    @Override
    public float getRainfall(LevelReader levelReader, BlockPos pos) {
        return EclipticClimateHelper.getRainfall(levelReader, pos);
    }
}