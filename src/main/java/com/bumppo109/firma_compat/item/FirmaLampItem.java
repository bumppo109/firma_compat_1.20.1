package com.bumppo109.firma_compat.item;

import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.items.LampBlockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FirmaLampItem extends LampBlockItem {

    private static final String FUEL_KEY = "firma_fuel";
    private final int maxFuel;

    public FirmaLampItem(Block block, Properties properties, int maxFuel) {
        super(block, properties.stacksTo(1));
        this.maxFuel = maxFuel;
    }

    // ====================== Fuel NBT ======================
    public static int getFuel(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(FUEL_KEY)) {
            syncFuelFromFluid(stack);   // Lazy sync when first accessed
        }
        return tag.getInt(FUEL_KEY);
    }

    public static void setFuel(ItemStack stack, int fuel) {
        stack.getOrCreateTag().putInt(FUEL_KEY, Mth.clamp(fuel, 0, 20));
    }

    public static boolean isLit(ItemStack stack) {
        return getFuel(stack) > 0;
    }

    // ====================== Sync fuel with TFC fluid amount ======================
    private static void syncFuelFromFluid(ItemStack stack) {
        FluidStack fluid = getFluidInside(stack);   // from parent class
        int fuel = fluid.isEmpty() ? 0 : Math.min(20, fluid.getAmount() / 50); // Adjust divisor as needed
        setFuel(stack, fuel);
    }

    // ====================== Fuel Consumption While Held ======================
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);

        if (level.isClientSide) return;
        if (!(entity instanceof Player player)) return;

        // Only drain when held
        if (!selected && player.getOffhandItem() != stack) return;

        int fuel = getFuel(stack);
        if (fuel > 0 && level.getGameTime() % 40 == 0) {     // every 2 seconds
            setFuel(stack, fuel - 1);

            if (getFuel(stack) == 0) {
                player.inventoryMenu.sendAllDataToRemote();   // Refresh for dynamic lighting
            }
        }
    }

    // ====================== Tooltip ======================
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag); // Keep original TFC fluid tooltip

        int fuel = getFuel(stack);
        if (maxFuel > 0) {
            tooltip.add(Component.literal("§7Fuel: " + fuel + " / " + maxFuel));
        }
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if (getFuel(stack) == 0 && !getFluidInside(stack).isEmpty()) {
            return super.getDescriptionId(stack) + ".empty";
        }
        return super.getDescriptionId(stack);
    }

    private static FluidStack getFluidInside(ItemStack stack) {
        return (FluidStack)stack.getCapability(Capabilities.FLUID_ITEM).map((cap) -> cap.getFluidInTank(0)).orElse(FluidStack.EMPTY);
    }
}