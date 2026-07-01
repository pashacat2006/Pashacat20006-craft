package com.pashacat2015.haha_mod.network;

import com.pashacat2015.haha_mod.cat_mod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(cat_mod.MODID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals
    );

    private static int id = 0;

    public static void register() {
        CHANNEL.registerMessage(id++, QuestClaimPacket.class,
                QuestClaimPacket::encode,
                QuestClaimPacket::decode,
                QuestClaimPacket::handle);

        CHANNEL.registerMessage(id++, QuestSyncPacket.class,
                QuestSyncPacket::encode,
                QuestSyncPacket::decode,
                QuestSyncPacket::handle);

        CHANNEL.registerMessage(id++, QuestRequestSyncPacket.class,
                QuestRequestSyncPacket::encode,
                QuestRequestSyncPacket::decode,
                QuestRequestSyncPacket::handle);
    }
}
