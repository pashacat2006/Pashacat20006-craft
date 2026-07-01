package com.pashacat2015.haha_mod.item;

import com.pashacat2015.haha_mod.Screen.CookingBookMenu;
import com.pashacat2015.haha_mod.network.QuestSyncPacket;
import com.pashacat2015.haha_mod.quest.QuestProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class CookingBookItem extends Item {

    public CookingBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.translatable("gui.haha_mod.cookingbook");
                }

                @Override
                public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player p) {
                    return new CookingBookMenu(containerId, inventory);
                }
            }, buf -> QuestProgress.writeToNetwork(serverPlayer, buf));
            QuestSyncPacket.sendToPlayer(serverPlayer);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
