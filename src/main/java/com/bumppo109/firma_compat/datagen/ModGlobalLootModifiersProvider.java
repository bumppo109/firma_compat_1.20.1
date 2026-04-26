package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.item.ModItems;
import com.bumppo109.firma_compat.loot.AddItemModifier;
import com.bumppo109.firma_compat.loot.ReplaceItemModifier;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider implements IConditionBuilder {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, FirmaCompat.MODID);
    }

    @Override
    protected void start() {
        //LootItemCondition modLoaded = (LootItemCondition) modLoaded("tfc");

        add("straw_from_grass", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
                LootItemRandomChanceCondition.randomChance(0.9f).build()}, TFCItems.STRAW.get()));

        // Replacement with mod loaded condition - very clean now
        add("replace_raw_iron", new ReplaceItemModifier(
                new LootItemCondition[0],
                Items.RAW_IRON,
                TFCItems.GRADED_ORES.get(Ore.HEMATITE).get(Ore.Grade.POOR).get(),
                1,
                "tfc"
        ));
    }
}