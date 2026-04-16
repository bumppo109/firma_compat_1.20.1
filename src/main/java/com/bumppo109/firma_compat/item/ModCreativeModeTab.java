package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;
import java.util.function.Supplier;

public class ModCreativeModeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FirmaCompat.MODID);

    public static final RegistryObject<CreativeModeTab> FIRMA_COMPAT_TAB = CREATIVE_MODE_TABS.register("firma_compat_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(Items.OAK_PLANKS)) // ← Change this to one of your items/blocks later
                    .title(Component.translatable("creativetab.firma_compat_tab"))
                    .displayItems((parameters, output) -> {   // ← parameters + output (1.20.1 style)

                        for (CompatWood wood : CompatWood.VALUES) {
                            var woodBlocks = ModBlocks.WOODS.get(wood);
                            if (woodBlocks != null) {
                                woodBlocks.forEach((type, reg) -> {
                                    if (type.needsItem() && reg != null) {
                                        accept(output, reg);
                                    }
                                });
                            }

                            // Add lumber and supports
                            accept(output, ModItems.LUMBER, wood);
                            accept(output, ModItems.SUPPORTS, wood);
                        }
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    // Helper for a single Supplier
    private static void accept(CreativeModeTab.Output output, Supplier<? extends ItemLike> supplier) {
        if (supplier == null) {
            FirmaCompat.LOGGER.warn("Supplier was null when adding to creative tab");
            return;
        }

        ItemLike itemLike = supplier.get();
        if (itemLike == null || itemLike.asItem() == Items.AIR) {
            FirmaCompat.LOGGER.warn("Skipping invalid or AIR item in FirmaCompat creative tab");
            return;
        }

        output.accept(itemLike);
    }

    // Helper for Map<CompatWood, Supplier<ItemLike>>
    private static void accept(CreativeModeTab.Output output,
                               Map<CompatWood, ? extends Supplier<? extends ItemLike>> map,
                               CompatWood wood) {
        if (map == null) {
            FirmaCompat.LOGGER.warn("Map was null for wood: {}", wood);
            return;
        }

        Supplier<? extends ItemLike> supplier = map.get(wood);
        if (supplier != null) {
            accept(output, supplier);
        } else {
            FirmaCompat.LOGGER.warn("No supplier found for wood: {}", wood);
        }
    }
}