package com.creeping_creeper.slimeworld.init.block.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import slimeknights.tconstruct.common.TinkerEffect;
import slimeknights.tconstruct.common.TinkerTags;

import java.util.function.Supplier;

public class BaseFlowerBlock extends FlowerBlock implements SuspiciousEffectHolder {
    private final Supplier<TinkerEffect> effect;
    private final int duration;
    private final SimpleParticleType PARTICLE_TYPE;

    public BaseFlowerBlock(Supplier<TinkerEffect> effect, int duration, Properties properties, SimpleParticleType particleTypes) {
        super((Supplier<MobEffect>) null, 0, properties);
        this.effect = effect;
        this.duration = duration;
        this.PARTICLE_TYPE = particleTypes;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(TinkerTags.Blocks.SLIMY_SOIL);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(TinkerTags.Blocks.SLIMY_SOIL);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        VoxelShape voxelshape = this.getShape(state, level, pos, CollisionContext.empty());
        Vec3 vec3 = voxelshape.bounds().getCenter();
        double d0 = (double)pos.getX() + vec3.x;
        double d1 = (double)pos.getZ() + vec3.z;

        for(int i = 0; i < 3; ++i) {
            if (random.nextBoolean()) {
                level.addParticle(this.PARTICLE_TYPE, d0 + random.nextDouble() / (double)5.0F, (double)pos.getY() + ((double)0.5F - random.nextDouble()), d1 + random.nextDouble() / (double)5.0F, 0.0F, 0.0F, 0.0F);
            }
        }
    }

    @Override
    public MobEffect getSuspiciousEffect() {
        return this.effect.get();
    }

    @Override
    public int getEffectDuration() {
        return this.duration * 20;
    }
}
