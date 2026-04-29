package com.bumppo109.firma_compat.addons.rnr;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import com.therighthon.rnr.common.block.*;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CompatRnRBlocks {
    public static final DeferredRegister<Block> BLOCKS;
    public static final BlockBehaviour.Properties ROOF_PROPERTIES;
    public static final Map<CompatWood, RegistryObject<Block>> WOOD_SHINGLE_ROOFS;
    public static final Map<CompatWood, RegistryObject<Block>> WOOD_SHINGLE_ROOF_SLABS;
    public static final Map<CompatWood, RegistryObject<Block>> WOOD_SHINGLE_ROOF_STAIRS;

    public static final Map<CompatRock, Map<CompatRnRStoneType, RegistryObject<Block>>> ROCK_BLOCKS;
    public static final Map<CompatRock, Map<CompatRnRStoneType, RegistryObject<Block>>> ROCK_SLABS;
    public static final Map<CompatRock, Map<CompatRnRStoneType, RegistryObject<Block>>> ROCK_STAIRS;

    public static final RegistryObject<Block> TAMPED_DIRT;
    public static final RegistryObject<Block> TAMPED_MUD;
    public static final RegistryObject<Block> OVER_HEIGHT_GRAVEL;

    public static final RegistryObject<Block> GRAVEL_ROAD;
    public static final RegistryObject<Block> GRAVEL_ROAD_SLAB;
    public static final RegistryObject<Block> GRAVEL_ROAD_STAIRS;

    public static final RegistryObject<Block> MACADAM_ROAD;
    public static final RegistryObject<Block> MACADAM_ROAD_SLAB;
    public static final RegistryObject<Block> MACADAM_ROAD_STAIRS;


    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (Function)null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (Function)((block) -> new BlockItem((Block) block, new Item.Properties())));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties) {
        return register(name, blockSupplier, (Function)((block) -> new BlockItem((Block) block, blockItemProperties)));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory) {
        return RegistrationHelpers.registerBlock(BLOCKS, CompatRnRItems.ITEMS, name, blockSupplier, blockItemFactory);
    }

    static {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FirmaCompat.MODID);

        TAMPED_DIRT = register("tamped_dirt", () -> new TampedSoilBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(3.0F).sound(SoundType.ROOTED_DIRT)));
        TAMPED_MUD = register("tamped_mud", () -> new TampedSoilBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(3.0F).sound(SoundType.PACKED_MUD)));
        OVER_HEIGHT_GRAVEL = register("over_height_gravel", () -> new OverHeightBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.GRAVEL).strength(1.5F, 10.0F).requiresCorrectToolForDrops()));

        ROOF_PROPERTIES = ExtendedProperties.of(MapColor.WOOD).strength(1.0F, 0.6F).noOcclusion().isViewBlocking(TFCBlocks::never).sound(SoundType.WOOD).flammable(50, 100).properties();
        WOOD_SHINGLE_ROOFS = Helpers.mapOfKeys(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_shingles", () -> new Block(ROOF_PROPERTIES)));
        WOOD_SHINGLE_ROOF_SLABS = Helpers.mapOfKeys(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_shingles_slab", () -> new SlabBlock(ROOF_PROPERTIES)));
        WOOD_SHINGLE_ROOF_STAIRS = Helpers.mapOfKeys(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_shingles_stairs", () -> new StairBlock(((Block)((RegistryObject)WOOD_SHINGLE_ROOFS.get(wood)).get()).defaultBlockState(), ROOF_PROPERTIES)));

        ROCK_BLOCKS = Helpers.mapOfKeys(CompatRock.class, (rock) -> Helpers.mapOfKeys(CompatRnRStoneType.class, (type) -> register(rock.name() + "_" + type.name(), () -> type.create(rock))));
        ROCK_SLABS = Helpers.mapOfKeys(CompatRock.class, (rock) -> Helpers.mapOfKeys(CompatRnRStoneType.class, CompatRnRStoneType::hasVariants, (type) -> register(rock.name() + "_" + type.name() + "_slab", () -> type.createRockSlab(rock, type))));
        ROCK_STAIRS = Helpers.mapOfKeys(CompatRock.class, (rock) -> Helpers.mapOfKeys(CompatRnRStoneType.class, CompatRnRStoneType::hasVariants, (type) -> register(rock.name() + "_" + type.name() + "_stairs", () -> type.createPathStairs(rock, type))));

        GRAVEL_ROAD = register("gravel_road", () -> new GravelPathBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.GRAVEL).strength(1.5F, 10.0F).requiresCorrectToolForDrops()));
        GRAVEL_ROAD_SLAB = register("gravel_road_slab", () -> new GravelPathSlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.GRAVEL).strength(1.5F, 10.0F).requiresCorrectToolForDrops()));
        GRAVEL_ROAD_STAIRS = register("gravel_road_stairs", () -> new PathStairBlock(Blocks.STONE::defaultBlockState, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.GRAVEL).strength(1.5F, 10.0F).requiresCorrectToolForDrops(), GravelPathBlock.getDefaultSpeedFactor()));

        MACADAM_ROAD = register("macadam_road", () -> new MacadamPathBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.GRAVEL).strength(1.5F, 10.0F).requiresCorrectToolForDrops()));
        MACADAM_ROAD_SLAB = register("macadam_road_slab", () -> new MacadamPathSlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.GRAVEL).strength(1.5F, 10.0F).requiresCorrectToolForDrops()));
        MACADAM_ROAD_STAIRS = register("macadam_road_stairs", () -> new PathStairBlock(Blocks.STONE::defaultBlockState, BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.GRAVEL).strength(1.5F, 10.0F).requiresCorrectToolForDrops(), MacadamPathBlock.getDefaultSpeedFactor()));
    }
}
