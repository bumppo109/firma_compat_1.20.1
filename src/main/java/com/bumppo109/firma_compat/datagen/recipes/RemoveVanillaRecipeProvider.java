package com.bumppo109.firma_compat.datagen.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RemoveVanillaRecipeProvider implements DataProvider, IConditionBuilder {

    // === Your unconditional removals (cleaned up duplicates) ===
    private static final List<String> VANILLA_RECIPES_TO_REMOVE = List.of(
            // Food
            "bread", "rabbit_stew", "beetroot_soup", "mushroom_stew", "pumpkin_pie",
            "cake", "cookie",

            // Metal / Netherite
            "copper_block", "netherite_ingot",
            "netherite_axe", "netherite_pickaxe", "netherite_shovel", "netherite_hoe",
            "netherite_helmet", "netherite_chestplate", "netherite_leggings", "netherite_boots",

            "iron_block",
            "iron_ingot_from_iron_block",
            "iron_bars",
            "iron_nugget",
            "gold_nugget",
            "chain",
            "gold_block",
            "emerald_block",
            "lapis_block",
            "diamond_block",
            "netherite_block",
            "netherite_ingot_from_netherite_block",
            "diamond",

            // Dyeing wool, carpets, beds, terracotta, candles
            "dye_white_wool", "dye_light_gray_wool", "dye_gray_wool", "dye_black_wool",
            "dye_brown_wool", "dye_red_wool", "dye_orange_wool", "dye_yellow_wool",
            "dye_lime_wool", "dye_green_wool", "dye_cyan_wool", "dye_light_blue_wool",
            "dye_blue_wool", "dye_purple_wool", "dye_magenta_wool", "dye_pink_wool",

            "dye_white_carpet", "dye_light_gray_carpet", "dye_gray_carpet", "dye_black_carpet",
            "dye_brown_carpet", "dye_red_carpet", "dye_orange_carpet", "dye_yellow_carpet",
            "dye_lime_carpet", "dye_green_carpet", "dye_cyan_carpet", "dye_light_blue_carpet",
            "dye_blue_carpet", "dye_purple_carpet", "dye_magenta_carpet", "dye_pink_carpet",

            "candle", "white_candle", "light_gray_candle", "gray_candle", "black_candle",
            "brown_candle", "red_candle", "orange_candle", "yellow_candle",
            "lime_candle", "green_candle", "cyan_candle", "light_blue_candle",
            "blue_candle", "purple_candle", "magenta_candle", "pink_candle",

            "dye_white_bed", "dye_light_gray_bed", "dye_gray_bed", "dye_black_bed",
            "dye_brown_bed", "dye_red_bed", "dye_orange_bed", "dye_yellow_bed",
            "dye_lime_bed", "dye_green_bed", "dye_cyan_bed", "dye_light_blue_bed",
            "dye_blue_bed", "dye_purple_bed", "dye_magenta_bed", "dye_pink_bed",

            "white_terracotta", "light_gray_terracotta", "gray_terracotta", "black_terracotta",
            "brown_terracotta", "red_terracotta", "orange_terracotta", "yellow_terracotta",
            "lime_terracotta", "green_terracotta", "cyan_terracotta", "light_blue_terracotta",
            "blue_terracotta", "purple_terracotta", "magenta_terracotta", "pink_terracotta",

            // Stone-related
            "quartz_pillar", "quartz_bricks",
            "nether_bricks", "red_nether_bricks",

            "stone_bricks", "cobblestone", "smooth_stone", "cracked_stone_bricks", "chiseled_stone_bricks",
            "granite", "polished_granite", "andesite", "polished_andesite", "diorite", "polished_diorite",

            "stone_brick_slab_from_stone_stonecutting",
            "stone_brick_stairs_from_stone_stonecutting",
            "stone_brick_walls_from_stone_stonecutting",
            "chiseled_stone_bricks_from_stone_stonecutting",
            "stone_bricks_from_stone_stonecutting",
            "stone_pressure_plate",
            "stone_button",

            "deepslate_bricks", "cobbled_deepslate", "chiseled_deepslate", "polished_deepslate",
            "cracked_deepslate_bricks", "cracked_deepslate_tiles",

            "deepslate_brick_slab_from_cobbled_deepslate_stonecutting",
            "deepslate_brick_stairs_from_cobbled_deepslate_stonecutting",
            "deepslate_brick_walls_from_cobbled_deepslate_stonecutting",
            "deepslate_bricks_from_cobbled_deepslate_stonecutting",
            "deepslate_tiles_from_cobbled_deepslate_stonecutting",

            "sandstone", "smooth_sandstone", "cut_sandstone", "chiseled_sandstone",
            "red_sandstone", "smooth_red_sandstone", "cut_red_sandstone", "chiseled_red_sandstone",

            "prismarine", "prismarine_bricks", "dark_prismarine",

            "smooth_basalt", "polished_basalt",

            "chiseled_polished_blackstone", "polished_blackstone", "polished_blackstone_bricks",
            "cracked_polished_blackstone_bricks", "polished_blackstone_pressure_plate", "polished_blackstone_button",

            "polished_blackstone_bricks_from_blackstone_stonecutting",
            "polished_blackstone_brick_slab_from_blackstone_stonecutting",
            "polished_blackstone_brick_stairs_from_blackstone_stonecutting",
            "polished_blackstone_brick_wall_from_blackstone_stonecutting",
            "chiseled_polished_blackstone_from_blackstone_stonecutting",

            "end_stone_bricks",

            "stone_button", "stone_pressure_plate",
            "polished_blackstone_button", "polished_blackstone_pressure_plate",

            //Natural
            "packed_mud", "mud_bricks",
            "bone_block", "bone_meal_from_bone_block",

            "decorated_pot",
            "stonecutter",
            "smithing_table",
            "blast_furnace",
            "cauldron",
            "chiseled_bookshelf",
            "tripwire_hook",
            "piston",
            "daylight_detector",
            "hopper",
            "observer",
            "detector_rail",
            "activator_rail",
            "minecart",
            "tnt",
            "shears",
            "carrot_on_a_stick",
            "warped_fungus_on_a_stick",
            "crossbow",
            "honey_bottle",

            "yellow_dye_from_dandelion",
            "red_dye_from_poppy",
            "light_blue_dye_from_blue_orchid",
            "magenta_dye_from_allium",
            "light_gray_dye_from_azure_bluet",
            "red_dye_from_tulip",
            "orange_dye_from_orange_tulip",
            "light_gray_dye_from_white_tulip",
            "pink_dye_from_pink_tulip",
            "light_gray_dye_from_oxeye_daisy",
            "blue_dye_from_cornflower",
            "white_dye_from_lily_of_the_valley",
            "orange_dye_from_torchflower",
            "black_dye_from_wither_rose",
            "pink_dye_from_pink_petals",
            "yellow_dye_from_sunflower",
            "magenta_dye_from_lilac",
            "red_dye_from_rose_bush",
            "pink_dye_from_peony",
            "cyan_dye_from_pitcher_plant",

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

    // === Remove these recipes ONLY when a specific mod is loaded ===
    // Format: "recipe_id", "modid"
    private static final List<ConditionalRemoval> CONDITIONAL_REMOVALS = List.of(
            // new ConditionalRemoval("acacia_planks", "firmaciv")
    );

    private final PackOutput packOutput;

    public RemoveVanillaRecipeProvider(PackOutput output) {
        this.packOutput = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        // 1. Unconditional removals (forge:false only)
        for (String recipeName : VANILLA_RECIPES_TO_REMOVE) {
            generateDisabledRecipe(cache, recipeName, null);
        }

        // 2. Conditional removals (forge:false + mod_loaded)
        for (ConditionalRemoval entry : CONDITIONAL_REMOVALS) {
            generateDisabledRecipe(cache, entry.recipeId, entry.modId);
        }

        return CompletableFuture.completedFuture(null);
    }

    /**
     * Generates a disabled recipe JSON in data/minecraft/recipes/
     * If modId is null → only {"conditions": [{"type": "forge:false"}]}
     * If modId is provided → {"conditions": [{"type": "forge:false"}, {"type": "forge:mod_loaded", "modid": "..."}]}
     */
    private void generateDisabledRecipe(CachedOutput cache, String recipeName, @javax.annotation.Nullable String modId) {
        JsonObject root = new JsonObject();
        JsonArray conditions = new JsonArray();

        // Always add forge:false
        JsonObject falseCondition = new JsonObject();
        falseCondition.addProperty("type", "forge:false");
        conditions.add(falseCondition);

        // Add mod_loaded condition if specified
        if (modId != null && !modId.isEmpty()) {
            JsonObject modCondition = new JsonObject();
            modCondition.addProperty("type", "forge:mod_loaded");
            modCondition.addProperty("modid", modId);
            conditions.add(modCondition);
        }

        root.add("conditions", conditions);

        // Write to data/minecraft/recipes/<recipeName>.json
        ResourceLocation recipeId = ResourceLocation.withDefaultNamespace(recipeName);
        DataProvider.saveStable(cache,
                root,
                packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "recipes")
                        .json(recipeId)
        );
    }

    @Override
    public String getName() {
        return "Firma Compat - Vanilla Recipe Removals";
    }

    // Simple record for conditional entries
    private record ConditionalRemoval(String recipeId, String modId) {}
}