package com.bumppo109.firma_compat.datagen.tfcdata;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

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

        for (CompatMetal metal : CompatMetal.values()) {
            var metalItems = ModItems.METAL_ITEMS.get(metal);
            if(metalItems != null) {
                metalItems.forEach((type, reg) -> {
                    itemHeat(cache, reg.get(), type.heatCapacity(), metal.tierForgeTemp(), metal.tierWeldTemp());
                });
            }
            metalDef(cache, metal);
        }

        return CompletableFuture.completedFuture(null);
    }

    protected void metalDef(CachedOutput cache, CompatMetal metal) {
        Fluid fluid = ModFluids.METALS.get(metal).getSource();
        String fluidId = Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).toString();
        int tier = 0;

        switch (metal.metalTier()) {
            case TIER_0 -> tier = 0;
            case TIER_I -> tier = 1;
            case TIER_II -> tier = 2;
            case TIER_III -> tier = 3;
            case TIER_IV -> tier = 4;
            case TIER_V -> tier = 5;
            case TIER_VI -> tier = 6;
        }

        metal(cache, metal.getSerializedName(), tier, fluidId, metal.meltTemp(), 0.00857);
    }

    protected void itemHeat(CachedOutput cache, Item item, double heatCapacity, double forgeTemp, double weldTemp) {
        ResourceLocation itemRes = ForgeRegistries.ITEMS.getKey(item);
        String itemId = Objects.requireNonNull(itemRes).toString();
        String itemPath = Objects.requireNonNull(itemRes).getPath();

        JsonObject singleIng = ingredient(itemId);

        itemHeat(cache, itemPath, singleIng, heatCapacity, forgeTemp, weldTemp);
    }

    protected void logFuel(CachedOutput cache, CompatWood wood) {
        String woodName = wood.getSerializedName();
        String logSuffix = "log";
        String woodSuffix = "wood";

        if(!wood.equals(CompatWood.CRIMSON) && !wood.equals(CompatWood.WARPED)){
            fuel(cache, woodName + "_logs",
                    new JsonElement[]{
                            ingredient("minecraft:" + woodName + "_" + logSuffix),
                            ingredient("minecraft:" + woodName + "_" + woodSuffix),
                            ingredient("minecraft:stripped_" + woodName + "_" + logSuffix),
                            ingredient("minecraft:stripped_" + woodName + "_" + woodSuffix)
                    },
                    1000,      // duration
                    650.0f,    // temperature
                    0.95f);    // purity
        }
    }
}
