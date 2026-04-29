package com.bumppo109.firma_compat.addons.rnr;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.ModRegistryRock;
import com.therighthon.rnr.common.block.*;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public enum CompatRnRStoneType implements StringRepresentable {
    FLAGSTONES((rock, self) -> new StonePathBlock(properties(rock).strength(rock.category().hardness(5.0F), 8.0F)), true),
    SETT_ROAD((rock, self) -> new StonePathBlock(properties(rock).strength(rock.category().hardness(5.0F), 8.0F)), true),
    COBBLED_ROAD((rock, self) -> new StonePathBlock(properties(rock).strength(rock.category().hardness(5.0F), 8.0F)), true);

    public static final CompatRnRStoneType[] VALUES = values();
    private final boolean variants;
    private final BiFunction<ModRegistryRock, CompatRnRStoneType, Block> blockFactory;
    private final String serializedName;

    /*
    public static CompatRnRStoneType valueOf(int i) {
        return i >= 0 && i < VALUES.length ? VALUES[i] : GRAVEL_ROAD;
    }

     */

    private static BlockBehaviour.Properties properties(ModRegistryRock rock) {
        return Properties.of().mapColor(rock.color()).sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM);
    }

    private CompatRnRStoneType(BiFunction<ModRegistryRock, CompatRnRStoneType, Block> blockFactory, boolean variants) {
        this.blockFactory = blockFactory;
        this.variants = variants;
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
    }

    public boolean hasVariants() {
        return this.variants;
    }

    public Block create(ModRegistryRock rock) {
        return (Block)this.blockFactory.apply(rock, this);
    }

    public Block createRockSlab(ModRegistryRock rock, CompatRnRStoneType type) {
        BlockBehaviour.Properties properties = Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5F, 10.0F).requiresCorrectToolForDrops();

        return new StonePathSlabBlock(properties);
    }

    public PathStairBlock createPathStairs(ModRegistryRock rock, CompatRnRStoneType type) {
        Supplier<BlockState> state = () -> ((Block)rock.getBlock(CompatRock.BlockType.HARDENED).get()).defaultBlockState();
        BlockBehaviour.Properties properties = Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5F, 10.0F).requiresCorrectToolForDrops();

        return new PathStairBlock(state, properties, StonePathBlock.getDefaultSpeedFactor());
    }

    public String getSerializedName() {
        return this.serializedName;
    }
}
