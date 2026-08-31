package com.creeping_creeper.slimeworld.init.entity.monster;

import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CinnabarSpider extends Spider {
    public CinnabarSpider(EntityType<? extends CinnabarSpider> p_33786_, Level p_33787_) {
        super(p_33786_, p_33787_);
    }

    public static AttributeSupplier.Builder createCaveSpider() {
        return Spider.createAttributes().add(Attributes.MAX_HEALTH, 16.0F);
    }

    public boolean doHurtTarget(@NotNull Entity entity) {
        if (super.doHurtTarget(entity)) {
            if (entity instanceof LivingEntity) {
                int $$1 = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    $$1 = 7;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    $$1 = 15;
                }

                if ($$1 > 0) {
                    ((LivingEntity)entity).addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, $$1 * 20, 2), this);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    protected float getStandingEyeHeight(@NotNull Pose p_32265_, @NotNull EntityDimensions p_32266_) {
        return 0.45F;
    }
}
