package com.bumppo109.firma_compat.item;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Locale;

public enum CompatMeal implements StringRepresentable {

    // ==================== VANILLA MEALS ====================

    RABBIT_STEW(Items.RABBIT_STEW, new Object[]{
            CompatFood.COOKED_RABBIT,
            CompatFood.CARROT,
            CompatFood.POTATO,
            CompatFood.BROWN_MUSHROOM,   // add mushroom to CompatFood if missing
            Items.BOWL              // or whatever represents the bowl
    }),

    BEETROOT_SOUP(Items.BEETROOT_SOUP, new Object[]{
            CompatFood.BEETROOT,
            CompatFood.BEETROOT,
            CompatFood.BEETROOT,
            CompatFood.BEETROOT,
            CompatFood.BEETROOT,
            CompatFood.BEETROOT,
            Items.BOWL
    }),

    MUSHROOM_STEW(Items.MUSHROOM_STEW, new Object[]{
            CompatFood.BROWN_MUSHROOM,
            CompatFood.RED_MUSHROOM,
            Items.BOWL
    }),

    ;

    private final String serializedName;
    private final Item resultItem;
    private final Object[] ingredients;   // mix of CompatFood and Item

    CompatMeal(Item resultItem, Object[] ingredients) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.resultItem = resultItem;
        this.ingredients = ingredients;
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    public Item getResultItem() {
        return resultItem;
    }

    /**
     * Sums up all nutritional values from the ingredients
     * and returns a formatted FoodStats object (exactly like CompatFood).
     */
    public FoodStats calculateFoodStats() {
        int hunger = 8;
        float saturation = 3.0f;
        float decayModifier = 2.5f;
        float protein = 0f;
        float grain = 0f;
        float fruit = 0f;
        float vegetable = 0f;
        float dairy = 0f;
        float water = 0f;

        for (Object obj : ingredients) {
            if (obj instanceof CompatFood food) {
                //hunger += food.getHunger();
                //saturation += food.getSaturation();
                //decayModifier += food.getDecayModifier();
                protein += food.getProtein();
                grain += food.getGrain();
                fruit += food.getFruit();
                vegetable += food.getVegetable();
                dairy += food.getDairy();
                water += food.getWater();
            }
            // If you pass a plain Item, you can add special handling here later
        }

        //modify by 80%
        protein     *= 0.8f;
        grain       *= 0.8f;
        fruit       *= 0.8f;
        vegetable   *= 0.8f;
        dairy       *= 0.6f;
        water       *= 0.5f;

        protein = Math.round(protein * 100f) / 100f;
        grain   = Math.round(grain * 100f) / 100f;
        fruit   = Math.round(fruit * 100f) / 100f;
        vegetable   = Math.round(vegetable * 100f) / 100f;
        dairy   = Math.round(dairy * 100f) / 100f;
        water   = Math.round(water * 100f) / 100f;

        return new FoodStats(hunger, saturation, decayModifier,
                protein, grain, fruit, vegetable, dairy, water);
    }

    /**
     * Small record that holds the summed food stats
     * (identical structure to what CompatFood provides)
     */
    public record FoodStats(
            int hunger,
            float saturation,
            float decayModifier,
            float protein,
            float grain,
            float fruit,
            float vegetable,
            float dairy,
            float water
    ) {
        // You can add a toJson() method here later if you want
    }
}
