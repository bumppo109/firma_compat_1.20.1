package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModVillagerTrades {

    @SubscribeEvent
    public static void addTrades(VillagerTradesEvent event) {

        if (event.getType() == VillagerProfession.CARTOGRAPHER) {

            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades =
                    event.getTrades();

            // Level 2 = Apprentice
            trades.get(2).add((trader, random) ->
                    new MerchantOffer(
                            new ItemStack(Items.EMERALD, 4),
                            new ItemStack(TFCBlocks.SMALL_ORES.get(Ore.MAGNETITE).get(), 2),
                            16,     // max uses
                            5,      // villager xp
                            0.05f   // price multiplier
                    )
            );
        }

        if (event.getType() == VillagerProfession.MASON) {

            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades =
                    event.getTrades();

            // Level 2 = Apprentice
            trades.get(3).add((trader, random) ->
                    new MerchantOffer(
                            new ItemStack(Items.EMERALD, 8),
                            new ItemStack(TFCItems.KAOLIN_CLAY.get(), 16),
                            16,     // max uses
                            5,      // villager xp
                            0.05f   // price multiplier
                    )
            );
        }
    }
}