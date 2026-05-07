package com.creeping_creeper.slimeworld.init.block;

import com.creeping_creeper.slimeworld.init.ModParticles;
import com.creeping_creeper.slimeworld.library.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.shared.TinkerEffects;

public class IchorVentBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0F, 0.0F, 0.0F, 16.0F, 14.0F, 16.0F);

    public IchorVentBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, Entity entity) {
        if (!entity.isSteppingCarefully() && !level.canSeeSkyFromBelowWater(pos.above()) && entity instanceof LivingEntity living && !living.hasEffect(TinkerEffects.antigravity.get())) {
            if(living.addEffect(new MobEffectInstance(TinkerEffects.antigravity.get(), 300))){
                ParticleUtil.slimeParticle(level, ModParticles.IchorSlimeParticle.get(), 12, 1, living.getX(), living.getY() + 0.1, living.getZ());
            }
        }
        super.stepOn(level, pos, state, entity);
    }
}
