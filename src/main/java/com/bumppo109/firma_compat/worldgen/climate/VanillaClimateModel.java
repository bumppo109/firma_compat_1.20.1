package com.bumppo109.firma_compat.worldgen.climate;

import com.bumppo109.firma_compat.FirmaCompat;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.TimeInvariantClimateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;

public class VanillaClimateModel implements TimeInvariantClimateModel {

    public static final VanillaClimateModel INSTANCE = new VanillaClimateModel();

    @Override
    public ClimateModelType type() {
        return ModClimateModels.VANILLA_BASED.get();
    }

    @Override
    public float getTemperature(LevelReader level, BlockPos pos) {
        float rawTemp = getRawTemperature(level, pos);

        if (rawTemp != Float.NaN) {
            float celsius = rawTemp * 27.5f;

            // Log only on server to reduce spam
            if (!level.isClientSide() && level instanceof ServerLevel) {
                FirmaCompat.LOGGER.info("Climate Temperature from Temperature: {}, set to: {}", rawTemp, celsius);
            }
            return celsius;
        }

        if (!level.isClientSide()) {
            FirmaCompat.LOGGER.info("Climate Temperature from Temperature: NOT FOUND, set to: 0");
        }
        return 0.0f;
    }

    @Override
    public float getRainfall(LevelReader level, BlockPos pos) {
        float rawVeg = getRawVegetation(level, pos);

        if (rawVeg != Float.NaN) {
            float rainfall = (rawVeg + 1.0f) * 250.0f;

            if (!level.isClientSide() && level instanceof ServerLevel) {
                FirmaCompat.LOGGER.info("Climate Rainfall from Vegetation: {}, set to: {}", rawVeg, rainfall);
            }
            return rainfall;
        }

        if (!level.isClientSide()) {
            FirmaCompat.LOGGER.info("Climate Rainfall from Vegetation: NOT FOUND, set to: 0");
        }
        return 0.0f;
    }

    // ==================== INTERNAL HELPERS ====================

    private float getRawTemperature(LevelReader level, BlockPos pos) {
        return sampleNoise(level, pos, true);   // true = temperature
    }

    private float getRawVegetation(LevelReader level, BlockPos pos) {
        return sampleNoise(level, pos, false);  // false = vegetation
    }

    private float sampleNoise(LevelReader level, BlockPos pos, boolean isTemperature) {
        if (level instanceof ServerLevel serverLevel) {
            ChunkGenerator generator = serverLevel.getChunkSource().getGenerator();

            if (generator instanceof NoiseBasedChunkGenerator) {
                try {
                    RandomState randomState = serverLevel.getChunkSource().randomState();
                    if (randomState != null) {
                        var router = randomState.router();
                        var context = new DensityFunction.SinglePointContext(
                                pos.getX(), pos.getY(), pos.getZ()
                        );

                        double value = isTemperature
                                ? router.temperature().compute(context)
                                : router.vegetation().compute(context);

                        return (float) value;
                    }
                } catch (Exception ignored) {}
            }
        }
        return Float.NaN; // Signal "not found"
    }
}