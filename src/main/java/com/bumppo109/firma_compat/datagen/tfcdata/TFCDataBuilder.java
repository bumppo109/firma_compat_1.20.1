package com.bumppo109.firma_compat.datagen.tfcdata;

import com.bumppo109.firma_compat.datagen.recipes.TFCRecipeBuilder;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.gson.*;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

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

    // ====================== Fuel Builder (Supports multiple ingredients) ======================

    /**
     * Creates a TFC Fuel definition that can have 1 or more ingredients (OR logic).
     * Example: All variants of acacia wood/logs.
     *
     * Files saved to: data/<modid>/tfc/fuels/<name>.json
     *
     * @param cache        CachedOutput from run()
     * @param name         Filename (e.g. "acacia_wood")
     * @param ingredients  One or more JsonElements (item or tag)
     * @param duration     Burn duration in ticks
     * @param temperature  Burn temperature in °C
     * @param purity       Optional purity (default = 1.0). Pass null or 1.0f to omit.
     */
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

        saveData(cache, "tfc/fuels/" + name, json);
    }

    /**
     * Convenience overload for a SINGLE ingredient (most common case)
     */
    protected void fuel(CachedOutput cache, String name,
                        JsonElement ingredient,
                        int duration,
                        float temperature,
                        @Nullable Float purity) {

        fuel(cache, name, new JsonElement[]{ingredient}, duration, temperature, purity);
    }

    /**
     * Convenience overload for SINGLE ingredient without purity
     */
    protected void fuel(CachedOutput cache, String name,
                        JsonElement ingredient,
                        int duration,
                        float temperature) {

        fuel(cache, name, ingredient, duration, temperature, null);
    }

    /**
     * Creates a TFC Support definition (used for structural support / collapse prevention).
     * Files are saved to: data/<modid>/tfc/supports/<name>.json
     *
     * Example JSON:
     * {
     *   "ingredient": [ "tfc:wood/horizontal_support/acacia", ... ],
     *   "support_up": 2,
     *   "support_down": 2,
     *   "support_horizontal": 4
     * }
     *
     * @param cache              CachedOutput from run()
     * @param name               Name of the support file (e.g. "acacia_supports", "my_mod_supports")
     * @param ingredients        One or more JsonElements (usually block items or block tags)
     * @param supportUp          How many blocks upward this support provides
     * @param supportDown        How many blocks downward this support provides
     * @param supportHorizontal  How many blocks horizontally (X and Z) this support provides
     */
    protected void support(CachedOutput cache, String name,
                           JsonElement[] ingredients,
                           int supportUp,
                           int supportDown,
                           int supportHorizontal) {

        JsonObject json = new JsonObject();

        // TFC expects "ingredient" as a JSON array (even for one entry)
        JsonArray ingredientArray = new JsonArray();
        for (JsonElement ing : ingredients) {
            ingredientArray.add(ing);
        }
        json.add("ingredient", ingredientArray);

        json.addProperty("support_up", supportUp);
        json.addProperty("support_down", supportDown);
        json.addProperty("support_horizontal", supportHorizontal);

        saveData(cache, "tfc/supports/" + name, json);
    }

    /**
     * Convenience overload for a SINGLE ingredient
     */
    protected void support(CachedOutput cache, String name,
                           JsonElement ingredient,
                           int supportUp,
                           int supportDown,
                           int supportHorizontal) {

        support(cache, name, new JsonElement[]{ingredient}, supportUp, supportDown, supportHorizontal);
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

    // ====================== Save Method ======================

    /**
     * This is the key fix:
     * Path = data/<modid>/tfc/<subfolder>/<name>.json
     * No extra "tfc" folder inside tfc/
     */
    protected void saveData(CachedOutput cache, String path, JsonObject dataJson) {
        Path targetPath = output.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(modId)           // firma_compat
                .resolve("tfc")           // ← tfc folder
                .resolve(path + ".json"); // fuels/xxx or supports/xxx

        String jsonString = GSON.toJson(dataJson);
        byte[] data = jsonString.getBytes(StandardCharsets.UTF_8);
        HashCode hash = Hashing.sha256().hashBytes(data);

        try {
            cache.writeIfNeeded(targetPath, data, hash);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save TFC data: " + path, e);
        }
    }

    @Override
    public String getName() {
        return modId + " TFC Custom Data";
    }
}
