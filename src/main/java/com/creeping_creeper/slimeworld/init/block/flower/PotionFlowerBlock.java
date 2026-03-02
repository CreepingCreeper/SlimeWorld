package com.creeping_creeper.slimeworld.init.block.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.shared.TinkerEffects;

public class PotionFlowerBlock extends BaseFlowerBlock {
    public PotionFlowerBlock(Properties properties) {
        super(TinkerEffects.venom, 11, properties, ParticleTypes.ASH);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity living) {
            TinkerEffects.venom.get().apply(living, 100,  0, true);
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 25));
        }
    }
}
