package com.pashacat2015.haha_mod.init.block.entity;

import com.pashacat2015.haha_mod.cat_mod;
import com.pashacat2015.haha_mod.init.BlockMod;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Регистрация типов BlockEntity (блок-сущностей) мода.
 */
public class BlockEntity {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITYS =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, cat_mod.MODID);

    /** BlockEntity для стола готовки, привязан к блоку cookingtable */
    public static final RegistryObject<BlockEntityType<CookingTableEntity>> COOKING_BE =
            BLOCK_ENTITYS.register("cookingtable_be",
                    () -> BlockEntityType.Builder.of(CookingTableEntity::new, BlockMod.COOKING_TABLE_BLOCK.get()).build(null));


    public static final RegistryObject<BlockEntityType<FriteuseEntity>> FRITE_BE =
            BLOCK_ENTITYS.register("friteuse_be",
                    () -> BlockEntityType.Builder.of(FriteuseEntity::new, BlockMod.FRITEUSE_BLOCK.get()).build(null));

    public static void register(IEventBus event) {
        BLOCK_ENTITYS.register(event);
    }
}
