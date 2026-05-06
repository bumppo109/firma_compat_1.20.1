package com.bumppo109.firma_compat.worldgen.feature;

import com.bumppo109.firma_compat.util.climate.models.VanillaClimateHelper;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class ClimatePlacement extends PlacementModifier {

    public static final MapCodec<ClimatePlacement> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Codec.FLOAT.optionalFieldOf("min_temperature", Float.NEGATIVE_INFINITY).forGetter(c -> c.minTemp),
                    Codec.FLOAT.optionalFieldOf("max_temperature", Float.POSITIVE_INFINITY).forGetter(c -> c.maxTemp),
                    Codec.FLOAT.optionalFieldOf("min_rainfall", Float.NEGATIVE_INFINITY).forGetter(c -> c.minRainfall),
                    Codec.FLOAT.optionalFieldOf("max_rainfall", Float.POSITIVE_INFINITY).forGetter(c -> c.maxRainfall),
                    Codec.INT.optionalFieldOf("min_elevation", Integer.MIN_VALUE).forGetter(c -> c.minElevation),
                    Codec.INT.optionalFieldOf("max_elevation", Integer.MAX_VALUE).forGetter(c -> c.maxElevation)
            ).apply(instance, ClimatePlacement::new)
    );

    private final float minTemp;
    private final float maxTemp;
    private final float minRainfall;
    private final float maxRainfall;
    private final int minElevation;
    private final int maxElevation;

    public ClimatePlacement(
            float minTemp, float maxTemp,
            float minRainfall, float maxRainfall,
            int minElevation, int maxElevation
    ) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minRainfall = minRainfall;
        this.maxRainfall = maxRainfall;
        this.minElevation = minElevation;
        this.maxElevation = maxElevation;
    }

    @Override
    public PlacementModifierType<?> type() {
        return ModPlacement.COMPAT_CLIMATE_PLACEMENT.get();
    }

    private boolean isValid(WorldGenLevel level, BlockPos pos) {

        float temp = VanillaClimateHelper.getTemperature(
                level, pos
        );

        float rain = VanillaClimateHelper.getRainfall(
                level, pos
        );

        int elevation = pos.getY();

        return elevation >= minElevation && elevation <= maxElevation
                && temp >= minTemp && temp <= maxTemp
                && rain >= minRainfall && rain <= maxRainfall;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        WorldGenLevel level = context.getLevel();

        if (isValid(level, pos)) {
            return Stream.of(pos);
        }

        return Stream.empty();
    }
}