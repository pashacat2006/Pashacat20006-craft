package com.pashacat2015.haha_mod.Screen;

import com.pashacat2015.haha_mod.init.BlockMod;
import com.pashacat2015.haha_mod.init.block.entity.FriteuseEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Серверное и клиентское меню (контейнер) стола готовки.
 * Связывает слоты игрока со слотами BlockEntity и задаёт их позиции на экране.
 */
public class FriteuseMenu extends AbstractContainerMenu {
    public final FriteuseEntity blockenty;
    private final Level l;
    private final ContainerData dat;

    /** Конструктор для клиента — позиция блока приходит из сети */
    public FriteuseMenu(int ContanirId, Inventory inv, FriendlyByteBuf extradat) {
        this(ContanirId, inv, inv.player.level().getBlockEntity(extradat.readBlockPos()), new SimpleContainerData(2));
    }

    /** Конструктор для сервера */
    public FriteuseMenu(int contanirid, Inventory inv, BlockEntity entity, ContainerData dat1) {
        super(MenuType.FRITEUSE_MENU.get(), contanirid);
        checkContainerSize(inv, 3);
        blockenty = ((FriteuseEntity) entity);
        this.l = inv.player.level();
        this.dat = blockenty.data;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // Слоты стола — та же сетка, что у инвентаря (5 + col*16, row*20), но 3×3 и выше
        var handler = blockenty.getItemHandler();
        this.addSlot(new SlotItemHandler(handler, 0, 67, 27));  // Butter / масло
        this.addSlot(new SlotItemHandler(handler, 1, 67, 72));  // Food / еда
        this.addSlot(new SlotItemHandler(handler, 2, 136, 55)); // Результат
        addDataSlots(blockenty.data);
    }

    /** Идёт ли сейчас процесс готовки */
    public boolean isCrafting() {
        return dat.get(0) > 0;
    }

    /** Высота стрелки прогресса для отрисовки на экране */
    public int getScaledProgress() {
        int progress = this.dat.get(0);
        int maxProgress = this.dat.get(1);
        int progressArrowSize = 24;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    /** Три ряда основного инвентаря игрока */
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 12 + l * 18, 114 + i * 18));
            }
        }
    }

    /** Панель быстрого доступа (хотбар) */
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 9 + i * 18, 177));
        }
    }

    // --- Индексы слотов для Shift+клик (быстрый перенос) ---
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    private static final int TE_INVENTORY_SLOT_COUNT = 3;

    /** Shift+клик: перенос между инвентарём игрока и слотами стола */
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // Из инвентаря игрока — в стол
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // Из стола — в инвентарь игрока
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }

        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    /** Можно ли ещё пользоваться меню (игрок рядом с блоком) */
    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(l, blockenty.getBlockPos()), player, BlockMod.FRITEUSE_BLOCK.get());
    }
}
