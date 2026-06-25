package com.pashacat2015.haha_mod.init;

import com.pashacat2015.haha_mod.cat_mod;
import com.pashacat2015.haha_mod.init.block.custom.CookingTable;
import com.pashacat2015.haha_mod.init.block.custom.Friteuse;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Регистрация всех блоков мода.
 */
public class BlockMod {
    /** Отложенный регистр блоков Forge */
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, cat_mod.MODID);

    /** Руда Oman — светится, требует кирку, высокая прочность */
    public static final RegistryObject<Block> OMAN_BLOCK_ORE = registryObject("omanore", () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_CYAN)
            .sound(SoundType.AMETHYST)
            .requiresCorrectToolForDrops()
            .strength(5.5f, 1200f)
            .lightLevel(state -> 10)
    ));

    /** Стол готовки с GUI и BlockEntity */
    public static final RegistryObject<Block> COOKING_TABLE_BLOCK = registryObject("cookingtable",
            () -> new CookingTable(BlockBehaviour.Properties.copy(Blocks.ACACIA_WOOD).noOcclusion()));

    public static final RegistryObject<Block> FRITEUSE_BLOCK = registryObject("friteuse",
            () -> new Friteuse(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static void registerBlocks(IEventBus bus) {
        BLOCKS.register(bus);
    }

    /** Регистрирует блок и автоматически создаёт для него предмет-блок */
    public static <T extends Block> RegistryObject<T> registryObject(String name, Supplier<T> block) {
        RegistryObject<T> RE = BLOCKS.register(name, block);
        registerBlockItem(name, RE);
        return RE;
    }

    /** Предмет-блок для установки блока из руки/креатива */
    public static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        itemMain.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
