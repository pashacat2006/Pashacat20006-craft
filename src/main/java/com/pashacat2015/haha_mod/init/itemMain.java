package com.pashacat2015.haha_mod.init;

import com.pashacat2015.haha_mod.cat_mod;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Регистрация всех предметов мода.
 */
public class itemMain {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, cat_mod.MODID);

    public static final RegistryObject<Item> XYGUGREENHEART = RegisterObjectCreate("xygugreenheart", 20);
    public static final RegistryObject<Item> XYGUREDNHEART = RegisterObjectCreateFood("xyguredheart", 20, FoodMod.XYGUREDHEART);
    public static final RegistryObject<Item> OM = RegisterObjectCreateFood("om", 64, FoodMod.OM);
    public static final RegistryObject<Item> CORA = RegisterObjectCreate("cora", 95);
    public static final RegistryObject<Item> SKEWER = RegisterObjectCreate("skewer", 10);
    public static final RegistryObject<Item> BARBEQUI = RegisterObjectCreateFood("barbequi", 64, FoodMod.BARBEQUI);
    public static final RegistryObject<Item> PIZZA = RegisterObjectCreateFood("pizza", 64, FoodMod.PIZZA);
    public static final RegistryObject<Item> SANDWITH = RegisterObjectCreateFood("sandwith", 64, FoodMod.SANDWITH);
    public static final RegistryObject<Item> CHEESE = RegisterObjectCreateFood("cheese", 64, FoodMod.CHEESE);
    public static final RegistryObject<Item> FRENCHFRICE = RegisterObjectCreateFood("frenchfrice", 64, FoodMod.FRENCHFRICE);
    public static final RegistryObject<Item> BUTTER = RegisterObjectCreateFood("butter", 64, FoodMod.BUTTER);

    /** Обычный предмет без свойств еды */
    public static RegistryObject<Item> RegisterObjectCreate(String id, int stack) {
        return ITEMS.register(id, () -> new Item(new Item.Properties().stacksTo(stack)));
    }

    /** Съедобный предмет */
    public static RegistryObject<Item> RegisterObjectCreateFood(String id, int stack, FoodProperties food) {
        return ITEMS.register(id, () -> new Item(new Item.Properties().stacksTo(stack).food(food)));
    }

    public static void registerReso(IEventBus bus) {
        ITEMS.register(bus);
    }
}
