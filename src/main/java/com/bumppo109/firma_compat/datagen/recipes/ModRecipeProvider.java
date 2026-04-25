package com.bumppo109.firma_compat.datagen.recipes;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.*;
import com.bumppo109.firma_compat.item.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.AbstractQueue;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ModRecipeProvider extends TFCRecipeBuilder {

    public ModRecipeProvider(PackOutput output, String modId) {
        super(output, modId);
    }

    //TODO - try overriding the vanilla namespace if fences/fence gates dont remove original recipe with empty recipe provider
    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        for (CompatWood wood : CompatWood.VALUES) {

            String woodName = wood.getSerializedName();
            Item lumber = ModItems.LUMBER.get(wood).get();

            if(wood == CompatWood.CRIMSON || wood == CompatWood.WARPED) {
                damageToolShapeless(cache, "from_logs", ("minecraft:" + woodName + "_stems"), "tfc:saws", lumber, 4);
                clutchStemRecipe(cache, wood, lumber);
                damageInputsShaped(cache, woodName + "_support",
                        new String[]{"XX ", "Y  "},
                        Map.of(
                                'X', tagIngredient("minecraft:" + woodName + "_stems"),
                                'Y', tagIngredient("tfc:saws")
                        ),
                        simpleResult("firma_compat:" + woodName + "_support", 8));

            } else {
                damageToolShapeless(cache, "from_logs", ("minecraft:" + woodName + "_logs"), "tfc:saws", lumber, 4);
                clutchRecipe(cache, wood, lumber);
                damageInputsShaped(cache, woodName + "_support",
                        new String[]{"XX ", "Y  "},
                        Map.of(
                                'X', tagIngredient("minecraft:" + woodName + "_logs"),
                                'Y', tagIngredient("tfc:saws")
                        ),
                        simpleResult("firma_compat:" + woodName + "_support", 8));
            }

            damageToolShapeless(cache, "from_planks", wood.planks(), "tfc:saws", lumber, 4);
            damageToolShapeless(cache, "from_stairs", wood.stair(), "tfc:saws", lumber, 3);
            damageToolShapeless(cache, "from_slab", wood.slab(), "tfc:saws", lumber, 2);

            doorRecipe(cache, wood, lumber);
            trapdoorRecipe(cache, wood, lumber);
            loomRecipe(cache, wood, lumber);
            signRecipe(cache, wood, lumber);
            hangingSignRecipe(cache, wood, lumber);
            fenceRecipe(cache, wood, lumber);
            fenceGateRecipe(cache, wood, lumber);
            planksRecipe(cache, wood, lumber);
            pressurePlateRecipe(cache, wood, lumber);

            logFenceRecipe(cache, wood, lumber);
            toolRackRecipe(cache, wood, lumber);
            sluiceRecipe(cache, wood, lumber);
            barrelRecipe(cache, wood, lumber);
            scribingTableRecipe(cache, wood);
            sewingTableRecipe(cache, wood);
            shelfRecipe(cache, wood, lumber);
            axleRecipe(cache, wood);
            bladedAxleRecipe(cache, wood);
            encasedAxleRecipe(cache, wood, lumber);
            gearBoxRecipe(cache, wood, lumber);
            waterWheelRecipe(cache, wood, lumber);
        }
        //extra wood
        damageToolShapeless(cache, null, Items.BAMBOO_BLOCK, "tfc:saws", ModItems.BAMBOO_LUMBER.get(), 8);
        planksRecipe(cache, ModItems.BAMBOO_LUMBER.get(), Items.BAMBOO_PLANKS);
        fenceRecipe(cache, ModItems.BAMBOO_LUMBER.get(), Items.BAMBOO_PLANKS, Items.BAMBOO_FENCE);
        fenceGateRecipe(cache, ModItems.BAMBOO_LUMBER.get(), Items.BAMBOO_PLANKS, Items.BAMBOO_FENCE_GATE);
        vanillaShaped(cache, Items.BAMBOO_DOOR,
                new String[]{"XX ", "XX ", "XX "},
                Map.of('X', ingredient(ModItems.BAMBOO_LUMBER.get())),
                simpleResult(Items.BAMBOO_DOOR, 2),
                null);
        vanillaShaped(cache, Items.BAMBOO_TRAPDOOR,
                new String[]{"XXX", "XXX"},
                Map.of('X', ingredient(ModItems.BAMBOO_LUMBER.get())),
                simpleResult(Items.BAMBOO_TRAPDOOR, 3),
                null);
        vanillaShaped(cache, Items.BAMBOO_PRESSURE_PLATE,
                new String[]{"XX "},
                Map.of('X', ingredient(ModItems.BAMBOO_LUMBER.get())),
                simpleResult(Items.BAMBOO_PRESSURE_PLATE, 1),
                null);

        for (CompatRock rock : CompatRock.VALUES) {
            Item loose = ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE).get().asItem();
            Item looseCobble = ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE_COBBLE).get().asItem();
            Item hardCobble = rock.hardCobbleBlock().get().asItem();
            Item brickItem = rock.brickItem();
            Item bricksBlock = rock.bricksBlock().get().asItem();
            Item brickSlabBlock = rock.getSlab(CompatRock.BlockType.BRICK).get().asItem();
            Item brickStairBlock = rock.getStair(CompatRock.BlockType.BRICK).get().asItem();
            Item brickWallBlock = rock.getWall(CompatRock.BlockType.BRICK).get().asItem();
            Item aqueductBlock = ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.BRICK_AQUEDUCT).get().asItem();

            //brick item
            damageToolShapeless(cache, null, loose, "tfc:chisels", brickItem, 1);
            //brick block
            vanillaShaped(cache, bricksBlock,
                    new String[]{"AXA", "XAX", "AXA"},
                    Map.of('X', ingredient("tfc:mortar"), 'A', ingredient(brickItem)),
                    simpleResult(bricksBlock, 4),
                    null);
            if(rock != CompatRock.STONE && rock != CompatRock.DEEPSLATE && rock != CompatRock.BLACKSTONE && rock != CompatRock.END_STONE && rock != CompatRock.NETHERRACK){
                //brick slab block
                vanillaShaped(cache, brickSlabBlock,
                        new String[]{"XXX"},
                        Map.of('X', ingredient(bricksBlock)),
                        simpleResult(brickSlabBlock, 6),
                        null);
                //brick stairs block
                vanillaShaped(cache, brickStairBlock,
                        new String[]{"  X", " XX", "XXX"},
                        Map.of('X', ingredient(bricksBlock)),
                        simpleResult(brickStairBlock, 4),
                        null);
                //brick wall block
                vanillaShaped(cache, brickWallBlock,
                        new String[]{"XXX", "XXX"},
                        Map.of('X', ingredient(bricksBlock)),
                        simpleResult(brickWallBlock, 6),
                        null);
            }
            //chisel
            chisel(cache, bricksBlock, brickSlabBlock, ChiselMode.SLAB);
            chisel(cache, bricksBlock, brickStairBlock, ChiselMode.STAIR);
            //loose cobble
            vanillaShaped(cache, looseCobble,
                    new String[]{"XX ", "XX "},
                    Map.of('X', ingredient(loose)),
                    simpleResult(looseCobble, 1),
                    null);
            //hardened cobble
            vanillaShaped(cache, hardCobble,
                    new String[]{"AXA", "XAX", "AXA"},
                    Map.of('X', ingredient(loose), 'A', ingredient("tfc:mortar")),
                    simpleResult(hardCobble, 4),
                    null);
            //aqueduct
            vanillaShaped(cache, aqueductBlock,
                    new String[]{"X X", "AXA"},
                    Map.of('X', ingredient(brickItem), 'A', ingredient("tfc:mortar")),
                    simpleResult(aqueductBlock, 1),
                    null);

            //collapse
            collapse(cache, ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE_COBBLE).get(), null, rock);

            //landslide
            selfLandslide(cache, ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE_COBBLE).get());
        }
        //extra brick
        damageToolShapeless(cache, null, ModItems.BRICK.get(CompatRock.STONE).get(), "tfc:chisels", Items.STONE_BUTTON, 1);
        damageToolShapeless(cache, null, ModItems.BRICK.get(CompatRock.BLACKSTONE).get(), "tfc:chisels", Items.POLISHED_BLACKSTONE_BUTTON, 1);
        vanillaShaped(cache, Items.STONE_PRESSURE_PLATE,
                new String[]{"XX "},
                Map.of('X', ingredient(ModItems.BRICK.get(CompatRock.STONE).get())),
                simpleResult(Items.STONE_PRESSURE_PLATE, 1),
                null);
        vanillaShaped(cache, Items.POLISHED_BLACKSTONE_PRESSURE_PLATE,
                new String[]{"XX "},
                Map.of('X', ingredient(ModItems.BRICK.get(CompatRock.BLACKSTONE).get())),
                simpleResult(Items.POLISHED_BLACKSTONE_PRESSURE_PLATE, 1),
                null);


        damageToolShapeless(cache, null, Items.QUARTZ, "tfc:chisels", ModItems.QUARTZ_BRICK.get(), 1);
        damageToolShapeless(cache, null, Items.PRISMARINE_SHARD, "tfc:chisels", ModItems.PRISMARINE_BRICK.get(), 1);
        vanillaShaped(cache, Blocks.QUARTZ_BRICKS.asItem(),
                new String[]{"AXA", "XAX", "AXA"},
                Map.of('X', ingredient("tfc:mortar"), 'A', ingredient(ModItems.QUARTZ_BRICK.get())),
                simpleResult(Blocks.QUARTZ_BRICKS.asItem(), 4),
                null);
        vanillaShaped(cache, Blocks.PRISMARINE_BRICKS.asItem(),
                new String[]{"AXA", "XAX", "AXA"},
                Map.of('X', ingredient("tfc:mortar"), 'A', ingredient(ModItems.PRISMARINE_BRICK.get())),
                simpleResult(Blocks.PRISMARINE_BRICKS.asItem(), 4),
                null);

        //extra aqueducts
        vanillaShapeless(cache,"from_moss", Items.MOSS_BLOCK, ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.BRICK_AQUEDUCT).get().asItem() , ModBlocks.AQUEDUCTS.get(Aqueducts.MOSSY_STONE_BRICKS).get().asItem(), 1);
        vanillaShapeless(cache,"from_vine", Items.VINE, ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.BRICK_AQUEDUCT).get().asItem() , ModBlocks.AQUEDUCTS.get(Aqueducts.MOSSY_STONE_BRICKS).get().asItem(), 1);
        vanillaShaped(cache, ModBlocks.AQUEDUCTS.get(Aqueducts.BRICKS).get().asItem(),
                new String[]{"X X", "AXA"},
                Map.of('X', ingredient("minecraft:brick"), 'A', ingredient("tfc:mortar")),
                simpleResult(ModBlocks.AQUEDUCTS.get(Aqueducts.BRICKS).get().asItem(), 1),
                null);
        vanillaShaped(cache, ModBlocks.AQUEDUCTS.get(Aqueducts.NETHER_BRICKS).get().asItem(),
                new String[]{"X X", "AXA"},
                Map.of('X', ingredient("minecraft:nether_brick"), 'A', ingredient("tfc:mortar")),
                simpleResult(ModBlocks.AQUEDUCTS.get(Aqueducts.NETHER_BRICKS).get().asItem(), 1),
                null);
        vanillaShaped(cache, ModBlocks.AQUEDUCTS.get(Aqueducts.PRISMARINE_BRICKS).get().asItem(),
                new String[]{"X X", "AXA"},
                Map.of('X', ingredient("firma_compat:prismarine_brick"), 'A', ingredient("tfc:mortar")),
                simpleResult(ModBlocks.AQUEDUCTS.get(Aqueducts.PRISMARINE_BRICKS).get().asItem(), 1),
                null);
        vanillaShaped(cache, ModBlocks.AQUEDUCTS.get(Aqueducts.QUARTZ_BRICKS).get().asItem(),
                new String[]{"X X", "AXA"},
                Map.of('X', ingredient("firma_compat:quartz_brick"), 'A', ingredient("tfc:mortar")),
                simpleResult(ModBlocks.AQUEDUCTS.get(Aqueducts.QUARTZ_BRICKS).get().asItem(), 1),
                null);


        //======== Chisel Recipes for Vanilla Blocks
        chisel(cache, Items.STONE, Items.SMOOTH_STONE, ChiselMode.SMOOTH);
        chisel(cache, Items.STONE, Items.STONE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.STONE, Items.STONE_SLAB, ChiselMode.SLAB);
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.SMOOTH_STONE, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.STONE_STAIRS, ChiselMode.STAIR, "from_hardened");
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.STONE_SLAB, ChiselMode.SLAB, "from_hardened");
        chisel(cache, Items.SMOOTH_STONE, Items.SMOOTH_STONE_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.COBBLESTONE, Items.COBBLESTONE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.COBBLESTONE, Items.COBBLESTONE_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.MOSSY_COBBLESTONE, Items.MOSSY_COBBLESTONE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.MOSSY_COBBLESTONE, Items.MOSSY_COBBLESTONE_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.STONE_BRICKS, Items.CHISELED_STONE_BRICKS, ChiselMode.SMOOTH);
        chisel(cache, Items.STONE_BRICKS, Items.STONE_BRICK_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.STONE_BRICKS, Items.STONE_BRICK_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.MOSSY_STONE_BRICKS, Items.MOSSY_STONE_BRICK_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.MOSSY_STONE_BRICKS, Items.MOSSY_STONE_BRICK_SLAB, ChiselMode.SLAB);

        chisel(cache, Items.GRANITE, Items.POLISHED_GRANITE, ChiselMode.SMOOTH);
        chisel(cache, Items.GRANITE, Items.GRANITE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.GRANITE, Items.GRANITE_SLAB, ChiselMode.SLAB);
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.GRANITE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.POLISHED_GRANITE, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.GRANITE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.GRANITE_STAIRS, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.GRANITE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.GRANITE_SLAB, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, Items.POLISHED_GRANITE, Items.POLISHED_GRANITE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.POLISHED_GRANITE, Items.POLISHED_GRANITE_STAIRS, ChiselMode.SLAB);

        chisel(cache, Items.ANDESITE, Items.POLISHED_ANDESITE, ChiselMode.SMOOTH);
        chisel(cache, Items.ANDESITE, Items.ANDESITE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.ANDESITE, Items.ANDESITE_SLAB, ChiselMode.SLAB);
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.ANDESITE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.POLISHED_ANDESITE, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.ANDESITE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.ANDESITE_STAIRS, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.ANDESITE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.ANDESITE_SLAB, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, Items.POLISHED_ANDESITE, Items.POLISHED_ANDESITE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.POLISHED_ANDESITE, Items.POLISHED_ANDESITE_STAIRS, ChiselMode.SLAB);

        chisel(cache, Items.DIORITE, Items.POLISHED_DIORITE, ChiselMode.SMOOTH);
        chisel(cache, Items.DIORITE, Items.DIORITE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.DIORITE, Items.DIORITE_SLAB, ChiselMode.SLAB);
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.DIORITE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.POLISHED_DIORITE, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.DIORITE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.DIORITE_STAIRS, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.DIORITE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.DIORITE_SLAB, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, Items.POLISHED_DIORITE, Items.POLISHED_DIORITE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.POLISHED_DIORITE, Items.POLISHED_DIORITE_STAIRS, ChiselMode.SLAB);

        chisel(cache, Items.DEEPSLATE, Items.POLISHED_DEEPSLATE, ChiselMode.SMOOTH);
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.DEEPSLATE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.POLISHED_DEEPSLATE, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, Items.COBBLED_DEEPSLATE, Items.COBBLED_DEEPSLATE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.COBBLED_DEEPSLATE, Items.COBBLED_DEEPSLATE_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.POLISHED_DEEPSLATE, Items.POLISHED_DEEPSLATE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.POLISHED_DEEPSLATE, Items.POLISHED_DEEPSLATE_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.DEEPSLATE_BRICKS, Items.CHISELED_DEEPSLATE, ChiselMode.SMOOTH);
        chisel(cache, Items.DEEPSLATE_BRICKS, Items.DEEPSLATE_BRICK_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.DEEPSLATE_BRICKS, Items.DEEPSLATE_BRICK_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.DEEPSLATE_TILES, Items.DEEPSLATE_TILE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.DEEPSLATE_TILES, Items.DEEPSLATE_TILE_SLAB, ChiselMode.SLAB);

        chisel(cache, Items.BRICKS, Items.BRICK_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.BRICKS, Items.BRICK_SLAB, ChiselMode.SLAB);

        chisel(cache, Items.MUD_BRICKS, Items.MUD_BRICK_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.MUD_BRICKS, Items.MUD_BRICK_SLAB, ChiselMode.SLAB);

        chisel(cache, Items.SANDSTONE, Items.SMOOTH_SANDSTONE, ChiselMode.SMOOTH);
        chisel(cache, Items.SANDSTONE, Items.SANDSTONE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.SANDSTONE, Items.SANDSTONE_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.SMOOTH_SANDSTONE, Items.CUT_SANDSTONE, ChiselMode.SMOOTH);
        chisel(cache, Items.SMOOTH_SANDSTONE, Items.SMOOTH_SANDSTONE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.SMOOTH_SANDSTONE, Items.SMOOTH_SANDSTONE_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.CUT_SANDSTONE, Items.CHISELED_SANDSTONE, ChiselMode.SMOOTH);
        chisel(cache, Items.CUT_SANDSTONE, Blocks.CUT_SANDSTONE_SLAB.asItem(), ChiselMode.SLAB);

        chisel(cache, Items.RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE, ChiselMode.SMOOTH);
        chisel(cache, Items.RED_SANDSTONE, Items.RED_SANDSTONE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.RED_SANDSTONE, Items.RED_SANDSTONE_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.SMOOTH_RED_SANDSTONE, Items.CUT_RED_SANDSTONE, ChiselMode.SMOOTH);
        chisel(cache, Items.SMOOTH_RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.SMOOTH_RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.CUT_RED_SANDSTONE, Items.CHISELED_RED_SANDSTONE, ChiselMode.SMOOTH);
        chisel(cache, Items.CUT_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE_SLAB.asItem(), ChiselMode.SLAB);

        chisel(cache, Items.PRISMARINE, Items.PRISMARINE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.PRISMARINE, Items.PRISMARINE_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.PRISMARINE_BRICKS, Items.DARK_PRISMARINE, ChiselMode.SMOOTH);
        chisel(cache, Items.PRISMARINE_BRICKS, Items.PRISMARINE_BRICK_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.PRISMARINE_BRICKS, Items.PRISMARINE_BRICK_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.DARK_PRISMARINE, Items.DARK_PRISMARINE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.DARK_PRISMARINE, Items.DARK_PRISMARINE_SLAB, ChiselMode.SLAB);

        chisel(cache, Items.NETHER_BRICKS, Items.CHISELED_NETHER_BRICKS, ChiselMode.SMOOTH);
        chisel(cache, Items.NETHER_BRICKS, Items.NETHER_BRICK_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.NETHER_BRICKS, Items.NETHER_BRICK_SLAB, ChiselMode.SLAB);

        chisel(cache, Items.RED_NETHER_BRICKS, Items.RED_NETHER_BRICK_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.RED_NETHER_BRICKS, Items.RED_NETHER_BRICK_SLAB, ChiselMode.SLAB);

        chisel(cache, Items.BASALT, Items.SMOOTH_BASALT, ChiselMode.SMOOTH);
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.BASALT).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.SMOOTH_BASALT, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, Items.SMOOTH_BASALT, Items.POLISHED_BASALT, ChiselMode.SMOOTH);

        chisel(cache, Items.BLACKSTONE, Items.POLISHED_BLACKSTONE, ChiselMode.SMOOTH);
        chisel(cache, Items.BLACKSTONE, Items.BLACKSTONE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.BLACKSTONE, Items.BLACKSTONE_SLAB, ChiselMode.SLAB);
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.BLACKSTONE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.POLISHED_BLACKSTONE, ChiselMode.SMOOTH, "from_hardened");
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.BLACKSTONE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.BLACKSTONE_STAIRS, ChiselMode.STAIR, "from_hardened");
        chisel(cache, ModBlocks.ROCK_BLOCKS.get(CompatRock.BLACKSTONE).get(CompatRock.BlockType.HARDENED).get().asItem(), Items.BLACKSTONE_SLAB, ChiselMode.SLAB, "from_hardened");
        chisel(cache, Items.POLISHED_BLACKSTONE, Items.POLISHED_BLACKSTONE_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.POLISHED_BLACKSTONE, Items.POLISHED_BLACKSTONE_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.POLISHED_BLACKSTONE_BRICKS, Items.CHISELED_POLISHED_BLACKSTONE, ChiselMode.SMOOTH);
        chisel(cache, Items.POLISHED_BLACKSTONE_BRICKS, Items.POLISHED_BLACKSTONE_BRICK_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.POLISHED_BLACKSTONE_BRICKS, Items.POLISHED_BLACKSTONE_BRICK_SLAB, ChiselMode.SLAB);

        chisel(cache, Items.END_STONE_BRICKS, Items.END_STONE_BRICK_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.END_STONE_BRICKS, Items.END_STONE_BRICK_SLAB, ChiselMode.SLAB);

        chisel(cache, Items.PURPUR_BLOCK, Items.PURPUR_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.PURPUR_BLOCK, Items.PURPUR_SLAB, ChiselMode.SLAB);

        chisel(cache, Items.QUARTZ_BLOCK, Items.SMOOTH_QUARTZ, ChiselMode.SMOOTH);
        chisel(cache, Items.QUARTZ_BLOCK, Items.QUARTZ_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.QUARTZ_BLOCK, Items.QUARTZ_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.SMOOTH_QUARTZ, Items.SMOOTH_QUARTZ_STAIRS, ChiselMode.STAIR);
        chisel(cache, Items.SMOOTH_QUARTZ, Items.SMOOTH_QUARTZ_SLAB, ChiselMode.SLAB);
        chisel(cache, Items.QUARTZ_BRICKS, Items.CHISELED_QUARTZ_BLOCK, ChiselMode.SMOOTH);

        damageToolShapeless(cache, null, Items.CRACKED_STONE_BRICKS, "tfc:hammers", Items.STONE_BRICKS, 1);
        damageToolShapeless(cache, null, Items.CRACKED_DEEPSLATE_BRICKS, "tfc:hammers", Items.DEEPSLATE_BRICKS, 1);
        damageToolShapeless(cache, null, Items.CRACKED_DEEPSLATE_TILES, "tfc:hammers", Items.DEEPSLATE_TILES, 1);
        damageToolShapeless(cache, null, Items.CRACKED_NETHER_BRICKS, "tfc:hammers", Items.NETHER_BRICKS, 1);
        damageToolShapeless(cache, null, Items.CRACKED_POLISHED_BLACKSTONE_BRICKS, "tfc:hammers", Items.POLISHED_BLACKSTONE_BRICKS, 1);

        damageToolShapeless(cache, null, Items.CHISELED_STONE_BRICKS, "tfc:chisels", Items.STONE_BRICKS, 1);
        damageToolShapeless(cache, null, Items.SMOOTH_STONE, "tfc:chisels", Items.STONE, 1);
        damageToolShapeless(cache, null, Items.POLISHED_GRANITE, "tfc:chisels", Items.GRANITE, 1);
        damageToolShapeless(cache, null, Items.POLISHED_ANDESITE, "tfc:chisels", Items.ANDESITE, 1);
        damageToolShapeless(cache, null, Items.POLISHED_DIORITE, "tfc:chisels", Items.DIORITE, 1);
        damageToolShapeless(cache, null, Items.CHISELED_DEEPSLATE, "tfc:chisels", Items.DEEPSLATE_BRICKS, 1);
        damageToolShapeless(cache, null, Items.POLISHED_DEEPSLATE, "tfc:chisels", Items.DEEPSLATE, 1);
        damageToolShapeless(cache, null, Items.CHISELED_SANDSTONE, "tfc:chisels", Items.CUT_SANDSTONE, 1);
        damageToolShapeless(cache, null, Items.CUT_SANDSTONE, "tfc:chisels", Items.SMOOTH_SANDSTONE, 1);
        damageToolShapeless(cache, null, Items.SMOOTH_SANDSTONE, "tfc:chisels", Items.SANDSTONE, 1);
        damageToolShapeless(cache, null, Items.CHISELED_RED_SANDSTONE, "tfc:chisels", Items.CUT_RED_SANDSTONE, 1);
        damageToolShapeless(cache, null, Items.CUT_RED_SANDSTONE, "tfc:chisels", Items.SMOOTH_RED_SANDSTONE, 1);
        damageToolShapeless(cache, null, Items.SMOOTH_RED_SANDSTONE, "tfc:chisels", Items.RED_SANDSTONE, 1);
        damageToolShapeless(cache, null, Items.DARK_PRISMARINE, "tfc:chisels", Items.PRISMARINE_BRICKS, 1);
        damageToolShapeless(cache, null, Items.CHISELED_NETHER_BRICKS, "tfc:chisels", Items.NETHER_BRICKS, 1);
        damageToolShapeless(cache, null, Items.SMOOTH_BASALT, "tfc:chisels", Items.BASALT, 1);
        damageToolShapeless(cache, null, Items.POLISHED_BASALT, "tfc:chisels", Items.SMOOTH_BASALT, 1);
        damageToolShapeless(cache, null, Items.CHISELED_QUARTZ_BLOCK, "tfc:chisels", Items.QUARTZ_BRICKS, 1);
        damageToolShapeless(cache, null, Items.SMOOTH_QUARTZ, "tfc:chisels", Items.QUARTZ_BLOCK, 1);

        // ================ NATURAL

        //mud
        selfLandslide(cache, Blocks.PACKED_MUD);
        selfLandslide(cache, Blocks.MUD);
        planksRecipe(cache, Items.MUD, Items.PACKED_MUD);

        //clay grasses
        landslide(cache, ModBlocks.CLAY_GRASS_BLOCK.get(), ModBlocks.CLAY_DIRT.get(), null);
        landslide(cache, ModBlocks.CLAY_PODZOL.get(), ModBlocks.CLAY_DIRT.get(), null);
        selfLandslide(cache, ModBlocks.CLAY_DIRT.get());
        landslide(cache, ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get(), ModBlocks.KAOLIN_CLAY_DIRT.get(), null);
        landslide(cache, ModBlocks.KAOLIN_CLAY_PODZOL.get(), ModBlocks.KAOLIN_CLAY_DIRT.get(), null);
        selfLandslide(cache, ModBlocks.KAOLIN_CLAY_DIRT.get());
        landslide(cache, ModBlocks.COMPAT_FARMLAND.get(), Blocks.DIRT, null);

        //deposit
        selfLandslide(cache, ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get());
        selfLandslide(cache, ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get());
        selfLandslide(cache, ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get());
        selfLandslide(cache, ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get());

        return CompletableFuture.completedFuture(null);
    }

    protected String itemId(ItemLike itemLike) {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath();
    }

    /**
     * Generates a TFC collapse recipe for a CompatRock.
     * Includes: raw rock, hardened rock, all normal ores, and only POOR graded ores.
     *
     * @param cache        CachedOutput
     * @param resultBlock  The resulting cobble block after collapse
     * @param recipeSuffix Optional suffix for the recipe filename (null = no suffix)
     * @param compatRock   The rock type to generate the recipe for
     */
    protected void collapse(CachedOutput cache, Block resultBlock, @Nullable String recipeSuffix, CompatRock compatRock) {
        ResourceLocation resultKey = ForgeRegistries.BLOCKS.getKey(resultBlock);
        Objects.requireNonNull(resultKey, "Result block has no registry name");

        String baseName = resultKey.getPath();
        String resultId = resultKey.toString();

        JsonArray ingredients = new JsonArray();

        // Raw rock
        Supplier<Block> rawBlock = compatRock.rawBlock();
        if (rawBlock != null) {
            ResourceLocation key = ForgeRegistries.BLOCKS.getKey(rawBlock.get());
            if (key != null) ingredients.add(key.toString());
        }

        // Hardened rock
        var rockBlocks = ModBlocks.ROCK_BLOCKS.get(compatRock);
        if (rockBlocks != null) {
            Supplier<Block> hardened = rockBlocks.get(CompatRock.BlockType.HARDENED);
            if (hardened != null) {
                ResourceLocation key = ForgeRegistries.BLOCKS.getKey(hardened.get());
                if (key != null) ingredients.add(key.toString());
            }
        }

        // Normal (non-graded) ores for this rock
        var normalOres = ModBlocks.ORES.get(compatRock);
        if (normalOres != null) {
            normalOres.forEach((ore, supplier) -> {
                if (supplier != null) {
                    ResourceLocation key = ForgeRegistries.BLOCKS.getKey(supplier.get());
                    if (key != null) ingredients.add(key.toString());
                }
            });
        }

        // Only POOR graded ores for this rock
        var gradedOres = ModBlocks.GRADED_ORES.get(compatRock);
        if (gradedOres != null) {
            gradedOres.forEach((ore, gradeMap) -> {
                Supplier<Block> poorOre = gradeMap.get(CompatOre.Grade.POOR);
                if (poorOre != null) {
                    ResourceLocation key = ForgeRegistries.BLOCKS.getKey(poorOre.get());
                    if (key != null) ingredients.add(key.toString());
                }
            });
        }

        // Generate the recipe
        collapseOrLandslide(cache, "collapse", baseName, recipeSuffix, ingredients, resultId);
    }

    protected void landslide(CachedOutput cache, Block inputBlock, Block resultBlock, @Nullable String recipeSuffix) {
        ResourceLocation inputRes = ForgeRegistries.BLOCKS.getKey(inputBlock);
        String inputPath = Objects.requireNonNull(inputRes).getPath();
        String inputId = Objects.requireNonNull(inputRes).toString();

        String outputId = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(resultBlock)).toString();

        landslide(cache, inputPath, inputId, outputId, recipeSuffix);
    }

    protected void selfLandslide(CachedOutput cache, Block block) {
        ResourceLocation inputRes = ForgeRegistries.BLOCKS.getKey(block);
        String inputPath = Objects.requireNonNull(inputRes).getPath();
        String inputId = Objects.requireNonNull(inputRes).toString();

        landslide(cache, inputPath, inputId, inputId, null);
    }

    /**
     * Creates a simple shapeless crafting recipe with exactly 2 ingredients.
     *
     * @param cache      The CachedOutput for recipe generation
     * @param ingredient1 First ingredient item
     * @param ingredient2 Second ingredient item
     * @param result      The resulting item
     * @param count       The count of the result (usually 1)
     */
    protected void vanillaShapeless(CachedOutput cache,
                                    Item ingredient1,
                                    Item ingredient2,
                                    Item result,
                                    int count) {

        JsonArray ingredients = new JsonArray();
        ingredients.add(ingredient(ingredient1));
        ingredients.add(ingredient(ingredient2));

        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result)).toString());
        if (count != 1) {
            resultObj.addProperty("count", count);
        }

        vanillaShapeless(cache, result, null, ingredients, resultObj, null);
    }

    protected void vanillaShapeless(CachedOutput cache, String suffix,
                                    Item ingredient1,
                                    Item ingredient2,
                                    Item result,
                                    int count) {

        JsonArray ingredients = new JsonArray();
        ingredients.add(ingredient(ingredient1));
        ingredients.add(ingredient(ingredient2));

        JsonObject resultObj = new JsonObject();
        resultObj.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result)).toString());
        if (count != 1) {
            resultObj.addProperty("count", count);
        }

        vanillaShapeless(cache, result, suffix, ingredients, resultObj, null);
    }

    protected void damageToolShapeless(CachedOutput cache,
                                       @Nullable String suffix,
                                       ItemLike input,
                                       String toolTag,
                                       ItemLike output,
                                       int outputCount) {

        ResourceLocation inputItem = ForgeRegistries.ITEMS.getKey(input.asItem());

        assert inputItem != null;
        String recipeName = inputItem.getPath();
        String inputId = inputItem.toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output.asItem())).toString();

        JsonArray ingredients = new JsonArray();
        ingredients.add(ingredient(inputId));      // Main input item
        ingredients.add(tagIngredient(toolTag));         // Tool that takes damage

        JsonObject result = simpleResult(outputId, outputCount);

        if(suffix != null) {
            recipeName = recipeName + "_" + suffix;
        }

        damageInputsShapeless(cache, recipeName, ingredients, result);
    }

    protected void damageToolShapeless(CachedOutput cache,
                                       @Nullable String suffix,
                                       String inputTag,
                                       String toolTag,
                                       ItemLike output,
                                       int outputCount) {
        String recipeName = itemId(output);
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output.asItem())).toString();

        JsonArray ingredients = new JsonArray();
        ingredients.add(tagIngredient(inputTag));      // Main input item
        ingredients.add(tagIngredient(toolTag));         // Tool that takes damage

        JsonObject result = simpleResult(outputId, outputCount);

        if(suffix != null) {
            recipeName = recipeName + "_" + suffix;
        }

        damageInputsShapeless(cache, recipeName, ingredients, result);
    }

    /**
     * Creates a simple TFC chisel recipe that turns one block into another using a chisel.
     *
     * @param cache      The CachedOutput for recipe generation
     * @param input      The input block/item (usually a block)
     * @param output     The resulting block (as Item)
     * @param mode       The chisel mode (e.g. ChiselMode.SMOOTH, ChiselMode.SLAB, etc.)
     */
    protected void chisel(CachedOutput cache, Item input, Item output, ChiselMode mode) {

        String name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output)).getPath();

        // Use full resource location string for both ingredient and result
        String ingredientStr = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(input)).toString();
        String resultStr     = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output)).toString();

        // Call the original method, but now passing strings instead of JsonObjects
        chisel(cache, name, ingredientStr, resultStr, mode, null, null, null);
    }

    protected void chisel(CachedOutput cache, Item input, Item output, ChiselMode mode, String recipeSuffix) {

        String name = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output)).getPath();

        // Use full resource location string for both ingredient and result
        String ingredientStr = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(input)).toString();
        String resultStr     = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output)).toString();

        // Call the original method, but now passing strings instead of JsonObjects
        chisel(cache, name, ingredientStr, resultStr, mode, null, null, recipeSuffix);
    }

    protected String[] doorPattern = new String[]{"XX ", "XX ", "XX "};
    protected String[] trapdoorPattern = new String[]{"XXX", "XXX"};
    protected String[] planksPattern = new String[]{"XX ", "XX "};
    protected String[] fenceGatePattern = new String[]{"XYX", "XYX"};
    protected String[] fencePattern = new String[]{"YXY", "YXY"};
    protected String[] signPattern = new String[]{"XXX", "XXX", " Y "};
    protected String[] hangingSignPattern = new String[]{"Y Y", "XXX", "XXX"};
    protected String[] pressurePlatePattern = new String[]{"XX "};

    protected String[] toolRackPattern = new String[]{"XXX", "   ", "XXX"};
    protected String[] loomPattern = new String[]{"XXX", "XYX", "X X"};
    protected String[] sluicePattern = new String[]{"  Y", " YX", "YXX"};
    protected String[] barrelPattern = new String[]{"X X", "X X", "XXX"};
    protected String[] scribingPattern = new String[]{"Y Z", "AAA", "B B"};
    protected String[] sewingPattern = new String[]{" YZ", "AAA", "B B"};
    protected String[] shelfPattern = new String[]{"AAA", "X X", "Y Y"};
    protected String[] axlePattern = new String[]{"ABA"};
    protected String[] bladedAxlePattern = new String[]{"AB"};
    protected String[] encasedAxlePattern = new String[]{" A ", "XBX", " A "};
    protected String[] clutchPattern = new String[]{"XAX", "BCD", "XAX"};
    protected String[] gearBoxPattern = new String[]{" X ", "XAX", " X "};
    protected String[] waterWheelPattern = new String[]{"XAX", "ABA", "XAX"};


    protected void doorRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = wood.door().asItem();

        vanillaShaped(cache, outputItem,
                doorPattern,
                Map.of('X', ingredient(lumber)),
                simpleResult(outputItem, 2),
                null);
    }
    protected void trapdoorRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = wood.trapdoor().asItem();

        vanillaShaped(cache, outputItem,
                trapdoorPattern,
                Map.of('X', ingredient(lumber)),
                simpleResult(outputItem, 3),
                null);
    }
    protected void planksRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = wood.planks().asItem();

        vanillaShaped(cache, outputItem,
                planksPattern,
                Map.of('X', ingredient(lumber)),
                simpleResult(outputItem, 1),
                null);
    }
    protected void planksRecipe(CachedOutput cache, Item lumber, Item planks) {
        vanillaShaped(cache, planks,
                planksPattern,
                Map.of('X', ingredient(lumber)),
                simpleResult(planks, 1),
                null);
    }
    protected void fenceRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = wood.fence().asItem();

        vanillaShaped(cache, outputItem,
                fencePattern,
                Map.of('X', ingredient(lumber), 'Y', ingredient(wood.planks().asItem())),
                simpleResult(outputItem, 8),
                null);
    }
    protected void fenceRecipe(CachedOutput cache, Item lumber, Item fencePostItem, Item fence) {
        vanillaShaped(cache, fence,
                fencePattern,
                Map.of('X', ingredient(lumber), 'Y', ingredient(fencePostItem)),
                simpleResult(fence, 8),
                null);
    }
    protected void logFenceRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.LOG_FENCE).get().asItem();

        vanillaShaped(cache, outputItem,
                fencePattern,
                Map.of('X', ingredient(lumber), 'Y', ingredient(wood.log().asItem())),
                simpleResult(outputItem, 8),
                null);
    }
    protected void fenceGateRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = wood.fenceGate().asItem();

        vanillaShaped(cache, outputItem,
                fenceGatePattern,
                Map.of('X', ingredient(lumber), 'Y', ingredient(wood.planks().asItem())),
                simpleResult(outputItem, 2),
                null);
    }
    protected void fenceGateRecipe(CachedOutput cache, Item lumber, Item fencePostItem, Item fence) {
        vanillaShaped(cache, fence,
                fenceGatePattern,
                Map.of('X', ingredient(lumber), 'Y', ingredient(fencePostItem)),
                simpleResult(fence, 2),
                null);
    }
    protected void signRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = wood.sign().asItem();

        vanillaShaped(cache, outputItem,
                signPattern,
                Map.of('X', ingredient(lumber), 'Y', tagIngredient("forge:rods/wooden")),
                simpleResult(outputItem, 3),
                null);
    }
    protected void hangingSignRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = wood.hangingSign().asItem();

        vanillaShaped(cache, outputItem,
                hangingSignPattern,
                Map.of('X', ingredient(lumber), 'Y', tagIngredient("firma_compat:chains")),
                simpleResult(outputItem, 3),
                null);
    }
    protected void pressurePlateRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = wood.pressurePlate().asItem();

        vanillaShaped(cache, outputItem,
                pressurePlatePattern,
                Map.of('X', ingredient(lumber)),
                simpleResult(outputItem, 1),
                null);
    }
    protected void toolRackRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.TOOL_RACK).get().asItem();

        vanillaShaped(cache, outputItem,
                toolRackPattern,
                Map.of('X', ingredient(lumber)),
                simpleResult(outputItem, 1),
                null);
    }
    protected void loomRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.LOOM).get().asItem();

        vanillaShaped(cache, outputItem,
                loomPattern,
                Map.of('X', ingredient(lumber), 'Y', tagIngredient("forge:rods/wooden")),
                simpleResult(outputItem, 1),
                null);
    }
    protected void sluiceRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SLUICE).get().asItem();

        vanillaShaped(cache, outputItem,
                sluicePattern,
                Map.of('X', ingredient(lumber), 'Y', tagIngredient("forge:rods/wooden")),
                simpleResult(outputItem, 1),
                null);
    }
    protected void barrelRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.BARREL).get().asItem();

        vanillaShaped(cache, outputItem,
                barrelPattern,
                Map.of('X', ingredient(lumber)),
                simpleResult(outputItem, 1),
                null);
    }
    protected void sewingTableRecipe(CachedOutput cache, CompatWood wood) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SEWING_TABLE).get().asItem();

        vanillaShaped(cache, outputItem,
                sewingPattern,
                Map.of('Y', tagIngredient("forge:shears"), 'Z', ingredient("minecraft:leather"), 'A', ingredient(wood.planks().asItem()), 'B', ingredient(wood.log().asItem())),
                simpleResult(outputItem, 1),
                null);
    }
    protected void scribingTableRecipe(CachedOutput cache, CompatWood wood) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SCRIBING_TABLE).get().asItem();

        vanillaShaped(cache, outputItem,
                scribingPattern,
                Map.of('Y', tagIngredient("forge:feathers"), 'Z', tagIngredient("forge:dyes/black"), 'A', ingredient(wood.slab().asItem()), 'B', ingredient(wood.planks().asItem())),
                simpleResult(outputItem, 1),
                null);
    }
    protected void shelfRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SHELF).get().asItem();

        vanillaShaped(cache, outputItem,
                shelfPattern,
                Map.of('A', ingredient(wood.planks().asItem()) ,'X', ingredient(lumber), 'Y', tagIngredient("forge:rods/wooden")),
                simpleResult(outputItem, 2),
                null);
    }
    protected void axleRecipe(CachedOutput cache, CompatWood wood) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get().asItem();

        vanillaShaped(cache, outputItem,
                axlePattern,
                Map.of('A', ingredient(wood.strippedLog().asItem()) ,'B', ingredient("tfc:glue")),
                simpleResult(outputItem, 4),
                null);
    }
    protected void bladedAxleRecipe(CachedOutput cache, CompatWood wood) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.BLADED_AXLE).get().asItem();
        Item axle = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get().asItem();

        vanillaShaped(cache, outputItem,
                bladedAxlePattern,
                Map.of('A', ingredient(axle) ,'B', ingredient("tfc:metal/ingot/steel")),
                simpleResult(outputItem, 1),
                null);
    }
    protected void encasedAxleRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.ENCASED_AXLE).get().asItem();
        Item axle = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get().asItem();

        vanillaShaped(cache, outputItem,
                encasedAxlePattern,
                Map.of('A', ingredient(wood.log().asItem()) ,'B', ingredient(axle), 'X', ingredient(lumber)),
                simpleResult(outputItem, 4),
                null);
    }
    protected void clutchRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.CLUTCH).get().asItem();
        Item axle = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get().asItem();

        vanillaShaped(cache, outputItem,
                clutchPattern,
                Map.of('A', tagIngredient("minecraft:" + wood.getSerializedName() + "_logs"),'X', ingredient(lumber),'B', ingredient("tfc:brass_mechanisms"), 'C', ingredient(axle), 'D', ingredient("minecraft:redstone")),
                simpleResult(outputItem, 1),
                null);
    }
    protected void clutchStemRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.CLUTCH).get().asItem();
        Item axle = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get().asItem();

        vanillaShaped(cache, outputItem,
                clutchPattern,
                Map.of('A', tagIngredient("minecraft:" + wood.getSerializedName() + "_stems"),'X', ingredient(lumber),'B', ingredient("tfc:brass_mechanisms"), 'C', ingredient(axle), 'D', ingredient("minecraft:redstone")),
                simpleResult(outputItem, 1),
                null);
    }
    protected void gearBoxRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.GEAR_BOX).get().asItem();

        vanillaShaped(cache, outputItem,
                gearBoxPattern,
                Map.of('X', ingredient(lumber),'A', ingredient("tfc:brass_mechanisms")),
                simpleResult(outputItem, 2),
                null);
    }
    protected void waterWheelRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Item outputItem = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.WATER_WHEEL).get().asItem();
        Item axle = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get().asItem();

        vanillaShaped(cache, outputItem,
                waterWheelPattern,
                Map.of('X', ingredient(lumber),'A', ingredient(wood.planks().asItem()), 'B', ingredient(axle)),
                simpleResult(outputItem, 1),
                null);
    }
}