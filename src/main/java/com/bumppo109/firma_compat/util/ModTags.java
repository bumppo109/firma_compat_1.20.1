package com.bumppo109.firma_compat.util;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> PREVENT_INTERACTION = tag("prevent_interaction");
        public static final TagKey<Block> MAKES_PRIMITIVE_ANVIL = tag("makes_primitive_anvil");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }
    }

    public static class Fluids {
        public static final TagKey<Fluid> WATERLOGGING_WATER = tag("waterlogging_water");


        private static TagKey<Fluid> tag(String name) {
            return FluidTags.create(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> CHAINS = tag("chains");
        public static final TagKey<Item> COMPAT_LOOSE = tag("compat_loose");
        public static final TagKey<Item> PREVENT_INTERACTION = tag("prevent_interaction");

        public static final TagKey<Item> HAMMERS = tag("hammers");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }
    }

    public static class Entities {
        public static final TagKey<EntityType<?>> REMOVE_ENTITIES = tag("remove_entities");


        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, name));
        }
    }
}