package com.creeping_creeper.slimeworld.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import slimeknights.tconstruct.shared.TinkerEffects;

public class IchorVentBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 14.0F, 16.0F);

    public IchorVentBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isSteppingCarefully() && !level.canSeeSkyFromBelowWater(pos.above()) && entity instanceof LivingEntity living && !living.hasEffect(TinkerEffects.antigravity.get())) {
            living.addEffect(new MobEffectInstance(TinkerEffects.antigravity.get(), 300));
            if (level instanceof ServerLevel serverLevel){
                serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX(), pos.getY() + 0.5D, pos.getZ(), 1, 0.0F, 0.0F, 0, 2);
            }
        }
        super.stepOn(level, pos, state, entity);
    }
}
