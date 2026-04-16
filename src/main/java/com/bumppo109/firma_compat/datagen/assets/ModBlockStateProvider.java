package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, FirmaCompat.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        FirmaCompat.LOGGER.info("Starting blockstate and model generation for Firma Compat...");

        // Wood blocks
        for (CompatWood wood : CompatWood.VALUES) {
            generateWoodBlock(wood);
        }

        FirmaCompat.LOGGER.info("Finished blockstate and model generation.");
    }

    // ========================================================================
    // WOOD BLOCKS
    // ========================================================================
    private void generateWoodBlock(CompatWood wood) {
        String woodName = wood.getSerializedName();
        var map = ModBlocks.WOODS.get(wood);

        Block twig = map.get(CompatWood.BlockType.TWIG).get();
        Block logFence = map.get(CompatWood.BlockType.LOG_FENCE).get();
        Block vSupport = map.get(CompatWood.BlockType.VERTICAL_SUPPORT).get();
        Block hSupport = map.get(CompatWood.BlockType.HORIZONTAL_SUPPORT).get();
        Block toolRack = map.get(CompatWood.BlockType.TOOL_RACK).get();
        Block sluice = map.get(CompatWood.BlockType.SLUICE).get();
        Block barrel = map.get(CompatWood.BlockType.BARREL).get();
        Block loom = map.get(CompatWood.BlockType.LOOM).get();
        Block scribingTable = map.get(CompatWood.BlockType.SCRIBING_TABLE).get();
        Block sewingTable = map.get(CompatWood.BlockType.SEWING_TABLE).get();
        Block shelf = map.get(CompatWood.BlockType.SHELF).get();
        Block axle = map.get(CompatWood.BlockType.AXLE).get();
        Block bladedAxle = map.get(CompatWood.BlockType.BLADED_AXLE).get();
        Block encasedAxle = map.get(CompatWood.BlockType.ENCASED_AXLE).get();
        Block clutch = map.get(CompatWood.BlockType.CLUTCH).get();
        Block gearBox = map.get(CompatWood.BlockType.GEAR_BOX).get();
        Block waterWheel = map.get(CompatWood.BlockType.WATER_WHEEL).get();
        Block windmill = map.get(CompatWood.BlockType.WINDMILL).get();

        // Textures
        String logSuffix = woodName.equals("crimson") || woodName.equals("warped") ? "stem" : "log";
        ResourceLocation logSide = mcLoc("block/" + woodName + "_" + logSuffix);
        ResourceLocation logTop = mcLoc("block/" + woodName + "_" + logSuffix + "_top");
        ResourceLocation strippedLog = mcLoc("block/stripped_" + woodName + "_" + logSuffix);
        ResourceLocation planks = mcLoc("block/" + woodName + "_planks");

        ResourceLocation scribingMisc = modLoc("block/template/scribing_paraphernalia");
        ResourceLocation axleCasing = modLoc("block/template/axle_casing");
        ResourceLocation axleCasingFront = modLoc("block/template/axle_casing_front");
        ResourceLocation axleCasingRound = modLoc("block/template/axle_casing_round");
        ResourceLocation axleUnpowered = modLoc("block/template/axle_casing_unpowered");
        ResourceLocation axlePowered = modLoc("block/template/axle_casing_powered");

        // Generate blocks
        twigBlock(twig, logTop, logSide, wood);
        logFenceBlock(logFence, logSide, planks, wood);
        supportBlocks(vSupport, hSupport, strippedLog, wood);
        toolRackBlock(toolRack, planks, wood);
        sluiceBlock(sluice, strippedLog, wood);
        barrelBlock(barrel, wood);
        loomBlock(loom, planks, wood);
        scribingTableBlock(scribingTable, strippedLog, logSide, planks, scribingMisc, wood);
        sewingTableBlock(sewingTable, logSide, planks, wood);
        shelfBlock(shelf, planks, wood);
        axleBlock(axle, strippedLog, wood);
        bladedAxleBlock(bladedAxle, strippedLog, wood);
        encasedAxleBlock(encasedAxle, strippedLog, planks, axleCasing, axleCasingFront, wood);
        clutchBlock(clutch, strippedLog, planks, axleUnpowered, axlePowered, wood);
        gearBoxBlock(gearBox, planks, axleCasingFront, axleCasingRound, wood);
        waterWheelBlock(waterWheel, planks, wood);
        windmillBlock(windmill, wood);
    }

    // ========================================================================
    // BARREL BLOCK - FULL IMPLEMENTATION
    // ========================================================================
    private void barrelBlock(Block barrelBlock, CompatWood wood) {
        String woodName = wood.getSerializedName();

        ModelFile barrelModel = models()
                .withExistingParent("block/barrel/" + woodName + "/barrel", modLoc("block/template/barrel/barrel"))
                .texture("particle", wood.planksTexture())
                .texture("planks", wood.planksTexture())
                .texture("sheet", wood.strippedLogTexture());

        ModelFile barrelSealedModel = models()
                .withExistingParent("block/barrel/" + woodName + "/barrel_sealed", modLoc("block/template/barrel/barrel_sealed"))
                .texture("particle", wood.planksTexture())
                .texture("planks", wood.planksTexture())
                .texture("sheet", wood.strippedLogTexture());

        ModelFile barrelSideModel = models()
                .withExistingParent("block/barrel/" + woodName + "/barrel_side", modLoc("block/template/barrel/barrel_side"))
                .texture("particle", wood.planksTexture())
                .texture("planks", wood.planksTexture())
                .texture("sheet", wood.strippedLogTexture());

        ModelFile barrelSideRackModel = models()
                .withExistingParent("block/barrel/" + woodName + "/barrel_side_rack", modLoc("block/template/barrel/barrel_side_rack"))
                .texture("particle", wood.planksTexture())
                .texture("planks", wood.planksTexture())
                .texture("sheet", wood.strippedLogTexture());

        ModelFile barrelSideSealedModel = models()
                .withExistingParent("block/barrel/" + woodName + "/barrel_side_sealed", modLoc("block/template/barrel/barrel_side_sealed"))
                .texture("particle", wood.planksTexture())
                .texture("planks", wood.planksTexture())
                .texture("sheet", wood.strippedLogTexture());

        ModelFile barrelSideSealedRackModel = models()
                .withExistingParent("block/barrel/" + woodName + "/barrel_side_sealed_rack", modLoc("block/template/barrel/barrel_side_sealed_rack"))
                .texture("particle", wood.planksTexture())
                .texture("planks", wood.planksTexture())
                .texture("sheet", wood.strippedLogTexture());

        var builder = getVariantBuilder(barrelBlock);

        // Facing UP (vertical barrel)
        builder
                .partialState().with(BarrelBlock.FACING, Direction.UP).with(BarrelBlock.RACK, true).with(BarrelBlock.SEALED, true)
                .modelForState().modelFile(barrelSealedModel).addModel()
                .partialState().with(BarrelBlock.FACING, Direction.UP).with(BarrelBlock.RACK, true).with(BarrelBlock.SEALED, false)
                .modelForState().modelFile(barrelModel).addModel()
                .partialState().with(BarrelBlock.FACING, Direction.UP).with(BarrelBlock.RACK, false).with(BarrelBlock.SEALED, true)
                .modelForState().modelFile(barrelSealedModel).addModel()
                .partialState().with(BarrelBlock.FACING, Direction.UP).with(BarrelBlock.RACK, false).with(BarrelBlock.SEALED, false)
                .modelForState().modelFile(barrelModel).addModel();

        // Horizontal facings
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            int yRot = (int) dir.toYRot() + 90; // Adjust rotation so barrel lies correctly

            builder
                    // Rack + Sealed
                    .partialState().with(BarrelBlock.FACING, dir).with(BarrelBlock.RACK, true).with(BarrelBlock.SEALED, true)
                    .modelForState().modelFile(barrelSideSealedRackModel).rotationY(yRot).addModel()

                    // Rack + Unsealed
                    .partialState().with(BarrelBlock.FACING, dir).with(BarrelBlock.RACK, true).with(BarrelBlock.SEALED, false)
                    .modelForState().modelFile(barrelSideRackModel).rotationY(yRot).addModel()

                    // No Rack + Sealed
                    .partialState().with(BarrelBlock.FACING, dir).with(BarrelBlock.RACK, false).with(BarrelBlock.SEALED, true)
                    .modelForState().modelFile(barrelSideSealedModel).rotationY(yRot).addModel()

                    // No Rack + Unsealed
                    .partialState().with(BarrelBlock.FACING, dir).with(BarrelBlock.RACK, false).with(BarrelBlock.SEALED, false)
                    .modelForState().modelFile(barrelSideModel).rotationY(yRot).addModel();
        }
    }

    // ========================================================================
    // PRIVATE HELPERS - WOOD
    // ========================================================================

    private void twigBlock(Block block, ResourceLocation top, ResourceLocation side, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile model = models().withExistingParent("block/twig/" + name, modLoc("block/template/twig"))
                .texture("side", side).texture("top", top);

        simpleBlock(block,
                ConfiguredModel.builder().modelFile(model).rotationY(0).buildLast(),
                ConfiguredModel.builder().modelFile(model).rotationY(90).buildLast(),
                ConfiguredModel.builder().modelFile(model).rotationY(180).buildLast(),
                ConfiguredModel.builder().modelFile(model).rotationY(270).buildLast()
        );
    }

    private void logFenceBlock(Block block, ResourceLocation logTex, ResourceLocation planksTex, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile post = models().withExistingParent("block/log_fence/" + name + "/post", mcLoc("block/fence_post"))
                .texture("texture", logTex);
        ModelFile side = models().withExistingParent("block/log_fence/" + name + "/side", mcLoc("block/fence_side"))
                .texture("texture", planksTex);

        //inventory
        models()
                .withExistingParent("block/log_fence/" + name + "/inventory", modLoc("block/template/log_fence_inventory"))
                .texture("log", logTex)
                .texture("planks", planksTex);

        getMultipartBuilder(block)
                .part().modelFile(post).addModel().end()
                .part().modelFile(side).uvLock(true).addModel().condition(BlockStateProperties.NORTH, true).end()
                .part().modelFile(side).rotationY(90).uvLock(true).addModel().condition(BlockStateProperties.EAST, true).end()
                .part().modelFile(side).rotationY(180).uvLock(true).addModel().condition(BlockStateProperties.SOUTH, true).end()
                .part().modelFile(side).rotationY(270).uvLock(true).addModel().condition(BlockStateProperties.WEST, true).end();

    }

    private void supportBlocks(Block vertical, Block horizontal, ResourceLocation texture, CompatWood wood) {
        String woodName = wood.getSerializedName();

        ModelFile connection = models().withExistingParent("block/support/" + woodName + "/connection", modLoc("block/template/support/connection"))
                .texture("texture", texture).texture("particle", texture);
        ModelFile horiz = models().withExistingParent("block/support/" + woodName + "/horizontal", modLoc("block/template/support/horizontal"))
                .texture("texture", texture).texture("particle", texture);
        ModelFile vert = models().withExistingParent("block/support/" + woodName + "/vertical", modLoc("block/template/support/vertical"))
                .texture("texture", texture).texture("particle", texture);

        //inventory
        models()
                .withExistingParent(("block/support/" + woodName + "/inventory"), modLoc("block/template/support/inventory"))
                .texture("texture", texture);

        getMultipartBuilder(vertical)
                .part().modelFile(vert).addModel().end()
                .part().modelFile(connection).rotationY(270).addModel().condition(BlockStateProperties.NORTH, true).end()
                .part().modelFile(connection).addModel().condition(BlockStateProperties.EAST, true).end()
                .part().modelFile(connection).rotationY(90).addModel().condition(BlockStateProperties.SOUTH, true).end()
                .part().modelFile(connection).rotationY(180).addModel().condition(BlockStateProperties.WEST, true).end();

        getMultipartBuilder(horizontal)
                .part().modelFile(horiz).addModel().end()
                .part().modelFile(connection).rotationY(270).addModel().condition(BlockStateProperties.NORTH, true).end()
                .part().modelFile(connection).addModel().condition(BlockStateProperties.EAST, true).end()
                .part().modelFile(connection).rotationY(90).addModel().condition(BlockStateProperties.SOUTH, true).end()
                .part().modelFile(connection).rotationY(180).addModel().condition(BlockStateProperties.WEST, true).end();
    }

    private void toolRackBlock(Block block, ResourceLocation planks, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile model = models().withExistingParent("block/tool_rack/" + name, modLoc("block/template/tool_rack"))
                .texture("texture", planks).texture("particle", planks);

        horizontalBlock(block, model, 0);
    }

    private void sluiceBlock(Block block, ResourceLocation texture, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile lower = models().withExistingParent("block/sluice/" + name + "/lower", modLoc("block/template/sluice/lower"))
                .texture("texture", texture);
        ModelFile upper = models().withExistingParent("block/sluice/" + name + "/upper", modLoc("block/template/sluice/upper"))
                .texture("texture", texture);

        getVariantBuilder(block)
                .partialState().with(SluiceBlock.UPPER, true).with(HorizontalDirectionalBlock.FACING, Direction.EAST).modelForState().modelFile(upper).rotationY(90).addModel()
                .partialState().with(SluiceBlock.UPPER, true).with(HorizontalDirectionalBlock.FACING, Direction.NORTH).modelForState().modelFile(upper).addModel()
                .partialState().with(SluiceBlock.UPPER, true).with(HorizontalDirectionalBlock.FACING, Direction.SOUTH).modelForState().modelFile(upper).rotationY(180).addModel()
                .partialState().with(SluiceBlock.UPPER, true).with(HorizontalDirectionalBlock.FACING, Direction.WEST).modelForState().modelFile(upper).rotationY(270).addModel()
                .partialState().with(SluiceBlock.UPPER, false).with(HorizontalDirectionalBlock.FACING, Direction.EAST).modelForState().modelFile(lower).rotationY(90).addModel()
                .partialState().with(SluiceBlock.UPPER, false).with(HorizontalDirectionalBlock.FACING, Direction.NORTH).modelForState().modelFile(lower).addModel()
                .partialState().with(SluiceBlock.UPPER, false).with(HorizontalDirectionalBlock.FACING, Direction.SOUTH).modelForState().modelFile(lower).rotationY(180).addModel()
                .partialState().with(SluiceBlock.UPPER, false).with(HorizontalDirectionalBlock.FACING, Direction.WEST).modelForState().modelFile(lower).rotationY(270).addModel();
    }

    private String woodNameFromBlock(Block block) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath().replace("_sluice", "");
    }

    private void loomBlock(Block block, ResourceLocation planks, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile model = models().withExistingParent("block/loom/" + name, modLoc("block/template/loom"))
                .texture("texture", planks).texture("particle", planks);

        getVariantBuilder(block)
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST).modelForState().modelFile(model).rotationY(270).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH).modelForState().modelFile(model).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH).modelForState().modelFile(model).rotationY(180).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST).modelForState().modelFile(model).rotationY(90).addModel();
    }

    private void scribingTableBlock(Block block, ResourceLocation stripped, ResourceLocation logSide, ResourceLocation planks, ResourceLocation misc, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile model = models().withExistingParent("block/scribing_table/" + name, modLoc("block/template/scribing_table"))
                .texture("top", stripped)
                .texture("leg", logSide)
                .texture("side", planks)
                .texture("misc", misc)
                .texture("particle", planks);

        getVariantBuilder(block)
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST).modelForState().modelFile(model).rotationY(90).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH).modelForState().modelFile(model).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH).modelForState().modelFile(model).rotationY(180).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST).modelForState().modelFile(model).rotationY(270).addModel();
    }

    private void sewingTableBlock(Block block, ResourceLocation logSide, ResourceLocation planks, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile model = models().withExistingParent("block/sewing_table/" + name, modLoc("block/template/sewing_table"))
                .texture("0", logSide)
                .texture("1", planks);

        getVariantBuilder(block)
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST).modelForState().modelFile(model).rotationY(90).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH).modelForState().modelFile(model).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH).modelForState().modelFile(model).rotationY(180).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST).modelForState().modelFile(model).rotationY(270).addModel();
    }

    private void shelfBlock(Block block, ResourceLocation planks, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile model = models().withExistingParent("block/shelf/" + name, modLoc("block/template/shelf"))
                .texture("0", planks);

        getVariantBuilder(block)
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.EAST).modelForState().modelFile(model).rotationY(90).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.NORTH).modelForState().modelFile(model).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.SOUTH).modelForState().modelFile(model).rotationY(180).addModel()
                .partialState().with(HorizontalDirectionalBlock.FACING, Direction.WEST).modelForState().modelFile(model).rotationY(270).addModel();
    }

    private void axleBlock(Block block, ResourceLocation strippedLog, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile axleModel = models()
                .withExistingParent("block/axle/" + name, modLoc("block/template/axle"))
                .texture("wood", strippedLog);

        simpleBlock(block, axleModel);
    }

    private void bladedAxleBlock(Block block, ResourceLocation strippedLog, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile axleModel = models()
                .withExistingParent("block/bladed_axle/" + name, modLoc("block/template/bladed_axle"))
                .texture("wood", strippedLog);
        simpleBlock(block, axleModel);
    }

    private void encasedAxleBlock(Block block, ResourceLocation stripped, ResourceLocation planks, ResourceLocation casing, ResourceLocation casingFront, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile model = models().withExistingParent("block/encased_axle/" + name, modLoc("block/template/ore_column"))
                .texture("side", stripped)
                .texture("end", planks)
                .texture("overlay", casing)
                .texture("overlay_end", casingFront)
                .texture("particle", stripped);

        getVariantBuilder(block)
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.X).modelForState().modelFile(model).rotationX(90).rotationY(90).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Y).modelForState().modelFile(model).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Z).modelForState().modelFile(model).rotationX(90).addModel();
    }

    private void clutchBlock(Block block, ResourceLocation stripped, ResourceLocation planks, ResourceLocation unpowered, ResourceLocation powered, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile off = models().withExistingParent("block/clutch/" + name, modLoc("block/template/ore_column"))
                .texture("side", stripped).texture("end", planks).texture("overlay", unpowered).texture("overlay_end", unpowered).texture("particle", stripped);
        ModelFile on = models().withExistingParent("block/clutch/" + name + "_powered", modLoc("block/template/ore_column"))
                .texture("side", stripped).texture("end", planks).texture("overlay", powered).texture("overlay_end", powered).texture("particle", stripped);

        getVariantBuilder(block)
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.X).with(BlockStateProperties.POWERED, false).modelForState().modelFile(off).rotationX(90).rotationY(90).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.X).with(BlockStateProperties.POWERED, true).modelForState().modelFile(on).rotationX(90).rotationY(90).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Y).with(BlockStateProperties.POWERED, false).modelForState().modelFile(off).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Y).with(BlockStateProperties.POWERED, true).modelForState().modelFile(on).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Z).with(BlockStateProperties.POWERED, false).modelForState().modelFile(off).rotationX(90).addModel()
                .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Z).with(BlockStateProperties.POWERED, true).modelForState().modelFile(on).rotationX(90).addModel();
    }

    private void gearBoxBlock(Block block, ResourceLocation planks, ResourceLocation portTex, ResourceLocation faceTex, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile port = models().withExistingParent("block/gear_box/" + name + "/port", modLoc("block/template/gear_box/port"))
                .texture("all", planks).texture("overlay", portTex);
        ModelFile face = models().withExistingParent("block/gear_box/" + name + "/face", modLoc("block/template/gear_box/face"))
                .texture("all", planks).texture("overlay", faceTex);

        getMultipartBuilder(block)
                .part().modelFile(port).addModel().condition(BlockStateProperties.NORTH, true).end()
                .part().modelFile(face).addModel().condition(BlockStateProperties.NORTH, false).end()
                .part().modelFile(port).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, true).end()
                .part().modelFile(face).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, false).end()
                .part().modelFile(port).rotationY(90).addModel().condition(BlockStateProperties.EAST, true).end()
                .part().modelFile(face).rotationY(90).addModel().condition(BlockStateProperties.EAST, false).end()
                .part().modelFile(port).rotationY(270).addModel().condition(BlockStateProperties.WEST, true).end()
                .part().modelFile(face).rotationY(270).addModel().condition(BlockStateProperties.WEST, false).end()
                .part().modelFile(port).rotationX(90).addModel().condition(BlockStateProperties.DOWN, true).end()
                .part().modelFile(face).rotationX(90).addModel().condition(BlockStateProperties.DOWN, false).end()
                .part().modelFile(port).rotationX(270).addModel().condition(BlockStateProperties.UP, true).end()
                .part().modelFile(face).rotationX(270).addModel().condition(BlockStateProperties.UP, false).end();
    }

    private void waterWheelBlock(Block block, ResourceLocation planks, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile model = models().getBuilder("block/water_wheel/" + name)
                .texture("particle", planks);
        simpleBlock(block, model);
    }

    private void windmillBlock(Block block, CompatWood wood) {
        String name = wood.getSerializedName();

        ModelFile axleModel = models()
                .withExistingParent("block/windmill/" + name, modLoc("block/template/empty"));

        simpleBlock(block, axleModel);
    }

    // ========================================================================
    // PRIVATE HELPERS - ROCK
    // ========================================================================
}