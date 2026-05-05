package com.bumppo109.firma_compat.util.chunkData;

import com.bumppo109.firma_compat.FirmaCompat;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraft.resources.ResourceLocation;

public class ModNetworking {

    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(FirmaCompat.MODID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    public static void register() {
        int id = 0;

        CHANNEL.registerMessage(
                id++,
                ClimateSyncPacket.class,
                ClimateSyncPacket::encode,
                ClimateSyncPacket::decode,
                ClimateSyncPacket::handle
        );
    }
}