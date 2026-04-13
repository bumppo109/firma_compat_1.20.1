package com.bumppo109.firma_compat.block;

import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.rock.RockCategory;
import net.dries007.tfc.common.blocks.rock.RockDisplayCategory;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Supplier;

public interface ModRegistryRock extends StringRepresentable {
    RockDisplayCategory displayCategory();

    default RockCategory category() {
        return this.displayCategory().category();
    }

    default MapColor color() {
        return MapColor.STONE;
    }

    Supplier<? extends Block> getBlock(CompatRock.BlockType var1);

    Supplier<? extends SlabBlock> getSlab(CompatRock.BlockType var1);

    Supplier<? extends StairBlock> getStair(CompatRock.BlockType var1);

    Supplier<? extends WallBlock> getWall(CompatRock.BlockType var1);
}
