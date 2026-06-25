package com.pashacat2015.haha_mod;

import com.mojang.logging.LogUtils;
import com.pashacat2015.haha_mod.Screen.CookingtableScreen;
import com.pashacat2015.haha_mod.Screen.FriteuseScreen;
import com.pashacat2015.haha_mod.Screen.MenuType;
import com.pashacat2015.haha_mod.init.BlockMod;
import com.pashacat2015.haha_mod.init.CreativeTabs;
import com.pashacat2015.haha_mod.init.block.entity.BlockEntity;
import com.pashacat2015.haha_mod.init.itemMain;
import com.pashacat2015.haha_mod.recipe.RecipreMod;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Главный класс мода. Точка входа — здесь регистрируются все системы.
 * MODID должен совпадать с modId в META-INF/mods.toml.
 */
@Mod(cat_mod.MODID)
public class cat_mod {
    /** Идентификатор мода, используется во всех регистрациях */
    public static final String MODID = "haha_mod";

    private static final Logger LOGGER = LogUtils.getLogger();

    public cat_mod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // События жизненного цикла мода
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::addCreative);

        // Регистрация предметов, блоков, сущностей и меню
        itemMain.registerReso(modEventBus);
        CreativeTabs.register(modEventBus);
        BlockMod.registerBlocks(modEventBus);
        BlockEntity.register(modEventBus);
        MenuType.register(modEventBus);
        RecipreMod.register(modEventBus);

        // Шина событий Forge (игровые события)
        MinecraftForge.EVENT_BUS.register(this);
    }

    /** Общая настройка после загрузки всех регистров (сервер и клиент) */
    private void setup(final FMLCommonSetupEvent event) {
    }

    /** Клиентская настройка: привязка GUI-экрана к типу меню */
    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(MenuType.COOKINGTABLE_MENU.get(), CookingtableScreen::new));
        event.enqueueWork(() -> MenuScreens.register(MenuType.FRITEUSE_MENU.get(), FriteuseScreen::new));
    }

    /** Добавление предметов и блоков мода во ванильные вкладки креатива */
    public void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(itemMain.XYGUGREENHEART.get());
            event.accept(itemMain.XYGUREDNHEART.get());
            event.accept(itemMain.CORA.get());
            event.accept(itemMain.SKEWER.get());
        }
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(itemMain.XYGUREDNHEART.get());
            event.accept(itemMain.OM.get());
            event.accept(itemMain.BARBEQUI.get());
            event.accept(itemMain.PIZZA.get());
            event.accept(itemMain.SANDWITH.get());
            event.accept(itemMain.CHEESE.get());
            event.accept(itemMain.FRENCHFRICE.get());
            event.accept(itemMain.BUTTER.get());
        }
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(BlockMod.COOKING_TABLE_BLOCK.get());
            event.accept(BlockMod.FRITEUSE_BLOCK.get());
        }
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(BlockMod.OMAN_BLOCK_ORE.get());
        }
    }
}
