package com.pashacat2015.haha_mod.init.block.entity;

import com.pashacat2015.haha_mod.Screen.FriteuseMenu;
import com.pashacat2015.haha_mod.recipe.FriteuseRecipe;
import com.pashacat2015.haha_mod.recipe.RecipreMod;
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

import java.util.Optional;

/**
 * BlockEntity стола готовки.
 * Хранит 12 слотов, прогресс крафта и синхронизирует данные с GUI.
 */
public class FriteuseEntity extends BlockEntity implements MenuProvider {
    /** Инвентарь стола: 12 слотов */
    private final ItemStackHandler itemHandrel = new ItemStackHandler(3);

    // --- Номера слотов в инвентаре ---
    private static final int INPUT_SLOT = 0;       // Основной слот ингредиента (шампур)
    private static final int INPUT_SLOT1 = 1;      // Слот ингредиента 2// Слот топлива (уголь)
    private static final int OUTPUT_SLOT = 2;     // Слот результата

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public ItemStackHandler getItemHandler() {
        return itemHandrel;
    }

    /** Данные для синхронизации прогресса готовки с клиентом (GUI) */
    public final ContainerData data;
    private int progress = 0;
    private int maxprogress = 600;

    public FriteuseEntity(BlockPos pos, BlockState state) {
        super(com.pashacat2015.haha_mod.init.block.entity.BlockEntity.FRITE_BE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FriteuseEntity.this.progress;
                    case 1 -> FriteuseEntity.this.maxprogress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FriteuseEntity.this.progress = value;
                    case 1 -> FriteuseEntity.this.maxprogress = value;
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
        return Component.translatable("block.haha_mod.friteuse");
    }

    /** Создание серверного меню (контейнера) для игрока */
    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new FriteuseMenu(containerId, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", itemHandrel.serializeNBT());
        tag.putInt("friteuse.progress", progress);
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandrel.deserializeNBT(tag.getCompound("inventory"));
        progress = tag.getInt("friteuse.progress");
    }

    /** Вызывается каждый тик сервера — логика готовки */
    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (level.isClientSide()) {
            return;
        }
        boolean wasCrafting = progress > 0;
        if (hasRecipe()) {
            increaseCraftingProgress();
            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
        if (wasCrafting || progress > 0) {
            setChanged(level, blockPos, blockState);
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    /** Завершение крафта: забираем шампур, кладём Cora в выход */
    private void craftItem() {
        Optional<FriteuseRecipe> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return;
        }
        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());
        for (int i = 0; i < INPUT_SLOT1 + 1; i++) {
            int count = recipe.get().getIngredientCount(i);
            if (count > 0 && !recipe.get().getIngredients().get(i).isEmpty()) {
                this.itemHandrel.extractItem(i, count, false);
            }
        }
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
        Optional<FriteuseRecipe> recipe = getCurrentRecipe();
        if (recipe.isEmpty()) {
            return false;
        }
        ItemStack result = recipe.get().getResultItem(getLevel().registryAccess());
        return canInsertOutputSlot(result.getCount()) && canInsertItemOutputSlot(result.getItem());
    }

    private Optional<FriteuseRecipe> getCurrentRecipe() {
        if (level == null) {
            return Optional.empty();
        }
        SimpleContainer inventory = new SimpleContainer(this.itemHandrel.getSlots());
        for (int i = 0; i < itemHandrel.getSlots(); i++) {
            inventory.setItem(i, this.itemHandrel.getStackInSlot(i));
        }
        return level.getRecipeManager()
                .getRecipesFor(RecipreMod.FRITEUSE_TYPE.get(), inventory, level)
                .stream()
                .findFirst();
    }

    private boolean canInsertItemOutputSlot(Item item) {
        return this.itemHandrel.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandrel.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertOutputSlot(int count) {
        return this.itemHandrel.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandrel.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }
}
