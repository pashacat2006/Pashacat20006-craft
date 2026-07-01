package com.pashacat2015.haha_mod.network;

import com.pashacat2015.haha_mod.quest.QuestManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class QuestClaimPacket {

    private final String questId;

    public QuestClaimPacket(String questId) {
        this.questId = questId;
    }

    public static void encode(QuestClaimPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.questId);
    }

    public static QuestClaimPacket decode(FriendlyByteBuf buf) {
        return new QuestClaimPacket(buf.readUtf());
    }

    public static void handle(QuestClaimPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                QuestManager.tryClaim(player, msg.questId);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void send(String questId) {
        PacketHandler.CHANNEL.sendToServer(new QuestClaimPacket(questId));
    }
}
