package com.creeping_creeper.slimeworld.init.effect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import slimeknights.tconstruct.common.TinkerEffect;

public class StandEffect extends TinkerEffect {
    public StandEffect(MobEffectCategory typeIn, int color, boolean show) {
        super(typeIn, color, show);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living.isInFluidType()) {
            CollisionContext collisioncontext = CollisionContext.of(living);
            if (!living.isSteppingCarefully() && collisioncontext.isAbove(LiquidBlock.STABLE_SHAPE, living.blockPosition(), true) && living.level().getFluidState(living.blockPosition().above()).is(Fluids.EMPTY)) {
                living.setOnGround(true);
            } else {
                Vec3 movement = living.getDeltaMovement();
                double y = movement.y;
                if (!living.isSwimming() && y < 0) {
                    living.setDeltaMovement(movement.add(0.0F, y * 0.5 + 0.05, 0.0F));
                }
            }
        }
    }
}