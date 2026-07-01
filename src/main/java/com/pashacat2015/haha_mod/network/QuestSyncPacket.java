package com.pashacat2015.haha_mod.network;

import com.pashacat2015.haha_mod.Screen.CookingBookMenu;
import com.pashacat2015.haha_mod.quest.QuestProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.Set;
import java.util.function.Supplier;

public class QuestSyncPacket {

    private final Set<String> completedIds;

    public QuestSyncPacket(Set<String> completedIds) {
        this.completedIds = completedIds;
    }

    public static void encode(QuestSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.completedIds.size());
        for (String id : msg.completedIds) {
            buf.writeUtf(id);
        }
    }

    public static QuestSyncPacket decode(FriendlyByteBuf buf) {
        return new QuestSyncPacket(QuestProgress.readFromNetwork(buf));
    }

    public static void handle(QuestSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.player.containerMenu instanceof CookingBookMenu menu) {
                menu.updateCompleted(msg.completedIds);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static void sendToPlayer(ServerPlayer player) {
        PacketHandler.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new QuestSyncPacket(QuestProgress.getCompletedIds(player))
        );
    }
}
