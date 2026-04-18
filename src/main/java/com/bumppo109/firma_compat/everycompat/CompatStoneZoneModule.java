package com.bumppo109.firma_compat.everycompat;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatOre;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.util.ModTags;
import com.bumppo109.firma_compat.worldgen.CompatSingleBlockVein;
import com.bumppo109.firma_compat.worldgen.CompatVein;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.rock.AqueductBlock;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.util.collections.IWeighted;
import net.dries007.tfc.util.collections.Weighted;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.ItemOnlyEntrySet;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.RenderLayer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.misc.UtilityTag;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.resources.textures.TextureImage;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.stone_zone.StoneZone;
import net.mehvahdjukaar.stone_zone.api.StoneZoneEntrySet;
import net.mehvahdjukaar.stone_zone.api.StoneZoneModule;
import net.mehvahdjukaar.stone_zone.api.set.VanillaRockChildKeys;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneType;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneTypeRegistry;
import net.mehvahdjukaar.stone_zone.api.set.stone.VanillaStoneChildKeys;
import net.mehvahdjukaar.stone_zone.api.set.stone.VanillaStoneTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.bumppo109.firma_compat.block.ModBlocks.GRADED_ORES;
import static com.bumppo109.firma_compat.block.ModBlocks.ORES;

public class CompatStoneZoneModule extends StoneZoneModule {

    public final SimpleEntrySet<StoneType, Block> LOOSE;
    public final SimpleEntrySet<StoneType, Block> LOOSE_COBBLE;
    public final SimpleEntrySet<StoneType, Block> HARDENED_COBBLE;
    public final SimpleEntrySet<StoneType, Block> HARDENED;

    public final ItemOnlyEntrySet<StoneType, Item> BRICK;
    public final SimpleEntrySet<StoneType, Block> AQUEDUCT;

    //public final ItemOnlyEntrySet<MudType, Item> MUD_BRICK;
    //public final SimpleEntrySet<MudType, Block> DRYING_MUD_BRICK;

    public final Map<String, SimpleEntrySet<StoneType, Block>> ORE_ENTRY_SETS = new HashMap<>();

    public CompatStoneZoneModule(){
        super(FirmaCompat.MODID, FirmaCompat.MODID);

        ResourceKey<CreativeModeTab> tab = CreativeModeTabs.BUILDING_BLOCKS;

        LOOSE = StoneZoneEntrySet.of(StoneType.class,"loose",
                        getModBlock("stone_loose"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new LooseRockBlock(BlockBehaviour.Properties.of().strength(0.05f, 0.0f).noCollission())
                )
                .requiresChildren(VanillaStoneChildKeys.STONE)
                .addTag(ResourceLocation.fromNamespaceAndPath("tfc", "compat_loose"), Registries.ITEM)
                .addTag(TFCTags.Items.METAMORPHIC_ITEMS, Registries.ITEM)
                .addTag(TFCTags.Blocks.CAN_BE_SNOW_PILED, Registries.BLOCK)
                .addTexture(modRes("item/stone_loose"))
                .copyParentDrop()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(LOOSE);

        LOOSE_COBBLE = StoneZoneEntrySet.of(StoneType.class,"cobble", "loose",
                        getModBlock("loose_stone_cobble"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(Blocks.COBBLESTONE))
                )
                .requiresFromMap(LOOSE.blocks)
                .addTag(TFCTags.Blocks.CAN_LANDSLIDE, Registries.BLOCK)
                .addTexture(modRes("block/loose_stone_cobble"), PaletteStrategies.MAIN_CHILD)
                .addRecipe(modRes("crafting/loose_stone_cobble"))
                .dropSelf()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(LOOSE_COBBLE);

        HARDENED_COBBLE = StoneZoneEntrySet.of(StoneType.class, "cobble","hardened",
                        getModBlock("hardened_andesite_cobble"), () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new Block(Utils.copyPropertySafe(Blocks.COBBLESTONE))
                )
                .requiresFromMap(LOOSE.blocks)
                .addTexture(modRes("block/loose_stone_cobble"), PaletteStrategies.MAIN_CHILD)
                .defaultRecipe()
                .dropSelf()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(HARDENED_COBBLE);

        HARDENED = StoneZoneEntrySet.of(StoneType.class,"hardened",
                        getModBlock("stone_hardened"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.stone))
                )
                .requiresFromMap(LOOSE.blocks)
                .requiresChildren(VanillaStoneChildKeys.STONE)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.BREAKS_WHEN_ISOLATED, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_TRIGGER_COLLAPSE, Registries.BLOCK)
                .addTag(TFCTags.Blocks.CAN_COLLAPSE, Registries.BLOCK)
                .noDrops()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(HARDENED);

        BRICK = ItemOnlyEntrySet.builder(StoneType.class, "brick",
                        getModItem("stone_brick"), () -> VanillaStoneTypes.STONE,
                        w -> new Item(new Item.Properties())
                )
                .requiresFromMap(LOOSE.blocks)
                .addTexture(modRes("item/stone_brick"), PaletteStrategies.MAIN_CHILD)
                .addTag(modRes("compat_brick"), Registries.ITEM)
                .excludeBlockTypes("tfc:.*")
                .setTabKey(tab)
                .build();
        this.addEntry(BRICK);

        AQUEDUCT = StoneZoneEntrySet.of(StoneType.class,"brick_aqueduct",
                        getModBlock("stone_brick_aqueduct"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new AqueductBlock((Utils.copyPropertySafe(stoneType.block)))
                )
                .requiresChildren(VanillaRockChildKeys.BRICKS)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ResourceLocation.fromNamespaceAndPath("tfc", "aqueducts"), Registries.BLOCK)
                .addRecipe(modRes("crafting/stone_brick_aqueduct"))
                .dropSelf()
                .excludeBlockTypes("tfc:.*")
                .setTabKey(tab)
                .build();
        this.addEntry(AQUEDUCT);

        for (CompatOre ore : CompatOre.values()) {
            if (ore.isGraded() || !ore.hasBlock()) continue;

            String oreName = ore.name().toLowerCase();

            SimpleEntrySet<StoneType, Block> ungradedSet = StoneZoneEntrySet.of(StoneType.class, oreName + "_ore",
                            getModBlock("stone_" + oreName + "_ore"), () -> VanillaStoneTypes.STONE,
                            stoneType -> new Block(Utils.copyPropertySafe(stoneType.block))
                    )
                    .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                    .addTag(TFCTags.Blocks.CAN_COLLAPSE, Registries.BLOCK)
                    .addTag(TFCTags.Blocks.CAN_START_COLLAPSE, Registries.BLOCK)
                    .addTag(TFCTags.Blocks.CAN_TRIGGER_COLLAPSE, Registries.BLOCK)
                    .copyParentDrop()
                    .setRenderType(RenderLayer.CUTOUT)
                    .setTabKey(tab)
                    .excludeBlockTypes("tfc:.*")
                    .build();

            this.addEntry(ungradedSet);
            ORE_ENTRY_SETS.put("ungraded_" + oreName, ungradedSet);
        }

        // 2. Graded ores (poor/normal/rich variants for each graded ore)
        for (CompatOre ore : CompatOre.values()) {
            if (!ore.isGraded() || !ore.hasBlock()) continue;

            String oreName = ore.name().toLowerCase();

            for (CompatOre.Grade grade : CompatOre.Grade.values()) {
                String gradeName = grade.name().toLowerCase();

                SimpleEntrySet<StoneType, Block> gradedSet = StoneZoneEntrySet.of(StoneType.class,oreName + "_ore", gradeName,
                                getModBlock(gradeName + "_stone_" + oreName + "_ore"), () -> VanillaStoneTypes.STONE,
                                stoneType -> new Block(Utils.copyPropertySafe(stoneType.block))
                        )
                        .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                        .addTag(TFCTags.Blocks.CAN_COLLAPSE, Registries.BLOCK)
                        .addTag(TFCTags.Blocks.CAN_START_COLLAPSE, Registries.BLOCK)
                        .addTag(TFCTags.Blocks.CAN_TRIGGER_COLLAPSE, Registries.BLOCK)
                        .copyParentDrop()
                        .setRenderType(RenderLayer.CUTOUT)
                        .setTabKey(tab)
                        .excludeBlockTypes("tfc:.*")
                        .build();

                this.addEntry(gradedSet);
                ORE_ENTRY_SETS.put(gradeName + "_" + oreName, gradedSet);
            }
        }

        /* TODO - pending mod with mud brick set
        MUD_BRICK = ItemOnlyEntrySet.builder(MudType.class, "brick",
                        getModItem("mud_brick"), () -> VanillaMudTypes.MUD,
                        w -> new Item(new Item.Properties())
                )
                .addTexture(modRes("item/mud_brick"), PaletteStrategies.MAIN_CHILD)
                .excludeBlockTypes("tfc:.*")
                .setTabKey(tab)
                .build();
        this.addEntry(MUD_BRICK);

        DRYING_MUD_BRICK = StoneZoneEntrySet.of(MudType.class,"bricks", "drying",
                        getModBlock("drying_mud_brick"), () -> VanillaMudTypes.MUD,
                        mudType -> new DryingBricksBlock(ExtendedProperties.of(MapColor.DIRT).noCollission().noOcclusion().instabreak().sound(SoundType.STEM).randomTicks()
                                .blockEntity(TFCBlockEntities.TICK_COUNTER), () -> MUD_BRICK.items.get(mudType))
                )
                .requiresFromMap(MUD_BRICK.items)
                .addTag(BlockTags.MINEABLE_WITH_SHOVEL, Registries.BLOCK)
                .addRecipe(modRes("crafting/drying_mud_bricks"))
                .copyParentDrop()
                .dropSelf()
                .excludeBlockTypes("tfc:.*")
                .setTabKey(tab)
                .build();
        this.addEntry(DRYING_MUD_BRICK);

         */
    }

    /*
    @Override
    public boolean isEntryAlreadyRegistered(String entrySetId, String blockId, BlockType blockType, Registry<?> registry) {
        return false;
    }
     */


    @Override
    public void addDynamicClientResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicClientResources(executor);

        executor.accept((manager, sink) -> {
            //Knapping texture
            LOOSE.blocks.forEach((stoneType, block) -> {
                if (stoneType == null) return;

                ResourceLocation rawResLoc = BuiltInRegistries.BLOCK.getKey(stoneType.block);
                ResourceLocation looseResLoc = BuiltInRegistries.BLOCK.getKey(LOOSE.blocks.get(stoneType));

                if (rawResLoc == null) return;

                String rawPath = rawResLoc.getPath();
                String loosePath = looseResLoc.getPath();
                String rawNamespace = rawResLoc.getNamespace();

                // Target path: tfc:gui/knapping/granite.png (or tfc:gui/knapping/rock/granite.png – see note below)
                ResourceLocation targetLoc = ResourceLocation.fromNamespaceAndPath(
                        "tfc",
                        "gui/knapping/" + loosePath             // ← crucial: add filename + extension
                );

                // Source texture: minecraft:block/granite.png (add .png if missing)
                ResourceLocation sourceLoc = ResourceLocation.fromNamespaceAndPath(
                        rawNamespace,
                        "block/" + rawPath                    // ← safer with explicit .png
                );

                try (TextureImage rawTexture = TextureImage.open(manager, sourceLoc)) {
                    // Only add if not already present (prevents overwrite / spam)
                    sink.addTextureIfNotPresent(manager, String.valueOf(targetLoc), () -> rawTexture);
                } catch (IOException e) {
                    EveryCompat.LOGGER.error("Failed to copy knapping texture for {} from {} : {}",
                            rawResLoc, sourceLoc, e.getMessage());
                }
            });
        });
    }


    @Override
    // RECIPES, TAGS
    //everycomp log tags formatted -> everycomp:[modid]/[woodType]_logs
    public void addDynamicServerResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> {
            for(StoneType stoneType : StoneTypeRegistry.INSTANCE){
                ResourceLocation rockTag = ResourceLocation.fromNamespaceAndPath(stoneType.getNamespace(), "stone_type/" + stoneType.getTypeName());
                UtilityTag.createAndAddCustomTags(rockTag, sink, stoneType.stone);

                if(LOOSE.items.get(stoneType) != null){
                    if(HARDENED_COBBLE.items.get(stoneType) != null){
                        generateBrickBlockRecipe(sink, LOOSE.items.get(stoneType).toString(), HARDENED_COBBLE.items.get(stoneType).toString(), 4, null);
                    }
                    if(BRICK.items.get(stoneType) != null){
                        if(stoneType.getChild(VanillaRockChildKeys.BRICKS) != null && stoneType.getChild(VanillaRockChildKeys.BUTTON) != null && stoneType.getChild(VanillaRockChildKeys.PRESSURE_PLATE) != null){
                            generateBrickRecipe(sink, LOOSE.items.get(stoneType), BRICK.items.get(stoneType), "c:tools/chisel", 1,null);
                        }
                        if(stoneType.getChild(VanillaRockChildKeys.BUTTON) != null){
                            generateBrickRecipe(sink, BRICK.items.get(stoneType), stoneType.getItemOfThis("button"), "c:tools/chisel", 1,null);
                            UtilityTag.createAndAddCustomTags(modRes("remove_from_crafting"), sink, stoneType.getItemOfThis("button"));
                        }
                        if(stoneType.getChild(VanillaRockChildKeys.PRESSURE_PLATE) != null){
                            generatePressurePlateFromBrickRecipe(sink, stoneType, BRICK.items.get(stoneType).asItem(), 1, null);
                            UtilityTag.createAndAddCustomTags(modRes("remove_from_crafting"), sink, stoneType.getItemOfThis("pressure_plate"));
                        }
                        if(stoneType.getChild(VanillaRockChildKeys.BRICKS) != null){
                            generateBrickBlockRecipe(sink, BRICK.items.get(stoneType).toString(), Utils.getID(Objects.requireNonNull(stoneType.getChild(VanillaRockChildKeys.BRICKS))).toString(), 4, null);
                            UtilityTag.createAndAddCustomTags(modRes("remove_from_crafting"), sink, stoneType.getItemOfThis("bricks"));
                        }
                    }
                }
            }

            HARDENED.blocks.forEach((stoneType, block) -> {
                if (stoneType == null) return;  // safety check
                //raw
                generateLootTableForStone(null, stoneType.stone, LOOSE.blocks.get(stoneType), sink, manager);
                UtilityTag.createAndAddCustomTags(ResourceLocation.fromNamespaceAndPath("tfc", "breaks_when_isolated"), sink, stoneType.stone);
                UtilityTag.createAndAddCustomTags(ResourceLocation.fromNamespaceAndPath("tfc", "can_collapse"), sink, stoneType.stone);
                UtilityTag.createAndAddCustomTags(ResourceLocation.fromNamespaceAndPath("tfc", "can_start_collapse"), sink, stoneType.stone);
                UtilityTag.createAndAddCustomTags(ResourceLocation.fromNamespaceAndPath("tfc", "can_trigger_collapse"), sink, stoneType.stone);
                //hardened
                generateLootTableForStone("hardened", stoneType.stone, LOOSE.blocks.get(stoneType), sink, manager);
            });

        //Building Hardend Data Map
            JsonObject hardenedDataMap = new JsonObject();
            JsonObject hardenedArray = new JsonObject();

            for(CompatRock rock : CompatRock.VALUES){
                Block rawBlock = rock.rawBlock().get();
                Block hardenedRock = ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.HARDENED).get();

                if (rawBlock == null || hardenedRock == null) continue;

                String rawId = BuiltInRegistries.BLOCK.getKey(rawBlock).toString();
                String hardenedId = BuiltInRegistries.BLOCK.getKey(hardenedRock).toString();

                hardenedArray.addProperty(rawId, hardenedId);
            }
            for(StoneType stone : StoneTypeRegistry.INSTANCE){
                Block rawBlock = stone.block;
                Block hardenedRock = HARDENED.blocks.get(stone);

                if (rawBlock == null || hardenedRock == null) continue;

                String rawId = BuiltInRegistries.BLOCK.getKey(rawBlock).toString();
                String hardenedId = BuiltInRegistries.BLOCK.getKey(hardenedRock).toString();

                hardenedArray.addProperty(rawId, hardenedId);
            }
            // Finalize the map
            hardenedDataMap.add("values", hardenedArray);

            // Write the file
            ResourceLocation hardenedDataMapPath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                    "data_maps/block/worldgen/hardened_rock_replacement.json"
            );

            sink.addJson(hardenedDataMapPath, hardenedDataMap, ResType.GENERIC);
            FirmaCompat.LOGGER.info("Generated hardened replacement map with {} entries: {}", hardenedArray.size(), hardenedDataMapPath);

        //Loose Block Worldgen
            for(StoneType stone : StoneTypeRegistry.INSTANCE){
                Block looseBlock = LOOSE.blocks.get(stone);
                String loosePath = Utils.getID(looseBlock).getPath();
                String looseNamespace = Utils.getID(looseBlock).getNamespace();

                //TODO - is there a better way to get the stoneType Tag?
                ResourceLocation rockTag = ResourceLocation.fromNamespaceAndPath(stone.getNamespace(), "stone_type/" + stone.getTypeName());

                generateLoosePlacedFeature(sink, rockTag, looseBlock);
                generateLooseConfiguredFeature(sink, looseBlock);
            }

        //Configured Vein Feature Files
            for (CompatVein vein : CompatVein.values()) {
                String veinName = vein.name().toLowerCase(Locale.ROOT);

                JsonArray blocksArray = new JsonArray();

                // ────────────────────────────────────────────────────────────────
                // PART 1: Dynamic rules from StoneZone stone types
                // ────────────────────────────────────────────────────────────────
                for (StoneType stoneType : StoneTypeRegistry.INSTANCE) {
                    Block target = stoneType.block;
                    if (target == null || target == Blocks.AIR) continue;

                    String targetId = BuiltInRegistries.BLOCK.getKey(target).toString();

                    List<Pair<BlockState, Double>> weights = getOreWeightsForStone(vein, stoneType);
                    if (weights == null || weights.isEmpty()) continue;

                    JsonObject rule = createRule(targetId, weights);
                    blocksArray.add(rule);
                }

                // ────────────────────────────────────────────────────────────────
                // PART 2: Append fixed rules (from your original map or hardcoded)
                // ────────────────────────────────────────────────────────────────
                Map<Block, IWeighted<BlockState>> fixedMap = buildReplacementMap(vein);  // your original method
                // OR if you have a different fixed map: Map<Block, IWeighted<BlockState>> fixedMap = getFixedMapForVein(vein);

                for (CompatRock rock : CompatRock.VALUES) {
                    Block target = rock.rawBlock().get();  // ← the block this ore should replace
                    if (target == null || target == Blocks.AIR) continue;

                    String targetId = BuiltInRegistries.BLOCK.getKey(target).toString();

                    List<Pair<BlockState, Double>> weights;

                    if (!vein.ore.isGraded()) {
                        // Non-graded ore
                        var oreMap = ModBlocks.ORES.get(rock);
                        if (oreMap == null) continue;

                        Supplier<Block> oreId = oreMap.get(vein.ore);
                        if (oreId == null) continue;

                        Block oreBlock = oreId.get();
                        if (oreBlock == null || oreBlock == Blocks.AIR) continue;

                        weights = List.of(Pair.of(oreBlock.defaultBlockState(), 1.0));
                    } else {
                        // Graded ore (poor / normal / rich)
                        var rockOreMap = ModBlocks.GRADED_ORES.get(rock);
                        if (rockOreMap == null) continue;

                        var gradeMap = rockOreMap.get(vein.ore);
                        if (gradeMap == null) continue;

                        Supplier<Block> poorId  = gradeMap.get(CompatOre.Grade.POOR);
                        Supplier<Block> normalId = gradeMap.get(CompatOre.Grade.NORMAL);
                        Supplier<Block> richId   = gradeMap.get(CompatOre.Grade.RICH);

                        if (poorId == null || normalId == null || richId == null) continue;

                        Block poor   = poorId.get();
                        Block normal = normalId.get();
                        Block rich   = richId.get();

                        if (poor == null || normal == null || rich == null ||
                                poor == Blocks.AIR || normal == Blocks.AIR || rich == Blocks.AIR) {
                            continue;
                        }

                        // Use your original weighting logic (adjust field name if needed)
                        weights = switch (vein.gradedVeinClass) {  // or vein.gradedVeinClass if that's the actual field
                            case SURFACE -> List.of(
                                    Pair.of(poor.defaultBlockState(),   70.0),
                                    Pair.of(normal.defaultBlockState(), 25.0),
                                    Pair.of(rich.defaultBlockState(),    5.0)
                            );
                            case RICH -> List.of(
                                    Pair.of(poor.defaultBlockState(),   15.0),
                                    Pair.of(normal.defaultBlockState(), 25.0),
                                    Pair.of(rich.defaultBlockState(),   60.0)
                            );
                            case NORMAL -> List.of(
                                    Pair.of(poor.defaultBlockState(),   35.0),
                                    Pair.of(normal.defaultBlockState(), 40.0),
                                    Pair.of(rich.defaultBlockState(),   25.0)
                            ); // fallback / balanced
                        };
                    }

                    // Create and append the rule
                    JsonObject rule = createRule(targetId, weights);
                    blocksArray.add(rule);
                }

                if (blocksArray.size() == 0) {
                    FirmaCompat.LOGGER.warn("Skipping vein {}: no valid replacements at all", veinName);
                    continue;
                }

                // Build config (rest unchanged)
                JsonObject configJson = new JsonObject();
                configJson.addProperty("rarity", vein.rarity);
                configJson.addProperty("density", vein.density);
                configJson.addProperty("min_y", vein.minY);
                configJson.addProperty("max_y", vein.maxY);
                configJson.addProperty("random_name", veinSeedFromName(veinName));
                configJson.add("blocks", blocksArray);

                // Type-specific fields...
                switch (vein.veinType) {
                    case DISC -> {
                        configJson.addProperty("size", vein.size != null ? vein.size : 20);
                        configJson.addProperty("height", vein.height != null ? vein.height : 4);
                    }
                    case CLUSTER -> {
                        configJson.addProperty("size", vein.size != null ? vein.size : 20);
                    }
                    case PIPE -> {
                        configJson.addProperty("height", vein.pipeHeight != null ? vein.pipeHeight : 60);
                        configJson.addProperty("radius", vein.radius != null ? vein.radius : 5);
                        configJson.addProperty("min_skew", vein.minSkew != null ? vein.minSkew : 5);
                        configJson.addProperty("max_skew", vein.maxSkew != null ? vein.maxSkew : 13);
                        configJson.addProperty("min_slant", vein.minSlant != null ? vein.minSlant : 0);
                        configJson.addProperty("max_slant", vein.maxSlant != null ? vein.maxSlant : 2);
                        configJson.addProperty("sign", vein.sign != null ? vein.sign.floatValue() : 0.0f);
                    }
                }

                JsonObject root = new JsonObject();
                root.addProperty("type", "tfc:" + vein.veinType.name().toLowerCase(Locale.ROOT) + "_vein");
                root.add("config", configJson);

                ResourceLocation path = ResourceLocation.fromNamespaceAndPath(
                        FirmaCompat.MODID,
                        "worldgen/configured_feature/stonezone/vein/" + veinName + ".json"
                );

                sink.addJson(path, root, ResType.GENERIC);
                FirmaCompat.LOGGER.info("Generated vein configured feature: {}", path);
            }

        //Placed Vein Feature Files
            for (CompatVein vein : CompatVein.values()) {
                String veinName = vein.name().toLowerCase(Locale.ROOT);

                JsonObject placedJson = new JsonObject();

                // Reference to the configured feature
                String featurePath = FirmaCompat.MODID + ":stonezone/vein/" + veinName;
                // Alternative if you used overworld prefix:
                // String featurePath = FirmaCompat.MODID + ":overworld/vein/" + veinName;

                placedJson.addProperty("feature", featurePath);

                // Empty placement modifiers (as requested)
                placedJson.add("placement", new JsonArray());

                // Write individual placed feature
                ResourceLocation placedPath = ResourceLocation.fromNamespaceAndPath(
                        FirmaCompat.MODID,
                        "worldgen/placed_feature/stonezone/vein/" + veinName + ".json"
                );

                sink.addJson(placedPath, placedJson, ResType.GENERIC);
                FirmaCompat.LOGGER.info("Generated placed feature: {}", placedPath);
            }

        //Copper Vein Placer Configured Feature
            JsonObject configuredCopperVeinPlacer = new JsonObject();
            List<JsonObject> featureCopperEntries = new ArrayList<>();
            int veinCopperPlacerCount = 0;

            for (CompatVein vein : CompatVein.values()) {
                if(vein.ore == CompatOre.MALACHITE || vein.ore == CompatOre.NATIVE_COPPER || vein.ore == CompatOre.TETRAHEDRITE){
                    String veinName = vein.name().toLowerCase(Locale.ROOT);

                    String featurePath = FirmaCompat.MODID + ":stonezone/vein/" + veinName;

                    JsonObject entry = new JsonObject();
                    entry.addProperty("chance", 0f);   // placeholder - will set equal value later
                    entry.addProperty("feature", featurePath);
                    featureCopperEntries.add(entry);
                    veinCopperPlacerCount++;
                }
                float equalChance = 1.0f / veinCopperPlacerCount;
                for (JsonObject entry : featureCopperEntries) {
                    entry.addProperty("chance", equalChance);
                }

                JsonObject config = new JsonObject();
                JsonArray featuresArray = new JsonArray();
                for (JsonObject entry : featureCopperEntries) {
                    featuresArray.add(entry);
                }
                config.add("features", featuresArray);

                String defaultFeature = FirmaCompat.MODID + ":stonezone/vein/" + CompatVein.SURFACE_NATIVE_COPPER.name().toLowerCase(Locale.ROOT);
                // Or use the overworld version if preferred:
                // String defaultFeature = FirmaCompat.MODID + ":overworld/vein/native_copper";

                config.addProperty("default", defaultFeature);

                configuredCopperVeinPlacer.addProperty("type", "minecraft:random_selector");
                configuredCopperVeinPlacer.add("config", config);

                ResourceLocation placedPath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                        "worldgen/configured_feature/stonezone_copper_vein_placer.json"
                );

                sink.addJson(placedPath, configuredCopperVeinPlacer, ResType.GENERIC);
                FirmaCompat.LOGGER.info("Generated placed feature: {}", placedPath);
            }

        //Iron vein placer configured feature
            JsonObject configuredIronVeinPlacer = new JsonObject();
            List<JsonObject> featureIronEntries = new ArrayList<>();
            int veinIronPlacerCount = 0;

            for (CompatVein vein : CompatVein.values()) {
                if(vein.ore == CompatOre.HEMATITE || vein.ore == CompatOre.LIMONITE || vein.ore == CompatOre.MAGNETITE){
                    String veinName = vein.name().toLowerCase(Locale.ROOT);

                    String featurePath = FirmaCompat.MODID + ":stonezone/vein/" + veinName;

                    JsonObject entry = new JsonObject();
                    entry.addProperty("chance", 0f);   // placeholder - will set equal value later
                    entry.addProperty("feature", featurePath);
                    featureIronEntries.add(entry);
                    veinIronPlacerCount++;
                }
                float equalChance = 1.0f / veinIronPlacerCount;
                for (JsonObject entry : featureIronEntries) {
                    entry.addProperty("chance", equalChance);
                }

                JsonObject config = new JsonObject();
                JsonArray featuresArray = new JsonArray();
                for (JsonObject entry : featureIronEntries) {
                    featuresArray.add(entry);
                }
                config.add("features", featuresArray);

                String defaultFeature = FirmaCompat.MODID + ":stonezone/vein/" + CompatVein.SURFACE_HEMATITE.name().toLowerCase(Locale.ROOT);
                // Or use the overworld version if preferred:
                // String defaultFeature = FirmaCompat.MODID + ":overworld/vein/native_copper";

                config.addProperty("default", defaultFeature);

                configuredIronVeinPlacer.addProperty("type", "minecraft:random_selector");
                configuredIronVeinPlacer.add("config", config);

                ResourceLocation placedPath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                        "worldgen/configured_feature/stonezone_iron_vein_placer.json"
                );

                sink.addJson(placedPath, configuredIronVeinPlacer, ResType.GENERIC);
                FirmaCompat.LOGGER.info("Generated placed feature: {}", placedPath);
            }

        //Copper & Iron vein placer placed feature files
            JsonObject copperVeinPlacerJson = new JsonObject();
            JsonObject ironVeinPlacerJson = new JsonObject();

            copperVeinPlacerJson.addProperty("feature", "firma_compat:stonezone_copper_vein_placer");
            ironVeinPlacerJson.addProperty("feature", "firma_compat:stonezone_iron_vein_placer");

            // Empty placement modifiers (as requested)
            copperVeinPlacerJson.add("placement", new JsonArray());
            ironVeinPlacerJson.add("placement", new JsonArray());

                // Write individual placed feature
                ResourceLocation copperVeinPlacerPath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                        "worldgen/placed_feature/stonezone_copper_vein_placer.json"
                );
            ResourceLocation ironVeinPlacerPath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                    "worldgen/placed_feature/stonezone_iron_vein_placer.json"
            );

                sink.addJson(copperVeinPlacerPath, copperVeinPlacerJson, ResType.GENERIC);
                sink.addJson(ironVeinPlacerPath, ironVeinPlacerJson, ResType.GENERIC);

        //Tag - vein features
            JsonObject tagJson = new JsonObject();
            tagJson.addProperty("replace", false);  // Optional: prevents overriding vanilla/other mods

            JsonArray valuesArray = new JsonArray();

            for (CompatVein vein : CompatVein.values()) {
                //copper & iron veins are handled with the copper/iron placer features
                if(vein.ore != CompatOre.NATIVE_COPPER && vein.ore != CompatOre.MALACHITE && vein.ore != CompatOre.TETRAHEDRITE &&
                        vein.ore != CompatOre.HEMATITE && vein.ore != CompatOre.LIMONITE && vein.ore != CompatOre.MAGNETITE){
                    String veinName = vein.name().toLowerCase(Locale.ROOT);
                    String featurePath = FirmaCompat.MODID + ":stonezone/vein/" + veinName;
                    valuesArray.add(featurePath);
                }
            }
            valuesArray.add("firma_compat:stonezone_copper_vein_placer");
            valuesArray.add("firma_compat:stonezone_iron_vein_placer");

            tagJson.add("values", valuesArray);

            // Write the tag file
            ResourceLocation tagPath = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "tags/worldgen/placed_feature/stonezone_veins.json"  // or whatever name you prefer
            );

            sink.addJson(tagPath, tagJson, ResType.GENERIC);
            FirmaCompat.LOGGER.info("Generated placed feature tag: {}", tagPath);

        //Tag - loose features
            JsonObject looseTagJson = new JsonObject();
            //tagJson.addProperty("replace", false);  // Optional: prevents overriding vanilla/other mods

            JsonArray looseValuesArray = new JsonArray();

            for (StoneType stone : StoneTypeRegistry.INSTANCE) {
                String loosePath = Utils.getID(LOOSE.blocks.get(stone)).getPath();
                String looseNamespace = Utils.getID(LOOSE.blocks.get(stone)).getNamespace();

                if(LOOSE.blocks.get(stone) != null){
                    String featurePath = FirmaCompat.MODID + ":loose/" + loosePath;
                    looseValuesArray.add(featurePath);
                }
            }

            looseTagJson.add("values", looseValuesArray);

            // Write the tag file
            ResourceLocation looseTagPath = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "tags/worldgen/placed_feature/stonezone_loose.json"
            );

            sink.addJson(looseTagPath, looseTagJson, ResType.GENERIC);
            FirmaCompat.LOGGER.info("Generated placed feature tag: {}", looseTagPath);

        //Biome Modifier File
            JsonObject biomeModifier = new JsonObject();

            String placedFeatureTag = FirmaCompat.MODID + ":stonezone_veins";

            biomeModifier.addProperty("type", "neoforge:add_features");
            biomeModifier.addProperty("biomes", "#c:is_overworld");
            biomeModifier.addProperty("features", "#" + placedFeatureTag);
            biomeModifier.addProperty("step", "underground_ores");

            // Write individual placed feature
            ResourceLocation biomeModifierPath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                    "neoforge/biome_modifier/add_stonezone_veins.json"
            );

            sink.addJson(biomeModifierPath, biomeModifier, ResType.GENERIC);
            FirmaCompat.LOGGER.info("Generated biome modifier: {}", biomeModifierPath);

        //Landslide Recipes
            for (StoneType stone : StoneTypeRegistry.INSTANCE) {
                Block looseCobble = LOOSE_COBBLE.blocks.get(stone);
                if (looseCobble == null) continue;
                String looseCobbleId   = BuiltInRegistries.BLOCK.getKey(looseCobble).toString();
                String looseCobblePath = Utils.getID(looseCobble).getPath();

                JsonObject landslideRecipeJson = new JsonObject();
                JsonArray landslideArray = new JsonArray();

                landslideArray.add(looseCobbleId);

                landslideRecipeJson.addProperty("type", "tfc:landslide");
                landslideRecipeJson.add("ingredient", landslideArray);
                landslideRecipeJson.addProperty("result", looseCobbleId);

                // Write individual placed feature
                ResourceLocation landslideRecipeLoc = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                        "recipe/landslide/" + looseCobblePath + ".json"
                );

                sink.addJson(landslideRecipeLoc, landslideRecipeJson, ResType.GENERIC);
                FirmaCompat.LOGGER.info("Generated biome modifier: {}", landslideRecipeLoc);
            }


        //Collapse Recipes
            for (StoneType stone : StoneTypeRegistry.INSTANCE) {
                // Skip if required blocks are missing
                Block looseCobble = LOOSE_COBBLE.blocks.get(stone);
                if (looseCobble == null) continue;

                Block hardened = HARDENED.blocks.get(stone);
                if (hardened == null) continue;

                String rawId           = BuiltInRegistries.BLOCK.getKey(stone.stone).toString();
                String hardenedId      = BuiltInRegistries.BLOCK.getKey(hardened).toString();
                String looseCobbleId   = BuiltInRegistries.BLOCK.getKey(looseCobble).toString();

                String looseNs         = Utils.getID(looseCobble).getNamespace();
                String stoneName       = stone.getTypeName();

                // ────────────────────────────────────────────────────────────────
                // 1. Main collapse recipe: raw + hardened + ALL poor ores + ALL non-graded ores
                //    → result: loose cobble
                // ────────────────────────────────────────────────────────────────
                JsonArray poorCollapseArray = new JsonArray();
                poorCollapseArray.add(rawId);
                poorCollapseArray.add(hardenedId);

                for (Map.Entry<String, SimpleEntrySet<StoneType, Block>> entry : ORE_ENTRY_SETS.entrySet()) {
                    String key = entry.getKey();
                    SimpleEntrySet<StoneType, Block> oreSet = entry.getValue();

                    // Include only:
                    // - keys starting with "poor_" (poor graded ores)
                    // - keys that have NO prefix at all (non-graded ores)
                    boolean isPoor = key.startsWith("poor_");
                    boolean isUngraded = !key.startsWith("poor_") &&
                            !key.startsWith("normal_") &&
                            !key.startsWith("rich_");

                    if (!isPoor && !isUngraded) {
                        continue;
                    }

                    Block oreBlock = oreSet.blocks.get(stone);
                    if (oreBlock != null && oreBlock != Blocks.AIR) {
                        String oreId = BuiltInRegistries.BLOCK.getKey(oreBlock).toString();
                        poorCollapseArray.add(oreId);

                        FirmaCompat.LOGGER.debug("Poor/ungraded collapse: {} for {} → {}",
                                key, stoneName, oreId);
                    }
                }

                JsonObject poorCollapseJson = new JsonObject();
                poorCollapseJson.addProperty("type", "tfc:collapse");
                poorCollapseJson.add("ingredient", poorCollapseArray);
                poorCollapseJson.addProperty("result", looseCobbleId);

                ResourceLocation poorLoc = ResourceLocation.fromNamespaceAndPath(
                        FirmaCompat.MODID,
                        "recipe/collapse/" + looseNs + "/" + stoneName + ".json"
                );
                sink.addJson(poorLoc, poorCollapseJson, ResType.GENERIC);

                // ────────────────────────────────────────────────────────────────
                // 2. Rich ores collapse → corresponding normal ore
                // ────────────────────────────────────────────────────────────────
                for (Map.Entry<String, SimpleEntrySet<StoneType, Block>> entry : ORE_ENTRY_SETS.entrySet()) {
                    String key = entry.getKey();
                    if (!key.startsWith("rich_")) continue;

                    String normalKey = "normal_" + key.substring("rich_".length());
                    SimpleEntrySet<StoneType, Block> normalSet = ORE_ENTRY_SETS.get(normalKey);
                    if (normalSet == null) continue;

                    Block richBlock = entry.getValue().blocks.get(stone);
                    Block normalBlock = normalSet.blocks.get(stone);

                    if (richBlock != null && richBlock != Blocks.AIR &&
                            normalBlock != null && normalBlock != Blocks.AIR) {

                        String richId   = BuiltInRegistries.BLOCK.getKey(richBlock).toString();
                        String normalId = BuiltInRegistries.BLOCK.getKey(normalBlock).toString();

                        JsonObject recipe = new JsonObject();
                        recipe.addProperty("type", "tfc:collapse");
                        JsonArray ing = new JsonArray();
                        ing.add(richId);
                        recipe.add("ingredient", ing);
                        recipe.addProperty("result", normalId);

                        String oreType = key.substring("rich_".length());
                        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(
                                FirmaCompat.MODID,
                                "recipe/collapse/" + looseNs + "/rich_to_normal/" + stoneName + "_" + oreType + ".json"
                        );
                        sink.addJson(loc, recipe, ResType.GENERIC);

                        FirmaCompat.LOGGER.debug("Rich→Normal: {} → {} for {}", richId, normalId, stoneName);
                    }
                }

                // ────────────────────────────────────────────────────────────────
                // 3. Normal ores collapse → corresponding poor ore
                // ────────────────────────────────────────────────────────────────
                for (Map.Entry<String, SimpleEntrySet<StoneType, Block>> entry : ORE_ENTRY_SETS.entrySet()) {
                    String key = entry.getKey();
                    if (!key.startsWith("normal_")) continue;

                    String poorKey = "poor_" + key.substring("normal_".length());
                    SimpleEntrySet<StoneType, Block> poorSet = ORE_ENTRY_SETS.get(poorKey);
                    if (poorSet == null) continue;

                    Block normalBlock = entry.getValue().blocks.get(stone);
                    Block poorBlock   = poorSet.blocks.get(stone);

                    if (normalBlock != null && normalBlock != Blocks.AIR &&
                            poorBlock != null && poorBlock != Blocks.AIR) {

                        String normalId = BuiltInRegistries.BLOCK.getKey(normalBlock).toString();
                        String poorId   = BuiltInRegistries.BLOCK.getKey(poorBlock).toString();

                        JsonObject recipe = new JsonObject();
                        recipe.addProperty("type", "tfc:collapse");
                        JsonArray ing = new JsonArray();
                        ing.add(normalId);
                        recipe.add("ingredient", ing);
                        recipe.addProperty("result", poorId);

                        String oreType = key.substring("normal_".length());
                        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(
                                FirmaCompat.MODID,
                                "recipe/collapse/" + looseNs + "/normal_to_poor/" + stoneName + "_" + oreType + ".json"
                        );
                        sink.addJson(loc, recipe, ResType.GENERIC);

                        FirmaCompat.LOGGER.debug("Normal→Poor: {} → {} for {}", normalId, poorId, stoneName);
                    }
                }

                FirmaCompat.LOGGER.info("Generated all collapse recipes for stone: {}", stoneName);
            }

        // Generate individual loot tables for EVERY ore block variant
            for (Map.Entry<String, SimpleEntrySet<StoneType, Block>> entry : ORE_ENTRY_SETS.entrySet()) {
                String oreKey = entry.getKey(); // e.g. "poor_native_copper"
                SimpleEntrySet<StoneType, Block> oreSet = entry.getValue();

                // Determine the base dropped item for this ore variant
                ResourceLocation baseDroppedItem;
                String oreName;
                String gradePrefix = "";

                if (oreKey.startsWith("poor_")) {
                    gradePrefix = "poor_";
                    oreName = oreKey.substring("poor_".length());
                } else if (oreKey.startsWith("normal_")) {
                    gradePrefix = "normal_";
                    oreName = oreKey.substring("normal_".length());
                } else if (oreKey.startsWith("rich_")) {
                    gradePrefix = "rich_";
                    oreName = oreKey.substring("rich_".length());
                } else if (oreKey.startsWith("ungraded_")) {
                    oreName = oreKey.substring("ungraded_".length());
                } else {
                    FirmaCompat.LOGGER.warn("Unexpected ore key format: {}", oreKey);
                    continue;
                }

                baseDroppedItem = ResourceLocation.fromNamespaceAndPath("tfc", "ore/" + gradePrefix + oreName);

                // Now generate a loot table for EVERY stone variant of this ore
                for (Map.Entry<StoneType, Block> blockEntry : oreSet.blocks.entrySet()) {
                    StoneType stoneType = blockEntry.getKey();
                    Block oreBlock = blockEntry.getValue();

                    if (oreBlock == null || oreBlock == Blocks.AIR) continue;

                    ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(oreBlock);
                    if (blockId == null) continue;

                    // Loot table path MUST match the block's registry path
                    // e.g. data/firma_compat/loot_tables/blocks/poor_stone_native_copper_ore.json
                    ResourceLocation lootLoc = ResourceLocation.fromNamespaceAndPath(
                            blockId.getNamespace(),
                            "loot_table/blocks/" + blockId.getPath() + ".json"
                    );

                    // Build loot table JSON (identical structure for all)
                    JsonObject lootTable = new JsonObject();
                    lootTable.addProperty("type", "minecraft:block");

                    JsonArray pools = new JsonArray();
                    JsonObject pool = new JsonObject();
                    pool.addProperty("rolls", 1);

                    JsonArray entries = new JsonArray();
                    JsonObject oreEntry = new JsonObject();
                    oreEntry.addProperty("type", "minecraft:item");
                    oreEntry.addProperty("name", baseDroppedItem.toString());
                    entries.add(oreEntry);

                    pool.add("entries", entries);

                    JsonArray conditions = new JsonArray();
                    JsonObject survivesExplosion = new JsonObject();
                    survivesExplosion.addProperty("condition", "minecraft:survives_explosion");
                    conditions.add(survivesExplosion);

                    pool.add("conditions", conditions);
                    pools.add(pool);

                    lootTable.add("pools", pools);

                    // Write the loot table
                    sink.addJson(lootLoc, lootTable, ResType.GENERIC);

                    FirmaCompat.LOGGER.debug("Generated loot table for block {} → drops {}",
                            blockId, baseDroppedItem);
                }
            }
        });
    }

    private JsonObject createRule(String targetId, List<Pair<BlockState, Double>> weights) {
        JsonObject rule = new JsonObject();

        JsonArray replaceArray = new JsonArray();
        replaceArray.add(targetId);
        rule.add("replace", replaceArray);

        JsonArray withArray = new JsonArray();
        for (Pair<BlockState, Double> pair : weights) {
            String oreId = BuiltInRegistries.BLOCK.getKey(pair.getFirst().getBlock()).toString();

            JsonObject entry = new JsonObject();
            entry.addProperty("block", oreId);

            double weight = pair.getSecond();
            if (Math.abs(weight - 1.0) > 0.001) {
                entry.addProperty("weight", weight);
            }

            withArray.add(entry);
        }

        rule.add("with", withArray);
        return rule;
    }

    private List<Pair<BlockState, Double>> getOreWeightsForStone(CompatVein vein, StoneType stoneType) {
        String oreName = vein.ore.name().toLowerCase(Locale.ROOT);

        if (!vein.ore.isGraded()) {
            // Non-graded ore
            String ungradedKey = "ungraded_" + oreName;
            SimpleEntrySet<StoneType, Block> entrySet = ORE_ENTRY_SETS.get(ungradedKey);
            if (entrySet == null) return null;

            Block oreBlock = entrySet.blocks.get(stoneType);
            if (oreBlock == null || oreBlock == Blocks.AIR) return null;

            return List.of(Pair.of(oreBlock.defaultBlockState(), 1.0));
        } else {
            // Graded ore
            SimpleEntrySet<StoneType, Block> poorSet   = ORE_ENTRY_SETS.get("poor_"   + oreName);
            SimpleEntrySet<StoneType, Block> normalSet = ORE_ENTRY_SETS.get("normal_" + oreName);
            SimpleEntrySet<StoneType, Block> richSet   = ORE_ENTRY_SETS.get("rich_"   + oreName);

            if (poorSet == null || normalSet == null || richSet == null) return null;

            Block poor   = poorSet.blocks.get(stoneType);
            Block normal = normalSet.blocks.get(stoneType);
            Block rich   = richSet.blocks.get(stoneType);

            if (poor == null || normal == null || rich == null ||
                    poor == Blocks.AIR || normal == Blocks.AIR || rich == Blocks.AIR) {
                return null;
            }

            // Reuse your original weighting logic
            return switch (vein.gradedVeinClass) {  // ← note: you used String "normal"/"surface" here
                case SURFACE -> List.of(
                        Pair.of(poor.defaultBlockState(),   70.0),
                        Pair.of(normal.defaultBlockState(), 25.0),
                        Pair.of(rich.defaultBlockState(),    5.0)
                );
                case RICH -> List.of(
                        Pair.of(poor.defaultBlockState(),   15.0),
                        Pair.of(normal.defaultBlockState(), 25.0),
                        Pair.of(rich.defaultBlockState(),   60.0)
                );
                case NORMAL -> List.of(
                        Pair.of(poor.defaultBlockState(),   35.0),
                        Pair.of(normal.defaultBlockState(), 40.0),
                        Pair.of(rich.defaultBlockState(),   25.0));
            };
        }
    }

    private JsonObject heightProviderJson(int minY, int maxY) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", "minecraft:uniform"); // or "absolute" if you prefer
        JsonObject min = new JsonObject();
        min.addProperty("absolute", minY);
        JsonObject max = new JsonObject();
        max.addProperty("absolute", maxY);
        obj.add("min_inclusive", min);
        obj.add("max_inclusive", max);
        return obj;
    }

    //Block Map
    private static Map<Block, IWeighted<BlockState>> buildSingleReplacementMap(CompatSingleBlockVein vein) {
        Map<Block, IWeighted<BlockState>> map = new HashMap<>();

        int added = 0;
        int skipped = 0;

        for (CompatRock rock : CompatRock.VALUES) {
            Block target = rock.rawBlock().get();  // ← fixed: use vanilla equiv (STONE, GRANITE, etc.)
            if (target == null || target == Blocks.AIR) {
                skipped++;
                continue;
            }
            if (vein.oreBlock == null || vein.oreBlock == Blocks.AIR) {
                skipped++;
                continue;
            }

            map.put(target, new Weighted<>(
                    List.of(Pair.of(vein.oreBlock.defaultBlockState(), 1.0))
            ));
            added++;

        }

        System.out.println("Vein " + vein.name() + ": Added " + added + " replacements, skipped " + skipped);
        return map;
    }

    //Graded BlockMap
    private static Map<Block, IWeighted<BlockState>> buildReplacementMap(CompatVein vein) {
        Map<Block, IWeighted<BlockState>> map = new HashMap<>();

        int added = 0;
        int skipped = 0;

        for (CompatRock rock : CompatRock.VALUES) {
            Block target = rock.rawBlock().get();
            if (target == null || target == Blocks.AIR) {
                skipped++;
                continue;
            }

            if (!vein.ore.isGraded()) {
                var oreId = ModBlocks.ORES.get(rock).get(vein.ore);
                if (oreId == null) {
                    skipped++;
                    continue;
                }
                Block oreBlock = oreId.get();
                if (oreBlock == null || oreBlock == Blocks.AIR) {
                    skipped++;
                    continue;
                }

                map.put(target, new Weighted<>(
                        List.of(Pair.of(oreBlock.defaultBlockState(), 1.0))
                ));
                added++;
            } else {
                var oreMap = ModBlocks.GRADED_ORES.get(rock);
                if (oreMap == null) {
                    skipped++;
                    continue;
                }
                var gradeMap = oreMap.get(vein.ore);
                if (gradeMap == null) {
                    skipped++;
                    continue;
                }

                var poorId   = gradeMap.get(CompatOre.Grade.POOR);
                var normalId = gradeMap.get(CompatOre.Grade.NORMAL);
                var richId   = gradeMap.get(CompatOre.Grade.RICH);

                if (poorId == null || normalId == null || richId == null) {
                    skipped++;
                    continue;
                }

                Block poor   = poorId.get();
                Block normal = normalId.get();
                Block rich   = richId.get();

                if (poor == null || normal == null || rich == null) {
                    skipped++;
                    continue;
                }

                List<Pair<BlockState, Double>> weights = switch(vein.gradedVeinClass){
                    case SURFACE -> List.of(
                            Pair.of(poor.defaultBlockState(),   70.0),
                            Pair.of(normal.defaultBlockState(), 25.0),
                            Pair.of(rich.defaultBlockState(),    5.0));
                    case NORMAL -> List.of(
                            Pair.of(poor.defaultBlockState(),   35.0),
                            Pair.of(normal.defaultBlockState(), 40.0),
                            Pair.of(rich.defaultBlockState(),   25.0));
                    case RICH -> List.of(
                            Pair.of(poor.defaultBlockState(),   15.0),
                            Pair.of(normal.defaultBlockState(), 25.0),
                            Pair.of(rich.defaultBlockState(),   60.0));
                };

                map.put(target, new Weighted<>(weights));
                added++;
            }
        }

        System.out.println("Vein " + vein.name() + ": Added " + added + " replacements, skipped " + skipped);
        return map;
    }

    //BlockMap Helpers

    // Helper: get non-graded ore block for rock (from your ORES map)
    private static Block getOreBlock(CompatOre ore, CompatRock rock) {
        // Assuming your registration provides access; adjust if needed
        return ORES.get(rock).get(ore).get();
    }

    // Helper: get graded ore block for rock + grade (from your GRADED_ORES map)
    private static Block getGradedOreBlock(CompatOre ore, CompatRock rock, CompatOre.Grade grade) {
        // Assuming your registration provides access; adjust if needed
        return GRADED_ORES.get(rock).get(ore).get(grade).get();
    }

    // Simple deterministic seed – same as TFC mostly uses
    private static long veinSeedFromName(String name) {
        long hash = 0;
        for (char c : name.toCharArray()) {
            hash = 31 * hash + c;
        }
        return hash & 0x7FFFFFFFFFFFFFFFL; // positive
    }

    /**
     * Generates a pressure plate recipe from lumber planks.
     * Pattern: 2×1 horizontal (2 lumber → 1 pressure plate)
     *
     * @param sink       ResourceSink to write the JSON
     * @param stone       The WoodType providing the lumber
     * @param brickItem The child key for the lumber item (e.g. "lumber")
     * @param count      Number of pressure plates produced (usually 1)
     * @param suffix     Optional suffix for recipe path
     */
    public void generatePressurePlateFromBrickRecipe(
            ResourceSink sink,
            StoneType stone,
            Item brickItem,
            int count,
            @Nullable String suffix
    ) {
        String brickItemPath = Utils.getID(Objects.requireNonNull(brickItem)).getPath();
        String brickNamespace = Utils.getID(Objects.requireNonNull(brickItem)).getNamespace();

        String plateItemPath = Utils.getID(Objects.requireNonNull(stone.getChild("pressure_plate"))).getPath();
        String plateItemNamespace = Utils.getID(Objects.requireNonNull(stone.getChild("pressure_plate"))).getNamespace();

        if (count < 1) {
            FirmaCompat.LOGGER.warn("Invalid count {} for pressure plate recipe {}, defaulting to 1", count, brickItemPath);
            count = 1;
        }

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        JsonObject key = new JsonObject();
        JsonObject brickKey = new JsonObject();
        brickKey.addProperty("item", brickNamespace + ":" + brickItemPath);
        key.add("L", brickKey);
        recipe.add("key", key);

        // Pattern: pressure plate (2 planks)
        JsonArray pattern = new JsonArray();
        pattern.add("LL");
        recipe.add("pattern", pattern);

        JsonObject result = new JsonObject();
        result.addProperty("count", count);
        result.addProperty("id", plateItemNamespace + ":" + plateItemPath);
        recipe.add("result", result);

        ResourceLocation outputLoc = ResourceLocation.parse(plateItemNamespace + ":" + plateItemPath);
        String recipePath = "crafting/" + outputLoc.getPath();

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += suffix;
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(
                FirmaCompat.MODID, recipePath
        );

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    public void generateBrickRecipe(
            ResourceSink sink,
            Item inputItem,
            Item outputItem,
            String toolTag,
            int count,
            @Nullable String suffix
    ) {
        String input = Utils.getID(inputItem).getPath();
        String inputNamespace = Utils.getID(inputItem).getNamespace();

        if (count < 1) {
            EveryCompat.LOGGER.warn("Invalid count {} for brick recipe {} → {}, defaulting to 1",
                    count, input, outputItem);
            count = 1;
        }

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "tfc:advanced_shapeless_crafting");

        JsonArray ingredients = new JsonArray();

        // Tool (saw)
        JsonObject toolIngredient = new JsonObject();
        toolIngredient.addProperty("tag", toolTag);
        ingredients.add(toolIngredient);

        // Input log/block
        JsonObject inputIngredient = new JsonObject();
        inputIngredient.addProperty("item", inputNamespace + ":" + input);
        ingredients.add(inputIngredient);

        recipe.add("ingredients", ingredients);

        // Primary ingredient = tool
        JsonObject primaryIngredient = new JsonObject();
        primaryIngredient.addProperty("tag", toolTag);
        recipe.add("primary_ingredient", primaryIngredient);

        // Remainder with damage modifier
        JsonObject remainder = new JsonObject();
        JsonArray modifiers = new JsonArray();
        JsonObject damageModifier = new JsonObject();
        damageModifier.addProperty("type", "tfc:damage_crafting_remainder");
        modifiers.add(damageModifier);
        remainder.add("modifiers", modifiers);
        recipe.add("remainder", remainder);

        // Result
        JsonObject result = new JsonObject();
        result.addProperty("count", count);           // ← now uses the parameter
        result.addProperty("id", String.valueOf(outputItem));
        recipe.add("result", result);

        // Create recipe ResourceLocation based on output item's ID + optional suffix
        ResourceLocation outputLoc = ResourceLocation.parse(outputItem.toString());
        String recipePath = "crafting/" + outputLoc.getPath();  // e.g. "brick/acacia_brick"

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += suffix;
        }

        // Final location: <output_namespace>:recipe/<recipePath>
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(
                FirmaCompat.MODID, recipePath
        );

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    /**
     * Generates a Minecraft shaped crafting recipe for TFC-style rock bricks.
     * Pattern is fixed as:
     *   X Y X
     *   Y X Y
     *   X Y X
     * (Bricks in X positions, mortar in Y positions)
     *
     * @param sink       ResourceSink to write the generated JSON
     * @param inputItem  Full item ID for the brick ingredient (e.g. "tfc:brick/andesite", "mycoolmod:brick/basalt")
     * @param outputItem Full output item ID (e.g. "tfc:rock/bricks/andesite", "mycoolmod:rock/bricks/basalt")
     * @param count      Number of output items per craft (e.g. 4, 8)
     * @param suffix     Optional suffix to append to the recipe path (e.g. "_from_bricks"). Null/empty = no suffix.
     */
    public void generateBrickBlockRecipe(
            ResourceSink sink,
            String inputItem,
            String outputItem,
            int count,
            @Nullable String suffix
    ) {
        if (count < 1) {
            count = 1;
            EveryCompat.LOGGER.warn("Invalid count {} for brick recipe {} → {}, clamped to 1",
                    count, inputItem, outputItem);
        }

        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "misc");

        // Key definitions
        JsonObject key = new JsonObject();

        JsonObject brickKey = new JsonObject();
        brickKey.addProperty("item", inputItem);
        key.add("X", brickKey);

        JsonObject mortarKey = new JsonObject();
        mortarKey.addProperty("item", "tfc:mortar");  // fixed as per TFC standard
        key.add("Y", mortarKey);

        recipe.add("key", key);

        // Fixed 3x3 pattern from your example
        JsonArray pattern = new JsonArray();
        pattern.add("XYX");
        pattern.add("YXY");
        pattern.add("XYX");
        recipe.add("pattern", pattern);

        // Result
        JsonObject result = new JsonObject();
        result.addProperty("count", count);
        result.addProperty("id", outputItem);
        recipe.add("result", result);

        // Build recipe ResourceLocation based on output item's namespace + path
        ResourceLocation outLoc = ResourceLocation.parse(outputItem);
        String recipePath = "crafting/" + outLoc.getPath();  // e.g. "bricks/andesite"

        if (suffix != null && !suffix.isEmpty()) {
            recipePath += suffix;
        }

        // Final location: <output_namespace>:recipes/<recipePath>
        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(
                FirmaCompat.MODID, recipePath
        );

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }

    private void generateLootTableForStone(
            @Nullable String type,
            Block hardenedBlock,
            Block looseBlock,
            ResourceSink sink,
            ResourceManager manager
    ) {
        ResourceLocation templateLoc = modRes("loot_table/blocks/stone_hardened.json");
        String outputNamespace = "";

        ResourceLocation hardenedId = BuiltInRegistries.BLOCK.getKey(hardenedBlock);
        ResourceLocation looseId = BuiltInRegistries.BLOCK.getKey(looseBlock);

        if ((hardenedId == null || hardenedId.equals(BuiltInRegistries.BLOCK.getDefaultKey())) &&
                (looseId == null || looseId.equals(BuiltInRegistries.BLOCK.getDefaultKey()))) return;

        String hardenedPath = "";
        String loosePath = looseId.getPath();
        String outputPath = "loot_table/blocks/";

        if(type != null){
            hardenedPath = hardenedId.getPath() + "_" + type;
            outputNamespace = StoneZone.MOD_ID;
            outputPath = outputPath + FirmaCompat.MODID + "/" + hardenedId.getNamespace() + "/" +  hardenedPath;
        } else {
            hardenedPath = hardenedId.getPath();
            outputNamespace = hardenedId.getNamespace();
            outputPath = outputPath + hardenedPath;
        }

        ResourceLocation outputLoc = ResourceLocation.fromNamespaceAndPath(
                outputNamespace,outputPath + ".json");


        try (InputStream stream = manager.getResource(templateLoc).orElseThrow(
                () -> new FileNotFoundException("Loot template not found: " + templateLoc)).open()) {

            String templateText = new String(stream.readAllBytes(), StandardCharsets.UTF_8);

            // Replace with clean block ID string (e.g. "firma_compat:granite_loose")
            String modifiedText = templateText.replace("minecraft:stone", hardenedId.toString());
            modifiedText = modifiedText.replace("firma_compat:stone_loose", looseId.toString());
            modifiedText = modifiedText.replace("firma_compat:blocks/stone", hardenedId.getNamespace() + "blocks/" + hardenedId.getPath());

            JsonObject json = JsonParser.parseString(modifiedText).getAsJsonObject();

            sink.addJson(outputLoc, json, ResType.GENERIC);

        } catch (Exception e) {  // catch broader to log everything
            EveryCompat.LOGGER.error("Failed generating loot table for {} from {}: {}",
                    hardenedId, templateLoc, e.getMessage(), e);
        }
    }

    public static void generateLoosePlacedFeature(
            ResourceSink sink,
            ResourceLocation rockTag,
            Block looseBlock
    ) {
        if (looseBlock == null) {
            FirmaCompat.LOGGER.warn("Skipping placed feature: invalid Block");
            return;
        }

        String loosePath = Utils.getID(looseBlock).getPath();
        String looseNamespace = Utils.getID(looseBlock).getNamespace();

        String looseBlockId = BuiltInRegistries.BLOCK.getKey(looseBlock).toString();

        // Build the predicate for tfc:would_survive_with_fluid
        JsonObject survivePredicate = new JsonObject();
        survivePredicate.addProperty("type", "tfc:would_survive_with_fluid");

        JsonObject stateObj = new JsonObject();
        stateObj.addProperty("Name", looseBlockId);

        JsonObject propertiesObj = new JsonObject();
        propertiesObj.addProperty("fluid", "empty");
        stateObj.add("Properties", propertiesObj);

        survivePredicate.add("state", stateObj);

        // Build the full block predicate filter
        JsonObject filterObj = new JsonObject();
        filterObj.addProperty("type", "minecraft:block_predicate_filter");
        filterObj.add("predicate", survivePredicate);

        // Build the first filter (above acacia logs)
        JsonObject rockFilter = new JsonObject();
        rockFilter.addProperty("type", "minecraft:block_predicate_filter");

        JsonObject allOfPredicate = new JsonObject();
        allOfPredicate.addProperty("type", "minecraft:all_of");

        JsonArray predicatesArray = new JsonArray();

        // Predicate 1: matching acacia logs below
        JsonObject logMatch = new JsonObject();
        logMatch.addProperty("type", "minecraft:matching_block_tag");
        logMatch.add("offset", jsonArray(0, -1, 0));
        logMatch.addProperty("tag", String.valueOf(rockTag));
        predicatesArray.add(logMatch);

        // Predicate 2: sturdy face upward
        JsonObject sturdyFace = new JsonObject();
        sturdyFace.addProperty("type", "minecraft:has_sturdy_face");
        sturdyFace.add("offset", jsonArray(0, -1, 0));
        sturdyFace.addProperty("direction", "up");
        predicatesArray.add(sturdyFace);

        allOfPredicate.add("predicates", predicatesArray);
        rockFilter.add("predicate", allOfPredicate);

        // Combine both filters
        JsonArray placementArray = new JsonArray();
        placementArray.add(rockFilter);
        placementArray.add(filterObj);

        // Root JSON
        JsonObject root = new JsonObject();
        root.addProperty("feature", FirmaCompat.MODID + ":" + "loose/" + loosePath);
        root.add("placement", placementArray);

        // Write the file
        ResourceLocation path = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                "worldgen/placed_feature/loose/" + loosePath + ".json");

        sink.addJson(path, root, ResType.GENERIC);
        FirmaCompat.LOGGER.info("Generated loose rock bamboo placed feature: {}", path);
    }

    // Tiny helper to create [0, -1, 0] array
    private static JsonArray jsonArray(int... values) {
        JsonArray arr = new JsonArray();
        for (int v : values) arr.add(v);
        return arr;
    }

    public static void generateLooseConfiguredFeature(
            ResourceSink sink,
            Block looseBlock
    ) {
        if (looseBlock == null || looseBlock == Blocks.AIR) {
            FirmaCompat.LOGGER.warn("Skipping random_selector feature: invalid loose block");
            return;
        }

        String blockId = BuiltInRegistries.BLOCK.getKey(looseBlock).toString();
        String blockPath = Utils.getID(looseBlock).getPath();

        // Helper function to create one nested "feature" object with given count
        Function<Integer, JsonObject> createFluidFeature = count -> {
            JsonObject state = new JsonObject();
            state.addProperty("Name", blockId);

            JsonObject props = new JsonObject();
            props.addProperty("fluid", "empty");
            props.addProperty("count", String.valueOf(count));
            state.add("Properties", props);

            JsonObject toPlace = new JsonObject();
            toPlace.addProperty("type", "minecraft:simple_state_provider");
            toPlace.add("state", state);

            JsonObject config = new JsonObject();
            config.add("to_place", toPlace);

            JsonObject featureObj = new JsonObject();
            featureObj.addProperty("type", "tfc:block_with_fluid");
            featureObj.add("config", config);

            JsonObject wrapper = new JsonObject();
            wrapper.add("feature", featureObj);
            wrapper.add("placement", new JsonArray()); // empty placement

            return wrapper;
        };

        // Build the "features" array with chances
        JsonArray featuresArray = new JsonArray();

        // Chance 0.4 → count 2
        JsonObject entry2 = new JsonObject();
        entry2.addProperty("chance", 0.4);
        entry2.add("feature", createFluidFeature.apply(2));
        featuresArray.add(entry2);

        // Chance 0.2 → count 3
        JsonObject entry3 = new JsonObject();
        entry3.addProperty("chance", 0.2);
        entry3.add("feature", createFluidFeature.apply(3));
        featuresArray.add(entry3);

        // Default (remaining chance) → count 1
        JsonObject defaultFeature = createFluidFeature.apply(1);

        // Root config
        JsonObject config = new JsonObject();
        config.add("features", featuresArray);
        config.add("default", defaultFeature);

        // Root JSON
        JsonObject root = new JsonObject();
        root.addProperty("type", "minecraft:random_selector");
        root.add("config", config);

        // Write the file
        String blockName = BuiltInRegistries.BLOCK.getKey(looseBlock).getPath();

        ResourceLocation path = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                "worldgen/configured_feature/" + "loose/" + blockPath + ".json"
        );

        sink.addJson(path, root, ResType.GENERIC);
        FirmaCompat.LOGGER.info("Generated random_selector loose count feature: {}", path);
    }
}
