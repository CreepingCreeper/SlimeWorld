package com.creeping_creeper.slimeworld.init.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import slimeknights.tconstruct.common.TinkerEffect;

public class FloatingEffect extends TinkerEffect {
    public FloatingEffect(MobEffectCategory typeIn, int color, boolean show) {
        super(typeIn, color, show);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living.isInFluidType() && !living.isSteppingCarefully()) {
            CollisionContext collisioncontext = CollisionContext.of(living);
            if (collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, living.blockPosition(), true) && living.level().getFluidState(living.blockPosition().above()).is(Fluids.EMPTY)) {
                living.setOnGround(true);
            } else {
                Vec3 vec3 = living.getDeltaMovement();
                if(vec3.y<0){
                    living.setDeltaMovement(new Vec3(vec3.x, vec3.y * 0.3, vec3.z));
                }
            }
        }
    }
}