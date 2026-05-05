package com.bumppo109.firma_compat;

import com.bumppo109.firma_compat.addons.firmalife.CompatFLBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRItems;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.config.FirmaCompatConfig;
import com.bumppo109.firma_compat.event.ModEvents;
import com.bumppo109.firma_compat.everycompat.EveryCompatHandler;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.ModCreativeModeTab;
import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.loot.ModLootModifiers;
import com.bumppo109.firma_compat.worldgen.ModFeatures;
import com.bumppo109.firma_compat.util.chunkData.ModNetworking;
import com.bumppo109.firma_compat.util.climate.models.ModClimateModels;
import com.bumppo109.firma_compat.worldgen.feature.ModPlacement;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(FirmaCompat.MODID)
public class FirmaCompat
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "firma_compat";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean isEclipticLoaded = false;
    public static boolean isLSOLoaded = false;

    public FirmaCompat(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        ModEvents.init();

        ModNetworking.register();

        ModCreativeModeTab.register(modEventBus);
        FirmaCompatConfig.register();

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModFeatures.register(modEventBus);
        ModClimateModels.registerVanillaModel();
        ModPlacement.PLACEMENT_MODIFIERS.register(modEventBus);

        if(ModList.get().isLoaded("eclipticseasons") && ModList.get().isLoaded("legendarysurvivaloverhaul")){
            ModClimateModels.registerEclipticLsoModel();
        } else if (ModList.get().isLoaded("eclipticseasons")) {
            ModClimateModels.registerEclipticModel();
        }


        if(ModList.get().isLoaded("firmalife")){
            CompatFLBlocks.BLOCKS.register(modEventBus);
        }
        if(ModList.get().isLoaded("rnr")){
            CompatRnRBlocks.BLOCKS.register(modEventBus);
            CompatRnRItems.ITEMS.register(modEventBus);
        }

        modEventBus.addListener(this::commonSetup);

        modEventBus.addListener(FirmaCompatClient::clientSetup);

        if(ModList.get().isLoaded("everycomp") || ModList.get().isLoaded("stonezone") || ModList.get().isLoaded("gemsrealm")){
            EveryCompatHandler.registerModules();
        }
        this.modIntegration(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);

    }

    private void modIntegration(IEventBus forgeBus) {
        isEclipticLoaded = ModList.get().isLoaded("eclipticseasons");
        isLSOLoaded = ModList.get().isLoaded("legendarysurvivaloverhaul");
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

}
