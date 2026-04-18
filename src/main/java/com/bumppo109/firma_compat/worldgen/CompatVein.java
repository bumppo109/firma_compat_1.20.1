package com.bumppo109.firma_compat.worldgen;

import com.bumppo109.firma_compat.block.CompatOre;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.minecraft.world.level.block.Block;

public enum CompatVein {
    AMETHYST(
            CompatOre.AMETHYST,
            null,
            null, null, null, null, null,
            VeinType.DISC,
            25, 0.2f,
            40, 60,
            8, 4,
            null, null, null, null, null, null, null
    ),

    CINNABAR(
            CompatOre.CINNABAR,
            null,
            null, null, null, null, null,
            VeinType.CLUSTER,
            14, 0.6f,
            -70, 10,
            18, null,
            null, null, null, null, null, null, null
    ),

    DIAMOND(
            CompatOre.DIAMOND,
            null,
            null, null, null, null, null,
            VeinType.PIPE,
            30, 0.15f,
            -64, 100,
            0, 0,
            5, 13,
            0, 2,
            0,
            60,
            5
    ),

    BORAX(
            CompatOre.BORAX,
            null,                           // no graded prefix → null
            null, null, null, null, null,   // no indicator
            VeinType.DISC,
            40, 0.2f,
            40, 100,
            23, 3,                          // size, height
            null, null, null, null, null, null, null
    ),

    CRYOLITE(
            CompatOre.CRYOLITE,
            null,
            null, null, null, null, null,
            VeinType.CLUSTER,
            16, 0.7f,
            -70, -10,
            18, null,
            null, null, null, null, null, null, null
    ),

    EMERALD(
            CompatOre.EMERALD,
            null,
            null, null, null, null, null,
            VeinType.PIPE,
            80, 0.15f,
            -64, 100,
            null, null,                     // no size/height for pipe
            5, 13,                          // min_skew, max_skew
            0, 2,                           // min_slant, max_slant
            0,                              // sign
            60,                             // pipe height
            5                               // radius
    ),

    PYRITE(
            CompatOre.PYRITE,
            null,
            null, null, null, null, null,
            VeinType.CLUSTER,
            16, 0.35f,
            -50, 70,
            15, null,
            null, null, null, null, null, null, null
    ),

    GRAPHITE(
            CompatOre.GRAPHITE,
            null,
            null, null, null, null, null,
            VeinType.CLUSTER,
            20, 0.4f,
            -30, 60,
            20, null,
            null, null, null, null, null, null, null
    ),

    GYPSUM(
            CompatOre.GYPSUM,
            null,
            null, null, null, null, null,
            VeinType.DISC,
            70, 0.3f,
            40, 100,
            25, 5,
            null, null, null, null, null, null, null
    ),

    NORMAL_BISMUTHINITE(
            CompatOre.BISMUTHINITE,
            GradedVeinClass.NORMAL,
            TFCBlocks.SMALL_ORES.get(Ore.BISMUTHINITE).get(), 0, 35, 1, 4,
            VeinType.CLUSTER,
            45,0.6f,-80, 20,40,
            null,null, null, null, null, null, null, null
    ),

    NORMAL_GARNIERITE(
            CompatOre.GARNIERITE,
            GradedVeinClass.NORMAL,
            TFCBlocks.SMALL_ORES.get(Ore.GARNIERITE).get(),
            12,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            25, 0.3f,
            -80, 0,
            18, null,
            null, null, null, null, null, null, null
    ),

    NORMAL_MALACHITE(
            CompatOre.MALACHITE,
            GradedVeinClass.NORMAL,
            TFCBlocks.SMALL_ORES.get(Ore.MALACHITE).get(),
            25,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            45, 0.5f,
            -30, 70,
            30, null,
            null, null, null, null, null, null, null
    ),

    NORMAL_NATIVE_GOLD(
            CompatOre.NATIVE_GOLD,
            GradedVeinClass.NORMAL,
            TFCBlocks.SMALL_ORES.get(Ore.NATIVE_GOLD).get(),
            40,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            90, 0.25f,
            0, 70,
            15, null,
            null, null, null, null, null, null, null
    ),

    NORMAL_NATIVE_SILVER(
            CompatOre.NATIVE_SILVER,
            GradedVeinClass.NORMAL,
            TFCBlocks.SMALL_ORES.get(Ore.NATIVE_SILVER).get(),
            0,      // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            9,      // indicatorCount
            VeinType.CLUSTER,
            25, 0.6f,
            -80, 20,
            25, null,
            null, null, null, null, null, null, null
    ),

    NORMAL_TETRAHEDRITE(
            CompatOre.TETRAHEDRITE,
            GradedVeinClass.NORMAL,
            TFCBlocks.SMALL_ORES.get(Ore.TETRAHEDRITE).get(),
            25,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            40, 0.5f,
            -30, 70,
            30, null,
            null, null, null, null, null, null, null
    ),
    LAPIS_LAZULI(
            CompatOre.LAPIS_LAZULI,
            null,
            null, null, null, null, null,
            VeinType.CLUSTER,
            30, 0.12f,
            -20, 80,
            30, null,
            null, null, null, null, null, null, null
    ),

    OPAL(
            CompatOre.OPAL,
            null,
            null, null, null, null, null,
            VeinType.DISC,
            25, 0.2f,
            40, 60,
            8, 4,
            null, null, null, null, null, null, null
    ),

    RICH_NATIVE_GOLD(
            CompatOre.NATIVE_GOLD,
            GradedVeinClass.NORMAL,
            TFCBlocks.SMALL_ORES.get(Ore.NATIVE_GOLD).get(),
            0,      // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            4,      // indicatorCount
            VeinType.CLUSTER,
            50, 0.5f,
            -80, 20,
            40, null,
            null, null, null, null, null, null, null
    ),

    RUBY(
            CompatOre.RUBY,
            null,
            null, null, null, null, null,
            VeinType.CLUSTER,
            12, 0.2f,
            -70, -10,
            22, null,
            null, null, null, null, null, null, null
    ),

    SALTPETER(
            CompatOre.SALTPETER,
            null,
            null, null, null, null, null,
            VeinType.DISC,
            110, 0.4f,
            40, 100,
            35, 5,
            null, null, null, null, null, null, null
    ),

    SULFUR(
            CompatOre.SULFUR,
            null,
            null, null, null, null, null,
            VeinType.DISC,
            4, 0.25f,
            -64, -45,
            18, 5,
            null, null, null, null, null, null, null
    ),

    SYLVITE(
            CompatOre.SYLVITE,
            null,
            null, null, null, null, null,
            VeinType.DISC,
            60, 0.35f,
            40, 100,
            35, 5,
            null, null, null, null, null, null, null
    ),

    SURFACE_TETRAHEDRITE(
            CompatOre.TETRAHEDRITE,
            GradedVeinClass.SURFACE,
            TFCBlocks.SMALL_ORES.get(Ore.TETRAHEDRITE).get(),
            8,      // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            30, 0.12f,
            -20, 80,
            20, null,
            null, null, null, null, null, null, null
    ),

    SURFACE_SPHALERITE(
            CompatOre.SPHALERITE,
            GradedVeinClass.SURFACE,
            TFCBlocks.SMALL_ORES.get(Ore.SPHALERITE).get(),
            12,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            30, 0.3f,
            40, 130,
            20, null,
            null, null, null, null, null, null, null
    ),

    SURFACE_NATIVE_SILVER(
            CompatOre.NATIVE_SILVER,
            GradedVeinClass.SURFACE,
            TFCBlocks.SMALL_ORES.get(Ore.NATIVE_SILVER).get(),
            12,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            15, 0.2f,
            90, 180,
            10, null,
            null, null, null, null, null, null, null
    ),

    SURFACE_NATIVE_COPPER(
            CompatOre.NATIVE_COPPER,
            GradedVeinClass.SURFACE,
            TFCBlocks.SMALL_ORES.get(Ore.NATIVE_COPPER).get(),
            14,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            24, 0.25f,
            40, 130,
            20, null,
            null, null, null, null, null, null, null
    ),

    SURFACE_MALACHITE(
            CompatOre.MALACHITE,
            GradedVeinClass.SURFACE,
            TFCBlocks.SMALL_ORES.get(Ore.MALACHITE).get(),
            14,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            32, 0.3f,
            40, 130,
            20, null,
            null, null, null, null, null, null, null
    ),

    SURFACE_MAGNETITE(
            CompatOre.MAGNETITE,
            GradedVeinClass.SURFACE,
            TFCBlocks.SMALL_ORES.get(Ore.MAGNETITE).get(),
            24,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            90, 0.4f,
            10, 90,
            20, null,
            null, null, null, null, null, null, null
    ),

    SURFACE_LIMONITE(
            CompatOre.LIMONITE,
            GradedVeinClass.SURFACE,
            TFCBlocks.SMALL_ORES.get(Ore.LIMONITE).get(),
            24,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            90, 0.4f,
            10, 90,
            20, null,
            null, null, null, null, null, null, null
    ),

    SURFACE_BISMUTHINITE(
            CompatOre.BISMUTHINITE,
            GradedVeinClass.SURFACE,
            TFCBlocks.SMALL_ORES.get(Ore.BISMUTHINITE).get(),
            14,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            32, 0.3f,
            40, 130,
            20, null,
            null, null, null, null, null, null, null
    ),

    SURFACE_CASSITERITE(
            CompatOre.CASSITERITE,
            GradedVeinClass.SURFACE,
            TFCBlocks.SMALL_ORES.get(Ore.CASSITERITE).get(),
            12,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            5, 0.4f,
            80, 180,
            15, null,
            null, null, null, null, null, null, null
    ),

    SURFACE_HEMATITE(
            CompatOre.HEMATITE,
            GradedVeinClass.SURFACE,
            TFCBlocks.SMALL_ORES.get(Ore.HEMATITE).get(),
            24,     // indicatorRarity
            35,     // indicatorDepth
            1,      // indicatorUnderRarity
            0,      // indicatorCount
            VeinType.CLUSTER,
            45, 0.4f,
            10, 90,
            20, null,
            null, null, null, null, null, null, null
    )
    ;

    public final CompatOre ore;
    public final GradedVeinClass gradedVeinClass;
    public final Block indicator;
    public final Integer indicatorRarity;
    public final Integer indicatorDepth;
    public final Integer indicatorUnderRarity;
    public final Integer indicatorCount;
    public final VeinType veinType;
    public final int rarity;
    public final float density;
    public final int minY, maxY;
    public final Integer size;
    public final Integer height;
    public final Integer minSkew, maxSkew;
    public final Integer minSlant, maxSlant;
    public final Integer sign;
    public final Integer pipeHeight;
    public final Integer radius;

    CompatVein(
            CompatOre ore,
            GradedVeinClass gradedVeinClass,
            Block indicator, Integer indicatorRarity, Integer indicatorDepth, Integer indicatorUnderRarity, Integer indicatorCount,
            VeinType veinType,
            int rarity, float density, int minY, int maxY,
            Integer size, Integer height,
            Integer minSkew, Integer maxSkew,
            Integer minSlant, Integer maxSlant,
            Integer sign,
            Integer pipeHeight,
            Integer radius
            //String randomName
    ) {
        this.ore = ore;
        this.gradedVeinClass = gradedVeinClass;
        this.indicator = indicator;
        this.indicatorDepth = indicatorDepth;
        this.indicatorRarity = indicatorRarity;
        this.indicatorUnderRarity = indicatorUnderRarity;
        this.indicatorCount = indicatorCount;
        this.veinType = veinType;
        this.rarity = rarity;
        this.density = density;
        this.minY = minY;
        this.maxY = maxY;
        this.size = size;
        this.height = height;
        this.minSkew = minSkew;
        this.maxSkew = maxSkew;
        this.minSlant = minSlant;
        this.maxSlant = maxSlant;
        this.sign = sign;
        this.pipeHeight = pipeHeight;
        this.radius = radius;
        //this.randomName = randomName;
    }

    public enum VeinType {
        CLUSTER, DISC, PIPE
    }

    public enum GradedVeinClass {
        SURFACE, NORMAL, RICH
    }

}

