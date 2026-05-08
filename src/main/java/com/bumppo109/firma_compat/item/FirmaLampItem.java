package com.bumppo109.firma_compat.item;

import net.dries007.tfc.common.blocks.devices.LampBlock;
import net.dries007.tfc.common.capabilities.Capabilities;
import net.dries007.tfc.common.items.LampBlockItem;
import net.dries007.tfc.util.LampFuel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FirmaLampItem extends LampBlockItem {

    private static final String LIT_KEY = "firma_lit";

    public FirmaLampItem(Block block, Properties properties) {
        super(block, properties.stacksTo(1));
    }

    // ====================== Lit State ======================
    public static boolean isLit(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(LIT_KEY);
    }

    public static void setLit(ItemStack stack, boolean lit) {
        stack.getOrCreateTag().putBoolean(LIT_KEY, lit);
    }

    // ====================== Fuel ======================
    public static int getFuel(ItemStack stack) {
        return getFluidInside(stack).getAmount();
    }

    public static boolean hasFuel(ItemStack stack) {
        return getFuel(stack) > 0;
    }

    // ====================== Drain ======================
    private static void drainFuel(ItemStack stack, int amount) {
        stack.getCapability(Capabilities.FLUID_ITEM)
                .ifPresent(handler -> handler.drain(amount, FluidAction.EXECUTE));
    }

    // ====================== TFC Burn Logic ======================
    private static void drainFuelWithTFC(ItemStack stack, Level level) {
        FluidStack fluid = getFluidInside(stack);
        if (fluid.isEmpty()) return;

        Block block = ((BlockItem) stack.getItem()).getBlock();
        LampFuel fuel = LampFuel.get(fluid.getFluid(), block.defaultBlockState());
        if (fuel == null) return;

        int burnRate = fuel.getBurnRate();
        if (burnRate <= 0) return;

        if (level.getGameTime() % burnRate == 0) {
            drainFuel(stack, 1);
        }
    }

    // ====================== Inventory Tick ======================
    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, level, entity, slot, selected);

        if (!(entity instanceof Player player)) return;

        boolean isHeld = selected || ItemStack.isSameItemSameTags(player.getOffhandItem(), stack);
        if (!isHeld) return;

        // 🔥 CLIENT: particles
        if (level.isClientSide) {
            if (isLit(stack) && hasFuel(stack) && level.random.nextFloat() < 0.3f) {
                //spawnHeldFlame(level, player, selected);
            }
            return;
        }

        // ⛽ SERVER: burn fuel
        if (isLit(stack) && hasFuel(stack)) {
            boolean wasLit = true;

            drainFuelWithTFC(stack, level);

            if (!hasFuel(stack)) {
                setLit(stack, false);
                syncLightState(player);
            }
        }
    }

    // ====================== Particle ======================
    /*
    private static void spawnHeldFlame(Level level, Player player, boolean mainHand) {
        double offset = mainHand ? 0.3 : -0.3;

        double yaw = Math.toRadians(player.getYRot());
        double x = player.getX() + offset * Math.cos(yaw);
        double y = player.getY() + player.getEyeHeight() - 0.2;
        double z = player.getZ() + offset * Math.sin(yaw);

        level.addParticle(ParticleTypes.FLAME, x, y, z, 0, 0.01, 0);

        if (level.random.nextFloat() < 0.2f) {
            level.addParticle(ParticleTypes.SMOKE, x, y, z, 0, 0.01, 0);
        }
    }

     */

    // ====================== Placement ======================
    @Override
    protected boolean updateCustomBlockEntityTag(
            BlockPos pos,
            Level level,
            @Nullable Player player,
            ItemStack stack,
            BlockState state
    ) {
        boolean result = super.updateCustomBlockEntityTag(pos, level, player, stack, state);

        if (!level.isClientSide) {
            BlockState current = level.getBlockState(pos);

            if (current.hasProperty(LampBlock.LIT)) {
                level.setBlock(
                        pos,
                        current.setValue(LampBlock.LIT, isLit(stack)),
                        3
                );
            }
        }

        return result;
    }

    // ====================== Sync (optimized) ======================
    private static void syncLightState(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.getInventory().setChanged();
        }
    }

    // ====================== Prevent Bobbing ======================
    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        if (!slotChanged && ItemStack.isSameItem(oldStack, newStack)) {
            return false;
        }
        return super.shouldCauseReequipAnimation(oldStack, newStack, slotChanged);
    }

    // ====================== Tooltip ======================
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        tooltip.add(Component.literal("§7Fuel: " + getFuel(stack) + " mB"));
        tooltip.add(Component.literal(isLit(stack) ? "§6Lit" : "§8Unlit"));
    }

    // ====================== Name ======================
    @Override
    public String getDescriptionId(ItemStack stack) {
        if (!hasFuel(stack)) return super.getDescriptionId(stack) + ".empty";
        if (isLit(stack)) return super.getDescriptionId(stack) + ".lit";
        return super.getDescriptionId(stack) + ".filled";
    }

    // ====================== Glow ======================
    /*
    @Override
    public boolean isFoil(ItemStack stack) {
        return isLit(stack);
    }

     */

    // ====================== Helper ======================
    private static FluidStack getFluidInside(ItemStack stack) {
        return stack.getCapability(Capabilities.FLUID_ITEM)
                .map(cap -> cap.getFluidInTank(0))
                .orElse(FluidStack.EMPTY);
    }
}