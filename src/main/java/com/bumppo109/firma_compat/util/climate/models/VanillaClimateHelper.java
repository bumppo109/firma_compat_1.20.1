package com.bumppo109.firma_compat.util.climate.models;

import com.bumppo109.firma_compat.util.chunkData.ClientClimateCache;
import com.bumppo109.firma_compat.util.chunkData.ClimateData;
import com.bumppo109.firma_compat.util.chunkData.ServerClimateCache;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;

public class VanillaClimateHelper {

    public static float tempScale = 27.5f;

    public static float getTemperature(LevelReader level, BlockPos pos) {
        ClimateData data = getClimateData(level, pos);

        if (data == null) return 0f;

        return data.temperature * tempScale;
    }

    public static float getRainfall(LevelReader level, BlockPos pos) {
        ClimateData data = getClimateData(level, pos);

        if (data == null) return 0f;

        return (data.vegetation + 1f) * 250f;
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

        return Float.NaN;
    }
}