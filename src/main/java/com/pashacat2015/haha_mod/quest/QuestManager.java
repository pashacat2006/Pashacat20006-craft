package com.pashacat2015.haha_mod.quest;

import com.pashacat2015.haha_mod.cat_mod;
import com.pashacat2015.haha_mod.network.QuestSyncPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.Recipe;

import java.util.List;
import java.util.Set;

public class QuestManager {

    public static boolean isCompleted(Set<String> completed, Quest quest) {
        return completed.contains(quest.id);
    }

    public static boolean canClaim(Set<String> completed, Quest quest) {
        if (isCompleted(completed, quest)) {
            return false;
        }
        return !isLockedByPriority(completed, quest);
    }

    public static boolean isLockedByPriority(Set<String> completed, Quest quest) {
        if (isCompleted(completed, quest)) {
            return false;
        }
        for (Quest other : QuestRegistry.allSorted()) {
            if (other.priority < quest.priority && !completed.contains(other.id)) {
                return true;
            }
        }
        return false;
    }

    public static Quest getBlockingQuest(Set<String> completed, Quest quest) {
        for (Quest other : QuestRegistry.allSorted()) {
            if (other.priority < quest.priority && !completed.contains(other.id)) {
                return other;
            }
        }
        return null;
    }

    public static void tryClaim(ServerPlayer player, String questId) {
        Quest quest = QuestRegistry.get(questId);
        if (quest == null) {
            return;
        }

        Set<String> completed = QuestProgress.getCompletedIds(player);

        if (isCompleted(completed, quest)) {
            player.sendSystemMessage(Component.translatable("quest.haha_mod.already_completed"));
            return;
        }

        Quest blocker = getBlockingQuest(completed, quest);
        if (blocker != null) {
            player.sendSystemMessage(Component.translatable(
                    "quest.haha_mod.need_previous", blocker.title));
            return;
        }

        if (!quest.condition.test(player)) {
            player.sendSystemMessage(Component.translatable("quest.haha_mod.condition_not_met"));
            return;
        }

        QuestProgress.setCompleted(player, quest.id);
        unlockRewardRecipes(player, quest.rewardRecipeIds);

        player.sendSystemMessage(Component.translatable("quest.haha_mod.completed", quest.title));
        QuestSyncPacket.sendToPlayer(player);
    }

    private static void unlockRewardRecipes(ServerPlayer player, List<String> recipeIds) {
        if (recipeIds.isEmpty()) {
            return;
        }

        for (String recipeId : recipeIds) {
            ResourceLocation recipeLoc = new ResourceLocation(cat_mod.MODID, recipeId);
            var recipeManager = player.server.getRecipeManager();

            recipeManager.byKey(recipeLoc).ifPresentOrElse(recipe -> {
                QuestProgress.unlockRecipe(player, recipeId);
                player.awardRecipes(List.of(recipe));
                player.sendSystemMessage(Component.translatable(
                        "quest.haha_mod.recipe_unlocked", recipeId));
            }, () -> player.sendSystemMessage(Component.translatable(
                    "quest.haha_mod.recipe_not_found", recipeLoc.toString())));
        }
    }
}
