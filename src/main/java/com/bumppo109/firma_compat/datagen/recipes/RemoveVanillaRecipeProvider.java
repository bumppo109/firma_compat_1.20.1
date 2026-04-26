package com.bumppo109.firma_compat.datagen.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RemoveVanillaRecipeProvider implements DataProvider {

    //TODO - not working, empty folder. relying on KubeJS

    // ← Put all your recipe names here
    private static final List<String> RECIPES_TO_DISABLE = List.of(
            "bread", "cake", "cookie", "rabbit_stew", "beetroot_soup",
            "netherite_ingot", "stone_bricks", "oak_planks" /* add the rest */
            // ... your full list
    );

    private final PackOutput packOutput;

    public RemoveVanillaRecipeProvider(PackOutput output) {
        this.packOutput = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        System.out.println("§a[RemoveVanillaRecipeProvider] Starting – " + RECIPES_TO_DISABLE.size() + " files");

        JsonObject template = createTemplateJson();   // build once

        int count = 0;
        for (String name : RECIPES_TO_DISABLE) {
            Path filePath = packOutput.getOutputFolder(PackOutput.Target.DATA_PACK)
                    .resolve("minecraft")           // or "firma_compat" if you prefer your namespace
                    .resolve("recipes")
                    .resolve(name + ".json");

            filePath.getParent().toFile().mkdirs();   // ensure folder exists

            try {
                DataProvider.saveStable(cache, template, filePath);
                count++;
                System.out.println("§aSaved: minecraft:recipes/" + name + ".json");
            } catch (Exception e) {
                System.err.println("§cFailed to save " + name);
                e.printStackTrace();
            }
        }

        System.out.println("§a[RemoveVanillaRecipeProvider] Finished – " + count + " files written");
        return CompletableFuture.completedFuture(null);
    }

    private JsonObject createTemplateJson() {
        JsonObject root = new JsonObject();
        JsonArray conditions = new JsonArray();

        JsonObject falseCond = new JsonObject();
        falseCond.addProperty("type", "forge:false");
        conditions.add(falseCond);

        root.add("conditions", conditions);
        return root;
    }

    @Override
    public String getName() {
        return "Firma Compat - Vanilla Recipe Removals";
    }
}