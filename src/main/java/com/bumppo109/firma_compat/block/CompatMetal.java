package com.bumppo109.firma_compat.block;

import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.TFCChainBlock;
import net.dries007.tfc.common.blocks.devices.AnvilBlock;
import net.dries007.tfc.common.blocks.devices.LampBlock;
import net.dries007.tfc.common.items.*;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistryMetal;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum CompatMetal implements RegistryMetal {

    NETHERITE(0x593F3C, Rarity.EPIC, Metal.Tier.TIER_V, Tiers.NETHERITE, ArmorMaterials.NETHERITE, false, true, true, false),
    POOR_NETHERITE(0x854038, Rarity.EPIC, Metal.Tier.TIER_V, null, null, true, true, false, false),
    //IRON is used to enable EveryCompat Gems Realm module
    IRON(0x854038, Rarity.COMMON, Metal.Tier.TIER_I, Tiers.IRON, ArmorMaterials.IRON, true, true, true, true)
    ;

    private final String serializedName;
    private final boolean ingot, parts, armor, utility;
    private final Metal.Tier metalTier;
    @Nullable
    private final net.minecraft.world.item.Tier toolTier;
    @Nullable
    private final ArmorMaterial armorTier;
    private final Rarity rarity;
    private final int color;

    CompatMetal(int color, Rarity rarity, Metal.Tier tier, boolean ingot, boolean parts, boolean armor, boolean utility)
    {
        this(color, rarity, tier, null, null, ingot, parts, armor, utility);
    }

    CompatMetal(int color, Rarity rarity, Metal.Tier metalTier, @Nullable net.minecraft.world.item.Tier toolTier, @Nullable ArmorMaterial armorTier, boolean ingot, boolean parts, boolean armor, boolean utility)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.metalTier = metalTier;
        this.toolTier = toolTier;
        this.armorTier = armorTier;
        this.rarity = rarity;
        this.color = color;

        this.ingot = ingot;
        this.parts = parts;
        this.armor = armor;
        this.utility = utility;
    }

    @Nonnull
    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public int getColor()
    {
        return color;
    }

    public Rarity getRarity()
    {
        return rarity;
    }

    public boolean hasIngot()
    {
        return ingot;
    }

    public boolean hasParts()
    {
        return parts;
    }

    public boolean hasArmor()
    {
        return armor;
    }

    public boolean hasTools()
    {
        return toolTier != null;
    }

    public boolean hasUtilities()
    {
        return utility;
    }

    @Nonnull
    @Override
    public net.minecraft.world.item.Tier toolTier()
    {
        return Objects.requireNonNull(toolTier, "Tried to get non-existent tier from " + name());
    }

    @Nonnull
    @Override
    public ArmorMaterial armorTier()
    {
        return Objects.requireNonNull(armorTier, "Tried to get non-existent armor tier from " + name());
    }

    @Nonnull
    @Override
    public Metal.Tier metalTier()
    {
        return metalTier;
    }

    @Override
    public Supplier<Block> getFullBlock() {
        return null;
    }

    @Override
    public MapColor mapColor() {
        return null;
    }

    private enum Type {
        DEFAULT(CompatMetal::hasIngot),
        PART(CompatMetal::hasParts),
        TOOL(CompatMetal::hasTools),
        ARMOR(CompatMetal::hasArmor),
        UTILITY(CompatMetal::hasUtilities);

        private final Predicate<CompatMetal> predicate;

        Type(Predicate<CompatMetal> predicate) {
            this.predicate = predicate;
        }

        boolean hasType(CompatMetal metal) {
            return this.predicate.test(metal);
        }
    }

    public enum ItemType {
        INGOT(Type.DEFAULT, true),
        DOUBLE_INGOT(Type.PART, false),
        SHEET(Type.PART, false),
        DOUBLE_SHEET(Type.PART, false),
        ROD(Type.PART, false),

        UNFINISHED_HELMET(Type.ARMOR, false),
        UNFINISHED_CHESTPLATE(Type.ARMOR, false),
        UNFINISHED_LEGGINGS(Type.ARMOR, false),
        UNFINISHED_BOOTS(Type.ARMOR, false),

        SWORD_BLADE(Type.TOOL, true),
        AXE_HEAD(Type.TOOL, true),
        SHOVEL_HEAD(Type.TOOL, true),
        HOE_HEAD(Type.TOOL, true),
        PICKAXE_HEAD(Type.TOOL, true);

        private final Function<RegistryMetal, Item> itemFactory;
        private final Type type;
        private final boolean mold;

        public static Item.Properties properties() {
            return (new Item.Properties());
        }

        ItemType(Type type, boolean mold) {
            this(type, mold, (metal) -> new Item(properties()));
        }

        ItemType(Type type, Function<RegistryMetal, Item> itemFactory) {
            this(type, false, itemFactory);
        }

        ItemType(Type type, boolean mold, Function<RegistryMetal, Item> itemFactory) {
            this.type = type;
            this.mold = mold;
            this.itemFactory = itemFactory;
        }

        public Item create(RegistryMetal metal) {
            return this.itemFactory.apply(metal);
        }

        public boolean has(CompatMetal metal) {
            return this.type.hasType(metal);
        }

        public boolean hasMold() {
            return this.mold;
        }

        public boolean isCommonTagPart() {
            boolean isCommon = false;

            switch (type) {
                case DEFAULT -> isCommon = true;
                case PART -> isCommon = true;
                default -> isCommon = false;
            }

            return isCommon;
        }

        public double heatCapacity() {
            double heatCapacity = 0;

            switch (this) {
                case ROD -> heatCapacity = 3.571;
                case SHEET, SWORD_BLADE, DOUBLE_INGOT, UNFINISHED_BOOTS -> heatCapacity = 5.714;
                case DOUBLE_SHEET, UNFINISHED_CHESTPLATE, UNFINISHED_LEGGINGS, UNFINISHED_HELMET -> heatCapacity = 11.429;
                default -> heatCapacity = 2.857;
            }

            return heatCapacity;
        }

        public double metalAmount() {
            return switch (this) {
                case ROD -> 50;
                case SHEET, SWORD_BLADE, DOUBLE_INGOT, UNFINISHED_BOOTS -> 200;
                case DOUBLE_SHEET, UNFINISHED_CHESTPLATE, UNFINISHED_LEGGINGS, UNFINISHED_HELMET -> 400;
                default -> 100;
            };
        }
    }

    public enum BlockType {
        ;
        private final Function<RegistryMetal, Block> blockFactory;
        private final BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory;
        private final Type type;

        BlockType(Type type, Function<RegistryMetal, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory) {
            this.type = type;
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
        }

        BlockType(Type type, Function<RegistryMetal, Block> blockFactory) {
            this(type, blockFactory, BlockItem::new);
        }

        public Supplier<Block> create(RegistryMetal metal) {
            return () -> (Block)this.blockFactory.apply(metal);
        }

        public Function<Block, BlockItem> createBlockItem(Item.Properties properties) {
            return (block) -> (BlockItem)this.blockItemFactory.apply(block, properties);
        }

        public boolean has(CompatMetal metal) {
            return this.type.hasType(metal);
        }
    }

    public Supplier<Item> ingot() {
        return switch (this) {
            case NETHERITE -> () -> Items.NETHERITE_INGOT;
            default -> () -> ModItems.METAL_ITEMS.get(this).get(ItemType.INGOT).get();
        };
    }

    public double tierForgeTemp() {
        return switch (metalTier()) {
            case TIER_0 -> 0;
            case TIER_I -> 648;
            case TIER_II -> 570;
            case TIER_III -> 921;
            case TIER_IV, TIER_VI -> 924;
            case TIER_V -> 891;
        };
    }

    public double tierWeldTemp() {
        return switch (metalTier()) {
            case TIER_0 -> 0;
            case TIER_I -> 864;
            case TIER_II -> 760;
            case TIER_III -> 1228;
            case TIER_IV, TIER_VI -> 1232;
            case TIER_V -> 1180;
        };
    }

    public double meltTemp() {
        return switch (this) {
            case NETHERITE -> 1535;
            case POOR_NETHERITE -> 1450;
            default -> 950;
        };
    }
}