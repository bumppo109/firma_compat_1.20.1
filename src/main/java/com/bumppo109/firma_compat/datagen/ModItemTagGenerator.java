package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.util.ModTags;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                               CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, FirmaCompat.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        for (CompatWood wood : CompatWood.VALUES) {
            tag(ItemTags.WOODEN_FENCES)
                    .add(ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.LOG_FENCE).get().asItem());
            tag(ItemTags.FENCES)
                    .add(ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.LOG_FENCE).get().asItem());
        }
        tag(TFCTags.Items.CARRIED_BY_HORSE)
                .add(ModBlocks.COMPAT_CHEST.get().asItem())
                .add(ModBlocks.COMPAT_TRAPPED_CHEST.get().asItem());

        this.tag(ModTags.Items.CHAINS)
                .add(Items.CHAIN)
                .add(TFCBlocks.METALS.get(Metal.Default.COPPER).get(Metal.BlockType.CHAIN).get().asItem())
                .add(TFCBlocks.METALS.get(Metal.Default.BISMUTH_BRONZE).get(Metal.BlockType.CHAIN).get().asItem())
                .add(TFCBlocks.METALS.get(Metal.Default.BRONZE).get(Metal.BlockType.CHAIN).get().asItem())
                .add(TFCBlocks.METALS.get(Metal.Default.BLACK_BRONZE).get(Metal.BlockType.CHAIN).get().asItem())
                .add(TFCBlocks.METALS.get(Metal.Default.WROUGHT_IRON).get(Metal.BlockType.CHAIN).get().asItem())
                .add(TFCBlocks.METALS.get(Metal.Default.STEEL).get(Metal.BlockType.CHAIN).get().asItem())
                .add(TFCBlocks.METALS.get(Metal.Default.BLACK_STEEL).get(Metal.BlockType.CHAIN).get().asItem())
                .add(TFCBlocks.METALS.get(Metal.Default.RED_STEEL).get(Metal.BlockType.CHAIN).get().asItem())
                .add(TFCBlocks.METALS.get(Metal.Default.BLUE_STEEL).get(Metal.BlockType.CHAIN).get().asItem())
        ;
    }
}