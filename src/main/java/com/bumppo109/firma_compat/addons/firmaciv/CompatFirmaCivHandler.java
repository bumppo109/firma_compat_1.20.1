package com.bumppo109.firma_compat.addons.firmaciv;

import com.bumppo109.firma_compat.FirmaCompat;
import com.nebby1999.firmacivplus.WatercraftMaterial;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;

public class CompatFirmaCivHandler {

    public static void init(IEventBus modEventBus) {
        CompatFirmaCivBlocks.init(modEventBus);

        if (net.minecraftforge.fml.loading.FMLEnvironment.dist.isClient()) {
            CompatFirmaCivClient.init(modEventBus);
        }
    }

    public static void commonSetup() {
        if (ModList.get().isLoaded("firmacivplus")) {
            WatercraftMaterial.addMaterials(CompatWatercraftMaterial.values());
            FirmaCompat.LOGGER.info("Registered {} CompatWatercraftMaterials with FirmaCiv+",
                    CompatWatercraftMaterial.values().length);
        }
    }
}