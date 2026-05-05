package com.bumppo109.firma_compat.util.climate.models;

import com.bumppo109.firma_compat.FirmaCompat;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;

public class EclipticClimateHelper {

    // Scale Ecliptic downfall (usually 0-2 range) to TFC 0-500
    public static float rainScale = 320.0f;

    public static float getTemperature(LevelReader levelReader, BlockPos pos) {
        if (!(levelReader instanceof Level level)) {
            return VanillaClimateHelper.getTemperature(levelReader, pos);
        }

        SolarTerm solarTerm = EclipticSeasonsApi.getInstance().getSolarTerm(level);

        float baseTemp = VanillaClimateHelper.getTemperature(level, pos);
        float seasonalOffset = solarTerm.getTemperatureChange() * VanillaClimateHelper.tempScale;

        return baseTemp + seasonalOffset;
    }

    public static float getRainfall(LevelReader levelReader, BlockPos pos) {
        if (!(levelReader instanceof Level level)) {
            return VanillaClimateHelper.getRainfall(levelReader, pos);
        }

        try {
            Biome biome = level.getBiome(pos).value();
            SolarTerm solarTerm = EclipticUtil.getNowSolarTerm(level);
            boolean serverSide = !level.isClientSide();

            float downfall = EclipticUtil.getDownfallFloat(level, solarTerm, biome, pos, serverSide);

            float rainfall = downfall * rainScale;

            return Mth.clamp(rainfall, 0.0f, 500.0f);
        } catch (Exception e) {
            FirmaCompat.LOGGER.warn("Failed to get rainfall at {}: {}", pos, e.getMessage());
            return ((Biome)level.getBiome(pos).value()).getPrecipitationAt(pos) != Biome.Precipitation.NONE ? 300.0F : 0.0F;
        }
    }
}
