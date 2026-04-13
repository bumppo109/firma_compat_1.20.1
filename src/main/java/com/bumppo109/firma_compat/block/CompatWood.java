package com.bumppo109.firma_compat.block;

import com.bumppo109.firma_compat.FirmaCompatHelpers;
import net.dries007.tfc.common.blockentities.BarrelBlockEntity;
import net.dries007.tfc.common.blockentities.LoomBlockEntity;
import net.dries007.tfc.common.blockentities.SluiceBlockEntity;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blockentities.rotation.WaterWheelBlockEntity;
import net.dries007.tfc.common.blockentities.rotation.WindmillBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.dries007.tfc.common.blocks.JarShelfBlock;
import net.dries007.tfc.common.blocks.devices.BarrelBlock;
import net.dries007.tfc.common.blocks.devices.SluiceBlock;
import net.dries007.tfc.common.blocks.rotation.*;
import net.dries007.tfc.common.blocks.wood.*;
import net.dries007.tfc.common.items.BarrelBlockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.apache.commons.lang3.function.TriFunction;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public enum CompatWood implements ModRegistryWood
{
    //Wood color, then bark color
    ACACIA(false, MapColor.COLOR_ORANGE, MapColor.STONE,11, 210),
    BIRCH(false, MapColor.SAND, MapColor.QUARTZ,7, 145),
    CHERRY(false, MapColor.TERRACOTTA_WHITE, MapColor.TERRACOTTA_PINK,8, 170),
    DARK_OAK(false, MapColor.COLOR_BROWN, MapColor.COLOR_BROWN,10, 0),
    JUNGLE(false, MapColor.DIRT, MapColor.PODZOL,7, 255),
    MANGROVE(false, MapColor.COLOR_RED, MapColor.PODZOL,8, 100),
    OAK(false, MapColor.WOOD, MapColor.PODZOL,10, 120),
    SPRUCE(false, MapColor.PODZOL, MapColor.COLOR_BROWN,7, 0),
    CRIMSON(false, MapColor.CRIMSON_STEM, MapColor.CRIMSON_STEM,10, 0),
    WARPED(false, MapColor.WARPED_STEM, MapColor.WARPED_STEM,10, 0)
    ;

    public static final CompatWood[] VALUES = values();

    private final String serializedName;
    private final boolean conifer;
    private final MapColor woodColor;
    private final MapColor barkColor;
    private final int daysToGrow;
    private final BlockSetType blockSet;
    private final WoodType woodType;
    private final int autumnIndex;
    //private final float saplingDropRate;

    CompatWood(boolean evergreen, MapColor woodColor, MapColor barkColor, int daysToGrow, int autumnIndex) {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
        this.conifer = evergreen;
        this.woodColor = woodColor;
        this.barkColor = barkColor;
        this.autumnIndex = autumnIndex;
        this.daysToGrow = daysToGrow;
        this.blockSet = new BlockSetType(serializedName);
        this.woodType = new WoodType(FirmaCompatHelpers.modIdentifier(this.serializedName).toString(), this.blockSet);
        //this.saplingDropRate = saplingDropRate;
    }

    // Add these methods inside the CompatWood enum class (after the constructor)

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
            case ACACIA   -> ResourceLocation.withDefaultNamespace("block/stripped_acacia_log");
            case BIRCH    -> ResourceLocation.withDefaultNamespace("block/stripped_birch_log");
            case CHERRY   -> ResourceLocation.withDefaultNamespace("block/stripped_cherry_log");
            case DARK_OAK -> ResourceLocation.withDefaultNamespace("block/stripped_dark_oak_log");
            case JUNGLE   -> ResourceLocation.withDefaultNamespace("block/stripped_jungle_log");
            case MANGROVE -> ResourceLocation.withDefaultNamespace("block/stripped_mangrove_log");
            case OAK      -> ResourceLocation.withDefaultNamespace("block/stripped_oak_log");
            case SPRUCE   -> ResourceLocation.withDefaultNamespace("block/stripped_spruce_log");
            case CRIMSON  -> ResourceLocation.withDefaultNamespace("block/stripped_crimson_stem");
            case WARPED   -> ResourceLocation.withDefaultNamespace("block/stripped_warped_stem");
            // No default needed — enum switch is exhaustive
        };
    }

    //Texture Locations
    /*
    public ResourceLocation strippedLogSideTexture(ModRegistryWood wood)
    {
        return Helpers.identifierMC("stripped_" + wood.getSerializedName() + "_log");
    }
    public ResourceLocation strippedLogTopTexture(ModRegistryWood wood)
    {
        return Helpers.identifierMC("stripped_" + wood.getSerializedName() + "_log_top");
    }
    public ResourceLocation logSideTexture(ModRegistryWood wood)
    {
        return Helpers.identifierMC(wood.getSerializedName() + "_log");
    }
    public ResourceLocation logTopTexture(ModRegistryWood wood)
    {
        return Helpers.identifierMC(wood.getSerializedName() + "_log_top");
    }

     */

    @Override
    public String getSerializedName()
    {
        return serializedName;
    }

    public boolean isConifer()
    {
        return conifer;
    }

    @Override
    public MapColor woodColor()
    {
        return woodColor;
    }

    @Override
    public MapColor barkColor()
    {
        return barkColor;
    }

    @Override
    public Supplier<Integer> ticksToGrow() {
        return null;
    }

    @Override
    public BlockSetType getBlockSet()
    {
        return blockSet;
    }

    @Override
    public WoodType getVanillaWoodType()
    {
        return woodType;
    }

    /*
    public Supplier<Integer> ticksToGrow() {
        return () -> daysToGrow() * Calendar.CALENDAR_TICKS_IN_DAY;
    }

     */

    public int daysToGrow() {
        //return (Integer)((ForgeConfigSpec.IntValue) AFCConfig.SERVER.saplingGrowthDays.get(this)).get();
        return defaultDaysToGrow();
    }

    @Override
    public int autumnIndex()
    {
        return autumnIndex;
    }

    @Override
    public Supplier<Block> getBlock(BlockType type) {
        return ModBlocks.WOODS.get(this).get(type);
    }

    /*
    public float getSaplingDropRate()
    {
        return saplingDropRate;
    }

     */

    public int defaultDaysToGrow() {
        return daysToGrow;
    }

    public enum BlockType
    {
        TWIG(wood -> GroundcoverBlock.twig(ExtendedProperties.of().strength(0.05F, 0.0F).sound(SoundType.WOOD).noCollission().flammableLikeWool())),
        LOG_FENCE(wood -> new TFCFenceBlock(properties(wood).strength(2.0F, 3.0F).flammableLikeLogs())),
        VERTICAL_SUPPORT(wood -> new VerticalSupportBlock(properties(wood).strength(1.0F).noOcclusion().flammableLikeLogs())),
        HORIZONTAL_SUPPORT(wood -> new HorizontalSupportBlock(properties(wood).strength(1.0F).noOcclusion().flammableLikeLogs())),
        TOOL_RACK(wood -> new ToolRackBlock(properties(wood).strength(2.0F).noOcclusion().blockEntity(TFCBlockEntities.TOOL_RACK))),
        LOOM((self, wood) -> new TFCLoomBlock(properties(wood).strength(2.5F).noOcclusion().flammableLikePlanks().blockEntity(TFCBlockEntities.LOOM).ticks(LoomBlockEntity::tick), self.planksTexture(wood))),
        SLUICE(wood -> new SluiceBlock(properties(wood).strength(3F).noOcclusion().flammableLikeLogs().blockEntity(TFCBlockEntities.SLUICE).serverTicks(SluiceBlockEntity::serverTick))),
        BARREL((self, wood) -> new BarrelBlock(properties(wood).strength(2.5f).flammableLikePlanks().noOcclusion().blockEntity(TFCBlockEntities.BARREL).serverTicks(BarrelBlockEntity::serverTick)), BarrelBlockItem::new),
        SCRIBING_TABLE(wood -> new ScribingTableBlock(properties(wood).noOcclusion().strength(2.5F).flammable(20, 30))),
        SEWING_TABLE(wood -> new SewingTableBlock(properties(wood).noOcclusion().strength(2.5F).flammable(20, 30))),
        SHELF(wood -> new JarShelfBlock(properties(wood).noOcclusion().strength(2.5f).flammableLikePlanks().blockEntity(TFCBlockEntities.JARS))),

        AXLE((self, wood) -> new AxleBlock(properties(wood).noOcclusion().strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.AXLE), getBlock(wood, self.windmill()), self.planksTexture(wood))),
        BLADED_AXLE((self, wood) -> new BladedAxleBlock(properties(wood).noOcclusion().strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.BLADED_AXLE), getBlock(wood, self.axle()))),
        ENCASED_AXLE((self, wood) -> new EncasedAxleBlock(properties(wood).strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.ENCASED_AXLE))),
        CLUTCH((self, wood) -> new ClutchBlock(properties(wood).strength(2.5F).flammableLikeLogs().pushReaction(PushReaction.DESTROY).blockEntity(TFCBlockEntities.CLUTCH), getBlock(wood, self.axle()))),
        GEAR_BOX((self, wood) -> new GearBoxBlock(properties(wood).strength(2f).noOcclusion().blockEntity(TFCBlockEntities.GEAR_BOX), getBlock(wood, self.axle()))),
        WINDMILL((self, wood) -> new WindmillBlock(properties(wood).strength(9f).noOcclusion().blockEntity(TFCBlockEntities.WINDMILL).ticks(WindmillBlockEntity::serverTick, WindmillBlockEntity::clientTick), getBlock(wood, self.axle()))),
        WATER_WHEEL((self, wood) -> new WaterWheelBlock(properties(wood).strength(9f).noOcclusion().blockEntity(TFCBlockEntities.WATER_WHEEL).ticks(WaterWheelBlockEntity::serverTick, WaterWheelBlockEntity::clientTick), getBlock(wood, self.axle()), self.waterWheelTexture(wood)));

        private static ExtendedProperties properties(ModRegistryWood wood)
        {
            return ExtendedProperties.of(wood.woodColor()).sound(SoundType.WOOD).instrument(NoteBlockInstrument.BASS);
        }

        @SuppressWarnings("unchecked")
        private static <B extends Block> Supplier<? extends B> getBlock(ModRegistryWood wood, BlockType type)
        {
            return (Supplier<? extends B>) wood.getBlock(type);
        }

        private final BiFunction<BlockType, ModRegistryWood, Block> blockFactory;
        private final TriFunction<Block, Item.Properties, ModRegistryWood, ? extends BlockItem> blockItemFactory;

        BlockType(Function<ModRegistryWood, Block> blockFactory)
        {
            this((self, wood) -> blockFactory.apply(wood));
        }

        BlockType(BiFunction<BlockType, ModRegistryWood, Block> blockFactory)
        {
            this(blockFactory, BlockItem::new);
        }

        BlockType(BiFunction<BlockType, ModRegistryWood, Block> blockFactory, BiFunction<Block, Item.Properties, ? extends BlockItem> blockItemFactory)
        {
            this(blockFactory, (block, properties, self) -> blockItemFactory.apply(block, properties));
        }

        BlockType(BiFunction<BlockType, ModRegistryWood, Block> blockFactory, TriFunction<Block, Item.Properties, ModRegistryWood, ? extends BlockItem> blockItemFactory)
        {
            this.blockFactory = blockFactory;
            this.blockItemFactory = blockItemFactory;
        }

        @Nullable
        public Function<Block, BlockItem> createBlockItem(ModRegistryWood wood, Item.Properties properties)
        {
            return needsItem() ? block -> blockItemFactory.apply(block, properties, wood) : null;
        }

        public String nameFor(ModRegistryWood wood)
        {
            return switch (this)
            {
                // N.B. Only stairs and slabs use the variant of planks, this is consistent with stairs + slabs in the rest of the mod
                // and also with that these stair and slabs are based on the planks block
                //case SLAB -> "wood/planks/%s_slab".formatted(wood.getSerializedName());
                //case STAIRS -> "wood/planks/%s_stairs".formatted(wood.getSerializedName());
                default -> "%s_%s".formatted(wood.getSerializedName(), name().toLowerCase(Locale.ROOT));
            };
        }

        public boolean needsItem()
        {
            return switch (this)
            {
                case VERTICAL_SUPPORT, HORIZONTAL_SUPPORT, WINDMILL -> false;
                default -> true;
            };
        }

        /*
        private BlockType stripped()
        {
            return switch (this)
            {
                //case LOG -> STRIPPED_LOG;
                //case WOOD -> STRIPPED_WOOD;
                default -> throw new IllegalStateException("Block type " + name() + " does not have a stripped variant");
            };
        }

         */

        //TODO - redirect to base mod
        private ResourceLocation planksTexture(ModRegistryWood wood)
        {
            return ResourceLocation.withDefaultNamespace("block/" + wood.getSerializedName() + "_planks");
        }
        private ResourceLocation waterWheelTexture(ModRegistryWood wood)
        {
            return FirmaCompatHelpers.modIdentifier("textures/entity/" + wood.getSerializedName() + "_water_wheel.png");
        }

        private BlockType twig() {return TWIG;}

        //private BlockType fallenLeaves() {return FALLEN_LEAVES;}

        //private BlockType leaves() {return LEAVES;}

        private BlockType axle() {return AXLE;}

        private BlockType windmill() {return WINDMILL;}

        public Supplier<Block> create(ModRegistryWood wood)
        {
            return () -> blockFactory.apply(this, wood);
        }
    }

    public static void registerBlockSetTypes()
    {
        for (CompatWood wood : VALUES)
        {
            BlockSetType.register(wood.blockSet);
            WoodType.register(wood.woodType);
        }
    }



}
