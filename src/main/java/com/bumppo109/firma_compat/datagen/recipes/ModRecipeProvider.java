package com.bumppo109.firma_compat.datagen.recipes;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addons.firmalife.CompatFLBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRItems;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRStoneType;
import com.bumppo109.firma_compat.block.*;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.fluid.ModFluids;
import com.bumppo109.firma_compat.item.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.therighthon.rnr.common.block.RNRBlocks;
import com.therighthon.rnr.common.item.RNRItems;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.common.items.HammerItem;
import net.dries007.tfc.common.recipes.ChiselRecipe;
import net.dries007.tfc.util.Metal;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceSink;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.stone_zone.api.set.stone.StoneType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.json.Json;
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
        //remove vanilla recipes
        falseRecipe(cache, "candle", null);
        falseRecipe(cache, "dried_kelp", null);
        for (String recipe : RemoveRecipes.REMOVE_RECIPE) {
            falseRecipe(cache, recipe, null);
        }
        for (DyeColor color : DyeColor.values()) {
            String colorName = color.getSerializedName();

            falseRecipe(cache, "dye_" + colorName + "_wool", null);
            falseRecipe(cache, "dye_" + colorName + "_carpet", null);
            falseRecipe(cache, "dye_" + colorName + "_bed", null);
            falseRecipe(cache, colorName + "_terracotta", null);
            falseRecipe(cache, colorName + "_candle", null);
        }

        JsonElement woodenRodIngredient = tagIngredient("forge:rods/wooden");

        for (CompatWood wood : CompatWood.VALUES) {
            JsonElement lumberIngredient = ingredient(ModItems.LUMBER.get(wood).get());
            JsonElement logIngredient = ingredient(wood.log().asItem());
            JsonElement strippedLogIngredient = ingredient(wood.strippedLog().asItem());
            JsonElement planksIngredient = ingredient(wood.planks().asItem());
            JsonElement axleIngredient = ingredient(ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get().asItem());

            Map<Character, JsonElement> simpleLumberMap = Map.of('X', lumberIngredient);

            if(wood.equals(CompatWood.WARPED) || wood.equals(CompatWood.CRIMSON)){
                damageInputsShapeless(cache,
                        new JsonElement[]{tagIngredient("minecraft:" + wood.getSerializedName() + "_stems"), tagIngredient("tfc:saws")},
                        ModItems.LUMBER.get(wood).get(), 8, "_from_log", null);
            } else {
                damageInputsShapeless(cache,
                        new JsonElement[]{tagIngredient("minecraft:" + wood.getSerializedName() + "_logs"), tagIngredient("tfc:saws")},
                        ModItems.LUMBER.get(wood).get(), 8, "_from_log", null);
            }
            damageInputsShapeless(cache,
                    new JsonElement[]{ingredient(wood.planks().asItem()), tagIngredient("tfc:saws")},
                    ModItems.LUMBER.get(wood).get(), 4, "_from_planks", null);
            damageInputsShapeless(cache,
                    new JsonElement[]{ingredient(wood.stair().asItem()), tagIngredient("tfc:saws")},
                    ModItems.LUMBER.get(wood).get(), 3, "_from_stairs", null);
            damageInputsShapeless(cache,
                    new JsonElement[]{ingredient(wood.slab().asItem()), tagIngredient("tfc:saws")},
                    ModItems.LUMBER.get(wood).get(), 2, "_from_slab", null);

            shapedRecipe(cache, wood.planks().asItem(), 1, planksPattern, simpleLumberMap);
            shapedRecipe(cache, wood.door().asItem(), 2, doorPattern, simpleLumberMap);
            shapedRecipe(cache, wood.trapdoor().asItem(), 3, trapdoorPattern, simpleLumberMap);
            shapedRecipe(cache, wood.pressurePlate().asItem(), 1, pressurePlatePattern, simpleLumberMap);
            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.BARREL).get().asItem(), 1, barrelPattern, simpleLumberMap);
            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.TOOL_RACK).get().asItem(), 1, toolRackPattern, simpleLumberMap);

            shapedRecipe(cache, wood.fence().asItem(), 8, fencePattern, Map.of('X', lumberIngredient, 'Y', logIngredient));
            shapedRecipe(cache, wood.fenceGate().asItem(), 2, fenceGatePattern, Map.of('X', lumberIngredient, 'Y', logIngredient));

            shapedRecipe(cache, wood.sign().asItem(), 3, signPattern, Map.of('X', lumberIngredient, 'Y', woodenRodIngredient));
            shapedRecipe(cache, wood.hangingSign().asItem(), 3, hangingSignPattern, Map.of('X', lumberIngredient, 'Y', tagIngredient("firma_compat:chains")));

            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.LOOM).get().asItem(),1,
                    loomPattern, Map.of('X', lumberIngredient, 'Z', woodenRodIngredient));
            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SLUICE).get().asItem(),1,
                    sluicePattern, Map.of('X', lumberIngredient, 'Z', woodenRodIngredient));

            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SHELF).get().asItem(),2,
                    shelfPattern, Map.of('Y', planksIngredient,'X', lumberIngredient, 'Z', woodenRodIngredient));

            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SEWING_TABLE).get().asItem(),1,
                    sewingPattern,
                    Map.of('Y', ingredient(Items.INK_SAC),'Z', ingredient(Items.FEATHER), 'A', planksIngredient, 'B', logIngredient));
            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SCRIBING_TABLE).get().asItem(),1,
                    scribingPattern,
                    Map.of('Y', ingredient(Items.INK_SAC),'Z', ingredient(Items.FEATHER), 'A', ingredient(wood.slab().asItem()), 'B', logIngredient));

            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get().asItem(),4,
                    axlePattern,
                    Map.of('A', strippedLogIngredient, 'B', ingredient("tfc:glue")));
            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.BLADED_AXLE).get().asItem(),1,
                    bladedAxlePattern,
                    Map.of('A', axleIngredient,'B', ingredient("tfc:metal/ingot/steel")));
            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.ENCASED_AXLE).get().asItem(),4,
                    encasedAxlePattern,
                    Map.of('A', strippedLogIngredient,'X', lumberIngredient, 'B', axleIngredient));
            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.CLUTCH).get().asItem(),2,
                    clutchPattern,
                    Map.of('A', strippedLogIngredient,'X', lumberIngredient,
                            'B', ingredient("tfc:brass_mechanisms"), 'C', axleIngredient, 'D', tagIngredient("forge:dusts/redstone")));
            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.GEAR_BOX).get().asItem(),2,
                    gearBoxPattern,
                    Map.of('A', ingredient("tfc:brass_mechanisms"),'X', lumberIngredient));
            shapedRecipe(cache, ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.WATER_WHEEL).get().asItem(),1,
                    waterWheelPattern,
                    Map.of('A', planksIngredient,'X', lumberIngredient, 'B', axleIngredient));

            chisel(cache, wood.planks(), wood.stair(), ChiselRecipe.Mode.STAIR);
            chisel(cache, wood.planks(), wood.slab(), ChiselRecipe.Mode.SLAB, ModItems.LUMBER.get(wood).get(), null, null);
        }
        //Wood Extra
        damageInputsShapeless(cache,
                new JsonElement[]{tagIngredient("minecraft:bamboo_blocks"), tagIngredient("tfc:saws")},
                Items.BAMBOO_PLANKS, 4, "_from_log", null);
        chisel(cache, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_SLAB, ChiselRecipe.Mode.SLAB, Items.BAMBOO, null, null);
        chisel(cache, Blocks.BAMBOO_MOSAIC, Blocks.BAMBOO_MOSAIC_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.BAMBOO_MOSAIC, Blocks.BAMBOO_MOSAIC_SLAB, ChiselRecipe.Mode.SLAB, Items.BAMBOO, null, null);

        for (CompatRock rock : CompatRock.VALUES) {
            Item brickItem;
            if (rock == CompatRock.NETHERRACK) {
                brickItem = Items.NETHER_BRICK;
            } else {
                brickItem = rock.brickItem();
            }
            JsonElement brickIngredient = ingredient(brickItem);
            JsonElement looseIngredient = ingredient(ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE).get().asItem());

            shapedRecipe(cache, rock.looseCobbleBlock().get().asItem(),1,
                    planksPattern,
                    Map.of('X', looseIngredient));
            shapedRecipe(cache, rock.hardenedCobbleBlock().get().asItem(),4,
                    new String[]{"ABA", "BAB", "ABA"},
                    Map.of('A', looseIngredient,'B', tagIngredient("tfc:mortar")));

            damageInputsShapeless(cache,
                    new JsonElement[]{looseIngredient, tagIngredient("tfc:chisels")},
                    brickItem, 1, null, null);
            shapedRecipe(cache, rock.bricksBlock().get().asItem(),4,
                    new String[]{"ABA", "BAB", "ABA"},
                    Map.of('A', brickIngredient,'B', tagIngredient("tfc:mortar")));
            if(!rock.equals(CompatRock.NETHERRACK)){
                shapedRecipe(cache, ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.BRICK_AQUEDUCT).get().asItem(),1,
                        new String[]{"A A", "BAB"},
                        Map.of('A', brickIngredient,'B', tagIngredient("tfc:mortar")));

            }
            if(rock != CompatRock.STONE && rock != CompatRock.DEEPSLATE && rock != CompatRock.BLACKSTONE && rock != CompatRock.END_STONE && rock != CompatRock.NETHERRACK){
                shapedRecipe(cache, rock.getSlab(CompatRock.BlockType.BRICK).get().asItem(),6,
                        new String[]{"AAA"},
                        Map.of('A', ingredient(rock.bricksBlock().get().asItem())));
                shapedRecipe(cache, rock.getStair(CompatRock.BlockType.BRICK).get().asItem(),8,
                        new String[]{"  A", " AA", "AAA"},
                        Map.of('A', ingredient(rock.bricksBlock().get().asItem())));
                shapedRecipe(cache, rock.getWall(CompatRock.BlockType.BRICK).get().asItem(),6,
                        new String[]{"AAA", "AAA"},
                        Map.of('A', ingredient(rock.bricksBlock().get().asItem())));

                chisel(cache, rock.bricksBlock().get(), rock.getStair(CompatRock.BlockType.BRICK).get(), ChiselRecipe.Mode.STAIR);
                chisel(cache, rock.bricksBlock().get(), rock.getSlab(CompatRock.BlockType.BRICK).get(), ChiselRecipe.Mode.SLAB);
            }

            oreCollapseToRaw(cache, ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE_COBBLE).get(), null, rock);

            var oreMap = ModBlocks.GRADED_ORES.get(rock);

            oreMap.forEach((ore, gradeMap) -> {
                gradeMap.forEach((grade, blockSupplier) -> {
                    if(grade.equals(CompatOre.Grade.RICH)) {
                        collapse(cache, oreMap.get(ore).get(grade).get(), oreMap.get(ore).get(CompatOre.Grade.NORMAL).get());
                    }
                    if(grade.equals(CompatOre.Grade.NORMAL)) {
                        collapse(cache, oreMap.get(ore).get(grade).get(), oreMap.get(ore).get(CompatOre.Grade.POOR).get());
                    }
                });
            });

            landslide(cache, ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE_COBBLE).get());
        }

        //Extra Aqueducts
        shapedRecipe(cache, ModBlocks.AQUEDUCTS.get(Aqueducts.BRICKS).get().asItem(),1,
                new String[]{"A A", "BAB"},
                Map.of('A', ingredient(Items.BRICK),'B', tagIngredient("tfc:mortar")));
        shapedRecipe(cache, ModBlocks.AQUEDUCTS.get(Aqueducts.NETHER_BRICKS).get().asItem(),1,
                new String[]{"A A", "BAB"},
                Map.of('A', ingredient(Items.NETHER_BRICK),'B', tagIngredient("tfc:mortar")));
        shapedRecipe(cache, ModBlocks.AQUEDUCTS.get(Aqueducts.PRISMARINE_BRICKS).get().asItem(),1,
                new String[]{"A A", "BAB"},
                Map.of('A', ingredient(ModItems.PRISMARINE_BRICK.get()),'B', tagIngredient("tfc:mortar")));
        shapedRecipe(cache, ModBlocks.AQUEDUCTS.get(Aqueducts.QUARTZ_BRICKS).get().asItem(),1,
                new String[]{"A A", "BAB"},
                Map.of('A', ingredient(ModItems.QUARTZ_BRICK.get()),'B', tagIngredient("tfc:mortar")));

        shapelessRecipe(cache,
                new JsonElement[]{ingredient(ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.BRICK_AQUEDUCT).get().asItem()),
                        ingredient(Items.MOSS_BLOCK)},
                ModBlocks.AQUEDUCTS.get(Aqueducts.MOSSY_STONE_BRICKS).get().asItem(), 1);
        shapelessRecipe(cache,
                new JsonElement[]{ingredient(ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.BRICK_AQUEDUCT).get().asItem()),
                        ingredient(Items.VINE)},
                ModBlocks.AQUEDUCTS.get(Aqueducts.MOSSY_STONE_BRICKS).get().asItem(), 1);

        //Extra Stone Crafting
        chiselCrafting(cache, ModItems.BRICK.get(CompatRock.STONE).get(), Items.STONE_BUTTON);
        chiselCrafting(cache, ModItems.BRICK.get(CompatRock.BLACKSTONE).get(), Items.POLISHED_BLACKSTONE_BUTTON);
        shapedRecipe(cache, Items.STONE_PRESSURE_PLATE,1,
                new String[]{"AA "},
                Map.of('A', ingredient(ModItems.BRICK.get(CompatRock.STONE).get())));
        shapedRecipe(cache, Items.POLISHED_BLACKSTONE_PRESSURE_PLATE,1,
                new String[]{"AA "},
                Map.of('A', ingredient(ModItems.BRICK.get(CompatRock.BLACKSTONE).get())));

        chiselCrafting(cache, Items.PRISMARINE_SHARD, ModItems.PRISMARINE_BRICK.get());
        shapedRecipe(cache, Items.PRISMARINE_BRICKS,4,
                new String[]{"ABA", "BAB", "ABA"},
                Map.of('A', ingredient(ModItems.PRISMARINE_BRICK.get()),'B', tagIngredient("tfc:mortar")));
        chiselCrafting(cache, Items.QUARTZ, ModItems.QUARTZ_BRICK.get());
        shapedRecipe(cache, Items.QUARTZ_BRICKS,4,
                new String[]{"ABA", "BAB", "ABA"},
                Map.of('A', ingredient(ModItems.QUARTZ_BRICK.get()),'B', tagIngredient("tfc:mortar")));

        chisel(cache, Blocks.STONE, Blocks.SMOOTH_STONE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.STONE, Blocks.STONE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.STONE, Blocks.STONE_SLAB, ChiselRecipe.Mode.SLAB, ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.LOOSE).get(), null, null);
        chisel(cache, Blocks.COBBLESTONE, Blocks.COBBLESTONE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.COBBLESTONE, Blocks.COBBLESTONE_SLAB, ChiselRecipe.Mode.SLAB, ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.LOOSE).get(), null, null);
        chisel(cache, Blocks.MOSSY_COBBLESTONE, Blocks.MOSSY_COBBLESTONE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.MOSSY_COBBLESTONE, Blocks.MOSSY_COBBLESTONE_SLAB, ChiselRecipe.Mode.SLAB, ModBlocks.ROCK_BLOCKS.get(CompatRock.STONE).get(CompatRock.BlockType.LOOSE).get(), null, null);
        chisel(cache, Blocks.STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.STONE_BRICKS, Blocks.STONE_BRICK_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.STONE_BRICKS, Blocks.STONE_BRICK_SLAB, ChiselRecipe.Mode.SLAB);
        chisel(cache, Blocks.MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICK_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.MOSSY_STONE_BRICKS, Blocks.MOSSY_STONE_BRICK_SLAB, ChiselRecipe.Mode.SLAB);
        chisel(cache, Blocks.SMOOTH_STONE, Blocks.SMOOTH_STONE_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.GRANITE, Blocks.POLISHED_GRANITE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.GRANITE, Blocks.GRANITE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.GRANITE, Blocks.GRANITE_SLAB, ChiselRecipe.Mode.SLAB, ModBlocks.ROCK_BLOCKS.get(CompatRock.GRANITE).get(CompatRock.BlockType.LOOSE).get(), null, null);
        chisel(cache, Blocks.POLISHED_GRANITE, Blocks.POLISHED_GRANITE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.POLISHED_GRANITE, Blocks.POLISHED_GRANITE_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.ANDESITE, Blocks.ANDESITE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.ANDESITE, Blocks.ANDESITE_SLAB, ChiselRecipe.Mode.SLAB, ModBlocks.ROCK_BLOCKS.get(CompatRock.ANDESITE).get(CompatRock.BlockType.LOOSE).get(), null, null);
        chisel(cache, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_ANDESITE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.POLISHED_ANDESITE, Blocks.POLISHED_ANDESITE_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.DIORITE, Blocks.POLISHED_DIORITE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.DIORITE, Blocks.DIORITE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.DIORITE, Blocks.DIORITE_SLAB, ChiselRecipe.Mode.SLAB, ModBlocks.ROCK_BLOCKS.get(CompatRock.DIORITE).get(CompatRock.BlockType.LOOSE).get(), null, null);
        chisel(cache, Blocks.POLISHED_DIORITE, Blocks.POLISHED_DIORITE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.POLISHED_DIORITE, Blocks.POLISHED_DIORITE_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.COBBLED_DEEPSLATE, Blocks.COBBLED_DEEPSLATE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.COBBLED_DEEPSLATE, Blocks.COBBLED_DEEPSLATE_SLAB, ChiselRecipe.Mode.SLAB, ModBlocks.ROCK_BLOCKS.get(CompatRock.DEEPSLATE).get(CompatRock.BlockType.LOOSE).get(), null, null);
        chisel(cache, Blocks.DEEPSLATE, Blocks.POLISHED_DEEPSLATE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.POLISHED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.POLISHED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE_SLAB, ChiselRecipe.Mode.SLAB);
        chisel(cache, Blocks.DEEPSLATE_BRICKS, Blocks.CHISELED_DEEPSLATE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICK_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICK_SLAB, ChiselRecipe.Mode.SLAB);
        chisel(cache, Blocks.DEEPSLATE_TILES, Blocks.DEEPSLATE_TILE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.DEEPSLATE_TILES, Blocks.DEEPSLATE_TILE_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.SANDSTONE, Blocks.SMOOTH_SANDSTONE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.SMOOTH_SANDSTONE, Blocks.CUT_SANDSTONE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.CUT_SANDSTONE, Blocks.CHISELED_SANDSTONE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.SANDSTONE, Blocks.SANDSTONE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.SANDSTONE, Blocks.SANDSTONE_SLAB, ChiselRecipe.Mode.SLAB);
        chisel(cache, Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_SANDSTONE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.SMOOTH_SANDSTONE, Blocks.SMOOTH_SANDSTONE_SLAB, ChiselRecipe.Mode.SLAB);
        chisel(cache, Blocks.CUT_SANDSTONE, Blocks.CUT_SANDSTONE_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.SMOOTH_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.CUT_RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.RED_SANDSTONE, Blocks.RED_SANDSTONE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.RED_SANDSTONE, Blocks.RED_SANDSTONE_SLAB, ChiselRecipe.Mode.SLAB);
        chisel(cache, Blocks.SMOOTH_RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.SMOOTH_RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE_SLAB, ChiselRecipe.Mode.SLAB);
        chisel(cache, Blocks.CUT_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.BASALT, Blocks.POLISHED_BASALT, ChiselRecipe.Mode.SMOOTH);

        chisel(cache, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.BLACKSTONE, Blocks.BLACKSTONE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.BLACKSTONE, Blocks.BLACKSTONE_SLAB, ChiselRecipe.Mode.SLAB, ModBlocks.ROCK_BLOCKS.get(CompatRock.BLACKSTONE).get(CompatRock.BlockType.LOOSE).get(), null, null);
        chisel(cache, Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.POLISHED_BLACKSTONE, Blocks.POLISHED_BLACKSTONE_SLAB, ChiselRecipe.Mode.SLAB);
        chisel(cache, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CHISELED_POLISHED_BLACKSTONE, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.END_STONE_BRICKS, Blocks.END_STONE_BRICK_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.END_STONE_BRICKS, Blocks.END_STONE_BRICK_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.BRICKS, Blocks.BRICK_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.BRICKS, Blocks.BRICK_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.MUD_BRICKS, Blocks.MUD_BRICK_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.MUD_BRICKS, Blocks.MUD_BRICK_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.NETHER_BRICKS, Blocks.CHISELED_NETHER_BRICKS, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.NETHER_BRICKS, Blocks.NETHER_BRICK_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.RED_NETHER_BRICKS, Blocks.RED_NETHER_BRICK_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.RED_NETHER_BRICKS, Blocks.RED_NETHER_BRICK_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.PRISMARINE_BRICKS, Blocks.DARK_PRISMARINE, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.PRISMARINE_BRICKS, Blocks.PRISMARINE_BRICK_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.PRISMARINE_BRICKS, Blocks.PRISMARINE_BRICK_SLAB, ChiselRecipe.Mode.SLAB);
        chisel(cache, Blocks.DARK_PRISMARINE, Blocks.DARK_PRISMARINE_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.DARK_PRISMARINE, Blocks.DARK_PRISMARINE_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.QUARTZ_BRICKS, Blocks.CHISELED_QUARTZ_BLOCK, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.SMOOTH_QUARTZ, Blocks.QUARTZ_BLOCK, ChiselRecipe.Mode.SMOOTH);
        chisel(cache, Blocks.SMOOTH_QUARTZ, Blocks.SMOOTH_QUARTZ_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.SMOOTH_QUARTZ, Blocks.SMOOTH_QUARTZ_SLAB, ChiselRecipe.Mode.SLAB);
        chisel(cache, Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.QUARTZ_BLOCK, Blocks.QUARTZ_SLAB, ChiselRecipe.Mode.SLAB);

        chisel(cache, Blocks.PURPUR_BLOCK, Blocks.PURPUR_STAIRS, ChiselRecipe.Mode.STAIR);
        chisel(cache, Blocks.PURPUR_BLOCK, Blocks.PURPUR_SLAB, ChiselRecipe.Mode.SLAB);

        //Stonecutting
        stonecutting(cache, Blocks.DEEPSLATE_BRICKS, Blocks.CHISELED_DEEPSLATE, 1, null);
        stonecutting(cache, Blocks.SANDSTONE, Blocks.SMOOTH_SANDSTONE, 1, null);
        stonecutting(cache, Blocks.RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE, 1, null);
        stonecutting(cache, Blocks.BASALT, Blocks.POLISHED_BASALT, 1, null);
        stonecutting(cache, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE, 1, null);
        stonecutting(cache, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CHISELED_POLISHED_BLACKSTONE, 1, null);
        stonecutting(cache, Blocks.PRISMARINE_BRICKS, Blocks.DARK_PRISMARINE, 1, null);
        stonecutting(cache, Blocks.SMOOTH_QUARTZ, Blocks.QUARTZ_BLOCK, 1, null);
        stonecutting(cache, Blocks.QUARTZ_BRICKS, Blocks.CHISELED_QUARTZ_BLOCK, 1, null);
        stonecutting(cache, Blocks.QUARTZ_BRICKS, Blocks.QUARTZ_PILLAR, 1, null);

        //Chisel Crafting Stone Blocks
        chiselCrafting(cache, Blocks.STONE, Blocks.SMOOTH_STONE);
        chiselCrafting(cache, Blocks.STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS);
        hammerCrafting(cache, Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS);

        chiselCrafting(cache, Blocks.DEEPSLATE, Blocks.POLISHED_DEEPSLATE);
        chiselCrafting(cache, Blocks.DEEPSLATE_BRICKS, Blocks.CHISELED_DEEPSLATE);
        hammerCrafting(cache, Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS);
        hammerCrafting(cache, Blocks.DEEPSLATE_TILES, Blocks.CRACKED_DEEPSLATE_TILES);

        chiselCrafting(cache, Blocks.ANDESITE, Blocks.POLISHED_ANDESITE);
        chiselCrafting(cache, Blocks.DIORITE, Blocks.POLISHED_DIORITE);
        chiselCrafting(cache, Blocks.GRANITE, Blocks.POLISHED_GRANITE);

        chiselCrafting(cache, Blocks.SANDSTONE, Blocks.SMOOTH_SANDSTONE);
        chiselCrafting(cache, Blocks.SMOOTH_SANDSTONE, Blocks.CUT_SANDSTONE);
        chiselCrafting(cache, Blocks.CUT_SANDSTONE, Blocks.CHISELED_SANDSTONE);

        chiselCrafting(cache, Blocks.RED_SANDSTONE, Blocks.SMOOTH_RED_SANDSTONE);
        chiselCrafting(cache, Blocks.SMOOTH_RED_SANDSTONE, Blocks.CUT_RED_SANDSTONE);
        chiselCrafting(cache, Blocks.CUT_RED_SANDSTONE, Blocks.CHISELED_RED_SANDSTONE);

        chiselCrafting(cache, Blocks.BASALT, Blocks.POLISHED_BASALT);

        chiselCrafting(cache, Blocks.BLACKSTONE, Blocks.POLISHED_BLACKSTONE);
        chiselCrafting(cache, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CHISELED_POLISHED_BLACKSTONE);
        hammerCrafting(cache, Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);

        hammerCrafting(cache, Blocks.NETHER_BRICKS, Blocks.CRACKED_NETHER_BRICKS);
        chiselCrafting(cache, Blocks.NETHER_BRICKS, Blocks.CHISELED_NETHER_BRICKS);

        chiselCrafting(cache, Blocks.SMOOTH_QUARTZ, Blocks.QUARTZ_BLOCK);
        chiselCrafting(cache, Blocks.QUARTZ_BRICKS, Blocks.QUARTZ_PILLAR);

        chiselCrafting(cache, Blocks.PRISMARINE_BRICKS, Blocks.DARK_PRISMARINE);

        //Natural Blocks
        landslide(cache, Blocks.PACKED_MUD);
        landslide(cache, Blocks.MUD);
        shapedRecipe(cache, Items.MUD_BRICKS, 1, planksPattern, Map.of('X', ingredient(ModItems.MUD_BRICK.get())));

        landslide(cache, ModBlocks.CLAY_GRASS_BLOCK.get());
        landslide(cache, ModBlocks.CLAY_PODZOL.get());
        landslide(cache, ModBlocks.CLAY_DIRT.get());

        landslide(cache, ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get());
        landslide(cache, ModBlocks.KAOLIN_CLAY_PODZOL.get());
        landslide(cache, ModBlocks.KAOLIN_CLAY_DIRT.get());
        landslide(cache, ModBlocks.COMPAT_FARMLAND.get(), Blocks.DIRT);
        landslide(cache, ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get());
        landslide(cache, ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get());
        landslide(cache, ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get());
        landslide(cache, ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get());

        landslide(cache, Blocks.GRASS_BLOCK, Blocks.DIRT);
        landslide(cache, Blocks.PODZOL, Blocks.DIRT);
        landslide(cache, Blocks.MYCELIUM, Blocks.DIRT);
        landslide(cache, Blocks.DIRT_PATH, Blocks.DIRT);
        landslide(cache, Blocks.COARSE_DIRT, Blocks.COARSE_DIRT);
        landslide(cache, Blocks.DIRT);
        landslide(cache, Blocks.FARMLAND, Blocks.DIRT);

        //Metal - melt, anvil, weld,
        heatingMetalRecipe(cache, CompatMetal.NETHERITE, Items.NETHERITE_SCRAP, 25);
        heatingMetalRecipe(cache, CompatMetal.NETHERITE, Items.NETHERITE_INGOT, 100);
        heatingMetalRecipe(cache, CompatMetal.NETHERITE, Items.NETHERITE_BLOCK, 100);

        heatingMetalRecipe(cache, CompatMetal.NETHERITE, Items.NETHERITE_HELMET, 600);
        heatingMetalRecipe(cache, CompatMetal.NETHERITE, Items.NETHERITE_CHESTPLATE, 800);
        heatingMetalRecipe(cache, CompatMetal.NETHERITE, Items.NETHERITE_LEGGINGS, 600);
        heatingMetalRecipe(cache, CompatMetal.NETHERITE, Items.NETHERITE_BOOTS, 400);

        heatingMetalRecipe(cache, CompatMetal.NETHERITE, Items.NETHERITE_SWORD, 200);
        heatingMetalRecipe(cache, CompatMetal.NETHERITE, Items.NETHERITE_PICKAXE, 100);
        heatingMetalRecipe(cache, CompatMetal.NETHERITE, Items.NETHERITE_AXE, 100);
        heatingMetalRecipe(cache, CompatMetal.NETHERITE, Items.NETHERITE_SHOVEL, 100);
        heatingMetalRecipe(cache, CompatMetal.NETHERITE, Items.NETHERITE_HOE, 100);

        for (CompatMetal metal : CompatMetal.values()) {
            var metalItems = ModItems.METAL_ITEMS.get(metal);
            if(metalItems != null && !metal.equals(CompatMetal.IRON)) {
                metalItems.forEach((type, reg) -> {
                    heatingMetalRecipe(cache, metal, type);
                    if(metal.hasParts()){
                        if(type.equals(CompatMetal.ItemType.ROD)){
                            generateAnvilRecipe(cache, tagIngredient("forge:ingots/" + metal.getSerializedName()), reg.get(), 1,
                                    metal.metalTier().ordinal(), forgingRules(type), false);
                        } else if(type.equals(CompatMetal.ItemType.SHEET)) {
                            generateAnvilRecipe(cache, tagIngredient("forge:double_ingots/" + metal.getSerializedName()), reg.get(), 1,
                                    metal.metalTier().ordinal(), forgingRules(type), false);
                        }
                    }
                    if(metal.hasTools()){
                        if(type.equals(CompatMetal.ItemType.SWORD_BLADE)){
                            generateAnvilRecipe(cache, tagIngredient("forge:double_ingots/" + metal.getSerializedName()), reg.get(), 1,
                                    metal.metalTier().ordinal(), forgingRules(type), true);
                        } else {
                            generateAnvilRecipe(cache, tagIngredient("forge:ingots/" + metal.getSerializedName()), reg.get(), 1,
                                    metal.metalTier().ordinal(), forgingRules(type), true);
                        }
                    }
                    if(metal.hasArmor()){
                        if(type.equals(CompatMetal.ItemType.UNFINISHED_BOOTS)){
                            generateAnvilRecipe(cache, tagIngredient("forge:double_sheets/" + metal.getSerializedName()), reg.get(), 1,
                                    metal.metalTier().ordinal(), forgingRules(type), false);
                        } else {
                            generateAnvilRecipe(cache, tagIngredient("forge:sheets/" + metal.getSerializedName()), reg.get(), 1,
                                    metal.metalTier().ordinal(), forgingRules(type), false);
                        }
                    }
                });
            }
            weldingRecipe(cache,
                    metal.ingot().get(),
                    metal.ingot().get(),
                    5,
                    ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.DOUBLE_INGOT).get()
            );
            weldingRecipe(cache,
                    ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.SHEET).get(),
                    ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.SHEET).get(),
                    5,
                    ModItems.METAL_ITEMS.get(metal).get(CompatMetal.ItemType.DOUBLE_SHEET).get()
            );
        }
        damageInputsShapeless(cache,
                new JsonElement[]{ingredient(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.SWORD_BLADE).get()),
                        tagIngredient("forge:rods/wooden")},
                Items.NETHERITE_SWORD, 1, null, null);
        damageInputsShapeless(cache,
                new JsonElement[]{ingredient(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.PICKAXE_HEAD).get()),
                        tagIngredient("forge:rods/wooden")},
                Items.NETHERITE_PICKAXE, 1, null, null);
        damageInputsShapeless(cache,
                new JsonElement[]{ingredient(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.AXE_HEAD).get()),
                        tagIngredient("forge:rods/wooden")},
                Items.NETHERITE_AXE, 1, null, null);
        damageInputsShapeless(cache,
                new JsonElement[]{ingredient(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.SHOVEL_HEAD).get()),
                        tagIngredient("forge:rods/wooden")},
                Items.NETHERITE_SHOVEL, 1, null, null);
        damageInputsShapeless(cache,
                new JsonElement[]{ingredient(ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.HOE_HEAD).get()),
                        tagIngredient("forge:rods/wooden")},
                Items.NETHERITE_HOE, 1, null, null);

        weldingRecipe(cache,
                ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.UNFINISHED_BOOTS).get(),
                ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.SHEET).get(),
                5,
                Items.NETHERITE_BOOTS
        );
        weldingRecipe(cache,
                ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.UNFINISHED_LEGGINGS).get(),
                ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.SHEET).get(),
                5,
                Items.NETHERITE_LEGGINGS
        );
        weldingRecipe(cache,
                ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.UNFINISHED_CHESTPLATE).get(),
                ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.DOUBLE_SHEET).get(),
                5,
                Items.NETHERITE_CHESTPLATE
        );
        weldingRecipe(cache,
                ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.UNFINISHED_HELMET).get(),
                ModItems.METAL_ITEMS.get(CompatMetal.NETHERITE).get(CompatMetal.ItemType.SHEET).get(),
                5,
                Items.NETHERITE_HELMET
        );

        //Misc Metal
        generateAnvilRecipe(cache,
                tagIngredient("forge:ingots/cast_iron"),
                Items.CHAIN, 16,
                3,
                CHAIN_RULES,
                false);

        heatingMetalRecipe(cache, Metal.Default.GOLD, Items.GOLD_BLOCK, 100, 1060);
        heatingMetalRecipe(cache, Metal.Default.GOLD, Items.GOLD_NUGGET, 10, 1060);

        heatingMetalRecipe(cache, Metal.Default.CAST_IRON, Items.CHAIN, 6, 1535);
        heatingMetalRecipe(cache, Metal.Default.CAST_IRON, Items.IRON_NUGGET, 10, 1535);

        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.COPPER_BLOCK, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.EXPOSED_COPPER, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WEATHERED_COPPER, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.OXIDIZED_COPPER, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_COPPER_BLOCK, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_EXPOSED_COPPER, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_WEATHERED_COPPER, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_OXIDIZED_COPPER, 100, 1080);

        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.CUT_COPPER, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.EXPOSED_CUT_COPPER, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WEATHERED_CUT_COPPER, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.OXIDIZED_CUT_COPPER, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_CUT_COPPER, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_EXPOSED_CUT_COPPER, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_WEATHERED_CUT_COPPER, 100, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_OXIDIZED_CUT_COPPER, 100, 1080);

        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.CUT_COPPER_STAIRS, 75, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.EXPOSED_CUT_COPPER_STAIRS, 75, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WEATHERED_CUT_COPPER_STAIRS, 75, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.OXIDIZED_CUT_COPPER_STAIRS, 75, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_CUT_COPPER_STAIRS, 75, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_EXPOSED_CUT_COPPER_STAIRS, 75, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_WEATHERED_CUT_COPPER_STAIRS, 75, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_OXIDIZED_CUT_COPPER_STAIRS, 75, 1080);

        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.CUT_COPPER_SLAB, 50, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.EXPOSED_CUT_COPPER_SLAB, 50, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WEATHERED_CUT_COPPER_SLAB, 50, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.OXIDIZED_CUT_COPPER_SLAB, 50, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_CUT_COPPER_SLAB, 50, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_EXPOSED_CUT_COPPER_SLAB, 50, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_WEATHERED_CUT_COPPER_SLAB, 50, 1080);
        heatingMetalRecipe(cache, Metal.Default.COPPER, Items.WAXED_OXIDIZED_CUT_COPPER_SLAB, 50, 1080);

        //Food
        heatingFoodRecipe(cache, Items.KELP, Items.DRIED_KELP);
        heatingFoodRecipe(cache, Items.SALMON, Items.COOKED_SALMON);
        heatingFoodRecipe(cache, Items.COD, Items.COOKED_COD);
        heatingFoodRecipe(cache, Items.BEEF, Items.COOKED_BEEF);
        heatingFoodRecipe(cache, Items.CHICKEN, Items.COOKED_CHICKEN);
        heatingFoodRecipe(cache, Items.MUTTON, Items.COOKED_MUTTON);
        heatingFoodRecipe(cache, Items.RABBIT, Items.COOKED_RABBIT);
        heatingFoodRecipe(cache, Items.PORKCHOP, Items.COOKED_PORKCHOP);
        heatingFoodRecipe(cache, Items.POTATO, Items.BAKED_POTATO);
        heatingFoodRecipe(cache, Items.KELP, Items.DRIED_KELP);
        heatingFoodRecipe(cache, Items.CHORUS_FRUIT, Items.POPPED_CHORUS_FRUIT);

        //Misc
        vanillaShaped(cache, new String[]{" X ", "ABA", "BCB"},
                Map.of('X', ingredient("tfc:metal/saw_blade/wrought_iron"), 'A', tagIngredient("tfc:lumber"), 'B', ingredient("firma_compat:stone_brick"), 'C', ingredient("tfc:brass_mechanisms")),
                Blocks.STONECUTTER.asItem(), 1,
                null);

        vanillaShaped(cache, new String[]{" X ", "ABA", "BBB"},
                Map.of('X', ingredient("minecraft:book"), 'A', ingredient("tfc:gem/diamond"), 'B', ingredient("minecraft:obsidian")),
                Blocks.ENCHANTING_TABLE.asItem(), 1,
                null);

        vanillaShaped(cache, new String[]{"AB "},
                Map.of('A', ingredient("minecraft:mud"), 'B', ingredient("tfc:straw")),
                ModBlocks.DRYING_MUD_BRICK.get().asItem(), 4,
                null);

        vanillaShaped(cache, new String[]{"XXX", " B ", " X "},
                Map.of('X', tagIngredient("firma_compat:lumber"), 'B', ingredient("minecraft:chiseled_bookshelf")),
                Blocks.LECTERN.asItem(), 1,
                null);

        vanillaShaped(cache, new String[]{"XXX", "BBB", "XXX"},
                Map.of('X', tagIngredient("firma_compat:lumber"), 'B', tagIngredient("forge:rods/wooden")),
                Blocks.CHISELED_BOOKSHELF.asItem(), 1,
                null);

        vanillaShaped(cache, new String[]{"  X", " XA", "X B"},
                Map.of('X', tagIngredient("forge:rods/wooden"), 'B', ingredient("minecraft:carrot"), 'A', tagIngredient("forge:string")),
                Items.CARROT_ON_A_STICK, 1,
                null);
        vanillaShaped(cache, new String[]{"  X", " XA", "X B"},
                Map.of('X', tagIngredient("forge:rods/wooden"), 'B', ingredient("minecraft:warped_fungus"), 'A', tagIngredient("forge:string")),
                Items.WARPED_FUNGUS_ON_A_STICK, 1,
                null);

        vanillaShaped(cache, new String[]{"XAX", "X X", "X X"},
                Map.of('X', tagIngredient("tfc:lumber"), 'A', tagIngredient("forge:string")),
                Items.SCAFFOLDING, 8,
                null);

        vanillaShaped(cache, planksPattern,
                Map.of('X', tagIngredient("minecraft:planks")),
                Items.CRAFTING_TABLE, 1,
                null);

        //RnR
        if(ModList.get().isLoaded("rnr")){
            for (CompatWood wood : CompatWood.VALUES) {
                chiselCrafting(cache, wood.log().asItem(), CompatRnRItems.WOOD_SHINGLE.get(wood).get(), 4, "rnr");

                blockModRecipe(cache, CompatRnRItems.WOOD_SHINGLE.get(wood).get(), RNRBlocks.ROOF_FRAME.get(), CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood).get(), null, "rnr");
                blockModRecipe(cache, CompatRnRItems.WOOD_SHINGLE.get(wood).get(), RNRBlocks.ROOF_FRAME_STAIRS.get(), CompatRnRBlocks.WOOD_SHINGLE_ROOF_STAIRS.get(wood).get(), null, "rnr");
                blockModRecipe(cache, CompatRnRItems.WOOD_SHINGLE.get(wood).get(), RNRBlocks.ROOF_FRAME_SLAB.get(), CompatRnRBlocks.WOOD_SHINGLE_ROOF_SLABS.get(wood).get(), null, "rnr");

                chisel(cache, CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood).get(), CompatRnRBlocks.WOOD_SHINGLE_ROOF_SLABS.get(wood).get(), ChiselRecipe.Mode.SLAB, "rnr");
                chisel(cache, CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood).get(), CompatRnRBlocks.WOOD_SHINGLE_ROOF_STAIRS.get(wood).get(), ChiselRecipe.Mode.STAIR, "rnr");

                vanillaShaped(cache, new String[]{"  X", " XX", "XXX"},
                        Map.of('X', ingredient(CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood).get().asItem())),
                        CompatRnRBlocks.WOOD_SHINGLE_ROOF_STAIRS.get(wood).get().asItem(), 8,
                        null);
                vanillaShaped(cache, new String[]{"XXX"},
                        Map.of('X', ingredient(CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood).get().asItem())),
                        CompatRnRBlocks.WOOD_SHINGLE_ROOF_SLABS.get(wood).get().asItem(), 6,
                        null);
            }
            for (CompatRock rock : CompatRock.VALUES) {
                chiselCrafting(cache, rock.rawBlock().get(), CompatRnRItems.FLAGSTONE.get(rock).get(), 4, "rnr");
            }
            CompatRnRBlocks.ROCK_BLOCKS.forEach((rock, typeMap) -> {
                typeMap.forEach((road, blockSupplier) -> {
                    Item inputItem = null;
                    if(road.equals(CompatRnRStoneType.FLAGSTONES)){
                        inputItem = CompatRnRItems.FLAGSTONE.get(rock).get().asItem();
                    }else if(road.equals(CompatRnRStoneType.COBBLED_ROAD)){
                        inputItem = ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE).get().asItem();
                    }else if(road.equals(CompatRnRStoneType.SETT_ROAD)){
                        inputItem = ModItems.BRICK.get(rock).get();
                    }
                    if(inputItem != null){
                        blockModRecipe(cache, inputItem, RNRBlocks.BASE_COURSE.get(), blockSupplier.get(), null, "rnr");
                    }
                    landslide(cache, blockSupplier.get(), "rnr");
                });
            });
            CompatRnRBlocks.ROCK_STAIRS.forEach((rock, typeMap) -> {
                typeMap.forEach((road, blockSupplier) -> {
                    mattock(cache, CompatRnRBlocks.ROCK_BLOCKS.get(rock).get(road).get(), blockSupplier.get(), ChiselRecipe.Mode.STAIR, null);

                    landslide(cache, blockSupplier.get(), CompatRnRBlocks.ROCK_SLABS.get(rock).get(road).get(), "rnr");
                });
            });
            CompatRnRBlocks.ROCK_SLABS.forEach((rock, typeMap) -> {
                typeMap.forEach((road, blockSupplier) -> {
                    mattock(cache, CompatRnRBlocks.ROCK_BLOCKS.get(rock).get(road).get(), blockSupplier.get(), ChiselRecipe.Mode.SLAB, null);

                    landslide(cache, blockSupplier.get(), "rnr");
                });
            });
            mattock(cache, Blocks.MUD, CompatRnRBlocks.TAMPED_MUD.get(), ChiselRecipe.Mode.SMOOTH, null);
            mattock(cache, Blocks.DIRT, CompatRnRBlocks.TAMPED_DIRT.get(), ChiselRecipe.Mode.SMOOTH, "from_dirt");
            mattock(cache, Blocks.GRASS_BLOCK, CompatRnRBlocks.TAMPED_DIRT.get(), ChiselRecipe.Mode.SMOOTH, "from_grass_block");
            mattock(cache, Blocks.PODZOL, CompatRnRBlocks.TAMPED_DIRT.get(), ChiselRecipe.Mode.SMOOTH, "from_podzol");
            mattock(cache, Blocks.MYCELIUM, CompatRnRBlocks.TAMPED_DIRT.get(), ChiselRecipe.Mode.SMOOTH, "from_mycelium");
            mattock(cache, Blocks.COARSE_DIRT, CompatRnRBlocks.TAMPED_DIRT.get(), ChiselRecipe.Mode.SMOOTH, "from_coarse_dirt");

            blockModRecipe(cache, RNRItems.CRUSHED_BASE_COURSE.get(), CompatRnRBlocks.TAMPED_DIRT.get(), RNRBlocks.BASE_COURSE.get(), "from_tamped_dirt", "rnr");
            blockModRecipe(cache, RNRItems.CRUSHED_BASE_COURSE.get(), CompatRnRBlocks.TAMPED_MUD.get(), RNRBlocks.BASE_COURSE.get(), "from_tamped_mud", "rnr");
            blockModRecipe(cache, CompatRnRItems.GRAVEL_FILL.get(), RNRBlocks.BASE_COURSE.get(), CompatRnRBlocks.GRAVEL_ROAD.get(), null, "rnr");
            blockModRecipe(cache, CompatRnRItems.GRAVEL_FILL.get(), CompatRnRBlocks.GRAVEL_ROAD.get(), CompatRnRBlocks.OVER_HEIGHT_GRAVEL.get(), null, "rnr");
            mattock(cache, CompatRnRBlocks.OVER_HEIGHT_GRAVEL.get(), CompatRnRBlocks.MACADAM_ROAD.get(), ChiselRecipe.Mode.SMOOTH, null);

            mattock(cache, CompatRnRBlocks.MACADAM_ROAD.get(), CompatRnRBlocks.MACADAM_ROAD_STAIRS.get(), ChiselRecipe.Mode.STAIR, null);
            mattock(cache, CompatRnRBlocks.MACADAM_ROAD.get(), CompatRnRBlocks.MACADAM_ROAD_SLAB.get(), ChiselRecipe.Mode.SLAB, null);

            mattock(cache, CompatRnRBlocks.GRAVEL_ROAD.get(), CompatRnRBlocks.GRAVEL_ROAD_STAIRS.get(), ChiselRecipe.Mode.STAIR, null);
            mattock(cache, CompatRnRBlocks.GRAVEL_ROAD.get(), CompatRnRBlocks.GRAVEL_ROAD_SLAB.get(), ChiselRecipe.Mode.SLAB, null);
        }

        for (CompatWood wood : CompatWood.VALUES){
            barrelPressRecipe(cache, wood);
            stompingBarrelRecipe(cache, wood);
            bigBarrelRecipe(cache, wood);
            wineShelfRecipe(cache, wood);
            foodShelfRecipe(cache, wood);
            jarbnetRecipe(cache, wood);
            hangerRecipe(cache, wood);
        }
        for (CompatRock rock : CompatRock.VALUES) {
            Block poorOre = CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.POOR).get();
            Block normalOre = CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.NORMAL).get();
            Block richOre = CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.RICH).get();

            collapse(cache, poorOre, ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE_COBBLE).get(), "firmalife");
            collapse(cache, normalOre, poorOre, "firmalife");
            collapse(cache, richOre, normalOre, "firmalife");
        }

        return CompletableFuture.completedFuture(null);
    }

    protected String[] doorPattern = new String[]{"XX ", "XX ", "XX "};
    protected String[] trapdoorPattern = new String[]{"XXX", "XXX"};
    protected String[] planksPattern = new String[]{"XX ", "XX "};
    protected String[] pressurePlatePattern = new String[]{"XX "};
    protected String[] toolRackPattern = new String[]{"XXX", "   ", "XXX"};
    protected String[] barrelPattern = new String[]{"X X", "X X", "XXX"};
    protected String[] fenceGatePattern = new String[]{"XYX", "XYX"};
    protected String[] fencePattern = new String[]{"YXY", "YXY"};
    protected String[] signPattern = new String[]{"XXX", "XXX", " Y "};
    protected String[] hangingSignPattern = new String[]{"Y Y", "XXX", "XXX"};
    protected String[] loomPattern = new String[]{"XXX", "XZX", "X X"};
    protected String[] sluicePattern = new String[]{"  Z", " ZX", "ZXX"};
    protected String[] shelfPattern = new String[]{"YYY", "X X", "Z Z"};
    protected String[] scribingPattern = new String[]{"Y Z", "AAA", "B B"};
    protected String[] sewingPattern = new String[]{" YZ", "AAA", "B B"};
    protected String[] axlePattern = new String[]{"ABA"};
    protected String[] bladedAxlePattern = new String[]{"AB"};
    protected String[] encasedAxlePattern = new String[]{" A ", "XBX", " A "};
    protected String[] clutchPattern = new String[]{"XAX", "BCD", "XAX"};
    protected String[] gearBoxPattern = new String[]{" X ", "XAX", " X "};
    protected String[] waterWheelPattern = new String[]{"XAX", "ABA", "XAX"};

    protected void shapedRecipe(CachedOutput cache, Item output, Integer count, String[] pattern, Map<Character, JsonElement> key) {
        vanillaShaped(cache,
                pattern,
                key,
                output,
                count,
                null);
    }

    protected void shapelessRecipe(CachedOutput cache, JsonElement[] itemArray, Item output, Integer count) {
        vanillaShapeless(cache,
                itemArray,
                output,
                count,
                null, null, null);
    }

    protected void shapelessRecipe(CachedOutput cache, JsonElement[] itemArray, Item output, Integer count, @Nullable String suffix, @Nullable String requiredMod) {
        vanillaShapeless(cache,
                itemArray,
                output,
                count,
                null, suffix, requiredMod);
    }

    //TFC Recipes
    protected void heatingFoodRecipe(CachedOutput cache, ItemLike input, ItemLike output) {
        ResourceLocation inputKey = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(input.asItem()));
        String name = inputKey.getPath();

        JsonObject ingredient = ingredient(input.asItem());
        JsonObject result = ingredient(output.asItem());

        heatingRecipe(cache, name, "result_item", ingredient, result, 200,null, null);
    }

    protected void heatingMetalRecipe(CachedOutput cache, CompatMetal metal, CompatMetal.ItemType itemType) {
        heatingMetalRecipe(cache, metal, ModItems.METAL_ITEMS.get(metal).get(itemType).get(), (int) itemType.metalAmount());
    }

    protected void heatingMetalRecipe(CachedOutput cache, CompatMetal metal, ItemLike input, int amount) {
        ResourceLocation inputKey = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(input.asItem()));
        Fluid fluid = ModFluids.METALS.get(metal).getSource();
        String name = inputKey.getPath();

        JsonObject ingredient = ingredient(input.asItem());
        JsonObject result = fluidIngredient(fluid, amount);

        heatingRecipe(cache, name, "result_fluid", ingredient, result, (int) metal.meltTemp(), null, null);
    }

    protected void heatingMetalRecipe(CachedOutput cache, Metal.Default metal, ItemLike input, int amount, int meltTemp) {
        ResourceLocation inputKey = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(input.asItem()));
        Fluid fluid = TFCFluids.METALS.get(metal).getSource();
        String name = inputKey.getPath();

        JsonObject ingredient = ingredient(input.asItem());
        JsonObject result = fluidIngredient(fluid, amount);

        heatingRecipe(cache, name, "result_fluid", ingredient, result, meltTemp, null, null);
    }

    protected void chisel(CachedOutput cache, Block ingredient, Block result, ChiselRecipe.Mode mode, String requiredMod) {
        chisel(cache, ingredient, result, mode, null, null, requiredMod);
    }

    protected void chisel(CachedOutput cache, Block ingredient, Block result, ChiselRecipe.Mode mode) {
        chisel(cache, ingredient, result, mode, null, null, null);
    }

    protected void hammerCrafting(CachedOutput cache, ItemLike input, ItemLike result) {

        JsonElement[] itemArray = new JsonElement[]{ingredient(input.asItem()), tagIngredient("tfc:hammers")};

        damageInputsShapeless(cache, itemArray, result.asItem(), 1, null, null);
    }

    protected void chiselCrafting(CachedOutput cache, ItemLike input, ItemLike result, int count, String requiredMod) {

        JsonElement[] itemArray = new JsonElement[]{ingredient(input.asItem()), tagIngredient("tfc:chisels")};

        damageInputsShapeless(cache, itemArray, result.asItem(), count, null, requiredMod);
    }

    protected void chiselCrafting(CachedOutput cache, ItemLike input, ItemLike result) {

        JsonElement[] itemArray = new JsonElement[]{ingredient(input.asItem()), tagIngredient("tfc:chisels")};

        damageInputsShapeless(cache, itemArray, result.asItem(), 1, null, null);
    }

    protected void damageInputsShapeless(CachedOutput cache, ItemLike input, String toolTag, ItemLike result, Integer count, @Nullable String requiredMod) {

        JsonElement[] itemArray = new JsonElement[]{ingredient(input.asItem()), tagIngredient(toolTag)};

        damageInputsShapeless(cache, itemArray, result.asItem(), count, null, requiredMod);
    }

    protected void damageInputsShapeless(CachedOutput cache, JsonElement item, JsonElement toolTag, Item result, Integer count, @Nullable String suffix, @Nullable String requiredMod) {

        JsonElement[] itemArray = new JsonElement[]{item, toolTag};

        damageInputsShapeless(cache, itemArray, result, count, suffix, requiredMod);
    }

    //Anvil Forging Rules
    public String[] forgingRules(CompatMetal.ItemType itemType) {
        return switch (itemType) {
            case PICKAXE_HEAD -> PICKAXE_HEAD_RULES;
            case SHOVEL_HEAD -> SHOVEL_HEAD_RULES;
            case AXE_HEAD -> AXE_HEAD_RULES;
            case HOE_HEAD -> HOE_HEAD_RULES;
            case UNFINISHED_HELMET -> UNFINISHED_HELMET_RULES;
            case UNFINISHED_CHESTPLATE -> UNFINISHED_CHESTPLATE_RULES;
            case UNFINISHED_LEGGINGS -> UNFINISHED_GREAVES_RULES;
            case UNFINISHED_BOOTS -> UNFINISHED_BOOTS_RULES;
            default -> HIT_3X;
        };
    }

    private static final String[] HIT_3X = {
            "hit_third_last",
            "hit_second_last",
            "hit_last"
    };

    private static final String[] PICKAXE_HEAD_RULES = {
            "draw_not_last",
            "bend_not_last",
            "punch_last"
    };

    private static final String[] SHOVEL_HEAD_RULES = {
            "hit_not_last",
            "punch_last"
    };

    private static final String[] AXE_HEAD_RULES = {
            "upset_third_last",
            "hit_second_last",
            "punch_last"
    };

    private static final String[] HOE_HEAD_RULES = {
            "bend_not_last",
            "hit_not_last",
            "punch_last"
    };

    private static final String[] UNFINISHED_HELMET_RULES = {
            "bend_third_last",
            "bend_second_last",
            "hit_last"
    };

    private static final String[] UNFINISHED_CHESTPLATE_RULES = {
            "upset_third_last",
            "hit_second_last",
            "hit_last"
    };

    private static final String[] UNFINISHED_GREAVES_RULES = {
            "bend_any",
            "draw_any",
            "hit_any"
    };

    private static final String[] UNFINISHED_BOOTS_RULES = {
            "shrink_third_last",
            "bend_second_last",
            "bend_last"
    };

    private static final String[] TRAPDOOR_RULES = {
            "bend_last","draw_second_last","draw_third_last"
    };

    private static final String[] CHAIN_RULES = {
            "hit_any","hit_any","draw_last",
    };

    private static final String[] BARS_RULES = {
            "punch_third_last", "punch_second_last", "upset_last",
    };

    protected void oreCollapseToRaw(
            CachedOutput cache,
            Block resultBlock,
            @Nullable String recipeSuffix,
            CompatRock compatRock
    ) {

        ResourceLocation resultKey = ForgeRegistries.BLOCKS.getKey(resultBlock);
        if (resultKey == null) throw new IllegalStateException("Missing result block registry name");

        String resultId = resultKey.toString();
        String name = resultKey.getPath();

        JsonArray ingredients = new JsonArray();

        addBlock(ingredients, compatRock.rawBlock());
        addBlock(ingredients, ModBlocks.ROCK_BLOCKS.get(compatRock).get(CompatRock.BlockType.HARDENED));

        var normalOres = ModBlocks.ORES.get(compatRock);
        if (normalOres != null) {
            normalOres.forEach((ore, supplier) -> addBlock(ingredients, supplier));
        }

        var graded = ModBlocks.GRADED_ORES.get(compatRock);
        if (graded != null) {
            graded.forEach((ore, map) -> addBlock(ingredients, map.get(CompatOre.Grade.POOR)));
        }

        collapseOrLandslide(cache, "collapse", name, recipeSuffix, ingredients, resultId, null);
    }

    protected void collapse(CachedOutput cache, Block input, Block output, String requiredMod) {
        ResourceLocation inputRes = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(input));

        String suffix = "_from_" + inputRes.getPath();

        collapseOrLandslide(cache, "collapse", input, output, suffix, requiredMod);
    }

    protected void collapse(CachedOutput cache, Block input, Block output) {
        ResourceLocation inputRes = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(input));

        String suffix = "_from_" + inputRes.getPath();

        collapseOrLandslide(cache, "collapse", input, output, suffix,null);
    }

    protected void collapse(CachedOutput cache, Block block) {
        collapseOrLandslide(cache, "collapse", block, block, null,null);
    }

    protected void landslide(CachedOutput cache, Block input, Block output, String requiredMod) {
        ResourceLocation inputRes = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(input));

        String suffix = "_from_" + inputRes.getPath();

        collapseOrLandslide(cache, "landslide", input, output, suffix, requiredMod);
    }

    protected void landslide(CachedOutput cache, Block input, Block output) {
        ResourceLocation inputRes = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(input));

        String suffix = "_from_" + inputRes.getPath();

        collapseOrLandslide(cache, "landslide", input, output, suffix,null);
    }

    protected void landslide(CachedOutput cache, Block block, String requiredMod) {
        collapseOrLandslide(cache, "landslide", block, block, null, requiredMod);
    }

    protected void landslide(CachedOutput cache, Block block) {
        collapseOrLandslide(cache, "landslide", block, block, null,null);
    }


// ================== RNR ==================
    protected void mattock(CachedOutput cache, Block input, Block output, ChiselRecipe.Mode mode, @Nullable String suffix) {
        ResourceLocation inputRes = Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(input));
        ResourceLocation outputRes = Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(output));

        mattockRecipe(
                cache,
                outputRes.getPath(),
                inputRes.toString(),
                outputRes.toString(),
                mode,
                null,
                null,
                suffix,
                "rnr"
        );
    }

// ================== FIRMALIFE ==================
    protected void barrelPressRecipe(CachedOutput cache, CompatWood wood) {
        Block result = CompatFLBlocks.BARREL_PRESSES.get(wood).get();
        Block barrel = CompatFLBlocks.STOMPING_BARRELS.get(wood).get();

        vanillaShaped(cache,
                new String[]{"AB ", "CD "} ,
                Map.of(
                        'A', tagIngredient("forge:rods/wrought_iron"),
                        'B', ingredient(barrel.asItem()),
                        'C', tagIngredient("forge:sheets/wrought_iron"),
                        'D', ingredient("tfc:brass_mechanisms")
                ),
                result.asItem(),1,
                "firmalife");
    }

    protected void hangerRecipe(CachedOutput cache, CompatWood wood) {
        Block block = CompatFLBlocks.HANGERS.get(wood).get();

        vanillaShaped(cache,
                new String[]{"AAA", " B ", " B "},
                Map.of(
                        'A', ingredient(wood.planks().asItem()),
                        'B', tagIngredient("forge:string")
                ),
                block.asItem(),1,
                "firmalife");
    }

    protected void jarbnetRecipe(CachedOutput cache, CompatWood wood) {
        Block block = CompatFLBlocks.JARBNETS.get(wood).get();

        vanillaShaped(cache,
                new String[]{"X  ", "BAA", "X  "},
                Map.of(
                        'A', ingredient(ModItems.LUMBER.get(wood).get()),
                        'B', tagIngredient("forge:rods/brass"),
                        'X', ingredient(wood.log().asItem())
                ),
                block.asItem(), 2,
                "firmalife");
    }

    protected void bigBarrelRecipe(CachedOutput cache, CompatWood wood) {
        Block block = CompatFLBlocks.BIG_BARRELS.get(wood).get();

        vanillaShaped(cache,
                new String[]{"XAX", "ABA", "XAX"},
                Map.of(
                        'A', ingredient("firmalife:barrel_stave"),
                        'B', ingredient("tfc:glue"),
                        'X', ingredient(wood.log().asItem())
                ),
                block.asItem(),1,
                "firmalife");
    }

    protected void foodShelfRecipe(CachedOutput cache, CompatWood wood) {
        Block block = CompatFLBlocks.FOOD_SHELVES.get(wood).get();

        vanillaShaped(cache,
                new String[]{"AAA", "BBB", "AAA"},
                Map.of(
                        'A', ingredient(wood.planks().asItem()),
                        'B', ingredient(ModItems.LUMBER.get(wood).get())
                ),
                block.asItem(),1,
                "firmalife");
    }

    protected void stompingBarrelRecipe(CachedOutput cache, CompatWood wood) {
        Block block = CompatFLBlocks.STOMPING_BARRELS.get(wood).get();

        vanillaShaped(cache,
                new String[]{"ABA", "AAA", "BBB"},
                Map.of(
                        'A', ingredient(ModItems.LUMBER.get(wood).get()),
                        'B', ingredient("tfc:glue")
                ),
                block.asItem(),1,
                "firmalife");
    }

    protected void wineShelfRecipe(CachedOutput cache, CompatWood wood) {
        Block block = CompatFLBlocks.WINE_SHELVES.get(wood).get();

        vanillaShaped(cache,
                new String[]{"ABA", "ABA", "ABA"},
                Map.of(
                        'A', ingredient(wood.log().asItem()),
                        'B', ingredient("firmalife:treated_lumber")
                ),
                block.asItem(),1,
                "firmalife");
    }

    protected void falseRecipe(CachedOutput cache, String resLoc, @Nullable String requiredMod) {
        JsonObject json = new JsonObject();

        JsonObject falseCondition = new JsonObject();
        falseCondition.addProperty("type", "forge:false");

        JsonArray falseArray = new JsonArray();
        falseArray.add(falseCondition);

        json.add("conditions", falseArray);

        saveMCRecipe(cache, resLoc, json, modLoadedCondition(requiredMod));
    }
}