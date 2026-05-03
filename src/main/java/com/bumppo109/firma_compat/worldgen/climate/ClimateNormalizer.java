package com.bumppo109.firma_compat.worldgen.climate;

import com.bumppo109.firma_compat.FirmaCompat;
import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;

import java.util.Map;
import java.util.Optional;

public class ClimateNormalizer {

    private static final float TARGET_MIN = -24.0f;
    private static final float TARGET_MAX = 34.0f;

    private float rawMin = Float.MAX_VALUE;
    private float rawMax = Float.MIN_VALUE;
    private float rawMean = 0.0f;
    private int sampleCount = 0;

    private boolean calibrated = false;

    /**
     * Calibrate using Ecliptic's processed climate data when available
     */
    public void calibrate(RegistryAccess registryAccess) {
        Optional<Registry<Biome>> biomeRegistry = registryAccess.registry(Registries.BIOME);
        if (biomeRegistry.isEmpty()) {
            FirmaCompat.LOGGER.warn("Could not access biome registry for calibration");
            return;
        }

        int count = 0;
        for (Holder.Reference<Biome> holder : biomeRegistry.get().holders().toList()) {  // More stable way
            Biome biome = holder.value();
            float temp;

            var settings = BiomeClimateManager.getBiomeClimateSettings(biome, true);
            if (settings != BiomeClimateManager.EMPTY) {
                temp = settings.getTemperature(SolarTerm.SPRING_EQUINOX);
            } else {
                temp = biome.getBaseTemperature();
            }

            rawMin = Math.min(rawMin, temp);
            rawMax = Math.max(rawMax, temp);
            rawMean += temp;
            sampleCount++;
            count++;
        }

        if (sampleCount > 0) {
            rawMean /= sampleCount;
        }

        calibrated = true;

        FirmaCompat.LOGGER.info("ClimateNormalizer calibrated from registry with {} biomes. Range: [{:.2f} - {:.2f}], Mean: {:.2f}",
                count, rawMin, rawMax, rawMean);
    }

    /**
     * Normalize a temperature value (works with both raw biome temp and Ecliptic climate temp)
     */
    public float normalize(float rawTemperature) {
        /*
        if (!calibrated) {
        // Try to calibrate on first use if missed
        if (Minecraft.getInstance().level != null) { // client side example
            // fallback or trigger calibration
        }
        return rawTemperature * 28.0f;
    }
         */
        if (!calibrated || sampleCount < 3) {
            return rawTemperature * 28.0f; // fallback for -1 to 1
        }

        float range = (rawMax - rawMin) + 0.0001f;
        float centered = (rawTemperature - rawMean) / (range * 0.5f);

        // tanh compression
        float compressed = (float) Math.tanh(centered * 2.6f); // slightly higher for Ecliptic data

        return TARGET_MIN + (TARGET_MAX - TARGET_MIN) * (compressed + 1.0f) * 0.5f;
    }

    public boolean isCalibrated() {
        return calibrated;
    }
}