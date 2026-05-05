package com.bumppo109.firma_compat.util.climate.render;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;

public class ClimateGrassColor implements BlockColor {

    @Override
    public int getColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {

        if (level == null || pos == null) return -1;

        //ChunkPos chunk = new ChunkPos(pos);
        int cx = pos.getX() >> 4;
        int cz = pos.getZ() >> 4;
        ChunkPos chunk = new ChunkPos(cx, cz);

        ClimateRenderData data = ClientClimateRenderCache.get(chunk);

        if (data == null) return -1;

        return ClimateColorUtil.computeGrass(data.temp, data.veg);
    }
}

