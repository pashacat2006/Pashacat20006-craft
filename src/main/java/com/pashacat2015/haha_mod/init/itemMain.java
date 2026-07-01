package com.pashacat2015.haha_mod.init;

import com.pashacat2015.haha_mod.cat_mod;
import com.pashacat2015.haha_mod.item.CookingBookItem;
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
    public static final RegistryObject<Item> BURGER = RegisterObjectCreateFood("burger", 64, FoodMod.BURGER);
    public static final RegistryObject<Item> TACO = RegisterObjectCreateFood("taco", 64, FoodMod.TACO);
    public static final RegistryObject<Item> EGG = RegisterObjectCreateFood("egg", 64, FoodMod.EGG);
    public static final RegistryObject<Item> BUBLETEA = RegisterObjectCreateFood("bubletea", 64, FoodMod.BUBLETEA);
    public static final RegistryObject<Item> CHOCOLATE = RegisterObjectCreateFood("chocolate", 64, FoodMod.CHOCOLATE);
    public static final RegistryObject<Item> CHOCOLATEMILK = RegisterObjectCreateFood("chocolatemilk", 64, FoodMod.CHOCOLATEMILK);
    public static final RegistryObject<Item> CUBESUGAR = RegisterObjectCreateFood("cubesugar", 64, FoodMod.CUBESUGAR);
    public static final RegistryObject<Item> CULITH = RegisterObjectCreateFood("culith", 64, FoodMod.CULITH);
    public static final RegistryObject<Item> LEMONJUICE = RegisterObjectCreateFood("lemonjuice", 64, FoodMod.LEMONJUICE);
    public static final RegistryObject<Item> PIZZACOUNT = RegisterObjectCreateFood("pizzacount", 64, FoodMod.PIZZACOUNT);
    public static final RegistryObject<Item> STRABERRYJUICE = RegisterObjectCreateFood("straberryjuice", 64, FoodMod.STRABERRYJUICE);
    public static final RegistryObject<Item> SHUSHILOSOS = RegisterObjectCreateFood("shushilosos", 64, FoodMod.SHUSHILOSOS);
    public static final RegistryObject<Item> COOKINGBOOK = ITEMS.register("cookingbook",
            () -> new CookingBookItem(new Item.Properties().stacksTo(1)));

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
