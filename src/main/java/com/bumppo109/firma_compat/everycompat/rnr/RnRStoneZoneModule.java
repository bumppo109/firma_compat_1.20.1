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
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
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
                        getModBlock("andesite_cobbled_road"), () -> VanillaStoneTypes.ANDESITE,
                        stoneType -> new StonePathBlock(Utils.copyPropertySafe(CompatRnRBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRnRStoneType.COBBLED_ROAD).get()))
                )
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE, Registries.BLOCK)
                .addTag(ModTags.Blocks.COBBLED_ROAD_BLOCKS, Registries.BLOCK)
                .addTexture(modRes("block/andesite_loose_cobblestone"), PaletteStrategies.MAIN_CHILD)
                .dropSelf()
                .setTabKey(tab)
                .excludeBlockTypes("tfc:.*")
                .build();
        this.addEntry(COBBLED_ROAD);

        COBBLED_ROAD_STAIRS = SimpleEntrySet.builder(StoneType.class, "cobbled_road_stairs",
                        getModBlock("andesite_cobbled_road_stairs"), () -> VanillaStoneTypes.ANDESITE,
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
                        getModBlock("andesite_cobbled_road_slab"), () -> VanillaStoneTypes.ANDESITE,
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

                    generateBlockModRecipe(sink, stone, COBBLED_ROAD, cobbledItem);

                    generateLandslideRecipe(sink, stone, COBBLED_ROAD, COBBLED_ROAD);
                    generateLandslideRecipe(sink, stone, COBBLED_ROAD_STAIRS, COBBLED_ROAD_SLAB);
                    generateLandslideRecipe(sink, stone, COBBLED_ROAD_SLAB, COBBLED_ROAD_SLAB);

                    generateMattockRecipe(sink, stone, COBBLED_ROAD, COBBLED_ROAD_STAIRS, "stair");
                    generateMattockRecipe(sink, stone, COBBLED_ROAD, COBBLED_ROAD_SLAB, "slab");

                    if(FLAGSTONE.items.get(stone) != null){
                        generateBrickRecipe(sink, stone.stone.asItem(), FLAGSTONE.items.get(stone), "tfc:chisels", 4, null);
                        ResourceLocation flagstoneItem = Utils.getID(FLAGSTONE.items.get(stone));

                        generateBlockModRecipe(sink, stone, FLAGSTONES, flagstoneItem);

                        generateLandslideRecipe(sink, stone, FLAGSTONES, FLAGSTONES);
                        generateLandslideRecipe(sink, stone, FLAGSTONES_STAIRS, FLAGSTONES_SLAB);
                        generateLandslideRecipe(sink, stone, FLAGSTONES_SLAB, FLAGSTONES_SLAB);

                        generateMattockRecipe(sink, stone, FLAGSTONES, FLAGSTONES_STAIRS, "stair");
                        generateMattockRecipe(sink, stone, FLAGSTONES, FLAGSTONES_SLAB, "slab");
                    }
                }
                if(stone.getChild(VanillaStoneChildKeys.BRICKS) != null){
                    ResourceLocation brickItem = ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID, FirmaCompat.MODID + "/" + stone.getNamespace() + "/" + stone.getTypeName() + "_brick");

                    generateBlockModRecipe(sink, stone, SETT_ROAD, brickItem);

                    generateLandslideRecipe(sink, stone, SETT_ROAD, SETT_ROAD);
                    generateLandslideRecipe(sink, stone, SETT_ROAD_STAIRS, SETT_ROAD_SLAB);
                    generateLandslideRecipe(sink, stone, SETT_ROAD_SLAB, SETT_ROAD_SLAB);

                    generateMattockRecipe(sink, stone, SETT_ROAD, SETT_ROAD_STAIRS, "stair");
                    generateMattockRecipe(sink, stone, SETT_ROAD, SETT_ROAD_SLAB, "slab");
                }
            }
        });
    }

    private void generateBlockModRecipe(ResourceSink sink, StoneType stone, SimpleEntrySet<StoneType, Block> baseEntrySet, ResourceLocation transformItem) {
        Block base = baseEntrySet.blocks.get(stone);
        Block frameBlock = RNRBlocks.BASE_COURSE.get();

        if (base != null){
            ResourceLocation frameID = Utils.getID(frameBlock);
            ResourceLocation baseId = Utils.getID(base);

            if (frameID != null && baseId != null){
                JsonObject recipe = new JsonObject();
                recipe.addProperty("type", "rnr:block_mod");

                JsonArray ingredient = new JsonArray();
                ingredient.add(frameID.toString());
                recipe.add("input_block", ingredient);

                JsonObject input_item = new JsonObject();
                input_item.addProperty("item", transformItem.toString());
                recipe.add("input_item", input_item);

                recipe.addProperty("output_block", baseId.toString());

                ResourceLocation recipePath = ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID,"block_mod/" + baseId.getPath());

                sink.addJson(recipePath, recipe, ResType.RECIPES);
            }
        }
    }

    private void generateLandslideRecipe(ResourceSink sink, StoneType stone, SimpleEntrySet<StoneType, Block> baseEntrySet, SimpleEntrySet<StoneType, Block> outputEntrySet) {
        Block base = baseEntrySet.blocks.get(stone);
        Block output = outputEntrySet.blocks.get(stone);

        if (base != null && output != null) {
            ResourceLocation baseId = Utils.getID(base);
            ResourceLocation outputId = Utils.getID(output);

            JsonObject recipe = new JsonObject();
            recipe.addProperty("type", "tfc:landslide");
            recipe.addProperty("ingredient", baseId.toString());
            recipe.addProperty("result", outputId.toString());

            ResourceLocation recipePath = ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID,"landslide/" + baseId.getPath());

            sink.addJson(recipePath, recipe, ResType.RECIPES);
        }
    }

    private void generateMattockRecipe(ResourceSink sink, StoneType stone, SimpleEntrySet<StoneType, Block> baseEntrySet, SimpleEntrySet<StoneType, Block> outputEntrySet, String chiselMode) {
        Block base = baseEntrySet.blocks.get(stone);
        Block output = outputEntrySet.blocks.get(stone);

        if (base != null && output != null) {
            ResourceLocation baseId = Utils.getID(base);
            ResourceLocation outputId = Utils.getID(output);

            JsonObject recipe = new JsonObject();
            recipe.addProperty("type", "rnr:mattock");
            recipe.addProperty("ingredient", baseId.toString());
            recipe.addProperty("result", outputId.toString());
            recipe.addProperty("mode", chiselMode);

            ResourceLocation recipePath = ResourceLocation.fromNamespaceAndPath(StoneZone.MOD_ID,"mattock/" + chiselMode + "/" + outputId.getPath());

            sink.addJson(recipePath, recipe, ResType.RECIPES);
        }
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

        String output = Utils.getID(outputItem).getPath();
        String outputNamespace = Utils.getID(outputItem).getNamespace();

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
        result.addProperty("item", outputNamespace + ":" + output);
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
}
