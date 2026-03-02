package com.creeping_creeper.slimeworld.init.block.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.shared.TinkerEffects;

public class FieryFlowerBlock extends BaseFlowerBlock {
    public FieryFlowerBlock(Properties properties) {
        super(TinkerEffects.conductive, 11, properties, ParticleTypes.FLAME);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity living) {
            int time = Math.max(living.getRemainingFireTicks(), 0);
            if (!living.fireImmune() && time < 400) {
                TinkerEffects.conductive.get().apply(living, 100, 0, true);
                living.setRemainingFireTicks(time + 2);
            }
        }
    }
}
