package com.bumppo109.firma_compat.event;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.util.ModTags;
import net.dries007.tfc.common.entities.TFCEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID)
public class MobSpawnControl {

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide() || !(event.getEntity() instanceof Mob mob)) {
            return;
        }

        // Skip entities that were loaded from disk (chunk load, dimension travel, etc.)
        if (event.loadedFromDisk()) {
            return;
        }

        // Replace specific vanilla mobs with TFC equivalents
        EntityType<?> replacement = getReplacement(mob.getType());

        if (replacement != null) {
            // Cancel the original spawn
            event.setCanceled(true);

            // Spawn the replacement instead
            Mob newMob = (Mob) replacement.create(event.getLevel());
            if (newMob != null) {
                newMob.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
                // Optional: copy some data like health, NBT, etc. if needed
                event.getLevel().addFreshEntity(newMob);
            }
        }

        else if (mob.getType().is(ModTags.Entities.REMOVE_ENTITIES)) {
            event.setCanceled(true);
        }
    }

    /**
     * Returns the TFC replacement EntityType for a given vanilla mob, or null if no replacement.
     */
    private static EntityType<?> getReplacement(EntityType<?> original) {
        if (original == EntityType.COW) {
            return TFCEntities.COW.get();
        }
        if (original == EntityType.SHEEP) {
            return TFCEntities.SHEEP.get();
        }
        if (original == EntityType.PIG) {
            return TFCEntities.PIG.get();
        }
        if (original == EntityType.CHICKEN) {
            return TFCEntities.CHICKEN.get();
        }
        if (original == EntityType.HORSE) {
            return TFCEntities.HORSE.get();
        }
        if (original == EntityType.MULE) {
            return TFCEntities.MULE.get();
        }
        if (original == EntityType.GOAT) {
            return TFCEntities.GOAT.get();
        }
        if (original == EntityType.RABBIT) {
            return TFCEntities.RABBIT.get();
        }


        return null; // no replacement
    }
}