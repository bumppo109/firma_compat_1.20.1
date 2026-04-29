package com.bumppo109.firma_compat.addons.firmaciv;

import com.alekiponi.firmaciv.client.render.entity.vehicle.CanoeRenderer;
import com.bumppo109.firma_compat.FirmaCompat;
import com.nebby1999.firmacivplus.FirmaCivPlusEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

@OnlyIn(Dist.CLIENT)
public class CompatFirmaCivClient {

    public static void init(IEventBus bus) {
        bus.addListener(CompatFirmaCivClient::registerRenderers);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (CompatWatercraftMaterial material : CompatWatercraftMaterial.values()) {
            if (material.isSoftwood) {
                event.registerEntityRenderer(
                        FirmaCivPlusEntities.getCanoes().get(material).get(),
                        context -> new CanoeRenderer(
                                context,
                                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "textures/entity/watercraft/dugout_canoe/" + material.getSerializedName() + ".png")
                        )
                );
            }
        }
    }
}