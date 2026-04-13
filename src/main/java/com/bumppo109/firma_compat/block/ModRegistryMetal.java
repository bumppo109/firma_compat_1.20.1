package com.bumppo109.firma_compat.block;

import net.dries007.tfc.util.Metal;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public interface ModRegistryMetal extends StringRepresentable {
    Tier toolTier();

    ArmorMaterial armorTier();

    Metal.Tier metalTier();

    Supplier<Block> getFullBlock();

    MapColor mapColor();

    Rarity getRarity();
}
