package com.bumppo109.firma_compat.block;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.blocks.rock.AqueductBlock;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.RockDisplayCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.block.Blocks;

import net.dries007.tfc.common.blocks.TFCBlocks; // only kept if you still need it elsewhere; can be removed if unused now

/**
 * Default rocks that are used for block registration calls. Not extensible.
 */
public enum CompatRock implements ModRegistryRock {

    GRANITE(RockDisplayCategory.MAFIC_IGNEOUS_INTRUSIVE, MapColor.DIRT),
    DIORITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_INTRUSIVE, MapColor.QUARTZ),
    DEEPSLATE(RockDisplayCategory.FELSIC_IGNEOUS_INTRUSIVE, MapColor.DEEPSLATE),
    ANDESITE(RockDisplayCategory.INTERMEDIATE_IGNEOUS_EXTRUSIVE, MapColor.STONE),
    BASALT(RockDisplayCategory.MAFIC_IGNEOUS_EXTRUSIVE, MapColor.COLOR_BLACK),
    CALCITE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_WHITE),
    TUFF(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_GRAY),
    DRIPSTONE(RockDisplayCategory.SEDIMENTARY, MapColor.TERRACOTTA_BROWN),
    STONE(RockDisplayCategory.METAMORPHIC, MapColor.STONE),
    BLACKSTONE(RockDisplayCategory.METAMORPHIC, MapColor.COLOR_BLACK),
    NETHERRACK(RockDisplayCategory.METAMORPHIC, MapColor.NETHER),
    END_STONE(RockDisplayCategory.METAMORPHIC, MapColor.SAND);

    public static final CompatRock[] VALUES = values();

    private final String serializedName;
    private final RockDisplayCategory category;
    private final MapColor color;

    CompatRock(RockDisplayCategory category, MapColor color) {
        this.serializedName = name().toLowerCase(Locale.ROOT);
        this.category = category;
        this.color = color;
    }

    @Override
    public RockDisplayCategory displayCategory() {
        return category;
    }

    @Override
    public MapColor color() {
        return color;
    }

    @Override
    public Supplier<? extends Block> getBlock(BlockType type) {
        return ModBlocks.ROCK_BLOCKS.get(this).get(type);
    }

    @Override
    public Supplier<? extends SlabBlock> getSlab(BlockType type) {
        return ModBlocks.ROCK_DECORATIONS.get(this).get(type).slab();
    }

    @Override
    public Supplier<? extends StairBlock> getStair(BlockType type) {
        return ModBlocks.ROCK_DECORATIONS.get(this).get(type).stair();
    }

    @Override
    public Supplier<? extends WallBlock> getWall(BlockType type) {
        return ModBlocks.ROCK_DECORATIONS.get(this).get(type).wall();
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    public enum BlockType implements StringRepresentable {
        HARDENED((rock, self) -> new Block(properties(rock)), true),
        LOOSE_COBBLE((rock, self) -> new Block(properties(rock)), false),
        HARDENED_COBBLE((rock, self) -> new Block(properties(rock)), true),
        LOOSE((rock, self) -> new LooseRockBlock(properties(rock).noCollission()), false),
        BRICK((rock, self) -> new Block(properties(rock)), true),
        BRICK_AQUEDUCT((rock, self) -> new AqueductBlock(properties(rock)), false);  // assuming AqueductBlock exists and takes Properties

        public static final BlockType[] VALUES = BlockType.values();

        private static BlockBehaviour.Properties properties(ModRegistryRock rock)
        {
            return BlockBehaviour.Properties.of().mapColor(rock.color()).sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM);
        }

        private final boolean variants;
        private final BiFunction<ModRegistryRock, BlockType, Block> blockFactory;
        private final String serializedName;

        BlockType(BiFunction<ModRegistryRock, BlockType, Block> blockFactory, boolean variants) {
            this.blockFactory = blockFactory;
            this.variants = variants;
            this.serializedName = name().toLowerCase(Locale.ROOT);
        }

        public boolean hasVariants() {
            return variants;
        }

        public Block create(ModRegistryRock rock) {
            return blockFactory.apply(rock, this);
        }

        @Override
        public String getSerializedName() {
            return serializedName;
        }

        public SlabBlock createSlab(ModRegistryRock rock) {
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .sound(SoundType.STONE)
                    .strength(1.5f, 10)
                    .requiresCorrectToolForDrops();

            return new SlabBlock(properties);
        }

        public StairBlock createStairs(ModRegistryRock rock) {
            final Supplier<BlockState> state = () -> rock.getBlock(this).get().defaultBlockState();
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .sound(SoundType.STONE)
                    .strength(1.5f, 10)
                    .requiresCorrectToolForDrops();

            return new StairBlock(state, properties);
        }

        public WallBlock createWall(ModRegistryRock rock) {
            final BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .sound(SoundType.STONE)
                    .strength(1.5f, 10)
                    .requiresCorrectToolForDrops();

            return new WallBlock(properties);
        }
    }

    /**
     * Returns the vanilla Minecraft block that this rock type is primarily based on.
     */
    public Supplier<Block> vanillaEquivalent() {
        return switch (this) {
            case GRANITE     -> () -> Blocks.GRANITE;
            case DIORITE     -> () -> Blocks.DIORITE;
            case ANDESITE    -> () -> Blocks.ANDESITE;
            case STONE       -> () -> Blocks.STONE;
            case BLACKSTONE  -> () -> Blocks.BLACKSTONE;
            case DEEPSLATE   -> () -> Blocks.DEEPSLATE;
            case BASALT      -> () -> Blocks.BASALT;
            case CALCITE     -> () -> Blocks.CALCITE;
            case TUFF        -> () -> Blocks.TUFF;
            case DRIPSTONE   -> () -> Blocks.DRIPSTONE_BLOCK;
            case NETHERRACK  -> () -> Blocks.NETHERRACK;
            case END_STONE   -> () -> Blocks.END_STONE;
        };
    }

    public Supplier<Block> rawBlock() {
        return switch (this) {
            case ANDESITE -> () -> Blocks.ANDESITE;
            case DEEPSLATE -> () -> Blocks.DEEPSLATE;
            case DIORITE -> () -> Blocks.DIORITE;
            case CALCITE -> () -> Blocks.CALCITE;
            case END_STONE -> () -> Blocks.END_STONE;
            case STONE -> () -> Blocks.STONE;
            case BLACKSTONE -> () -> Blocks.BLACKSTONE;
            case NETHERRACK -> () -> Blocks.NETHERRACK;
            case GRANITE -> () -> Blocks.GRANITE;
            case BASALT -> () -> Blocks.BASALT;
            case TUFF -> () -> Blocks.TUFF;
            case DRIPSTONE -> () -> Blocks.DRIPSTONE_BLOCK;
        };
    }

    public Supplier<Block> bricksBlock() {
        return switch (this) {
            case DEEPSLATE -> () -> Blocks.DEEPSLATE_BRICKS;
            case END_STONE -> () -> Blocks.END_STONE_BRICKS;
            case STONE -> () -> Blocks.STONE_BRICKS;
            case BLACKSTONE -> () -> Blocks.POLISHED_BLACKSTONE_BRICKS;
            case NETHERRACK -> () -> Blocks.NETHER_BRICKS;
            default -> () -> ModBlocks.ROCK_BLOCKS.get(this).get(BlockType.BRICK).get();
        };
    }

    public Item brickItem() {
        if (this == NETHERRACK) {
            return Items.NETHER_BRICK;
        } else {
            return ModItems.BRICK.get(this).get();
        }
    }

    public Supplier<Block> hardCobbleBlock() {
        return switch (this) {
            case STONE -> () -> Blocks.COBBLESTONE;
            case DEEPSLATE -> () -> Blocks.COBBLED_DEEPSLATE;
            default -> () -> ModBlocks.ROCK_BLOCKS.get(this).get(BlockType.HARDENED_COBBLE).get();
        };
    }

    public ResourceLocation rawTexture() {
        return switch (this) {
            case BASALT -> ResourceLocation.withDefaultNamespace("block/basalt_side");
            case DRIPSTONE -> ResourceLocation.withDefaultNamespace("block/dripstone_block");
            default -> ResourceLocation.withDefaultNamespace("block/" + this.serializedName);
        };
    }
    public ResourceLocation bricksTexture() {
        return switch (this) {
            case STONE -> ResourceLocation.withDefaultNamespace("block/stone_bricks");
            case DEEPSLATE -> ResourceLocation.withDefaultNamespace("block/deepslate_bricks");
            case END_STONE -> ResourceLocation.withDefaultNamespace("block/end_stone_bricks");
            case BLACKSTONE -> ResourceLocation.withDefaultNamespace("block/polished_blackstone_bricks");
            case NETHERRACK -> ResourceLocation.withDefaultNamespace("block/nether_bricks");
            default -> ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/" + this.serializedName + "_bricks");
        };
    }
    public ResourceLocation cobbleTexture() {
        return switch (this) {
            case STONE -> ResourceLocation.withDefaultNamespace("block/cobblestone");
            case DEEPSLATE -> ResourceLocation.withDefaultNamespace("block/cobbled_deepslate");
            default -> ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/loose_" + this.serializedName + "_cobble");
        };
    }
}