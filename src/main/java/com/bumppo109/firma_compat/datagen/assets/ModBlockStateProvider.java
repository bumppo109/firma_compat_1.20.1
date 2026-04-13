package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.dries007.tfc.common.blocks.devices.SluiceBlock;
import net.dries007.tfc.common.blocks.devices.BarrelBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FirmaCompat.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (CompatWood wood : CompatWood.VALUES) {
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

            //Empty Model
            ModelFile emptyModel = new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath("tfc", "block/empty"));

            // Construct vanilla texture locations dynamically
            String logSuffix = switch(woodName){
                case "crimson", "warped" -> "stem";
                default -> "log";
            };
            ResourceLocation logSideTexture = mcLoc("block/" + woodName + "_" + logSuffix);
            ResourceLocation logTopTexture = mcLoc("block/" + woodName + "_" + logSuffix + "_top");
            ResourceLocation strippedLogSideTexture = mcLoc("block/stripped_" + woodName + "_" + logSuffix);
            ResourceLocation strippedLogTopTexture = mcLoc("block/stripped_" + woodName + "_" + logSuffix + "_top");
            ResourceLocation planksTexture = wood.planksTexture();

            ResourceLocation scribingParaphernaliaTexture = modLoc("block/template/scribing_paraphernalia");
            ResourceLocation axleCasingTexture = modLoc("block/template/axle_casing");
            ResourceLocation axleCasingFrontTexture = modLoc("block/template/axle_casing_front");
            ResourceLocation axleCasingRoundTexture = modLoc("block/template/axle_casing_round");
            ResourceLocation axleCasingUnpoweredTexture = modLoc("block/template/axle_casing_unpowered");
            ResourceLocation axleCasingPoweredTexture = modLoc("block/template/axle_casing_powered");

            //Twig
            ModelFile twigModel = models()
                    .withExistingParent(("block/twig/" + woodName), modLoc("block/template/twig"))
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

            //Log Fence
            ModelFile logFenceInventoryModel = models()
                    .withExistingParent(("block/log_fence/" + woodName + "/inventory"), modLoc("block/template/log_fence_inventory"))
                    .texture("log", logSideTexture)
                    .texture("planks", planksTexture);
            ModelFile logFencePostModel = models()
                    .withExistingParent(("block/log_fence/" + woodName + "/post"), mcLoc("block/fence_post"))
                    .texture("texture", logSideTexture);
            ModelFile logFenceSideModel = models()
                    .withExistingParent(("block/log_fence/" + woodName + "/side"), mcLoc("block/fence_side"))
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

            //TODO - Vertical Support
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

            //TODO - Horizontal Support
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

            //TODO - Tool Rack
            ModelFile toolRackModel = models()
                    .withExistingParent(("block/tool_rack/" + woodName), modLoc("block/template/tool_rack"))
                    .texture("texture", planksTexture)
                    .texture("particle", planksTexture);

            horizontalBlock(toolRackBlock, toolRackModel, 0);

            //TODO - Sluice
            ModelFile sluiceLowerModel = models()
                    .withExistingParent(("block/sluice/" + woodName + "/lower"), modLoc("block/template/sluice/lower"))
                    .texture("texture", strippedLogSideTexture);
            ModelFile sluiceUpperModel = models()
                    .withExistingParent(("block/sluice/" + woodName + "/upper"), modLoc("block/template/sluice/upper"))
                    .texture("texture", strippedLogSideTexture);

            getVariantBuilder(sluiceBlock)
                    // Upper part (upper=true)
                    .partialState().with(SluiceBlock.UPPER, true).with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                    .modelForState().modelFile(sluiceUpperModel).rotationY(90).addModel()
                    .partialState().with(SluiceBlock.UPPER, true).with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                    .modelForState().modelFile(sluiceUpperModel).rotationY(0).addModel()
                    .partialState().with(SluiceBlock.UPPER, true).with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                    .modelForState().modelFile(sluiceUpperModel).rotationY(180).addModel()
                    .partialState().with(SluiceBlock.UPPER, true).with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                    .modelForState().modelFile(sluiceUpperModel).rotationY(270).addModel()

                    // Lower part (upper=false)
                    .partialState().with(SluiceBlock.UPPER, false).with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                    .modelForState().modelFile(sluiceLowerModel).rotationY(90).addModel()
                    .partialState().with(SluiceBlock.UPPER, false).with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                    .modelForState().modelFile(sluiceLowerModel).rotationY(0).addModel()
                    .partialState().with(SluiceBlock.UPPER, false).with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                    .modelForState().modelFile(sluiceLowerModel).rotationY(180).addModel()
                    .partialState().with(SluiceBlock.UPPER, false).with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                    .modelForState().modelFile(sluiceLowerModel).rotationY(270).addModel();
            //TODO - Barrel
            barrelBlock(barrelBlock, wood);

            //TODO - Loom - check offset
            ModelFile loomModel = models()
                    .withExistingParent(("block/loom/" + woodName), modLoc("block/template/loom"))
                    .texture("texture", planksTexture)
                    .texture("particle", planksTexture);

            getVariantBuilder(loomBlock)
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                    .modelForState().modelFile(loomModel).rotationY(270).addModel()
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                    .modelForState().modelFile(loomModel).rotationY(180).addModel()
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                    .modelForState().modelFile(loomModel).rotationY(0).addModel()  // default, no rotation
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                    .modelForState().modelFile(loomModel).rotationY(90).addModel();

            //TODO - Scribing Table
            ModelFile scribingTableModel = models()
                    .withExistingParent(("block/scribing_table/" + woodName), modLoc("block/template/scribing_table"))
                    .texture("top", strippedLogSideTexture)
                    .texture("leg", logSideTexture)
                    .texture("side", planksTexture)
                    .texture("misc", scribingParaphernaliaTexture)
                    .texture("particle", planksTexture);

            getVariantBuilder(scribingTableBlock)
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                    .modelForState().modelFile(scribingTableModel).rotationY(90).addModel()
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                    .modelForState().modelFile(scribingTableModel).rotationY(0).addModel()
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                    .modelForState().modelFile(scribingTableModel).rotationY(180).addModel()  // default, no rotation
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                    .modelForState().modelFile(scribingTableModel).rotationY(270).addModel();

            //TODO - Sewing Table
            ModelFile sewingTableModel = models()
                    .withExistingParent(("block/sewing_table/" + woodName), modLoc("block/template/sewing_table"))
                    .texture("0", logSideTexture)
                    .texture("1", planksTexture);

            getVariantBuilder(sewingTableBlock)
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                    .modelForState().modelFile(sewingTableModel).rotationY(90).addModel()
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                    .modelForState().modelFile(sewingTableModel).rotationY(0).addModel()
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                    .modelForState().modelFile(sewingTableModel).rotationY(180).addModel()  // default, no rotation
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                    .modelForState().modelFile(sewingTableModel).rotationY(270).addModel();

            //TODO - Shelf
            ModelFile shelfModel = models()
                    .withExistingParent(("block/shelf/" + woodName), modLoc("block/template/shelf"))
                    .texture("0", planksTexture);

            getVariantBuilder(shelfBlock)
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST)
                    .modelForState().modelFile(shelfModel).rotationY(90).addModel()
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH)
                    .modelForState().modelFile(shelfModel).rotationY(0).addModel()
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH)
                    .modelForState().modelFile(shelfModel).rotationY(180).addModel()  // default, no rotation
                    .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST)
                    .modelForState().modelFile(shelfModel).rotationY(270).addModel();

            //TODO - Bladed Axle
            ModelFile bladedAxleModel = models()
                    .withExistingParent(("block/bladed_axle/" + woodName), modLoc("block/template/bladed_axle"))
                    .texture("wood", strippedLogSideTexture);

            simpleBlock(bladedAxleBlock, emptyModel);

            //TODO - Axle
            ModelFile axleModel = models()
                    .withExistingParent(("block/axle/" + woodName), modLoc("block/template/axle"))
                    .texture("wood", strippedLogSideTexture);

            simpleBlock(axleBlock, emptyModel);

            //TODO - Encased Axle
            ModelFile encasedAxleModel = models()
                    .withExistingParent(("block/encased_axle/" + woodName), modLoc("block/template/ore_column"))
                    .texture("side", strippedLogSideTexture)
                    .texture("end", planksTexture)
                    .texture("overlay", axleCasingTexture)
                    .texture("overlay_end", axleCasingFrontTexture)
                    .texture("particle", strippedLogSideTexture);

            getVariantBuilder(encasedAxleBlock)
                    .partialState().with(BlockStateProperties.AXIS, Direction.Axis.X)
                    .modelForState().modelFile(encasedAxleModel).rotationX(90).rotationY(90).addModel()
                    .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Y)
                    .modelForState().modelFile(encasedAxleModel).addModel()
                    .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Z)
                    .modelForState().modelFile(encasedAxleModel).rotationX(90).addModel();

            //TODO - Gear Box
            ModelFile gearBoxPortModel = models()
                    .withExistingParent(("block/gear_box/" + woodName + "/port"), modLoc("block/template/gear_box/port"))
                    .texture("all", planksTexture)
                    .texture("overlay", axleCasingFrontTexture);
            ModelFile gearBoxFaceModel = models()
                    .withExistingParent(("block/gear_box/" + woodName + "/face"), modLoc("block/template/gear_box/face"))
                    .texture("all", planksTexture)
                    .texture("overlay", axleCasingRoundTexture);

            getMultipartBuilder(gearBoxBlock)
                    // North
                    .part().modelFile(gearBoxPortModel).addModel().condition(BlockStateProperties.NORTH, true).end()
                    .part().modelFile(gearBoxFaceModel).addModel().condition(BlockStateProperties.NORTH, false).end()

                    // South
                    .part().modelFile(gearBoxPortModel).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, true).end()
                    .part().modelFile(gearBoxFaceModel).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, false).end()

                    // East
                    .part().modelFile(gearBoxPortModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end()
                    .part().modelFile(gearBoxFaceModel).rotationY(90).addModel().condition(BlockStateProperties.EAST, false).end()

                    // West
                    .part().modelFile(gearBoxPortModel).rotationY(270).addModel().condition(BlockStateProperties.WEST, true).end()
                    .part().modelFile(gearBoxFaceModel).rotationY(270).addModel().condition(BlockStateProperties.WEST, false).end()

                    // Down
                    .part().modelFile(gearBoxPortModel).rotationX(90).addModel().condition(BlockStateProperties.DOWN, true).end()
                    .part().modelFile(gearBoxFaceModel).rotationX(90).addModel().condition(BlockStateProperties.DOWN, false).end()

                    // Up
                    .part().modelFile(gearBoxPortModel).rotationX(270).addModel().condition(BlockStateProperties.UP, true).end()
                    .part().modelFile(gearBoxFaceModel).rotationX(270).addModel().condition(BlockStateProperties.UP, false).end();

            //TODO - Clutch
            ModelFile clutchModel = models()
                    .withExistingParent(("block/clutch/" + woodName), modLoc("block/template/ore_column"))
                    .texture("side", strippedLogSideTexture)
                    .texture("end", planksTexture)
                    .texture("overlay", axleCasingUnpoweredTexture)
                    .texture("overlay_end", axleCasingFrontTexture)
                    .texture("particle", strippedLogSideTexture);
            ModelFile clutchPoweredModel = models()
                    .withExistingParent(("block/clutch/" + woodName + "_powered"), modLoc("block/template/ore_column"))
                    .texture("side", strippedLogSideTexture)
                    .texture("end", planksTexture)
                    .texture("overlay", axleCasingPoweredTexture)
                    .texture("overlay_end", axleCasingFrontTexture)
                    .texture("particle", strippedLogSideTexture);

            getVariantBuilder(clutchBlock)
                    // Axis X
                    .partialState().with(BlockStateProperties.AXIS, Direction.Axis.X).with(BlockStateProperties.POWERED, false)
                    .modelForState().modelFile(clutchModel).rotationX(90).rotationY(90).addModel()
                    .partialState().with(BlockStateProperties.AXIS, Direction.Axis.X).with(BlockStateProperties.POWERED, true)
                    .modelForState().modelFile(clutchPoweredModel).rotationX(90).rotationY(90).addModel()

                    // Axis Y
                    .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Y).with(BlockStateProperties.POWERED, false)
                    .modelForState().modelFile(clutchModel).addModel()
                    .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Y).with(BlockStateProperties.POWERED, true)
                    .modelForState().modelFile(clutchPoweredModel).addModel()

                    // Axis Z
                    .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Z).with(BlockStateProperties.POWERED, false)
                    .modelForState().modelFile(clutchModel).rotationX(90).addModel()
                    .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Z).with(BlockStateProperties.POWERED, true)
                    .modelForState().modelFile(clutchPoweredModel).rotationX(90).addModel();
            //TODO - Waterwheel
            ModelFile waterWheelModel = models()
                    .getBuilder("block/water_wheel/" + woodName)
                    .texture("particle", planksTexture);

            simpleBlock(waterWheelBlock, waterWheelModel);

            simpleBlock(windmillBlock, emptyModel);
        }

    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    private void barrelBlock(Block barrelBlock, CompatWood wood) {
        // Model files (adjust paths if your templates are in a different folder)
        ModelFile barrelModel = models()
                .withExistingParent(("block/barrel/" + wood.getSerializedName() + "/barrel"), modLoc("block/template/barrel/barrel"))
                .texture("particle", wood.planksTexture())
                .texture("planks", wood.planksTexture())
                .texture("sheet", wood.strippedLogTexture());
        ModelFile barrelSealedModel = models()
                .withExistingParent(("block/barrel/" + wood.getSerializedName() + "/barrel_sealed"), modLoc("block/template/barrel/barrel_sealed"))
                .texture("particle", wood.planksTexture())
                .texture("planks", wood.planksTexture())
                .texture("sheet", wood.strippedLogTexture());
        ModelFile barrelSideModel = models()
                .withExistingParent(("block/barrel/" + wood.getSerializedName() + "/barrel_side"), modLoc("block/template/barrel/barrel_side"))
                .texture("particle", wood.planksTexture())
                .texture("planks", wood.planksTexture())
                .texture("sheet", wood.strippedLogTexture());
        ModelFile barrelSideRackModel = models()
                .withExistingParent(("block/barrel/" + wood.getSerializedName() + "/barrel_side_rack"), modLoc("block/template/barrel/barrel_side_rack"))
                .texture("particle", wood.planksTexture())
                .texture("planks", wood.planksTexture())
                .texture("sheet", wood.strippedLogTexture());
        ModelFile barrelSideSealedModel = models()
                .withExistingParent(("block/barrel/" + wood.getSerializedName() + "/barrel_side_sealed"), modLoc("block/template/barrel/barrel_side_sealed"))
                .texture("particle", wood.planksTexture())
                .texture("planks", wood.planksTexture())
                .texture("sheet", wood.strippedLogTexture());
        ModelFile barrelSideSealedRackModel = models()
                .withExistingParent(("block/barrel/" + wood.getSerializedName() + "/barrel_side_sealed_rack"), modLoc("block/template/barrel/barrel_side_sealed_rack"))
                .texture("particle", wood.planksTexture())
                .texture("planks", wood.planksTexture())
                .texture("sheet", wood.strippedLogTexture());

        var builder = getVariantBuilder(barrelBlock);

        // Facing UP — no rotation needed
        builder
                .partialState().with(BarrelBlock.FACING, Direction.UP).with(BarrelBlock.RACK, true).with(BarrelBlock.SEALED, true)
                .modelForState().modelFile(barrelSealedModel).addModel()
                .partialState().with(BarrelBlock.FACING, Direction.UP).with(BarrelBlock.RACK, true).with(BarrelBlock.SEALED, false)
                .modelForState().modelFile(barrelModel).addModel()
                .partialState().with(BarrelBlock.FACING, Direction.UP).with(BarrelBlock.RACK, false).with(BarrelBlock.SEALED, true)
                .modelForState().modelFile(barrelSealedModel).addModel()
                .partialState().with(BarrelBlock.FACING, Direction.UP).with(BarrelBlock.RACK, false).with(BarrelBlock.SEALED, false)
                .modelForState().modelFile(barrelModel).addModel();

        // Horizontal facings — adjust rotation so barrel lies along the facing axis
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            // Base rotation from direction
            int baseYRot = (int) dir.toYRot(); // N=0, E=90, S=180, W=270

            // Add 90° so that when facing east/west, the model aligns east-west
            // This compensates for side models being authored facing north
            int fixedYRot = (baseYRot + 90) % 360;

            ModelFile rackModel = BarrelBlock.SEALED.getPossibleValues().contains(true) ? barrelSideSealedRackModel : barrelSideRackModel;
            ModelFile noRackModel = BarrelBlock.SEALED.getPossibleValues().contains(true) ? barrelSideSealedModel : barrelSideModel;

            // Rack + Sealed
            builder.partialState().with(BarrelBlock.FACING, dir).with(BarrelBlock.RACK, true).with(BarrelBlock.SEALED, true)
                    .modelForState().modelFile(barrelSideSealedRackModel).rotationY(fixedYRot).addModel();

            // Rack + Unsealed
            builder.partialState().with(BarrelBlock.FACING, dir).with(BarrelBlock.RACK, true).with(BarrelBlock.SEALED, false)
                    .modelForState().modelFile(barrelSideRackModel).rotationY(fixedYRot).addModel();

            // No Rack + Sealed
            builder.partialState().with(BarrelBlock.FACING, dir).with(BarrelBlock.RACK, false).with(BarrelBlock.SEALED, true)
                    .modelForState().modelFile(barrelSideSealedModel).rotationY(fixedYRot).addModel();

            // No Rack + Unsealed
            builder.partialState().with(BarrelBlock.FACING, dir).with(BarrelBlock.RACK, false).with(BarrelBlock.SEALED, false)
                    .modelForState().modelFile(barrelSideModel).rotationY(fixedYRot).addModel();
        }
    }
}
