package com.bumppo109.firma_compat.datagen.assets;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.addons.firmalife.CompatFLBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRBlocks;
import com.bumppo109.firma_compat.addons.rnr.CompatRnRItems;
import com.bumppo109.firma_compat.block.*;
import com.bumppo109.firma_compat.item.ModItems;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.util.Metal;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLang extends LanguageProvider {
    public ModLang(PackOutput output) {
        super(output, FirmaCompat.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("creative_tab.firma_compat_tab", "Firma Compat");

        add("fluid.firma_compat.metal.netherite", "Molten Netherite");
        add("fluid.firma_compat.metal.poor_netherite", "Molten Poor Netherite");
        add("metal.firma_compat.netherite", "Netherite");
        add("metal.firma_compat.poor_netherite", "Poor Netherite");

        //Wood Good Compat
        add("item_type.firma_compat.lumber", "%s Lumber");
        add("block_type.firma_compat.twig", "%s Twig");
        add("block_type.firma_compat.support", "%s Support");
        add("block_type.firma_compat.vertical_support", "%s Support");
        add("block_type.firma_compat.horizontal_support", "%s Support");
        add("block_type.firma_compat.log_fence", "%s Log Fence");
        add("block_type.firma_compat.tool_rack", "%s Tool Rack");
        add("block_type.firma_compat.loom", "%s Loom");
        add("block_type.firma_compat.sluice", "%s Sluice");
        add("block_type.firma_compat.barrel", "%s Barrel");
        add("block_type.firma_compat.scribing_table", "%s Scribing Table");
        add("block_type.firma_compat.sewing_table", "%s Sewing Table");
        add("block_type.firma_compat.shelf", "%s Shelf");
        add("block_type.firma_compat.axle", "%s Axle");
        add("block_type.firma_compat.bladed_axle", "%s Bladed Axle");
        add("block_type.firma_compat.encased_axle", "%s Encased Axle");
        add("block_type.firma_compat.clutch", "%s Clutch");
        add("block_type.firma_compat.gear_box", "%s Gear Box");
        add("block_type.firma_compat.windmill", "%s Windmill");
        add("block_type.firma_compat.water_wheel", "%s Water Wheel");

        add("block_type.firma_compat.big_barrel", "%s Keg");
        add("block_type.firma_compat.food_shelf", "%s Food Shelf");
        add("block_type.firma_compat.wine_shelf", "%s Wine Shelf");
        add("block_type.firma_compat.hanger", "%s Hanger");
        add("block_type.firma_compat.jarbnet", "%s Jarbnet");
        add("block_type.firma_compat.stomping_barrel", "%s Stomping Barrel");
        add("block_type.firma_compat.barrel_press", "%s Barrel Press");

        add("block_type.firma_compat.shingles", "%s Shingles");
        add("block_type.firma_compat.shingles_stairs", "%s Shingles Stairs");
        add("block_type.firma_compat.shingles_slab", "%s Shingles Slab");

        //Firmalife & RnR
        for(CompatWood wood : CompatWood.VALUES) {
            //Firmalife
            add(CompatFLBlocks.FOOD_SHELVES.get(wood).get().getDescriptionId(), getBlockDisplayName(CompatFLBlocks.FOOD_SHELVES.get(wood).get()));
            add(CompatFLBlocks.WINE_SHELVES.get(wood).get().getDescriptionId(), getBlockDisplayName(CompatFLBlocks.WINE_SHELVES.get(wood).get()));
            add(CompatFLBlocks.JARBNETS.get(wood).get().getDescriptionId(), getBlockDisplayName(CompatFLBlocks.JARBNETS.get(wood).get()));
            add(CompatFLBlocks.HANGERS.get(wood).get().getDescriptionId(), getBlockDisplayName(CompatFLBlocks.HANGERS.get(wood).get()));
            add(CompatFLBlocks.BARREL_PRESSES.get(wood).get().getDescriptionId(), getBlockDisplayName(CompatFLBlocks.BARREL_PRESSES.get(wood).get()));
            add(CompatFLBlocks.STOMPING_BARRELS.get(wood).get().getDescriptionId(), getBlockDisplayName(CompatFLBlocks.STOMPING_BARRELS.get(wood).get()));
            add(CompatFLBlocks.BIG_BARRELS.get(wood).get().getDescriptionId(), getBlockDisplayName(CompatFLBlocks.BIG_BARRELS.get(wood).get()));
            //RnR
            add(CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood).get().getDescriptionId(), getBlockDisplayName(CompatRnRBlocks.WOOD_SHINGLE_ROOFS.get(wood).get()));
            add(CompatRnRBlocks.WOOD_SHINGLE_ROOF_STAIRS.get(wood).get().getDescriptionId(), getBlockDisplayName(CompatRnRBlocks.WOOD_SHINGLE_ROOF_STAIRS.get(wood).get()));
            add(CompatRnRBlocks.WOOD_SHINGLE_ROOF_SLABS.get(wood).get().getDescriptionId(), getBlockDisplayName(CompatRnRBlocks.WOOD_SHINGLE_ROOF_SLABS.get(wood).get()));

            add(CompatRnRItems.WOOD_SHINGLE.get(wood).get().getDescriptionId(), getItemDisplayName(CompatRnRItems.WOOD_SHINGLE.get(wood).get()));
        }
        for(CompatRock rock : CompatRock.VALUES) {
            add(CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.POOR).get().getDescriptionId(), getBlockDisplayName(CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.POOR).get()));
            add(CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.NORMAL).get().getDescriptionId(), getBlockDisplayName(CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.NORMAL).get()));
            add(CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.RICH).get().getDescriptionId(), getBlockDisplayName(CompatFLBlocks.CHROMITE_ORES.get(rock).get(Ore.Grade.RICH).get()));
            add(CompatRnRItems.FLAGSTONE.get(rock).get().getDescriptionId(), getItemDisplayName(CompatRnRItems.FLAGSTONE.get(rock).get()));
        }
        add(CompatRnRItems.GRAVEL_FILL.get(), getItemDisplayName(CompatRnRItems.GRAVEL_FILL.get()));
        add(CompatRnRBlocks.TAMPED_MUD.get(), getBlockDisplayName(CompatRnRBlocks.TAMPED_MUD.get()));
        add(CompatRnRBlocks.TAMPED_DIRT.get(), getBlockDisplayName(CompatRnRBlocks.TAMPED_DIRT.get()));
        add(CompatRnRBlocks.OVER_HEIGHT_GRAVEL.get(), getBlockDisplayName(CompatRnRBlocks.OVER_HEIGHT_GRAVEL.get()));

        CompatRnRBlocks.ROCK_BLOCKS.forEach((rock, typeMap) -> {
            typeMap.forEach((road, blockSupplier) -> {
                add(blockSupplier.get().getDescriptionId(), getBlockDisplayName(blockSupplier.get()));
            });
        });
        CompatRnRBlocks.ROCK_STAIRS.forEach((rock, typeMap) -> {
            typeMap.forEach((road, blockSupplier) -> {
                add(blockSupplier.get().getDescriptionId(), getBlockDisplayName(blockSupplier.get()));
            });
        });
        CompatRnRBlocks.ROCK_SLABS.forEach((rock, typeMap) -> {
            typeMap.forEach((road, blockSupplier) -> {
                add(blockSupplier.get().getDescriptionId(), getBlockDisplayName(blockSupplier.get()));
            });
        });

        // Wood Related
        Block chestBlock = ModBlocks.COMPAT_CHEST.get();
        Block trappedChestBlock = ModBlocks.COMPAT_TRAPPED_CHEST.get();
        Item minecartChest = ModItems.COMPAT_CHEST_MINECART.get();

        add(chestBlock, "Chest");
        add(trappedChestBlock, "Trapped Chest");
        add(minecartChest, "Minecart with Chest");

        for (CompatWood wood : CompatWood.VALUES) {
            Item supportItem = ModItems.SUPPORTS.get(wood).get().asItem();
            String supportName = getItemDisplayName(supportItem);
            //add(supportItem.getDescriptionId(), supportName);

            //WOODS
            for (CompatWood.BlockType blockType : CompatWood.BlockType.values()) {
                Block block = ModBlocks.WOODS.get(wood).get(blockType).get();
                String blockName;

                if(blockType.equals(CompatWood.BlockType.VERTICAL_SUPPORT) || blockType.equals(CompatWood.BlockType.HORIZONTAL_SUPPORT)){
                    blockName = supportName;
                } else {
                    blockName = getBlockDisplayName(block);
                }
                add(block.getDescriptionId(), blockName);
            }
            //Hanging Signs
            /*
            for(Metal metal : Metal.values()){
                //TODO - null values from Block variables
                //TODO - add item names
                Block ceilingBlock = ModBlocks.CEILING_HANGING_SIGNS.get(wood).get(metal).get();
                Block wallBlock = ModBlocks.WALL_HANGING_SIGNS.get(wood).get(metal).get();
                String MetalName = metal.getSerializedName();

                add(ceilingBlock.getDescriptionId(), wood.getSerializedName().toUpperCase(Locale.ROOT) + " Hanging Sign");
                add(wallBlock.getDescriptionId(), wood.getSerializedName().toUpperCase(Locale.ROOT) + " Hanging Sign");
            }
             */

            //Lumber
            Item lumberItem = ModItems.LUMBER.get(wood).get();
            String lumberName = getItemDisplayName(lumberItem);

            add(lumberItem.getDescriptionId(), lumberName);
        }
        //Rock
        for (CompatRock rock : CompatRock.VALUES) {
            //ROCK_BLOCKS
            add(rock.getSlab(CompatRock.BlockType.BRICK).get().getDescriptionId(), getBlockDisplayName(rock.getSlab(CompatRock.BlockType.BRICK).get()));
            add(rock.getStair(CompatRock.BlockType.BRICK).get().getDescriptionId(), getBlockDisplayName(rock.getStair(CompatRock.BlockType.BRICK).get()));
            add(rock.getWall(CompatRock.BlockType.BRICK).get().getDescriptionId(), getBlockDisplayName(rock.getWall(CompatRock.BlockType.BRICK).get()));

            for (CompatRock.BlockType blockType : CompatRock.BlockType.values()) {
                Block block = ModBlocks.ROCK_BLOCKS.get(rock).get(blockType).get();
                Item brick = ModItems.BRICK.get(rock).get();

                String blockName = getBlockDisplayName(block);
                String brickName = getItemDisplayName(brick);

                add(block.getDescriptionId(), blockName);
                //add(brick.getDescriptionId(), brickName);
            }
            //Ore
            for(CompatOre ore : CompatOre.values()){
                if(!ore.isGraded()){
                    Block block = ModBlocks.ORES.get(rock).get(ore).get();
                    String blockName = getBlockDisplayName(block);

                    add(block.getDescriptionId(), blockName);
                } else {
                    for(CompatOre.Grade grade : CompatOre.Grade.values()){
                        Block block = ModBlocks.GRADED_ORES.get(rock).get(ore).get(grade).get();
                        String blockName = getBlockDisplayName(block);

                        add(block.getDescriptionId(), blockName);
                    }
                }
            }
        }

        for(Aqueducts brick : Aqueducts.VALUES){
            Block block = ModBlocks.AQUEDUCTS.get(brick).get();
            String blockName = getBlockDisplayName(block);

            add(block.getDescriptionId(), blockName);
        }

        //Other Blocks
        add(ModBlocks.PRIMITIVE_ANVIL.get().getDescriptionId(), "Primitive Anvil");
        add(ModBlocks.NATIVE_GOLD_GRAVEL_DEPOSIT.get().getDescriptionId(), "Native Gold Gravel Deposit");
        add(ModBlocks.NATIVE_SILVER_GRAVEL_DEPOSIT.get().getDescriptionId(), "Native Silver Gravel Deposit");
        add(ModBlocks.NATIVE_COPPER_GRAVEL_DEPOSIT.get().getDescriptionId(), "Native Copper Gravel Deposit");
        add(ModBlocks.CASSITERITE_GRAVEL_DEPOSIT.get().getDescriptionId(), "Cassiterite Gravel Deposit");
        add(ModBlocks.DRYING_MUD_BRICK.get().getDescriptionId(), "Wet Mud Brick");
        add(ModBlocks.COMPAT_FARMLAND.get().getDescriptionId(), "Farmland");
        add(ModBlocks.CLAY_PODZOL.get().getDescriptionId(), "Clay Podzol");
        add(ModBlocks.CLAY_GRASS_BLOCK.get().getDescriptionId(), "Clay Grass Block");
        add(ModBlocks.CLAY_DIRT.get().getDescriptionId(), "Clay Dirt");
        add(ModBlocks.KAOLIN_CLAY_PODZOL.get().getDescriptionId(), "Kaolin Clay Podzol");
        add(ModBlocks.KAOLIN_CLAY_GRASS_BLOCK.get().getDescriptionId(), "Kaolin Clay Grass Block");
        add(ModBlocks.KAOLIN_CLAY_DIRT.get().getDescriptionId(), "Kaolin Clay Dirt");

        //Metal Items
        for(CompatMetal metal : CompatMetal.values()){
            for(CompatMetal.ItemType itemType : CompatMetal.ItemType.values()){
                if(itemType.has(metal)){
                    Item metalItem = ModItems.METAL_ITEMS.get(metal).get(itemType).get();
                    String itemName = getItemDisplayName(metalItem);

                    add(metalItem.getDescriptionId(), itemName);
                }
            }
            //Metal Bucket items
            Item bucketItem = ModItems.METAL_FLUID_BUCKETS.get(metal).get();
            String bucketName = cleanUpString(metal.getSerializedName()) + " Bucket";

            add(bucketItem.getDescriptionId(), bucketName);
            //Metal Fluid Block
            Block fluidBlock = ModBlocks.METAL_FLUIDS.get(metal).get();
            String fluidName = getBlockDisplayName(fluidBlock);

            add(fluidBlock.getDescriptionId(), fluidName);
        }

        for(Metal.Default metal : Metal.Default.values()){
            if(metal.hasUtilities()){
                add("block.firma_compat." + metal.getSerializedName() + "_lantern.empty", cleanUpString(metal.getSerializedName()) + " Lantern");
                add("block.firma_compat." + metal.getSerializedName() + "_lantern.filled.filled", cleanUpString(metal.getSerializedName()) + " Lantern");
                add("block.firma_compat." + metal.getSerializedName() + "_lantern.filled.lit", cleanUpString(metal.getSerializedName()) + " Lantern");
            }
        }
        add("block.firma_compat.lantern.empty", "Lantern");
        add("block.firma_compat.lantern.filled.filled", "Lantern");
        add("block.firma_compat.lantern.filled.lit", "Lantern");



        //Other Items
        add(ModItems.BAMBOO_LUMBER.get().getDescriptionId(), "Bamboo Lumber");
        add(ModItems.MUD_BRICK.get().getDescriptionId(), "Mud Brick");
        add(ModItems.DEEPSLATE_TILE.get().getDescriptionId(), "Deepslate Tile");
        add(ModItems.QUARTZ_BRICK.get().getDescriptionId(), "Quartz Brick");
        add(ModItems.PRISMARINE_BRICK.get().getDescriptionId(), "Prismarine Brick");
        add(ModItems.UNFIRED_POT.get().getDescriptionId(), "Unfired Simple Pot");
    }

    private String cleanUpString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        // Replace underscores with spaces
        String normalized = input.replace('_', ' ');

        // Split into words, capitalize each, and join back
        String[] words = normalized.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        // Remove trailing space
        return result.toString().trim();
    }

    private String getBlockDisplayName(Block block) {
        if (block == null) {
            return "Unknown Block";
        }

        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
        if (id == null || id.equals(BuiltInRegistries.BLOCK.getDefaultKey())) {
            return "Air";
        }

        // Get the path part, e.g. "wood/planks/oak" or "metal/block/copper"
        String path = id.getPath();

        // Split on both "_" and "/" to handle folder-like paths nicely
        String[] parts = path.split("[_/]");

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            // Capitalize first letter, lowercase the rest
            String word = Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase();
            sb.append(word);

            sb.append(" ");
            first = false;
        }

        // Trim trailing space
        String result = sb.toString().trim();
        return result;
    }

    private String getItemDisplayName(Item item) {
        if (item == null) {
            return "Unknown Item";
        }

        ResourceLocation id = BuiltInRegistries.ITEM.getKey(item);
        if (id == null || id.equals(BuiltInRegistries.ITEM.getDefaultKey())) {
            return "Air";
        }

        // Get the path part, e.g. "metal/ingot/copper" or "lumber/oak"
        String path = id.getPath();

        // Split on both "_" and "/" to handle folder-like paths nicely
        String[] parts = path.split("[_/]");

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (String part : parts) {
            if (part.isEmpty()) {
                continue;
            }

            // Capitalize first letter, lowercase the rest
            String word = Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase();
            sb.append(word).append(" ");
            first = false;
        }

        // Trim trailing space
        String result = sb.toString().trim();
        return result;
    }
}

