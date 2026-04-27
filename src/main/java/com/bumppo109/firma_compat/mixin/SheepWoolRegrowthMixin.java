package com.bumppo109.firma_compat.mixin;

import com.bumppo109.firma_compat.config.FirmaCompatConfig;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Sheep.class)
public abstract class SheepWoolRegrowthMixin {

    @Shadow
    public abstract boolean isSheared();

    // Tracks when this specific sheep was last sheared (in world ticks)
    private long firmaCompat$lastShearedTime = -1;

    @Inject(method = "shear(Lnet/minecraft/sounds/SoundSource;)V", at = @At("TAIL"))
    private void onShear(net.minecraft.sounds.SoundSource source, CallbackInfo ci) {
        this.firmaCompat$lastShearedTime = ((Sheep) (Object) this).level().getGameTime();
    }

    @Inject(method = "ate()V", at = @At("HEAD"), cancellable = true)
    private void onAte(CallbackInfo ci) {
        Sheep sheep = (Sheep) (Object) this;

        // If the sheep is not sheared, allow normal behavior
        if (!this.isSheared()) {
            return;
        }

        long currentTime = sheep.level().getGameTime();
        int requiredDelay = FirmaCompatConfig.COMMON.sheepWoolRegrowthDelayTicks.get();

        // If delay is 0, do nothing (vanilla behavior)
        if (requiredDelay <= 0) {
            return;
        }

        // If not enough time has passed since last shearing, cancel regrowth
        if (this.firmaCompat$lastShearedTime != -1 &&
                (currentTime - this.firmaCompat$lastShearedTime < requiredDelay)) {

            ci.cancel(); // Prevent setSheared(false) this time
        }
    }
}