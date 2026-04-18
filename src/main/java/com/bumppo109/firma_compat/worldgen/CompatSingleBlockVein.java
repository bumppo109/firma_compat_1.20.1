package com.bumppo109.firma_compat.worldgen;

import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.minecraft.world.level.block.Block;

public enum CompatSingleBlockVein {
    BITUMINOUS_COAL(
            TFCBlocks.ORES.get(Rock.ANDESITE).get(Ore.BITUMINOUS_COAL).get(),
            null, null, null, null, null,
            true, true,
            VeinType.DISC,
            210, 0.9f,
            -35, -12,
            50, 3,           // size, height (disc thickness)
            null, null, null, null, null, null, null
    ),
    LIGNITE(
            TFCBlocks.ORES.get(Rock.ANDESITE).get(Ore.LIGNITE).get(),
            null, null, null, null, null,
            true, true,
            VeinType.DISC,
            160, 0.85f,
            -20, -8,
            40, 2,
            null, null, null, null, null, null, null
    ),
    HALITE(
            TFCBlocks.ORES.get(Rock.ANDESITE).get(Ore.HALITE).get(),
            null, null, null, null, null,
            true, true,
            VeinType.DISC,
            110, 0.85f,
            -45, -12,
            35, 4,
            null, null, null, null, null, null, null
    )
    ;

    public final Block oreBlock;
    public final Block indicator;
    public final Integer indicatorRarity;
    public final Integer indicatorDepth;
    public final Integer indicatorUnderRarity;
    public final Integer indicatorCount;
    public final Boolean project;
    public final Boolean project_offset;
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

    CompatSingleBlockVein(
            Block oreBlock,
            Block indicator, Integer indicatorRarity, Integer indicatorDepth, Integer indicatorUnderRarity, Integer indicatorCount,
            Boolean project, Boolean project_offset,
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
        this.oreBlock = oreBlock;
        this.indicator = indicator;
        this.indicatorDepth = indicatorDepth;
        this.indicatorRarity = indicatorRarity;
        this.indicatorUnderRarity = indicatorUnderRarity;
        this.indicatorCount = indicatorCount;
        this.project = project;
        this.project_offset = project_offset;
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
    }

    public enum VeinType {
        CLUSTER, DISC, PIPE
    }

}
