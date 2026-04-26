package com.bumppo109.firma_compat.datagen.recipes;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.ModItems;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public abstract class TFCRecipeBuilder implements DataProvider {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    protected final PackOutput output;
    protected final String modId;

    public TFCRecipeBuilder(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    /**
     * Creates a TFC heating recipe that turns raw food into cooked food
     * Example: beef → cooked_beef at 200°C
     */
    public void generateFoodHeatingRecipe(CachedOutput cache, Item inputFood, Item outputFood) {
        ResourceLocation inputKey = BuiltInRegistries.ITEM.getKey(inputFood);
        ResourceLocation outputKey = BuiltInRegistries.ITEM.getKey(outputFood);

        JsonObject root = new JsonObject();
        root.addProperty("type", "tfc:heating");

        // Ingredient with not_rotten wrapper
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("type", "tfc:not_rotten");
        JsonObject inner = new JsonObject();
        inner.addProperty("item", inputKey.toString());
        ingredient.add("ingredient", inner);
        root.add("ingredient", ingredient);

        // Result item with copy_food modifier
        JsonObject resultItem = new JsonObject();
        JsonObject stack = new JsonObject();
        stack.addProperty("item", outputKey.toString());
        resultItem.add("stack", stack);

        JsonArray modifiers = new JsonArray();
        modifiers.add("tfc:copy_food");
        resultItem.add("modifiers", modifiers);
        root.add("result_item", resultItem);

        root.addProperty("temperature", 200);

        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                "recipes/heating/food/" + outputKey.getPath() + ".json");

        saveRecipe(cache, loc.toString(), root);
    }

    /**
     * Creates a TFC heating recipe that melts a tool into molten metal
     * Example: bismuth_bronze axe → 100mb bismuth bronze at 985°C
     */
    public void generateToolMeltingRecipe(CachedOutput cache, CompatMetal metal, Item input, int amount) {
        ResourceLocation inputKey = BuiltInRegistries.ITEM.getKey(input);
        ResourceLocation moltenKey = BuiltInRegistries.FLUID.getKey(ModFluids.METALS.get(metal).getSource());

        JsonObject root = new JsonObject();
        root.addProperty("type", "tfc:heating");

        // Ingredient: metal tool
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", inputKey.toString());
        root.add("ingredient", ingredient);

        // Result fluid
        JsonObject resultFluidObj = new JsonObject();
        resultFluidObj.addProperty("fluid", moltenKey.toString());
        resultFluidObj.addProperty("amount", amount);
        root.add("result_fluid", resultFluidObj);

        root.addProperty("temperature", metal.meltTemp());
        root.addProperty("use_durability", true);

        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                "recipes/heating/" + inputKey.getPath() + ".json");

        saveRecipe(cache, loc.toString(), root);
    }

    public void generateMeltingRecipe(CachedOutput cache, CompatMetal metal, Item input, int amount) {
        ResourceLocation inputKey = BuiltInRegistries.ITEM.getKey(input);
        ResourceLocation moltenKey = BuiltInRegistries.FLUID.getKey(ModFluids.METALS.get(metal).getSource());

        JsonObject root = new JsonObject();
        root.addProperty("type", "tfc:heating");

        // Ingredient: metal tool
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", inputKey.toString());
        root.add("ingredient", ingredient);

        // Result fluid
        JsonObject resultFluidObj = new JsonObject();
        resultFluidObj.addProperty("fluid", moltenKey.toString());
        resultFluidObj.addProperty("amount", amount);
        root.add("result_fluid", resultFluidObj);

        root.addProperty("temperature", metal.meltTemp());

        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                "recipes/heating/" + inputKey.getPath() + ".json");

        saveRecipe(cache, loc.toString(), root);
    }

    public void generateMeltingRecipe(CachedOutput cache, Metal.Default metal, Item input, int amount, int temperature) {
        ResourceLocation inputKey = BuiltInRegistries.ITEM.getKey(input);
        ResourceLocation moltenKey = BuiltInRegistries.FLUID.getKey(TFCFluids.METALS.get(metal).getSource());

        JsonObject root = new JsonObject();
        root.addProperty("type", "tfc:heating");

        // Ingredient: metal tool
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", inputKey.toString());
        root.add("ingredient", ingredient);

        // Result fluid
        JsonObject resultFluidObj = new JsonObject();
        resultFluidObj.addProperty("fluid", moltenKey.toString());
        resultFluidObj.addProperty("amount", amount);
        root.add("result_fluid", resultFluidObj);

        root.addProperty("temperature", temperature);

        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                "recipes/heating/" + inputKey.getPath() + ".json");

        saveRecipe(cache, loc.toString(), root);
    }

    public void generateMeltingRecipe(CachedOutput cache, CompatMetal metal, CompatMetal.ItemType itemType) {
        ResourceLocation inputKey = BuiltInRegistries.ITEM.getKey(ModItems.METAL_ITEMS.get(metal).get(itemType).get());
        ResourceLocation moltenKey = BuiltInRegistries.FLUID.getKey(ModFluids.METALS.get(metal).getSource());

        JsonObject root = new JsonObject();
        root.addProperty("type", "tfc:heating");

        // Ingredient: metal tool
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", inputKey.toString());
        root.add("ingredient", ingredient);

        // Result fluid
        JsonObject resultFluidObj = new JsonObject();
        resultFluidObj.addProperty("fluid", moltenKey.toString());
        resultFluidObj.addProperty("amount", itemType.metalAmount());
        root.add("result_fluid", resultFluidObj);

        root.addProperty("temperature", metal.meltTemp());

        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,
                "recipes/heating/" + inputKey.getPath() + ".json");

        saveRecipe(cache, loc.toString(), root);
    }

    /**
     * Generates a TFC Welding recipe.
     *
     * Example: Unfinished bismuth bronze chestplate + double sheet → finished chestplate
     *
     * @param cache          cache
     * @param inputA    Item ID of the first input (usually the unfinished piece)
     * @param inputB   Item ID of the second input (usually the double sheet)
     * @param tier          Welding tier (1 = stone anvil, 2 = bronze, 3 = wrought iron, 4 = steel, 5 = black steel, 6 = blue/red steel)
     * @param result        Resulting item ID
     */
    public void generateWeldingRecipe(CachedOutput cache,
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

        // Output path: recipes/welding/bismuth_bronze_chestplate.json
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(
                FirmaCompat.MODID,
                "recipes/welding/" + resultKey.getPath() + ".json"
        );

        saveRecipe(cache, loc.toString(), root);

        FirmaCompat.LOGGER.debug("Generated welding recipe: {}", loc);
    }

    /**
     *
     * @param cache
     * @param inputTag
     * @param resultItem
     * @param tier
     * @param rules
     * @param applyForgingBonus
     */
    public void generateAnvilRecipe(CachedOutput cache,
                                     String inputTag,
                                     Item resultItem,
                                     int tier,
                                     String[] rules,
                                     boolean applyForgingBonus) {

        ResourceLocation resultKey = BuiltInRegistries.ITEM.getKey(resultItem);

        JsonObject root = new JsonObject();
        root.addProperty("type", "tfc:anvil");

        // Input Tag
        JsonObject input = new JsonObject();
        input.addProperty("tag", inputTag);
        root.add("input", input);

        // Result
        JsonObject result = new JsonObject();
        result.addProperty("item", resultItem.toString());
        root.add("result", result);

        root.addProperty("tier", tier);

        // Rules array
        JsonArray rulesArray = new JsonArray();
        for (String rule : rules) {
            rulesArray.add(rule);
        }
        root.add("rules", rulesArray);

        root.addProperty("apply_forging_bonus", applyForgingBonus);

        // Output location
        ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(
                FirmaCompat.MODID,
                "recipes/anvil/" + resultKey.getPath() + ".json"
        );

        saveRecipe(cache, loc.toString(), root);

        FirmaCompat.LOGGER.info("Generated TFC anvil recipe: {}", loc);
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
                                       JsonElement ingredient, String result) {

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:" + recipeType);
        json.add("ingredient", ingredient);
        json.addProperty("result", result);

        saveRecipe(cache,recipeType + "/" + recipeName, json);
    }

    protected void landslide(CachedOutput cache, String name, String ingredient, String result, @Nullable String recipeSuffix) {
        collapseOrLandslide(cache, "landslide", name, recipeSuffix, new com.google.gson.JsonPrimitive(ingredient), result);
    }

    // ====================== Crafting ======================

    /**
     * Base method for tfc:advanced_shaped_crafting
     */
    protected void advancedShaped(CachedOutput cache, String name,
                                  String[] pattern,
                                  Map<Character, JsonElement> key,
                                  JsonObject resultProvider,
                                  int inputRow, int inputColumn) {

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:advanced_shaped_crafting");

        JsonArray patternArray = new JsonArray();
        for (String row : pattern) patternArray.add(row);
        json.add("pattern", patternArray);

        JsonObject keyObj = new JsonObject();
        key.forEach((ch, ing) -> keyObj.add(ch.toString(), ing));
        json.add("key", keyObj);

        json.add("result", resultProvider);
        json.addProperty("input_row", inputRow);
        json.addProperty("input_column", inputColumn);

        saveRecipe(cache, "crafting/" + name, json);
    }

    /**
     * Base method for tfc:advanced_shapeless_crafting
     */
    protected void advancedShapeless(CachedOutput cache, String name,
                                     JsonArray ingredients,
                                     JsonObject resultProvider,
                                     JsonElement primaryIngredient) {   // can be null

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:advanced_shapeless_crafting");

        json.add("ingredients", ingredients);
        json.add("result", resultProvider);

        if (primaryIngredient != null) {
            json.add("primary_ingredient", primaryIngredient);
        }

        saveRecipe(cache, "crafting/" + name, json);
    }

    /**
     * tfc:damage_inputs_shaped_crafting  ← CORRECTED
     */
    protected void damageInputsShaped(CachedOutput cache, String name,
                                      String[] pattern,
                                      Map<Character, JsonElement> key,
                                      JsonObject result) {   // Usually simpleResult()

        JsonObject innerRecipe = new JsonObject();
        innerRecipe.addProperty("type", "minecraft:crafting_shaped");

        JsonArray patternArray = new JsonArray();
        for (String row : pattern) patternArray.add(row);
        innerRecipe.add("pattern", patternArray);

        JsonObject keyObj = new JsonObject();
        key.forEach((ch, ing) -> keyObj.add(ch.toString(), ing));
        innerRecipe.add("key", keyObj);

        innerRecipe.add("result", result);

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:damage_inputs_shaped_crafting");
        json.add("recipe", innerRecipe);

        saveRecipe(cache, "crafting/" + name, json);
    }

    /**
     * tfc:damage_inputs_shapeless_crafting  ← CORRECTED
     */
    protected void damageInputsShapeless(CachedOutput cache, String name,
                                         JsonArray ingredients,
                                         JsonObject result) {   // Usually simpleResult()

        JsonObject innerRecipe = new JsonObject();
        innerRecipe.addProperty("type", "minecraft:crafting_shapeless");
        innerRecipe.add("ingredients", ingredients);
        innerRecipe.add("result", result);

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:damage_inputs_shapeless_crafting");
        json.add("recipe", innerRecipe);

        saveRecipe(cache, "crafting/" + name, json);
    }

    /**
     * Base method for vanilla minecraft:crafting_shaped
     */
    protected void vanillaShaped(CachedOutput cache, Item item,
                                 @Nullable String suffix,
                                 String[] pattern,
                                 Map<Character, JsonElement> key,
                                 JsonObject result,
                                 @Nullable String group) {

        String itemPath = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();

        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");

        JsonArray patternArray = new JsonArray();
        for (String row : pattern) {
            patternArray.add(row);
        }
        json.add("pattern", patternArray);

        JsonObject keyObj = new JsonObject();
        key.forEach((ch, ing) -> keyObj.add(ch.toString(), ing));
        json.add("key", keyObj);

        json.add("result", result);

        if (group != null && !group.isEmpty()) {
            json.addProperty("group", group);
        }

        saveRecipe(cache, "crafting/" + itemPath + "_" + suffix, json);
    }

    protected void vanillaShaped(CachedOutput cache, Item item,
                                 String[] pattern,
                                 Map<Character, JsonElement> key,
                                 JsonObject result,
                                 @Nullable String group) {

        String itemPath = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();

        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");

        JsonArray patternArray = new JsonArray();
        for (String row : pattern) {
            patternArray.add(row);
        }
        json.add("pattern", patternArray);

        JsonObject keyObj = new JsonObject();
        key.forEach((ch, ing) -> keyObj.add(ch.toString(), ing));
        json.add("key", keyObj);

        json.add("result", result);

        if (group != null && !group.isEmpty()) {
            json.addProperty("group", group);
        }

        saveRecipe(cache, "crafting/" + itemPath, json);
    }

    /**
     * Base method for vanilla minecraft:crafting_shapeless
     */
    protected void vanillaShapeless(CachedOutput cache, Item item,
                                    @Nullable String suffix,
                                    JsonArray ingredients,
                                    JsonObject result,
                                    @Nullable String group) {

        String itemPath = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath();

        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shapeless");

        json.add("ingredients", ingredients);
        json.add("result", result);

        if (group != null && !group.isEmpty()) {
            json.addProperty("group", group);
        }

        saveRecipe(cache, "crafting/" + itemPath, json);
    }

    // ========================= Chisel =============================

    /**
     * Base method for tfc:chisel
     */
    protected void chisel(CachedOutput cache, String name,
                          String ingredient,           // Changed: now simple string
                          String result,               // Changed: now simple string
                          ChiselMode mode,
                          @Nullable JsonElement itemIngredient,
                          @Nullable JsonObject extraDrop,
                          @Nullable String recipeSuffix) {

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:chisel");

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

        saveRecipe(cache, "chisel/" + mode.getSerializedName() + "/" + name, json);
    }

    // ========================= Collapse/Landslide =============================

    /**
     * Base method for tfc:collapse
     */
    protected void collapse(CachedOutput cache, String name,
                            JsonElement blockInput,
                            JsonElement blockResult,
                            boolean copyInput) {

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:collapse");
        json.add("ingredient", blockInput);
        if (copyInput) {
            json.addProperty("copy_input", true);
        } else {
            json.add("result", blockResult);
        }

        saveRecipe(cache, "collapse/" + name, json);
    }

    /**
     * Base method for tfc:landslide
     */
    protected void landslide(CachedOutput cache, String name,
                            JsonElement blockInput,
                            JsonElement blockResult,
                            boolean copyInput) {

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:landslide");
        json.add("ingredient", blockInput);
        if (copyInput) {
            json.addProperty("copy_input", true);
        } else {
            json.add("result", blockResult);
        }

        saveRecipe(cache, "landslide/" + name, json);
    }

    // ====================== Helpers ======================

    protected JsonObject ingredient(String item) {
        JsonObject obj = new JsonObject();
        obj.addProperty("item", item);
        return obj;
    }
    protected JsonObject ingredient(Item item) {
        String itemName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString();

        JsonObject obj = new JsonObject();
        obj.addProperty("item", itemName);
        return obj;
    }

    protected JsonObject tagIngredient(String tag) {
        JsonObject obj = new JsonObject();
        obj.addProperty("tag", tag);
        return obj;
    }

    protected JsonObject simpleResult(String item, int count) {
        JsonObject result = new JsonObject();
        result.addProperty("item", item);
        if (count > 1) result.addProperty("count", count);
        return result;
    }

    protected JsonObject simpleResult(Item item, int count) {
        String itemName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString();

        JsonObject result = new JsonObject();
        result.addProperty("item", itemName);
        if (count > 1) result.addProperty("count", count);
        return result;
    }

    protected JsonObject copyInputResult(String item, int count) {
        JsonObject result = new JsonObject();
        result.addProperty("item", item);
        if (count > 1) result.addProperty("count", count);

        JsonArray modifiers = new JsonArray();
        modifiers.add(modifier("tfc:copy_heat"));
        modifiers.add(modifier("tfc:copy_forging_bonus"));
        result.add("modifiers", modifiers);
        return result;
    }

    protected JsonObject modifier(String type) {
        JsonObject mod = new JsonObject();
        mod.addProperty("type", type);
        return mod;
    }

    protected JsonObject blockIngredient(String block) {

        JsonObject obj = new JsonObject();
        obj.addProperty("block", block);   // or use "tag" for tags
        return obj;
    }

    // ====================== Save Method (1.20.1 compatible) ======================

    protected void saveRecipe(CachedOutput cache, String path, JsonObject recipeJson) {
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

    @Override
    public String getName() {
        return modId + " TFC Recipes";
    }

    // Optional enum for chisel mode
    public enum ChiselMode {
        SMOOTH, STAIR, SLAB;

        public String getSerializedName() {
            return name().toLowerCase();
        }
    }
}