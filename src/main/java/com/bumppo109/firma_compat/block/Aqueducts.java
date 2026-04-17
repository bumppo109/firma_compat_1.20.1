package com.bumppo109.firma_compat.block;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Locale;
import java.util.function.Supplier;

public enum Aqueducts implements StringRepresentable {
    MOSSY_STONE_BRICKS,
    BRICKS,
    PRISMARINE_BRICKS,
    QUARTZ_BRICKS,
    NETHER_BRICKS
    ;

    public static final Aqueducts[] VALUES = values();

    private final String serializedName;


    Aqueducts()
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
            case BRICKS -> () -> Blocks.BRICKS;
            case PRISMARINE_BRICKS -> () -> Blocks.PRISMARINE_BRICKS;
            case QUARTZ_BRICKS -> () -> Blocks.QUARTZ_BRICKS;
            case NETHER_BRICKS -> () -> Blocks.NETHER_BRICKS;
            case MOSSY_STONE_BRICKS -> () -> Blocks.MOSSY_STONE_BRICKS;
        };
    }

    public ResourceLocation bricksTexture() {
        return switch (this) {
            case BRICKS -> ResourceLocation.withDefaultNamespace("block/bricks");
            case PRISMARINE_BRICKS -> ResourceLocation.withDefaultNamespace("block/prismarine_bricks");
            case QUARTZ_BRICKS -> ResourceLocation.withDefaultNamespace("block/quartz_bricks");
            case NETHER_BRICKS -> ResourceLocation.withDefaultNamespace("block/nether_bricks");
            case MOSSY_STONE_BRICKS -> ResourceLocation.withDefaultNamespace("block/mossy_stone_bricks");
        };
    }
}
