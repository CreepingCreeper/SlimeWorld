package com.creeping_creeper.slimeworld.init.block;

import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModParticles;
import com.creeping_creeper.slimeworld.init.ModSounds;
import com.creeping_creeper.slimeworld.init.block.entity.PotentSulfurBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PotentSulfurBlock extends BaseEntityBlock {
    private final Supplier<? extends MobEffect> effect;

    public PotentSulfurBlock(Supplier<? extends MobEffect> effect, BlockBehaviour.Properties properties) {
        super(properties);
        this.effect = effect;
    }

    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PotentSulfurBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (level.getFluidState(pos.above()).isSourceOfType(Fluids.WATER)) {
            spawnBubbleParticlesAt(level, random, pos.getX(), pos.getY() + 1, pos.getZ());
            spawnBubbleParticlesAt(level, random, pos.getX(), pos.getY() + 1, pos.getZ());
            if (random.nextInt(10) == 0) {
                level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), ModSounds.NOXIOUS_GAS.get(), SoundSource.AMBIENT, 1.0F, 1.0F, false);
            }

        }
    }

    private static void spawnBubbleParticlesAt(Level level, RandomSource random, double x, double y, double z) {
        level.addAlwaysVisibleParticle(ModParticles.SulfurBubbles.get(), x + (double)random.nextFloat(), y + (double)random.nextFloat(), z + (double)random.nextFloat(), 0.0F, 0.0F, 0.0F);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return createTickerHelper(type, ModItems.PotentSulfurEntity.get(), level.isClientSide() ? PotentSulfurBlockEntity::clientTick : PotentSulfurBlockEntity::serverTick);
    }

    public MobEffect getEffect() {
        return effect.get();
    }
}

