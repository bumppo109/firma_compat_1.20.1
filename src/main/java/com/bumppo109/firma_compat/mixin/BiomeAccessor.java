package com.bumppo109.firma_compat.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Biome.class)
public interface BiomeAccessor {

    // Public method - no invoker needed, but we can expose the modified climate if wanted
    @Accessor("climateSettings")
    Biome.ClimateSettings getClimateSettings();   // optional, for future use

    // If you ever need the frozen modifier logic, we can add more later
}