package com.bumppo109.firma_compat.util.climate.models;

import com.bumppo109.firma_compat.config.FirmaCompatConfig;
import com.bumppo109.firma_compat.util.chunkData.ClientClimateCache;
import com.bumppo109.firma_compat.util.chunkData.ClimateData;
import com.bumppo109.firma_compat.util.chunkData.ServerClimateCache;
import net.dries007.tfc.util.climate.Climate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;

import static org.joml.Math.lerp;

public class VanillaClimateHelper {

    public static double tempScale = FirmaCompatConfig.COMMON.tempScale.get();
    public static double tempShift = FirmaCompatConfig.COMMON.tempShift.get();
    public static boolean doTempLerp = FirmaCompatConfig.COMMON.doTempLerp.get();

    public static double rainScale = FirmaCompatConfig.COMMON.rainScale.get();
    public static double tempLerpValue = FirmaCompatConfig.COMMON.tempLerpValue.get();

    public static float maxElevationChange = 17.822f;
    public static float elevationModifier = 0.16225F;

    public static float getTemperature(LevelReader level, BlockPos pos) {
        ClimateData data = getClimateData(level, pos);
        Holder<Biome> biomeHolder = level.getBiome(pos);
        float biomeTemp = biomeHolder.value().getBaseTemperature();
        float biomeScaled = biomeTemp * (float) tempScale;
        float noiseTemp;
        float adjTemp;

        if (data == null){
            noiseTemp = 0f;
        } else {
            noiseTemp = (data.temperature * (float) tempScale) + (float) tempShift;
        }

        if(doTempLerp){
            adjTemp = lerp(noiseTemp, biomeScaled, (float) tempLerpValue);
        } else {
            adjTemp = noiseTemp;
        }

        return getAdjustedAverageTempByElevation(pos.getY(), adjTemp);
    }

    public static float getRainfall(LevelReader level, BlockPos pos) {
        ClimateData data = getClimateData(level, pos);

        if (data == null) return 0f;

        return (data.vegetation + 1f) * (float) rainScale;
    }

    public static float getAdjustedAverageTempByElevation(int y, float averageTemperature) {
        if ((float)y > 63.0F) {
            float elevationTemperature = Mth.clamp(((float)y - 63.0F) * elevationModifier, 0.0F, maxElevationChange);
            return averageTemperature - elevationTemperature;
        } else {
            return averageTemperature;
        }
    }

    public static float getTemperatureWorldgen(LevelReader level, BlockPos pos) {
        float raw = getRawTemperature(level, pos);
        if (Float.isNaN(raw)) return 0f;

        return (raw * (float) tempScale) + (float) tempShift;
    }

    public static float getRainfallWorldgen(LevelReader level, BlockPos pos) {
        float raw = getRawVegetation(level, pos);
        if (Float.isNaN(raw)) return 0f;

        return (raw + 1f) * (float) rainScale;
    }

    public static ClimateData getClimateData(LevelReader level, BlockPos pos) {
        ChunkPos chunkPos = new ChunkPos(pos);

        ClimateData data;

        if (level.isClientSide()) {
            data = ClientClimateCache.get(chunkPos);
        } else if (level instanceof ServerLevel serverLevel) {
            data = ServerClimateCache.get(serverLevel, chunkPos);
        } else {
            return null;
        }

        if (data != null) return data;

        // ===== Neighbor fallback =====
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {

                ChunkPos nearbyPos = new ChunkPos(chunkPos.x + dx, chunkPos.z + dz);

                ClimateData nearby;

                if (level.isClientSide()) {
                    nearby = ClientClimateCache.get(nearbyPos);
                } else if (level instanceof ServerLevel serverLevel) {
                    nearby = ServerClimateCache.get(serverLevel, nearbyPos);
                } else {
                    nearby = null;
                }

                if (nearby != null) {
                    return nearby;
                }
            }
        }

        return null;
    }

    // ==================== RAW NOISE (OPTIONAL USE) ====================

    public static float getRawTemperature(LevelReader level, BlockPos pos) {
        return sampleNoise(level, pos, true);
    }

    public static float getRawVegetation(LevelReader level, BlockPos pos) {
        return sampleNoise(level, pos, false);
    }

    private static float sampleNoise(LevelReader level, BlockPos pos, boolean isTemperature) {

        ServerLevel serverLevel = null;

        if (level instanceof ServerLevel sl) {
            serverLevel = sl;
        } else if (level instanceof WorldGenLevel wgl) {
            serverLevel = wgl.getLevel();
        }

        if (serverLevel != null) {

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
                } catch (Exception ignored) {
                }
            }
        }

        return Float.NaN;
    }
}