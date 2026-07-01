package com.pashacat2015.haha_mod.Screen;

import com.pashacat2015.haha_mod.quest.Quest;
import com.pashacat2015.haha_mod.quest.QuestManager;
import com.pashacat2015.haha_mod.quest.QuestProgress;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class CookingBookMenu extends AbstractContainerMenu {

    private final Set<String> completedQuestIds = new HashSet<>();

    public CookingBookMenu(int containerId, Inventory playerInv, FriendlyByteBuf buf) {
        this(containerId, playerInv);
        completedQuestIds.addAll(QuestProgress.readFromNetwork(buf));
    }

    public CookingBookMenu(int containerId, Inventory playerInv) {
        super(MenuType.COOKINGBOOK_MENU.get(), containerId);
    }

    public void updateCompleted(Set<String> ids) {
        completedQuestIds.clear();
        completedQuestIds.addAll(ids);
    }

    public boolean isQuestDone(String questId) {
        return completedQuestIds.contains(questId);
    }

    public boolean isQuestDone(Quest quest) {
        return QuestManager.isCompleted(completedQuestIds, quest);
    }

    public boolean canClaimQuest(Quest quest) {
        return QuestManager.canClaim(completedQuestIds, quest);
    }

    public boolean isQuestLocked(Quest quest) {
        return QuestManager.isLockedByPriority(completedQuestIds, quest);
    }

    public Quest getBlockingQuest(Quest quest) {
        return QuestManager.getBlockingQuest(completedQuestIds, quest);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
