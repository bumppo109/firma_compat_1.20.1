package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.worldgen.climate.SmoothedBiomeClimateModel;
import net.dries007.tfc.util.events.SelectClimateModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClimateEventHandler {

    @SubscribeEvent
    public static void onSelectClimateModel(SelectClimateModelEvent event) {
        // Optional: only apply to overworld (or specific dimensions)
        if (event.level().dimension() == net.minecraft.world.level.Level.OVERWORLD) {
            event.setModel(SmoothedBiomeClimateModel.INSTANCE);
        }
    }
}