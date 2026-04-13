package com.bumppo109.firma_compat.block;

import com.bumppo109.firma_compat.item.ModItems;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Locale;
import java.util.function.Supplier;

public enum CompatBricks implements StringRepresentable {
    STONE_BRICKS,
    MOSSY_STONE_BRICKS,
    DEEPSLATE_BRICKS,
    DEEPSLATE_TILES,
    BRICKS,
    POLISHED_BLACKSTONE_BRICKS,
    END_STONE_BRICKS,
    PRISMARINE_BRICKS,
    QUARTZ_BRICKS,
    NETHER_BRICKS
    ;

    public static final CompatBricks[] VALUES = values();

    private final String serializedName;


    CompatBricks()
    {
        this.serializedName = this.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getSerializedName() {
        return serializedName;
    }

    public String getSingleName() {
        String name = this.name().toLowerCase(Locale.ROOT);

        if(name.endsWith("s")){
            name = name.substring(0, name.length()-1);
        }

        return name;
    }

    /**
     * Returns a Supplier for the vanilla Minecraft block that this brick type is based on.
     * Use this to copy properties, e.g., BlockBehaviour.Properties.copy(vanillaEquivalent().get()).
     */
    public Supplier<Block> vanillaEquivalent() {
        return switch (this) {
            case DEEPSLATE_BRICKS -> () -> Blocks.DEEPSLATE_BRICKS;
            case DEEPSLATE_TILES -> () -> Blocks.DEEPSLATE_TILES;
            case BRICKS -> () -> Blocks.BRICKS;
            case POLISHED_BLACKSTONE_BRICKS -> () -> Blocks.POLISHED_BLACKSTONE_BRICKS;
            case END_STONE_BRICKS -> () -> Blocks.END_STONE_BRICKS;
            case PRISMARINE_BRICKS -> () -> Blocks.PRISMARINE_BRICKS;
            case QUARTZ_BRICKS -> () -> Blocks.QUARTZ_BRICKS;
            case NETHER_BRICKS -> () -> Blocks.NETHER_BRICKS;
            case MOSSY_STONE_BRICKS -> () -> Blocks.MOSSY_STONE_BRICKS;
            default -> () -> Blocks.STONE_BRICKS;
        };
    }

    public Item brickItem() {
        return switch (this) {
            case DEEPSLATE_BRICKS -> ModItems.DEEPSLATE_BRICK.get();
            case DEEPSLATE_TILES -> ModItems.DEEPSLATE_TILE.get();
            case STONE_BRICKS -> ModItems.STONE_BRICK.get();
            case BRICKS -> Items.BRICK;
            case POLISHED_BLACKSTONE_BRICKS -> ModItems.POLISHED_BLACKSTONE_BRICK.get();
            case END_STONE_BRICKS -> ModItems.END_STONE_BRICK.get();
            case PRISMARINE_BRICKS -> ModItems.PRISMARINE_BRICK.get();
            case QUARTZ_BRICKS -> ModItems.QUARTZ_BRICK.get();
            case NETHER_BRICKS -> Items.NETHER_BRICK;
            default -> null;
        };
    }

    public String vanillaTexture() {
        return switch (this) {
            case DEEPSLATE_BRICKS -> "deepslate_bricks";
            case DEEPSLATE_TILES -> "deepslate_tiles";
            case BRICKS -> "bricks";
            case POLISHED_BLACKSTONE_BRICKS -> "polished_blackstone_bricks";
            case END_STONE_BRICKS -> "end_stone_bricks";
            case PRISMARINE_BRICKS -> "prismarine_bricks";
            case QUARTZ_BRICKS -> "quartz_bricks";
            case NETHER_BRICKS -> "nether_bricks";
            case MOSSY_STONE_BRICKS -> "mossy_stone_bricks";
            default -> "stone_bricks";
        };
    }
}
