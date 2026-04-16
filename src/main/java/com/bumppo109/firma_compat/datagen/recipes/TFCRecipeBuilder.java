package com.bumppo109.firma_compat.datagen.recipes;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

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
    protected void vanillaShaped(CachedOutput cache, String name,
                                 String[] pattern,
                                 Map<Character, JsonElement> key,
                                 JsonObject result,
                                 @Nullable String group) {

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

        saveRecipe(cache, "crafting/" + name, json);
    }

    /**
     * Base method for vanilla minecraft:crafting_shapeless
     */
    protected void vanillaShapeless(CachedOutput cache, String name,
                                    JsonArray ingredients,
                                    JsonObject result,
                                    @Nullable String group) {

        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shapeless");

        json.add("ingredients", ingredients);
        json.add("result", result);

        if (group != null && !group.isEmpty()) {
            json.addProperty("group", group);
        }

        saveRecipe(cache, "crafting/" + name, json);
    }

    // ========================= Chisel =============================

    /**
     * Base method for tfc:chisel
     */
    protected void chisel(CachedOutput cache, String name,
                          JsonElement blockIngredient,      // BlockIngredient
                          String resultBlockState,          // e.g. "tfc:rock/smooth/granite"
                          ChiselMode mode,
                          JsonElement itemIngredient,       // optional
                          JsonObject extraDrop) {           // optional

        JsonObject json = new JsonObject();
        json.addProperty("type", "tfc:chisel");
        json.add("ingredient", blockIngredient);
        json.addProperty("result", resultBlockState);
        json.addProperty("mode", mode.getSerializedName());

        if (itemIngredient != null) {
            json.add("item_ingredient", itemIngredient);
        }
        if (extraDrop != null) {
            json.add("extra_drop", extraDrop);
        }

        saveRecipe(cache, "chisel/" + name, json);
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