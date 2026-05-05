package com.bumppo109.firma_compat;

import com.bumppo109.firma_compat.addons.firmalife.CompatFLBlocks;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.dries007.tfc.client.model.entity.HorseChestLayer;
import net.dries007.tfc.util.Helpers;

import java.util.stream.Stream;

import static com.bumppo109.firma_compat.block.ModBlocks.GRADED_ORES;
import static com.bumppo109.firma_compat.block.ModBlocks.ORES;
import static net.dries007.tfc.common.blocks.GroundcoverBlock.TWIG;
import static net.dries007.tfc.common.blocks.wood.Wood.BlockType.*;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class FirmaCompatClient
{
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event)
    {
        //adjusts grass colors to match climate model - too much lag
        /*
        Minecraft.getInstance().getBlockColors().register(
                new ClimateGrassColor(),
                Blocks.GRASS_BLOCK,
                Blocks.GRASS,
                Blocks.TALL_GRASS,
                Blocks.FERN,
                Blocks.LARGE_FERN
        );

         */

        // Render Types
        final RenderType cutout = RenderType.cutout();

        ModBlocks.WOODS.values().forEach(map -> {
            Stream.of(TWIG, BARREL, SCRIBING_TABLE, JAR_SHELF, ENCASED_AXLE, CLUTCH, GEAR_BOX, SEWING_TABLE)
                    .map(map::get)
                    .filter(ro -> ro != null && ro.isPresent())
                    .forEach(ro -> ItemBlockRenderTypes.setRenderLayer(ro.get(), cutout));
        });

        if(ModList.get().isLoaded("firmalife")){
            for(CompatWood wood : CompatWood.VALUES){
                ItemBlockRenderTypes.setRenderLayer(CompatFLBlocks.FOOD_SHELVES.get(wood).get(), cutout);
                ItemBlockRenderTypes.setRenderLayer(CompatFLBlocks.HANGERS.get(wood).get(), cutout);
                ItemBlockRenderTypes.setRenderLayer(CompatFLBlocks.JARBNETS.get(wood).get(), cutout);
                ItemBlockRenderTypes.setRenderLayer(CompatFLBlocks.STOMPING_BARRELS.get(wood).get(), cutout);
                ItemBlockRenderTypes.setRenderLayer(CompatFLBlocks.BARREL_PRESSES.get(wood).get(), cutout);
                ItemBlockRenderTypes.setRenderLayer(CompatFLBlocks.WINE_SHELVES.get(wood).get(), cutout);
            }
            for(CompatRock rock : CompatRock.VALUES){
                ItemBlockRenderTypes.setRenderLayer(CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.POOR).get(), cutout);
                ItemBlockRenderTypes.setRenderLayer(CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.NORMAL).get(), cutout);
                ItemBlockRenderTypes.setRenderLayer(CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.RICH).get(), cutout);
            }
        }

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

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get(), cutout);

        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CLAY_GRASS_BLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CLAY_PODZOL.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get(), cutout);
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.KAOLIN_CLAY_PODZOL.get(), cutout);

        ORES.forEach((rock, oreMap) ->
                oreMap.forEach((ore, blockId) ->
                        ItemBlockRenderTypes.setRenderLayer(blockId.get(), RenderType.cutout())
                )
        );

        // Graded ores (rich/poor/normal variants)
        GRADED_ORES.forEach((rock, oreMap) ->
                oreMap.forEach((ore, gradeMap) ->
                        gradeMap.forEach((grade, blockId) ->
                                ItemBlockRenderTypes.setRenderLayer(blockId.get(), RenderType.cutout())
                        )
                )
        );

        /*
        event.enqueueWork(() -> {
            PlacedItemBlockEntityRenderer.MODELS.putAll(Map.of(
                    ModItems.SWEET_BERRIES_JAR.get(), translucent("block/sweet_berries_jar"),
                    ModItems.SWEET_BERRIES_JAR_UNSEALED.get(), translucent("block/sweet_berries_jar_unsealed"),
                    ModItems.GLOW_BERRIES_JAR.get(), translucent("block/glow_berries_jar"),
                    ModItems.GLOW_BERRIES_JAR_UNSEALED.get(), translucent("block/glow_berries_jar_unsealed")));
        });

         */

    }
}