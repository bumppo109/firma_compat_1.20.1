package com.bumppo109.firma_compat.block;

import net.dries007.tfc.common.blocks.ExtendedBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.Metal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;

public enum CompatOre {
    NATIVE_COPPER(CompatOre.Type.GRADED),
    NATIVE_GOLD(CompatOre.Type.GRADED),
    HEMATITE(CompatOre.Type.GRADED),
    NATIVE_SILVER(CompatOre.Type.GRADED),
    CASSITERITE(CompatOre.Type.GRADED),
    BISMUTHINITE(CompatOre.Type.GRADED),
    GARNIERITE(CompatOre.Type.GRADED),
    MALACHITE(CompatOre.Type.GRADED),
    MAGNETITE(CompatOre.Type.GRADED),
    LIMONITE(CompatOre.Type.GRADED),
    SPHALERITE(CompatOre.Type.GRADED),
    TETRAHEDRITE(CompatOre.Type.GRADED),
    GYPSUM(CompatOre.Type.NORMAL),
    CINNABAR(CompatOre.Type.NORMAL),
    CRYOLITE(CompatOre.Type.NORMAL),
    BORAX(CompatOre.Type.NORMAL),
    GRAPHITE(CompatOre.Type.NORMAL_WITH_POWDER),
    SALTPETER(CompatOre.Type.NORMAL_WITH_POWDER),
    SULFUR(CompatOre.Type.NORMAL_WITH_POWDER),
    SYLVITE(CompatOre.Type.NORMAL_WITH_POWDER),
    AMETHYST(CompatOre.Type.GEM),
    DIAMOND(CompatOre.Type.GEM),
    EMERALD(CompatOre.Type.GEM),
    LAPIS_LAZULI(CompatOre.Type.GEM),
    OPAL(CompatOre.Type.GEM),
    PYRITE(CompatOre.Type.GEM),
    RUBY(CompatOre.Type.GEM),
    SAPPHIRE(CompatOre.Type.GEM),
    TOPAZ(CompatOre.Type.GEM);

    private final CompatOre.Type type;

    private CompatOre(CompatOre.Type type) {
        this.type = type;
    }

    public boolean isGraded() {
        return this.type == CompatOre.Type.GRADED;
    }

    public boolean hasBlock() {
        return this.type != CompatOre.Type.ITEM_ONLY;
    }

    public Block create(ModRegistryRock rock) {
        BlockBehaviour.Properties properties = Properties.of().mapColor(MapColor.STONE).sound(SoundType.STONE).strength(rock.category().hardness(6.5F), 10.0F).requiresCorrectToolForDrops();
        return (Block) new ExtendedBlock(ExtendedProperties.of(properties).flammable(5, 120));
    }

    public static enum Grade {
        POOR,
        NORMAL,
        RICH;

        private static final Grade[] VALUES = values();

        public static Grade valueOf(int i) {
            return i >= 0 && i < VALUES.length ? VALUES[i] : NORMAL;
        }
    }

    static enum Type {
        GRADED,
        NORMAL,
        NORMAL_WITH_POWDER,
        GEM,
        ITEM_ONLY;
    }
}
