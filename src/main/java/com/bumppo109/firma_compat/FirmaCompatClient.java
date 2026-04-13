package com.bumppo109.firma_compat;

import java.util.function.Predicate;
import java.util.stream.Stream;

import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.dries007.tfc.client.model.entity.HorseChestLayer;
import net.dries007.tfc.util.Helpers;

import static net.dries007.tfc.common.blocks.wood.Wood.BlockType.*;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FirmaCompatClient
{
    public static void clientSetup(FMLClientSetupEvent event)
    {
        // Render Types
        final RenderType solid = RenderType.solid();
        final RenderType cutout = RenderType.cutout();
        final RenderType cutoutMipped = RenderType.cutoutMipped();
        final RenderType translucent = RenderType.translucent();
        final Predicate<RenderType> ghostBlock = rt -> rt == cutoutMipped || rt == Sheets.translucentCullBlockSheet();

        final Predicate<RenderType> leafPredicate = layer -> Minecraft.useFancyGraphics() ? layer == cutoutMipped : layer == solid;
        ModBlocks.WOODS.values().forEach(map -> {
            Stream.of(TWIG, BARREL, SCRIBING_TABLE, JAR_SHELF, ENCASED_AXLE, CLUTCH, GEAR_BOX, SEWING_TABLE).forEach(type -> ItemBlockRenderTypes.setRenderLayer(map.get(type).get(), cutout));
        });

        event.enqueueWork(() -> {
            ModBlocks.WOODS.values().forEach(map -> ItemProperties.register(map.get(BARREL).get().asItem(), Helpers.identifier("sealed"), (stack, level, entity, unused) -> stack.hasTag() ? 1.0f : 0f));

            HorseChestLayer.registerChest(ModBlocks.COMPAT_CHEST.get().asItem(), Helpers.identifier("textures/entity/chest/horse/compat_chest.png"));
            HorseChestLayer.registerChest(ModBlocks.COMPAT_TRAPPED_CHEST.get().asItem(), Helpers.identifier("textures/entity/chest/horse/compat_chest.png"));

            ModBlocks.WOODS.forEach((wood, map) -> {
                HorseChestLayer.registerChest(map.get(BARREL).get().asItem(), Helpers.identifier("textures/entity/chest/horse/" + wood.getSerializedName() + "_barrel.png"));
            });
        });

        for (CompatWood wood : CompatWood.VALUES)
        {
            Sheets.addWoodType(wood.getVanillaWoodType());
        }
    }
}