package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID)
public class NoPlaceableItemHandler {

    // Tag key for items that should not be placeable
    private static final ResourceLocation NO_PLACEABLE_TAG = ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "no_placeable");

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = event.getLevel();
        ItemStack stack = event.getItemStack();
        BlockPos pos = event.getPos();
        InteractionHand hand = event.getHand();

        // Skip if item is empty or not in the tag
        if (stack.isEmpty()) {
            return;
        }

        // Check if the item belongs to the blocked tag
        if (stack.is(ModTags.Items.PREVENT_INTERACTION)) {
            // Cancel placement and return failure (no block placed)
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);

            // Optional: give feedback to the player
            /*
            if (!level.isClientSide) {
                player.displayClientMessage(
                        Component.literal("You cannot place this item."),
                        true
                );


                // Optional sound effect
                level.playSound(null, pos, SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
             */
        }
    }
}
