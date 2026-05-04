package com.bumppo109.firma_compat.worldgen.climate;

import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;

public class EclipticSeasonalAdjuster {

    /**
     * Returns a realistic Celsius temperature offset for the given Solar Term.
     * Tuned to work well with your current normalization.
     */
    public static float getSeasonalOffset(SolarTerm term) {
        return switch (term) {
            // === SPRING ===
            case BEGINNING_OF_SPRING -> -4.5f;
            case RAIN_WATER         -> -3.0f;
            case INSECTS_AWAKENING  -> -1.5f;
            case SPRING_EQUINOX     -> +0.5f;
            case FRESH_GREEN        -> +2.0f;
            case GRAIN_RAIN         -> +3.5f;

            // === SUMMER ===
            case BEGINNING_OF_SUMMER -> +5.0f;
            case LESSER_FULLNESS     -> +6.5f;
            case GRAIN_IN_EAR        -> +7.5f;
            case SUMMER_SOLSTICE     -> +8.5f;
            case LESSER_HEAT         -> +8.0f;
            case GREATER_HEAT        -> +7.0f;

            // === AUTUMN ===
            case BEGINNING_OF_AUTUMN -> +4.0f;
            case END_OF_HEAT         -> +2.0f;
            case WHITE_DEW           -> +0.5f;
            case AUTUMNAL_EQUINOX    -> -1.5f;
            case COLD_DEW            -> -3.5f;
            case FIRST_FROST         -> -5.5f;

            // === WINTER ===
            case BEGINNING_OF_WINTER -> -7.5f;
            case LIGHT_SNOW          -> -9.0f;
            case HEAVY_SNOW          -> -10.0f;
            case WINTER_SOLSTICE     -> -11.0f;
            case LESSER_COLD         -> -10.5f;
            case GREATER_COLD        -> -9.5f;

            case NONE -> 0.0f;
        };
    }

    /**
     * Optional: Get a seasonal multiplier (1.0 = neutral)
     */
    public static float getSeasonalMultiplier(SolarTerm term) {
        float offset = getSeasonalOffset(term);
        return 1.0f + (offset * 0.035f); // gentle multiplier
    }

    /**
     * Apply seasonal adjustment to a base temperature
     */
    public static float applyToBase(float baseTemp, SolarTerm term) {
        return baseTemp + getSeasonalOffset(term);
    }
}