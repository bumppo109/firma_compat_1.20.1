package com.bumppo109.firma_compat.util.chunkData;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClimateSyncPacket {

    private final ChunkPos pos;
    private final ClimateData data;

    public ClimateSyncPacket(ChunkPos pos, ClimateData data) {
        this.pos = pos;
        this.data = data;
    }

    public static void encode(ClimateSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeChunkPos(msg.pos);
        buf.writeFloat(msg.data.temperature);
        buf.writeFloat(msg.data.vegetation);
        buf.writeFloat(msg.data.continentalness);
        buf.writeFloat(msg.data.erosion);
        buf.writeFloat(msg.data.weirdness);
    }

    public static ClimateSyncPacket decode(FriendlyByteBuf buf) {
        ChunkPos pos = buf.readChunkPos();
        ClimateData data = new ClimateData(
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat(),
                buf.readFloat()
        );

        return new ClimateSyncPacket(pos, data);
    }

    public static void handle(ClimateSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientClimateCache.put(msg.pos, msg.data);
            //System.out.println("Received climate for chunk " + msg.pos);
            //Minecraft.getInstance().levelRenderer.allChanged();
        });
        ctx.get().setPacketHandled(true);
    }
}