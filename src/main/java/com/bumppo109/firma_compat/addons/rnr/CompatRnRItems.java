package com.bumppo109.firma_compat.addons.rnr;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import net.dries007.tfc.util.Helpers;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class CompatRnRItems {
    public static final DeferredRegister<Item> ITEMS;
    public static final Map<CompatWood, RegistryObject<Item>> WOOD_SHINGLE;
    public static final Map<CompatRock, RegistryObject<Item>> FLAGSTONE;
    public static final RegistryObject<Item> GRAVEL_FILL;

    private static RegistryObject<Item> register(String name) {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, FirmaCompat.MODID);

        WOOD_SHINGLE = Helpers.mapOfKeys(CompatWood.class, (wood) -> register(wood.getSerializedName() + "_shingle"));
        FLAGSTONE = Helpers.mapOfKeys(CompatRock.class, (type) -> register(type.name() + "_flagstone"));
        GRAVEL_FILL = register("gravel_fill");
    }
}
