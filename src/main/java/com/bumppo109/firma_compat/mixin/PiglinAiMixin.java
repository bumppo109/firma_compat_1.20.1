package com.bumppo109.firma_compat.mixin;

import com.bumppo109.firma_compat.util.ModTags;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {

    /**
     * Redirects the core bartering check in PiglinAi
     */
    @Redirect(
            method = "isBarterCurrency(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z")
    )
    private static boolean firmaCompat_isBarterCurrency(ItemStack stack, net.minecraft.world.item.Item ignored) {
        return stack.is(ModTags.Items.PIGLIN_BARTERING_ITEMS);
    }

    /**
     * Safety redirect for isPiglinCurrency() coming from IForgeItem
     */
    @Redirect(
            method = "canAdmire(Lnet/minecraft/world/entity/monster/piglin/Piglin;Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isPiglinCurrency()Z")
    )
    private static boolean firmaCompat_canAdmire_redirect(ItemStack stack) {
        return stack.is(ModTags.Items.PIGLIN_BARTERING_ITEMS);
    }

    /**
     * Safety redirect when stopping holding item during bartering
     */
    @Redirect(
            method = "stopHoldingOffHandItem(Lnet/minecraft/world/entity/monster/piglin/Piglin;Z)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isPiglinCurrency()Z")
    )
    private static boolean firmaCompat_stopHolding_redirect(ItemStack stack) {
        return stack.is(ModTags.Items.PIGLIN_BARTERING_ITEMS);
    }
}