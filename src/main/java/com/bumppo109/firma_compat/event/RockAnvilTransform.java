package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.util.ModTags;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "firma_compat") // Replace with your mod ID
public class RockAnvilTransform {
    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

        ItemStack stack = event.getItemStack();
        if (!stack.is(ModTags.Items.HAMMERS)) {
            return;
        }

        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);

        if (state.is(ModTags.Blocks.MAKES_PRIMITIVE_ANVIL)) {
            if (!level.isClientSide()) {
                BlockState newState = ModBlocks.PRIMITIVE_ANVIL.get().defaultBlockState();
                level.setBlock(pos, newState, 3);
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }


    }
}
