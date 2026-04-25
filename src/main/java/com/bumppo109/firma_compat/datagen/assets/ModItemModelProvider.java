package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Supplier;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, FirmaCompat.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (CompatWood wood : CompatWood.VALUES) {
            var woodMap = ModBlocks.WOODS.get(wood);
            var woodSupportMap = ModItems.SUPPORTS.get(wood);
            var woodLumberMap = ModItems.LUMBER.get(wood);
            String woodName = wood.getSerializedName();

            ResourceLocation planksTexture = mcLoc("block/" + woodName + "_planks");
            ResourceLocation axleCasingFrontTexture = modLoc("block/template/axle_casing_front");

            Block twigBlock = woodMap.get(CompatWood.BlockType.TWIG).get();
            Block logFenceBlock = woodMap.get(CompatWood.BlockType.LOG_FENCE).get();
            Item supportBlockItem = woodSupportMap.get();
            Block toolRackBlock = woodMap.get(CompatWood.BlockType.TOOL_RACK).get();
            Block sluiceBlock = woodMap.get(CompatWood.BlockType.SLUICE).get();
            Block barrelBlock = woodMap.get(CompatWood.BlockType.BARREL).get();
            Block loomBlock = woodMap.get(CompatWood.BlockType.LOOM).get();
            Block scribingTableBlock = woodMap.get(CompatWood.BlockType.SCRIBING_TABLE).get();
            Block sewingTableBlock = woodMap.get(CompatWood.BlockType.SEWING_TABLE).get();
            Block shelfBlock = woodMap.get(CompatWood.BlockType.SHELF).get();
            Block bladedAxleBlock = woodMap.get(CompatWood.BlockType.BLADED_AXLE).get();
            Block axleBlock = woodMap.get(CompatWood.BlockType.AXLE).get();
            Block encasedAxleBlock = woodMap.get(CompatWood.BlockType.ENCASED_AXLE).get();
            Block clutchBlock = woodMap.get(CompatWood.BlockType.CLUTCH).get();
            Block gearBoxBlock = woodMap.get(CompatWood.BlockType.GEAR_BOX).get();
            Block waterWheelBlock = woodMap.get(CompatWood.BlockType.WATER_WHEEL).get();
            //Block windmillBlock = woodMap.get(CompatWood.BlockType.WINDMILL).get();

            Item lumberItem = woodLumberMap.get();

            //lumber
            basicItem(lumberItem);
            //twig
            basicItem(twigBlock.asItem());
            //log fence
            withExistingParent(blockPathName(logFenceBlock), modLoc("block/log_fence/" + woodName + "/inventory"));
            //supports
            withExistingParent(itemPathName(supportBlockItem), modLoc("block/support/" + woodName + "/inventory"));
            //tool rack
            withExistingParent(blockPathName(toolRackBlock), modLoc("block/tool_rack/" + woodName));
            //sluice
            withExistingParent(blockPathName(sluiceBlock), modLoc("block/sluice/" + woodName + "/lower"));
            //barrel
            withExistingParent(blockPathName(barrelBlock), modLoc("block/barrel/" + woodName + "/barrel"));
            //loom
            withExistingParent(blockPathName(loomBlock), modLoc("block/loom/" + woodName));
            //scribing table
            withExistingParent(blockPathName(scribingTableBlock), modLoc("block/scribing_table/" + woodName));
            //sewing table
            withExistingParent(blockPathName(sewingTableBlock), modLoc("block/sewing_table/" + woodName));
            //shelf
            withExistingParent(blockPathName(shelfBlock), modLoc("block/shelf/" + woodName));
            //axle
            withExistingParent(blockPathName(axleBlock), modLoc("block/axle/" + woodName));
            //bladed axle
            withExistingParent(blockPathName(bladedAxleBlock), modLoc("block/bladed_axle/" + woodName));
            //encased axle
            withExistingParent(blockPathName(encasedAxleBlock), modLoc("block/encased_axle/" + woodName));
            //gear box
            withExistingParent(blockPathName(gearBoxBlock), modLoc("block/template/ore"))
                    .texture("all", planksTexture)
                    .texture("overlay", axleCasingFrontTexture);
            //clutch
            withExistingParent(blockPathName(clutchBlock), modLoc("block/clutch/" + woodName));
            //water wheel
            basicItem(waterWheelBlock.asItem());
            //windmill - N/A
        }

        for (CompatRock rock : CompatRock.VALUES) {

            if(rock == CompatRock.BLACKSTONE) {
                withExistingParent(Objects.requireNonNull(ModItems.BRICK.get(rock).getId()).getPath(),
                        ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                        ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/polished_blackstone_brick"));
            } else if (rock != CompatRock.NETHERRACK) {
                basicItem(ModItems.BRICK.get(rock).get());
            }
            basicItem(ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.LOOSE).get().asItem());

            if(rock != CompatRock.STONE && rock != CompatRock.DEEPSLATE && rock != CompatRock.BLACKSTONE && rock != CompatRock.END_STONE && rock != CompatRock.NETHERRACK){
                evenSimplerBlockItem(rock.getSlab(CompatRock.BlockType.BRICK));
                evenSimplerBlockItem(rock.getStair(CompatRock.BlockType.BRICK));
                wallItem(rock.getWall(CompatRock.BlockType.BRICK), ModBlocks.ROCK_BLOCKS.get(rock).get(CompatRock.BlockType.BRICK));
            }
        }

        basicItem(ModItems.PRISMARINE_BRICK.get());
        basicItem(ModItems.QUARTZ_BRICK.get());
        basicItem(ModBlocks.DRYING_MUD_BRICK.get().asItem());
        basicItem(ModItems.MUD_BRICK.get());
        basicItem(ModItems.UNFIRED_POT.get());
        basicItem(ModItems.NETHERITE_SCRAP_INGOT.get());

        for (CompatMetal metal : CompatMetal.values()) {
            var metalItems = ModItems.METAL_ITEMS.get(metal);
            if(metalItems != null) {
                metalItems.forEach((type, reg) -> {
                    basicItem(reg.get());
                });
            }
        }
    }

    public void evenSimplerBlockItem(Supplier<? extends Block> block) {
        this.withExistingParent(FirmaCompat.MODID + ":" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath(),
                modLoc("block/" + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath()));
    }
    public void wallItem(Supplier<? extends WallBlock> block, Supplier<Block> baseBlock) {
        this.withExistingParent(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    private String blockPathName(Block block) {
        return block.builtInRegistryHolder().key().location().getPath();
    }
    
    private String itemPathName(Item item) {
        return item.builtInRegistryHolder().key().location().getPath();
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.withDefaultNamespace("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID,"item/" + item.getId().getPath()));
    }
}