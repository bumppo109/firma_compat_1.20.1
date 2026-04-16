package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatOre;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.ModItems;
import com.google.common.collect.ImmutableMap;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.fluids.SimpleFluid;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.CompoundIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface ModAccessors {

    default Ingredient ingredientOf(CompatMetal metal, CompatMetal.ItemType type) {
        return type.isCommonTagPart()
                ? Ingredient.of(commonTagOf(metal, type))
                : Ingredient.of(ModItems.METAL_ITEMS.get(metal).get(type).get());
    }

    /*
    default Ingredient ingredientOf(Metal metal, Metal.ItemType type) {
        return type.isCommonTagPart()
                ? Ingredient.of(commonTagOf(metal, type))
                : Ingredient.of(ModItems.METAL_ITEMS.get(metal).get(type).get());
    }

     */

    default Ingredient ingredientOf(Metal metal, Metal.BlockType type) {
        return Ingredient.of(TFCBlocks.METALS.get(metal).get(type).get());
    }

    default Ingredient ingredientOf(CompatMetal metal, CompatMetal.BlockType type) {
        return Ingredient.of(TFCBlocks.METALS.get(metal).get(type).get());
    }

    default Ingredient ingredientOf(Ingredient... values) {
        return CompoundIngredient.of(values);
    }

    default <T> TagKey<T> logsTagOf(ResourceKey<Registry<T>> registry, CompatWood wood) {
        return TagKey.create(registry, Helpers.identifier(wood.getSerializedName() + "_logs"));
    }

    default <T> TagKey<T> vanillaLogsTag(ResourceKey<Registry<T>> registry, CompatWood wood) {
        return TagKey.create(registry, ResourceLocation.withDefaultNamespace(wood.getSerializedName() + "_logs"));
    }

    default TagKey<Item> commonTagOf(CompatMetal metal, CompatMetal.ItemType type) {
        assert type.isCommonTagPart() : "Non-typical use of tag for " + metal.getSerializedName() + " / " + type.name();
        assert type.has(metal) : "Non-typical use of " + metal.getSerializedName() + " / " + type.name();
        return commonTagOf(Registries.ITEM, type.name() + "s/" + metal.name());
    }

    /*
    default TagKey<Item> commonTagOf(Metal metal, Metal.ItemType type) {
        assert type.isCommonTagPart() : "Non-typical use of tag for " + metal.getSerializedName() + " / " + type.name();
        assert type.has(metal) : "Non-typical use of " + metal.getSerializedName() + " / " + type.name();
        return commonTagOf(Registries.ITEM, type.name() + "s/" + metal.name());
    }

     */

    /*
    default TagKey<Block> oreBlockTagOf(CompatOre ore, @Nullable CompatOre.Grade grade) {
        return commonTagOf(Registries.BLOCK, "ores/" + (ore.isGraded() ? ore.metal().name() : ore.name()) + (grade == null ? "" : "/" + grade.name()));
    }

     */

    //TODO - common forge tags?
    default <T> TagKey<T> commonTagOf(ResourceKey<Registry<T>> key, String name) {
        return TagKey.create(key, ResourceLocation.fromNamespaceAndPath("c", name.toLowerCase(Locale.ROOT)));
    }

    default Item dyeOf(DyeColor color) {
        return itemOf(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_dye"));
    }

    default Item dyedOf(DyeColor color, String suffix) {
        return itemOf(ResourceLocation.withDefaultNamespace(color.getSerializedName() + "_" + suffix));
    }

    default Item itemOf(ResourceLocation name) {
        assert BuiltInRegistries.ITEM.containsKey(name) : "No item '" + name + "'";
        return BuiltInRegistries.ITEM.get(name);
    }

    default Fluid fluidOf(DyeColor color) {
        return TFCFluids.COLORED_FLUIDS.get(color).getSource();
    }

    default Fluid fluidOf(SimpleFluid fluid) {
        return TFCFluids.SIMPLE_FLUIDS.get(fluid).getSource();
    }

    default Fluid fluidOf(CompatMetal metal) {
        return ModFluids.METALS.get(metal).getSource();
    }

    default Fluid fluidOf(Metal metal) {
        return TFCFluids.METALS.get(metal).getSource();
    }

    /**
     * FIXED for Forge 1.20.1
     * Handles CompoundIngredient correctly (getChildren() returns Collection<Ingredient>)
     */
    default String nameOf(Ingredient ingredient) {
        if (ingredient.isEmpty()) {
            throw new AssertionError("Cannot get name of empty ingredient");
        }

        // Handle CompoundIngredient
        if (ingredient instanceof CompoundIngredient compound) {
            Collection<Ingredient> children = compound.getChildren();
            if (!children.isEmpty()) {
                return nameOf(children.iterator().next());   // recurse on first child
            }
        }

        // Fallback for vanilla Ingredient
        ItemStack[] items = ingredient.getItems();
        if (items.length > 0 && !items[0].isEmpty()) {
            return nameOf(items[0].getItem());
        }

        throw new AssertionError("Could not extract name from ingredient: " + ingredient);
    }

    default String nameOf(Fluid fluid) {
        assert fluid != Fluids.EMPTY : "Should never get name of empty fluid";
        return BuiltInRegistries.FLUID.getKey(fluid).getPath();
    }

    default String nameOf(ItemLike item) {
        assert item.asItem() != Items.AIR : "Should never get name of Items.AIR";
        assert item.asItem() != Items.BARRIER : "Should never get name of Items.BARRIER";
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }

    default int units(CompatMetal.ItemType type) {
        return switch (type) {
            default -> 100;
            case DOUBLE_INGOT, SHEET, SWORD_BLADE, UNFINISHED_BOOTS -> 200;
            case DOUBLE_SHEET, UNFINISHED_HELMET, UNFINISHED_CHESTPLATE, UNFINISHED_LEGGINGS -> 400;
        };
    }

    default int units(CompatMetal.BlockType type) {
        return switch (type) {
            default -> 100;
        };
    }

    /*
    default float temperatureOf(Metal metal) {
        return FluidHeat.MANAGER.getOrThrow(Helpers.identifier(metal.getSerializedName())).meltTemperature();
    }

    default float temperatureOf(CompatMetal metal) {
        return FluidHeat.MANAGER.getOrThrow(FirmaCompatHelpers.modIdentifier(metal.getSerializedName())).meltTemperature();
    }

    default int hours(int hours) {
        return hours * ICalendar.CALENDAR_TICKS_IN_HOUR;
    }

     */

    default <T1, T2, V> Map<T1, V> pivot(Map<T1, Map<T2, V>> map, T2 key) {
        final ImmutableMap.Builder<T1, V> builder = new ImmutableMap.Builder<>();
        for (Map.Entry<T1, Map<T2, V>> entry : map.entrySet()) {
            if (entry.getValue().containsKey(key)) {
                builder.put(entry.getKey(), entry.getValue().get(key));
            }
        }
        return builder.build();
    }

    default BlockGetter empty() {
        return new BlockGetter() {
            @Nullable
            @Override
            public BlockEntity getBlockEntity(BlockPos pos) {
                return null;
            }

            @Override
            public BlockState getBlockState(BlockPos pos) {
                return Blocks.AIR.defaultBlockState();
            }

            @Override
            public FluidState getFluidState(BlockPos pos) {
                return Fluids.EMPTY.defaultFluidState();
            }

            @Override
            public int getHeight() {
                return 0;
            }

            @Override
            public int getMinBuildHeight() {
                return 0;
            }
        };
    }
}