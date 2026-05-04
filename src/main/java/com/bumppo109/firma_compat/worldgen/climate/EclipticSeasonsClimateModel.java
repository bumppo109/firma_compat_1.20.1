package com.bumppo109.firma_compat.worldgen.climate;

import com.bumppo109.firma_compat.FirmaCompat;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.TimeInvariantClimateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;

public class EclipticSeasonsClimateModel implements TimeInvariantClimateModel {

    public static final EclipticSeasonsClimateModel INSTANCE = new EclipticSeasonsClimateModel();

    @Override
    public ClimateModelType type() {
        return ModClimateModels.ECLIPTIC_BASED.get();
    }

    @Override
    public float getTemperature(LevelReader levelReader, BlockPos pos) {
        Biome biome = levelReader.getBiome(pos).value();
        if (!(levelReader instanceof Level level)) {
            // Worldgen fallback
            return Climate.toActualTemperature(biome.getBaseTemperature());
        }

        return EclipticUtil.getTemperatureFloat(level, biome, pos);
    }

    @Override
    public float getRainfall(LevelReader levelReader, BlockPos pos) {
        if (!(levelReader instanceof Level level)) {
            // Worldgen fallback
            Biome biome = levelReader.getBiome(pos).value();
            return biome.getPrecipitationAt(pos) != Biome.Precipitation.NONE ? 250f : 80f;
        }

        try {
            Biome biome = level.getBiome(pos).value();
            SolarTerm solarTerm = EclipticUtil.getNowSolarTerm(level);
            boolean serverSide = !level.isClientSide();

            float downfall = EclipticUtil.getDownfallFloat(level, solarTerm, biome, pos, serverSide);

            // Scale Ecliptic downfall (usually 0-2 range) to TFC 0-500
            float rainfall = downfall * 320.0f;   // tweak this multiplier if needed

            return Mth.clamp(rainfall, 0.0f, 500.0f);
        } catch (Exception e) {
            FirmaCompat.LOGGER.warn("Failed to get rainfall at {}: {}", pos, e.getMessage());
            return 180.0f;
        }
    }
}