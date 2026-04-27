package com.bumppo109.firma_compat.addons.firmalife;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import com.eerussianguy.firmalife.common.blockentities.BarrelPressBlockEntity;
import com.eerussianguy.firmalife.common.blockentities.FLBlockEntities;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.eerussianguy.firmalife.common.blocks.*;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public class CompatFLBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, FirmaCompat.MODID);

    public static final Map<CompatWood, RegistryObject<Block>> FOOD_SHELVES = Helpers.mapOfKeys(CompatWood.class, (wood) ->
            register(wood.getSerializedName() + "_food_shelf", () -> new FoodShelfBlock(shelfProperties().mapColor(wood.woodColor()))));

    public static final Map<CompatWood, RegistryObject<Block>> HANGERS = Helpers.mapOfKeys(CompatWood.class, (wood) ->
            register(wood.getSerializedName() + "_hanger", () -> new HangerBlock(hangerProperties().mapColor(wood.woodColor()))));

    public static final Map<CompatWood, RegistryObject<Block>> JARBNETS = Helpers.mapOfKeys(CompatWood.class, (wood) ->
            register(wood.getSerializedName() + "_jarbnet", () -> new JarbnetBlock(jarbnetProperties().mapColor(wood.woodColor()))));

    public static final Map<CompatWood, RegistryObject<Block>> BIG_BARRELS = Helpers.mapOfKeys(CompatWood.class, (wood) ->
            register(wood.getSerializedName() + "_big_barrel", () -> new BigBarrelBlock(ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD).noOcclusion().strength(10.0F).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.BIG_BARREL))));

    public static final Map<CompatWood, RegistryObject<Block>> WINE_SHELVES = Helpers.mapOfKeys(CompatWood.class, (wood) ->
            register(wood.getSerializedName() + "_wine_shelf", () -> new WineShelfBlock(ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD).noOcclusion().strength(4.0F).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.WINE_SHELF))));

    public static final Map<CompatWood, RegistryObject<Block>> STOMPING_BARRELS = Helpers.mapOfKeys(CompatWood.class, (wood) ->
            register(wood.getSerializedName() + "_stomping_barrel", () -> new StompingBarrelBlock(ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD).noOcclusion().strength(4.0F).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.STOMPING_BARREL))));

    public static final Map<CompatWood, RegistryObject<Block>> BARREL_PRESSES = Helpers.mapOfKeys(CompatWood.class, (wood) ->
            register(wood.getSerializedName() + "_barrel_press", () -> new BarrelPressBlock(ExtendedProperties.of().mapColor(wood.woodColor()).sound(SoundType.WOOD).noOcclusion().strength(4.0F).pushReaction(PushReaction.BLOCK).flammableLikeLogs().blockEntity(FLBlockEntities.BARREL_PRESS).ticks(BarrelPressBlockEntity::tick))));


    public static final Map<CompatRock, Map<Ore.Grade, RegistryObject<Block>>> CHROMITE_ORES = Helpers.mapOfKeys(CompatRock.class, rock ->
            Helpers.mapOfKeys(Ore.Grade.class, grade ->
                    register((grade.name() + "_" + rock.name() + "_chromite_ore"), () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.STONE).strength(3, 10).requiresCorrectToolForDrops()))
            )
    );

    public static ExtendedProperties shelfProperties() {
        return ExtendedProperties.of().strength(0.3F).sound(SoundType.WOOD).noOcclusion().blockEntity(FLBlockEntities.FOOD_SHELF);
    }

    public static ExtendedProperties hangerProperties() {
        return ExtendedProperties.of().strength(0.3F).sound(SoundType.WOOD).noOcclusion().blockEntity(FLBlockEntities.HANGER);
    }

    public static ExtendedProperties jarbnetProperties() {
        return ExtendedProperties.of().strength(0.3F).sound(SoundType.WOOD).noOcclusion().randomTicks().lightLevel((s) -> (Boolean)s.getValue(JarbnetBlock.LIT) ? 11 : 0).blockEntity(FLBlockEntities.JARBNET);
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
