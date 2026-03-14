package com.creeping_creeper.slimeworld.init.block.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.shared.TinkerEffects;

public class PoisonFlowerBlock extends BaseFlowerBlock {
    public PoisonFlowerBlock(Properties properties) {
        super(TinkerEffects.venom, 11, properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity living) {
            TinkerEffects.venom.get().apply(living, 100,  0, true);
            living.addEffect(new MobEffectInstance(MobEffects.POISON, 25));
            RandomSource random = living.getRandom();
            if (entity.tickCount % 20 == 0) level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.THORNS_HIT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
        }
    }

    @Override
    public SimpleParticleType particleType(){
        return ParticleTypes.ASH;
    }
}
