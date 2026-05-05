package com.bumppo109.firma_compat.util.climate.render;

public class ClimateColorUtil {

    public static int computeGrass(float temp, float veg) {

        // normalize from roughly [-1, 1]
        float t = clamp((temp + 1f) * 0.5f);
        float v = clamp((veg + 1f) * 0.5f);

        // vegetation drives greenness
        float r = (0.25f + 0.5f * t) * (1f - v * 0.6f);
        float g = (0.6f + 0.4f * v);
        float b = (0.15f + 0.2f * (1f - t));

        int R = (int)(r * 255);
        int G = (int)(g * 255);
        int B = (int)(b * 255);

        return (R << 16) | (G << 8) | B;
    }

    private static float clamp(float f) {
        return Math.max(0f, Math.min(1f, f));
    }
}