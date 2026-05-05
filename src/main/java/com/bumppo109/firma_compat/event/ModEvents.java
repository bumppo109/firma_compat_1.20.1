package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.mixin.BlockEntityTypeAccessor;
import com.eerussianguy.firmalife.common.blockentities.FLBlockEntities;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.NotNull;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ModEvents
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ModEvents::setup);
        bus.addListener(ModEvents::onPackFinder);
    }

    private static void setup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(ModEvents::modifyBlockEntityTypes);
    }

    // ====================== BLOCK ENTITY MODIFICATIONS ======================

    private static void modifyBlockEntityTypes()
    {
        FirmaCompat.LOGGER.info("Starting Firma Compat BlockEntityType modifications...");

        // Debug: check if mixin is applied
        FirmaCompat.LOGGER.info("Chest BE class: {}", TFCBlockEntities.CHEST.get().getClass().getName());

        // Special single blocks (chests)
        modifyBlockEntityType(TFCBlockEntities.CHEST.get(), ModBlocks.COMPAT_CHEST.get());
        modifyBlockEntityType(TFCBlockEntities.TRAPPED_CHEST.get(), ModBlocks.COMPAT_TRAPPED_CHEST.get());  // Note: trapped chests usually use the same BE as normal chests in TFC
        modifyBlockEntityType(TFCBlockEntities.TICK_COUNTER.get(), ModBlocks.DRYING_MUD_BRICK.get());  // Note: trapped chests usually use the same BE as normal chests in TFC

        addDynamicBlocksByName();

        FirmaCompat.LOGGER.info("Finished BlockEntityType modifications.");
    }

    /**
     * Safe version for a single block
     */
    private static void modifyBlockEntityType(BlockEntityType<?> type, Block extraBlock)
    {
        if (extraBlock == null) {
            FirmaCompat.LOGGER.warn("Attempted to add null block to BE type {}", type);
            return;
        }

        try {
            BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor) (Object) type;
            Set<Block> validBlocks = new HashSet<>(accessor.accessor$getValidBlocks());

            if (validBlocks.add(extraBlock)) {
                FirmaCompat.LOGGER.info("Successfully added block {} to BlockEntityType {}",
                        extraBlock.getDescriptionId(), type);
            } else {
                FirmaCompat.LOGGER.debug("Block {} was already registered for BE {}",
                        extraBlock.getDescriptionId(), type);
            }

            accessor.accessor$setValidBlocks(validBlocks);
        } catch (Exception e) {
            FirmaCompat.LOGGER.error("Failed to add block {} to BlockEntityType {}. Mixin issue?",
                    extraBlock, type, e);
        }
    }

    /**
     * Legacy multi-block version (kept for safety)
     */
    private static void modifyBlockEntityType(BlockEntityType<?> type, Stream<Block> extraBlocks)
    {
        try {
            BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor) (Object) type;
            Set<Block> validBlocks = new HashSet<>(accessor.accessor$getValidBlocks());

            extraBlocks
                    .filter(b -> b != null)
                    .forEach(validBlocks::add);

            accessor.accessor$setValidBlocks(validBlocks);
        } catch (Exception e) {
            FirmaCompat.LOGGER.error("Failed in multi-block BE modification", e);
        }
    }

    /**
     * NEW: Dynamically add EveryCompat-generated blocks by name pattern
     * This mimics what you did successfully in 1.21.1 NeoForge
     */
    private static void addDynamicBlocksByName()
    {
        String modid = FirmaCompat.MODID;

        // Example patterns - adjust or expand as needed
        addBlocksBySuffix(TFCBlockEntities.LOOM.get(),        modid, "_loom");
        addBlocksBySuffix(TFCBlockEntities.BARREL.get(),      modid, "_barrel");
        addBlocksBySuffix(TFCBlockEntities.TOOL_RACK.get(),   modid, "_tool_rack");
        addBlocksBySuffix(TFCBlockEntities.SLUICE.get(),      modid, "_sluice");
        addBlocksBySuffix(TFCBlockEntities.AXLE.get(),        modid, "_axle");
        addBlocksBySuffix(TFCBlockEntities.BLADED_AXLE.get(), modid, "_bladed_axle");
        addBlocksBySuffix(TFCBlockEntities.WATER_WHEEL.get(), modid, "_water_wheel");
        addBlocksBySuffix(TFCBlockEntities.WINDMILL.get(),    modid, "_windmill");
        addBlocksBySuffix(TFCBlockEntities.CLUTCH.get(),      modid, "_clutch");
        addBlocksBySuffix(TFCBlockEntities.GEAR_BOX.get(),    modid, "_gear_box");

        // Chests are usually handled separately because EveryCompat may name them differently
        addBlocksBySuffix(TFCBlockEntities.CHEST.get(), modid, "_chest");
    }

    private static void addDynamicFLBlocksByName()
    {
        String modid = FirmaCompat.MODID;

        // Example patterns - adjust or expand as needed
        addBlocksBySuffix(FLBlockEntities.FOOD_SHELF.get(),        modid, "_food_shelf");
        addBlocksBySuffix(FLBlockEntities.HANGER.get(),        modid, "_hanger");
        addBlocksBySuffix(FLBlockEntities.JARBNET.get(),        modid, "_jarbnet");
        addBlocksBySuffix(FLBlockEntities.BIG_BARREL.get(),        modid, "_big_barrel");
        addBlocksBySuffix(FLBlockEntities.WINE_SHELF.get(),        modid, "_wine_shelf");
        addBlocksBySuffix(FLBlockEntities.STOMPING_BARREL.get(),        modid, "_stomping_barrel");
        addBlocksBySuffix(FLBlockEntities.BARREL_PRESS.get(),        modid, "_barrel_press");
    }

    private static void addBlocksBySuffix(BlockEntityType<?> type, String namespace, String suffix)
    {
        ForgeRegistries.BLOCKS.getEntries().stream()
                .filter(entry -> {
                    ResourceLocation key = entry.getKey().location();
                    return key.getNamespace().equals(namespace) && key.getPath().endsWith(suffix);
                })
                .map(entry -> entry.getValue())
                .forEach(block -> modifyBlockEntityType(type, block));
    }

    // ====================== RESOURCE PACK INJECTION (unchanged but safer) ======================

    public static void onPackFinder(AddPackFindersEvent event)
    {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;

        try
        {
            final IModFile modFile = ModList.get().getModFileById(FirmaCompat.MODID).getFile();

            try (PathPackResources pack = new PathPackResources(modFile.getFileName() + ":overload", true, modFile.getFilePath())
            {
                private final IModFile file = ModList.get().getModFileById(FirmaCompat.MODID).getFile();

                @NotNull
                @Override
                protected Path resolve(String @NotNull ... paths)
                {
                    return file.findResource(paths);
                }
            })
            {
                final PackMetadataSection metadata = pack.getMetadataSection(PackMetadataSection.TYPE);
                if (metadata != null)
                {
                    FirmaCompat.LOGGER.info("Injecting Firma Compat override pack");
                    event.addRepositorySource(consumer ->
                            consumer.accept(Pack.readMetaAndCreate(
                                    "firma_compat_override",                                 // ← changed
                                    Component.literal("Firma Compat Resources"),
                                    true,
                                    id -> pack,
                                    PackType.CLIENT_RESOURCES,
                                    Pack.Position.TOP,
                                    PackSource.BUILT_IN
                            ))
                    );
                }
            }
        }
        catch (IOException e)
        {
            FirmaCompat.LOGGER.error("Failed to inject Firma Compat resource pack", e);
        }
    }
}