package com.bumppo109.firma_compat.datagen.recipes;

import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

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

            damageToolShapeless(cache, "from_logs", ("minecraft:" + woodName + "_logs"), "tfc:saws", lumber, 4);
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

            toolRackRecipe(cache, wood, lumber);
            sluiceRecipe(cache, wood, lumber);
            barrelRecipe(cache, wood, lumber);
            scribingTableRecipe(cache, wood);
            sewingTableRecipe(cache, wood);
            shelfRecipe(cache, wood, lumber);
            axleRecipe(cache, wood);
            bladedAxleRecipe(cache, wood);
            encasedAxleRecipe(cache, wood, lumber);
            clutchRecipe(cache, wood, lumber);
            gearBoxRecipe(cache, wood, lumber);
            waterWheelRecipe(cache, wood, lumber);

            damageInputsShaped(cache, woodName + "_support",
                    new String[]{"XX ", "Y  "},
                    Map.of(
                            'X', tagIngredient("minecraft:" + woodName + "_logs"),
                            'Y', tagIngredient("tfc:saws")
                    ),
                    simpleResult("firma_compat:" + woodName + "_support", 8));
        }


        return CompletableFuture.completedFuture(null);
    }

    protected String itemId(ItemLike itemLike) {
        return BuiltInRegistries.ITEM.getKey(itemLike.asItem()).getPath();
    }

    protected void damageToolShapeless(CachedOutput cache,
                                       @Nullable String suffix,
                                       ItemLike input,
                                       String toolTag,
                                       ItemLike output,
                                       int outputCount) {
        String recipeName = itemId(output);
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(output.asItem())).toString();

        JsonArray ingredients = new JsonArray();
        ingredients.add(ingredient(itemId(input)));      // Main input item
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
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.door().asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.door().asItem())).getPath();

        vanillaShaped(cache, outputName,
                doorPattern,
                Map.of('X', ingredient(lumberId)),
                simpleResult(outputId, 2),
                null);
    }
    protected void trapdoorRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.trapdoor().asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.trapdoor().asItem())).getPath();

        vanillaShaped(cache, outputName,
                trapdoorPattern,
                Map.of('X', ingredient(lumberId)),
                simpleResult(outputId, 3),
                null);
    }
    protected void planksRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.planks().asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.planks().asItem())).getPath();

        vanillaShaped(cache, outputName,
                planksPattern,
                Map.of('X', ingredient(lumberId)),
                simpleResult(outputId, 1),
                null);
    }
    protected void fenceRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.fence().asItem())).toString();
        String planksId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.planks().asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.fence().asItem())).getPath();

        vanillaShaped(cache, outputName,
                fencePattern,
                Map.of('X', ingredient(lumberId), 'Y', ingredient(planksId)),
                simpleResult(outputId, 8),
                null);
    }
    protected void fenceGateRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.fenceGate().asItem())).toString();
        String planksId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.planks().asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.fenceGate().asItem())).getPath();

        vanillaShaped(cache, outputName,
                fenceGatePattern,
                Map.of('X', ingredient(lumberId), 'Y', ingredient(planksId)),
                simpleResult(outputId, 2),
                null);
    }
    protected void signRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.sign().asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.sign().asItem())).getPath();

        vanillaShaped(cache, outputName,
                signPattern,
                Map.of('X', ingredient(lumberId), 'Y', tagIngredient("forge:rods/wooden")),
                simpleResult(outputId, 3),
                null);
    }
    protected void hangingSignRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.hangingSign().asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.hangingSign().asItem())).getPath();

        vanillaShaped(cache, outputName,
                hangingSignPattern,
                Map.of('X', ingredient(lumberId), 'Y', tagIngredient("firma_compat:chains")),
                simpleResult(outputId, 3),
                null);
    }
    protected void pressurePlateRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.pressurePlate().asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.pressurePlate().asItem())).getPath();

        vanillaShaped(cache, outputName,
                pressurePlatePattern,
                Map.of('X', ingredient(lumberId)),
                simpleResult(outputId, 1),
                null);
    }
    protected void toolRackRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.TOOL_RACK).get();
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                toolRackPattern,
                Map.of('X', ingredient(lumberId)),
                simpleResult(outputId, 1),
                null);
    }
    protected void loomRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.LOOM).get();
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                loomPattern,
                Map.of('X', ingredient(lumberId), 'Y', tagIngredient("forge:rods/wooden")),
                simpleResult(outputId, 1),
                null);
    }
    protected void sluiceRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SLUICE).get();
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                sluicePattern,
                Map.of('X', ingredient(lumberId), 'Y', tagIngredient("forge:rods/wooden")),
                simpleResult(outputId, 1),
                null);
    }
    protected void barrelRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.BARREL).get();
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                barrelPattern,
                Map.of('X', ingredient(lumberId)),
                simpleResult(outputId, 1),
                null);
    }
    protected void sewingTableRecipe(CachedOutput cache, CompatWood wood) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SEWING_TABLE).get();
        String plankId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.planks().asItem())).toString();
        String logId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.log().asItem())).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                sewingPattern,
                Map.of('Y', tagIngredient("forge:shears"), 'Z', ingredient("minecraft:leather"), 'A', ingredient(plankId), 'B', ingredient(logId)),
                simpleResult(outputId, 1),
                null);
    }
    protected void scribingTableRecipe(CachedOutput cache, CompatWood wood) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SCRIBING_TABLE).get();
        String plankId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.planks().asItem())).toString();
        String slabId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.slab().asItem())).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                scribingPattern,
                Map.of('Y', tagIngredient("forge:feathers"), 'Z', tagIngredient("forge:dyes/black"), 'A', ingredient(slabId), 'B', ingredient(plankId)),
                simpleResult(outputId, 1),
                null);
    }
    protected void shelfRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SHELF).get();
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String plankId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.planks().asItem())).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                shelfPattern,
                Map.of('A', ingredient(plankId) ,'X', ingredient(lumberId), 'Y', tagIngredient("forge:rods/wooden")),
                simpleResult(outputId, 2),
                null);
    }
    protected void axleRecipe(CachedOutput cache, CompatWood wood) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get();
        String strippedId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.strippedLog().asItem())).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                axlePattern,
                Map.of('A', ingredient(strippedId) ,'B', ingredient("tfc:glue")),
                simpleResult(outputId, 4),
                null);
    }
    protected void bladedAxleRecipe(CachedOutput cache, CompatWood wood) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.BLADED_AXLE).get();
        Block axleBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get();
        String axleId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(axleBlock.asItem())).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                bladedAxlePattern,
                Map.of('A', ingredient(axleId) ,'B', ingredient("tfc:metal/ingot/steel")),
                simpleResult(outputId, 1),
                null);
    }
    protected void encasedAxleRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.ENCASED_AXLE).get();
        Block axleBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get();
        String axleId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(axleBlock.asItem())).toString();
        String logId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.strippedLog().asItem())).toString();
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                encasedAxlePattern,
                Map.of('A', ingredient(logId) ,'B', ingredient(axleId), 'X', ingredient(lumberId)),
                simpleResult(outputId, 4),
                null);
    }
    protected void clutchRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.CLUTCH).get();
        Block axleBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get();
        String axleId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(axleBlock.asItem())).toString();
        String logId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.strippedLog().asItem())).toString();
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                clutchPattern,
                Map.of('A', tagIngredient(logId),'X', ingredient(lumberId),'B', ingredient("tfc:brass_mechanisms"), 'C', ingredient(axleId), 'D', ingredient("minecraft:redstone")),
                simpleResult(outputId, 1),
                null);
    }
    protected void gearBoxRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.GEAR_BOX).get();
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                gearBoxPattern,
                Map.of('X', ingredient(lumberId),'A', ingredient("tfc:brass_mechanisms")),
                simpleResult(outputId, 2),
                null);
    }
    protected void waterWheelRecipe(CachedOutput cache, CompatWood wood, Item lumber) {
        Block outputBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.WATER_WHEEL).get();
        Block axleBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get();
        String axleId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(axleBlock.asItem())).toString();
        String plankId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(wood.planks().asItem())).toString();
        String lumberId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lumber)).toString();
        String outputId = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).toString();
        String outputName = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(outputBlock.asItem())).getPath();

        vanillaShaped(cache, outputName,
                waterWheelPattern,
                Map.of('X', ingredient(lumberId),'A', ingredient(plankId), 'B', ingredient(axleId)),
                simpleResult(outputId, 1),
                null);
    }


    protected void toolHeadShaped(CachedOutput cache, String metal, String toolType) {
        String inputMetal = "tfc:metal/sheet/" + metal;
        String outputItem = modId + ":" + toolType + "_head/" + metal;

        advancedShaped(cache, toolType + "_head_" + metal,
                new String[]{" S ", " S ", "S  "},
                Map.of('S', ingredient(inputMetal)),
                copyInputResult(outputItem, 1),
                0, 0);   // input is usually one of the sheets
    }
}