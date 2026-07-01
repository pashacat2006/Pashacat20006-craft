package com.pashacat2015.haha_mod.recipe;

import com.pashacat2015.haha_mod.cat_mod;
import com.pashacat2015.haha_mod.init.block.custom.Friteuse;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipreMod {
    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, cat_mod.MODID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, cat_mod.MODID);

    public static final RegistryObject<RecipeSerializer<CookingtableRecipe>> COOKINGTABLE_SERIALEZATOR =
            SERIALIZERS.register("cookingtable", () -> CookingtableRecipe.Serializer.INSTATE);
    public static final RegistryObject<RecipeType<CookingtableRecipe>> COOKINGTABLE_TYPE =
            RECIPE_TYPES.register("cookingtable", () -> CookingtableRecipe.Type.INSTANCE);


    public static final RegistryObject<RecipeSerializer<FriteuseRecipe>> FRITEUSE_SERIALEZATOR =
            SERIALIZERS.register("friteuse", () -> FriteuseRecipe.Serializer.INSTATE);
    public static final RegistryObject<RecipeType<FriteuseRecipe>> FRITEUSE_TYPE =
            RECIPE_TYPES.register("friteuse", () -> FriteuseRecipe.Type.INSTANCE);


    public static final RegistryObject<RecipeSerializer<KnifetableRecipe>> KNIFETABLE_SERIALEZATOR =
            SERIALIZERS.register("knifetable", () -> KnifetableRecipe.Serializer.INSTATE);
    public static final RegistryObject<RecipeType<KnifetableRecipe>> KNIFETABLE_TYPE =
            RECIPE_TYPES.register("knifetable", () -> KnifetableRecipe.Type.INSTANCE);

    public static void register(IEventBus event) {
        SERIALIZERS.register(event);
        RECIPE_TYPES.register(event);
    }
}
