package com.bumppo109.firma_compat.datagen.tfcdata;

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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public abstract class TFCDataBuilder implements DataProvider {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    protected final PackOutput output;
    protected final String modId;

    public TFCDataBuilder(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    /**
     * Generates a TFC Food Item JSON file.
     *
     * Example output:
     * {
     *   "ingredient": {"item": "tfc:food/cooked_beef"},
     *   "hunger": 4,
     *   "saturation": 2,
     *   "decay_modifier": 1.5,
     *   "protein": 2.5
     * }
     */
    public void generateTFCFoodData(CachedOutput cache,
                                     Item food,
                                     int hunger,
                                     float saturation,
                                     float water,
                                     float decayModifier,
                                     float grain,
                                     float fruit,
                                     float vegetables,
                                     float protein,
                                     float dairy
    ) {
        ResourceLocation foodKey = BuiltInRegistries.ITEM.getKey(food);

        JsonObject root = new JsonObject();

        // Ingredient
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", foodKey.toString());
        root.add("ingredient", ingredient);

        // Stats
        root.addProperty("hunger", hunger);
        root.addProperty("saturation", saturation);
        root.addProperty("water", water);
        root.addProperty("decay_modifier", decayModifier);
        root.addProperty("grain", grain);
        root.addProperty("fruit", fruit);
        root.addProperty("vegetables", vegetables);
        root.addProperty("protein", protein);
        root.addProperty("dairy", dairy);

        saveData(cache, "food_items/" + foodKey.getPath(), root);
    }

    public void generateMinecraftFoodData(CachedOutput cache,
                                     String food,
                                     int hunger,
                                     float saturation,
                                     float decayModifier,
                                     float protein,
                                     float grain,
                                     float fruit,
                                     float vegetables,
                                     float dairy,
                                     float water
    ) {
        String path = "minecraft";
        String id = food;

        JsonObject root = new JsonObject();

        // Ingredient
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", path + ":" + id);
        root.add("ingredient", ingredient);

        // Stats
        root.addProperty("hunger", hunger);
        root.addProperty("saturation", saturation);
        root.addProperty("water", water);
        root.addProperty("decay_modifier", decayModifier);
        root.addProperty("grain", grain);
        root.addProperty("fruit", fruit);
        root.addProperty("vegetables", vegetables);
        root.addProperty("protein", protein);
        root.addProperty("dairy", dairy);

        saveData(cache, "food_items/" + food, root);
    }

    /**
     * Creates a TFC Metal definition JSON.
     * Files are saved to: data/<modid>/tfc/metals/<name>.json
     *
     * @param cache                CachedOutput
     * @param name                 Name of the metal file (e.g. "netherite", "poor_netherite")
     * @param tier                 Metal tier (0-6, corresponds to Metal.Tier.TIER_0 ... TIER_VI)
     * @param fluid                Fluid ID (usually "tfc:metal/<metalname>" or your mod's fluid)
     * @param meltTemperature      Melting temperature in °C
     * @param specificHeatCapacity Specific heat capacity (note: small decimal values like 0.00857)
     */
    protected void metal(CachedOutput cache, String name, int tier, String fluid,
                         double meltTemperature, double specificHeatCapacity) {

        JsonObject json = new JsonObject();

        json.addProperty("tier", tier);
        json.addProperty("fluid", fluid);
        json.addProperty("melt_temperature", meltTemperature);
        json.addProperty("specific_heat_capacity", specificHeatCapacity);

        saveData(cache, "metals/" + name, json);
    }

    /**
     * Creates a TFC Item Heat definition.
     * Supports both a single ingredient object or an array of ingredients.
     * Files are saved to: data/<modid>/tfc/item_heats/<subPath>.json
     *
     * @param cache       The CachedOutput
     * @param subPath     Path inside item_heats folder (e.g. "metal/bismuth_block" or "ore/cassiterite")
     * @param ingredient  Either a single JsonObject (from ingredient()) or a JsonArray
     * @param heatCapacity      Heat capacity value
     * @param forgingTemperature Forging temperature
     * @param weldingTemperature Welding temperature (can be omitted by passing null if not needed)
     */
    /*
    JsonArray oreArray = new JsonArray();
    oreArray.add(ingredient("tfc:ore/small_cassiterite"));
    oreArray.add(ingredient("tfc:ore/normal_cassiterite"));
    oreArray.add(ingredient("tfc:ore/poor_cassiterite"));
    oreArray.add(ingredient("tfc:ore/rich_cassiterite"));

    itemHeat(cache, "ore/cassiterite", oreArray, 2.857, 138, 184);
     */
    protected void itemHeat(CachedOutput cache, String subPath,
                            JsonElement ingredient,
                            double heatCapacity,
                            double forgingTemperature,
                            double weldingTemperature) {

        JsonObject json = new JsonObject();

        // Automatically handle single object vs array
        if (ingredient.isJsonArray()) {
            json.add("ingredient", ingredient);
        } else {
            json.add("ingredient", ingredient);  // single object or primitive
        }

        json.addProperty("heat_capacity", heatCapacity);
        json.addProperty("forging_temperature", forgingTemperature);
        json.addProperty("welding_temperature", weldingTemperature);

        saveData(cache, "item_heats/" + subPath, json);
    }

    protected void itemHeat(CachedOutput cache, String subPath,
                            JsonElement ingredient,
                            double heatCapacity,
                            double forgingTemperature) {

        JsonObject json = new JsonObject();

        // Automatically handle single object vs array
        if (ingredient.isJsonArray()) {
            json.add("ingredient", ingredient);
        } else {
            json.add("ingredient", ingredient);  // single object or primitive
        }

        json.addProperty("heat_capacity", heatCapacity);
        json.addProperty("forging_temperature", forgingTemperature);

        saveData(cache, "item_heats/" + subPath, json);
    }

    protected void itemHeat(CachedOutput cache, String subPath,
                            JsonElement ingredient,
                            double heatCapacity) {

        JsonObject json = new JsonObject();

        // Automatically handle single object vs array
        if (ingredient.isJsonArray()) {
            json.add("ingredient", ingredient);
        } else {
            json.add("ingredient", ingredient);  // single object or primitive
        }

        json.addProperty("heat_capacity", heatCapacity);

        saveData(cache, "item_heats/" + subPath, json);
    }

    // ====================== Fuel Builder ======================

    /**
     * Creates a TFC Fuel definition (varargs version - recommended).
     * Files saved to: data/<modid>/tfc/fuels/<name>.json
     */
    protected void fuel(CachedOutput cache, String name, JsonElement... ingredients) {
        fuel(cache, name, ingredients, 1000, 650.0f, 0.95f);
    }

    protected void fuel(CachedOutput cache, String name,
                        JsonElement[] ingredients,
                        int duration,
                        float temperature,
                        @Nullable Float purity) {

        JsonObject json = new JsonObject();

        JsonArray ingredientArray = new JsonArray();
        for (JsonElement ing : ingredients) {
            ingredientArray.add(ing);
        }
        json.add("ingredient", ingredientArray);

        json.addProperty("duration", duration);
        json.addProperty("temperature", temperature);

        if (purity != null && Math.abs(purity - 1.0F) > 0.001F) {
            json.addProperty("purity", purity);
        }

        saveData(cache, "fuels/" + name, json);
    }

    // ====================== Support Builder ======================

    /**
     * Creates a TFC Support definition (varargs version - recommended).
     * Files saved to: data/<modid>/tfc/supports/<name>.json
     */
    protected void support(CachedOutput cache, String name, JsonElement... ingredients) {
        support(cache, name, ingredients, 2, 2, 4);
    }

    protected void support(CachedOutput cache, String name,
                           JsonElement[] ingredients,
                           int supportUp,
                           int supportDown,
                           int supportHorizontal) {

        JsonObject json = new JsonObject();

        JsonArray ingredientArray = new JsonArray();
        for (JsonElement ing : ingredients) {
            ingredientArray.add(ing);
        }
        json.add("ingredient", ingredientArray);

        json.addProperty("support_up", supportUp);
        json.addProperty("support_down", supportDown);
        json.addProperty("support_horizontal", supportHorizontal);

        saveData(cache, "supports/" + name, json);
    }

    // ====================== Helpers ======================

    protected JsonObject ingredient(String itemId) {
        JsonObject obj = new JsonObject();
        obj.addProperty("item", itemId);
        return obj;
    }

    protected JsonObject ingredient(ItemLike item) {
        return ingredient(item.asItem().toString());
    }

    protected JsonObject tagIngredient(String tag) {
        JsonObject obj = new JsonObject();
        obj.addProperty("tag", tag);
        return obj;
    }

    protected JsonObject tagIngredient(TagKey<Item> tag) {
        return tagIngredient(tag.location().toString());
    }

    // ====================== Save Method (Correct Path) ======================

    /**
     * Saves JSON under: data/<modid>/tfc/<subfolder>/<name>.json
     * This prevents the double "tfc/tfc" folder issue.
     */
    protected void saveData(CachedOutput cache, String subPath, JsonObject dataJson) {
        Path targetPath = output.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(modId)           // firma_compat
                .resolve("tfc")           // tfc
                .resolve(subPath + ".json"); // fuels/xxx.json or supports/xxx.json

        String jsonString = GSON.toJson(dataJson);
        byte[] data = jsonString.getBytes(StandardCharsets.UTF_8);
        HashCode hash = Hashing.sha256().hashBytes(data);

        try {
            cache.writeIfNeeded(targetPath, data, hash);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save TFC data: " + subPath, e);
        }
    }

    @Override
    public String getName() {
        return modId + " TFC Custom Data";
    }
}