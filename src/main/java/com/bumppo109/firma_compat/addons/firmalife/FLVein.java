package com.bumppo109.firma_compat.addons.firmalife;

import com.eerussianguy.firmalife.common.blocks.FLBlocks;
import net.minecraft.world.level.block.Block;

public enum FLVein {
    DEEP_CHROMITE(
            GradedVeinClass.RICH,  // no graded class → it's a special deep cluster
            FLBlocks.SMALL_CHROMITE.get(),
            0,     // indicator rarity
            35,    // indicator depth
            1,     // underground rarity
            4,     // underground count
            VeinType.CLUSTER,
            90,    // rarity
            0.6f,  // density
            -80,   // min Y
            20,    // max Y
            15,    // size
            null,  // no height (cluster)
            null, null, null, null, null, null, null
            // random_name = "deep_chromite" is already implied by the vein name
    ),

    NORMAL_CHROMITE(
            GradedVeinClass.NORMAL,
            FLBlocks.SMALL_CHROMITE.get(),
            14,    // indicator rarity
            35,    // indicator depth
            1,     // underground rarity
            0,     // underground count
            VeinType.CLUSTER,
            50,    // rarity
            0.25f, // density
            40,    // min Y
            130,   // max Y
            8,     // size
            null,  // no height (cluster)
            null, null, null, null, null, null, null
            // random_name = "normal_chromite"
    )
    ;

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

    FLVein(
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
