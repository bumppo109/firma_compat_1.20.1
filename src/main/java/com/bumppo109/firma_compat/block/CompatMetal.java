package com.bumppo109.firma_compat.block;

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

    NETHERITE(0x593F3C, Rarity.EPIC, Metal.Tier.TIER_V, Tiers.NETHERITE, ArmorMaterials.NETHERITE, true, true, false),
    POOR_NETHERITE(0x854038, Rarity.EPIC, Metal.Tier.TIER_V, Tiers.NETHERITE, null, true, false, false)
    ;

    private final String serializedName;
    private final boolean parts, armor, utility;
    private final Metal.Tier metalTier;
    @Nullable
    private final net.minecraft.world.item.Tier toolTier;
    @Nullable
    private final ArmorMaterial armorTier;
    private final Rarity rarity;
    private final int color;

    CompatMetal(int color, Rarity rarity, Metal.Tier tier, boolean parts, boolean armor, boolean utility)
    {
        this(color, rarity, tier, null, null, parts, armor, utility);
    }

    CompatMetal(int color, Rarity rarity, Metal.Tier metalTier, @Nullable net.minecraft.world.item.Tier toolTier, @Nullable ArmorMaterial armorTier, boolean parts, boolean armor, boolean utility)
    {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.metalTier = metalTier;
        this.toolTier = toolTier;
        this.armorTier = armorTier;
        this.rarity = rarity;
        this.color = color;

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
        DEFAULT((metal) -> true),
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
        TUYERE(Type.TOOL, (metal) -> new TieredItem(metal.toolTier(), properties())),
        FISH_HOOK(Type.TOOL, false),
        FISHING_ROD(Type.TOOL, (metal) -> new TFCFishingRodItem(properties().defaultDurability(metal.toolTier().getUses()), metal.toolTier())),
        PICKAXE(Type.TOOL, (metal) -> new PickaxeItem(metal.toolTier(), (int) ToolItem.calculateVanillaAttackDamage(0.75F, metal.toolTier()), -2.8F, properties())),
        PICKAXE_HEAD(Type.TOOL, true),
        PROPICK(Type.TOOL, (metal) -> new PropickItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.5F, metal.toolTier()), -2.8F, properties())),
        PROPICK_HEAD(Type.TOOL, true),
        AXE(Type.TOOL, (metal) -> new AxeItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(1.5F, metal.toolTier()), -3.1F, properties())),
        AXE_HEAD(Type.TOOL, true),
        SHOVEL(Type.TOOL, (metal) -> new ShovelItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.875F, metal.toolTier()), -3.0F, properties())),
        SHOVEL_HEAD(Type.TOOL, true),
        HOE(Type.TOOL, (metal) -> new TFCHoeItem(metal.toolTier(), -1, -2.0F, properties())),
        HOE_HEAD(Type.TOOL, true),
        CHISEL(Type.TOOL, (metal) -> new ChiselItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.27F, metal.toolTier()), -1.5F, properties())),
        CHISEL_HEAD(Type.TOOL, true),
        HAMMER(Type.TOOL, (metal) -> new ToolItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(1.0F, metal.toolTier()), -3.0F, TFCTags.Blocks.MINEABLE_WITH_HAMMER, properties())),
        HAMMER_HEAD(Type.TOOL, true),
        SAW(Type.TOOL, (metal) -> new AxeItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.5F, metal.toolTier()), -3.0F, properties())),
        SAW_BLADE(Type.TOOL, true),
        JAVELIN(Type.TOOL, (metal) -> new JavelinItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(1.0F, metal.toolTier()), -2.2F, 2f, properties(), metal.getSerializedName())), //todo attackspeed is new
        JAVELIN_HEAD(Type.TOOL, true),
        SWORD(Type.TOOL, (metal) -> new SwordItem(metal.toolTier(), (int)ToolItem.calculateVanillaAttackDamage(1.0F, metal.toolTier()), -2.4F, properties())),
        SWORD_BLADE(Type.TOOL, true),
        MACE(Type.TOOL, (metal) -> new MaceItem(metal.toolTier(), (int)ToolItem.calculateVanillaAttackDamage(1.3F, metal.toolTier()), -3.0F, properties())),
        MACE_HEAD(Type.TOOL, true),
        KNIFE(Type.TOOL, (metal) -> new ToolItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.6F, metal.toolTier()), -2.0F, TFCTags.Blocks.MINEABLE_WITH_KNIFE, properties())),
        KNIFE_BLADE(Type.TOOL, true),
        SCYTHE(Type.TOOL, (metal) -> new ScytheItem(metal.toolTier(), ToolItem.calculateVanillaAttackDamage(0.7F, metal.toolTier()), -3.2F, TFCTags.Blocks.MINEABLE_WITH_SCYTHE, properties())),
        SCYTHE_BLADE(Type.TOOL, true),
        SHEARS(Type.TOOL, (metal) -> new ShearsItem(properties().defaultDurability(metal.toolTier().getUses()))),
        UNFINISHED_HELMET(Type.ARMOR, false),
        //HELMET(Type.ARMOR, (metal) -> new ArmorItem(metal.armorTier(), ArmorItem.Type.HELMET, properties())),
        UNFINISHED_CHESTPLATE(Type.ARMOR, false),
        //CHESTPLATE(Type.ARMOR, (metal) -> new ArmorItem(metal.armorTier(), ArmorItem.Type.CHESTPLATE, properties())),
        UNFINISHED_LEGGINGS(Type.ARMOR, false),
        //GREAVES(Type.ARMOR, (metal) -> new ArmorItem(metal.armorTier(), ArmorItem.Type.LEGGINGS, properties())),
        UNFINISHED_BOOTS(Type.ARMOR, false),
        UNFINISHED_LAMP(Type.UTILITY, false),
        //BOOTS(Type.ARMOR, (metal) -> new ArmorItem(metal.armorTier(), ArmorItem.Type.BOOTS, properties())),
        SHIELD(Type.TOOL, (metal) -> new TFCShieldItem(metal.toolTier(), properties()));

        private final Function<RegistryMetal, Item> itemFactory;
        private final Type type;
        private final boolean mold;

        public static Item.Properties properties() {
            return (new Item.Properties());//todo .tab(MetallumItemGroup.METAL);
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
    }

    public enum BlockType {
        ANVIL(Type.UTILITY, (metal) -> new AnvilBlock(ExtendedProperties.of().mapColor(MapColor.METAL).noOcclusion().sound(SoundType.METAL).strength(10.0F, 10.0F).requiresCorrectToolForDrops().blockEntity(TFCBlockEntities.ANVIL), metal.metalTier())),
        CHAIN(Type.UTILITY, (metal) -> new TFCChainBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.CHAIN))),
        LAMP(Type.UTILITY, (metal) -> new LampBlock(ExtendedProperties.of(MapColor.METAL).noOcclusion().sound(SoundType.LANTERN).strength(4.0F, 10.0F).randomTicks().lightLevel((state) -> (Boolean)state.getValue(LampBlock.LIT) ? 15 : 0).blockEntity(TFCBlockEntities.LAMP)), LampBlockItem::new),
        TRAPDOOR(Type.UTILITY, (metal) -> new TrapDoorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion().isValidSpawn(TFCBlocks::never), BlockSetType.IRON));

        // MetallumMetal.BlockType.BLOCK, MetallumMetal.BlockType.BLOCK_SLAB,
        // MetallumMetal.BlockType.BLOCK_STAIRS, MetallumMetal.BlockType.BARS,

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
}