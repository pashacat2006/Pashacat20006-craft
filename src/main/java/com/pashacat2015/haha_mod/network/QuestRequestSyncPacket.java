package com.pashacat2015.haha_mod.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class QuestRequestSyncPacket {

    public static void encode(QuestRequestSyncPacket msg, FriendlyByteBuf buf) {
    }

    public static QuestRequestSyncPacket decode(FriendlyByteBuf buf) {
        return new QuestRequestSyncPacket();
    }

    public static void handle(QuestRequestSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                QuestSyncPacket.sendToPlayer(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void send() {
        PacketHandler.CHANNEL.sendToServer(new QuestRequestSyncPacket());
    }
}
