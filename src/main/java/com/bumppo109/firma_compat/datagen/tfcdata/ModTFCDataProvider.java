package com.bumppo109.firma_compat.datagen.tfcdata;

import com.bumppo109.firma_compat.block.CompatWood;
import com.google.gson.JsonElement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class ModTFCDataProvider extends TFCDataBuilder {

    public ModTFCDataProvider(PackOutput output, String modId) {
        super(output, modId);
    }

    public CompletableFuture<?> run(CachedOutput cache) {

        for (CompatWood wood : CompatWood.VALUES) {
            logFuel(cache, wood);
        }

        support(cache, "wood_horizontal_supports",
                new JsonElement[]{
                        ingredient("minecraft:acacia_horizontal_support"),
                        ingredient("minecraft:birch_horizontal_support"),
                        ingredient("minecraft:cherry_horizontal_support"),
                        ingredient("minecraft:dark_oak_horizontal_support"),
                        ingredient("minecraft:jungle_horizontal_support"),
                        ingredient("minecraft:mangrove_horizontal_support"),
                        ingredient("minecraft:oak_horizontal_support"),
                        ingredient("minecraft:spruce_horizontal_support"),
                        ingredient("minecraft:crimson_horizontal_support"),
                        ingredient("minecraft:warped_horizontal_support"),
                },
                2,   // support_up
                2,   // support_down
                4);  // support_horizontal


        return CompletableFuture.completedFuture(null);
    }

    protected void logFuel(CachedOutput cache, CompatWood wood) {
        String woodName = wood.getSerializedName();

        fuel(cache, woodName + "_logs",
                new JsonElement[]{
                        ingredient("minecraft:" + woodName + "_log"),
                        ingredient("minecraft:" + woodName + "_wood"),
                        ingredient("minecraft:" + woodName + "_stripped_log"),
                        ingredient("minecraft:" + woodName + "_stripped_wood")
                },
                1000,      // duration
                650.0f,    // temperature
                0.95f);    // purity

    }

}
