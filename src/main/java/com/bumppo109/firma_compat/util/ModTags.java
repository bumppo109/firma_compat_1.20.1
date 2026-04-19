package com.bumppo109.firma_compat.util;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        //public static final TagKey<Block> METAL_DETECTOR_VALUABLES = tag("metal_detector_valuables");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CHAINS = tag("chains");
        public static final TagKey<Item> COMPAT_LOOSE = tag("compat_loose");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }
    }
}