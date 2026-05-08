package com.bumppo109.firma_compat.util;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> PREVENT_INTERACTION = tag("prevent_interaction");
        public static final TagKey<Block> MAKES_PRIMITIVE_ANVIL = tag("makes_primitive_anvil");
        public static final TagKey<Block> LOOSE_ROCKS = tag("loose_rocks");
        public static final TagKey<Block> ROCK_ORES = tag("rock_ores");
        public static final TagKey<Block> ROCK_RAW = tag("rock_raw");
        public static final TagKey<Block> ROCK_HARDENED = tag("rock_hardened");
        public static final TagKey<Block> AQUEDUCTS = tag("aqueducts");
        public static final TagKey<Block> TWIGS = tag("twigs");
        public static final TagKey<Block> SUPPORT_BEAMS = tag("support_beams");
        public static final TagKey<Block> LOOSE_COBBLE = tag("loose_cobble");
        public static final TagKey<Block> LANTERNS = tag("lanterns");

        public static final TagKey<Block> FLAGSTONE_ROAD_BLOCKS = tag("flagstone_road_blocks");
        public static final TagKey<Block> FLAGSTONE_ROAD_STAIRS = tag("flagstone_road_stairs");
        public static final TagKey<Block> FLAGSTONE_ROAD_SLABS = tag("flagstone_road_slabs");
        public static final TagKey<Block> COBBLED_ROAD_BLOCKS = tag("cobbled_road_blocks");
        public static final TagKey<Block> COBBLED_ROAD_STAIRS = tag("cobbled_road_stairs");
        public static final TagKey<Block> COBBLED_ROAD_SLABS = tag("cobbled_road_slabs");
        public static final TagKey<Block> SETT_ROAD_BLOCKS = tag("sett_road_blocks");
        public static final TagKey<Block> SETT_ROAD_STAIRS = tag("sett_road_stairs");
        public static final TagKey<Block> SETT_ROAD_SLABS = tag("sett_road_slabs");

        public static final TagKey<Block> GRAVEL_ROAD_BLOCKS = tag("gravel_road_blocks");
        public static final TagKey<Block> GRAVEL_ROAD_STAIRS = tag("gravel_road_stairs");
        public static final TagKey<Block> GRAVEL_ROAD_SLABS = tag("gravel_road_slabs");
        public static final TagKey<Block> MACADAM_ROAD_BLOCKS = tag("macadam_road_blocks");
        public static final TagKey<Block> MACADAM_ROAD_STAIRS = tag("macadam_road_stairs");
        public static final TagKey<Block> MACADAM_ROAD_SLABS = tag("macadam_road_slabs");



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
        public static final TagKey<Item> PREVENT_INTERACTION = tag("prevent_interaction");
        public static final TagKey<Item> PIGLIN_BARTERING_ITEMS = tag("piglin_bartering_items");
        public static final TagKey<Item> LANTERNS = tag("lanterns");

        public static final TagKey<Item> BRICKS = tag("bricks");
        public static final TagKey<Item> TWIGS = tag("twigs");
        public static final TagKey<Item> LOOSE_ROCKS = tag("loose_rocks");
        public static final TagKey<Item> BARRELS = tag("barrels");
        public static final TagKey<Item> LUMBER = tag("lumber");
        public static final TagKey<Item> SUPPORT_BEAMS = tag("support_beams");
        public static final TagKey<Item> MAKES_SOUL_LANTERN = tag("makes_soul_lantern");
        public static final TagKey<Item> MAKES_SOUL_TORCH = tag("makes_soul_torch");

        public static final TagKey<Item> MILKING_ITEMS = tag("milking_items");

        public static final TagKey<Item> HAMMERS = tag("hammers");

        public static final TagKey<Item> FLAGSTONES = tag("flagstones");

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