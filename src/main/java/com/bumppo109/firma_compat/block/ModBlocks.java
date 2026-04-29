package com.bumppo109.firma_compat.block;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addons.firmaciv.CompatWatercraftMaterial;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.ModItems;
import com.google.common.base.Suppliers;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.*;
import net.dries007.tfc.common.blocks.devices.DryingBricksBlock;
import net.dries007.tfc.common.blocks.rock.AqueductBlock;
import net.dries007.tfc.common.blocks.rock.RockAnvilBlock;
import net.dries007.tfc.common.blocks.soil.FarmlandBlock;
import net.dries007.tfc.common.blocks.wood.TFCChestBlock;
import net.dries007.tfc.common.blocks.wood.TFCTrappedChestBlock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.common.items.ChestBlockItem;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.bumppo109.firma_compat.block.CompatWood.shouldRegisterBlockType;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, FirmaCompat.MODID);

    //Wood
    public static final Map<CompatWood, Map<Wood.BlockType, RegistryObject<Block>>> WOODS = new HashMap<>();

    static {
        for (CompatWood wood : CompatWood.VALUES) {
            Map<Wood.BlockType, RegistryObject<Block>> woodMap = new HashMap<>();

            for (Wood.BlockType type : Wood.BlockType.values()) {
                // Only register the block types you want
                if (shouldRegisterBlockType(type)) {
                    RegistryObject<Block> block = register(
                            type.nameFor(wood),
                            type.create(wood),
                            type.createBlockItem(wood, new Item.Properties())
                    );
                    woodMap.put(type, block);
                }
            }

            WOODS.put(wood, woodMap);
        }
    }

    /* TODO - hanging signs
    private static <B extends SignBlock> Map<CompatWood, Map<Metal.Default, RegistryObject<B>>> registerHangingSigns(String variant, BiFunction<ExtendedProperties, WoodType, B> factory)
    {
        return Helpers.mapOfKeys(CompatWood.class, wood ->
                Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasUtilities, metal -> register(
                        "wood/planks/" + variant + "/" + metal.getSerializedName() + "/" + wood.getSerializedName(),
                        () -> factory.apply(ExtendedProperties.of(wood.woodColor()).sound(SoundType.WOOD).noCollission().strength(1F).flammableLikePlanks().blockEntity(TFCBlockEntities.HANGING_SIGN).ticks(SignBlockEntity::tick), wood.getVanillaWoodType()),
                        (Function<B, BlockItem>) null)
                )
        );
    }
     */

    public static final RegistryObject<Block> COMPAT_CHEST = register(
            "compat_chest",
            () -> new TFCChestBlock(
                    ExtendedProperties.of()
                            .strength(2.5F)
                            .flammableLikeLogs()
                            .blockEntity(TFCBlockEntities.CHEST)
                            .clientTicks(ChestBlockEntity::lidAnimateTick),
                    "compat_chest"  // texture identifier (folder or key for entity textures)
            ),
            block -> new ChestBlockItem(
                    block,
                    new Item.Properties(),
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "textures/entity/chest_boat/compat.png")  // required third param; dummy/custom path
            )
    );

    public static final RegistryObject<Block> COMPAT_TRAPPED_CHEST = register(
            "compat_trapped_chest",
            () -> new TFCTrappedChestBlock(
                    ExtendedProperties.of()
                            .strength(2.5F)
                            .flammableLikeLogs()
                            .blockEntity(TFCBlockEntities.TRAPPED_CHEST)
                            .clientTicks(ChestBlockEntity::lidAnimateTick),
                    "compat_trapped_chest"  // texture identifier
            ),
            block -> new ChestBlockItem(
                    block,
                    new Item.Properties(),
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "textures/entity/chest_boat/compat_trapped.png")  // or same as above
            )
    );

    // Ores

    public static final Map<CompatRock, Map<CompatOre, RegistryObject<Block>>> ORES = Helpers.mapOfKeys(CompatRock.class, rock ->
            Helpers.mapOfKeys(CompatOre.class, ore -> !ore.isGraded(), ore ->
                    register((rock.name() + "_" + ore.name() + "_ore"), () -> ore.create(rock))
            )
    );
    public static final Map<CompatRock, Map<CompatOre, Map<CompatOre.Grade, RegistryObject<Block>>>> GRADED_ORES = Helpers.mapOfKeys(CompatRock.class, rock ->
            Helpers.mapOfKeys(CompatOre.class, CompatOre::isGraded, ore ->
                    Helpers.mapOfKeys(CompatOre.Grade.class, grade ->
                            register((grade.name() + "_" + rock.name() + "_" + ore.name() + "_ore"), () -> ore.create(rock))
                    )
            )
    );

    // Metals

    public static final Map<CompatMetal, RegistryObject<LiquidBlock>> METAL_FLUIDS = Helpers.mapOfKeys(CompatMetal.class, metal ->
            registerNoItem("fluid/metal/" + metal.name(), () -> new LiquidBlock(ModFluids.METALS.get(metal).source(), BlockBehaviour.Properties.copy(Blocks.LAVA).noLootTable()))
    );

    // ====================== MAIN ROCK BLOCKS ======================
    public static final Map<CompatRock, Map<CompatRock.BlockType, RegistryObject<Block>>> ROCK_BLOCKS =
            Helpers.mapOfKeys(CompatRock.class, rock ->
                    Helpers.mapOfKeys(CompatRock.BlockType.class, type -> {
                        String name = getRockBlockRegistryName(rock, type);

                        return register(name, () -> type.create(rock));
                    })
            );

    // ====================== ROCK DECORATIONS (slab / stairs / wall) ======================
    public static final Map<CompatRock, Map<CompatRock.BlockType, DecorationBlockRegistryObject>> ROCK_DECORATIONS =
            Helpers.mapOfKeys(CompatRock.class, rock ->
                    Helpers.mapOfKeys(CompatRock.BlockType.class, CompatRock.BlockType::hasVariants, type -> {
                        return new DecorationBlockRegistryObject(
                                register(getRockDecorationName(rock, type, "slab"),   () -> type.createSlab(rock)),
                                register(getRockDecorationName(rock, type, "stairs"), () -> type.createStairs(rock)),
                                register(getRockDecorationName(rock, type, "wall"),   () -> type.createWall(rock))
                        );
                    })
            );

    //anvil
    public static final RegistryObject<Block> PRIMITIVE_ANVIL = register("primitive_anvil",
            () -> new RockAnvilBlock(
                    ExtendedProperties.of()
                            .mapColor(MapColor.STONE)
                            .sound(SoundType.STONE)
                            .strength(2.0F, 10.0F)
                            .requiresCorrectToolForDrops()
                            .blockEntity(TFCBlockEntities.ANVIL),
                    () -> Blocks.STONE
            )
    );

    //gravel deposit
    public static final RegistryObject<Block> CASSITERITE_GRAVEL_DEPOSIT = register("cassiterite_gravel_deposit",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
    public static final RegistryObject<Block> NATIVE_COPPER_GRAVEL_DEPOSIT = register("native_copper_gravel_deposit",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
    public static final RegistryObject<Block> NATIVE_GOLD_GRAVEL_DEPOSIT = register("native_gold_gravel_deposit",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));
    public static final RegistryObject<Block> NATIVE_SILVER_GRAVEL_DEPOSIT = register("native_silver_gravel_deposit",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.GRAVEL)));

    //aqueduct
    public static final Map<Aqueducts, RegistryObject<AqueductBlock>> AQUEDUCTS =
            Helpers.mapOfKeys(
                    Aqueducts.class,
                    brick -> register(
                            brick.getSingleName() + "_aqueduct",
                            () -> {
                                // Defer .get() to here – runs after registries are populated
                                Block vanilla = brick.vanillaEquivalent().get();
                                if (vanilla == null) {
                                    // Log and fallback to avoid crash during dev
                                    FirmaCompat.LOGGER.error("Vanilla block for brick {} is null – using default properties", brick);
                                    return new AqueductBlock(BlockBehaviour.Properties.of());
                                }
                                return new AqueductBlock(BlockBehaviour.Properties.copy(vanilla));
                            }
                    )
            );

    //Natural
    public static final RegistryObject<Block> DRYING_MUD_BRICK = register("drying_mud_brick",
            () -> new DryingBricksBlock(ExtendedProperties.of(MapColor.DIRT).noCollission().noOcclusion().instabreak().sound(SoundType.STEM).randomTicks().blockEntity(TFCBlockEntities.TICK_COUNTER), ModItems.MUD_BRICK));
    public static final RegistryObject<Block> COMPAT_FARMLAND = register("compat_farmland",
            () -> new FarmlandBlock(ExtendedProperties.of(MapColor.DIRT).strength(1.3F).sound(SoundType.GRAVEL).isViewBlocking(TFCBlocks::always).isSuffocating(TFCBlocks::always).blockEntity(TFCBlockEntities.FARMLAND), Suppliers.ofInstance(Blocks.DIRT)));
    public static final RegistryObject<Block> CLAY_GRASS_BLOCK = register("clay_grass_block",
            () -> new GrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK)));
    public static final RegistryObject<Block> CLAY_DIRT = register("clay_dirt",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)));
    public static final RegistryObject<Block> CLAY_PODZOL = register("clay_podzol",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.PODZOL)));
    public static final RegistryObject<Block> KAOLIN_CLAY_GRASS_BLOCK = register("kaolin_clay_grass_block",
            () -> new GrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK)));
    public static final RegistryObject<Block> KAOLIN_CLAY_DIRT = register("kaolin_clay_dirt",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)));
    public static final RegistryObject<Block> KAOLIN_CLAY_PODZOL = register("kaolin_clay_podzol",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.PODZOL)));

    // Helper methods
    private static String getRockBlockRegistryName(CompatRock rock, CompatRock.BlockType type) {
        if (rock == CompatRock.BLACKSTONE && type == CompatRock.BlockType.BRICK) {
            return "polished_blackstone_bricks";   // special case you requested
        } else if (type == CompatRock.BlockType.BRICK) {
            return rock.name() + "_bricks";
        }

        return rock.name().toLowerCase(Locale.ROOT) + "_" + type.name().toLowerCase(Locale.ROOT);
    }

    private static String getRockDecorationName(CompatRock rock, CompatRock.BlockType type, String suffix) {
        if (rock == CompatRock.BLACKSTONE && type == CompatRock.BlockType.BRICK) {
            return "polished_blackstone_brick_" + suffix;
        }
        return rock.name().toLowerCase(Locale.ROOT) + "_" + type.name().toLowerCase(Locale.ROOT) + "_" + suffix;
    }

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return RegistrationHelpers.registerBlock(ModBlocks.BLOCKS, ModItems.ITEMS, name, blockSupplier, blockItemFactory);
    }
}
