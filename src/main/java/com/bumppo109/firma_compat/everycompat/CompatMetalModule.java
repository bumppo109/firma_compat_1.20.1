package com.bumppo109.firma_compat.everycompat;

import net.mehvahdjukaar.every_compat.api.ItemOnlyEntrySet;
import net.mehvahdjukaar.every_compat.misc.UtilityTag;
import net.mehvahdjukaar.moonlight.api.resources.SimpleTagBuilder;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.xelbayria.gems_realm.api.GemsRealmModule;
import net.xelbayria.gems_realm.api.MetalPaletteStrategies;
import net.xelbayria.gems_realm.api.set.metal.MetalType;
import net.xelbayria.gems_realm.api.set.metal.VanillaMetalTypes;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import static net.xelbayria.gems_realm.api.set.metal.VanillaMetalChildKeys.INGOT;

public class CompatMetalModule extends GemsRealmModule {

    public final ItemOnlyEntrySet<MetalType, Item> DOUBLE_INGOT;
    public final ItemOnlyEntrySet<MetalType, Item> SHEET;
    public final ItemOnlyEntrySet<MetalType, Item> DOUBLE_SHEET;
    public final ItemOnlyEntrySet<MetalType, Item> ROD;

    public final ItemOnlyEntrySet<MetalType, Item> SWORD_BLADE;
    public final ItemOnlyEntrySet<MetalType, Item> PICKAXE_HEAD;
    public final ItemOnlyEntrySet<MetalType, Item> SHOVEL_HEAD;
    public final ItemOnlyEntrySet<MetalType, Item> AXE_HEAD;
    public final ItemOnlyEntrySet<MetalType, Item> HOE_HEAD;

    public final ItemOnlyEntrySet<MetalType, Item> UNFINISHED_HELMET;
    public final ItemOnlyEntrySet<MetalType, Item> UNFINISHED_CHESTPLATE;
    public final ItemOnlyEntrySet<MetalType, Item> UNFINISHED_LEGGINGS;
    public final ItemOnlyEntrySet<MetalType, Item> UNFINISHED_BOOTS;

    public CompatMetalModule(String modId) {
        super(modId, "firma_compat");
        ResourceKey<CreativeModeTab> ingredients = CreativeModeTabs.INGREDIENTS;

        ROD = ItemOnlyEntrySet.builder(MetalType.class, "rod",
                        getModItem("iron_rod"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_rod"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/iron_rod"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(ROD);

        DOUBLE_INGOT = ItemOnlyEntrySet.builder(MetalType.class, "double_ingot",
                        getModItem("iron_double_ingot"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_double_ingot"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/iron_double_ingot"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(DOUBLE_INGOT);

        SHEET = ItemOnlyEntrySet.builder(MetalType.class, "sheet",
                        getModItem("iron_sheet"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_sheet"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/iron_sheet"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(SHEET);

        DOUBLE_SHEET = ItemOnlyEntrySet.builder(MetalType.class, "double_sheet",
                        getModItem("iron_double_sheet"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/iron_double_sheet"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/iron_double_sheet"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(DOUBLE_SHEET);

        SWORD_BLADE = ItemOnlyEntrySet.builder(MetalType.class, "sword_blade",
                        getModItem("iron_sword_blade"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/sword_blade"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/sword_blade"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(SWORD_BLADE);

        PICKAXE_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "pickaxe_head",
                        getModItem("iron_pickaxe_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/pickaxe_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/pickaxe_head"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(PICKAXE_HEAD);

        SHOVEL_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "shovel_head",
                        getModItem("iron_shovel_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/shovel_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/shovel_head"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(SHOVEL_HEAD);

        AXE_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "axe_head",
                        getModItem("iron_axe_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/axe_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/axe_head"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(AXE_HEAD);

        HOE_HEAD = ItemOnlyEntrySet.builder(MetalType.class, "hoe_head",
                        getModItem("iron_hoe_head"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/hoe_head"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/hoe_head"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(HOE_HEAD);

        UNFINISHED_HELMET = ItemOnlyEntrySet.builder(MetalType.class, "unfinished_helmet",
                        getModItem("iron_unfinished_helmet"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/unfinished_helmet"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/unfinished_helmet"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(UNFINISHED_HELMET);

        UNFINISHED_CHESTPLATE = ItemOnlyEntrySet.builder(MetalType.class, "unfinished_chestplate",
                        getModItem("iron_unfinished_chestplate"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/unfinished_chestplate"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/unfinished_chestplate"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(UNFINISHED_CHESTPLATE);

        UNFINISHED_LEGGINGS = ItemOnlyEntrySet.builder(MetalType.class, "unfinished_leggings",
                        getModItem("iron_unfinished_leggings"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/unfinished_leggings"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/unfinished_leggings"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(UNFINISHED_LEGGINGS);

        UNFINISHED_BOOTS = ItemOnlyEntrySet.builder(MetalType.class, "unfinished_boots",
                        getModItem("iron_unfinished_boots"), () -> VanillaMetalTypes.IRON,
                        metalType -> new Item(new Item.Properties())
                )
                .requiresChildren(INGOT) //REASON: recipes, textures
                .addTexture(modRes("item/unfinished_boots"), MetalPaletteStrategies.INGOT_STANDARD)
                .generateItemModels(modRes("item/unfinished_boots"))
                .excludeBlockTypes("tfc:.*", "firmalife:.*")
                .setTabKey(ingredients)
                .build();
        this.addEntry(UNFINISHED_BOOTS);
    }

    @Override
    public boolean isEntryAlreadyRegistered(String entrySetId, ResourceLocation blockId, BlockType blockType, Registry<?> registry) {
        return false;
    }

    @Override
    public void addDynamicServerResources(Consumer<ResourceGenTask> executor) {
        super.addDynamicServerResources(executor);

        executor.accept((manager, sink) -> {

        });
    }
}