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

    public static final RegistryObject<net.minecraft.world.inventory.MenuType<FriteuseMenu>> FRITEUSE_MENU =
            registerMenuType("friteuse_menu", FriteuseMenu::new);

    public static final RegistryObject<net.minecraft.world.inventory.MenuType<KnifetableMenu>> KNIFETABLE_MENU =
            registerMenuType("knifetable_menu", KnifetableMenu::new);

    /** Меню книги готовки */
    public static final RegistryObject<net.minecraft.world.inventory.MenuType<CookingBookMenu>> COOKINGBOOK_MENU =
            registerMenuType("cookingbook_menu", (windowId, inv, buf) -> new CookingBookMenu(windowId, inv, buf));

    private static <T extends AbstractContainerMenu> RegistryObject<net.minecraft.world.inventory.MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus event) {
        MENUS.register(event);
    }
}
