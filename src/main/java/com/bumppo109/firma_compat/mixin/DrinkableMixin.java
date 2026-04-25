package com.bumppo109.firma_compat.mixin;

import net.dries007.tfc.common.TFCEffects;
import net.dries007.tfc.common.fluids.TFCFluids;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Drinkable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Drinkable.class)
public abstract class DrinkableMixin {

    @Inject(
            method = "attemptDrink(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Z)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void requireCrouchAndHandleSaltWaterThirst(
            Level level,
            Player player,
            boolean doDrink,
            CallbackInfoReturnable<InteractionResult> cir
    ) {
        // Force crouch requirement for all drinking
        if (!player.isCrouching()) {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
            return;
        }

        // Only apply extra thirst penalty on server when actually drinking
        if (level.isClientSide() || !doDrink) {
            return;
        }

        // Use TFC's own ray tracing helper (this is what TFC uses internally)
        BlockHitResult hit = Helpers.rayTracePlayer(level, player, net.minecraft.world.level.ClipContext.Fluid.SOURCE_ONLY);

        if (hit.getType() != HitResult.Type.BLOCK) {
            return;
        }

        BlockPos pos = hit.getBlockPos();
        FluidState fluidState = level.getFluidState(pos);

        // Apply extra thirst if player drinks salt water
        if (isSaltWater(fluidState)) {
            player.addEffect(new MobEffectInstance(
                    TFCEffects.THIRST.get(),
                    600,   // 30 seconds
                    1,     // amplifier
                    false,
                    true,
                    true
            ));
        }
    }

    private static boolean isSaltWater(FluidState fluidState) {
        if (fluidState.isEmpty()) {
            return false;
        }

        // Primary check: TFC Salt Water
        if (fluidState.is(TFCFluids.SALT_WATER.getSource())) {
            return true;
        }

        // Fallback: any ocean biome water (for vanilla oceans or other mods)
        // We can get biome here if needed, but fluid check is more reliable
        return false;
    }
}