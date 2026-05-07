package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.item.FirmaLampItem;
import net.dries007.tfc.common.blocks.devices.LampBlock;
import net.dries007.tfc.util.loot.CopyFluidFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID)
public class LampItemEventHandler {

    //Light Item with Flint & Steel
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();

        if (!player.isCrouching()) return;

        ItemStack used = event.getItemStack();
        InteractionHand hand = event.getHand();

        // Must be flint and steel
        if (!(used.getItem() instanceof FlintAndSteelItem)) return;

        // Check the OTHER hand for lamp
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND
                ? InteractionHand.OFF_HAND
                : InteractionHand.MAIN_HAND;

        ItemStack lampStack = player.getItemInHand(otherHand);

        if (!(lampStack.getItem() instanceof FirmaLampItem)) return;

        // Must have fuel
        if (FirmaLampItem.getFuel(lampStack) <= 0) return;

        // Already lit? do nothing (or toggle if you want)
        if (FirmaLampItem.isLit(lampStack)) return;

        // 🔥 Ignite
        FirmaLampItem.setLit(lampStack, true);

        // Damage flint & steel
        used.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));

        // Sound feedback
        player.level().playSound(
                null,
                player.getX(), player.getY(), player.getZ(),
                SoundEvents.FLINTANDSTEEL_USE,
                SoundSource.PLAYERS,
                1.0F,
                player.level().random.nextFloat() * 0.4F + 0.8F
        );

        event.setCanceled(true);
    }

    //Extinguish with Empty Hand
    @SubscribeEvent
    public static void onRightClickEmpty(PlayerInteractEvent.RightClickEmpty event) {
        Player player = event.getEntity();

        if (!player.isCrouching()) return;

        ItemStack main = player.getMainHandItem();

        if (main.getItem() instanceof FirmaLampItem lamp) {
            if (FirmaLampItem.isLit(main)) {
                FirmaLampItem.setLit(main, false);
            }
        }
    }

    //Pick up lit lamp block
    @Mod.EventBusSubscriber
    public class LampPickupHandler {

        @SubscribeEvent
        public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            Level level = event.getLevel();
            if (level.isClientSide) return;

            Player player = event.getEntity();
            BlockPos pos = event.getPos();
            BlockState state = level.getBlockState(pos);

            // Must be your lamp block
            if (!(state.getBlock() instanceof LampBlock)) return;

            // Must be sneaking with empty hand
            if (!player.isCrouching()) return;

            ItemStack held = player.getItemInHand(event.getHand());
            if (!held.isEmpty()) return;

            // Must be lit
            if (!state.getValue(LampBlock.LIT)) return;

            BlockEntity be = level.getBlockEntity(pos);

            // Create item
            ItemStack stack = new ItemStack(state.getBlock().asItem());

            // Copy fluid (TFC logic)
            CopyFluidFunction.copyToItem(stack, be);

            // 🔥 Copy lit state
            if (stack.getItem() instanceof FirmaLampItem) {
                FirmaLampItem.setLit(stack, true);
            }

            // Remove block
            level.removeBlock(pos, false);

            // Give item
            if (!player.addItem(stack)) {
                player.drop(stack, false);
            }

            event.setCanceled(true);
        }
    }
}
