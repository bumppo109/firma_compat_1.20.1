package com.bumppo109.firma_compat.everycompat;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.everycompat.firmalife.FLStoneZoneModule;
import com.bumppo109.firma_compat.everycompat.firmalife.FLWoodGoodModule;
import com.bumppo109.firma_compat.everycompat.rnr.RnRStoneZoneModule;
import com.bumppo109.firma_compat.everycompat.rnr.RnRWoodGoodModule;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;
import net.minecraftforge.fml.ModList;

public class EveryCompatHandler {

    private EveryCompatHandler() {} // no instances

    public static void registerModules() {
        if(ModList.get().isLoaded("everycomp")){
            CompatWoodGoodModule woodModule = new CompatWoodGoodModule();
            EveryCompatAPI.registerModule(woodModule);
        }
        if(ModList.get().isLoaded("stonezone")){
            CompatStoneZoneModule stoneModule = new CompatStoneZoneModule();
            EveryCompatAPI.registerModule(stoneModule);
        }
        if(ModList.get().isLoaded("gemsrealm")){
            CompatMetalModule metalModule = new CompatMetalModule(FirmaCompat.MODID);
            EveryCompatAPI.registerModule(metalModule);
        }

        if(ModList.get().isLoaded("firmalife")){
            if(ModList.get().isLoaded("stonezone")){
                FLStoneZoneModule flStoneModule = new FLStoneZoneModule();
                EveryCompatAPI.registerModule(flStoneModule);
            }
            if(ModList.get().isLoaded("everycomp")){
                FLWoodGoodModule flWoodModule = new FLWoodGoodModule();
                EveryCompatAPI.registerModule(flWoodModule);
            }
        }

        if(ModList.get().isLoaded("rnr")){
            if(ModList.get().isLoaded("stonezone")){
                RnRStoneZoneModule rnrStoneModule = new RnRStoneZoneModule();
                EveryCompatAPI.registerModule(rnrStoneModule);
            }
            if(ModList.get().isLoaded("everycomp")){
                RnRWoodGoodModule rnrWoodGoodModule = new RnRWoodGoodModule();
                EveryCompatAPI.registerModule(rnrWoodGoodModule);
            }
        }
    }
}

