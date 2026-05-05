package com.bumppo109.firma_compat.util.climate.models;

import com.bumppo109.firma_compat.FirmaCompat;
import com.bumppo109.firma_compat.util.chunkData.*;
import net.dries007.tfc.util.climate.BiomeBasedClimateModel;
import net.dries007.tfc.util.events.SelectClimateModelEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = FirmaCompat.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClimateEventHandler {

    private static final int RADIUS = 8;
    private static final Set<String> SENT = new HashSet<>();

    @SubscribeEvent
    public static void onSelectClimateModel(SelectClimateModelEvent event) {
        if (event.getModel() instanceof BiomeBasedClimateModel) {

            if(FirmaCompat.isEclipticLoaded && FirmaCompat.isLSOLoaded){
                event.setModel(EclipticLSOClimateModel.INSTANCE);
            } else if (FirmaCompat.isEclipticLoaded){
                event.setModel(EclipticSeasonsClimateModel.INSTANCE);
            } else {
                event.setModel(VanillaClimateModel.INSTANCE);
            }

            FirmaCompat.LOGGER.info("Applied Compat Climate Model for dimension: {}",
                    event.level() != null ? event.level().dimension().location() : "unknown");
        }
    }

    //Climate Caching
    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        ChunkPos pos = event.getChunk().getPos();

        int x = pos.getMiddleBlockX();
        int z = pos.getMiddleBlockZ();

        ClimateData data = VanillaNoiseSampler.sample(level, x, z);
        if (data == null) return;

        ServerClimateCache.put(level, pos, data);

        if (event.getChunk() instanceof net.minecraft.world.level.chunk.LevelChunk levelChunk) {
            ModNetworking.CHANNEL.send(
                    net.minecraftforge.network.PacketDistributor.TRACKING_CHUNK.with(() -> levelChunk),
                    new ClimateSyncPacket(pos, data)
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        int radius = 8; // chunks around player

        ChunkPos center = new ChunkPos(player.blockPosition());

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {

                ChunkPos pos = new ChunkPos(center.x + dx, center.z + dz);
                ClimateData data = ServerClimateCache.get(level, pos);

                if (data != null) {
                    ModNetworking.CHANNEL.sendTo(
                            new ClimateSyncPacket(pos, data),
                            player.connection.connection,
                            net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT
                    );
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        ChunkPos center = new ChunkPos(player.blockPosition());

        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {

                ChunkPos pos = new ChunkPos(center.x + dx, center.z + dz);

                String key = player.getUUID() + ":" + pos;

                // prevent spamming packets
                if (SENT.contains(key)) continue;

                ClimateData data = ServerClimateCache.get(level, pos);
                if (data == null) continue;

                ModNetworking.CHANNEL.sendTo(
                        new ClimateSyncPacket(pos, data),
                        player.connection.connection,
                        net.minecraftforge.network.NetworkDirection.PLAY_TO_CLIENT
                );

                SENT.add(key);
            }
        }
    }
}