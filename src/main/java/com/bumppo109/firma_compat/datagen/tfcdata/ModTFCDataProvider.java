package com.bumppo109.firma_compat.datagen.tfcdata;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.CompatFood;
import com.bumppo109.firma_compat.item.CompatMeal;
import com.bumppo109.firma_compat.item.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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

        itemHeat(cache, Items.NETHERITE_SCRAP, 2.857, CompatMetal.POOR_NETHERITE.tierForgeTemp(), CompatMetal.POOR_NETHERITE.tierWeldTemp());
        itemHeat(cache, Items.NETHERITE_INGOT, 2.857, CompatMetal.NETHERITE.tierForgeTemp(), CompatMetal.NETHERITE.tierWeldTemp());

        itemHeat(cache, Items.NETHERITE_HELMET, 11.429, CompatMetal.NETHERITE.tierForgeTemp(), CompatMetal.NETHERITE.tierWeldTemp());
        itemHeat(cache, Items.NETHERITE_CHESTPLATE, 11.429, CompatMetal.NETHERITE.tierForgeTemp(), CompatMetal.NETHERITE.tierWeldTemp());
        itemHeat(cache, Items.NETHERITE_LEGGINGS, 11.429, CompatMetal.NETHERITE.tierForgeTemp(), CompatMetal.NETHERITE.tierWeldTemp());
        itemHeat(cache, Items.NETHERITE_BOOTS, 5.714, CompatMetal.NETHERITE.tierForgeTemp(), CompatMetal.NETHERITE.tierWeldTemp());

        itemHeat(cache, Items.NETHERITE_SWORD, 5.714, CompatMetal.NETHERITE.tierForgeTemp(), CompatMetal.NETHERITE.tierWeldTemp());
        itemHeat(cache, Items.NETHERITE_PICKAXE, 5.714, CompatMetal.NETHERITE.tierForgeTemp(), CompatMetal.NETHERITE.tierWeldTemp());
        itemHeat(cache, Items.NETHERITE_AXE, 5.714, CompatMetal.NETHERITE.tierForgeTemp(), CompatMetal.NETHERITE.tierWeldTemp());
        itemHeat(cache, Items.NETHERITE_SHOVEL, 5.714, CompatMetal.NETHERITE.tierForgeTemp(), CompatMetal.NETHERITE.tierWeldTemp());
        itemHeat(cache, Items.NETHERITE_HOE, 5.714, CompatMetal.NETHERITE.tierForgeTemp(), CompatMetal.NETHERITE.tierWeldTemp());

        itemHeat(cache, Items.NETHERITE_BLOCK, 2.857, CompatMetal.NETHERITE.tierForgeTemp(), CompatMetal.NETHERITE.tierWeldTemp());

        itemHeat(cache, Items.CHAIN, 0.171, 921, 1228);

        itemHeat(cache, Items.GOLD_BLOCK, 1.667, 636, 848);
        itemHeat(cache, Items.GOLD_NUGGET, 0.833, 648, 864);
        itemHeat(cache, Items.IRON_NUGGET, 0.955, 921, 1228);
        itemHeat(cache, Items.IRON_TRAPDOOR, 5.714, 921, 1228);
        itemHeat(cache, Items.IRON_BARS, 0.714, 921, 1228);
        itemHeat(cache, Items.IRON_BLOCK, 2.857, 921, 1228);

        itemHeat(cache, Items.COPPER_BLOCK, 2.857, 648, 864);
        itemHeat(cache, Items.EXPOSED_COPPER, 2.857, 648, 864);
        itemHeat(cache, Items.WEATHERED_COPPER, 2.857, 648, 864);
        itemHeat(cache, Items.OXIDIZED_COPPER, 2.857, 648, 864);
        itemHeat(cache, Items.WAXED_COPPER_BLOCK, 2.857, 648, 864);
        itemHeat(cache, Items.WAXED_EXPOSED_COPPER, 2.857, 648, 864);
        itemHeat(cache, Items.WAXED_WEATHERED_COPPER, 2.857, 648, 864);
        itemHeat(cache, Items.WAXED_OXIDIZED_COPPER, 2.857, 648, 864);

        itemHeat(cache, Items.CUT_COPPER, 2.857, 648, 864);
        itemHeat(cache, Items.EXPOSED_CUT_COPPER, 2.857, 648, 864);
        itemHeat(cache, Items.WEATHERED_CUT_COPPER, 2.857, 648, 864);
        itemHeat(cache, Items.OXIDIZED_CUT_COPPER, 2.857, 648, 864);
        itemHeat(cache, Items.WAXED_CUT_COPPER, 2.857, 648, 864);
        itemHeat(cache, Items.WAXED_EXPOSED_CUT_COPPER, 2.857, 648, 864);
        itemHeat(cache, Items.WAXED_WEATHERED_CUT_COPPER, 2.857, 648, 864);
        itemHeat(cache, Items.WAXED_OXIDIZED_CUT_COPPER, 2.857, 648, 864);

        itemHeat(cache, Items.CUT_COPPER_STAIRS, 2.143, 648, 864);
        itemHeat(cache, Items.EXPOSED_CUT_COPPER_STAIRS, 2.143, 648, 864);
        itemHeat(cache, Items.WEATHERED_CUT_COPPER_STAIRS, 2.143, 648, 864);
        itemHeat(cache, Items.OXIDIZED_CUT_COPPER_STAIRS, 2.143, 648, 864);
        itemHeat(cache, Items.WAXED_CUT_COPPER_STAIRS, 2.143, 648, 864);
        itemHeat(cache, Items.WAXED_EXPOSED_CUT_COPPER_STAIRS, 2.143, 648, 864);
        itemHeat(cache, Items.WAXED_WEATHERED_CUT_COPPER_STAIRS, 2.143, 648, 864);
        itemHeat(cache, Items.WAXED_OXIDIZED_CUT_COPPER_STAIRS, 2.143, 648, 864);

        itemHeat(cache, Items.CUT_COPPER_SLAB, 1.429, 648, 864);
        itemHeat(cache, Items.EXPOSED_CUT_COPPER_SLAB, 1.429, 648, 864);
        itemHeat(cache, Items.WEATHERED_CUT_COPPER_SLAB, 1.429, 648, 864);
        itemHeat(cache, Items.OXIDIZED_CUT_COPPER_SLAB, 1.429, 648, 864);
        itemHeat(cache, Items.WAXED_CUT_COPPER_SLAB, 1.429, 648, 864);
        itemHeat(cache, Items.WAXED_EXPOSED_CUT_COPPER_SLAB, 1.429, 648, 864);
        itemHeat(cache, Items.WAXED_WEATHERED_CUT_COPPER_SLAB, 1.429, 648, 864);
        itemHeat(cache, Items.WAXED_OXIDIZED_CUT_COPPER_SLAB, 1.429, 648, 864);

        foodHeat(cache, Items.SALMON);
        foodHeat(cache, Items.COD);
        foodHeat(cache, Items.RABBIT);
        foodHeat(cache, Items.BEEF);
        foodHeat(cache, Items.CHICKEN);
        foodHeat(cache, Items.PORKCHOP);
        foodHeat(cache, Items.MUTTON);
        foodHeat(cache, Items.POTATO);
        foodHeat(cache, Items.KELP);
        foodHeat(cache, Items.CHORUS_FRUIT);

        for (CompatFood food : CompatFood.values()) {
            generateMinecraftFoodData(cache, food.getSerializedName(), food.getHunger(),
                    food.getSaturation(),
                    food.getDecayModifier(),
                    food.getProtein(),
                    food.getGrain(),
                    food.getFruit(),
                    food.getVegetable(),
                    food.getDairy(),
                    food.getWater());
        }

        for (CompatMeal meal : CompatMeal.values()) {
            CompatMeal.FoodStats stats = meal.calculateFoodStats();

            generateMinecraftFoodData(cache, meal.getSerializedName(), stats.hunger(),
                    stats.saturation(),
                    stats.decayModifier(),
                    stats.protein(),
                    stats.grain(),
                    stats.fruit(),
                    stats.vegetable(),
                    stats.dairy(),
                    stats.water());
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

    protected void foodHeat(CachedOutput cache, Item item) {
        ResourceLocation itemRes = ForgeRegistries.ITEMS.getKey(item);
        String itemId = Objects.requireNonNull(itemRes).toString();
        String itemPath = Objects.requireNonNull(itemRes).getPath();

        JsonObject singleIng = ingredient(itemId);

        itemHeat(cache, itemPath, singleIng, 1.0);
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
