package com.bumppo109.firma_compat.everycompat.firmalife;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addons.firmalife.CompatFLBlocks;
import com.bumppo109.firma_compat.addons.firmalife.FLVein;
import com.bumppo109.firma_compat.block.CompatRock;
import com.eerussianguy.firmalife.common.items.FLItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.util.collections.IWeighted;
import net.dries007.tfc.util.collections.Weighted;
import net.mehvahdjukaar.every_compat.api.RenderLayer;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.stone_zone.api.StoneZoneModule;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneType;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneTypeRegistry;
import net.mehvahdjukaar.stone_zone.api.set.stone.VanillaStoneTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

public class FLStoneZoneModule extends StoneZoneModule {

    public final SimpleEntrySet<StoneType, Block> POOR_CHORMITE;
    public final SimpleEntrySet<StoneType, Block> NORMAL_CHROMITE;
    public final SimpleEntrySet<StoneType, Block> RICH_CHROMITE;

    public final Map<String, SimpleEntrySet<StoneType, Block>> ORE_ENTRY_SETS = new HashMap<>();

    public FLStoneZoneModule(){
        super(FirmaCompat.MODID, FirmaCompat.MODID);

        ResourceKey<CreativeModeTab> tab = CreativeModeTabs.BUILDING_BLOCKS;

        POOR_CHORMITE = SimpleEntrySet.builder(StoneType.class, "chromite_ore", "poor",
                        getModBlock("poor_stone_chromite_ore"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.block))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(modRes("rock_ores"), Registries.BLOCK)
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(POOR_CHORMITE);

        NORMAL_CHROMITE = SimpleEntrySet.builder(StoneType.class, "chromite_ore", "normal",
                        getModBlock("normal_stone_chromite_ore"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.block))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(modRes("rock_ores"), Registries.BLOCK)
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(NORMAL_CHROMITE);

        RICH_CHROMITE = SimpleEntrySet.builder(StoneType.class, "chromite_ore", "rich",
                        getModBlock("rich_stone_chromite_ore"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new Block(Utils.copyPropertySafe(stoneType.block))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(modRes("rock_ores"), Registries.BLOCK)
                .setRenderType(RenderLayer.CUTOUT)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(RICH_CHROMITE);
    }

    @Override
    public void addDynamicClientResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicClientResources(executor);

        executor.accept((manager, sink) -> {
            for(StoneType stone : StoneTypeRegistry.INSTANCE){
                //poor
                String rawPath = Utils.getID(stone.stone).getPath();
                String rawNamespace = Utils.getID(stone.stone).getNamespace();
                String rawTexture = rawNamespace + ":block/" + rawPath;
                ResourceLocation poorOrePath = Utils.getID(POOR_CHORMITE.blocks.get(stone));

                JsonObject poorOreModelJson = new JsonObject();
                JsonObject poorTextureObj = new JsonObject();

                poorTextureObj.addProperty("all", rawTexture);
                poorTextureObj.addProperty("overlay", "firma_compat:block/template/ore_overlay/poor_chromite");

                poorOreModelJson.addProperty("parent", "firma_compat:block/template/ore");
                poorOreModelJson.add("textures", poorTextureObj);
                sink.addJson(poorOrePath, poorOreModelJson, ResType.BLOCK_MODELS);
                //normal
                ResourceLocation normalOrePath = Utils.getID(NORMAL_CHROMITE.blocks.get(stone));

                JsonObject normalOreModelJson = new JsonObject();
                JsonObject normalTextureObj = new JsonObject();

                normalTextureObj.addProperty("all", rawTexture);
                normalTextureObj.addProperty("overlay", "firma_compat:block/template/ore_overlay/normal_chromite");

                normalOreModelJson.addProperty("parent", "firma_compat:block/template/ore");
                normalOreModelJson.add("textures", normalTextureObj);
                sink.addJson(normalOrePath, normalOreModelJson, ResType.BLOCK_MODELS);
                //rich
                ResourceLocation richOrePath = Utils.getID(RICH_CHROMITE.blocks.get(stone));

                JsonObject richOreModelJson = new JsonObject();
                JsonObject richTextureObj = new JsonObject();

                richTextureObj.addProperty("all", rawTexture);
                richTextureObj.addProperty("overlay", "firma_compat:block/template/ore_overlay/rich_chromite");

                richOreModelJson.addProperty("parent", "firma_compat:block/template/ore");
                richOreModelJson.add("textures", richTextureObj);
                sink.addJson(richOrePath, richOreModelJson, ResType.BLOCK_MODELS);
            }
        });
    }


    @Override
    // RECIPES, TAGS
    //everycomp log tags formatted -> everycomp:[modid]/[woodType]_logs
    public void addDynamicServerResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> {

        //Configured Vein Feature Files
            for (FLVein vein : FLVein.values()) {
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
                    // Graded ore (poor / normal / rich)
                    //var rockOreMap = ModBlocks.GRADED_ORES.get(rock);
                    //if (rockOreMap == null) continue;

                    //var gradeMap = rockOreMap.get(vein.ore);
                    //if (gradeMap == null) continue;

                    //ModBlocks.Id<Block> poorId  = gradeMap.get(CompatOre.Grade.POOR);
                    //ModBlocks.Id<Block> normalId = gradeMap.get(CompatOre.Grade.NORMAL);
                    //ModBlocks.Id<Block> richId   = gradeMap.get(CompatOre.Grade.RICH);

                    //if (poorId == null || normalId == null || richId == null) continue;

                    Block poor   = CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.POOR).get();
                    Block normal = CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.NORMAL).get();
                    Block rich   = CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.RICH).get();

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
                        "worldgen/configured_feature/stonezone/firmalife/vein/" + veinName + ".json"
                );

                sink.addJson(path, root, ResType.GENERIC);
                FirmaCompat.LOGGER.info("Generated vein configured feature: {}", path);
            }

        //Placed Vein Feature Files
            for (FLVein vein : FLVein.values()) {
                String veinName = vein.name().toLowerCase(Locale.ROOT);

                JsonObject placedJson = new JsonObject();

                // Reference to the configured feature
                String featurePath = FirmaCompat.MODID + ":stonezone/firmalife/vein/" + veinName;
                // Alternative if you used overworld prefix:
                // String featurePath = FirmaCompat.MODID + ":overworld/vein/" + veinName;

                placedJson.addProperty("feature", featurePath);

                // Empty placement modifiers (as requested)
                placedJson.add("placement", new JsonArray());

                // Write individual placed feature
                ResourceLocation placedPath = ResourceLocation.fromNamespaceAndPath(
                        FirmaCompat.MODID,
                        "worldgen/placed_feature/stonezone/firmalife/vein/" + veinName + ".json"
                );

                sink.addJson(placedPath, placedJson, ResType.GENERIC);
                FirmaCompat.LOGGER.info("Generated placed feature: {}", placedPath);
            }

        //Tag - vein features
            JsonObject tagJson = new JsonObject();

            JsonArray valuesArray = new JsonArray();

            for (FLVein vein : FLVein.values()) {
                String veinName = vein.name().toLowerCase(Locale.ROOT);
                String featurePath = FirmaCompat.MODID + ":stonezone/firmalife/vein/" + veinName;
                valuesArray.add(featurePath);
            }

            tagJson.add("values", valuesArray);

            // Write the tag file
            ResourceLocation tagPath = ResourceLocation.fromNamespaceAndPath(
                    FirmaCompat.MODID,
                    "tags/worldgen/placed_feature/stonezone_firmalife_veins.json"  // or whatever name you prefer
            );

            sink.addJson(tagPath, tagJson, ResType.GENERIC);
            FirmaCompat.LOGGER.info("Generated placed feature tag: {}", tagPath);

        // Generate individual loot tables for EVERY ore block variant
            record ChromiteOre(ResourceLocation resLoc, Item oreItem) {}

            // Now generate a loot table for EVERY stone variant of this ore
            Item poorChromite = FLItems.CHROMIUM_ORES.get(Ore.Grade.POOR).get();
            Item normalChromite = FLItems.CHROMIUM_ORES.get(Ore.Grade.NORMAL).get();
            Item richChromite = FLItems.CHROMIUM_ORES.get(Ore.Grade.RICH).get();

            for (StoneType stoneType : StoneTypeRegistry.INSTANCE) {
                String poorOreBlockPath = Utils.getID(POOR_CHORMITE.blocks.get(stoneType)).getPath();
                String poorOreBlockNamespace = Utils.getID(POOR_CHORMITE.blocks.get(stoneType)).getNamespace();

                String normalOreBlockPath = Utils.getID(NORMAL_CHROMITE.blocks.get(stoneType)).getPath();
                String normalOreBlockNamespace = Utils.getID(NORMAL_CHROMITE.blocks.get(stoneType)).getNamespace();

                String richOreBlockPath = Utils.getID(RICH_CHROMITE.blocks.get(stoneType)).getPath();
                String richOreBlockNamespace = Utils.getID(RICH_CHROMITE.blocks.get(stoneType)).getNamespace();

                ResourceLocation poorBlockId = BuiltInRegistries.BLOCK.getKey(POOR_CHORMITE.blocks.get(stoneType));
                ResourceLocation normalBlockId = BuiltInRegistries.BLOCK.getKey(NORMAL_CHROMITE.blocks.get(stoneType));
                ResourceLocation richBlockId = BuiltInRegistries.BLOCK.getKey(RICH_CHROMITE.blocks.get(stoneType));

                if (poorBlockId == null || normalBlockId == null || richBlockId == null) continue;

                // Loot table path MUST match the block's registry path
                // e.g. data/firma_compat/loot_tables/blocks/poor_stone_native_copper_ore.json
                ResourceLocation poorLootLoc = ResourceLocation.fromNamespaceAndPath(poorOreBlockNamespace,
                        "loot_tables/blocks/" + poorOreBlockPath + ".json");
                ResourceLocation normalLootLoc = ResourceLocation.fromNamespaceAndPath(normalOreBlockNamespace,
                        "loot_tables/blocks/" + normalOreBlockPath + ".json");
                ResourceLocation richLootLoc = ResourceLocation.fromNamespaceAndPath(richOreBlockNamespace,
                        "loot_tables/blocks/" + richOreBlockPath + ".json");

                List<ChromiteOre> chromiteOres = List.of(
                        new ChromiteOre(poorLootLoc, poorChromite),
                        new ChromiteOre(normalLootLoc, normalChromite),
                        new ChromiteOre(richLootLoc, richChromite)
                );

                for(ChromiteOre ore : chromiteOres){
                    // Build loot table JSON (identical structure for all)
                    JsonObject lootTable = new JsonObject();
                    lootTable.addProperty("type", "minecraft:block");

                    JsonArray pools = new JsonArray();
                    JsonObject pool = new JsonObject();
                    pool.addProperty("rolls", 1);

                    JsonArray entries = new JsonArray();
                    JsonObject oreEntry = new JsonObject();
                    oreEntry.addProperty("type", "minecraft:item");
                    oreEntry.addProperty("name", "firmalife:" + ore.oreItem.toString().toLowerCase(Locale.ROOT));
                    entries.add(oreEntry);

                    pool.add("entries", entries);

                    JsonArray conditions = new JsonArray();
                    JsonObject survivesExplosion = new JsonObject();
                    survivesExplosion.addProperty("condition", "minecraft:survives_explosion");
                    conditions.add(survivesExplosion);

                    pool.add("conditions", conditions);
                    pools.add(pool);

                    lootTable.add("pools", pools);

                    // Write the loot tables
                    sink.addJson(ore.resLoc, lootTable, ResType.GENERIC);
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

    private List<Pair<BlockState, Double>> getOreWeightsForStone(FLVein vein, StoneType stoneType) {
        String oreName = "chromite";

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

    //Graded BlockMap
    private static Map<Block, IWeighted<BlockState>> buildReplacementMap(FLVein vein) {
        Map<Block, IWeighted<BlockState>> map = new HashMap<>();

        int added = 0;
        int skipped = 0;

        for (CompatRock rock : CompatRock.VALUES) {
            Block target = rock.rawBlock().get();
            if (target == null || target == Blocks.AIR) {
                skipped++;
                continue;
            }

            var oreMap = CompatFLBlocks.CHROMITE_ORES.get(rock);
            if (oreMap == null) {
                skipped++;
                continue;
            }
            /*
            var gradeMap = oreMap.get(vein.ore);
            if (gradeMap == null) {
                skipped++;
                continue;
            }

             */

            var poorId   = oreMap.get(Ore.Grade.POOR);
            var normalId = oreMap.get(Ore.Grade.NORMAL);
            var richId   = oreMap.get(Ore.Grade.RICH);

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

        System.out.println("Vein " + vein.name() + ": Added " + added + " replacements, skipped " + skipped);
        return map;
    }

    // Simple deterministic seed – same as TFC mostly uses
    private static long veinSeedFromName(String name) {
        long hash = 0;
        for (char c : name.toCharArray()) {
            hash = 31 * hash + c;
        }
        return hash & 0x7FFFFFFFFFFFFFFFL; // positive
    }
}
