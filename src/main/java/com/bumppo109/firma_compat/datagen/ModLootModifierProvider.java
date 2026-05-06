package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.loot.AddItemModifier;
import com.bumppo109.firma_compat.loot.ReplaceItemModifier;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;

public class ModLootModifierProvider extends GlobalLootModifierProvider {

    public ModLootModifierProvider(PackOutput output) {
        super(output, FirmaCompat.MODID);   // ← This matches your Forge version
    }

    @Override
    protected void start() {

        //food
        replaceItem(Blocks.MELON, Items.MELON, TFCBlocks.MELON.get().asItem(), 1, null);
        replaceItem(Blocks.PUMPKIN, Items.PUMPKIN, TFCBlocks.PUMPKIN.get().asItem(), 1, null);

        //candle
        replaceItem(Blocks.CANDLE, Items.CANDLE, TFCBlocks.CANDLE.get().asItem(), 1);
        replaceItem(Blocks.CANDLE_CAKE, Items.CANDLE, TFCBlocks.CANDLE.get().asItem(), 1, "_from_cake");

        for(DyeColor color : DyeColor.values()){
            ResourceLocation dyeCandle = ResourceLocation.fromNamespaceAndPath("minecraft",color.getSerializedName() + "_candle");
            ResourceLocation dyeCandleCake = ResourceLocation.fromNamespaceAndPath("minecraft",color.getSerializedName() + "_candle_cake");

            Block dyeCandleBlock = Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(dyeCandle));
            Block dyeCandleCakeBlock = Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(dyeCandleCake));

            replaceItem(dyeCandleBlock, dyeCandleBlock.asItem(), TFCBlocks.DYED_CANDLE.get(color).get().asItem(), 1, null);
            replaceItem(dyeCandleCakeBlock, dyeCandleBlock.asItem(), TFCBlocks.DYED_CANDLE.get(color).get().asItem(), 1, "_from_cake");
        }
    }

    protected void replaceItem(Block targetBlock, Item fromItem, Item toItem, Integer count){
        ResourceLocation blockRes = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(targetBlock));

        replaceItem(ResourceLocation.fromNamespaceAndPath(blockRes.getNamespace(), "blocks/" + blockRes.getPath()),
        fromItem, toItem, count, null);
    }

    protected void replaceItem(Block targetBlock, Item fromItem, Item toItem, Integer count, @Nullable String modifierSuffix){
        ResourceLocation blockRes = Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(targetBlock));

        replaceItem(ResourceLocation.fromNamespaceAndPath(blockRes.getNamespace(), "blocks/" + blockRes.getPath()),
                fromItem, toItem, count, modifierSuffix);
    }

    protected void replaceItem(ResourceLocation targetTable, Item fromItem, Item toItem, Integer count, @Nullable String modifierSuffix){
        String fromItemStr = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(fromItem)).getPath();
        String toItemStr = Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(toItem)).getPath();
        String modifierId = "replace_" + fromItemStr + "_with_" + toItemStr;

        if(modifierSuffix != null){
            modifierId = modifierId + "_" + modifierSuffix;
        }

        add(modifierId, new ReplaceItemModifier(
                new LootItemCondition[]{
                        LootTableIdCondition.builder(targetTable).build()
                },
                fromItem, toItem, count
        ));
    }
}