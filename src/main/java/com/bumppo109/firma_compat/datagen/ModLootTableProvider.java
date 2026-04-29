package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addons.firmalife.CompatFLBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRStoneType;
import com.bumppo109.firma_compat.block.Aqueducts;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.util.ModTags;
import com.eerussianguy.firmalife.common.blocks.BigBarrelBlock;
import com.eerussianguy.firmalife.common.items.FLItems;
import net.dries007.tfc.common.blocks.devices.BarrelBlock;
import net.dries007.tfc.common.blocks.devices.DryingBricksBlock;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.util.loot.IsIsolatedCondition;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fml.ModList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE;
import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.predicates.ExplosionCondition.survivesExplosion;

public class ModLootTableProvider extends LootTableProvider {

    public ModLootTableProvider(PackOutput output) {
        super(output,
                Set.of(),  // required tables (usually empty)
                List.of(new LootTableProvider.SubProviderEntry(
                        ModBlockLoot::new,
                        LootContextParamSets.BLOCK
                ))
        );
    }

    private static class ModBlockLoot extends BlockLootSubProvider {

        public ModBlockLoot() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            generateCompatLootTables();
        }

        protected void generateCompatLootTables() {
            dropSelf(ModBlocks.COMPAT_CHEST.get());
            dropSelf(ModBlocks.COMPAT_TRAPPED_CHEST.get());
            dropOther(ModBlocks.COMPAT_FARMLAND.get(), Blocks.DIRT);

            // === Wood blocks ===
            for (CompatWood wood : CompatWood.VALUES) {
                var woodMap = ModBlocks.WOODS.get(wood);
                if (woodMap == null) continue;

                for (CompatWood.BlockType type : CompatWood.BlockType.values()) {
                    if (!woodMap.containsKey(type)) continue;

                    Block block = woodMap.get(type).get();

                    if (type == CompatWood.BlockType.WINDMILL) {
                        Block axle = woodMap.containsKey(CompatWood.BlockType.AXLE)
                                ? woodMap.get(CompatWood.BlockType.AXLE).get()
                                : block;

                        add(block, createSingleItemTable(axle));
                    } else if (type == CompatWood.BlockType.BARREL) {
                        createBarrelLoot(block, wood);
                    } else if (type == CompatWood.BlockType.VERTICAL_SUPPORT || type == CompatWood.BlockType.HORIZONTAL_SUPPORT) {
                        dropOther(block, ModItems.SUPPORTS.get(wood).get().asItem());
                    } else if (type.needsItem()) {
                        dropSelf(block);
                    }
                }
            }

            // === Rock blocks ===
            for (CompatRock rock : CompatRock.VALUES) {
                var rockMap = ModBlocks.ROCK_BLOCKS.get(rock);
                if (rockMap == null) continue;

                for (CompatRock.BlockType type : CompatRock.BlockType.values()) {
                    if (!rockMap.containsKey(type)) continue;

                    Block block = rockMap.get(type).get();

                    if (type == CompatRock.BlockType.LOOSE) {
                        addLooseRockLoot(block);
                    } else if (type == CompatRock.BlockType.HARDENED) {
                        addHardenedRockLoot(block, rock);
                    } else {
                        dropSelf(block);
                    }
                }
            }

            // === Miscellaneous ===
            dropSelf(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get());
            dropSelf(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get());
            dropSelf(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get());
            dropSelf(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get());

            for (Aqueducts brick : Aqueducts.VALUES) {
                var aqueduct = ModBlocks.AQUEDUCTS.get(brick);
                if (aqueduct != null) dropSelf(aqueduct.get());
            }

            addDryingBricksLoot(ModBlocks.DRYING_MUD_BRICK.get(), ModItems.MUD_BRICK.get());

            // Ores (non-graded + graded)
            ModBlocks.ORES.forEach((rock, oreMap) -> oreMap.forEach((ore, id) -> {
                if (id != null) addSimpleOreDrop(id.get(), "ore/" + ore.name().toLowerCase(Locale.ROOT));
            }));

            ModBlocks.GRADED_ORES.forEach((rock, oreMap) -> oreMap.forEach((ore, gradeMap) ->
                    gradeMap.forEach((grade, id) -> {
                        if (id != null) {
                            String name = grade.name().toLowerCase(Locale.ROOT) + "_" + ore.name().toLowerCase(Locale.ROOT);
                            addSimpleOreDrop(id.get(), "ore/" + name);
                        }
                    })
            ));

            // Clay variants
            addClayDrop(ModBlocks.CLAY_GRASS_BLOCK.get(), Items.CLAY_BALL);
            addClayDrop(ModBlocks.CLAY_DIRT.get(), Items.CLAY_BALL);
            addClayDrop(ModBlocks.CLAY_PODZOL.get(), Items.CLAY_BALL);
            addClayDrop(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get(), TFCItems.KAOLIN_CLAY.get().asItem());
            addClayDrop(ModBlocks.KAOLIN_CLAY_DIRT.get(), TFCItems.KAOLIN_CLAY.get().asItem());
            addClayDrop(ModBlocks.KAOLIN_CLAY_PODZOL.get(), TFCItems.KAOLIN_CLAY.get().asItem());

        //============= FIRMALIFE
            if(ModList.get().isLoaded("firmalife")){
                for (CompatWood wood : CompatWood.VALUES) {
                    Block stompBarrel = CompatFLBlocks.STOMPING_BARRELS.get(wood).get();
                    Block barrelPress = CompatFLBlocks.BARREL_PRESSES.get(wood).get();
                    Block hanger = CompatFLBlocks.HANGERS.get(wood).get();
                    Block foodShelf = CompatFLBlocks.FOOD_SHELVES.get(wood).get();
                    Block wineShelf = CompatFLBlocks.WINE_SHELVES.get(wood).get();
                    Block jarbnet = CompatFLBlocks.JARBNETS.get(wood).get();

                    dropSelf(stompBarrel);
                    dropSelf(barrelPress);
                    dropSelf(hanger);
                    dropSelf(foodShelf);
                    dropSelf(wineShelf);
                    dropSelf(jarbnet);
                    bigBarrelLoot(wood);
                }
                for(CompatRock rock : CompatRock.VALUES){
                    for(Ore.Grade grade : Ore.Grade.values()){
                        Item chromiteItem = FLItems.CHROMIUM_ORES.get(grade).get();
                        Block chromiteOreBlock = CompatFLBlocks.CHROMITE_ORES.get(rock).get(grade).get();

                        addFLOreDrop(chromiteOreBlock, String.valueOf(chromiteItem));
                    }
                }
            }

        // =============== RnR
            if(ModList.get().isLoaded("rnr")){
                CompatRnRBlocks.ROCK_BLOCKS.forEach((rock, typeMap) -> {
                    typeMap.forEach((road, blockSupplier) -> {
                        dropSelf(blockSupplier.get());
                    });
                });
                CompatRnRBlocks.ROCK_STAIRS.forEach((rock, typeMap) -> {
                    typeMap.forEach((road, blockSupplier) -> {
                        dropSelf(blockSupplier.get());
                    });
                });
                CompatRnRBlocks.ROCK_SLABS.forEach((rock, typeMap) -> {
                    typeMap.forEach((road, blockSupplier) -> {
                        dropSelf(blockSupplier.get());
                    });
                });

                dropSelf(CompatRnRBlocks.GRAVEL_ROAD.get());
                dropSelf(CompatRnRBlocks.GRAVEL_ROAD_STAIRS.get());
                dropSelf(CompatRnRBlocks.GRAVEL_ROAD_SLAB.get());
                dropSelf(CompatRnRBlocks.MACADAM_ROAD.get());
                dropSelf(CompatRnRBlocks.MACADAM_ROAD_STAIRS.get());
                dropSelf(CompatRnRBlocks.MACADAM_ROAD_SLAB.get());

                dropSelf(CompatRnRBlocks.OVER_HEIGHT_GRAVEL.get());
                dropSelf(CompatRnRBlocks.TAMPED_DIRT.get());
                dropSelf(CompatRnRBlocks.TAMPED_MUD.get());

                for (CompatWood wood : CompatWood.VALUES){
                    dropSelf(CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood).get());
                    dropSelf(CompatRnRBlocks.WOOD_SHINGLE_ROOF_STAIRS.get(wood).get());
                    dropSelf(CompatRnRBlocks.WOOD_SHINGLE_ROOF_SLABS.get(wood).get());
                }
            }
        }

        private void bigBarrelLoot(CompatWood wood) {
            Block block = CompatFLBlocks.BIG_BARRELS.get(wood).get();
            Item blockItem = block.asItem();

            add(block, LootTable.lootTable()
                    .withPool(lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(lootTableItem(blockItem)
                                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                    .hasProperty(BigBarrelBlock.BARREL_PART, 0))
                                    )
                            )
                            .when(survivesExplosion())
                    ));
        }

        // ====================== HELPER METHODS ======================

        private void createBarrelLoot(Block block, CompatWood wood) {
            Item barrelItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.BARREL).get().asItem();

            var sealed = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                    .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(BarrelBlock.SEALED, true));

            add(block, LootTable.lootTable()
                    .withPool(lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(AlternativesEntry.alternatives(
                                    lootTableItem(barrelItem)
                                            .apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY))
                                            .apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                                                    .copy("", "BlockEntityTag", CopyNbtFunction.MergeStrategy.REPLACE))
                                            .when(sealed),
                                    lootTableItem(barrelItem)
                            ))
                            .when(survivesExplosion())
                    )
            );
        }

        private void addSimpleOreDrop(Block oreBlock, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath("tfc", path);
            Item item = BuiltInRegistries.ITEM.get(id);
            if (item == null || item == Items.AIR) {
                FirmaCompat.LOGGER.error("Missing ore item: {}", id);
                return;
            }
            add(oreBlock, createSingleItemTable(item));
        }

        private void addFLOreDrop(Block oreBlock, String path) {
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath("firmalife", path);
            Item item = BuiltInRegistries.ITEM.get(id);
            if (item == null || item == Items.AIR) {
                FirmaCompat.LOGGER.error("Missing ore item: {}", id);
                return;
            }
            add(oreBlock, createSingleItemTable(item));
        }

        private void addClayDrop(Block block, Item clay) {
            add(block, LootTable.lootTable()
                    .withPool(lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(lootTableItem(clay)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                            .when(survivesExplosion())
                    )
            );
        }

        private void addLooseRockLoot(Block block) {
            LootPoolEntryContainer.Builder<?> entry = lootTableItem(block)
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(LooseRockBlock.COUNT, 2))))
                    .apply(SetItemCountFunction.setCount(ConstantValue.exactly(3))
                            .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(LooseRockBlock.COUNT, 3))))
                    .apply(ApplyExplosionDecay.explosionDecay());

            add(block, LootTable.lootTable()
                    .withPool(lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(entry)
                            .when(survivesExplosion())
                    )
            );
        }

        private void addHardenedRockLoot(Block block, CompatRock rock) {
            Item raw = rock.rawBlock().get().asItem();
            Item loose = ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE).get().asItem();

            add(block, LootTable.lootTable()
                    .withPool(lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(AlternativesEntry.alternatives(
                                    lootTableItem(raw).when(() -> IsIsolatedCondition.INSTANCE),
                                    lootTableItem(loose).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 4)))
                            ))
                            .when(survivesExplosion())
                    )
            );
        }

        private void addDryingBricksLoot(Block dryingBlock, Item driedBrick) {
            LootTable.Builder builder = LootTable.lootTable();

            for (int count = 1; count <= 4; count++) {
                builder.withPool(lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(lootTableItem(dryingBlock)
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(dryingBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(DryingBricksBlock.COUNT, count)
                                                .hasProperty(DryingBricksBlock.DRIED, false)))
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count))))
                        .when(survivesExplosion()));

                builder.withPool(lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(lootTableItem(driedBrick)
                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(dryingBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(DryingBricksBlock.COUNT, count)
                                                .hasProperty(DryingBricksBlock.DRIED, true)))
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count))))
                        .when(survivesExplosion()));
            }
            add(dryingBlock, builder);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            List<Block> known = new ArrayList<>();

            // Wood
            for (CompatWood wood : CompatWood.VALUES) {
                var woodMap = ModBlocks.WOODS.get(wood);
                if (woodMap == null) continue;

                for (CompatWood.BlockType type : CompatWood.BlockType.values()) {
                    var supplier = woodMap.get(type);
                    if (supplier == null) continue;

                    Block block = supplier.get();

                    /* Filter
                    if (type == CompatWood.BlockType.VERTICAL_SUPPORT) {
                        continue;   // Skip - this block intentionally has no loot table
                    }
                     */

                    known.add(block);
                }
            }


            // Rock
            for (CompatRock rock : CompatRock.VALUES) {
                var map = ModBlocks.ROCK_BLOCKS.get(rock);
                if (map != null) map.values().forEach(r -> known.add(r.get()));
            }

            // Explicit blocks
            known.add(ModBlocks.COMPAT_CHEST.get());
            known.add(ModBlocks.COMPAT_TRAPPED_CHEST.get());
            known.add(ModBlocks.COMPAT_FARMLAND.get());
            known.add(ModBlocks.DRYING_MUD_BRICK.get());

            // Clay
            known.add(ModBlocks.CLAY_GRASS_BLOCK.get());
            known.add(ModBlocks.CLAY_DIRT.get());
            known.add(ModBlocks.CLAY_PODZOL.get());
            known.add(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get());
            known.add(ModBlocks.KAOLIN_CLAY_DIRT.get());
            known.add(ModBlocks.KAOLIN_CLAY_PODZOL.get());

            // Gravel deposits
            known.add(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get());
            known.add(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get());
            known.add(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get());
            known.add(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get());

            // Aqueducts
            for (Aqueducts brick : Aqueducts.VALUES) {
                var aqueduct = ModBlocks.AQUEDUCTS.get(brick);
                if (aqueduct != null) known.add(aqueduct.get());
            }

            // Ores
            ModBlocks.ORES.forEach((rock, oreMap) -> oreMap.values().forEach(id -> { if (id != null) known.add(id.get()); }));
            ModBlocks.GRADED_ORES.forEach((rock, oreMap) -> oreMap.forEach((ore, gradeMap) ->
                    gradeMap.values().forEach(id -> { if (id != null) known.add(id.get()); })
            ));

            //Firmalife
            for (CompatWood wood : CompatWood.VALUES) {
                known.add(CompatFLBlocks.FOOD_SHELVES.get(wood).get());
                known.add(CompatFLBlocks.HANGERS.get(wood).get());
                known.add(CompatFLBlocks.JARBNETS.get(wood).get());
                known.add(CompatFLBlocks.WINE_SHELVES.get(wood).get());
                known.add(CompatFLBlocks.BIG_BARRELS.get(wood).get());
                known.add(CompatFLBlocks.STOMPING_BARRELS.get(wood).get());
                known.add(CompatFLBlocks.BARREL_PRESSES.get(wood).get());
            }
            for(CompatRock rock : CompatRock.VALUES){
                for(Ore.Grade grade : Ore.Grade.values()){
                    Block chromiteOreBlock = CompatFLBlocks.CHROMITE_ORES.get(rock).get(grade).get();

                    known.add(chromiteOreBlock);
                }
            }

            //RnR
            CompatRnRBlocks.ROCK_BLOCKS.forEach((rock, typeMap) -> {
                typeMap.forEach((road, blockSupplier) -> {
                    known.add(blockSupplier.get());
                });
            });
            CompatRnRBlocks.ROCK_STAIRS.forEach((rock, typeMap) -> {
                typeMap.forEach((road, blockSupplier) -> {
                    known.add(blockSupplier.get());
                });
            });
            CompatRnRBlocks.ROCK_SLABS.forEach((rock, typeMap) -> {
                typeMap.forEach((road, blockSupplier) -> {
                    known.add(blockSupplier.get());
                });
            });

            known.add(CompatRnRBlocks.GRAVEL_ROAD.get());
            known.add(CompatRnRBlocks.GRAVEL_ROAD_STAIRS.get());
            known.add(CompatRnRBlocks.GRAVEL_ROAD_SLAB.get());
            known.add(CompatRnRBlocks.MACADAM_ROAD.get());
            known.add(CompatRnRBlocks.MACADAM_ROAD_STAIRS.get());
            known.add(CompatRnRBlocks.MACADAM_ROAD_SLAB.get());

            known.add(CompatRnRBlocks.OVER_HEIGHT_GRAVEL.get());
            known.add(CompatRnRBlocks.TAMPED_DIRT.get());
            known.add(CompatRnRBlocks.TAMPED_MUD.get());

            for (CompatWood wood : CompatWood.VALUES){
                known.add(CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood).get());
                known.add(CompatRnRBlocks.WOOD_SHINGLE_ROOF_STAIRS.get(wood).get());
                known.add(CompatRnRBlocks.WOOD_SHINGLE_ROOF_SLABS.get(wood).get());
            }


            return known;
        }
    }
}