package com.bumppo109.firma_compat.datagen;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.datagen.assets.BuiltinBlockStateProvider;
import com.bumppo109.firma_compat.datagen.assets.BuiltinItemModelProvider;
import com.bumppo109.firma_compat.datagen.assets.BuiltinLang;
import com.bumppo109.firma_compat.datagen.assets.StoneZoneLang;
import net.dries007.tfc.TerraFirmaCraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class DataEntryPoint {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> vanillaLookup = event.getLookupProvider();

        //Worldgen
        final var builtinEntries = add(event, new DatapackBuiltinEntriesProvider(
                packOutput,
                vanillaLookup,
                Set.of(FirmaCompat.MODID, TerraFirmaCraft.MOD_ID, "minecraft")
        ));
        final var lookup = builtinEntries.getRegistryProvider();

        // ── TFC compat providers ──
        //final var fluidHeat = add(event, new BuiltinFluidHeat(packOutput, lookup)).output();
        //final var itemHeat = add(event, new BuiltinItemHeat(packOutput, lookup, fluidHeat));

        //final var drinkables = add(event, new BuiltinDrinkables(packOutput, lookup)).output();

        //add(event, new BuiltinRecipes(packOutput, lookup, CompletableFuture.allOf(fluidHeat), itemHeat));

        //final var blockTags = add(event, new BuiltinBlockTags(event, lookup)).contentsGetter();

        //add(event, new BuiltinItemTags(event, lookup, blockTags));
        //add(event, new BuiltinFluidTags(event, lookup, drinkables));
        //add(event, new BuiltinPlacedFeatureTags(packOutput, vanillaLookup, existingFileHelper));
        //add(event, new BuiltinBiomeTags(packOutput, vanillaLookup, existingFileHelper));


        //add(event, new BuiltinDeposits(packOutput, lookup));
        //add(event, new BuiltinFoods(packOutput, lookup));
        //add(event, new BuiltinFuels(packOutput, lookup));
        //add(event, new BuiltinSupports(packOutput, lookup));
        //add(event, new BuiltinFauna(packOutput, lookup));

        // ── Assets & loot ──
        generator.addProvider(event.includeServer(), new BuiltinItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeServer(), new BuiltinBlockStateProvider(packOutput, existingFileHelper));
        event.getGenerator().addProvider(true, new BuiltinLang(packOutput));
        //event.getGenerator().addProvider(true, new BuiltinLootTableProvider(packOutput, vanillaLookup));
        //event.getGenerator().addProvider(true, new BuiltinLootModifier(packOutput, vanillaLookup));
    }

    private static <T extends DataProvider> T add(GatherDataEvent event, T provider) {
        return event.getGenerator().addProvider(true, provider);
    }

    private static <T> void tags(GatherDataEvent event, ResourceKey<Registry<T>> registry,
                                 CompletableFuture<HolderLookup.Provider> lookup,
                                 BiConsumer<HolderLookup.Provider, TagLookup<T>> callback) {
        add(event, new TagsProvider<T>(event.getGenerator().getPackOutput(), registry, lookup,
                FirmaCompat.MODID, event.getExistingFileHelper()) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {
                callback.accept(provider, this::tag);
            }
        });
    }

    @FunctionalInterface
    interface TagLookup<T> {
        TagsProvider.TagAppender<T> tag(TagKey<T> tag);
    }
}