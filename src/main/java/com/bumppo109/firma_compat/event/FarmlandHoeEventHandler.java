package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID)
public class FarmlandHoeEventHandler {

    @SubscribeEvent
    public static void onToolModification(BlockEvent.BlockToolModificationEvent event) {
        if (event.getToolAction() != ToolActions.HOE_TILL || event.isSimulated()) {
            return;
        }

        if (!event.getState().is(Blocks.DIRT) &&
                !event.getState().is(Blocks.GRASS_BLOCK) &&
                !event.getState().is(Blocks.DIRT_PATH)) {
            return;
        }
        BlockState replacement = ModBlocks.COMPAT_FARMLAND.get().defaultBlockState();
        event.setFinalState(replacement);
    }
}
