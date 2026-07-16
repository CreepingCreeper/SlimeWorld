package com.creeping_creeper.slimeworld.init.item;

import com.creeping_creeper.slimeworld.init.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import slimeknights.tconstruct.shared.TinkerEffects;

public class ModFood {

    public static final FoodProperties OCEAN_CAKE = new FoodProperties.Builder().nutrition(1).saturationMod(0.3f).alwaysEat().effect(() -> new MobEffectInstance(ModEffects.Floating.get(), 30 * 20, 0), 1.0f).build();
    public static final FoodProperties OCEAN_BOTTLE = new FoodProperties.Builder().alwaysEat().effect(() -> new MobEffectInstance(TinkerEffects.doubleJump.get(),  120 * 20), 1.0f).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120 * 20, 1), 1.0f).build();

    public static final FoodProperties EARTH_SLIME_BERRY = new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).effect(() -> new MobEffectInstance(MobEffects.SATURATION,  5), 0.2f).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,  300), 0.8f).build();
    public static final FoodProperties SKY_SLIME_BERRY = new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).effect(() -> new MobEffectInstance(MobEffects.SATURATION,  5), 0.2f).effect(() -> new MobEffectInstance(MobEffects.JUMP,  300), 0.8f).build();
    public static final FoodProperties BLOOD_SLIME_BERRY = new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).effect(() -> new MobEffectInstance(MobEffects.SATURATION,  5), 0.2f).effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST,  300), 0.8f).build();
    public static final FoodProperties ENDER_SLIME_BERRY = new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).effect(() -> new MobEffectInstance(MobEffects.SATURATION,  5), 0.2f).effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,  300), 0.8f).build();

    public static final FoodProperties BERRIPER = new FoodProperties.Builder().fast().nutrition(5).saturationMod(0.5F).effect(() -> new MobEffectInstance(TinkerEffects.selfDestructing.get(),  300), 0.05f).effect(() -> new MobEffectInstance(MobEffects.REGENERATION,  300), 1.0f).build();
    public static final FoodProperties TOMATO_PUDDING = new FoodProperties.Builder().nutrition(3).saturationMod(0.5F).effect(() -> new MobEffectInstance(MobEffects.GLOWING,  300), 0.6f).build();

    public static final FoodProperties BEEF_JERKY = new FoodProperties.Builder().alwaysEat().fast().nutrition(8).saturationMod(1.0F).meat().build();
    public static final FoodProperties MUTTON_JERKY = new FoodProperties.Builder().alwaysEat().fast().nutrition(6).saturationMod(1.0F).meat().build();
    public static final FoodProperties RABBIT_JERKY = new FoodProperties.Builder().alwaysEat().fast().nutrition(5).saturationMod(0.8F).meat().build();
    public static final FoodProperties FISH_JERKY = new FoodProperties.Builder().alwaysEat().fast().nutrition(3).saturationMod(0.8F).build();
    public static final FoodProperties ROTTEN_FLESH_JERKY = new FoodProperties.Builder().alwaysEat().fast().nutrition(4).saturationMod(0.4F).meat().build();
    public static final FoodProperties FRIED_EGG = new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build();
    public static final FoodProperties EARTH_SLIME_DROP = new FoodProperties.Builder().alwaysEat().fast().nutrition(1).saturationMod(1.0F)
            .effect(() ->new MobEffectInstance(MobEffects.UNLUCK, 100, 0), 0.8F)
            .effect(() ->new MobEffectInstance(MobEffects.LUCK, 700, 1), 1.0F).build();
    public static final FoodProperties SKY_SLIME_DROP = new FoodProperties.Builder().alwaysEat().fast().nutrition(3).saturationMod(1.0F)
            .effect(() ->new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0), 0.8F)
            .effect(() ->new MobEffectInstance(MobEffects.JUMP, 700, 1), 1.0F).build();
    public static final FoodProperties OCEAN_SLIME_DROP = new FoodProperties.Builder().alwaysEat().fast().nutrition(6).saturationMod(1.0F)
            .effect(() ->new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 0), 0.8F)
            .effect(() ->new MobEffectInstance(MobEffects.DIG_SPEED, 700, 1), 1.0F).build();
    public static final FoodProperties ICHOR_SLIME_DROP = new FoodProperties.Builder().alwaysEat().fast().nutrition(6).saturationMod(1.0F)
            .effect(() ->new MobEffectInstance(MobEffects.WITHER, 100, 0), 0.8F)
            .effect(() ->new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 700, 1), 1.0F).build();
    public static final FoodProperties MAGMA_SLIME_DROP = new FoodProperties.Builder().alwaysEat().fast().nutrition(3).saturationMod(1.0F)
            .effect(() ->new MobEffectInstance(MobEffects.WATER_BREATHING, 100, 0), 0.8F)
            .effect(() ->new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 700, 1), 1.0F).build();
    public static final FoodProperties ENDER_SLIME_DROP = new FoodProperties.Builder().alwaysEat().fast().nutrition(3).saturationMod(1.0F)
            .effect(() ->new MobEffectInstance(MobEffects.LEVITATION, 100, 0), 0.8F)
            .effect(() ->new MobEffectInstance(MobEffects.SLOW_FALLING, 700, 1), 1.0F).build();
  }
