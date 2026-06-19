package com.pashacat2015.haha_mod.init.block.entity;

import com.pashacat2015.haha_mod.Screen.CookingtableMenu;
import com.pashacat2015.haha_mod.init.itemMain;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * BlockEntity стола готовки.
 * Хранит 12 слотов, прогресс крафта и синхронизирует данные с GUI.
 */
public class CookingTableEntity extends BlockEntity implements MenuProvider {
    /** Инвентарь стола: 12 слотов */
    private final ItemStackHandler itemHandrel = new ItemStackHandler(12);

    // --- Номера слотов в инвентаре ---
    private static final int INPUT_SLOT = 0;       // Основной слот ингредиента (шампур)
    private static final int INPUT_SLOT1 = 1;      // Слот ингредиента 2
    private static final int INPUT_SLOT2 = 2;
    private static final int INPUT_SLOT3 = 3;
    private static final int INPUT_SLOT4 = 4;
    private static final int INPUT_SLOT5 = 5;
    private static final int INPUT_SLOT6 = 6;
    private static final int INPUT_SLOT7 = 7;
    private static final int INPUT_SLOT8 = 8;      // Слот ингредиента 9 (сетка 3x3)
    private static final int INPUT_SLOTCORA = 9;   // Слот Cora / кора
    private static final int INPUT_SLOTCOOL = 10;  // Слот топлива (уголь)
    private static final int OUTPUT_SLOT = 11;     // Слот результата

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    /** Данные для синхронизации прогресса готовки с клиентом (GUI) */
    protected final ContainerData data;
    private int progress = 0;
    private int maxprogress = 1200;

    public CookingTableEntity(BlockPos pos, BlockState state) {
        super(com.pashacat2015.haha_mod.init.block.entity.BlockEntity.COOKING_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> CookingTableEntity.this.progress;
                    case 1 -> CookingTableEntity.this.maxprogress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> CookingTableEntity.this.progress = value;
                    case 1 -> CookingTableEntity.this.maxprogress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    /** Поддержка автоматизации: другие моды могут читать/писать инвентарь через capability */
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandrel);
    }

    /** Выбросить все предметы из инвентаря при разрушении блока */
    public void drops() {
        SimpleContainer inmventory = new SimpleContainer(itemHandrel.getSlots());
        for (int i = 0; i < itemHandrel.getSlots(); i++) {
            inmventory.setItem(i, itemHandrel.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inmventory);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.haha_mod.cookingtable");
    }

    /** Создание серверного меню (контейнера) для игрока */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new CookingtableMenu(containerId, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandrel.serializeNBT());
        tag.putInt("cookingtable.progress", progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandrel.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("cookingtable.progress");
    }

    /** Вызывается каждый тик сервера — логика готовки */
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            setChanged(level, blockPos, blockState);
            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    /** Завершение крафта: забираем шампур, кладём Cora в выход */
    private void craftItem() {
        ItemStack result = new ItemStack(itemMain.CORA.get());
        this.itemHandrel.extractItem(INPUT_SLOT, 1, false);
        this.itemHandrel.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandrel.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasProgressFinished() {
        return progress >= maxprogress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    /** Проверка рецепта: шампур во входе и место в выходе */
    private boolean hasRecipe() {
        boolean hasCraftingitem = this.itemHandrel.getStackInSlot(INPUT_SLOT).getItem() == itemMain.SKEWER.get();
        ItemStack item = new ItemStack(itemMain.CORA.get());
        return hasCraftingitem && canInsertOutputSlot(item.getCount()) && canInsertItemOutputSlot(item.getItem());
    }

    private boolean canInsertItemOutputSlot(Item item) {
        return this.itemHandrel.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandrel.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertOutputSlot(int count) {
        return this.itemHandrel.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandrel.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }
}
