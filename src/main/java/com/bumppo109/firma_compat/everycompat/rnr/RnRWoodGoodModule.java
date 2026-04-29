package com.bumppo109.firma_compat.everycompat.rnr;

import com.bumppo109.firma_compat.FirmaCompat;
import com.eerussianguy.firmalife.common.blocks.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.therighthon.rnr.common.block.RNRBlocks;
import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.*;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public final class RnRWoodGoodModule extends SimpleModule {

    public final ItemOnlyEntrySet<WoodType, Item> SHINGLE;
    public final SimpleEntrySet<WoodType, Block> SHINGLES;
    public final SimpleEntrySet<WoodType, Block> SHINGLES_STAIRS;
    public final SimpleEntrySet<WoodType, Block> SHINGLES_SLAB;

    public RnRWoodGoodModule() {
        super(FirmaCompat.MODID, FirmaCompat.MODID, FirmaCompat.MODID);

        ResourceKey<CreativeModeTab> tab = CreativeModeTabs.BUILDING_BLOCKS;

        SHINGLE = ItemOnlyEntrySet.builder(WoodType.class, "shingle",
                        getModItem("oak_shingle"), () -> VanillaWoodTypes.OAK,
                        w -> new Item(new Item.Properties())
                )
                .requiresChildren("log")
                .addTexture(modRes("item/oak_shingle"), PaletteStrategies.MAIN_CHILD)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SHINGLE);

        SHINGLES = SimpleEntrySet.builder(WoodType.class, "shingles",
                        getModBlock("oak_shingles"), () -> VanillaWoodTypes.OAK,
                        w -> new Block(Utils.copyPropertySafe(w.planks))
                )
                .requiresChildren("log")
                .addTexture(modRes("block/oak_shingles"), PaletteStrategies.MAIN_CHILD)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SHINGLES);

        SHINGLES_STAIRS = SimpleEntrySet.builder(WoodType.class, "shingles_stairs",
                        getModBlock("oak_shingles_stairs"), () -> VanillaWoodTypes.OAK,
                        w -> new StairBlock(SHINGLES.blocks.get(w).defaultBlockState(), Utils.copyPropertySafe(w.planks))
                )
                .requiresFromMap(SHINGLES.blocks)
                //.addTexture(modRes("block/oak_shingles"), PaletteStrategies.MAIN_CHILD)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SHINGLES_STAIRS);

        SHINGLES_SLAB = SimpleEntrySet.builder(WoodType.class, "shingles_slab",
                        getModBlock("oak_shingles_slab"), () -> VanillaWoodTypes.OAK,
                        w -> new SlabBlock(Utils.copyPropertySafe(w.planks))
                )
                .requiresFromMap(SHINGLES.blocks)
                //.addTexture(modRes("block/oak_shingles"), PaletteStrategies.MAIN_CHILD)
                .addTag(BlockTags.MINEABLE_WITH_AXE, Registries.BLOCK)
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*").excludeBlockTypes("afc:.*").excludeBlockTypes("domum_ornamentum:.*")
                .build();
        this.addEntry(SHINGLES_SLAB);
    }

    @Override
    public void addDynamicClientResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicClientResources(executor);

        executor.accept((manager, sink) -> {});
    }

    @Override
    // RECIPES, TAGS
    //everycomp log tags formatted -> everycomp:[modid]/[woodType]_logs
    public void addDynamicServerResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> {
            for(WoodType wood : WoodTypeRegistry.INSTANCE){
                if(SHINGLE.items.get(wood) != null){
                    generateToolItemRecipe(sink, wood.log.asItem(), "tfc:chisels", SHINGLE.items.get(wood), 4, null);

                    generateBlockModShingleRecipe(sink, wood, SHINGLES);
                    generateBlockModShingleRecipe(sink, wood, SHINGLES_STAIRS);
                    generateBlockModShingleRecipe(sink, wood, SHINGLES_SLAB);

                    generateChiselStairRecipes(sink, wood, SHINGLES, SHINGLES_STAIRS, "stair");
                    generateChiselStairRecipes(sink, wood, SHINGLES, SHINGLES_SLAB, "slab");
                }

            }
        });
    }

    private void generateBlockModShingleRecipe(ResourceSink sink, WoodType wood, SimpleEntrySet<WoodType, Block> baseEntrySet) {
        Block base = baseEntrySet.blocks.get(wood);
        Block frameBlock = null;
        Item shingleItem = SHINGLE.items.get(wood);

        if(baseEntrySet == SHINGLES_STAIRS){
            frameBlock = RNRBlocks.ROOF_FRAME_STAIRS.get();
        } else if(baseEntrySet == SHINGLES_SLAB){
            frameBlock = RNRBlocks.ROOF_FRAME_SLAB.get();
        } else {
            frameBlock = RNRBlocks.ROOF_FRAME.get();
        }

        if (base != null && shingleItem != null){
            ResourceLocation frameID = Utils.getID(frameBlock);
            ResourceLocation baseId = Utils.getID(base);

            if (frameID != null && baseId != null){
                JsonObject recipe = new JsonObject();
                recipe.addProperty("type", "rnr:block_mod");

                JsonArray ingredient = new JsonArray();
                ingredient.add(frameID.toString());
                recipe.add("input_block", ingredient);

                JsonObject input_item = new JsonObject();
                input_item.addProperty("item", Utils.getID(shingleItem).toString());
                recipe.add("input_item", input_item);

                recipe.addProperty("output_block", baseId.toString());

                ResourceLocation recipePath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"block_mod/" + baseId.getPath());

                sink.addJson(recipePath, recipe, ResType.RECIPES);
            }
        }
    }

    private void generateChiselStairRecipes(
            ResourceSink sink,
            WoodType wood,
            SimpleEntrySet<WoodType, Block> baseEntrySet,
            SimpleEntrySet<WoodType, Block> stairsEntrySet,
            String chiselMode) {

        Block stairs = stairsEntrySet.blocks.get(wood);
        Block base = baseEntrySet.blocks.get(wood);

        if (stairs != null && base != null) {

            ResourceLocation stairsId = Utils.getID(stairs);
            ResourceLocation baseId = Utils.getID(base);

            // Skip if something is wrong / not registered
            if (stairsId != null && baseId != null) {

                JsonObject recipe = new JsonObject();
                recipe.addProperty("type", "tfc:chisel");

                JsonArray ingredient = new JsonArray();
                ingredient.add(baseId.toString());
                recipe.add("ingredient", ingredient);

                recipe.addProperty("mode", chiselMode);

                recipe.addProperty("result", stairsId.toString());

                ResourceLocation recipePath = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"chisel/" + stairsId.getPath());

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
}
