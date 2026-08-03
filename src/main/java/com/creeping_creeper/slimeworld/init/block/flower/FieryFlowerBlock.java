package com.creeping_creeper.slimeworld.init.block.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.shared.TinkerEffects;

public class FieryFlowerBlock extends BaseFlowerBlock {
    public FieryFlowerBlock(Properties properties) {
        super(TinkerEffects.conductive, 11, properties);
    }

    @Override
    public void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity living) {
            int time = Math.max(living.getRemainingFireTicks(), 0);
            if (!living.fireImmune() && time < 400) {
                TinkerEffects.conductive.get().apply(living, 100, 0, true);
                living.setRemainingFireTicks(time + 2);
                RandomSource random = living.getRandom();
                if (entity.tickCount % 20 == 0) level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
            }
        }
    }

    @Override
    public SimpleParticleType particleType(){
        return ParticleTypes.FLAME;
    }
}
