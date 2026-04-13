package com.bumppo109.firma_compat.event;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.mixin.BlockEntityTypeAccessor;
import net.minecraft.network.chat.Component;
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
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.NotNull;

import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.wood.Wood;

public class ModEvents
{
    public static void init()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ModEvents::setup);
        bus.addListener(ModEvents::onPackFinder);
    }

    public static void initFLCompat()
    {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ModEvents::setupFLCompat);
        bus.addListener(ModEvents::onFLCompatPackFinder);
        bus.addListener(ModEvents::onFLCompatDataPackFinder);

        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            //bus.addListener(com.therighthon.afc.event.ModEventClientBusEvents::clientFLCompatSetup);
        }
    }

    private static void setup(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            //AFCBlocks.registerFlowerPotFlowers();
            modifyBlockEntityTypes();
        });
    }

    private static void setupFLCompat(FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            //modifyFLBlockEntityTypes();
        });
    }

    //This is copied wholesale from FirmaLife
    //It is what allows the resources to load in the correct order, and needs to be rewritten for 1.20 unless things work out fo the box now.
    public static void onPackFinder(AddPackFindersEvent event)
    {
        try
        {
            if (event.getPackType() == PackType.CLIENT_RESOURCES)
            {
                final IModFile modFile = ModList.get().getModFileById(FirmaCompat.MODID).getFile();
                final Path resourcePath = modFile.getFilePath();
                try (PathPackResources pack = new PathPackResources(modFile.getFileName() + ":overload", true, resourcePath){

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
                                consumer.accept(Pack.readMetaAndCreate("afc_data", Component.literal("Firma Compat Resources"), true, id -> pack, PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN))
                        );
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void onFLCompatPackFinder(AddPackFindersEvent event)
    {
        try
        {
            if (event.getPackType() == PackType.CLIENT_RESOURCES)
            {
                final Path resourcePath = ModList.get().getModFileById(FirmaCompat.MODID).getFile().findResource("firmalife_compat_assets");
                try (PathPackResources pack = new PathPackResources("firmalife_compat_assets", true, resourcePath))
                {
                    final PackMetadataSection metadata = pack.getMetadataSection(PackMetadataSection.TYPE);
                    if (metadata != null)
                    {
                        FirmaCompat.LOGGER.info("Adding FirmaLife compatibility resource pack");
                        event.addRepositorySource(consumer ->
                                consumer.accept(Pack.readMetaAndCreate("firmalife_compat_assets", Component.literal("FirmaLife Compat Resources"), true, id -> pack, PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN))
                        );
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void onFLCompatDataPackFinder(AddPackFindersEvent event)
    {
        try
        {
            if (event.getPackType() == PackType.SERVER_DATA)
            {
                final Path resourcePath = ModList.get().getModFileById(FirmaCompat.MODID).getFile().findResource("firmalife_compat_data");
                try (PathPackResources pack = new PathPackResources("firmalife_compat_data", true, resourcePath))
                {
                    final PackMetadataSection metadata = pack.getMetadataSection(PackMetadataSection.TYPE);
                    if (metadata != null)
                    {
                        FirmaCompat.LOGGER.info("Adding FirmaLife compatibility data pack");
                        event.addRepositorySource(consumer ->
                                consumer.accept(Pack.readMetaAndCreate("firmalife_compat_data", Component.literal("FirmaLife Compat Data"), true, id -> pack, PackType.SERVER_DATA, Pack.Position.TOP, PackSource.BUILT_IN))
                        );
                    }
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }


    private static void modifyBlockEntityTypes()
    {
        FirmaCompat.LOGGER.info("BlockEntityType class: {}", TFCBlockEntities.CHEST.get().getClass().getName());

        modifyWood(TFCBlockEntities.LOOM.get(), CompatWood.BlockType.LOOM);
        modifyWood(TFCBlockEntities.BARREL.get(), CompatWood.BlockType.BARREL);
        modifyWood(TFCBlockEntities.SLUICE.get(), CompatWood.BlockType.SLUICE);
        modifyWood(TFCBlockEntities.TOOL_RACK.get(), CompatWood.BlockType.TOOL_RACK);
        modifyWood(TFCBlockEntities.AXLE.get(), CompatWood.BlockType.AXLE);
        modifyWood(TFCBlockEntities.BLADED_AXLE.get(), CompatWood.BlockType.BLADED_AXLE);
        modifyWood(TFCBlockEntities.WATER_WHEEL.get(), CompatWood.BlockType.WATER_WHEEL);
        modifyWood(TFCBlockEntities.WINDMILL.get(), CompatWood.BlockType.WINDMILL);

        modifyBlockEntityType(TFCBlockEntities.CHEST.get(), ModBlocks.COMPAT_CHEST.get());
        modifyBlockEntityType(TFCBlockEntities.CHEST.get(), ModBlocks.COMPAT_TRAPPED_CHEST.get());
    }

    private static void modifyWood(BlockEntityType<?> type, CompatWood.BlockType blockType)
    {
        modifyBlockEntityType(type, ModBlocks.WOODS.values().stream().map(map -> map.get(blockType).get()));
    }

    /*
    private static void modifyFLBlockEntityTypes()
    {
        modifyBlockEntityType(FLBlockEntities.STOMPING_BARREL.get(), Stream.of(FLCompatBlocks.STOMPING_BARRELS.get(AFCWood.BAOBAB).get()));
        modifyBlockEntityType(FLBlockEntities.STOMPING_BARREL.get(), Stream.of(FLCompatBlocks.STOMPING_BARRELS.get(AFCWood.EUCALYPTUS).get()));
        modifyBlockEntityType(FLBlockEntities.STOMPING_BARREL.get(), Stream.of(FLCompatBlocks.STOMPING_BARRELS.get(AFCWood.MAHOGANY).get()));
        modifyBlockEntityType(FLBlockEntities.STOMPING_BARREL.get(), Stream.of(FLCompatBlocks.STOMPING_BARRELS.get(AFCWood.HEVEA).get()));
        modifyBlockEntityType(FLBlockEntities.STOMPING_BARREL.get(), Stream.of(FLCompatBlocks.STOMPING_BARRELS.get(AFCWood.TUALANG).get()));
        modifyBlockEntityType(FLBlockEntities.STOMPING_BARREL.get(), Stream.of(FLCompatBlocks.STOMPING_BARRELS.get(AFCWood.TEAK).get()));
        modifyBlockEntityType(FLBlockEntities.STOMPING_BARREL.get(), Stream.of(FLCompatBlocks.STOMPING_BARRELS.get(AFCWood.CYPRESS).get()));
        modifyBlockEntityType(FLBlockEntities.STOMPING_BARREL.get(), Stream.of(FLCompatBlocks.STOMPING_BARRELS.get(AFCWood.FIG).get()));
        modifyBlockEntityType(FLBlockEntities.STOMPING_BARREL.get(), Stream.of(FLCompatBlocks.STOMPING_BARRELS.get(AFCWood.IRONWOOD).get()));
        modifyBlockEntityType(FLBlockEntities.STOMPING_BARREL.get(), Stream.of(FLCompatBlocks.STOMPING_BARRELS.get(AFCWood.IPE).get()));

        modifyBlockEntityType(FLBlockEntities.BARREL_PRESS.get(), Stream.of(FLCompatBlocks.BARREL_PRESSES.get(AFCWood.BAOBAB).get()));
        modifyBlockEntityType(FLBlockEntities.BARREL_PRESS.get(), Stream.of(FLCompatBlocks.BARREL_PRESSES.get(AFCWood.EUCALYPTUS).get()));
        modifyBlockEntityType(FLBlockEntities.BARREL_PRESS.get(), Stream.of(FLCompatBlocks.BARREL_PRESSES.get(AFCWood.MAHOGANY).get()));
        modifyBlockEntityType(FLBlockEntities.BARREL_PRESS.get(), Stream.of(FLCompatBlocks.BARREL_PRESSES.get(AFCWood.HEVEA).get()));
        modifyBlockEntityType(FLBlockEntities.BARREL_PRESS.get(), Stream.of(FLCompatBlocks.BARREL_PRESSES.get(AFCWood.TUALANG).get()));
        modifyBlockEntityType(FLBlockEntities.BARREL_PRESS.get(), Stream.of(FLCompatBlocks.BARREL_PRESSES.get(AFCWood.TEAK).get()));
        modifyBlockEntityType(FLBlockEntities.BARREL_PRESS.get(), Stream.of(FLCompatBlocks.BARREL_PRESSES.get(AFCWood.CYPRESS).get()));
        modifyBlockEntityType(FLBlockEntities.BARREL_PRESS.get(), Stream.of(FLCompatBlocks.BARREL_PRESSES.get(AFCWood.FIG).get()));
        modifyBlockEntityType(FLBlockEntities.BARREL_PRESS.get(), Stream.of(FLCompatBlocks.BARREL_PRESSES.get(AFCWood.IRONWOOD).get()));
        modifyBlockEntityType(FLBlockEntities.BARREL_PRESS.get(), Stream.of(FLCompatBlocks.BARREL_PRESSES.get(AFCWood.IPE).get()));
    }

     */

    private static void modifyBlockEntityType(BlockEntityType<?> type, Stream<Block> extraBlocks)
    {
        Set<Block> blocks = ((BlockEntityTypeAccessor) (Object) type).accessor$getValidBlocks();
        blocks = new HashSet<>(blocks);

        blocks.addAll(extraBlocks.collect(Collectors.toList())); //Autocompleted, could cause problems?
        ((BlockEntityTypeAccessor) (Object) type).accessor$setValidBlocks(blocks);
    }

    private static void modifyBlockEntityType(BlockEntityType<?> type, Block extraBlock)
    {
        if (extraBlock == null) return;

        Set<Block> validBlocks = new HashSet<>(((BlockEntityTypeAccessor) (Object) type).accessor$getValidBlocks());

        if (validBlocks.add(extraBlock)) {
            FirmaCompat.LOGGER.debug("Added block {} to BlockEntityType {}",
                    extraBlock.getDescriptionId(), type);
        }

        ((BlockEntityTypeAccessor) (Object) type).accessor$setValidBlocks(validBlocks);
    }
}
