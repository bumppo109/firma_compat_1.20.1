package com.bumppo109.firma_compat;

import com.bumppo109.firma_compat.addons.firmaciv.CompatWatercraftMaterial;
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
import com.mojang.logging.LogUtils;
import com.nebby1999.firmacivplus.AFCWatercraftMaterial;
import com.nebby1999.firmacivplus.FirmaCivPlusModsResolver;
import com.nebby1999.firmacivplus.WatercraftMaterial;
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

@Mod(FirmaCompat.MODID)
public class FirmaCompat
{
    public static final String MODID = "firma_compat";
    public static final Logger LOGGER = LogUtils.getLogger();

    public FirmaCompat(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModEvents.init();

        ModCreativeModeTab.register(modEventBus);
        FirmaCompatConfig.register();

        // Register your own blocks and items first
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModFluids.FLUIDS.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModFeatures.register(modEventBus);

        // Register addon blocks/items
        if (ModList.get().isLoaded("firmalife")) {
            CompatFLBlocks.BLOCKS.register(modEventBus);
        }
        if (ModList.get().isLoaded("rnr")) {
            CompatRnRBlocks.BLOCKS.register(modEventBus);
            CompatRnRItems.ITEMS.register(modEventBus);
        }

        // Event listeners
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(FirmaCompatClient::clientSetup);

        if (ModList.get().isLoaded("everycomp") ||
                ModList.get().isLoaded("stonezone") ||
                ModList.get().isLoaded("gemsrealm")) {
            EveryCompatHandler.registerModules();
        }

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        // Add items to creative tabs here if needed
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Server starting logic if needed
    }
}