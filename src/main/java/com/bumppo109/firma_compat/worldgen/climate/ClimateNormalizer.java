package com.bumppo109.firma_compat.worldgen.climate;

import com.bumppo109.firma_compat.FirmaCompat;
import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.HashMap;
import java.util.Map;

public class ClimateNormalizer {

    // Per-dimension calibration data
    private final Map<ResourceKey<net.minecraft.world.level.Level>, NormalizerData> dataMap = new HashMap<>();

    private static class NormalizerData {
        float rawMin = Float.MAX_VALUE;
        float rawMax = Float.MIN_VALUE;
        float rawMean = 0f;
        int sampleCount = 0;
        boolean calibrated = false;
    }

    public void calibrateForDimension(RegistryAccess registryAccess, ResourceKey<net.minecraft.world.level.Level> dimension) {
        NormalizerData data = dataMap.computeIfAbsent(dimension, k -> new NormalizerData());
        if (data.calibrated) return;

        Registry<Biome> registry = registryAccess.registryOrThrow(Registries.BIOME);

        for (Holder.Reference<Biome> holder : registry.holders().toList()) {
            Biome biome = holder.value();
            String ns = holder.key().location().getNamespace();
            if (ns.equals("tfc") || ns.equals("firmalife")) continue;

            float temp = getBestBaseTemp(biome);

            data.rawMin = Math.min(data.rawMin, temp);
            data.rawMax = Math.max(data.rawMax, temp);
            data.rawMean += temp;
            data.sampleCount++;
        }

        if (data.sampleCount > 0) data.rawMean /= data.sampleCount;
        data.calibrated = true;

        FirmaCompat.LOGGER.info("ClimateNormalizer calibrated for {} ({} biomes)", dimension.location(), data.sampleCount);
    }

    private float getBestBaseTemp(Biome biome) {
        var settings = BiomeClimateManager.getBiomeClimateSettings(biome, true);
        if (settings != BiomeClimateManager.EMPTY) {
            return settings.getTemperature(SolarTerm.SPRING_EQUINOX);
        }
        return biome.getBaseTemperature();
    }

    // ===================================================================
    // New normalization: Squish extremes → Smooth transitions
    // ===================================================================
    public float normalize(float rawTemp, ResourceKey<net.minecraft.world.level.Level> dimension) {
        NormalizerData data = dataMap.get(dimension);
        if (data == null || !data.calibrated || data.sampleCount < 5) {
            return rawTemp * 27f; // fallback
        }

        // Step 1: Squish extremes using tanh
        float range = (data.rawMax - data.rawMin) + 0.0001f;
        float centered = (rawTemp - data.rawMean) / (range * 0.5f);
        float squished = (float) Math.tanh(centered * 2.6f);   // higher = stronger squish

        // Step 2: Map squished value (-1..1) to a reasonable temperature range
        float baseTemp = squished * 26.0f;   // -26 to +26 range

        // Step 3: Gentle smoothing curve in the comfortable zone
        if (baseTemp > -8 && baseTemp < 26) {
            baseTemp = baseTemp * 0.88f;     // dampen mid-range for smoother feel
        }

        return baseTemp;
    }

    public float normalize(float rawTemp, net.minecraft.world.level.Level level) {
        return normalize(rawTemp, level.dimension());
    }
}