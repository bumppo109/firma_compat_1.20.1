package com.bumppo109.firma_compat.util.chunkData;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.DensityFunction;

public class VanillaNoiseSampler {

    private static final int SAMPLE_Y = 64;

    public static ClimateData sample(ServerLevel level, int x, int z) {
        ChunkGenerator generator = level.getChunkSource().getGenerator();

        if (!(generator instanceof NoiseBasedChunkGenerator)) {
            return null;
        }

        var randomState = level.getChunkSource().randomState();
        var router = randomState.router();

        var context = new DensityFunction.SinglePointContext(x, SAMPLE_Y, z);

        float temperature = (float) router.temperature().compute(context);
        float vegetation = (float) router.vegetation().compute(context);
        float continentalness = (float) router.continents().compute(context);
        float erosion = (float) router.erosion().compute(context);
        float weirdness = (float) router.ridges().compute(context); // "weirdness"

        return new ClimateData(temperature, vegetation, continentalness, erosion, weirdness);
    }
}