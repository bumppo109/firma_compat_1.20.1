package com.bumppo109.firma_compat.util.climate;

import com.bumppo109.firma_compat.FirmaCompat;
import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.HashMap;
import java.util.Map;

public class ClimateNormalizer {

    // Dimension → (Biome → normalized temperature)
    private final Map<ResourceKey<Level>, Map<Biome, Float>> dimensionBiomeCache = new HashMap<>();

    private boolean fullyCalibrated = false;

    public void calibrateAll(RegistryAccess registryAccess) {
        if (fullyCalibrated) return;

        Registry<LevelStem> dimensionRegistry = registryAccess.registryOrThrow(Registries.LEVEL_STEM);
        Registry<Biome> biomeRegistry = registryAccess.registryOrThrow(Registries.BIOME);

        for (ResourceKey<LevelStem> dimKey : dimensionRegistry.registryKeySet()) {
            ResourceKey<Level> levelKey = ResourceKey.create(Registries.DIMENSION, dimKey.location());

            Map<Biome, Float> dimMap = new HashMap<>();

            for (Holder.Reference<Biome> holder : biomeRegistry.holders().toList()) {
                Biome biome = holder.value();
                String ns = holder.key().location().getNamespace();

                if (ns.equals("tfc")) continue;

                float rawTemp = getBestBaseTemp(biome);
                float normalized = normalizeRaw(rawTemp);

                dimMap.put(biome, normalized);
            }

            dimensionBiomeCache.put(levelKey, dimMap);

            FirmaCompat.LOGGER.info("Calibrated normalization for dimension {} — {} biomes",
                    levelKey.location(), dimMap.size());
        }

        fullyCalibrated = true;
    }

    private float getBestBaseTemp(Biome biome) {
        var settings = BiomeClimateManager.getBiomeClimateSettings(biome, true);
        if (settings != BiomeClimateManager.EMPTY) {
            return settings.getTemperature(SolarTerm.SPRING_EQUINOX);
        }
        return biome.getBaseTemperature();
    }

    /**
     * Core normalization: Squish extremes + smooth mid-range
     */
    private float normalizeRaw(float rawTemp) {
        // Strong squishing of extremes
        float squished = (float) Math.tanh(rawTemp * 2.9f);

        // Reduced scale to bring everything down
        float baseTemp = squished * 24.5f;     // was 27–28, now 24.5

        // Smoothing in the main habitable range
        if (baseTemp > -11 && baseTemp < 28) {
            baseTemp *= 0.91f;                 // stronger smoothing
        }

        return baseTemp;
    }

    // ==================== Retrieval ====================

    public float getNormalized(Biome biome, ResourceKey<Level> dimension) {
        Map<Biome, Float> map = dimensionBiomeCache.get(dimension);
        if (map == null) return 18.0f; // safe default

        return map.getOrDefault(biome, 18.0f);
    }

    public float getNormalized(Biome biome, Level level) {
        return getNormalized(biome, level.dimension());
    }

    public boolean isCalibrated() {
        return fullyCalibrated;
    }
}