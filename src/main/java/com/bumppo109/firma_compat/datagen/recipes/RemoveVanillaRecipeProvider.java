package com.bumppo109.firma_compat.datagen.recipes;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import java.util.List;
import java.util.function.Consumer;

public class RemoveVanillaRecipeProvider extends RecipeProvider implements IConditionBuilder {

    // ===================== DEFINE YOUR RECIPE REMOVALS HERE =====================

    // Recipes to ALWAYS remove (unconditional)
    private static final List<String> UNCONDITIONAL_REMOVALS = List.of(
            //Stone
            "minecraft:stone_bricks",
            "minecraft:cobblestone",
            "minecraft:smooth_stone",
            "minecraft:cracked_stone_bricks",
            "minecraft:chiseled_stone_bricks",

            "minecraft:granite",
            "minecraft:polished_granite",
            "minecraft:andesite",
            "minecraft:polished_andesite",
            "minecraft:diorite",
            "minecraft:polished_diorite",

            "minecraft:deepslate_bricks",
            "minecraft:cobbled_deepslate",
            "minecraft:chiseled_deepslate",
            "minecraft:polished_deepslate",
            "minecraft:cracked_deepslate",
            "minecraft:cracked_deepslate_tiles",

            "minecraft:smooth_sandstone",
            "minecraft:cut_sandstone",
            "minecraft:chiseled_sandstone",
            "minecraft:smooth_red_sandstone",
            "minecraft:cut_red_sandstone",
            "minecraft:chiseled_red_sandstone",

            "minecraft:smooth_basalt",
            "minecraft:polished_basalt",

            "minecraft:chiseled_polished_blackstone",
            "minecraft:polished_blackstone",
            "minecraft:polished_blackstone_bricks",
            "minecraft:chiseled_polished_blackstone",
            "minecraft:cracked_polished_blackstone_bricks",

            "minecraft:end_stone_bricks",

            "minecraft:stone_button",
            "minecraft:polished_blackstone_button",

            //Wood
            "minecraft:acacia_log",
            "minecraft:acacia_stripped_log",
            "minecraft:acacia_wood",
            "minecraft:acacia_stripped_wood",
            "minecraft:acacia_planks",
            "minecraft:acacia_fence",
            "minecraft:acacia_fence_gate",
            "minecraft:acacia_sign",
            "minecraft:acacia_hanging_sign",
            "minecraft:acacia_trapdoor",
            "minecraft:acacia_door",
            "minecraft:acacia_pressure_plate",

            "minecraft:birch_log",
            "minecraft:birch_stripped_log",
            "minecraft:birch_wood",
            "minecraft:birch_stripped_wood",
            "minecraft:birch_planks",
            "minecraft:birch_fence",
            "minecraft:birch_fence_gate",
            "minecraft:birch_sign",
            "minecraft:birch_hanging_sign",
            "minecraft:birch_trapdoor",
            "minecraft:birch_door",
            "minecraft:birch_pressure_plate",

            "minecraft:cherry_log",
            "minecraft:cherry_stripped_log",
            "minecraft:cherry_wood",
            "minecraft:cherry_stripped_wood",
            "minecraft:cherry_planks",
            "minecraft:cherry_fence",
            "minecraft:cherry_fence_gate",
            "minecraft:cherry_sign",
            "minecraft:cherry_hanging_sign",
            "minecraft:cherry_trapdoor",
            "minecraft:cherry_door",
            "minecraft:cherry_pressure_plate",

            "minecraft:dark_oak_log",
            "minecraft:dark_oak_stripped_log",
            "minecraft:dark_oak_wood",
            "minecraft:dark_oak_stripped_wood",
            "minecraft:dark_oak_planks",
            "minecraft:dark_oak_fence",
            "minecraft:dark_oak_fence_gate",
            "minecraft:dark_oak_sign",
            "minecraft:dark_oak_hanging_sign",
            "minecraft:dark_oak_trapdoor",
            "minecraft:dark_oak_door",
            "minecraft:dark_oak_pressure_plate",

            "minecraft:jungle_log",
            "minecraft:jungle_stripped_log",
            "minecraft:jungle_wood",
            "minecraft:jungle_stripped_wood",
            "minecraft:jungle_planks",
            "minecraft:jungle_fence",
            "minecraft:jungle_fence_gate",
            "minecraft:jungle_sign",
            "minecraft:jungle_hanging_sign",
            "minecraft:jungle_trapdoor",
            "minecraft:jungle_door",
            "minecraft:jungle_pressure_plate",

            "minecraft:mangrove_log",
            "minecraft:mangrove_stripped_log",
            "minecraft:mangrove_wood",
            "minecraft:mangrove_stripped_wood",
            "minecraft:mangrove_planks",
            "minecraft:mangrove_fence",
            "minecraft:mangrove_fence_gate",
            "minecraft:mangrove_sign",
            "minecraft:mangrove_hanging_sign",
            "minecraft:mangrove_trapdoor",
            "minecraft:mangrove_door",
            "minecraft:mangrove_pressure_plate",

            "minecraft:oak_log",
            "minecraft:oak_stripped_log",
            "minecraft:oak_wood",
            "minecraft:oak_stripped_wood",
            "minecraft:oak_planks",
            "minecraft:oak_fence",
            "minecraft:oak_fence_gate",
            "minecraft:oak_sign",
            "minecraft:oak_hanging_sign",
            "minecraft:oak_trapdoor",
            "minecraft:oak_door",
            "minecraft:oak_pressure_plate",

            "minecraft:spruce_log",
            "minecraft:spruce_stripped_log",
            "minecraft:spruce_wood",
            "minecraft:spruce_stripped_wood",
            "minecraft:spruce_planks",
            "minecraft:spruce_fence",
            "minecraft:spruce_fence_gate",
            "minecraft:spruce_sign",
            "minecraft:spruce_hanging_sign",
            "minecraft:spruce_trapdoor",
            "minecraft:spruce_door",
            "minecraft:spruce_pressure_plate",

            "minecraft:crimson_stem",
            "minecraft:crimson_stripped_stem",
            "minecraft:crimson_hyphae",
            "minecraft:crimson_stripped_hyphae",
            "minecraft:crimson_planks",
            "minecraft:crimson_fence",
            "minecraft:crimson_fence_gate",
            "minecraft:crimson_sign",
            "minecraft:crimson_hanging_sign",
            "minecraft:crimson_trapdoor",
            "minecraft:crimson_door",
            "minecraft:crimson_pressure_plate",

            "minecraft:warped_stem",
            "minecraft:warped_stripped_stem",
            "minecraft:warped_hyphae",
            "minecraft:warped_stripped_hyphae",
            "minecraft:warped_planks",
            "minecraft:warped_fence",
            "minecraft:warped_fence_gate",
            "minecraft:warped_sign",
            "minecraft:warped_hanging_sign",
            "minecraft:warped_trapdoor",
            "minecraft:warped_door",
            "minecraft:warped_pressure_plate"
    );

    // Recipes to remove ONLY if a specific mod is loaded
    // Format: "recipe_id"  →  "modid"
    private static final List<ConditionalRemoval> CONDITIONAL_REMOVALS = List.of(
            //new ConditionalRemoval("minecraft:bread", "firmaciv")
    );

    // ===========================================================================

    public RemoveVanillaRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        // Process unconditional removals
        for (String recipeId : UNCONDITIONAL_REMOVALS) {
            addFalseRecipe(pWriter, ResourceLocation.parse(recipeId));
        }

        // Process conditional removals
        for (ConditionalRemoval entry : CONDITIONAL_REMOVALS) {
            addConditionalFalseRecipe(pWriter,
                    ResourceLocation.parse(entry.recipeId),
                    entry.modId);
        }
    }

    /**
     * Creates the minimal JSON with only {"conditions": [{"type": "forge:false"}]}
     */
    private void addFalseRecipe(Consumer<FinishedRecipe> pWriter, ResourceLocation recipeId) {
        ConditionalRecipe.builder()
                .addCondition(FALSE())
                .addRecipe(c -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.AIR)
                        .requires(Items.AIR)
                        .unlockedBy("has_air", has(Items.AIR))
                        .save(c, "forge:remove_dummy"))
                .build(pWriter, recipeId);
    }

    private void addConditionalFalseRecipe(Consumer<FinishedRecipe> pWriter, ResourceLocation recipeId, String modId) {
        ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition(modId))
                .addRecipe(innerConsumer -> ConditionalRecipe.builder()
                        .addCondition(FALSE())
                        .addRecipe(dummyConsumer -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.AIR)
                                .requires(Items.AIR)
                                .unlockedBy("has_air", has(Items.AIR))
                                .save(dummyConsumer, "forge:remove_dummy"))
                        .build(innerConsumer, recipeId))
                .build(pWriter, recipeId);
    }

    // Helper record for conditional removals
    private record ConditionalRemoval(String recipeId, String modId) {
    }
}