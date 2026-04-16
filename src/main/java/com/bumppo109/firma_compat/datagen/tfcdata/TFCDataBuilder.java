package com.bumppo109.firma_compat.datagen.tfcdata;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
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