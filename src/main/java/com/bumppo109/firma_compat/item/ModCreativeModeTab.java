package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addons.firmalife.CompatFLBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRItems;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRStoneType;
import com.bumppo109.firma_compat.block.*;
import com.bumppo109.firma_compat.util.ModTags;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

import static net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE;

public class ModCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FirmaCompat.MODID);

    public static final RegistryObject<CreativeModeTab> FIRMA_COMPAT_TAB = CREATIVE_MODE_TABS.register("firma_compat_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(Items.OAK_PLANKS)) // ← Change this to one of your items/blocks later
                    .title(Component.translatable("creative_tab.firma_compat_tab"))
                    .displayItems((parameters, output) -> {   // ← parameters + output (1.20.1 style)

                        accept(output, ModBlocks.COMPAT_CHEST);
                        accept(output, ModBlocks.COMPAT_TRAPPED_CHEST);

                        accept(output, ModBlocks.CLAY_DIRT);
                        accept(output, ModBlocks.CLAY_PODZOL);
                        accept(output, ModBlocks.CLAY_GRASS_BLOCK);
                        accept(output, ModBlocks.KAOLIN_CLAY_DIRT);
                        accept(output, ModBlocks.KAOLIN_CLAY_PODZOL);
                        accept(output, ModBlocks.KAOLIN_CLAY_GRASS_BLOCK);
                        accept(output, ModBlocks.DRYING_MUD_BRICK);
                        accept(output, ModBlocks.COMPAT_FARMLAND);

                        accept(output, ModBlocks.CASSITERITE_GRAVEL_DEPOSIT);
                        accept(output, ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT);
                        accept(output, ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT);
                        accept(output, ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT);

                        for (CompatWood wood : CompatWood.VALUES) {
                            var woodBlocks = ModBlocks.WOODS.get(wood);
                            if (woodBlocks != null) {
                                woodBlocks.forEach((type, reg) -> {
                                    if (type.needsItem() && reg != null) {
                                        accept(output, reg);
                                    }
                                });
                            }
                            // Add lumber and supports
                            accept(output, ModItems.LUMBER, wood);
                            accept(output, ModItems.SUPPORTS, wood);
                        }

                        for (CompatRock rock : CompatRock.VALUES) {
                            var rockBlocks = ModBlocks.ROCK_BLOCKS.get(rock);
                            if(rockBlocks != null) {
                                rockBlocks.forEach((type, reg) -> {
                                    if(type == CompatRock.BlockType.BRICK){
                                        if(rock != CompatRock.STONE && rock != CompatRock.DEEPSLATE && rock != CompatRock.BLACKSTONE && rock != CompatRock.END_STONE && rock != CompatRock.NETHERRACK) {
                                            accept(output, reg);
                                            accept(output, rock.getSlab(CompatRock.BlockType.BRICK));
                                            accept(output, rock.getStair(CompatRock.BlockType.BRICK));
                                            accept(output, rock.getWall(CompatRock.BlockType.BRICK));
                                        }
                                    }
                                    if(type == CompatRock.BlockType.HARDENED_COBBLE){
                                        if(rock != CompatRock.STONE && rock != CompatRock.DEEPSLATE) {
                                            accept(output, reg);
                                        }
                                    }
                                    accept(output, reg);
                                });
                            }
                        }
                        for (Aqueducts brick : Aqueducts.VALUES) {
                            accept(output, ModBlocks.AQUEDUCTS.get(brick));
                        }
                        accept(output, ModItems.PRISMARINE_BRICK);
                        accept(output, ModItems.QUARTZ_BRICK);
                        accept(output, ModItems.PRISMARINE_BRICK);

                        accept(output, ModBlocks.PRIMITIVE_ANVIL);

                        accept(output, ModItems.MUD_BRICK);
                        accept(output, ModItems.UNFIRED_POT);

                        for (CompatMetal metal : CompatMetal.values()) {
                            var metalItems = ModItems.METAL_ITEMS.get(metal);
                            if(metalItems != null && !metal.equals(CompatMetal.IRON)) {
                                metalItems.forEach((type, reg) -> {
                                    accept(output, reg);
                                });
                            }
                        }

                        //Ore
                        ModBlocks.ORES.forEach((rock, oreMap) -> {
                            oreMap.forEach((ore, blockSupplier) -> {
                                accept(output, blockSupplier);
                            });
                        });
                        ModBlocks.GRADED_ORES.forEach((rock, oreMap) -> {
                            oreMap.forEach((ore, gradeMap) -> {
                                gradeMap.forEach((grade, blockSupplier) -> {
                                    accept(output, blockSupplier);
                                });
                            });
                        });

                        //Firmalife
                        for (CompatWood wood : CompatWood.VALUES){
                            accept(output, CompatFLBlocks.FOOD_SHELVES.get(wood));
                            accept(output, CompatFLBlocks.WINE_SHELVES.get(wood));
                            accept(output, CompatFLBlocks.JARBNETS.get(wood));
                            accept(output, CompatFLBlocks.HANGERS.get(wood));
                            accept(output, CompatFLBlocks.BIG_BARRELS.get(wood));
                            accept(output, CompatFLBlocks.STOMPING_BARRELS.get(wood));
                            accept(output, CompatFLBlocks.BARREL_PRESSES.get(wood));
                        }
                        for (CompatRock rock : CompatRock.VALUES){
                            accept(output, CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.POOR));
                            accept(output, CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.NORMAL));
                            accept(output, CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.RICH));
                        }

                        //RnR
                        accept(output, CompatRnRItems.GRAVEL_FILL);
                        accept(output, CompatRnRBlocks.TAMPED_DIRT);
                        accept(output, CompatRnRBlocks.TAMPED_MUD);
                        accept(output, CompatRnRBlocks.OVER_HEIGHT_GRAVEL);
                        accept(output, CompatRnRBlocks.GRAVEL_ROAD);
                        accept(output, CompatRnRBlocks.GRAVEL_ROAD_STAIRS);
                        accept(output, CompatRnRBlocks.GRAVEL_ROAD_SLAB);
                        accept(output, CompatRnRBlocks.MACADAM_ROAD);
                        accept(output, CompatRnRBlocks.MACADAM_ROAD_STAIRS);
                        accept(output, CompatRnRBlocks.MACADAM_ROAD_SLAB);

                        CompatRnRBlocks.ROCK_BLOCKS.forEach((rock, typeMap) -> {
                            accept(output, CompatRnRItems.FLAGSTONE.get(rock));
                            typeMap.forEach((road, blockSupplier) -> {
                                accept(output, blockSupplier);
                            });
                        });
                        CompatRnRBlocks.ROCK_STAIRS.forEach((rock, typeMap) -> {
                            typeMap.forEach((road, blockSupplier) -> {
                                accept(output, blockSupplier);
                            });
                        });
                        CompatRnRBlocks.ROCK_SLABS.forEach((rock, typeMap) -> {
                            typeMap.forEach((road, blockSupplier) -> {
                                accept(output, blockSupplier);
                            });
                        });

                        for (CompatWood wood : CompatWood.VALUES){
                            accept(output, CompatRnRItems.WOOD_SHINGLE.get(wood));
                            accept(output, CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood));
                            accept(output, CompatRnRBlocks.WOOD_SHINGLE_ROOF_STAIRS.get(wood));
                            accept(output, CompatRnRBlocks.WOOD_SHINGLE_ROOF_SLABS.get(wood));
                        }

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    // Helper for a single Supplier
    private static void accept(CreativeModeTab.Output output, Supplier<? extends ItemLike> supplier) {
        if (supplier == null) {
            FirmaCompat.LOGGER.warn("Supplier was null when adding to creative tab");
            return;
        }

        ItemLike itemLike = supplier.get();
        if (itemLike == null || itemLike.asItem() == Items.AIR) {
            FirmaCompat.LOGGER.warn("Skipping invalid or AIR item in FirmaCompat creative tab");
            return;
        }

        output.accept(itemLike);
    }

    // Helper for Map<CompatWood, Supplier<ItemLike>>
    private static void accept(CreativeModeTab.Output output,
                               Map<CompatWood, ? extends Supplier<? extends ItemLike>> map,
                               CompatWood wood) {
        if (map == null) {
            FirmaCompat.LOGGER.warn("Map was null for wood: {}", wood);
            return;
        }

        Supplier<? extends ItemLike> supplier = map.get(wood);
        if (supplier != null) {
            accept(output, supplier);
        } else {
            FirmaCompat.LOGGER.warn("No supplier found for wood: {}", wood);
        }
    }
}