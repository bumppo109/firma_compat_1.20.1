package com.bumppo109.firma_compat.everycompat;

import com.bumppo109.firma_compat.FirmaCompat;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.BarrelBlockEntity;
import net.dries007.tfc.common.blockentities.LoomBlockEntity;
import net.dries007.tfc.common.blockentities.SluiceBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blockentities.rotation.WaterWheelBlockEntity;
import net.dries007.tfc.common.blockentities.rotation.WindmillBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.dries007.tfc.common.blocks.JarShelfBlock;
import net.dries007.tfc.common.blocks.devices.BarrelBlock;
import net.dries007.tfc.common.blocks.devices.SluiceBlock;
import net.dries007.tfc.common.blocks.rotation.*;
import net.dries007.tfc.common.blocks.wood.*;
import net.dries007.tfc.common.items.BarrelBlockItem;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.*;
import net.mehvahdjukaar.every_compat.misc.UtilityTag;
import net.mehvahdjukaar.every_compat.modules.EveryCompatModule;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Consumer;

import static net.mehvahdjukaar.every_compat.misc.UtilityTag.getATagOrCreateANew;

public final class CompatWoodGoodModule extends EveryCompatModule {

    public final ItemOnlyEntrySet<WoodType, Item> LUMBER;
    public final ItemOnlyEntrySet<WoodType, Item> SUPPORT;
    public final SimpleEntrySet<WoodType, Block> TWIG;
    public final SimpleEntrySet<WoodType, Block> LOG_FENCE;
    public final SimpleEntrySet<WoodType, Block> VERTICAL_SUPPORT;
    public final SimpleEntrySet<WoodType, Block> HORIZONTAL_SUPPORT;
    public final SimpleEntrySet<WoodType, Block> TOOL_RACK;
    public final SimpleEntrySet<WoodType, Block> LOOM;
    public final SimpleEntrySet<WoodType, Block> SLUICE;
    public final SimpleEntrySet<WoodType, Block> BARREL;
    public final SimpleEntrySet<WoodType, Block> SCRIBING_TABLE;
    public final SimpleEntrySet<WoodType, Block> SEWING_TABLE;
    public final SimpleEntrySet<WoodType, Block> SHELF;
    public SimpleEntrySet<WoodType, Block> AXLE;
    public final SimpleEntrySet<WoodType, Block> BLADED_AXLE;
    public final SimpleEntrySet<WoodType, Block> ENCASED_AXLE;
    public final SimpleEntrySet<WoodType, Block> CLUTCH;
    public final SimpleEntrySet<WoodType, Block> GEAR_BOX;
    public SimpleEntrySet<WoodType, Block> WINDMILL;
    public final SimpleEntrySet<WoodType, Block> WATER_WHEEL;

    //private final Map<WoodType, RotationDevicePair> rotationPairs = new HashMap<>();


    public CompatWoodGoodModule() {
        super(FirmaCompat.MODID, FirmaCompat.MODID, FirmaCompat.MODID);

        ResourceKey<CreativeModeTab> tab = CreativeModeTabs.BUILDING_BLOCKS;

        LUMBER = ItemOnlyEntrySet.builder(WoodType.class, "lumber",
                        getModItem("oak_lumber"), () -> VanillaWoodTypes.OAK,
                        w -> new Item(new Item.Properties())
                )
                .requiresChildren("planks")
                .addTexture(modRes("item/oak_lumber"), PaletteStrategies.MAIN_CHILD)
                .addTag(modRes("compat_lumber"), Registries.ITEM)
                .addTag(ResourceLocation.fromNamespaceAndPath("tfc","lumber"), Registries.ITEM)
                //.addRecipe(modRes("crafting/oak_planks"))
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .excludeBlockTypes("afc:.*")
                .build();
        this.addEntry(LUMBER);

        TWIG = SimpleEntrySet.builder(WoodType.class, "twig",
                        getModBlock("oak_twig"), () -> VanillaWoodTypes.OAK,
                        w -> GroundcoverBlock.twig(ExtendedProperties.of().strength(0.05F, 0.0F).sound(SoundType.WOOD).noCollission().flammableLikeWool())
                )
                .requiresChildren("log")
                .addTexture(modRes("item/oak_twig"), PaletteStrategies.MAIN_CHILD)
                .addTag(ResourceLocation.fromNamespaceAndPath("tfc", "twigs"), Registries.ITEM)
                .addTag(modRes("twigs"), Registries.BLOCK)
                .addTag(Tags.Items.RODS_WOODEN, Registries.ITEM)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(TWIG);

        LOG_FENCE = SimpleEntrySet.builder(WoodType.class, "log_fence",
                        getModBlock("oak_log_fence"), () -> VanillaWoodTypes.OAK,
                        w -> new
                                TFCFenceBlock(ExtendedProperties.of().strength(2.0F, 3.0F).flammableLikeLogs())
                )
                .requiresChildren("planks", "log")
                .addTag(ItemTags.WOODEN_FENCES, Registries.ITEM)
                .addTag(BlockTags.WOODEN_FENCES, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_log_fence"))
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(LOG_FENCE);

        VERTICAL_SUPPORT = SimpleEntrySet.builder(WoodType.class, "vertical_support",
                        getModBlock("oak_vertical_support"), () -> VanillaWoodTypes.OAK,
                        w -> new
                                VerticalSupportBlock(ExtendedProperties.of().strength(1.0F).noOcclusion().flammableLikeLogs())
                )
                .requiresChildren("log")
                .addTag(TFCTags.Blocks.SUPPORT_BEAM, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTexture(ResourceLocation.withDefaultNamespace("textures/item/barrier"))
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(VERTICAL_SUPPORT);

        HORIZONTAL_SUPPORT = SimpleEntrySet.builder(WoodType.class, "horizontal_support",
                        getModBlock("oak_horizontal_support"), () -> VanillaWoodTypes.OAK,
                        w -> new
                                HorizontalSupportBlock(ExtendedProperties.of().strength(1.0F).noOcclusion().flammableLikeLogs())
                )
                .requiresChildren("log")
                .addTag(TFCTags.Blocks.SUPPORT_BEAM, Registries.BLOCK)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .addTexture(ResourceLocation.withDefaultNamespace("textures/item/barrier"))
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(HORIZONTAL_SUPPORT);

        SUPPORT = ItemOnlyEntrySet.builder(WoodType.class, "support",
                        getModItem("oak_support"), () -> VanillaWoodTypes.OAK,
                        w -> new StandingAndWallBlockItem(VERTICAL_SUPPORT.blocks.get(w), HORIZONTAL_SUPPORT.blocks.get(w), new Item.Properties(), Direction.DOWN)
                )
                .requiresFromMap(VERTICAL_SUPPORT.blocks)
                .requiresFromMap(HORIZONTAL_SUPPORT.blocks)
                .requiresChildren("log")
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SUPPORT);

        TOOL_RACK = SimpleEntrySet.builder(WoodType.class, "tool_rack",
                        getModBlock("oak_tool_rack"), () -> VanillaWoodTypes.OAK,
                        w -> new
                                ToolRackBlock(ExtendedProperties.of().strength(2.0F).noOcclusion().blockEntity(TFCBlockEntities.TOOL_RACK))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_tool_rack"))
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(TOOL_RACK);

        LOOM = SimpleEntrySet.builder(WoodType.class, "loom",
                        getModBlock("oak_loom"), () -> VanillaWoodTypes.OAK,
                        w -> new
                                TFCLoomBlock(ExtendedProperties.of().strength(2.5F).noOcclusion().flammableLikePlanks()
                                .blockEntity(TFCBlockEntities.LOOM).ticks(LoomBlockEntity::tick),
                                getPlanksTextureId(w))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_loom"))
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(LOOM);

        SLUICE = SimpleEntrySet.builder(WoodType.class, "sluice",
                        getModBlock("oak_sluice"), () -> VanillaWoodTypes.OAK,
                        w -> new
                                SluiceBlock(ExtendedProperties.of().strength(3F).noOcclusion().flammableLikeLogs().blockEntity(TFCBlockEntities.SLUICE).serverTicks(SluiceBlockEntity::serverTick))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_sluice"))
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SLUICE);

        BARREL = SimpleEntrySet.builder(WoodType.class, "barrel",
                        getModBlock("oak_barrel"), () -> VanillaWoodTypes.OAK,
                        w -> new BarrelBlock(ExtendedProperties.of().strength(2.5f).flammableLikePlanks().noOcclusion()
                                .blockEntity(TFCBlockEntities.BARREL).serverTicks(BarrelBlockEntity::serverTick))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_barrel"))
                .setRenderType(RenderLayer.CUTOUT)
                .addCustomItem((wood, block, itemProperties) ->
                        new BarrelBlockItem(block, itemProperties))
                .copyParentDrop()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(BARREL);

        SCRIBING_TABLE = SimpleEntrySet.builder(WoodType.class, "scribing_table",
                        getModBlock("oak_scribing_table"), () -> VanillaWoodTypes.OAK,
                        w -> new
                                ScribingTableBlock(ExtendedProperties.of().noOcclusion().strength(2.5F).flammable(20, 30))
                )
                .requiresChildren("planks", "slab")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_scribing_table"))
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SCRIBING_TABLE);

        SEWING_TABLE = SimpleEntrySet.builder(WoodType.class, "sewing_table",
                        getModBlock("oak_sewing_table"), () -> VanillaWoodTypes.OAK,
                        w -> new
                                SewingTableBlock(ExtendedProperties.of().noOcclusion().strength(2.5F).flammable(20, 30))
                )
                .requiresChildren("planks", "log")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_sewing_table"))
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SEWING_TABLE);

        SHELF = SimpleEntrySet.builder(WoodType.class, "shelf",
                        getModBlock("oak_shelf"), () -> VanillaWoodTypes.OAK,
                        w -> new
                                JarShelfBlock(ExtendedProperties.of().noOcclusion().strength(2.5f).flammableLikePlanks().blockEntity(TFCBlockEntities.JARS))
                )
                .requiresChildren("planks")
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_shelf"))
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SHELF);

        AXLE = SimpleEntrySet.builder(WoodType.class, "axle",
                        () -> getModBlock("oak_axle").get(),
                        () -> VanillaWoodTypes.OAK,
                        w -> new AxleBlock(
                                ExtendedProperties.of()
                                        .noOcclusion()
                                        .strength(2.5F)
                                        .flammableLikeLogs()
                                        .pushReaction(PushReaction.DESTROY)
                                        .blockEntity(TFCBlockEntities.AXLE),
                                () -> (WindmillBlock) WINDMILL.blocks.get(w),   // safe: WINDMILL already assigned
                                getPlanksTextureId(w)
                        ))
                .requiresChildren("stripped_log")
                //.addRecipe(modRes("crafting/oak_axle"))
                .dropSelf()
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();

        this.addEntry(AXLE);

        WINDMILL = SimpleEntrySet.builder(WoodType.class, "windmill",
                        () -> getModBlock("oak_windmill").get(),
                        () -> VanillaWoodTypes.OAK,
                        w -> new WindmillBlock(
                                ExtendedProperties.of()
                                        .strength(9f)
                                        .noOcclusion()
                                        .blockEntity(TFCBlockEntities.WINDMILL)
                                        .ticks(WindmillBlockEntity::serverTick, WindmillBlockEntity::clientTick),
                                () -> (AxleBlock) AXLE.blocks.get(w)
                        ))
                .requiresFromMap(AXLE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setRenderType(RenderLayer.CUTOUT)
                .copyParentDrop()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(WINDMILL);

        BLADED_AXLE = SimpleEntrySet.builder(WoodType.class, "bladed_axle",
                        () -> getModBlock("oak_bladed_axle").get(), () -> VanillaWoodTypes.OAK,
                        w -> new
                                BladedAxleBlock(ExtendedProperties.of().noOcclusion().strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.BLADED_AXLE), () -> (AxleBlock) AXLE.blocks.get(w))
                )
                .requiresFromMap(AXLE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_bladed_axle"))
                .dropSelf()
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(BLADED_AXLE);

        ENCASED_AXLE = SimpleEntrySet.builder(WoodType.class, "encased_axle",
                        getModBlock("oak_encased_axle"), () -> VanillaWoodTypes.OAK,
                        w -> new
                                EncasedAxleBlock(ExtendedProperties.of().strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.ENCASED_AXLE))
                )
                .requiresFromMap(AXLE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_encased_axle"))
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(ENCASED_AXLE);

        CLUTCH = SimpleEntrySet.builder(WoodType.class, "clutch",
                        () -> getModBlock("oak_clutch").get(), () -> VanillaWoodTypes.OAK,
                        w -> new
                                ClutchBlock(ExtendedProperties.of().strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.CLUTCH), () -> (AxleBlock) AXLE.blocks.get(w))
                )
                .requiresFromMap(AXLE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_clutch"))
                .dropSelf()
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(CLUTCH);

        GEAR_BOX = SimpleEntrySet.builder(WoodType.class, "gear_box",
                        () -> getModBlock("oak_gear_box").get(), () -> VanillaWoodTypes.OAK,
                        w -> new
                                GearBoxBlock(ExtendedProperties.of().strength(2f).noOcclusion().blockEntity(TFCBlockEntities.GEAR_BOX), () -> (AxleBlock) AXLE.blocks.get(w))
                )
                .requiresFromMap(AXLE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_gear_box"))
                .dropSelf()
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(GEAR_BOX);

        //TODO - waterwheel not registering & check texture resLoc
        WATER_WHEEL = SimpleEntrySet.builder(WoodType.class, "water_wheel",
                        () -> getModBlock("oak_water_wheel").get(), () -> VanillaWoodTypes.OAK,
                        w -> new
                                WaterWheelBlock(ExtendedProperties.of().strength(9f).noOcclusion()
                                .blockEntity(TFCBlockEntities.WATER_WHEEL).ticks(WaterWheelBlockEntity::serverTick, WaterWheelBlockEntity::clientTick),
                                () -> (AxleBlock) AXLE.blocks.get(w),
                                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "textures/entity/" + FirmaCompat.MODID + "/" + w.getNamespace()+ "/" + w.getTypeName() + "_water_wheel.png"))
                )
                .requiresFromMap(AXLE.blocks)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                //.addRecipe(modRes("crafting/oak_water_wheel"))
                .addTexture(modRes("item/oak_water_wheel"), PaletteStrategies.MAIN_CHILD)
                .addTexture(modRes("entity/oak_water_wheel"), PaletteStrategies.MAIN_CHILD)
                .dropSelf()
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(WATER_WHEEL);
    }

    /*
    @Override
    //public boolean isEntryAlreadyRegistered(String entrySetId, String blockId, BlockType blockType, Registry<?> registry) {
    public boolean isEntryAlreadyRegistered(String entrySetId, ResourceLocation blockId, BlockType blockType, Registry<?> registry) {
        return false;
    }

     */

    @Override
    // RECIPES, TAGS
    //everycomp log tags formatted -> everycomp:[modid]/[woodType]_logs
    public void addDynamicServerResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> {
            for(var woodType : WoodTypeRegistry.INSTANCE){
                Item lumberItem = LUMBER.items.get(woodType);
                ResourceLocation logTag = getATagOrCreateANew("logs", "caps", woodType, sink, manager);
                ResourceLocation placedFeatureTag = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "tags/worldgen/placed_feature/twig_patches");

                if(lumberItem != null){
                    generateToolRackRecipe(sink, woodType, null);
                    generateLoomRecipe(sink, woodType, null);
                    generateSluiceRecipe(sink, woodType, null);
                    generateBarrelRecipe(sink, woodType, null);

                    if(woodType.getChild("planks") != null){
                        generatePlanksRecipe(sink, woodType, null);
                        generateToolItemRecipe(sink, woodType.planks.asItem(), "tfc:saws", lumberItem, 4, "from_planks");
                        UtilityTag.createAndAddCustomTags(modRes("remove_from_crafting"), sink, woodType.planks.asItem());
                        generateShelfRecipe(sink, woodType, null);
                        if(woodType.getChild("fence") != null){
                            generateFenceRecipe(sink, woodType, "fence", null);
                            UtilityTag.createAndAddCustomTags(modRes("remove_from_crafting"), sink, woodType.getItemOfThis("fence"));
                        }
                        if(woodType.getChild("fence_gate") != null){
                            generateFenceGateRecipe(sink, woodType, null);
                            UtilityTag.createAndAddCustomTags(modRes("remove_from_crafting"), sink, woodType.getItemOfThis("fence_gate"));
                        }
                        if(woodType.getChild("log") != null){
                            generateSewingTableRecipe(sink, woodType, null);
                        }
                        if(woodType.getChild("slab") != null){
                            generateScribingTableRecipe(sink, woodType, null);
                            generateToolItemRecipe(sink, woodType.getItemOfThis("slab"), "tfc:saws", lumberItem, 2, "from_slab");

                        }
                        if(woodType.getChild("stairs") != null) {
                            generateToolItemRecipe(sink, woodType.getItemOfThis("stairs"), "tfc:saws", lumberItem, 3, "from_stairs");
                        }
                    }
                    if(woodType.getChild("log") != null){
                        generateSupportRecipe(sink, logTag, "tfc:saws", Utils.getID(SUPPORT.items.get(woodType)).getPath(), 8, null);
                        generateToolTagRecipe(sink, logTag, "tfc:saws", lumberItem, 8, null);
                        generateFenceRecipe(sink, woodType, "log_fence", null);
                        supportData(sink, woodType);
                    }
                    if(woodType.getChild("door") != null){
                        generateDoorRecipe(sink, woodType, null);
                        UtilityTag.createAndAddCustomTags(modRes("remove_from_crafting"), sink, woodType.getItemOfThis("door"));
                    }
                    if(woodType.getChild("trapdoor") != null){
                        generateTrapdoorRecipe(sink, woodType, null);
                        UtilityTag.createAndAddCustomTags(modRes("remove_from_crafting"), sink, woodType.getItemOfThis("trapdoor"));
                    }
                    if(woodType.getChild("pressure_plate") != null){
                        generatePressurePlateRecipe(sink, woodType, null);
                        UtilityTag.createAndAddCustomTags(modRes("remove_from_crafting"), sink, woodType.getItemOfThis("pressure_plate"));
                    }
                    if(woodType.getChild("sign") != null){
                        generateSignRecipe(sink, woodType, null);
                        UtilityTag.createAndAddCustomTags(modRes("remove_from_crafting"), sink, woodType.getItemOfThis("sign"));
                    }
                    if(woodType.getChild("hanging_sign") != null){
                        generateHangingSignRecipe(sink, woodType, null);
                        UtilityTag.createAndAddCustomTags(modRes("remove_from_crafting"), sink, woodType.getItemOfThis("hanging_sign"));
                    }
                }
                if(AXLE.blocks.get(woodType) != null){
                    generateAxleRecipe(sink, woodType, null);
                    generateBladedAxleRecipe(sink, woodType, null);
                    generateEncasedAxleRecipe(sink, woodType, null);
                    generateClutchRecipe(sink, woodType, null);
                    generateGearBoxRecipe(sink, woodType, null);
                    generateWaterWheelRecipe(sink, woodType, null);
                }

                if(TWIG.blocks.get(woodType) != null){
                    //placed feature
                    generateTwigPatchPlacedFeature(sink, woodType, logTag.toString());
                    generateTwigPlacedFeature(sink, woodType);
                    //configured feature
                    generateTwigPatchConfiguredFeature(sink, woodType, 8, 5, 3);
                    generateTwigConfiguredFeature(sink, woodType);
                }
                //TFC data
                if(woodType.canBurn()){
                    if(!Objects.equals(woodType.getNamespace(), "assets/minecraft")
                            && !Objects.equals(woodType.getNamespace(), "tfc")
                            && !Objects.equals(woodType.getNamespace(), "afc")){
                        fuelData(woodType, sink, manager);
                    }
                }
            }
            //Create a single tag JSON with all placed feature references
            JsonObject tagJson = new JsonObject();

            JsonArray valuesArray = new JsonArray();

            for(WoodType wood: WoodTypeRegistry.INSTANCE){
                if(TWIG.blocks.get(wood) != null){
                    String twigPath = Utils.getID(TWIG.blocks.get(wood)).getPath();

                    String twigPatchFeature = "twig/" + twigPath + "_patch";
                    String featurePath = FirmaCompat.MODID + ":" + twigPatchFeature;
                    valuesArray.add(featurePath);
                }
            }

            tagJson.addProperty("replace", false);
            tagJson.add("values", valuesArray);

            // Write the tag file
            ResourceLocation tagPath = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "tags/worldgen/placed_feature/woodgood_twig_patches.json"  // or whatever name you prefer
            );

            sink.addJson(tagPath, tagJson, ResType.GENERIC);
            FirmaCompat.LOGGER.info("Generated placed feature tag: {}", tagPath);
        });
    }

    //Recipe JSON
    public void generatePlanksRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        String childKey = "planks";

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String doorItemPath = Utils.getID(Objects.requireNonNull(wood.getChild(childKey))).getPath();
        String doorNamespace = Utils.getID(Objects.requireNonNull(wood.getChild(childKey))).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key: L = lumber
        JsonObject key = new JsonObject();
        JsonObject lumberKey = new JsonObject();
        lumberKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", lumberKey);
        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("LL");
        pattern.add("LL");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("item",  doorNamespace + ":" + doorItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + FirmaCompat.MODID + "/" + doorNamespace + "/" + doorItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateDoorRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        String childKey = "door";

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String doorItemPath = Utils.getID(Objects.requireNonNull(wood.getChild(childKey))).getPath();
        String doorNamespace = Utils.getID(Objects.requireNonNull(wood.getChild(childKey))).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key: L = lumber
        JsonObject key = new JsonObject();
        JsonObject lumberKey = new JsonObject();
        lumberKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", lumberKey);
        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("LL");
        pattern.add("LL");
        pattern.add("LL");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("item",  doorNamespace + ":" + doorItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + FirmaCompat.MODID + "/" + doorNamespace + "/" + doorItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateTrapdoorRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        String childKey = "trapdoor";

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String doorItemPath = Utils.getID(Objects.requireNonNull(wood.getChild(childKey))).getPath();
        String doorNamespace = Utils.getID(Objects.requireNonNull(wood.getChild(childKey))).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key: L = lumber
        JsonObject key = new JsonObject();
        JsonObject lumberKey = new JsonObject();
        lumberKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", lumberKey);
        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("LLL");
        pattern.add("LLL");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 2);
        result.addProperty("item",  doorNamespace + ":" + doorItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + FirmaCompat.MODID + "/" + doorNamespace + "/" + doorItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generatePressurePlateRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        String childKey = "pressure_plate";

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String doorItemPath = Utils.getID(Objects.requireNonNull(wood.getChild(childKey))).getPath();
        String doorNamespace = Utils.getID(Objects.requireNonNull(wood.getChild(childKey))).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key: L = lumber
        JsonObject key = new JsonObject();
        JsonObject lumberKey = new JsonObject();
        lumberKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", lumberKey);
        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("LL");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("item",  doorNamespace + ":" + doorItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + FirmaCompat.MODID + "/" + doorNamespace + "/" + doorItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateFenceRecipe(
            ResourceSink sink,
            WoodType wood,
            String type,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);

        String lumberPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String outputPath = null;
        String outputNamespace = null;
        String connectorNamespace = null;
        String connectorPath = null;

        if(Objects.equals(type, "fence")){
            connectorPath = Utils.getID(wood.planks).getPath();
            connectorNamespace = Utils.getID(wood.planks).getNamespace();

            outputPath = Utils.getID(Objects.requireNonNull(wood.getChild(type))).getPath();
            outputNamespace = Utils.getID(Objects.requireNonNull(wood.getChild(type))).getNamespace();
        } else if (Objects.equals(type, "log_fence")){
            connectorPath = Utils.getID(wood.log).getPath();
            connectorNamespace = Utils.getID(wood.log).getNamespace();

            outputPath = Utils.getID(LOG_FENCE.blocks.get(wood)).getPath();
            outputNamespace = Utils.getID(LOG_FENCE.blocks.get(wood)).getNamespace();
        } else {
            connectorPath = "";
            connectorNamespace = "";
            outputPath = "";
            outputNamespace = "";
        }

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject postKey = new JsonObject();
        postKey.addProperty("item", lumberNamespace + ":" + lumberPath);
        key.add("L", postKey);

        JsonObject connectorKey = new JsonObject();
        connectorKey.addProperty("item", connectorNamespace + ":" + connectorPath);
        key.add("P", connectorKey);

        recipe.add("key", key);

        // Fixed pattern for fences (PLP × 2)
        JsonArray pattern = new JsonArray();
        pattern.add("PLP");
        pattern.add("PLP");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 8);
        result.addProperty("item",  outputNamespace + ":" + outputPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = null;

        if(type.equals("fence")){
            recipePath = "crafting/" + FirmaCompat.MODID + "/" + outputNamespace + "/" + outputPath;
        } else if(type.equals("log_fence")){
            recipePath = "crafting/" + outputPath;
        }

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateFenceGateRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);

        String lumberPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String outputPath = Utils.getID(Objects.requireNonNull(wood.getChild("fence_gate"))).getPath();
        String outputNamespace = Utils.getID(Objects.requireNonNull(wood.getChild("fence_gate"))).getNamespace();

        String connectorNamespace = Utils.getID(wood.planks).getNamespace();
        String connectorPath = Utils.getID(wood.planks).getPath();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject postKey = new JsonObject();
        postKey.addProperty("item", lumberNamespace + ":" + lumberPath);
        key.add("L", postKey);

        JsonObject connectorKey = new JsonObject();
        connectorKey.addProperty("item", connectorNamespace + ":" + connectorPath);
        key.add("P", connectorKey);

        recipe.add("key", key);

        // Fixed pattern for fences (PLP × 2)
        JsonArray pattern = new JsonArray();
        pattern.add("LPL");
        pattern.add("LPL");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 2);
        result.addProperty("item",  outputNamespace + ":" + outputPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + FirmaCompat.MODID + "/" + outputNamespace + "/" + outputPath;

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateToolRackRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        Item outputItem = TOOL_RACK.items.get(wood);

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key: L = lumber
        JsonObject key = new JsonObject();
        JsonObject lumberKey = new JsonObject();
        lumberKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", lumberKey);
        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("LLL");
        pattern.add("   ");
        pattern.add("LLL");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateLoomRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        Item outputItem = LOOM.items.get(wood);

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", logKey);

        JsonObject sawKey = new JsonObject();
        sawKey.addProperty("tag", "forge:rods/wooden");
        key.add("S", sawKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("LLL");
        pattern.add("LSL");
        pattern.add("L L");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateSluiceRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        Item outputItem = SLUICE.items.get(wood);

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", logKey);

        JsonObject sawKey = new JsonObject();
        sawKey.addProperty("tag", "forge:rods/wooden");
        key.add("S", sawKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("  S");
        pattern.add(" SL");
        pattern.add("SLL");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateBarrelRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        Item outputItem = BARREL.items.get(wood);

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", logKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("L L");
        pattern.add("L L");
        pattern.add("LLL");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateShelfRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        Item planksItem = wood.planks.asItem();
        Item outputItem = SHELF.items.get(wood);

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String plankItemPath = Utils.getID(Objects.requireNonNull(planksItem)).getPath();
        String plankItemNamespace = Utils.getID(Objects.requireNonNull(planksItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", logKey);

        JsonObject sawKey = new JsonObject();
        sawKey.addProperty("tag", "forge:rods/wooden");
        key.add("S", sawKey);

        JsonObject planksKey = new JsonObject();
        planksKey.addProperty("item", plankItemNamespace + ":" + plankItemPath);
        key.add("X", planksKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("XXX");
        pattern.add("L L");
        pattern.add("S S");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 2);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateAxleRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item strippedLogItem = wood.getItemOfThis("stripped_log");
        Item outputItem = AXLE.items.get(wood);

        String lumberItemPath = Utils.getID(Objects.requireNonNull(strippedLogItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(strippedLogItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", logKey);

        JsonObject sawKey = new JsonObject();
        sawKey.addProperty("item", TFCItems.GLUE.get().toString());
        key.add("S", sawKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("LSL");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 4);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateBladedAxleRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = AXLE.items.get(wood);
        Item outputItem = BLADED_AXLE.items.get(wood);

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", logKey);

        JsonObject sawKey = new JsonObject();
        sawKey.addProperty("item", TFCItems.METAL_ITEMS.get(Metal.Default.STEEL).get(Metal.ItemType.INGOT).get().toString());
        key.add("S", sawKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("LS ");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateEncasedAxleRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        Item axleItem = AXLE.items.get(wood);
        Item outputItem = ENCASED_AXLE.items.get(wood);

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String strippedLogPath = Utils.getID(Objects.requireNonNull(wood.getChild("stripped_log"))).getPath();
        String strippedLogNamespace = Utils.getID(Objects.requireNonNull(wood.getChild("stripped_log"))).getNamespace();

        String axlePath = Utils.getID(Objects.requireNonNull(axleItem)).getPath();
        String axleNamespace = Utils.getID(Objects.requireNonNull(axleItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", logKey);

        JsonObject axleKey = new JsonObject();
        axleKey.addProperty("item", axleNamespace + ":" + axlePath);
        key.add("S", axleKey);

        JsonObject strippedKey = new JsonObject();
        strippedKey.addProperty("item", strippedLogNamespace + ":" + strippedLogPath);
        key.add("X", strippedKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add(" X ");
        pattern.add("LSL");
        pattern.add(" X ");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 4);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }


    public void generateClutchRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        Item axleItem = AXLE.items.get(wood);
        Item outputItem = CLUTCH.items.get(wood);

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String strippedLogPath = Utils.getID(Objects.requireNonNull(wood.getChild("stripped_log"))).getPath();
        String strippedLogNamespace = Utils.getID(Objects.requireNonNull(wood.getChild("stripped_log"))).getNamespace();

        String axlePath = Utils.getID(Objects.requireNonNull(axleItem)).getPath();
        String axleNamespace = Utils.getID(Objects.requireNonNull(axleItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", logKey);

        JsonObject axleKey = new JsonObject();
        axleKey.addProperty("item", axleNamespace + ":" + axlePath);
        key.add("S", axleKey);

        JsonObject strippedKey = new JsonObject();
        strippedKey.addProperty("item", strippedLogNamespace + ":" + strippedLogPath);
        key.add("X", strippedKey);

        JsonObject redstoneKey = new JsonObject();
        redstoneKey.addProperty("tag", "forge:dusts/redstone");
        key.add("R", redstoneKey);

        JsonObject brassMechKey = new JsonObject();
        brassMechKey.addProperty("item", TFCItems.BRASS_MECHANISMS.get().toString());
        key.add("B", brassMechKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("LXL");
        pattern.add("BSR");
        pattern.add("LXL");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 2);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateGearBoxRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        Item outputItem = GEAR_BOX.items.get(wood);

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", logKey);

        JsonObject brassMechKey = new JsonObject();
        brassMechKey.addProperty("item", TFCItems.BRASS_MECHANISMS.get().toString());
        key.add("B", brassMechKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add(" L ");
        pattern.add("LBL");
        pattern.add(" L ");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 2);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateScribingTableRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item planksItem = wood.planks.asItem();
        Item slabItem = wood.getItemOfThis("slab");
        Item outputItem = SCRIBING_TABLE.items.get(wood);

        String plankItemPath = Utils.getID(Objects.requireNonNull(planksItem)).getPath();
        String plankItemNamespace = Utils.getID(Objects.requireNonNull(planksItem)).getNamespace();

        String slabItemPath = Utils.getID(Objects.requireNonNull(slabItem)).getPath();
        String slabItemNamespace = Utils.getID(Objects.requireNonNull(slabItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject plankKey = new JsonObject();
        plankKey.addProperty("item", plankItemNamespace + ":" + plankItemPath);
        key.add("P", plankKey);

        JsonObject slabKey = new JsonObject();
        slabKey.addProperty("item", slabItemNamespace + ":" + slabItemPath);
        key.add("S", slabKey);

        JsonObject featherKey = new JsonObject();
        featherKey.addProperty("item", Items.FEATHER.toString());
        key.add("F", featherKey);

        JsonObject inkKey = new JsonObject();
        inkKey.addProperty("item", Items.BLACK_DYE.toString());
        key.add("I", inkKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("F I");
        pattern.add("SSS");
        pattern.add("P P");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 2);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateSewingTableRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item planksItem = wood.planks.asItem();
        Item slabItem = wood.log.asItem();
        Item outputItem = SEWING_TABLE.items.get(wood);

        String plankItemPath = Utils.getID(Objects.requireNonNull(planksItem)).getPath();
        String plankItemNamespace = Utils.getID(Objects.requireNonNull(planksItem)).getNamespace();

        String slabItemPath = Utils.getID(Objects.requireNonNull(slabItem)).getPath();
        String slabItemNamespace = Utils.getID(Objects.requireNonNull(slabItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject plankKey = new JsonObject();
        plankKey.addProperty("item", plankItemNamespace + ":" + plankItemPath);
        key.add("P", plankKey);

        JsonObject slabKey = new JsonObject();
        slabKey.addProperty("item", slabItemNamespace + ":" + slabItemPath);
        key.add("S", slabKey);

        JsonObject featherKey = new JsonObject();
        featherKey.addProperty("item", Items.LEATHER.toString());
        key.add("F", featherKey);

        JsonObject inkKey = new JsonObject();
        inkKey.addProperty("tag", "forge:shears");
        key.add("I", inkKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add(" FI");
        pattern.add("PPP");
        pattern.add("S S");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 2);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateWaterWheelRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        Item axleItem = AXLE.items.get(wood);
        Item outputItem = WATER_WHEEL.items.get(wood);

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        String plankPath = Utils.getID(Objects.requireNonNull(wood.getChild("planks"))).getPath();
        String plankNamespace = Utils.getID(Objects.requireNonNull(wood.getChild("planks"))).getNamespace();

        String axlePath = Utils.getID(Objects.requireNonNull(axleItem)).getPath();
        String axleNamespace = Utils.getID(Objects.requireNonNull(axleItem)).getNamespace();

        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", logKey);

        JsonObject axleKey = new JsonObject();
        axleKey.addProperty("item", axleNamespace + ":" + axlePath);
        key.add("S", axleKey);

        JsonObject plankKey = new JsonObject();
        plankKey.addProperty("item", plankNamespace + ":" + plankPath);
        key.add("X", plankKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("LXL");
        pattern.add("XSX");
        pattern.add("LXL");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 1);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + outputItemPath;  // e.g. crafting/acacia_door

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateSignRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        Item outputItem = wood.getItemOfThis("sign");

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        assert outputItem != null;
        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", logKey);

        JsonObject axleKey = new JsonObject();
        axleKey.addProperty("tag", "forge:rods/wooden");
        key.add("S", axleKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("LLL");
        pattern.add("LLL");
        pattern.add(" S ");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 3);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + FirmaCompat.MODID + "/" + outputItemNamespace + "/" + outputItemPath;

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateHangingSignRecipe(
            ResourceSink sink,
            WoodType wood,
            @Nullable String suffix
    ) {
        Item lumberItem = LUMBER.items.get(wood);
        Item outputItem = wood.getItemOfThis("hanging_sign");

        String lumberItemPath = Utils.getID(Objects.requireNonNull(lumberItem)).getPath();
        String lumberNamespace = Utils.getID(Objects.requireNonNull(lumberItem)).getNamespace();

        assert outputItem != null;
        String outputItemPath = Utils.getID(outputItem).getPath();
        String outputItemNamespace = Utils.getID(outputItem).getNamespace();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("item", lumberNamespace + ":" + lumberItemPath);
        key.add("L", logKey);

        JsonObject axleKey = new JsonObject();
        axleKey.addProperty("tag", "firma_compat:chains");
        key.add("S", axleKey);

        recipe.add("key", key);

        // Pattern: door (6 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("S S");
        pattern.add("LLL");
        pattern.add("LLL");
        recipe.add("pattern", pattern);

        // Result: 1 door (vanilla wood type)
        JsonObject result = new JsonObject();
        result.addProperty("count", 3);
        result.addProperty("item",  outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe path
        String recipePath = "crafting/" + FirmaCompat.MODID + "/" + outputItemNamespace + "/" + outputItemPath;

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += ("_" + suffix);
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, recipePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateSupportRecipe(
            ResourceSink sink,
            ResourceLocation tag,
            String toolTag,
            String outputItem,
            int count,
            @Nullable String suffix
    ) {
        if (count < 1) {
            count = 1;
            EveryCompat.LOGGER.warn("Invalid count {} for support recipe → {}, clamped to 1", count, outputItem);
        }

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "tfc:damage_inputs_shapeless_crafting");

        // Inner vanilla shapeless recipe
        JsonObject innerRecipe = new JsonObject();
        innerRecipe.addProperty("type", "minecraft:crafting_shapeless");

        JsonArray ingredients = new JsonArray();

        // First log
        JsonObject log1 = new JsonObject();
        log1.addProperty("tag", String.valueOf(tag));
        ingredients.add(log1);

        // Second log
        JsonObject log2 = new JsonObject();
        log2.addProperty("tag", String.valueOf(tag));
        ingredients.add(log2);

        // Saw
        JsonObject saw = new JsonObject();
        saw.addProperty("tag", toolTag);
        ingredients.add(saw);

        innerRecipe.add("ingredients", ingredients);

        // Result
        JsonObject result = new JsonObject();
        result.addProperty("item", FirmaCompat.MODID + ":" + outputItem);
        result.addProperty("count", count);
        innerRecipe.add("result", result);

        // Wrap the inner recipe
        recipe.add("recipe", innerRecipe);

        // Build recipe ResourceLocation (unchanged from your original)
        ResourceLocation outLoc = ResourceLocation.parse(outputItem);
        String basePath = "crafting/" + outLoc.getPath();  // e.g. supports/acacia_support

        if (suffix != null && !suffix.isEmpty()) {
            basePath += suffix;
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, basePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    /*
    public void generateSupportRecipe(
            ResourceSink sink,
            ResourceLocation tag,
            String toolTag,
            String outputItem,
            int count,
            @Nullable String suffix
    ) {
        if (count < 1) {
            count = 1;
            EveryCompat.LOGGER.warn("Invalid count {} for support recipe → {}, clamped to 1", count, outputItem);
        }

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "tfc:advanced_shaped_crafting");

        // Column width hint (matches your example)
        recipe.addProperty("input_column", 1);

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject logKey = new JsonObject();
        logKey.addProperty("tag", String.valueOf(tag));
        key.add("L", logKey);

        JsonObject sawKey = new JsonObject();
        sawKey.addProperty("tag", toolTag);
        key.add("S", sawKey);

        recipe.add("key", key);

        // Pattern (fixed 2x2 layout from your example)
        JsonArray pattern = new JsonArray();
        pattern.add("LS");
        pattern.add("L ");
        recipe.add("pattern", pattern);

        // Remainder: damage the tool (saw)
        JsonObject remainder = new JsonObject();
        JsonArray modifiers = new JsonArray();
        JsonObject damage = new JsonObject();
        damage.addProperty("type", "tfc:damage_crafting_remainder");
        modifiers.add(damage);
        remainder.add("modifiers", modifiers);
        recipe.add("remainder", remainder);

        // Result
        JsonObject result = new JsonObject();
        result.addProperty("count", count);
        result.addProperty("item", FirmaCompat.MODID + ":" + outputItem);
        recipe.add("result", result);

        // Build recipe ResourceLocation based on output item namespace + path
        ResourceLocation outLoc = ResourceLocation.parse(outputItem);
        String basePath = "crafting/" + outLoc.getPath();  // e.g. supports/acacia_support

        if (suffix != null && !suffix.isEmpty()) {
            basePath += suffix;
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, basePath);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

     */

    public void generateToolItemRecipe(
            ResourceSink sink,
            Item inputItem,
            String toolTag,               // e.g. "tfc:saws"
            Item ouptutItem,
            int count,
            @Nullable String recipeSuffix
    ) {
        String inputItemPath = Utils.getID(Objects.requireNonNull(inputItem)).getPath();
        String inputItemNamespace = Utils.getID(Objects.requireNonNull(inputItem)).getNamespace();

        String outputItemPath = Utils.getID(Objects.requireNonNull(ouptutItem)).getPath();
        String outputItemNamespace = Utils.getID(Objects.requireNonNull(ouptutItem)).getNamespace();

        if (count < 1) {
            count = 1;
            EveryCompat.LOGGER.warn("Invalid result count {} → clamped to 1 for {}",
                    count, outputItemPath);
        }

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "tfc:advanced_shapeless_crafting");

        // Ingredients array (required for shapeless)
        JsonArray ingredients = new JsonArray();

        // The tool is marked as primary_ingredient
        JsonObject toolIngredient = new JsonObject();
        toolIngredient.addProperty("tag", toolTag);
        ingredients.add(toolIngredient);

        // The actual material being processed
        JsonObject materialIngredient = new JsonObject();

        materialIngredient.addProperty("item", inputItemNamespace + ":" + inputItemPath);

        ingredients.add(materialIngredient);

        recipe.add("ingredients", ingredients);

        // Mark which one is the primary (tool)
        JsonObject primary = new JsonObject();
        primary.addProperty("tag", toolTag);
        recipe.add("primary_ingredient", primary);

        // Remainder → damage the tool
        JsonObject remainder = new JsonObject();
        JsonArray modifiers = new JsonArray();
        JsonObject damageModifier = new JsonObject();
        damageModifier.addProperty("type", "tfc:damage_crafting_remainder");
        modifiers.add(damageModifier);
        remainder.add("modifiers", modifiers);
        recipe.add("remainder", remainder);

        // Result
        JsonObject result = new JsonObject();
        result.addProperty("count", count);

        result.addProperty("item", outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe ID
        String path = "crafting/" + outputItemPath;
        if (recipeSuffix != null && !recipeSuffix.isEmpty()) {
            path += "_" + recipeSuffix;
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, path);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateToolTagRecipe(
            ResourceSink sink,
            ResourceLocation inputTag,
            String toolTag,               // e.g. "tfc:saws"
            Item ouptutItem,
            int count,
            @Nullable String recipeSuffix
    ) {
        String outputItemPath = Utils.getID(Objects.requireNonNull(ouptutItem)).getPath();
        String outputItemNamespace = Utils.getID(Objects.requireNonNull(ouptutItem)).getNamespace();

        if (count < 1) {
            count = 1;
            EveryCompat.LOGGER.warn("Invalid result count {} → clamped to 1 for {}",
                    count, outputItemPath);
        }

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "tfc:advanced_shapeless_crafting");

        // Ingredients array (required for shapeless)
        JsonArray ingredients = new JsonArray();

        // The tool is marked as primary_ingredient
        JsonObject toolIngredient = new JsonObject();
        toolIngredient.addProperty("tag", toolTag);
        ingredients.add(toolIngredient);

        // The actual material being processed
        JsonObject materialIngredient = new JsonObject();

        materialIngredient.addProperty("tag", inputTag.toString());

        ingredients.add(materialIngredient);

        recipe.add("ingredients", ingredients);

        // Mark which one is the primary (tool)
        JsonObject primary = new JsonObject();
        primary.addProperty("tag", toolTag);
        recipe.add("primary_ingredient", primary);

        // Remainder → damage the tool
        JsonObject remainder = new JsonObject();
        JsonArray modifiers = new JsonArray();
        JsonObject damageModifier = new JsonObject();
        damageModifier.addProperty("type", "tfc:damage_crafting_remainder");
        modifiers.add(damageModifier);
        remainder.add("modifiers", modifiers);
        recipe.add("remainder", remainder);

        // Result
        JsonObject result = new JsonObject();
        result.addProperty("count", count);

        result.addProperty("item", outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe ID
        String path = "crafting/" + outputItemPath;
        if (recipeSuffix != null && !recipeSuffix.isEmpty()) {
            path += recipeSuffix;
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, path);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    //TFC Data files

    private ResourceLocation getPlanksTextureId(WoodType wood) {
        String namespace = wood.getNamespace();
        String typeName = wood.getTypeName();

        if ("assets/minecraft".equals(namespace)) {
            return ResourceLocation.fromNamespaceAndPath("assets/minecraft", "block/" + typeName + "_planks");
        } else if ("tfc".equals(namespace)) {
            return ResourceLocation.fromNamespaceAndPath("tfc", "block/wood/planks/" + typeName);
        } else {
            // Fallback for other mods — adjust if you support more
            FirmaCompat.LOGGER.warn("Unknown wood namespace for plank texture: {}", namespace);
            return ResourceLocation.fromNamespaceAndPath(namespace, "block/" + typeName + "_planks");
        }
    }

    public void fuelData(WoodType woodType, ResourceSink sink, ResourceManager manager) {

        ResourceLocation dataLoc = modRes("tfc/fuel/oak_logs.json");

        try (InputStream dataStream = manager.getResource(dataLoc)
                .orElseThrow(() -> new FileNotFoundException("File not found @ " + dataLoc)).open()) {

            JsonObject fuelData = RPUtils.deserializeJson(dataStream);

            // Editing the recipe
            fuelData.getAsJsonObject("ingredient")
                    .addProperty("tag", getATagOrCreateANew("logs", "caps", woodType, sink, manager).toString());

            // Adding to resources
            sink.addJson(
                    modRes("tfc/fuel/" + woodType.getTypeName() + "_logs.json"),
                    fuelData,
                    ResType.GENERIC
            );
        }
        catch (IOException e) {
            EveryCompat.LOGGER.error("Failed to generate the tfc data - fuel for {} : {}", woodType.getId(), e);
        }
    }

    public void supportData(ResourceSink sink, WoodType wood) {
        JsonObject config = new JsonObject();
        JsonArray ingredients = new JsonArray();

        String woodPath = Utils.getID(HORIZONTAL_SUPPORT.blocks.get(wood)).getPath();
        String woodNamespace = Utils.getID(HORIZONTAL_SUPPORT.blocks.get(wood)).getNamespace();

        String ingredientPath = woodNamespace + ":" + woodPath;

        ingredients.add(ingredientPath);

        // Only add if we actually have ingredients (avoid empty array)
        if (!ingredients.isEmpty()) {
            config.add("ingredient", ingredients);
        } else {
            EveryCompat.LOGGER.warn("No wood types found for horizontal support config");
        }

        // Fixed structural values (from your example)
        config.addProperty("support_down", 2);
        config.addProperty("support_horizontal", 4);
        config.addProperty("support_up", 2);

        // Destination path – adjust namespace/folder as needed
        ResourceLocation configLocation = modRes("tfc/support/compat_horizontal_support_beam.json");

        sink.addJson(configLocation, config, ResType.GENERIC);
    }

    //Placed Feature - Patch
    public void generateTwigPatchPlacedFeature(
            ResourceSink sink,
            WoodType wood,
            String logTag
    ) {
        String twigNamespace = Utils.getID(TWIG.blocks.get(wood)).getNamespace();
        String twigPath = Utils.getID(TWIG.blocks.get(wood)).getPath();

        // Reference to the configured patch feature
        ResourceLocation patchFeature = ResourceLocation.fromNamespaceAndPath(
                twigNamespace, "twig/" + twigPath + "_patch"
        );

        JsonObject json = new JsonObject();
        json.addProperty("feature", patchFeature.toString());

        JsonArray placement = new JsonArray();

        // 1. Count modifier (how many attempts per chunk)
        JsonObject count = new JsonObject();
        count.addProperty("type", "minecraft:count");
        count.addProperty("count", 16);
        placement.add(count);

        // 2. In square (spread within chunk)
        JsonObject inSquare = new JsonObject();
        inSquare.addProperty("type", "minecraft:in_square");
        placement.add(inSquare);

        // 3. Heightmap (place on top of terrain, ignoring leaves)
        JsonObject heightmap = new JsonObject();
        heightmap.addProperty("type", "minecraft:heightmap");
        heightmap.addProperty("heightmap", "MOTION_BLOCKING_NO_LEAVES");
        placement.add(heightmap);

        // 4. Complex block predicate filter: must be adjacent (including diagonal) to a log
        JsonObject blockFilter = new JsonObject();
        blockFilter.addProperty("type", "minecraft:block_predicate_filter");

        JsonObject predicate = new JsonObject();
        predicate.addProperty("type", "minecraft:any_of");

        JsonArray predicates = new JsonArray();

        // Helper to add a log-matching predicate with optional offset
        Consumer<int[]> addLogCheck = offset -> {
            JsonObject p = new JsonObject();
            p.addProperty("type", "minecraft:matching_block_tag");
            p.addProperty("tag", logTag);
            if (offset[0] != 0 || offset[1] != 0 || offset[2] != 0) {
                JsonArray off = new JsonArray();
                off.add(offset[0]);
                off.add(offset[1]);
                off.add(offset[2]);
                p.add("offset", off);
            }
            predicates.add(p);
        };

        // Center + 8 surrounding positions (including diagonals)
        addLogCheck.accept(new int[]{0, 0, 0});     // self (though usually not needed)
        addLogCheck.accept(new int[]{-1, 0, 0});
        addLogCheck.accept(new int[]{1, 0, 0});
        addLogCheck.accept(new int[]{0, 0, -1});
        addLogCheck.accept(new int[]{0, 0, 1});
        addLogCheck.accept(new int[]{1, 0, 1});
        addLogCheck.accept(new int[]{1, 0, -1});
        addLogCheck.accept(new int[]{-1, 0, 1});
        addLogCheck.accept(new int[]{-1, 0, -1});

        predicate.add("predicates", predicates);
        blockFilter.add("predicate", predicate);
        placement.add(blockFilter);

        // 5. Biome filter (required for placed features)
        JsonObject biome = new JsonObject();
        biome.addProperty("type", "minecraft:biome");
        placement.add(biome);

        json.add("placement", placement);

        // Path: placed_features/twig/acacia_patch.json
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(twigNamespace,
                "worldgen/placed_feature/twig/" + twigPath + "_patch.json");

        sink.addJson(id, json, ResType.GENERIC);
    }

    //Configured Feature - patch
    public void generateTwigPatchConfiguredFeature(
            ResourceSink sink,
            WoodType wood,
            int tries,              // default: 8
            int xzSpread,           // default: 5
            int ySpread             // default: 3
    ) {
        String twigNamespace = Utils.getID(TWIG.blocks.get(wood)).getNamespace();
        String twigPath = Utils.getID(TWIG.blocks.get(wood)).getPath();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:random_patch");

        JsonObject config = new JsonObject();
        config.addProperty("tries", tries);
        config.addProperty("xz_spread", xzSpread);
        config.addProperty("y_spread", ySpread);
        config.addProperty("feature", twigNamespace + ":twig/" + twigPath);

        recipe.add("config", config);

        // Typical path: placed_features/twig_patch/acacia.json or similar
        //String patchPath = "placed_features/twig_patch/" + wood.getTypeName();
        ResourceLocation patchId = ResourceLocation.fromNamespaceAndPath(
                twigNamespace, "twig/" + twigPath + "_patch"
        );

        sink.addJson(patchId, recipe, ResType.CONFIGURED_FEATURES);
    }

    //Placed Feature - single
    public void generateTwigPlacedFeature(
            ResourceSink sink,
            WoodType wood
    ) {
        String twigNamespace = Utils.getID(TWIG.blocks.get(wood)).getNamespace();
        String twigPath = Utils.getID(TWIG.blocks.get(wood)).getPath();
        // The actual block to place

        JsonObject json = new JsonObject();
        json.addProperty("feature",twigNamespace + ":twig/" + twigPath);

        JsonArray placement = new JsonArray();

        // 1. Must be on replaceable block
        JsonObject replaceableFilter = new JsonObject();
        replaceableFilter.addProperty("type", "minecraft:block_predicate_filter");

        JsonObject replaceablePredicate = new JsonObject();
        replaceablePredicate.addProperty("type", "minecraft:matching_block_tag");
        replaceablePredicate.addProperty("tag", "minecraft:replaceable");
        replaceableFilter.add("predicate", replaceablePredicate);
        placement.add(replaceableFilter);

        // 2. TFC survival check with fluid=empty
        JsonObject survivalFilter = new JsonObject();
        survivalFilter.addProperty("type", "block_predicate_filter");  // Note: no minecraft: prefix

        JsonObject survivalPredicate = new JsonObject();
        survivalPredicate.addProperty("type", "tfc:would_survive_with_fluid");

        JsonObject state = new JsonObject();
        state.addProperty("Name", twigNamespace + ":" + twigPath);

        JsonObject props = new JsonObject();
        props.addProperty("fluid", "empty");
        state.add("Properties", props);

        survivalPredicate.add("state", state);
        survivalFilter.add("predicate", survivalPredicate);
        placement.add(survivalFilter);

        json.add("placement", placement);

        // Path: configured_features/twig/acacia.json
        //String path = "configured_features/twig/" + wood.getTypeName();
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(twigNamespace,
                "worldgen/placed_feature/twig/" + twigPath + ".json");

        sink.addJson(id, json, ResType.GENERIC);
    }

    //Configured Feature - single
    public void generateTwigConfiguredFeature(
            ResourceSink sink,
            WoodType wood          // e.g. "twig/acacia" or just "acacia_twig"
    ) {
        String twigNamespace = Utils.getID(TWIG.blocks.get(wood)).getNamespace();
        String twigPath = Utils.getID(TWIG.blocks.get(wood)).getPath();

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "tfc:block_with_fluid");

        JsonObject config = new JsonObject();

        JsonObject toPlace = new JsonObject();
        toPlace.addProperty("type", "minecraft:simple_state_provider");

        JsonObject state = new JsonObject();
        state.addProperty("Name", twigNamespace + ":" + twigPath);

        JsonObject properties = new JsonObject();
        properties.addProperty("fluid", "empty");
        state.add("Properties", properties);

        toPlace.add("state", state);
        config.add("to_place", toPlace);

        recipe.add("config", config);

        ResourceLocation featureId = ResourceLocation.fromNamespaceAndPath(
                twigNamespace, "twig/" + twigPath
        );

        sink.addJson(featureId, recipe, ResType.CONFIGURED_FEATURES);
    }
}

