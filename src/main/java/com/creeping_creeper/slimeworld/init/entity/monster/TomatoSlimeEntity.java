package com.creeping_creeper.slimeworld.init.entity.monster;

import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.world.entity.TravelersPlateSlimeEntity;

public class TomatoSlimeEntity extends TravelersPlateSlimeEntity {
    public TomatoSlimeEntity(EntityType<? extends TomatoSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected @NotNull ParticleOptions getParticleType() {
        return ModParticles.TomatoSlimeParticle.get();
    }


    @Override
    protected void actuallyHurt(@NotNull DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (damageSrc.getEntity() instanceof LivingEntity living) {
            living.heal(this.getSize());
            level().playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.GENERIC_EAT, living.getSoundSource(), 1, 0.5f);
            if (living instanceof Player player){
                player.eat(level(), ModItems.TomatoPudding.get().getDefaultInstance());
            }
        }
    }

    @Override
    protected @NotNull MaterialId getPlating() {
        return MaterialIds.iron;
    }
}