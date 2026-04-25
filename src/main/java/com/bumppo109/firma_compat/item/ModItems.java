package com.bumppo109.firma_compat.item;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.block.CompatMetal;
import com.bumppo109.firma_compat.block.CompatRock;
import com.bumppo109.firma_compat.block.CompatWood;
import com.bumppo109.firma_compat.block.ModBlocks;
import com.bumppo109.firma_compat.fluid.ModFluids;
import net.dries007.tfc.common.entities.TFCEntities;
import net.dries007.tfc.common.items.JarItem;
import net.dries007.tfc.common.items.TFCMinecartItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, FirmaCompat.MODID);

    //Food
    /*
    public static final RegistryObject<Item> SWEET_BERRIES_JAR = register("sweet_berries_jar",
            () -> new JarItem(new Item.Properties(),
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/jar/sweet_berries"),false)
    );
    public static final RegistryObject<Item> SWEET_BERRIES_JAR_UNSEALED = register("sweet_berries_jar_unsealed",
            () -> new JarItem(new Item.Properties(),
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/jar/sweet_berries_unsealed"),true)
    );
    public static final RegistryObject<Item> SWEET_BERRIES_JAM = register("sweet_berries_jam",
            () -> new Item((new Item.Properties())));

    public static final RegistryObject<Item> GLOW_BERRIES_JAR = register("glow_berries_jar",
            () -> new JarItem(new Item.Properties(),
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/jar/glow_berries"),false)
    );
    public static final RegistryObject<Item> GLOW_BERRIES_JAR_UNSEALED = register("glow_berries_jar_unsealed",
            () -> new JarItem(new Item.Properties(),
                    ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "block/jar/glow_berries_unsealed"),true)
    );
    public static final RegistryObject<Item> GLOW_BERRIES_JAM = register("glow_berries_jam",
            () -> new Item((new Item.Properties())));

     */


    // Wood
    public static final Map<CompatWood, RegistryObject<Item>> LUMBER = Helpers.mapOfKeys(CompatWood.class, wood -> register(wood.name() + "_lumber"));

    public static final Map<CompatWood, RegistryObject<Item>> SUPPORTS = Helpers.mapOfKeys(CompatWood.class, wood ->
            register(wood.name() + "_support", () -> new StandingAndWallBlockItem(ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.VERTICAL_SUPPORT).get(), ModBlocks.WOODS.get(wood).get(CompatWood.BlockType.HORIZONTAL_SUPPORT).get(), new Item.Properties(), Direction.DOWN))
    );

    public static final RegistryObject<Item> BAMBOO_LUMBER = register("bamboo_lumber");

    public static final RegistryObject<Item> COMPAT_CHEST_MINECART = register("compat_chest_minecart",
            (() -> new TFCMinecartItem(new Item.Properties(), TFCEntities.CHEST_MINECART,
                    () -> ModBlocks.COMPAT_CHEST.get().asItem())));


    /*TODO - hanging signs
    public static final Map<CompatWood, Map<Metal.Default, RegistryObject<Item>>> HANGING_SIGNS = Helpers.mapOfKeys(CompatWood.class, wood ->
            Helpers.mapOfKeys(Metal.Default.class, Metal.Default::hasUtilities, metal ->
                    register("wood/hanging_sign/" + metal.name() + "/" + wood.name(), () -> new HangingSignItem(ModBlocks.CEILING_HANGING_SIGNS.get(wood).get(metal).get(), ModBlocks.WALL_HANGING_SIGNS.get(wood).get(metal).get(), new Item.Properties()))
            )
    );
     */

    //Rock
    public static final Map<CompatRock, RegistryObject<Item>> BRICK = Helpers.mapOfKeys(
            CompatRock.class,
            rock -> {
                String name = rock == CompatRock.BLACKSTONE
                        ? "polished_blackstone_brick"
                        : rock.name().toLowerCase(Locale.ROOT) + "_brick";
                return register(name);
            }
    );
    public static final RegistryObject<Item> DEEPSLATE_TILE = register("deepslate_tile");
    public static final RegistryObject<Item> QUARTZ_BRICK = register("quartz_brick");
    public static final RegistryObject<Item> PRISMARINE_BRICK = register("prismarine_brick");

    //Misc
    public static final RegistryObject<Item> MUD_BRICK = register("mud_brick");
    public static final RegistryObject<Item> UNFIRED_POT = register("unfired_pot");

    //Metals
    public static final RegistryObject<Item> NETHERITE_SCRAP_INGOT = register("netherite_scrap_ingot");


    public static final Map<CompatMetal, Map<CompatMetal.ItemType, RegistryObject<Item>>> METAL_ITEMS =
            Helpers.mapOfKeys(CompatMetal.class, metal ->
                    Helpers.mapOfKeys(CompatMetal.ItemType.class,
                            type -> type.has(metal),                    // ← THIS FILTER IS REQUIRED
                            type -> register(
                                    metal.getSerializedName() + "_" + type.name().toLowerCase(Locale.ROOT),
                                    () -> type.create(metal)
                            )
                    )
            );

    /*
    public static final Map<CompatMetal, Map<CompatMetal.ItemType, RegistryObject<Item>>> METAL_ITEMS = Helpers.mapOfKeys(CompatMetal.class, metal ->
            Helpers.mapOfKeys(CompatMetal.ItemType.class, type ->
                    register(metal.name() + "_" + type.name(), () -> type.create(metal))
            )
    );

     */

    public static final Map<CompatMetal, RegistryObject<BucketItem>> METAL_FLUID_BUCKETS = Helpers.mapOfKeys(CompatMetal.class, metal ->
            register("molten_" + metal.name() + "_bucket", () -> new BucketItem(ModFluids.METALS.get(metal).source(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)))
    );

    private static RegistryObject<Item> register(String name)
    {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item)
    {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }

}
