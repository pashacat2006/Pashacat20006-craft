package com.pashacat2015.haha_mod.Screen;

import com.pashacat2015.haha_mod.cat_mod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Регистрация типов меню (контейнеров) мода.
 */
public class MenuType {
    public static final DeferredRegister<net.minecraft.world.inventory.MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, cat_mod.MODID);

    /** Меню стола готовки */
    public static final RegistryObject<net.minecraft.world.inventory.MenuType<CookingtableMenu>> COOKINGTABLE_MENU =
            registerMenuType("cookingtable_menu", CookingtableMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<net.minecraft.world.inventory.MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus event) {
        MENUS.register(event);
    }
}
