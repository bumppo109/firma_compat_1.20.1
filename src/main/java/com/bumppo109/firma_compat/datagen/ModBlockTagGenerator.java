package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.util.ModTags;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, FirmaCompat.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        for (CompatWood wood : CompatWood.VALUES) {
            for (CompatWood.BlockType blockType : CompatWood.BlockType.values()) {
                tag(BlockTags.MINEABLE_WITH_AXE)
                        .add(ModBlocks.WOODS.get(wood).get(blockType).get());
            }
            tag(BlockTags.WOODEN_FENCES)
                    .add(ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.LOG_FENCE).get());
            tag(BlockTags.FENCES)
                    .add(ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.LOG_FENCE).get());
            tag(ModTags.Blocks.TWIGS)
                    .add(ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.TWIG).get());
            tag(ModTags.Blocks.SUPPORT_BEAMS)
                    .add(ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.HORIZONTAL_SUPPORT).get())
                    .add(ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.VERTICAL_SUPPORT).get());
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

        tag(TFCTags.Blocks.CAN_LANDSLIDE)
                .addTag(ModTags.Blocks.LOOSE_COBBLE)
                .add(ModBlocks.CLAY_GRASS_BLOCK.get())
                .add(ModBlocks.CLAY_PODZOL.get())
                .add(ModBlocks.CLAY_DIRT.get())
                .add(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get())
                .add(ModBlocks.KAOLIN_CLAY_PODZOL.get())
                .add(ModBlocks.KAOLIN_CLAY_DIRT.get())
                .add(ModBlocks.COMPAT_FARMLAND.get())
                .add(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get())
                .add(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get())
                .add(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get())
                .add(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get())

                .addTag(BlockTags.DIRT)
                .add(Blocks.PACKED_MUD)
                .add(Blocks.SAND)
                .add(Blocks.RED_SAND)
                .add(Blocks.GRAVEL)
        ;
    }
}