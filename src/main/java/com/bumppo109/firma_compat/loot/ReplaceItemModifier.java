package com.bumppo109.firma_compat.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ReplaceItemModifier extends LootModifier {

    public static final Supplier<Codec<ReplaceItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst).and(
                    inst.group(
                            ForgeRegistries.ITEMS.getCodec().fieldOf("from").forGetter(m -> m.fromItem),
                            ForgeRegistries.ITEMS.getCodec().fieldOf("to").forGetter(m -> m.toItem),
                            Codec.INT.optionalFieldOf("count", 1).forGetter(m -> m.countMultiplier),
                            Codec.STRING.optionalFieldOf("modid", "").forGetter(m -> m.modid)
                    )
            ).apply(inst, ReplaceItemModifier::new)));

    private final Item fromItem;
    private final Item toItem;
    private final int countMultiplier;
    private final String modid;   // empty = no mod condition

    // Constructor without modid (for backward compatibility)
    public ReplaceItemModifier(LootItemCondition[] conditionsIn, Item from, Item to, int countMultiplier) {
        this(conditionsIn, from, to, countMultiplier, "");
    }

    // Main constructor
    public ReplaceItemModifier(LootItemCondition[] conditionsIn, Item from, Item to, int countMultiplier, String modid) {
        super(conditionsIn);
        this.fromItem = from;
        this.toItem = to;
        this.countMultiplier = countMultiplier;
        this.modid = modid != null ? modid : "";
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        for (int i = 0; i < generatedLoot.size(); i++) {
            if (generatedLoot.get(i).is(fromItem)) {
                int newCount = generatedLoot.get(i).getCount() * countMultiplier;
                generatedLoot.set(i, new ItemStack(toItem, newCount));
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}