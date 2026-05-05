package com.bumppo109.firma_compat.addons.legendarysurvivaloverhaul;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID)
public class PromptLSOConfigReminder {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if(FirmaCompat.isLSOLoaded && FirmaCompat.isEclipticLoaded){
            if (!(event.getEntity() instanceof ServerPlayer player)) return;

            var data = player.getPersistentData();
            var tag = data.getCompound("firma_compat");

            if (!tag.getBoolean("seen_world_message")) {

                player.sendSystemMessage(Component.literal(
                        "Disable the Ecliptic Integration Config for LSO"
                ));

                tag.putBoolean("seen_world_message", true);
                data.put("firma_compat", tag);
            }
        }
    }
}
