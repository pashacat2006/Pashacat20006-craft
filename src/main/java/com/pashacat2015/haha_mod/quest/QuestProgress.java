package com.pashacat2015.haha_mod.quest;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public class QuestProgress {

    private static final String ROOT_KEY = "haha_mod_quests";

    private static CompoundTag tag(ServerPlayer player) {
        CompoundTag root = player.getPersistentData();
        if (!root.contains(ROOT_KEY)) {
            root.put(ROOT_KEY, new CompoundTag());
        }
        return root.getCompound(ROOT_KEY);
    }

    public static boolean isCompleted(ServerPlayer player, String questId) {
        return tag(player).getBoolean("done_" + questId);
    }

    public static void setCompleted(ServerPlayer player, String questId) {
        tag(player).putBoolean("done_" + questId, true);
    }

    public static boolean isRecipeUnlocked(ServerPlayer player, String recipeId) {
        return tag(player).getBoolean("recipe_" + recipeId);
    }

    public static void unlockRecipe(ServerPlayer player, String recipeId) {
        tag(player).putBoolean("recipe_" + recipeId, true);
    }

    public static Set<String> getUnlockedRecipeIds(ServerPlayer player) {
        Set<String> unlocked = new HashSet<>();
        CompoundTag root = tag(player);
        for (String key : root.getAllKeys()) {
            if (key.startsWith("recipe_") && root.getBoolean(key)) {
                unlocked.add(key.substring("recipe_".length()));
            }
        }
        return unlocked;
    }

    public static boolean hasFlag(ServerPlayer player, String flag) {
        return tag(player).getBoolean(flag);
    }

    public static void setFlag(ServerPlayer player, String flag, boolean value) {
        tag(player).putBoolean(flag, value);
    }

    public static Set<String> getCompletedIds(ServerPlayer player) {
        Set<String> done = new HashSet<>();
        for (Quest quest : QuestRegistry.allSorted()) {
            if (isCompleted(player, quest.id)) {
                done.add(quest.id);
            }
        }
        return done;
    }

    public static void writeToNetwork(ServerPlayer player, FriendlyByteBuf buf) {
        Set<String> done = getCompletedIds(player);
        buf.writeVarInt(done.size());
        for (String id : done) {
            buf.writeUtf(id);
        }
    }

    public static Set<String> readFromNetwork(FriendlyByteBuf buf) {
        Set<String> done = new HashSet<>();
        int count = buf.readVarInt();
        for (int i = 0; i < count; i++) {
            done.add(buf.readUtf());
        }
        return done;
    }
}
