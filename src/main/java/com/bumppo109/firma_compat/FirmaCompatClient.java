package com.bumppo109.firma_compat;

import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.dries007.tfc.client.model.entity.HorseChestLayer;
import net.dries007.tfc.util.Helpers;

import java.util.stream.Stream;

import static net.dries007.tfc.common.blocks.wood.Wood.BlockType.*;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FirmaCompatClient
{
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event)
    {
        // Render Types
        final RenderType cutout = RenderType.cutout();

        // Set cutout rendering for your wood blocks
        ModBlocks.WOODS.values().forEach(map -> {
            Stream.of(TWIG, BARREL, SCRIBING_TABLE, JAR_SHELF, ENCASED_AXLE, CLUTCH, GEAR_BOX, SEWING_TABLE)
                    .forEach(type -> {
                        var blockHolder = map.get(type);
                        if (blockHolder != null && blockHolder.isPresent()) {
                            ItemBlockRenderTypes.setRenderLayer(blockHolder.get(), cutout);
                        }
                    });
        });

        // Things that must run on the main thread
        event.enqueueWork(() -> {
            // Barrel sealed property
            ModBlocks.WOODS.values().forEach(map -> {
                var barrel = map.get(BARREL);
                if (barrel != null && barrel.isPresent()) {
                    ItemProperties.register(barrel.get().asItem(),
                            Helpers.identifier("sealed"),
                            (stack, level, entity, unused) -> stack.hasTag() ? 1.0f : 0f);
                }
            });

            // Horse chest compatibility
            HorseChestLayer.registerChest(ModBlocks.COMPAT_CHEST.get().asItem(),
                    Helpers.identifier("textures/entity/chest/horse/compat_chest.png"));

            HorseChestLayer.registerChest(ModBlocks.COMPAT_TRAPPED_CHEST.get().asItem(),
                    Helpers.identifier("textures/entity/chest/horse/compat_chest.png"));

            // Wood-specific barrels for horse chests
            ModBlocks.WOODS.forEach((wood, map) -> {
                var barrel = map.get(BARREL);
                if (barrel != null && barrel.isPresent()) {
                    HorseChestLayer.registerChest(barrel.get().asItem(),
                            Helpers.identifier("textures/entity/chest/horse/" + wood.getSerializedName() + "_barrel.png"));
                }
            });
        });

        // Register custom wood types (for chests, signs, boats, etc.)
        for (CompatWood wood : CompatWood.VALUES)
        {
            Sheets.addWoodType(wood.getVanillaWoodType());
        }
    }
}