package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.util.ModTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulFireInteractionHandler
{
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRightClickSoulFire(PlayerInteractEvent.RightClickBlock event)
    {
        // Server-side only
        if (event.getLevel().isClientSide()) return;

        // Main hand only
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        // Must click soul fire
        BlockState clickedState = event.getLevel().getBlockState(event.getPos());
        if (clickedState.getBlock() != Blocks.SOUL_FIRE) return;

        ItemStack heldStack = event.getItemStack();
        if (heldStack.isEmpty()) return;

        ItemStack resultStack = ItemStack.EMPTY;

        // Lantern conversions
        if (heldStack.is(ModTags.Items.MAKES_SOUL_LANTERN))
        {
            resultStack = new ItemStack(Items.SOUL_LANTERN, heldStack.getCount());
        }
        // Torch conversions
        else if (heldStack.is(ModTags.Items.MAKES_SOUL_TORCH))
        {
            resultStack = new ItemStack(Items.SOUL_TORCH, heldStack.getCount());
        }

        // No valid conversion
        if (resultStack.isEmpty()) return;

        // Copy NBT if present
        if (heldStack.hasTag())
        {
            resultStack.setTag(heldStack.getTag().copy());
        }

        // Replace held item
        event.getEntity().setItemInHand(event.getHand(), resultStack);

        // Sound feedback
        event.getLevel().playSound(
                null,
                event.getPos(),
                SoundEvents.SOUL_ESCAPE,
                SoundSource.BLOCKS,
                0.8F,
                1.0F
        );

        // Prevent default interaction
        event.setCanceled(true);
    }
}