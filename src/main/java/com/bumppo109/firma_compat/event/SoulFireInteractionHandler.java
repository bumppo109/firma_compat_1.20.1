package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.Metal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SoulFireInteractionHandler
{
    // ====================== ADD YOUR CONVERSIONS HERE ======================
    private static final Map<Item, Item> SOUL_FIRE_CONVERSIONS = new HashMap<>();

    static {
        // Format: SOUL_FIRE_CONVERSIONS.put(InputItem, OutputItem);
        //SOUL_FIRE_CONVERSIONS.put(TFCItems.TORCH.get()., Items.SOUL_TORCH);
        SOUL_FIRE_CONVERSIONS.put(ModBlocks.LANTERN.get().asItem(), Items.SOUL_LANTERN);
        SOUL_FIRE_CONVERSIONS.put(ModBlocks.COMPAT_LANTERNS.get(Metal.Default.COPPER).get().asItem(), Items.SOUL_LANTERN);
        SOUL_FIRE_CONVERSIONS.put(ModBlocks.COMPAT_LANTERNS.get(Metal.Default.BISMUTH_BRONZE).get().asItem(), Items.SOUL_LANTERN);
        SOUL_FIRE_CONVERSIONS.put(ModBlocks.COMPAT_LANTERNS.get(Metal.Default.BLACK_BRONZE).get().asItem(), Items.SOUL_LANTERN);
        SOUL_FIRE_CONVERSIONS.put(ModBlocks.COMPAT_LANTERNS.get(Metal.Default.BRONZE).get().asItem(), Items.SOUL_LANTERN);
        SOUL_FIRE_CONVERSIONS.put(ModBlocks.COMPAT_LANTERNS.get(Metal.Default.WROUGHT_IRON).get().asItem(), Items.SOUL_LANTERN);
        SOUL_FIRE_CONVERSIONS.put(ModBlocks.COMPAT_LANTERNS.get(Metal.Default.STEEL).get().asItem(), Items.SOUL_LANTERN);
        SOUL_FIRE_CONVERSIONS.put(ModBlocks.COMPAT_LANTERNS.get(Metal.Default.BLACK_STEEL).get().asItem(), Items.SOUL_LANTERN);
        SOUL_FIRE_CONVERSIONS.put(ModBlocks.COMPAT_LANTERNS.get(Metal.Default.BLUE_STEEL).get().asItem(), Items.SOUL_LANTERN);
        SOUL_FIRE_CONVERSIONS.put(ModBlocks.COMPAT_LANTERNS.get(Metal.Default.RED_STEEL).get().asItem(), Items.SOUL_LANTERN);
        // Add as many as you want below:
        // SOUL_FIRE_CONVERSIONS.put(Items.XXX, Items.YYY);
    }
    // =====================================================================

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onRightClickSoulFire(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getLevel().isClientSide()) return; // Server-side only

        ItemStack heldStack = event.getItemStack();
        if (heldStack.isEmpty()) return;

        Item inputItem = heldStack.getItem();

        // Check if this item has a conversion
        if (!SOUL_FIRE_CONVERSIONS.containsKey(inputItem)) return;

        // Check if clicked block is Soul Fire
        BlockState clickedState = event.getLevel().getBlockState(event.getPos());
        if (clickedState.getBlock() != Blocks.SOUL_FIRE) return;

        // Optional: Only allow main hand
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        Item outputItem = SOUL_FIRE_CONVERSIONS.get(inputItem);

        // Create new stack with same count
        ItemStack resultStack = new ItemStack(outputItem, heldStack.getCount());

        // Optional: Copy NBT (enchantments, name, etc.)
        if (heldStack.hasTag()) {
            resultStack.setTag(heldStack.getTag().copy());
        }

        // Replace item in hand
        event.getEntity().setItemInHand(event.getHand(), resultStack);

        // Visual & Audio feedback
        event.getLevel().playSound(null, event.getPos(),
                SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 0.8F, 1.0F);

        // Prevent default block interaction
        event.setCanceled(true);
    }
}