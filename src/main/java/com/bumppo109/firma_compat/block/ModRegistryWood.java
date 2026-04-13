package com.bumppo109.firma_compat.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public interface ModRegistryWood extends StringRepresentable {
    MapColor woodColor();

    MapColor barkColor();

    Supplier<Integer> ticksToGrow();

    int autumnIndex();

    Supplier<Block> getBlock(CompatWood.BlockType var1);

    BlockSetType getBlockSet();

    WoodType getVanillaWoodType();
}
