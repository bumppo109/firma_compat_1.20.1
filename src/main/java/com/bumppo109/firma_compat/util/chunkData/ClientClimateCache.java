package com.bumppo109.firma_compat.util.chunkData;

import net.minecraft.world.level.ChunkPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientClimateCache {

    private static final Map<ChunkPos, ClimateData> CACHE = new ConcurrentHashMap<>();

    public static void put(ChunkPos pos, ClimateData data) {
        CACHE.put(pos, data);
    }

    public static ClimateData get(ChunkPos pos) {
        return CACHE.get(pos);
    }

    public static void clear() {
        CACHE.clear();
    }
}