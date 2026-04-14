package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FirmaCompat.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (CompatWood wood : CompatWood.VALUES){
            String woodName = wood.getSerializedName();

            Block twigBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.TWIG).get();
            Block logFenceBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.LOG_FENCE).get();
            Block verticalSupportBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.VERTICAL_SUPPORT).get();
            Block horizontalSupportBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.HORIZONTAL_SUPPORT).get();
            Block toolRackBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.TOOL_RACK).get();
            Block sluiceBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SLUICE).get();
            Block barrelBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.BARREL).get();
            Block loomBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.LOOM).get();
            Block scribingTableBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SCRIBING_TABLE).get();
            Block sewingTableBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SEWING_TABLE).get();
            Block shelfBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.SHELF).get();
            Block axleBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.AXLE).get();
            Block bladedAxleBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.BLADED_AXLE).get();
            Block encasedAxleBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.ENCASED_AXLE).get();
            Block clutchBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.CLUTCH).get();
            Block gearBoxBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.GEAR_BOX).get();
            Block waterWheelBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.WATER_WHEEL).get();
            Block windmillBlock = ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.WINDMILL).get();

            // Construct vanilla texture locations dynamically
            String logSuffix = switch(woodName){
                case "crimson", "warped" -> "stem";
                default -> "log";
            };
            ResourceLocation logSideTexture = mcLoc("block/" + woodName + "_" + logSuffix);
            ResourceLocation logTopTexture = mcLoc("block/" + woodName + "_" + logSuffix + "_top");
            ResourceLocation strippedLogSideTexture = mcLoc("block/stripped_" + woodName + "_" + logSuffix);
            ResourceLocation strippedLogTopTexture = mcLoc("block/stripped_" + woodName + "_" + logSuffix + "_top");
            ResourceLocation planksTexture = mcLoc("block/" + woodName + "_planks");

            ResourceLocation scribingParaphernaliaTexture = modLoc("block/template/scribing_paraphernalia");
            ResourceLocation axleCasingTexture = modLoc("block/template/axle_casing");
            ResourceLocation axleCasingFrontTexture = modLoc("block/template/axle_casing_front");
            ResourceLocation axleCasingRoundTexture = modLoc("block/template/axle_casing_round");
            ResourceLocation axleCasingUnpoweredTexture = modLoc("block/template/axle_casing_unpowered");
            ResourceLocation axleCasingPoweredTexture = modLoc("block/template/axle_casing_powered");

            twigBlock(twigBlock, logTopTexture, logSideTexture);
            logFenceBlock(logFenceBlock, logSideTexture, planksTexture);
            supportBlocks(woodName, verticalSupportBlock, horizontalSupportBlock, strippedLogSideTexture);
            toolRackBlock(toolRackBlock, planksTexture);

        }
    }

    private void emptyModel() {
        ModelFile emptyModel = new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath("tfc", "block/empty"));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    public void twigBlock(Block twigBlock, ResourceLocation logTopTexture, ResourceLocation logSideTexture){
        String blockName = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(twigBlock)).getPath();

        ModelFile twigModel = models()
                .withExistingParent("block/twig/" + blockName, modLoc("block/template/twig"))
                .texture("side", logSideTexture)
                .texture("top", logTopTexture);

        simpleBlock(twigBlock,
                ConfiguredModel.builder()
                        .modelFile(twigModel).rotationY(90).buildLast(),
                ConfiguredModel.builder()
                        .modelFile(twigModel).buildLast(),
                ConfiguredModel.builder()
                        .modelFile(twigModel).rotationY(180).buildLast(),
                ConfiguredModel.builder()
                        .modelFile(twigModel).rotationY(270).buildLast()
        );
    }

    public void logFenceBlock(Block logFenceBlock, ResourceLocation logSideTexture, ResourceLocation planksTexture) {
        String blockName = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(logFenceBlock)).getPath();

        ModelFile logFenceInventoryModel = models()
                .withExistingParent(("block/log_fence/" + blockName + "/inventory"), modLoc("block/template/log_fence_inventory"))
                .texture("log", logSideTexture)
                .texture("planks", planksTexture);
        ModelFile logFencePostModel = models()
                .withExistingParent(("block/log_fence/" + blockName + "/post"), mcLoc("block/fence_post"))
                .texture("texture", logSideTexture);
        ModelFile logFenceSideModel = models()
                .withExistingParent(("block/log_fence/" + blockName + "/side"), mcLoc("block/fence_side"))
                .texture("texture", planksTexture);

        getMultipartBuilder(logFenceBlock)
                // Always show the post
                .part()
                .modelFile(logFencePostModel)
                .addModel()
                .end()

                // North connection
                .part()
                .modelFile(logFenceSideModel)
                .uvLock(true)
                .addModel()
                .condition(BlockStateProperties.NORTH, true)
                .end()

                // East connection
                .part()
                .modelFile(logFenceSideModel)
                .rotationY(90)
                .uvLock(true)
                .addModel()
                .condition(BlockStateProperties.EAST, true)
                .end()

                // South connection
                .part()
                .modelFile(logFenceSideModel)
                .rotationY(180)
                .uvLock(true)
                .addModel()
                .condition(BlockStateProperties.SOUTH, true)
                .end()

                // West connection
                .part()
                .modelFile(logFenceSideModel)
                .rotationY(270)
                .uvLock(true)
                .addModel()
                .condition(BlockStateProperties.WEST, true)
                .end();
    }

    public void supportBlocks(String woodName, Block verticalSupportBlock, Block horizontalSupportBlock, ResourceLocation strippedLogSideTexture) {
        ModelFile supportConnectionModel = models()
                .withExistingParent(("block/support/" + woodName + "/connection"), modLoc("block/template/support/connection"))
                .texture("texture", strippedLogSideTexture)
                .texture("particle", strippedLogSideTexture);
        ModelFile supportHorizontalModel = models()
                .withExistingParent(("block/support/" + woodName + "/horizontal"), modLoc("block/template/support/horizontal"))
                .texture("texture", strippedLogSideTexture)
                .texture("particle", strippedLogSideTexture);
        ModelFile supportVerticalModel = models()
                .withExistingParent(("block/support/" + woodName + "/vertical"), modLoc("block/template/support/vertical"))
                .texture("texture", strippedLogSideTexture)
                .texture("particle", strippedLogSideTexture);
        ModelFile supportInventoryModel = models()
                .withExistingParent(("block/support/" + woodName + "/inventory"), modLoc("block/template/support/inventory"))
                .texture("texture", strippedLogSideTexture);

        getMultipartBuilder(verticalSupportBlock)
                // Always show the vertical beam
                .part()
                .modelFile(supportVerticalModel)
                .addModel()
                .end()

                // Connections
                .part()
                .modelFile(supportConnectionModel)
                .rotationY(270)
                .addModel()
                .condition(BlockStateProperties.NORTH, true)
                .end()
                .part()
                .modelFile(supportConnectionModel)
                .addModel()  // east: no rotation
                .condition(BlockStateProperties.EAST, true)
                .end()
                .part()
                .modelFile(supportConnectionModel)
                .rotationY(90)
                .addModel()
                .condition(BlockStateProperties.SOUTH, true)
                .end()
                .part()
                .modelFile(supportConnectionModel)
                .rotationY(180)
                .addModel()
                .condition(BlockStateProperties.WEST, true)
                .end();

        getMultipartBuilder(horizontalSupportBlock)
                // Always show the horizontal beam
                .part()
                .modelFile(supportHorizontalModel)
                .addModel()
                .end()

                // Connections — same rotations as vertical
                .part()
                .modelFile(supportConnectionModel)
                .rotationY(270)
                .addModel()
                .condition(BlockStateProperties.NORTH, true)
                .end()
                .part()
                .modelFile(supportConnectionModel)
                .addModel()
                .condition(BlockStateProperties.EAST, true)
                .end()
                .part()
                .modelFile(supportConnectionModel)
                .rotationY(90)
                .addModel()
                .condition(BlockStateProperties.SOUTH, true)
                .end()
                .part()
                .modelFile(supportConnectionModel)
                .rotationY(180)
                .addModel()
                .condition(BlockStateProperties.WEST, true)
                .end();
    }

    private void toolRackBlock(Block toolRackBlock, ResourceLocation planksTexture) {
        String blockName = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(toolRackBlock)).getPath();

        ModelFile toolRackModel = models()
                .withExistingParent(("block/tool_rack" + blockName), modLoc("block/template/tool_rack"))
                .texture("texture", planksTexture)
                .texture("particle", planksTexture);

        horizontalBlock(toolRackBlock, toolRackModel, 0);
    }

}