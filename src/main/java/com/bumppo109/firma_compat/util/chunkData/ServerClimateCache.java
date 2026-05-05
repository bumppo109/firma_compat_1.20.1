package com.bumppo109.firma_compat.util.chunkData;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerClimateCache {

    private static final Map<ServerLevel, Map<ChunkPos, ClimateData>> CACHE = new ConcurrentHashMap<>();

    public static void put(ServerLevel level, ChunkPos pos, ClimateData data) {
        CACHE.computeIfAbsent(level, l -> new ConcurrentHashMap<>()).put(pos, data);
    }

    public static ClimateData get(ServerLevel level, ChunkPos pos) {
        var map = CACHE.get(level);
        return map != null ? map.get(pos) : null;
    }

    public static void clear(ServerLevel level) {
        CACHE.remove(level);
    }
}