package com.bumppo109.firma_compat.everycompat.rnr;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRStoneType;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.datagen.recipes.TFCRecipeBuilder;
import com.bumppo109.firma_compat.util.ModTags;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.therighthon.rnr.common.block.PathStairBlock;
import com.therighthon.rnr.common.block.RNRBlocks;
import com.therighthon.rnr.common.block.StonePathBlock;
import com.therighthon.rnr.common.block.StonePathSlabBlock;
import net.dries007.tfc.common.TFCTags;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.ItemOnlyEntrySet;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.stone_zone.StoneZone;
import net.mehvahdjukaar.stone_zone.api.StoneZoneModule;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneType;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneTypeRegistry;
import net.mehvahdjukaar.stone_zone.api.set.stone.VanillaStoneChildKeys;
import net.mehvahdjukaar.stone_zone.api.set.stone.VanillaStoneTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class RnRStoneZoneModule extends StoneZoneModule {

    public final ItemOnlyEntrySet<StoneType, Item> FLAGSTONE;
    public final SimpleEntrySet<StoneType, Block> FLAGSTONES;
    public final SimpleEntrySet<StoneType, Block> FLAGSTONES_STAIRS;
    public final SimpleEntrySet<StoneType, Block> FLAGSTONES_SLAB;
    public final SimpleEntrySet<StoneType, Block> COBBLED_ROAD;
    public final SimpleEntrySet<StoneType, Block> COBBLED_ROAD_STAIRS;
    public final SimpleEntrySet<StoneType, Block> COBBLED_ROAD_SLAB;
    public final SimpleEntrySet<StoneType, Block> SETT_ROAD;
    public final SimpleEntrySet<StoneType, Block> SETT_ROAD_STAIRS;
    public final SimpleEntrySet<StoneType, Block> SETT_ROAD_SLAB;

    public RnRStoneZoneModule() {
        super(FirmaCompat.MODID, FirmaCompat.MODID);

        ResourceKey<CreativeModeTab> tab = CreativeModeTabs.BUILDING_BLOCKS;

        FLAGSTONE = ItemOnlyEntrySet.builder(StoneType.class, "flagstone",
                        getModItem("stone_flagstone"), () -> VanillaStoneTypes.STONE,
                        w -> new Item(new Item.Properties())
                )
                .addTexture(modRes("item/stone_flagstone"), PaletteStrategies.MAIN_CHILD)
                .addTag(ModTags.Items.FLAGSTONES, Registries.ITEM)
                .excludeBlockTypes("tfc:.*")
                .setTabKey(tab)
                .build();
        this.addEntry(FLAGSTONE);

        FLAGSTONES = SimpleEntrySet.builder(StoneType.class, "flagstones",
                        getModBlock("stone_flagstones"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new StonePathBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnRStoneType.FLAGSTONES).get()))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ModTags.Blocks.FLAGSTONE_ROAD_BLOCKS, Registries.BLOCK)
                .addTexture(modRes("block/stone_flagstones"), PaletteStrategies.MAIN_CHILD)
                .dropSelf()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(FLAGSTONES);

        FLAGSTONES_STAIRS = SimpleEntrySet.builder(StoneType.class, "flagstones_stairs",
                        getModBlock("stone_flagstones_stairs"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new PathStairBlock(() -> FLAGSTONE.blocks.get(stoneType).defaultBlockState(),
                                Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnRStoneType.FLAGSTONES).get()), StonePathBlock.getDefaultSpeedFactor())
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ModTags.Blocks.FLAGSTONE_ROAD_STAIRS, Registries.BLOCK)
                .dropSelf()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(FLAGSTONES_STAIRS);

        FLAGSTONES_SLAB = SimpleEntrySet.builder(StoneType.class, "flagstones_slab",
                        getModBlock("stone_flagstones_slab"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new StonePathSlabBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnRStoneType.FLAGSTONES).get()))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ModTags.Blocks.FLAGSTONE_ROAD_SLABS, Registries.BLOCK)
                .dropSelf()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(FLAGSTONES_SLAB);

        COBBLED_ROAD = SimpleEntrySet.builder(StoneType.class, "cobbled_road",
                        getModBlock("stone_cobbled_road"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new StonePathBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnRStoneType.COBBLED_ROAD).get()))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ModTags.Blocks.COBBLED_ROAD_BLOCKS, Registries.BLOCK)
                .addTexture(modRes("block/loose_stone_cobble"), PaletteStrategies.MAIN_CHILD)
                .dropSelf()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(COBBLED_ROAD);

        COBBLED_ROAD_STAIRS = SimpleEntrySet.builder(StoneType.class, "cobbled_road_stairs",
                        getModBlock("stone_cobbled_road_stairs"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new PathStairBlock(() -> FLAGSTONE.blocks.get(stoneType).defaultBlockState(),
                                Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnRStoneType.COBBLED_ROAD).get()), StonePathBlock.getDefaultSpeedFactor())
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ModTags.Blocks.COBBLED_ROAD_STAIRS, Registries.BLOCK)
                .dropSelf()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(COBBLED_ROAD_STAIRS);

        COBBLED_ROAD_SLAB = SimpleEntrySet.builder(StoneType.class, "cobbled_road_slab",
                        getModBlock("stone_cobbled_road_slab"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new StonePathSlabBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnRStoneType.COBBLED_ROAD).get()))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ModTags.Blocks.COBBLED_ROAD_SLABS, Registries.BLOCK)
                .dropSelf()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(COBBLED_ROAD_SLAB);

        SETT_ROAD = SimpleEntrySet.builder(StoneType.class, "sett_road",
                        getModBlock("stone_sett_road"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new StonePathBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnRStoneType.SETT_ROAD).get()))
                )
                .requiresChildren(VanillaStoneChildKeys.BRICKS)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ModTags.Blocks.SETT_ROAD_BLOCKS, Registries.BLOCK)
                .addTexture(modRes("block/stone_sett_road"), PaletteStrategies.MAIN_CHILD)
                .dropSelf()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(SETT_ROAD);

        SETT_ROAD_STAIRS = SimpleEntrySet.builder(StoneType.class, "sett_road_stairs",
                        getModBlock("stone_sett_road_stairs"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new PathStairBlock(() -> FLAGSTONE.blocks.get(stoneType).defaultBlockState(),
                                Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnRStoneType.SETT_ROAD).get()), StonePathBlock.getDefaultSpeedFactor())
                )
                .requiresChildren(VanillaStoneChildKeys.BRICKS)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ModTags.Blocks.SETT_ROAD_STAIRS, Registries.BLOCK)
                .dropSelf()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(SETT_ROAD_STAIRS);

        SETT_ROAD_SLAB = SimpleEntrySet.builder(StoneType.class, "sett_road_slab",
                        getModBlock("stone_sett_road_slab"), () -> VanillaStoneTypes.STONE,
                        stoneType -> new StonePathSlabBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnRStoneType.SETT_ROAD).get()))
                )
                .requiresChildren(VanillaStoneChildKeys.BRICKS)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ModTags.Blocks.SETT_ROAD_SLABS, Registries.BLOCK)
                .dropSelf()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(SETT_ROAD_SLAB);
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

        });
    }


    @Override
    // RECIPES, TAGS
    //everycomp log tags formatted -> everycomp:[modid]/[woodType]_logs
    public void addDynamicServerResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> {
            for(StoneType stone : StoneTypeRegistry.INSTANCE){
                if(stone.stone != null){
                    ResourceLocation cobbledItem = ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID, FirmaCompat.MODID + "/" + stone.getNamespace() + "/" + stone.getTypeName() + "_loose");

                    generateBlockModRecipe(sink, stone, COBBLED_ROAD);
                    mattock(sink, COBBLED_ROAD.blocks.get(stone), COBBLED_ROAD_STAIRS.blocks.get(stone), TFCRecipeBuilder.ChiselMode.STAIR, null);
                    mattock(sink, COBBLED_ROAD.blocks.get(stone), COBBLED_ROAD_SLAB.blocks.get(stone), TFCRecipeBuilder.ChiselMode.SLAB, null);

                    blockModRecipe(sink, null, cobbledItem, RNRBlocks.BASE_COURSE.get(), COBBLED_ROAD.blocks.get(stone));

                    generatelandslideSelfRecipe(sink, COBBLED_ROAD.blocks.get(stone), null);
                    landsideRecipe(sink, COBBLED_ROAD_STAIRS.blocks.get(stone), COBBLED_ROAD_SLAB.blocks.get(stone), null);
                    generatelandslideSelfRecipe(sink, COBBLED_ROAD_SLAB.blocks.get(stone), null);

                    if(FLAGSTONE.items.get(stone) != null){
                        generateToolItemRecipe(sink, stone.stone.asItem(), "c:tools/chisel", FLAGSTONE.items.get(stone), 12, null);

                        blockModRecipe(sink, null, FLAGSTONE.items.get(stone), RNRBlocks.BASE_COURSE.get(), FLAGSTONES.blocks.get(stone));

                        mattock(sink, FLAGSTONES.blocks.get(stone), FLAGSTONES_STAIRS.blocks.get(stone), TFCRecipeBuilder.ChiselMode.STAIR, null);
                        mattock(sink, FLAGSTONES.blocks.get(stone), FLAGSTONES_SLAB.blocks.get(stone), TFCRecipeBuilder.ChiselMode.SLAB, null);

                        generatelandslideSelfRecipe(sink, FLAGSTONES.blocks.get(stone), null);
                        landsideRecipe(sink, FLAGSTONES_STAIRS.blocks.get(stone), COBBLED_ROAD_SLAB.blocks.get(stone), null);
                        generatelandslideSelfRecipe(sink, FLAGSTONES_SLAB.blocks.get(stone), null);
                    }
                }
                if(stone.getChild(VanillaStoneChildKeys.BRICKS) != null){
                    ResourceLocation brickItem = ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID, FirmaCompat.MODID + "/" + stone.getNamespace() + "/" + stone.getTypeName() + "_brick");
                     blockModRecipe(sink, null, brickItem, RNRBlocks.BASE_COURSE.get(), SETT_ROAD.blocks.get(stone));

                    mattock(sink, SETT_ROAD.blocks.get(stone), SETT_ROAD_STAIRS.blocks.get(stone), TFCRecipeBuilder.ChiselMode.STAIR, null);
                    mattock(sink, SETT_ROAD.blocks.get(stone), SETT_ROAD_SLAB.blocks.get(stone), TFCRecipeBuilder.ChiselMode.SLAB, null);

                    generatelandslideSelfRecipe(sink, SETT_ROAD.blocks.get(stone), null);
                    landsideRecipe(sink, SETT_ROAD_STAIRS.blocks.get(stone), COBBLED_ROAD_SLAB.blocks.get(stone), null);
                    generatelandslideSelfRecipe(sink, SETT_ROAD_SLAB.blocks.get(stone), null);
                }
            }
        });
    }

    protected void blockModRecipe(ResourceSink sink,
                                  @Nullable String suffix,
                                  ResourceLocation inputItem,
                                  Block inputBlock,
                                  Block outputBlock) {

        String recipeName = BuiltInRegistries.BLOCK.getKey(outputBlock).getPath();

        ResourceLocation inputBlockRes = BuiltInRegistries.BLOCK.getKey(inputBlock);
        ResourceLocation outputRes = BuiltInRegistries.BLOCK.getKey(outputBlock);

        JsonObject json = new JsonObject();

        json.addProperty("type", "rnr:block_mod");

        // input_item object
        JsonObject inputItemObj = new JsonObject();
        inputItemObj.addProperty("item", inputItem.getNamespace() + ":" + inputItem.getPath());
        json.add("input_item", inputItemObj);

        // input_block as string
        json.addProperty("input_block", inputBlockRes.getNamespace() + ":" + inputBlockRes.getPath());

        // output_block as string
        json.addProperty("output_block", outputRes.getNamespace() + ":" + outputRes.getPath());

        if(suffix == null){
            recipeName = recipeName;
        } else {
            recipeName = recipeName + "_" + suffix;
        }

        // Save the recipe
        ResourceLocation recipeLoc = ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID,
                "recipes/block_mod/" + recipeName + ".json");

        sink.addJson(recipeLoc, json, ResType.GENERIC);
    }

    protected void blockModRecipe(ResourceSink sink,
                                  @Nullable String suffix,
                                  Item inputItem,
                                  Block inputBlock,
                                  Block outputBlock) {

        String recipeName = BuiltInRegistries.BLOCK.getKey(outputBlock).getPath();

        ResourceLocation inputItemRes = BuiltInRegistries.ITEM.getKey(inputItem);
        ResourceLocation inputBlockRes = BuiltInRegistries.BLOCK.getKey(inputBlock);
        ResourceLocation outputRes = BuiltInRegistries.BLOCK.getKey(outputBlock);

        JsonObject json = new JsonObject();

        json.addProperty("type", "rnr:block_mod");

        // input_item object
        JsonObject inputItemObj = new JsonObject();
        inputItemObj.addProperty("item", inputItemRes.getNamespace() + ":" + inputItemRes.getPath());
        json.add("input_item", inputItemObj);

        // input_block as string
        json.addProperty("input_block", inputBlockRes.getNamespace() + ":" + inputBlockRes.getPath());

        // output_block as string
        json.addProperty("output_block", outputRes.getNamespace() + ":" + outputRes.getPath());

        if(suffix == null){
            recipeName = recipeName;
        } else {
            recipeName = recipeName + "_" + suffix;
        }

        // Save the recipe
        ResourceLocation recipeLoc = ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID,
                "recipes/block_mod/" + recipeName + ".json");

        sink.addJson(recipeLoc, json, ResType.GENERIC);
    }

    protected void mattock(ResourceSink sink, Block input, Block output, TFCRecipeBuilder.ChiselMode mode, @Nullable String suffix) {
        ResourceLocation outputRes = BuiltInRegistries.BLOCK.getKey(output);
        ResourceLocation inputRes = BuiltInRegistries.BLOCK.getKey(input);

        mattockRecipe(sink, outputRes.getPath(), inputRes.getNamespace() + ":" + inputRes.getPath(), outputRes.getNamespace() + ":" + outputRes.getPath(), mode, null, null, suffix);
    }

    protected void mattockRecipe(ResourceSink sink, String name,
                                 String ingredient,           // Changed: now simple string
                                 String result,               // Changed: now simple string
                                 TFCRecipeBuilder.ChiselMode mode,
                                 @Nullable JsonElement itemIngredient,
                                 @Nullable JsonObject extraDrop,
                                 @Nullable String recipeSuffix) {

        JsonObject json = new JsonObject();
        json.addProperty("type", "rnr:mattock");

        json.addProperty("ingredient", ingredient);   // ← Now a string, not object
        json.addProperty("result", result);           // ← Now a string
        json.addProperty("mode", mode.getSerializedName());

        if (itemIngredient != null) {
            json.add("item_ingredient", itemIngredient);
        }
        if (extraDrop != null) {
            json.add("extra_drop", extraDrop);
        }

        if(recipeSuffix == null){
            name = name;
        } else {
            name = name + "_" + recipeSuffix;
        }

        ResourceLocation recipeLoc = ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID,
                "recipes/mattock/" + mode.getSerializedName() + "/" + name + ".json");

        sink.addJson(recipeLoc, json, ResType.GENERIC);
    }

    public void landsideRecipe(ResourceSink sink, Block input, Block output, @Nullable String suffix) {
        if (input == null || output == null) return;

        String inputId = BuiltInRegistries.BLOCK.getKey(input).toString();
        String outputId = BuiltInRegistries.BLOCK.getKey(input).toString();

        String inputPath = Utils.getID(inputId).getPath();
        String outputPath = Utils.getID(outputId).getPath();

        String recipeName = outputPath + "_from_" + inputPath;

        JsonObject landslideRecipeJson = new JsonObject();

        landslideRecipeJson.addProperty("type", "tfc:landslide");
        landslideRecipeJson.addProperty("ingredient", inputId);
        landslideRecipeJson.addProperty("result", outputId);

        if(suffix != null) {
            recipeName = recipeName + "_" + suffix;
        }

        ResourceLocation landslideRecipeLoc = ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID,
                "recipes/landslide/" + recipeName + ".json");

        sink.addJson(landslideRecipeLoc, landslideRecipeJson, ResType.GENERIC);
    }

    public void generatelandslideSelfRecipe(ResourceSink sink, Block block, @Nullable String suffix) {

        if (block == null) return;

        String looseCobbleId = BuiltInRegistries.BLOCK.getKey(block).toString();
        String looseCobblePath = Utils.getID(block).getPath();

        String recipeName = looseCobblePath;

        JsonObject landslideRecipeJson = new JsonObject();
        JsonArray landslideArray = new JsonArray();
        landslideArray.add(looseCobbleId);

        landslideRecipeJson.addProperty("type", "tfc:landslide");
        landslideRecipeJson.add("ingredient", landslideArray);
        landslideRecipeJson.addProperty("result", looseCobbleId);

        if(suffix != null) {
            recipeName = recipeName + "_" + suffix;
        }

        ResourceLocation landslideRecipeLoc = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                "recipes/landslide/" + recipeName + ".json");

        sink.addJson(landslideRecipeLoc, landslideRecipeJson, ResType.GENERIC);
    }

    private void generateBlockModRecipe(ResourceSink sink, StoneType stone, SimpleEntrySet<StoneType, Block> baseEntrySet) {
        Block base = baseEntrySet.blocks.get(stone);
        Block baseCourse = RNRBlocks.BASE_COURSE.get();
        Item roadItem = null;

        if(baseEntrySet == FLAGSTONES){
            roadItem = FLAGSTONE.items.get(stone);
        } else if(baseEntrySet == COBBLED_ROAD){
            roadItem = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID, FirmaCompat.MODID + "/" + stone.getNamespace() + "/" + stone.getTypeName() +"_loose"));
        } else if(baseEntrySet == SETT_ROAD){
            roadItem = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID, FirmaCompat.MODID + "/" + stone.getNamespace() + "/" + stone.getTypeName() +"_brick"));
        }

        if (base != null && roadItem != null){
            ResourceLocation baseCourseID = Utils.getID(baseCourse);
            ResourceLocation baseId = Utils.getID(base);

            if (baseCourseID != null && baseId != null){
                JsonObject recipe = new JsonObject();
                recipe.addProperty("type", "rnr:block_mod");

                JsonArray ingredient = new JsonArray();
                ingredient.add(baseCourseID.toString());
                recipe.add("input_block", ingredient);

                JsonObject input_item = new JsonObject();
                input_item.addProperty("item", Utils.getID(roadItem).toString());
                recipe.add("input_item", input_item);

                recipe.addProperty("output_block", baseId.toString());


                // Final path example: firma_compat:recipes/mattock/andesite_cobbled_road_stairs.json
                ResourceLocation recipePath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block_mod/" + baseId.getPath());

                sink.addJson(recipePath, recipe, ResType.RECIPES);
            }
        }
    }

    public void generateToolItemRecipe(
            ResourceSink sink,
            Item inputitem,
            String toolTag,               // e.g. "c:tools/saw"
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

        materialIngredient.addProperty("item", Utils.getID(inputitem).toString());

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

        result.addProperty("id", outputItemNamespace + ":" + outputItemPath);
        recipe.add("result", result);

        // Recipe ID
        String path = "crafting/" + outputItemPath;
        if (recipeSuffix != null && !recipeSuffix.isEmpty()) {
            path += recipeSuffix;
        }

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, path);

        sink.addJson(recipeId, recipe, ResType.RECIPES);
    }
}
