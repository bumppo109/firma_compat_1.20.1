package com.bumppo109.firma_compat.mixin;

import com.bumppo109.firma_compat.config.FirmaCompatConfig;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Cow.class)
public abstract class CowMilkingCooldownMixin {

    // Tracks the last time this specific cow was successfully milked (in game ticks)
    private long firmaCompat$lastMilkedTime = -1;

    @Inject(
            method = "mobInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void onMilkingAttempt(Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResult> cir) {
        Cow cow = (Cow) (Object) this;
        ItemStack heldItem = pPlayer.getItemInHand(pHand);

        // Use your existing tag: ModTags.Items.MILKING_ITEMS
        if (!heldItem.is(com.bumppo109.firma_compat.util.ModTags.Items.MILKING_ITEMS) || cow.isBaby()) {
            return;
        }

        long currentTime = cow.level().getGameTime();
        int requiredDelay = FirmaCompatConfig.COMMON.cowMilkingDelayTicks.get();

        // If delay is disabled (0), allow vanilla behavior
        if (requiredDelay <= 0) {
            return;
        }

        // Check if the cow is still on cooldown
        if (this.firmaCompat$lastMilkedTime != -1 &&
                (currentTime - this.firmaCompat$lastMilkedTime < requiredDelay)) {

            // Optional: Add subtle feedback when player tries too early
            // pPlayer.playSound(net.minecraft.sounds.SoundEvents.COW_HURT, 0.6F, 1.2F);

            cir.setReturnValue(InteractionResult.FAIL);
            cir.cancel();
            return;
        }

        // Milking successful → start the cooldown
        this.firmaCompat$lastMilkedTime = currentTime;
    }
}