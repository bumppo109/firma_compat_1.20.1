package com.bumppo109.firma_compat.datagen.recipes;

import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatOre;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.ModItems;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.*;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.recipes.ChiselRecipe;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class TFCRecipeBuilder implements DataProvider {

    protected final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    protected final PackOutput output;
    protected final String modId;

    public TFCRecipeBuilder(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    // =========================================================
    //                    MOD CONDITIONS
    // =========================================================

    /**
     * Creates a Forge "mod_loaded" condition array.
     */
    @Nullable
    protected JsonArray modLoadedCondition(@Nullable String modid) {
        if (modid == null || modid.isEmpty()) return null;

        JsonObject condition = new JsonObject();
        condition.addProperty("type", "forge:mod_loaded");
        condition.addProperty("modid", modid);

        JsonArray array = new JsonArray();
        array.add(condition);
        return array;
    }

    /**
     * Injects conditions into a recipe JSON if present.
     */
    protected void applyConditions(JsonObject json, @Nullable JsonArray conditions) {
        if (conditions != null && conditions.size() > 0) {
            json.add("conditions", conditions);
        }
    }

    // =========================================================
    //                    SAVE PIPELINE (UPDATED)
    // =========================================================

    protected void saveRecipe(CachedOutput cache, String path, JsonObject recipeJson) {
        saveRecipe(cache, path, recipeJson, null);
    }

    protected void saveRecipe(CachedOutput cache, String path, JsonObject recipeJson, @Nullable JsonArray conditions) {

        applyConditions(recipeJson, conditions);

        Path targetPath = output.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(modId)
                .resolve("recipes")
                .resolve(path + ".json");

        String jsonString = GSON.toJson(recipeJson);
        byte[] data = jsonString.getBytes(StandardCharsets.UTF_8);
        HashCode hash = Hashing.sha256().hashBytes(data);

        try {
            cache.writeIfNeeded(targetPath, data, hash);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save TFC recipe: " + path, e);
        }
    }

    // =========================================================
    //                 CORE INGREDIENT HELPERS
    // =========================================================

    protected JsonObject ingredient(String item) {
        JsonObject obj = new JsonObject();
        obj.addProperty("item", item);
        return obj;
    }

    protected JsonObject ingredient(Item item) {
        return ingredient(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString());
    }

    protected JsonObject tagIngredient(String tag) {
        JsonObject obj = new JsonObject();
        obj.addProperty("tag", tag);
        return obj;
    }

    protected JsonObject fluidIngredient(Fluid fluid, int amount) {
        ResourceLocation inputKey = Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(fluid));

        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("fluid", inputKey.toString());
        ingredient.addProperty("amount", amount);

        return ingredient;
    }

    protected JsonObject notRottenIngredient(Item item) {
        ResourceLocation inputKey = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));

        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("type", "tfc:not_rotten");
        JsonObject inner = new JsonObject();
        inner.addProperty("item", inputKey.toString());
        ingredient.add("ingredient", inner);

        return ingredient;
    }

    protected JsonObject copyFoodIngredient(Item item) {
        ResourceLocation inputKey = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item));

        JsonObject resultItem = new JsonObject();
        JsonObject stack = new JsonObject();
        stack.addProperty("item", inputKey.toString());
        resultItem.add("stack", stack);

        JsonArray modifiers = new JsonArray();
        modifiers.add("tfc:copy_food");
        resultItem.add("modifiers", modifiers);

        return resultItem;
    }

    protected JsonObject simpleResult(Item item, int count) {
        return simpleResult(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString(), count);
    }

    protected JsonObject simpleResult(String item, int count) {
        JsonObject result = new JsonObject();
        result.addProperty("item", item);
        if (count > 1) result.addProperty("count", count);
        return result;
    }

    protected void addBlock(JsonArray array, Supplier<Block> supplier) {
        if (supplier == null) return;
        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(supplier.get());
        if (key != null) array.add(key.toString());
    }

    // =========================================================
    //            TFC BASE RECIPES
    // =========================================================

    public void generateAnvilRecipe(CachedOutput cache,
                                    JsonObject ingredient,
                                    Item resultItem,
                                    Integer count,
                                    int tier,
                                    String[] rules,
                                    boolean applyForgingBonus) {

        ResourceLocation resultKey = BuiltInRegistries.ITEM.getKey(resultItem);

        JsonObject root = new JsonObject();
        root.addProperty("type", "tfc:anvil");
        root.add("input", ingredient);

        // Result
        JsonObject result = new JsonObject();
        result.addProperty("item", resultKey.toString());
        result.addProperty("count", count);
        root.add("result", result);

        root.addProperty("tier", tier);

        // Rules array
        JsonArray rulesArray = new JsonArray();
        for (String rule : rules) {
            rulesArray.add(rule);
        }
        root.add("rules", rulesArray);

        root.addProperty("apply_forging_bonus", applyForgingBonus);

        saveRecipe(cache, "anvil/" + resultKey.getPath(), root);
    }

    public void weldingRecipe(CachedOutput cache,
                                      Item inputA,
                                      Item inputB,
                                      int tier,
                                      Item result) {

        ResourceLocation inputAKey = BuiltInRegistries.ITEM.getKey(inputA);
        ResourceLocation inputBKey = BuiltInRegistries.ITEM.getKey(inputB);
        ResourceLocation resultKey = BuiltInRegistries.ITEM.getKey(result);

        JsonObject root = new JsonObject();
        root.addProperty("type", "tfc:welding");

        // First input
        JsonObject first = new JsonObject();
        first.addProperty("item", inputAKey.toString());
        root.add("first_input", first);

        // Second input
        JsonObject second = new JsonObject();
        second.addProperty("item", inputBKey.toString());
        root.add("second_input", second);

        root.addProperty("tier", tier);

        // Result
        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", resultKey.toString());
        root.add("result", resultObj);

        saveRecipe(cache, "welding/" + resultKey.getPath(), root);
    }

    protected void heatingRecipe(CachedOutput cache, String name, String resultType, JsonObject ingredient, JsonObject result, int temperature, @Nullable String suffix, @Nullable String requiredMod) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:heating");

        json.add("ingredient", ingredient);
        json.add(resultType, result);
        json.addProperty("temperature", temperature);

        saveRecipe(cache, "heating/" + name, json, modLoadedCondition(requiredMod));
    }

    protected void chisel(CachedOutput cache,
                          Block ingredient,           // Changed: now simple string
                          Block result,               // Changed: now simple string
                          ChiselRecipe.Mode mode,
                          @Nullable ItemLike extraDrop,
                          @Nullable String recipeSuffix,
                          @Nullable String requiredMod) {

        ResourceLocation ingredientRes = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(ingredient));
        ResourceLocation resultRes = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(result));
        String name = resultRes.getPath();

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:chisel");

        json.addProperty("ingredient", ingredientRes.toString());
        json.addProperty("result", resultRes.toString());
        json.addProperty("mode", mode.getSerializedName());

        JsonObject extra = new JsonObject();

        if (extraDrop != null) {
            extra.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(extraDrop.asItem())).toString());
            json.add("extra_drop", extra);
        }

        if(recipeSuffix != null){
            name = name + "_" + recipeSuffix;
        }

        saveRecipe(cache, "chisel/" + mode.getSerializedName() + "/" + name, json, modLoadedCondition(requiredMod));
    }

    protected void damageInputsShapeless(CachedOutput cache, JsonElement[] itemArray, Item result, Integer count, @Nullable String suffix, @Nullable String requiredMod) {
        ResourceLocation resultRes = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result));
        String name = resultRes.getPath();

        if(suffix != null) {
            name = name + "_" + suffix;
        }

        JsonObject innerRecipe = vanillaShapelessJson(itemArray, result, count, null);

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:damage_inputs_shapeless_crafting");
        json.add("recipe", innerRecipe);

        saveRecipe(cache, "crafting/" + name, json, modLoadedCondition(requiredMod));
    }

    protected void advancedShaped(CachedOutput cache,
                                  String name,
                                  String[] pattern,
                                  Map<Character, JsonElement> key,
                                  JsonObject result,
                                  int row,
                                  int col,
                                  @Nullable String requiredMod) {

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:advanced_shaped_crafting");

        JsonArray patternArray = new JsonArray();
        for (String p : pattern) patternArray.add(p);
        json.add("pattern", patternArray);

        JsonObject keyObj = new JsonObject();
        key.forEach((c, v) -> keyObj.add(c.toString(), v));
        json.add("key", keyObj);

        json.add("result", result);
        json.addProperty("input_row", row);
        json.addProperty("input_column", col);

        saveRecipe(cache, "crafting/" + name, json, modLoadedCondition(requiredMod));
    }

    protected void advancedShapeless(CachedOutput cache,
                                     String name,
                                     JsonArray ingredients,
                                     JsonObject result,
                                     @Nullable JsonElement primary,
                                     @Nullable String requiredMod) {

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:advanced_shapeless_crafting");

        json.add("ingredients", ingredients);
        json.add("result", result);

        if (primary != null) {
            json.add("primary_ingredient", primary);
        }

        saveRecipe(cache, "crafting/" + name, json, modLoadedCondition(requiredMod));
    }

    /**
     * Creates a TFC Collapse or Landslide recipe.
     *
     * For collapse: ingredient is usually a JsonArray of many block IDs.
     * For landslide: ingredient is usually a single string (block ID).
     *
     * Files are saved to: data/<modid>/tfc/recipes/collapse/<name>.json
     *                    or data/<modid>/tfc/recipes/landslide/<name>.json
     *
     * @param cache       CachedOutput
     * @param recipeType      "collapse" or "landslide"
     * @param recipeSuffix  nullable suffix for recipe name
     * @param recipeName        Recipe name (e.g. "andesite" → andesite.json)
     * @param ingredient  JsonElement: either a JsonArray (for collapse) or a JsonPrimitive/String (for landslide)
     * @param result      Result block ID as string (e.g. "tfc:rock/cobble/andesite")
     */
    protected void collapseOrLandslide(CachedOutput cache, String recipeType, String recipeName, @Nullable String recipeSuffix,
                                       JsonElement ingredient, String result, @Nullable String requiredMod) {

        if(recipeSuffix != null) {
            recipeName = recipeName + "_" + recipeSuffix;
        }

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:" + recipeType);
        json.add("ingredient", ingredient);
        json.addProperty("result", result);

        saveRecipe(cache,recipeType + "/" + recipeName, json, modLoadedCondition(requiredMod));
    }

    protected void collapseOrLandslide(CachedOutput cache, String recipeType, Block input, Block output, @Nullable String suffix, @Nullable String requiredMod) {

        ResourceLocation inputRes =  Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(input));
        ResourceLocation outputRes =  Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(output));
        String recipeName = inputRes.getPath();

        JsonPrimitive ingredient = new JsonPrimitive(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(input)).toString());

        collapseOrLandslide(cache, recipeType, recipeName, suffix, ingredient, outputRes.toString(), requiredMod);
    }

    protected void blockModRecipe(CachedOutput cache,
                                  Item inputItem,
                                  Block inputBlock,
                                  Block outputBlock,
                                  @Nullable String suffix,
                                  @Nullable String requiredMod) {

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

        if(suffix != null){
            recipeName = recipeName + "_" + suffix;
        }

        // Save the recipe
        saveRecipe(cache, "block_mod/" + recipeName, json, modLoadedCondition(requiredMod));
    }

    protected void mattockRecipe(CachedOutput cache,
                                 String name,
                                 String ingredient,
                                 String result,
                                 ChiselRecipe.Mode mode,
                                 @Nullable JsonElement itemIngredient,
                                 @Nullable JsonObject extraDrop,
                                 @Nullable String recipeSuffix,
                                 @Nullable String requiredMod) {

        JsonObject json = new JsonObject();
        json.addProperty("type", "rnr:mattock");
        json.addProperty("ingredient", ingredient);
        json.addProperty("result", result);
        json.addProperty("mode", mode.getSerializedName());

        if (itemIngredient != null) json.add("item_ingredient", itemIngredient);
        if (extraDrop != null) json.add("extra_drop", extraDrop);

        String finalName = (recipeSuffix == null) ? name : name + "_" + recipeSuffix;

        saveRecipe(cache, "mattock/" + mode.getSerializedName() + "/" + finalName, json, modLoadedCondition(requiredMod));
    }

    // =========================================================
    //                VANILLA CRAFTING
    // =========================================================

    protected void vanillaShapeless(CachedOutput cache, JsonElement[] itemArray, Item result, Integer count, @Nullable String group, @Nullable String suffix, @Nullable String requiredMod) {
        ResourceLocation resultRes = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result));
        String name = resultRes.getPath();

        if(suffix != null) {
            name = name + "_" + suffix;
        }

        JsonObject recipeJson = vanillaShapelessJson(itemArray, result, count, group);

        saveRecipe(cache, "crafting/" + name, recipeJson, modLoadedCondition(requiredMod));
    }

    protected JsonObject vanillaShapelessJson(JsonElement[] itemArray, Item result, Integer count, @Nullable String group) {
        ResourceLocation outputRes = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result));

        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shapeless");

        JsonArray ingredients = new JsonArray();

        for (JsonElement item : itemArray) {
            ingredients.add(item);
        }

        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", outputRes.toString());
        resultObj.addProperty("count", count);

        json.add("ingredients", ingredients);
        json.add("result", resultObj);

        if (group != null && !group.isEmpty()) {
            json.addProperty("group", group);
        }

        return json;
    }

    protected void vanillaShaped(CachedOutput cache, String[] pattern, Map<Character, JsonElement> key, Item result, Integer count, @Nullable String requiredMod) {

        Map<Character, JsonElement> finalKey = new HashMap<>(key);

        vanillaShaped(cache, pattern, finalKey, result, count,null, null, requiredMod);
    }

    protected void vanillaShaped(CachedOutput cache, String[] pattern, Map<Character, JsonElement> key, Item result, Integer count,
                                           @Nullable String group, @Nullable String suffix, @Nullable String requiredMod) {
        ResourceLocation resultRes = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result));
        String name = resultRes.getPath();

        if(suffix != null) {
            name = name + "_" + suffix;
        }

        JsonObject recipeJson = vanillaShapedJson(pattern, key, result, count, group);

        saveRecipe(cache, "crafting/" + name, recipeJson, modLoadedCondition(requiredMod));
    }

    protected JsonObject vanillaShapedJson(String[] pattern, Map<Character, JsonElement> key, Item result, Integer count,
                                           @Nullable String group) {

        ResourceLocation outputRes = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result));

        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");

        JsonArray patternArray = new JsonArray();
        for (String row : pattern) patternArray.add(row);
        json.add("pattern", patternArray);

        JsonObject keyObj = new JsonObject();
        key.forEach((ch, ing) -> keyObj.add(ch.toString(), ing));
        json.add("key", keyObj);

        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", outputRes.toString());
        resultObj.addProperty("count", count);


        json.add("result", resultObj);

        if (group != null && !group.isEmpty()) {
            json.addProperty("group", group);
        }

        return json;
    }

    protected void stonecutting(CachedOutput cache, ItemLike input, ItemLike result, Integer count, @Nullable String requiredMod) {
        ResourceLocation inputRes = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(input.asItem()));
        ResourceLocation outputRes = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.asItem()));
        String recipeName = outputRes.getPath() + "_from_" + inputRes.getPath() + "_stonecutting";

        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:stonecutting");

        JsonObject ingredients = new JsonObject();
        ingredients.addProperty("item", inputRes.toString());

        json.add("ingredient", ingredients);
        json.addProperty("result", outputRes.toString());
        json.addProperty("count", count);

        saveRecipe(cache, "stonecutting/" + recipeName, json, modLoadedCondition(requiredMod));
    }

    // =========================================================
    //                GENERIC SAVE IDENTITY
    // =========================================================

    @Override
    public String getName() {
        return modId + " TFC Recipes";
    }
}