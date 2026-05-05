package com.bumppo109.firma_compat.util.climate.models;

import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.TimeInvariantClimateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

public class VanillaClimateModel implements TimeInvariantClimateModel {

    public static final VanillaClimateModel INSTANCE = new VanillaClimateModel();

    @Override
    public ClimateModelType type() {
        return ModClimateModels.VANILLA_BASED.get();
    }

    @Override
    public float getTemperature(LevelReader level, BlockPos pos) {
        return VanillaClimateHelper.getTemperature(level, pos);
    }

    @Override
    public float getRainfall(LevelReader level, BlockPos pos) {
        return VanillaClimateHelper.getRainfall(level, pos);
    }
}