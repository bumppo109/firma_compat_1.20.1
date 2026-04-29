package com.bumppo109.firma_compat.addons.firmaciv;

import com.alekiponi.firmaciv.common.block.CanoeComponentBlock;
import com.alekiponi.firmaciv.common.block.FirmacivBlocks;
import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class CompatFirmaCivBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, FirmaCompat.MODID);

    private static final Map<CompatWatercraftMaterial, RegistryObject<CanoeComponentBlock>> CANOE_COMPONENTS = new HashMap<>();

    public static void init(IEventBus bus) {
        for (CompatWatercraftMaterial material : CompatWatercraftMaterial.values()) {
            if (material.isSoftwood) {
                registerCanoeComponent(material);
            }
            // TODO: Add boat frame registration for hardwoods later
        }

        BLOCKS.register(bus);
    }

    private static void registerCanoeComponent(CompatWatercraftMaterial material) {
        String name = "canoe_component/" + material.getSerializedName();

        RegistryObject<CanoeComponentBlock> block = BLOCKS.register(name, () -> {
            // Safely get the properties AFTER FirmaCiv+ has registered its blocks
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(
                    getBoatFrameFlat()
            );

            return new CanoeComponentBlock(properties, material);
        });

        CANOE_COMPONENTS.put(material, block);
    }

    /**
     * Lazy getter to avoid accessing FirmaCiv+ blocks too early
     */
    private static Block getBoatFrameFlat() {
        if (FirmacivBlocks.BOAT_FRAME_FLAT.get() == null) {
            throw new IllegalStateException("Firmaciv BOAT_FRAME_FLAT is not registered yet! Registration order issue.");
        }
        return FirmacivBlocks.BOAT_FRAME_FLAT.get();
    }

    private static void registerBoatFrames(CompatWatercraftMaterial material) {
        // Placeholder for future hard wood boat frames
    }

    public static Map<CompatWatercraftMaterial, RegistryObject<CanoeComponentBlock>> getCanoeComponents() {
        return CANOE_COMPONENTS;
    }
}