package com.pashacat2015.haha_mod.init;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

/**
 * Свойства еды для съедобных предметов мода.
 */
public class FoodMod {
    /** Om — быстрая еда с шансом нанести урон */
    public static final FoodProperties OM = new FoodProperties.Builder()
            .nutrition(1).fast().saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.HARM, 1000), 0.5f)
            .build();

    /** Barbequi — даёт временное увеличение здоровья */
    public static final FoodProperties BARBEQUI = new FoodProperties.Builder()
            .nutrition(1).fast().saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 1200), 0.5f)
            .build();

    public static final FoodProperties PIZZA = new FoodProperties.Builder()
            .nutrition(1).fast().saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 1200), 0.5f)
            .build();


    public static final FoodProperties SANDWITH = new FoodProperties.Builder()
            .nutrition(1).fast().saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200), 0.5f)
            .build();


    public static final FoodProperties CHEESE = new FoodProperties.Builder()
            .nutrition(1).fast().saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1200), 0.5f)
            .build();


    public static final FoodProperties FRENCHFRICE= new FoodProperties.Builder()
            .nutrition(1).fast().saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.LUCK, 1200), 0.5f)
            .build();


    public static final FoodProperties BUTTER= new FoodProperties.Builder()
            .nutrition(1).fast().saturationMod(0.8f)
            .build();




    /** Xygu red heart — отравление и голод */
    public static final FoodProperties XYGUREDHEART = new FoodProperties.Builder()
            .nutrition(5).fast().saturationMod(0.8f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 800), 0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 1000), 0.5f)
            .build();
}
