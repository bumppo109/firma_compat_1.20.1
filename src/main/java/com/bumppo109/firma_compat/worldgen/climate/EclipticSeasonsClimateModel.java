package com.bumppo109.firma_compat.worldgen.climate;

import com.bumppo109.firma_compat.FirmaCompat;
import com.teamtea.eclipticseasons.api.EclipticSeasonsApi;
import com.teamtea.eclipticseasons.api.constant.biome.Temperature;
import com.teamtea.eclipticseasons.api.constant.solar.Season;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import com.teamtea.eclipticseasons.common.core.biome.BiomeClimateManager;
import net.dries007.tfc.mixin.accessor.BiomeAccessor;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.TimeInvariantClimateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;

public class EclipticSeasonsClimateModel implements TimeInvariantClimateModel {

    public static final EclipticSeasonsClimateModel INSTANCE = new EclipticSeasonsClimateModel();

    @Override
    public ClimateModelType type() {
        return ModClimateModels.ECLIPTIC_BASED.get();
    }

    @Override
    public float getTemperature(LevelReader levelReader, BlockPos pos) {
        if (!(levelReader instanceof Level level)) {
            return getVanillaLikeTemperature(levelReader, pos);
        }

        Biome biome = level.getBiome(pos).value();
        float rawTemp = biome.getBaseTemperature();

        try {
            return FirmaCompat.CLIMATE_NORMALIZER.normalize(rawTemp, level);
        } catch (Exception e) {
            return getVanillaLikeTemperature(levelReader, pos);
        }
    }

    @Override
    public float getRainfall(LevelReader levelReader, BlockPos pos) {
        //fallback
        if (!(levelReader instanceof Level level)) {
            return ((Biome)levelReader.getBiome(pos).value()).getPrecipitationAt(pos) != Precipitation.NONE ? 300.0F : 0.0F;
        }

        try {
            Holder<Biome> biomeHolder = level.getBiome(pos);
            Biome biome = biomeHolder.value();
            SolarTerm solarTerm = EclipticUtil.getNowSolarTerm(level);
            boolean serverSide = !level.isClientSide();

            float downfall = EclipticUtil.getDownfallFloat(level, solarTerm, biome, pos, serverSide);

            // TFC rainfall is roughly 0-500
            // Ecliptic downfall is usually 0-1+ range, so we scale it
            float rainfall = downfall * 350.0f;   // tweak multiplier as needed

            return Mth.clamp(rainfall, 0.0f, 500.0f);
        } catch (Exception e) {
            return 180.0f; // default
        }
    }

    /**
     * Fallback when we only have LevelReader (e.g. during worldgen)
     */
    private float getVanillaLikeTemperature(LevelReader levelReader, BlockPos pos) {
        Biome biome = levelReader.getBiome(pos).value();
        float vanillaTemp = biome.getBaseTemperature();

        return (vanillaTemp - 0.15f) / 0.0217f;
    }
}