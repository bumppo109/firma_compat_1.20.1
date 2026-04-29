package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addons.firmalife.CompatFLBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRStoneType;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.util.ModTags;
import com.eerussianguy.firmalife.common.FLTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static net.dries007.tfc.common.TFCTags.Blocks.*;
import static net.minecraft.tags.BlockTags.MINEABLE_WITH_AXE;
import static net.minecraft.tags.BlockTags.MINEABLE_WITH_PICKAXE;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, FirmaCompat.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        for (CompatWood wood : CompatWood.VALUES) {
            for (Wood.BlockType blockType : Wood.BlockType.values()) {
                tag(BlockTags.MINEABLE_WITH_AXE)
                        .add(ModBlocks.WOODS.get(wood).get(blockType).get());
            }
            tag(BlockTags.WOODEN_FENCES)
                    .add(ModBlocks.WOODS.get(wood).get(Wood.BlockType.LOG_FENCE).get());
            tag(BlockTags.FENCES)
                    .add(ModBlocks.WOODS.get(wood).get(Wood.BlockType.LOG_FENCE).get());
            tag(ModTags.Blocks.TWIGS)
                    .add(ModBlocks.WOODS.get(wood).get(Wood.BlockType.TWIG).get());
            tag(ModTags.Blocks.SUPPORT_BEAMS)
                    .add(ModBlocks.WOODS.get(wood).get(Wood.BlockType.HORIZONTAL_SUPPORT).get())
                    .add(ModBlocks.WOODS.get(wood).get(Wood.BlockType.VERTICAL_SUPPORT).get());
        }

        for(CompatRock rock : CompatRock.VALUES) {
            for (CompatRock.BlockType blockType : CompatRock.BlockType.values()) {
                tag(BlockTags.MINEABLE_WITH_PICKAXE)
                        .add(ModBlocks.ROCK_BLOCKS.get(rock).get(blockType).get());

                if(blockType.equals(CompatRock.BlockType.LOOSE)) {
                    tag(ModTags.Blocks.LOOSE_ROCKS)
                            .add(ModBlocks.ROCK_BLOCKS.get(rock).get(blockType).get());
                }
                if(blockType.equals(CompatRock.BlockType.LOOSE_COBBLE)) {
                    tag(ModTags.Blocks.LOOSE_COBBLE)
                            .add(ModBlocks.ROCK_BLOCKS.get(rock).get(blockType).get());
                }
                if(blockType.equals(CompatRock.BlockType.HARDENED)) {
                    tag(ModTags.Blocks.ROCK_HARDENED)
                            .add(ModBlocks.ROCK_BLOCKS.get(rock).get(blockType).get());
                }
                if(blockType.equals(CompatRock.BlockType.BRICK_AQUEDUCT)) {
                    tag(ModTags.Blocks.AQUEDUCTS)
                            .add(ModBlocks.ROCK_BLOCKS.get(rock).get(blockType).get());
                }
            }
        }

        //Ore
        ModBlocks.ORES.forEach((rock, oreMap) -> {
            oreMap.forEach((ore, blockSupplier) -> {
                tag(BlockTags.MINEABLE_WITH_PICKAXE)
                        .add(ModBlocks.ORES.get(rock).get(ore).get());
                tag(ModTags.Blocks.ROCK_ORES)
                        .add(ModBlocks.ORES.get(rock).get(ore).get());
            });
        });
        ModBlocks.GRADED_ORES.forEach((rock, oreMap) -> {
            oreMap.forEach((ore, gradeMap) -> {
                gradeMap.forEach((grade, blockSupplier) -> {
                    tag(BlockTags.MINEABLE_WITH_PICKAXE)
                        .add(ModBlocks.GRADED_ORES.get(rock).get(ore).get(grade).get());
                    tag(ModTags.Blocks.ROCK_ORES)
                            .add(ModBlocks.GRADED_ORES.get(rock).get(ore).get(grade).get());
                });
            });
        });

        tag(ModTags.Blocks.ROCK_RAW)
                .add(Blocks.STONE)
                .add(Blocks.DEEPSLATE)
                .add(Blocks.ANDESITE)
                .add(Blocks.DIORITE)
                .add(Blocks.GRANITE)
                .add(Blocks.CALCITE)
                .add(Blocks.TUFF)
                .add(Blocks.DRIPSTONE_BLOCK)
                .add(Blocks.BASALT)
                .add(Blocks.BLACKSTONE)
                .add(Blocks.NETHERRACK)
                .add(Blocks.END_STONE)
        ;

        //Firmalife
        for (CompatWood wood : CompatWood.VALUES){
            tag(BlockTags.MINEABLE_WITH_AXE)
                    .add(CompatFLBlocks.BARREL_PRESSES.get(wood).get())
                    .add(CompatFLBlocks.STOMPING_BARRELS.get(wood).get())
                    .add(CompatFLBlocks.BIG_BARRELS.get(wood).get())
                    .add(CompatFLBlocks.HANGERS.get(wood).get())
                    .add(CompatFLBlocks.JARBNETS.get(wood).get())
                    .add(CompatFLBlocks.FOOD_SHELVES.get(wood).get())
                    .add(CompatFLBlocks.WINE_SHELVES.get(wood).get());
        }
        for (CompatRock rock : CompatRock.VALUES){
            ResourceLocation poorOreRes = BuiltInRegistries.BLOCK.getKey(CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.POOR).get());
            ResourceLocation normalOreRes = BuiltInRegistries.BLOCK.getKey(CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.NORMAL).get());
            ResourceLocation richOreRes = BuiltInRegistries.BLOCK.getKey(CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.RICH).get());

            tag(MINEABLE_WITH_PICKAXE)
                    .addOptional(poorOreRes)
                    .addOptional(normalOreRes)
                    .addOptional(richOreRes);
            tag(CAN_COLLAPSE)
                    .addOptional(poorOreRes)
                    .addOptional(normalOreRes)
                    .addOptional(richOreRes);
            tag(CAN_START_COLLAPSE)
                    .addOptional(poorOreRes)
                    .addOptional(normalOreRes)
                    .addOptional(richOreRes);
            tag(CAN_TRIGGER_COLLAPSE)
                    .addOptional(poorOreRes)
                    .addOptional(normalOreRes)
                    .addOptional(richOreRes);
        }

        //RnR
        CompatRnRBlocks.ROCK_BLOCKS.forEach((rock, typeMap) -> {
            typeMap.forEach((road, blockSupplier) -> {
                tag(MINEABLE_WITH_PICKAXE).add(blockSupplier.get());
                if(road.equals(CompatRnRStoneType.FLAGSTONES)){
                    tag(ModTags.Blocks.FLAGSTONE_ROAD_BLOCKS).add(blockSupplier.get());
                } else if(road.equals(CompatRnRStoneType.COBBLED_ROAD)){
                    tag(ModTags.Blocks.COBBLED_ROAD_BLOCKS).add(blockSupplier.get());
                } else if(road.equals(CompatRnRStoneType.SETT_ROAD)){
                    tag(ModTags.Blocks.SETT_ROAD_BLOCKS).add(blockSupplier.get());
                }
            });
        });
        CompatRnRBlocks.ROCK_STAIRS.forEach((rock, typeMap) -> {
            typeMap.forEach((road, blockSupplier) -> {
                tag(MINEABLE_WITH_PICKAXE).add(blockSupplier.get());
                if(road.equals(CompatRnRStoneType.FLAGSTONES)){
                    tag(ModTags.Blocks.FLAGSTONE_ROAD_STAIRS).add(blockSupplier.get());
                } else if(road.equals(CompatRnRStoneType.COBBLED_ROAD)){
                    tag(ModTags.Blocks.COBBLED_ROAD_STAIRS).add(blockSupplier.get());
                } else if(road.equals(CompatRnRStoneType.SETT_ROAD)){
                    tag(ModTags.Blocks.SETT_ROAD_STAIRS).add(blockSupplier.get());
                }
            });
        });
        CompatRnRBlocks.ROCK_SLABS.forEach((rock, typeMap) -> {
            typeMap.forEach((road, blockSupplier) -> {
                tag(MINEABLE_WITH_PICKAXE).add(blockSupplier.get());
                if(road.equals(CompatRnRStoneType.FLAGSTONES)){
                    tag(ModTags.Blocks.FLAGSTONE_ROAD_SLABS).add(blockSupplier.get());
                } else if(road.equals(CompatRnRStoneType.COBBLED_ROAD)){
                    tag(ModTags.Blocks.COBBLED_ROAD_SLABS).add(blockSupplier.get());
                } else if(road.equals(CompatRnRStoneType.SETT_ROAD)){
                    tag(ModTags.Blocks.SETT_ROAD_SLABS).add(blockSupplier.get());
                }
            });
        });
        tag(ModTags.Blocks.GRAVEL_ROAD_BLOCKS).add(CompatRnRBlocks.GRAVEL_ROAD.get());
        tag(ModTags.Blocks.GRAVEL_ROAD_STAIRS).add(CompatRnRBlocks.GRAVEL_ROAD_STAIRS.get());
        tag(ModTags.Blocks.GRAVEL_ROAD_SLABS).add(CompatRnRBlocks.GRAVEL_ROAD_SLAB.get());
        tag(ModTags.Blocks.MACADAM_ROAD_BLOCKS).add(CompatRnRBlocks.MACADAM_ROAD.get());
        tag(ModTags.Blocks.MACADAM_ROAD_STAIRS).add(CompatRnRBlocks.MACADAM_ROAD_STAIRS.get());
        tag(ModTags.Blocks.MACADAM_ROAD_SLABS).add(CompatRnRBlocks.MACADAM_ROAD_SLAB.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(CompatRnRBlocks.GRAVEL_ROAD.get())
                .add(CompatRnRBlocks.GRAVEL_ROAD_SLAB.get())
                .add(CompatRnRBlocks.GRAVEL_ROAD_STAIRS.get())
                .add(CompatRnRBlocks.MACADAM_ROAD.get())
                .add(CompatRnRBlocks.MACADAM_ROAD_SLAB.get())
                .add(CompatRnRBlocks.MACADAM_ROAD_STAIRS.get())
                .add(CompatRnRBlocks.OVER_HEIGHT_GRAVEL.get())
                .add(CompatRnRBlocks.TAMPED_MUD.get())
                .add(CompatRnRBlocks.TAMPED_DIRT.get())
        ;
    }
}