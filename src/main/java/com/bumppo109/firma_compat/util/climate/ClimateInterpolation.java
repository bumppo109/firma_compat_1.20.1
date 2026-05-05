package com.bumppo109.firma_compat.util.climate;

import com.bumppo109.firma_compat.util.chunkData.ClientClimateCache;
import com.bumppo109.firma_compat.util.chunkData.ClimateData;
import net.minecraft.world.level.ChunkPos;

public class ClimateInterpolation {

    public static ClimateData sampleInterpolated(int x, int z) {

        int cx = x >> 4;
        int cz = z >> 4;

        float fx = (x & 15) / 15f;
        float fz = (z & 15) / 15f;

        ClimateData c00 = ClientClimateCache.get(new ChunkPos(cx, cz));
        ClimateData c10 = ClientClimateCache.get(new ChunkPos(cx + 1, cz));
        ClimateData c01 = ClientClimateCache.get(new ChunkPos(cx, cz + 1));
        ClimateData c11 = ClientClimateCache.get(new ChunkPos(cx + 1, cz + 1));

        if (c00 == null) return null;

        // fallback if neighbors missing
        c10 = (c10 != null) ? c10 : c00;
        c01 = (c01 != null) ? c01 : c00;
        c11 = (c11 != null) ? c11 : c00;

        return lerpAll(c00, c10, c01, c11, fx, fz);
    }

    private static ClimateData lerpAll(
            ClimateData c00, ClimateData c10,
            ClimateData c01, ClimateData c11,
            float x, float z
    ) {

        float t0 = lerp(c00.temperature, c10.temperature, x);
        float t1 = lerp(c01.temperature, c11.temperature, x);
        float temp = lerp(t0, t1, z);

        float v0 = lerp(c00.vegetation, c10.vegetation, x);
        float v1 = lerp(c01.vegetation, c11.vegetation, x);
        float veg = lerp(v0, v1, z);

        float c0 = lerp(c00.continentalness, c10.continentalness, x);
        float c1 = lerp(c01.continentalness, c11.continentalness, x);
        float cont = lerp(c0, c1, z);

        float e0 = lerp(c00.erosion, c10.erosion, x);
        float e1 = lerp(c01.erosion, c11.erosion, x);
        float ero = lerp(e0, e1, z);

        float w0 = lerp(c00.weirdness, c10.weirdness, x);
        float w1 = lerp(c01.weirdness, c11.weirdness, x);
        float wei = lerp(w0, w1, z);

        return new ClimateData(temp, veg, cont, ero, wei);
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
