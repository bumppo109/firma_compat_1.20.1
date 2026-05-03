package com.bumppo109.firma_compat.block;

import com.therighthon.afc.AFC;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistryWood;
import net.dries007.tfc.world.feature.tree.TFCTreeGrower;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

public enum CompatWood implements RegistryWood {
    ACACIA(false, MapColor.COLOR_ORANGE, MapColor.STONE,11, 210),
    BIRCH(false, MapColor.SAND, MapColor.QUARTZ,7, 145),
    CHERRY(false, MapColor.TERRACOTTA_WHITE, MapColor.TERRACOTTA_PINK,8, 170),
    DARK_OAK(false, MapColor.COLOR_BROWN, MapColor.COLOR_BROWN,10, 0),
    JUNGLE(false, MapColor.DIRT, MapColor.PODZOL,7, 255),
    MANGROVE(false, MapColor.COLOR_RED, MapColor.PODZOL,8, 100),
    OAK(false, MapColor.WOOD, MapColor.PODZOL,10, 120),
    SPRUCE(false, MapColor.PODZOL, MapColor.COLOR_BROWN,7, 0),
    CRIMSON(false, MapColor.CRIMSON_STEM, MapColor.CRIMSON_STEM,10, 0),
    WARPED(false, MapColor.WARPED_STEM, MapColor.WARPED_STEM,10, 0);

    public static final CompatWood[] VALUES = values();
    private final String serializedName;
    private final boolean conifer;
    private final MapColor woodColor;
    private final MapColor barkColor;
    private final TFCTreeGrower tree;
    private final int daysToGrow;
    private final BlockSetType blockSet;
    private final WoodType woodType;
    private final int autumnIndex;

    private CompatWood(boolean evergreen, MapColor woodColor, MapColor barkColor, int daysToGrow, int autumnIndex) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.conifer = evergreen;
        this.woodColor = woodColor;
        this.barkColor = barkColor;
        this.autumnIndex = autumnIndex;
        this.tree = new TFCTreeGrower(AFC.treeIdentifier("tree/" + this.serializedName), AFC.treeIdentifier("tree/" + this.serializedName + "_large"));
        this.daysToGrow = daysToGrow;
        this.blockSet = new BlockSetType(this.serializedName);
        this.woodType = new WoodType(Helpers.identifier(this.serializedName).toString(), this.blockSet);
    }

    public String getSerializedName() {
        return this.serializedName;
    }

    public boolean isConifer() {
        return this.conifer;
    }

    public MapColor woodColor() {
        return this.woodColor;
    }

    public MapColor barkColor() {
        return this.barkColor;
    }

    public Supplier<Block> getBlock(Wood.BlockType type) {
        return (Supplier)((Map)ModBlocks.WOODS.get(this)).get(type);
    }

    public BlockSetType getBlockSet() {
        return this.blockSet;
    }

    public WoodType getVanillaWoodType() {
        return this.woodType;
    }

    public TFCTreeGrower tree() {
        return this.tree;
    }

    public int daysToGrow() {
        return this.defaultDaysToGrow();
    }

    public int autumnIndex() {
        return this.autumnIndex;
    }

    public int defaultDaysToGrow() {
        return this.daysToGrow;
    }

    public static void registerBlockSetTypes() {
        for(CompatWood wood : VALUES) {
            BlockSetType.register(wood.blockSet);
            WoodType.register(wood.woodType);
        }

    }

    /**
     * Return true for BlockTypes you WANT to register.
     * Return false to skip them.
     */
    private static final Set<Wood.BlockType> ALLOWED_BLOCK_TYPES = Set.of(
            Wood.BlockType.LOOM,
            Wood.BlockType.LOG_FENCE,
            Wood.BlockType.TOOL_RACK,
            Wood.BlockType.TWIG,
            Wood.BlockType.VERTICAL_SUPPORT,
            Wood.BlockType.HORIZONTAL_SUPPORT,
            Wood.BlockType.SLUICE,
            Wood.BlockType.BARREL,
            Wood.BlockType.SCRIBING_TABLE,
            Wood.BlockType.SEWING_TABLE,
            Wood.BlockType.JAR_SHELF,
            Wood.BlockType.AXLE,
            Wood.BlockType.BLADED_AXLE,
            Wood.BlockType.ENCASED_AXLE,
            Wood.BlockType.CLUTCH,
            Wood.BlockType.GEAR_BOX,
            Wood.BlockType.WINDMILL,
            Wood.BlockType.WATER_WHEEL
    );

    public static boolean shouldRegisterBlockType(Wood.BlockType type) {
        return ALLOWED_BLOCK_TYPES.contains(type);
    }

    public Block planks() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> Blocks.ACACIA_PLANKS;
            case BIRCH    -> Blocks.BIRCH_PLANKS;
            case CHERRY   -> Blocks.CHERRY_PLANKS;
            case DARK_OAK -> Blocks.DARK_OAK_PLANKS;
            case JUNGLE   -> Blocks.JUNGLE_PLANKS;
            case MANGROVE -> Blocks.MANGROVE_PLANKS;
            case OAK      -> Blocks.OAK_PLANKS;
            case SPRUCE   -> Blocks.SPRUCE_PLANKS;
            case CRIMSON  -> Blocks.CRIMSON_PLANKS;
            case WARPED   -> Blocks.WARPED_PLANKS;
            // No default needed — enum switch is exhaustive
        };
    }

    public Block strippedLog() {
        return switch (this) {
            case ACACIA   -> Blocks.STRIPPED_ACACIA_LOG;
            case BIRCH    -> Blocks.STRIPPED_BIRCH_LOG;
            case CHERRY   -> Blocks.STRIPPED_CHERRY_LOG;
            case DARK_OAK -> Blocks.STRIPPED_DARK_OAK_LOG;
            case JUNGLE   -> Blocks.STRIPPED_JUNGLE_LOG;
            case MANGROVE -> Blocks.STRIPPED_MANGROVE_LOG;
            case OAK      -> Blocks.STRIPPED_OAK_LOG;
            case SPRUCE   -> Blocks.STRIPPED_SPRUCE_LOG;
            case CRIMSON  -> Blocks.STRIPPED_CRIMSON_STEM;   // Note: stems for fungi
            case WARPED   -> Blocks.STRIPPED_WARPED_STEM;
            // No default needed
        };
    }

    public Block stair() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> Blocks.ACACIA_STAIRS;
            case BIRCH    -> Blocks.BIRCH_STAIRS;
            case CHERRY   -> Blocks.CHERRY_STAIRS;
            case DARK_OAK -> Blocks.DARK_OAK_STAIRS;
            case JUNGLE   -> Blocks.JUNGLE_STAIRS;
            case MANGROVE -> Blocks.MANGROVE_STAIRS;
            case OAK      -> Blocks.OAK_STAIRS;
            case SPRUCE   -> Blocks.SPRUCE_STAIRS;
            case CRIMSON  -> Blocks.CRIMSON_STAIRS;
            case WARPED   -> Blocks.WARPED_STAIRS;
            // No default needed — enum switch is exhaustive
        };
    }

    public Block slab() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> Blocks.ACACIA_SLAB;
            case BIRCH    -> Blocks.BIRCH_SLAB;
            case CHERRY   -> Blocks.CHERRY_SLAB;
            case DARK_OAK -> Blocks.DARK_OAK_SLAB;
            case JUNGLE   -> Blocks.JUNGLE_SLAB;
            case MANGROVE -> Blocks.MANGROVE_SLAB;
            case OAK      -> Blocks.OAK_SLAB;
            case SPRUCE   -> Blocks.SPRUCE_SLAB;
            case CRIMSON  -> Blocks.CRIMSON_SLAB;
            case WARPED   -> Blocks.WARPED_SLAB;
            // No default needed — enum switch is exhaustive
        };
    }

    public Block log() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> Blocks.ACACIA_LOG;
            case BIRCH    -> Blocks.BIRCH_LOG;
            case CHERRY   -> Blocks.CHERRY_LOG;
            case DARK_OAK -> Blocks.DARK_OAK_LOG;
            case JUNGLE   -> Blocks.JUNGLE_LOG;
            case MANGROVE -> Blocks.MANGROVE_LOG;
            case OAK      -> Blocks.OAK_LOG;
            case SPRUCE   -> Blocks.SPRUCE_LOG;
            case CRIMSON  -> Blocks.CRIMSON_STEM;
            case WARPED   -> Blocks.WARPED_STEM;
            // No default needed — enum switch is exhaustive
        };
    }

    public Block door() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> Blocks.ACACIA_DOOR;
            case BIRCH    -> Blocks.BIRCH_DOOR;
            case CHERRY   -> Blocks.CHERRY_DOOR;
            case DARK_OAK -> Blocks.DARK_OAK_DOOR;
            case JUNGLE   -> Blocks.JUNGLE_DOOR;
            case MANGROVE -> Blocks.MANGROVE_DOOR;
            case OAK      -> Blocks.OAK_DOOR;
            case SPRUCE   -> Blocks.SPRUCE_DOOR;
            case CRIMSON  -> Blocks.CRIMSON_DOOR;
            case WARPED   -> Blocks.WARPED_DOOR;
            // No default needed — enum switch is exhaustive
        };
    }

    public Block trapdoor() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> Blocks.ACACIA_TRAPDOOR;
            case BIRCH    -> Blocks.BIRCH_TRAPDOOR;
            case CHERRY   -> Blocks.CHERRY_TRAPDOOR;
            case DARK_OAK -> Blocks.DARK_OAK_TRAPDOOR;
            case JUNGLE   -> Blocks.JUNGLE_TRAPDOOR;
            case MANGROVE -> Blocks.MANGROVE_TRAPDOOR;
            case OAK      -> Blocks.OAK_TRAPDOOR;
            case SPRUCE   -> Blocks.SPRUCE_TRAPDOOR;
            case CRIMSON  -> Blocks.CRIMSON_TRAPDOOR;
            case WARPED   -> Blocks.WARPED_TRAPDOOR;
            // No default needed — enum switch is exhaustive
        };
    }

    public Block fence() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> Blocks.ACACIA_FENCE;
            case BIRCH    -> Blocks.BIRCH_FENCE;
            case CHERRY   -> Blocks.CHERRY_FENCE;
            case DARK_OAK -> Blocks.DARK_OAK_FENCE;
            case JUNGLE   -> Blocks.JUNGLE_FENCE;
            case MANGROVE -> Blocks.MANGROVE_FENCE;
            case OAK      -> Blocks.OAK_FENCE;
            case SPRUCE   -> Blocks.SPRUCE_FENCE;
            case CRIMSON  -> Blocks.CRIMSON_FENCE;
            case WARPED   -> Blocks.WARPED_FENCE;
            // No default needed — enum switch is exhaustive
        };
    }

    public Block fenceGate() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> Blocks.ACACIA_FENCE_GATE;
            case BIRCH    -> Blocks.BIRCH_FENCE_GATE;
            case CHERRY   -> Blocks.CHERRY_FENCE_GATE;
            case DARK_OAK -> Blocks.DARK_OAK_FENCE_GATE;
            case JUNGLE   -> Blocks.JUNGLE_FENCE_GATE;
            case MANGROVE -> Blocks.MANGROVE_FENCE_GATE;
            case OAK      -> Blocks.OAK_FENCE_GATE;
            case SPRUCE   -> Blocks.SPRUCE_FENCE_GATE;
            case CRIMSON  -> Blocks.CRIMSON_FENCE_GATE;
            case WARPED   -> Blocks.WARPED_FENCE_GATE;
            // No default needed — enum switch is exhaustive
        };
    }

    public Block sign() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> Blocks.ACACIA_SIGN;
            case BIRCH    -> Blocks.BIRCH_SIGN;
            case CHERRY   -> Blocks.CHERRY_SIGN;
            case DARK_OAK -> Blocks.DARK_OAK_SIGN;
            case JUNGLE   -> Blocks.JUNGLE_SIGN;
            case MANGROVE -> Blocks.MANGROVE_SIGN;
            case OAK      -> Blocks.OAK_SIGN;
            case SPRUCE   -> Blocks.SPRUCE_SIGN;
            case CRIMSON  -> Blocks.CRIMSON_SIGN;
            case WARPED   -> Blocks.WARPED_SIGN;
            // No default needed — enum switch is exhaustive
        };
    }

    public Block hangingSign() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> Blocks.ACACIA_HANGING_SIGN;
            case BIRCH    -> Blocks.BIRCH_HANGING_SIGN;
            case CHERRY   -> Blocks.CHERRY_HANGING_SIGN;
            case DARK_OAK -> Blocks.DARK_OAK_HANGING_SIGN;
            case JUNGLE   -> Blocks.JUNGLE_HANGING_SIGN;
            case MANGROVE -> Blocks.MANGROVE_HANGING_SIGN;
            case OAK      -> Blocks.OAK_HANGING_SIGN;
            case SPRUCE   -> Blocks.SPRUCE_HANGING_SIGN;
            case CRIMSON  -> Blocks.CRIMSON_HANGING_SIGN;
            case WARPED   -> Blocks.WARPED_HANGING_SIGN;
            // No default needed — enum switch is exhaustive
        };
    }

    public Block pressurePlate() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> Blocks.ACACIA_PRESSURE_PLATE;
            case BIRCH    -> Blocks.BIRCH_PRESSURE_PLATE;
            case CHERRY   -> Blocks.CHERRY_PRESSURE_PLATE;
            case DARK_OAK -> Blocks.DARK_OAK_PRESSURE_PLATE;
            case JUNGLE   -> Blocks.JUNGLE_PRESSURE_PLATE;
            case MANGROVE -> Blocks.MANGROVE_PRESSURE_PLATE;
            case OAK      -> Blocks.OAK_PRESSURE_PLATE;
            case SPRUCE   -> Blocks.SPRUCE_PRESSURE_PLATE;
            case CRIMSON  -> Blocks.CRIMSON_PRESSURE_PLATE;
            case WARPED   -> Blocks.WARPED_PRESSURE_PLATE;
            // No default needed — enum switch is exhaustive
        };
    }

    public ResourceLocation planksTexture() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> ResourceLocation.withDefaultNamespace("block/acacia_planks");
            case BIRCH    -> ResourceLocation.withDefaultNamespace("block/birch_planks");
            case CHERRY   -> ResourceLocation.withDefaultNamespace("block/cherry_planks");
            case DARK_OAK -> ResourceLocation.withDefaultNamespace("block/dark_oak_planks");
            case JUNGLE   -> ResourceLocation.withDefaultNamespace("block/jungle_planks");
            case MANGROVE -> ResourceLocation.withDefaultNamespace("block/mangrove_planks");
            case OAK      -> ResourceLocation.withDefaultNamespace("block/oak_planks");
            case SPRUCE   -> ResourceLocation.withDefaultNamespace("block/spruce_planks");
            case CRIMSON  -> ResourceLocation.withDefaultNamespace("block/crimson_planks");
            case WARPED   -> ResourceLocation.withDefaultNamespace("block/warped_planks");
            // No default needed — enum switch is exhaustive
        };
    }

    public ResourceLocation logSideTexture() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA   -> ResourceLocation.withDefaultNamespace("block/acacia_log");
            case BIRCH    -> ResourceLocation.withDefaultNamespace("block/birch_log");
            case CHERRY   -> ResourceLocation.withDefaultNamespace("block/cherry_log");
            case DARK_OAK -> ResourceLocation.withDefaultNamespace("block/dark_oak_log");
            case JUNGLE   -> ResourceLocation.withDefaultNamespace("block/jungle_log");
            case MANGROVE -> ResourceLocation.withDefaultNamespace("block/mangrove_log");
            case OAK      -> ResourceLocation.withDefaultNamespace("block/oak_log");
            case SPRUCE   -> ResourceLocation.withDefaultNamespace("block/spruce_log");
            case CRIMSON  -> ResourceLocation.withDefaultNamespace("block/crimson_stem");
            case WARPED   -> ResourceLocation.withDefaultNamespace("block/warped_stem");
            // No default needed — enum switch is exhaustive
        };
    }

    public ResourceLocation strippedLogTexture() {
        return switch (this) {  // 'this' is the current enum instance
            case ACACIA -> ResourceLocation.withDefaultNamespace("block/stripped_acacia_log");
            case BIRCH -> ResourceLocation.withDefaultNamespace("block/stripped_birch_log");
            case CHERRY -> ResourceLocation.withDefaultNamespace("block/stripped_cherry_log");
            case DARK_OAK -> ResourceLocation.withDefaultNamespace("block/stripped_dark_oak_log");
            case JUNGLE -> ResourceLocation.withDefaultNamespace("block/stripped_jungle_log");
            case MANGROVE -> ResourceLocation.withDefaultNamespace("block/stripped_mangrove_log");
            case OAK -> ResourceLocation.withDefaultNamespace("block/stripped_oak_log");
            case SPRUCE -> ResourceLocation.withDefaultNamespace("block/stripped_spruce_log");
            case CRIMSON -> ResourceLocation.withDefaultNamespace("block/stripped_crimson_stem");
            case WARPED -> ResourceLocation.withDefaultNamespace("block/stripped_warped_stem");
            // No default needed — enum switch is exhaustive
        };
    }
}
