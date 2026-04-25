package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID)
public class NoInteractBlockHandler {

    // Tag key for blocks that should not be interactable
    //private static final ResourceLocation NO_INTERACT_TAG = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "no_interact");

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        InteractionHand hand = event.getHand();

        // Get the clicked block state
        BlockState state = level.getBlockState(pos);

        // Skip if the block is air or doesn't match the tag
        if (state.isAir()) {
            return;
        }

        // Check if the block belongs to the blocked tag
        if (state.is(ModTags.Blocks.PREVENT_INTERACTION)) {
            // Cancel the interaction
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);

            // Optional: give feedback to the player
            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.literal("For villager use only"),
                        true
                );
                // Optional sound effect
                level.playSound(null, pos, SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        } else if(state.is(Blocks.BARREL)){
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);

            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.literal("Try breaking it"),
                        true
                );
                //level.playSound(null, pos, SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }

    }
}