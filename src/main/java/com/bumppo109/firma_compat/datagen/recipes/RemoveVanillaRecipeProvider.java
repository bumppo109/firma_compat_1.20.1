package com.bumppo109.firma_compat.datagen.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.function.Consumer;

public class RemoveVanillaRecipeProvider extends RecipeProvider implements IConditionBuilder {
    // Recipes to ALWAYS remove (unconditional)
    private static final List<String> MINECRAFT_UNCONDITIONAL_REMOVALS = List.of(
            //Food
            "bread",
            "rabbit_stew",
            "beetroot_soup",
            "mushroom_stew",
            "pumpkin_pie",
            "cake",
            "cookie",
            "rabbit_stew",
            "mushroom_soup",
            "beetroot_soup",

            //Metal
            "copper_block",
            "netherite_axe",
            "netherite_pickaxe",
            "netherite_shovel",
            "netherite_hoe",
            "netherite_helmet",
            "netherite_chestplate",
            "netherite_leggings",
            "netherite_boots",
            "netherite_ingot",

            //Dye
            "dye_white_wool",
            "dye_light_gray_wool",
            "dye_gray_wool",
            "dye_black_wool",
            "dye_brown_wool",
            "dye_red_wool",
            "dye_orange_wool",
            "dye_yellow_wool",
            "dye_lime_wool",
            "dye_green_wool",
            "dye_cyan_wool",
            "dye_light_blue_wool",
            "dye_blue_wool",
            "dye_purple_wool",
            "dye_magenta_wool",
            "dye_pink_wool",

            "dye_white_carpet",
            "dye_light_gray_carpet",
            "dye_gray_carpet",
            "dye_black_carpet",
            "dye_brown_carpet",
            "dye_red_carpet",
            "dye_orange_carpet",
            "dye_yellow_carpet",
            "dye_lime_carpet",
            "dye_green_carpet",
            "dye_cyan_carpet",
            "dye_light_blue_carpet",
            "dye_blue_carpet",
            "dye_purple_carpet",
            "dye_magenta_carpet",
            "dye_pink_carpet",

            "dye_white_bed",
            "dye_light_gray_bed",
            "dye_gray_bed",
            "dye_black_bed",
            "dye_brown_bed",
            "dye_red_bed",
            "dye_orange_bed",
            "dye_yellow_bed",
            "dye_lime_bed",
            "dye_green_bed",
            "dye_cyan_bed",
            "dye_light_blue_bed",
            "dye_blue_bed",
            "dye_purple_bed",
            "dye_magenta_bed",
            "dye_pink_bed",

            "white_terracotta",
            "light_gray_terracotta",
            "gray_terracotta",
            "black_terracotta",
            "brown_terracotta",
            "red_terracotta",
            "orange_terracotta",
            "yellow_terracotta",
            "lime_terracotta",
            "green_terracotta",
            "cyan_terracotta",
            "light_blue_terracotta",
            "blue_terracotta",
            "purple_terracotta",
            "magenta_terracotta",
            "pink_terracotta",

            "white_candle",
            "light_gray_candle",
            "gray_candle",
            "black_candle",
            "brown_candle",
            "red_candle",
            "orange_candle",
            "yellow_candle",
            "lime_candle",
            "green_candle",
            "cyan_candle",
            "light_blue_candle",
            "blue_candle",
            "purple_candle",
            "magenta_candle",
            "pink_candle",
            //Stone
            "prismarine_bricks",
            "quartz_pillar",
            "packed_mud",
            "mud_bricks",
            "quartz_bricks",
            "nether_bricks",

            "stone_bricks",
            "cobblestone",
            "smooth_stone",
            "cracked_stone_bricks",
            "chiseled_stone_bricks",

            "granite",
            "polished_granite",
            "andesite",
            "polished_andesite",
            "diorite",
            "polished_diorite",

            "deepslate_bricks",
            "cobbled_deepslate",
            "chiseled_deepslate",
            "polished_deepslate",
            "cracked_deepslate",
            "cracked_deepslate_tiles",

            "smooth_sandstone",
            "cut_sandstone",
            "chiseled_sandstone",
            "smooth_red_sandstone",
            "cut_red_sandstone",
            "chiseled_red_sandstone",

            "smooth_basalt",
            "polished_basalt",

            "chiseled_polished_blackstone",
            "polished_blackstone",
            "polished_blackstone_bricks",
            "cracked_polished_blackstone_bricks",

            "end_stone_bricks",

            "stone_button",
            "stone_pressure_plate",
            "polished_blackstone_button",
            "polished_blackstone_pressure_plate",

            //Wood
            "bamboo_planks",
            "bamboo_fence",
            "bamboo_fence_gate",
            "bamboo_door",
            "bamboo_trapdoor",
            "bamboo_pressure_plate",

            "acacia_log",
            "acacia_stripped_log",
            "acacia_wood",
            "acacia_stripped_wood",
            "acacia_planks",
            "acacia_fence",
            "acacia_fence_gate",
            "acacia_sign",
            "acacia_hanging_sign",
            "acacia_trapdoor",
            "acacia_door",
            "acacia_pressure_plate",

            "birch_log",
            "birch_stripped_log",
            "birch_wood",
            "birch_stripped_wood",
            "birch_planks",
            "birch_fence",
            "birch_fence_gate",
            "birch_sign",
            "birch_hanging_sign",
            "birch_trapdoor",
            "birch_door",
            "birch_pressure_plate",

            "cherry_log",
            "cherry_stripped_log",
            "cherry_wood",
            "cherry_stripped_wood",
            "cherry_planks",
            "cherry_fence",
            "cherry_fence_gate",
            "cherry_sign",
            "cherry_hanging_sign",
            "cherry_trapdoor",
            "cherry_door",
            "cherry_pressure_plate",

            "dark_oak_log",
            "dark_oak_stripped_log",
            "dark_oak_wood",
            "dark_oak_stripped_wood",
            "dark_oak_planks",
            "dark_oak_fence",
            "dark_oak_fence_gate",
            "dark_oak_sign",
            "dark_oak_hanging_sign",
            "dark_oak_trapdoor",
            "dark_oak_door",
            "dark_oak_pressure_plate",

            "jungle_log",
            "jungle_stripped_log",
            "jungle_wood",
            "jungle_stripped_wood",
            "jungle_planks",
            "jungle_fence",
            "jungle_fence_gate",
            "jungle_sign",
            "jungle_hanging_sign",
            "jungle_trapdoor",
            "jungle_door",
            "jungle_pressure_plate",

            "mangrove_log",
            "mangrove_stripped_log",
            "mangrove_wood",
            "mangrove_stripped_wood",
            "mangrove_planks",
            "mangrove_fence",
            "mangrove_fence_gate",
            "mangrove_sign",
            "mangrove_hanging_sign",
            "mangrove_trapdoor",
            "mangrove_door",
            "mangrove_pressure_plate",

            "oak_log",
            "oak_stripped_log",
            "oak_wood",
            "oak_stripped_wood",
            "oak_planks",
            "oak_fence",
            "oak_fence_gate",
            "oak_sign",
            "oak_hanging_sign",
            "oak_trapdoor",
            "oak_door",
            "oak_pressure_plate",

            "spruce_log",
            "spruce_stripped_log",
            "spruce_wood",
            "spruce_stripped_wood",
            "spruce_planks",
            "spruce_fence",
            "spruce_fence_gate",
            "spruce_sign",
            "spruce_hanging_sign",
            "spruce_trapdoor",
            "spruce_door",
            "spruce_pressure_plate",

            "crimson_stem",
            "crimson_stripped_stem",
            "crimson_hyphae",
            "crimson_stripped_hyphae",
            "crimson_planks",
            "crimson_fence",
            "crimson_fence_gate",
            "crimson_sign",
            "crimson_hanging_sign",
            "crimson_trapdoor",
            "crimson_door",
            "crimson_pressure_plate",

            "warped_stem",
            "warped_stripped_stem",
            "warped_hyphae",
            "warped_stripped_hyphae",
            "warped_planks",
            "warped_fence",
            "warped_fence_gate",
            "warped_sign",
            "warped_hanging_sign",
            "warped_trapdoor",
            "warped_door",
            "warped_pressure_plate"
    );

    // Conditional removals (only if a mod is loaded)
    private static final List<ConditionalRemoval> MINECRAFT_CONDITIONAL_REMOVALS = List.of(
            // Example: new ConditionalRemoval("bread", "firmaciv")
    );

    public RemoveVanillaRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        // This is no longer used for dynamic generation
    }

    /**
     * Called from DataGenerators to generate all removal recipes dynamically
     */
    public void generateRemovalRecipes(ResourceSink sink) {
        // Unconditional removals
        for (String recipeId : MINECRAFT_UNCONDITIONAL_REMOVALS) {
            String path = "recipes/" + recipeId;
            generateDisabledRecipe(sink, path);
        }

        // Conditional removals
        for (ConditionalRemoval entry : MINECRAFT_CONDITIONAL_REMOVALS) {
            String path = "recipes/" + entry.recipeId;
            generateDisabledRecipeWithModCondition(sink, path, entry.modId);
        }
    }

    /**
     * Simple disabled recipe: {"conditions": [{"type": "forge:false"}]}
     */
    private void generateDisabledRecipe(ResourceSink sink, String recipePath) {
        JsonObject root = new JsonObject();
        JsonArray conditions = new JsonArray();
        JsonObject condition = new JsonObject();
        condition.addProperty("type", "forge:false");
        conditions.add(condition);
        root.add("conditions", conditions);

        ResourceLocation loc = ResourceLocation.withDefaultNamespace(recipePath);
        sink.addJson(loc, root, ResType.RECIPES);
    }

    /**
     * Disabled recipe with additional mod loaded condition
     */
    private void generateDisabledRecipeWithModCondition(ResourceSink sink, String recipePath, String modId) {
        JsonObject root = new JsonObject();
        JsonArray conditions = new JsonArray();

        // forge:false
        JsonObject falseCondition = new JsonObject();
        falseCondition.addProperty("type", "forge:false");
        conditions.add(falseCondition);

        // forge:mod_loaded
        JsonObject modCondition = new JsonObject();
        modCondition.addProperty("type", "forge:mod_loaded");
        modCondition.addProperty("modid", modId);
        conditions.add(modCondition);

        root.add("conditions", conditions);

        ResourceLocation loc = ResourceLocation.withDefaultNamespace(recipePath);
        sink.addJson(loc, root, ResType.RECIPES);
    }

    private record ConditionalRemoval(String recipeId, String modId) {}
}