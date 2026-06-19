package com.pashacat2015.haha_mod.Screen;

import com.pashacat2015.haha_mod.init.BlockMod;
import com.pashacat2015.haha_mod.init.block.entity.CookingTableEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

/**
 * Серверное и клиентское меню (контейнер) стола готовки.
 * Связывает слоты игрока со слотами BlockEntity и задаёт их позиции на экране.
 */
public class CookingtableMenu extends AbstractContainerMenu {
    public final CookingTableEntity blockenty;
    private final Level l;
    private final ContainerData dat;

    /** Конструктор для клиента — позиция блока приходит из сети */
    public CookingtableMenu(int ContanirId, Inventory inv, FriendlyByteBuf extradat) {
        this(ContanirId, inv, inv.player.level().getBlockEntity(extradat.readBlockPos()), new SimpleContainerData(2));
    }

    /** Конструктор для сервера */
    public CookingtableMenu(int contanirid, Inventory inv, BlockEntity entity, ContainerData dat1) {
        super(MenuType.COOKINGTABLE_MENU.get(), contanirid);
        checkContainerSize(inv, 12);
        blockenty = ((CookingTableEntity) entity);
        this.l = inv.player.level();
        this.dat = dat1;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        // Слоты стола — та же сетка, что у инвентаря (5 + col*16, row*20), но 3×3 и выше
        this.blockenty.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            int xOffset = -55;
            int yOffset = -8;

            for (int row = 0; row < 3; ++row) {
                for (int col = 0; col < 3; ++col) {
                    this.addSlot(new SlotItemHandler(
                            iItemHandler,
                            row * 3 + col,
                            col * 22 + xOffset,
                            row * 23 + yOffset
                    ));
                }
            }
            this.addSlot(new SlotItemHandler(iItemHandler, 9, 58, 80));   // Cora / кора
            this.addSlot(new SlotItemHandler(iItemHandler, 10, 18, 80));  // Уголь
            this.addSlot(new SlotItemHandler(iItemHandler, 11, 118, 15)); // Результат
        });
        addDataSlots(dat1);
    }

    /** Идёт ли сейчас процесс готовки */
    public boolean isCrafting() {
        return dat.get(0) > 0;
    }

    /** Высота стрелки прогресса для отрисовки на экране */
    public int getScaledProgress() {
        int progress = this.dat.get(0);
        int maxProgress = this.dat.get(1);
        int progressArrowSize = 26;

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    /** Три ряда основного инвентаря игрока */
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 5 + l * 16, 125 + i * 20));
            }
        }
    }

    /** Панель быстрого доступа (хотбар) */
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18 - 6, 202));
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
    private static final int TE_INVENTORY_SLOT_COUNT = 12;

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
        return stillValid(ContainerLevelAccess.create(l, blockenty.getBlockPos()), player, BlockMod.COOKING_TABLE_BLOCK.get());
    }
}
