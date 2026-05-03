package com.bumppo109.firma_compat.worldgen.climate;

import com.bumppo109.firma_compat.FirmaCompat;
import net.dries007.tfc.util.climate.BiomeBasedClimateModel;
import net.dries007.tfc.util.events.SelectClimateModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClimateEventHandler {

    @SubscribeEvent
    public static void onSelectClimateModel(SelectClimateModelEvent event) {
        // Only override if Ecliptic Seasons is present
        if (!ModList.get().isLoaded("eclipticseasons")) {
            return;
        }

        // Replace BiomeBasedClimateModel with our Ecliptic one
        if (event.getModel() instanceof BiomeBasedClimateModel) {
            event.setModel(EclipticSeasonsClimateModel.INSTANCE);

            FirmaCompat.LOGGER.info("Applied Ecliptic Seasons Climate Model for dimension: {}",
                    event.level() != null ? event.level().dimension().location() : "unknown");
        }
    }
}