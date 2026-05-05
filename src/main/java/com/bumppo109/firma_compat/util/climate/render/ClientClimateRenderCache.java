package com.bumppo109.firma_compat.util.climate.render;

import com.bumppo109.firma_compat.util.chunkData.ClimateData;
import net.minecraft.world.level.ChunkPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientClimateRenderCache {

    // key = chunk
    private static final Map<ChunkPos, ClimateRenderData> CACHE = new ConcurrentHashMap<>();

    public static void put(ChunkPos pos, ClimateData data) {
        CACHE.put(pos, new ClimateRenderData(data.temperature, data.vegetation));
    }

    public static ClimateRenderData get(ChunkPos pos) {
        return CACHE.get(pos);
    }
}