package com.bumppo109.firma_compat.worldgen.climate;

import com.bumppo109.firma_compat.mixin.BiomeAccessor;   // your package
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.TimeInvariantClimateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;

public class SmoothedBiomeClimateModel implements TimeInvariantClimateModel {

    public static final SmoothedBiomeClimateModel INSTANCE = new SmoothedBiomeClimateModel();

    @Override
    public ClimateModelType type() {
        return ModClimateModels.SMOOTHED_BIOME_BASED.get();
    }

    @Override
    public float getTemperature(LevelReader level, BlockPos pos) {
        float sum = 0.0F;
        int count = 0;

        // 5x5 sampling grid every 8 blocks for excellent smoothing
        for (int dx = -16; dx <= 16; dx += 8) {
            for (int dz = -16; dz <= 16; dz += 8) {
                BlockPos sample = pos.offset(dx, 0, dz);
                Biome biome = level.getBiome(sample).value();

                // Simple and reliable way:
                float baseTemp = biome.getBaseTemperature();

                // Re-apply height adjustment (the most important part of getTemperature)
                float adjusted = getHeightAdjustedTemperature(biome, sample, baseTemp);

                sum += adjusted;
                count++;
            }
        }

        float averaged = sum / count;

        return Climate.toActualTemperature(averaged);
    }

    /**
     * Replicates the private getHeightAdjustedTemperature logic from Biome class
     */
    private float getHeightAdjustedTemperature(Biome biome, BlockPos pos, float baseTemp) {
        // Apply temperature modifier (NONE or FROZEN)
        float f = biome.getModifiedClimateSettings() != null
                ? biome.getModifiedClimateSettings().temperatureModifier().modifyTemperature(pos, baseTemp)
                : baseTemp;   // fallback

        if (pos.getY() > 80) {
            // Use the static TEMPERATURE_NOISE (it's package-private but we can access via reflection or skip small noise)
            // For simplicity we approximate without the exact noise for high altitudes
            float heightPenalty = (pos.getY() - 80.0F) * 0.00125F; // rough equivalent of the original formula
            return f - heightPenalty;
        }
        return f;
    }

    @Override
    public float getRainfall(LevelReader level, BlockPos pos) {
        return level.getBiome(pos).value().getPrecipitationAt(pos) != Precipitation.NONE
                ? 300.0F
                : 0.0F;
    }

    @Override
    public float getAverageTemperature(LevelReader level, BlockPos pos) {
        return getTemperature(level, pos);
    }
}