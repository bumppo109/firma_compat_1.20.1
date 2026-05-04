package com.bumppo109.firma_compat.worldgen.climate;

import com.bumppo109.firma_compat.FirmaCompat;
import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;
import com.teamtea.eclipticseasons.api.util.EclipticUtil;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModelType;
import net.dries007.tfc.util.climate.TimeInvariantClimateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.IForgeRegistry;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.registry.TemperatureModifierRegistry;
import sfiomn.legendarysurvivaloverhaul.util.MathUtil;

public class LSOClimateModel implements TimeInvariantClimateModel {

    public static final LSOClimateModel INSTANCE = new LSOClimateModel();

    @Override
    public ClimateModelType type() {
        return ModClimateModels.LSO_BASED.get();
    }

    @Override
    public float getTemperature(LevelReader levelReader, BlockPos blockPos) {
        Biome biome = levelReader.getBiome(blockPos).value();

        if (!(levelReader instanceof Level level)) {
            // Worldgen fallback
            return Climate.toActualTemperature(biome.getBaseTemperature());
        }
        float biomeInfluence = TemperatureModifierRegistry.BIOME.get().getWorldInfluence(null, level, blockPos);
        float altitudeInfluence = TemperatureModifierRegistry.ALTITUDE.get().getWorldInfluence(null, level, blockPos);
        float dimensionInfluence = TemperatureModifierRegistry.DIMENSION.get().getWorldInfluence(null, level, blockPos);
        float timeInfluence = TemperatureModifierRegistry.TIME.get().getWorldInfluence(null, level, blockPos);

        return dimensionInfluence + biomeInfluence + altitudeInfluence + timeInfluence;
    }

    @Override
    public float getRainfall(LevelReader level, BlockPos pos) {
        return ((Biome)level.getBiome(pos).value()).getPrecipitationAt(pos) != Biome.Precipitation.NONE ? 300.0F : 0.0F;
    }
}
