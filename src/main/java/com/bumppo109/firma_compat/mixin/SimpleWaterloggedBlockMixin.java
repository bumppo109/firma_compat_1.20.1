package com.bumppo109.firma_compat.mixin;

import com.bumppo109.firma_compat.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;

@Mixin(SimpleWaterloggedBlock.class)
public interface SimpleWaterloggedBlockMixin extends SimpleWaterloggedBlock {

    /**
     * @author bumppo109
     * @reason Allow custom water-tagged fluids to waterlog (overwrite default check)
     */
    @Overwrite
    default boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return fluid.is(ModTags.Fluids.WATERLOGGING_WATER);  // Accepts any tagged fluid (vanilla water + customs)
    }

    /**
     * @author bumppo109
     * @reason Sync placeLiquid to use tag and set waterlogged state
     */
    @Overwrite
    default boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (fluidState.is(ModTags.Fluids.WATERLOGGING_WATER)) {
            if (!level.isClientSide()) {  // Mirror vanilla client check
                level.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, true), 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }
            return true;  // Success: Waterlogs the block
        }
        return false;  // Fail: Non-water fluid (e.g., lava) doesn't waterlog
    }
}