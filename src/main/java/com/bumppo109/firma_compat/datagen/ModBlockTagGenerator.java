package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
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
        }

    }
}