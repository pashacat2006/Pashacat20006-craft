package com.pashacat2015.haha_mod.init.block.custom;

import com.pashacat2015.haha_mod.init.block.entity.CookingTableEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Блок «Стол готовки».
 * При ПКМ открывает GUI, хранит предметы в BlockEntity и обрабатывает крафт по тикам.
 */
public class CookingTable extends BaseEntityBlock {
    /** Форма хитбокса блока (координаты в пикселях 0–16) */
    public static final VoxelShape shape = Block.box(0, 0, 0, 13, 11, 12);

    public CookingTable(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return shape;
    }

    /** Рисуется как обычная модель блока, а не block entity renderer */
    @Override
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    /** При ломании блока выбрасываем содержимое инвентаря */
    @Override
    public void onRemove(BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CookingTableEntity) {
                ((CookingTableEntity) blockEntity).drops();
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    /** Открытие GUI стола готовки по ПКМ */
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof CookingTableEntity) {
                NetworkHooks.openScreen(((ServerPlayer) player), (CookingTableEntity) entity, pos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CookingTableEntity(pos, state);
    }

    /** Тикер работает только на сервере — там идёт прогресс готовки */
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return createTickerHelper(type, com.pashacat2015.haha_mod.init.block.entity.BlockEntity.COOKING_BE.get(),
                (level1, blockPos, blockState, cookingTableEntity) -> cookingTableEntity.tick(level1, blockPos, blockState));
    }
}
