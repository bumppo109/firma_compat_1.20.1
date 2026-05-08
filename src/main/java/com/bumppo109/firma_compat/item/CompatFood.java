package com.bumppo109.firma_compat.item;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum CompatFood implements StringRepresentable {
    // === RAW MEATS ===
    BEEF(4, 0.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    PORKCHOP(4, 0.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    CHICKEN(4, 0.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    MUTTON(4, 0.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    RABBIT(4, 0.0f, 2.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    SALMON(4, 0.0f, 3.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    COD(4, 0.0f, 3.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),

    RED_MUSHROOM(2, 0.5f, 1.5f, 0.75f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    BROWN_MUSHROOM(2, 0.5f, 1.5f, 0.75f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),

    // === COOKED MEATS ===
    COOKED_BEEF(4, 2.0f, 1.5f, 2.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    COOKED_PORKCHOP(4, 2.0f, 1.5f, 2.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    COOKED_CHICKEN(4, 2.0f, 1.5f, 2.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    COOKED_MUTTON(4, 2.0f, 1.5f, 2.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    COOKED_RABBIT(4, 2.0f, 1.5f, 2.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    COOKED_SALMON(4, 1.0f, 2.25f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    COOKED_COD(4, 1.0f, 2.25f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),

    // === FRUITS ===
    APPLE(4, 0.5f, 1.7f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f),
    MELON_SLICE(4, 0.2f, 2.5f, 0.0f, 0.0f, 0.75f, 0.0f, 0.0f, 0.0f),
    SWEET_BERRIES(4, 0.3f, 2.5f, 0.0f, 0.0f, 0.75f, 0.0f, 0.0f, 0.0f),
    GLOW_BERRIES(4, 0.3f, 2.5f, 0.0f, 0.0f, 0.75f, 0.0f, 0.0f, 0.0f),

    // === VEGETABLES ===
    CARROT(4, 0.6f, 1.5f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f),
    POTATO(4, 0.4f, 2.0f, 0.0f, 0.0f, 0.0f, 2.5f, 0.0f, 0.0f),
    BAKED_POTATO(6, 0.8f, 1.2f, 0.0f, 0.0f, 0.0f, 3.0f, 0.0f, 0.0f),
    BEETROOT(4, 0.5f, 1.8f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f),
    KELP(1, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 2.0f, 0.0f, 0.0f),
    SUGAR_CANE(0, 0.0f, 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),

    // === GRAINS & BREAD ===
    //BREAD(4, 1.0f, 1.0f, 0.0f, 1.5f, 0.0f, 0.0f, 0.0f, 0.0f),

    // === SWEETS & MISC ===
    COOKIE(2, 0.2f, 2.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    //HONEY_BOTTLE(6, 0.6f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    //SUGAR(1, 0.1f, 1.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),

    // === SEAFOOD & OTHER ===
    DRIED_KELP(2, 0.2f, 2.5f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f),
    PUFFERFISH(1, 0.1f, 3.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),
    TROPICAL_FISH(2, 0.2f, 3.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f),

    ;

    private final String serializedName;

    // TFC Food Stats (fat and carbs removed)
    private final int hunger;
    private final float saturation;
    private final float decayModifier;
    private final float protein;
    private final float grain;
    private final float fruit;
    private final float vegetable;
    private final float dairy;
    private final float water;

    CompatFood(int hunger, float saturation, float decayModifier,
               float protein, float grain, float fruit,
               float vegetable, float dairy, float water) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.hunger = hunger;
        this.saturation = saturation;
        this.decayModifier = decayModifier;
        this.protein = protein;
        this.grain = grain;
        this.fruit = fruit;
        this.vegetable = vegetable;
        this.dairy = dairy;
        this.water = water;
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    // Getters
    public int getHunger() { return hunger; }
    public float getSaturation() { return saturation; }
    public float getDecayModifier() { return decayModifier; }
    public float getProtein() { return protein; }
    public float getGrain() { return grain; }
    public float getFruit() { return fruit; }
    public float getVegetable() { return vegetable; }
    public float getDairy() { return dairy; }
    public float getWater() { return water; }
}