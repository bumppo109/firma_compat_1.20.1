package com.bumppo109.firma_compat.datagen.assets;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class FirmalifeCustomLoaderBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    private final String loaderName;           // e.g. "food_shelf", "hanger", "jarbnet"
    private ResourceLocation baseModel;

    /**
     * Creates a builder for any Firmalife custom loader that uses the pattern:
     * {
     *   "loader": "firmalife:<loaderName>",
     *   "base": { "parent": "..." }
     * }
     */
    public FirmalifeCustomLoaderBuilder(
            String loaderName,
            T parent,
            ExistingFileHelper existingFileHelper) {
        super(
                ResourceLocation.fromNamespaceAndPath("firmalife", loaderName),
                parent,
                existingFileHelper
                //false   // no inline vanilla elements
        );
        this.loaderName = loaderName;
    }

    /**
     * Sets the base model that will be referenced in "base": { "parent": ... }
     */
    public FirmalifeCustomLoaderBuilder<T> base(ResourceLocation baseModel) {
        this.baseModel = baseModel;
        return this;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
        json = super.toJson(json);  // adds "loader": "firmalife:<loaderName>"

        if (baseModel != null) {
            JsonObject baseObj = new JsonObject();
            baseObj.addProperty("parent", baseModel.toString());
            json.add("base", baseObj);
        }

        return json;
    }
}