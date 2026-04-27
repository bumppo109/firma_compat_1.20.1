package com.bumppo109.firma_compat.worldgen.climate;

import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.TimeInvariantClimateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;

public class SmoothedBiomeClimateModel implements TimeInvariantClimateModel {

    public static final SmoothedBiomeClimateModel INSTANCE = new SmoothedBiomeClimateModel();

    @Override
    public ClimateModelType type() {
        return ModClimateModels.SMOOTHED_BIOME_BASED.get();
    }

    /*
    @Override
    public float getTemperature(LevelReader level, BlockPos pos) {
        float sum = 0.0F;
        int count = 0;

        // Smoothing grid (feel free to tweak radius if rivers are still too sharp)
        for (int dx = -16; dx <= 16; dx += 8) {
            for (int dz = -16; dz <= 16; dz += 8) {
                BlockPos sample = pos.offset(dx, 0, dz);
                Biome biome = level.getBiome(sample).value();

                float baseTemp = biome.getBaseTemperature();
                float adjusted = getHeightAdjustedTemperature(biome, sample, baseTemp);

                sum += adjusted;
                count++;
            }
        }

        float averagedVanilla = sum / count;

        // Gentle normalization — keeps values in safe range for TFC's formula
        float normalizedVanilla = normalizeVanillaTemperature(averagedVanilla);

        return Climate.toActualTemperature(normalizedVanilla);
    }

     */
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
     * Maps vanilla biome temps (~ -0.6 to 2.0) into a better spread for Koppen classification
     * while staying compatible with TFC's (x - 0.15)/0.0217 conversion.
     */
    /*
    private float normalizeVanillaTemperature(float vanilla) {
        float clamped = Mth.clamp(vanilla, -1.0F, 2.8F);   // safe clamp

        // Recommended starting values — tweak these two numbers
        return clamped * 1.25F - 0.22F;
    }

     */
    private float normalizeVanillaTemperature(float vanilla) {
        float clamped = Mth.clamp(vanilla, -1.0F, 2.8F);

        // Very gentle tanh
        double x = (clamped - 0.4) * 1.1;           // small shift + scale
        float tanhValue = (float) Math.tanh(x) * 1.35F;   // tiny multiplier

        return tanhValue - 0.05F;
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

    public float getRainfall(LevelReader level, BlockPos pos) {
        return ((Biome)level.getBiome(pos).value()).getPrecipitationAt(pos) != Precipitation.NONE ? 300.0F : 0.0F;
    }

    @Override
    public float getAverageTemperature(LevelReader level, BlockPos pos) {
        return getTemperature(level, pos);
    }
}