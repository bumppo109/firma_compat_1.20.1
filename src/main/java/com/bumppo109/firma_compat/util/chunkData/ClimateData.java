package com.bumppo109.firma_compat.util.chunkData;

public class ClimateData {
    public final float temperature;
    public final float vegetation;
    public final float continentalness;
    public final float erosion;
    public final float weirdness;

    public ClimateData(float temperature, float vegetation, float continentalness, float erosion, float weirdness) {
        this.temperature = temperature;
        this.vegetation = vegetation;
        this.continentalness = continentalness;
        this.erosion = erosion;
        this.weirdness = weirdness;
    }
}
