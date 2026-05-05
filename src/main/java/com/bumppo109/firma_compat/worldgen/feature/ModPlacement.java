package com.bumppo109.firma_compat.worldgen.feature;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModPlacement {
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS =
            DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, FirmaCompat.MODID);

    public static final Supplier<PlacementModifierType<ClimatePlacement>> COMPAT_CLIMATE_PLACEMENT =
            PLACEMENT_MODIFIERS.register("climate", () -> ClimatePlacement.CODEC::codec);
}

