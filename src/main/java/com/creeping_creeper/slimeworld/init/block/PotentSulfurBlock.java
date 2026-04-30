package com.creeping_creeper.slimeworld.init.block;

import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModParticles;
import com.creeping_creeper.slimeworld.init.ModSounds;
import com.creeping_creeper.slimeworld.init.block.entity.PotentSulfurBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PotentSulfurBlock extends BaseEntityBlock {
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 3);
    private final Supplier<? extends MobEffect> effect;

    public PotentSulfurBlock(Supplier<? extends MobEffect> effect, BlockBehaviour.Properties properties) {
        super(properties);
        this.effect = effect;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return validBlockState(this.defaultBlockState(), context.getLevel(), context.getClickedPos());
    }

    private static BlockState validBlockState(BlockState state, LevelReader level, BlockPos pos) {
        if (!level.getFluidState(pos.above()).isSourceOfType(Fluids.WATER)) {
            return state.setValue(TYPE, 0);
        } else {
            BlockState belowState = level.getBlockState(pos.below());
            if (!belowState.is(Blocks.MAGMA_BLOCK)) {
                return state.setValue(TYPE, 1);
            } else {
                boolean isGeyser = state.getValue(TYPE) > 1;
                if (!isGeyser) {
                    BlockEntity var6 = level.getBlockEntity(pos);
                    if (var6 instanceof PotentSulfurBlockEntity potentSulfurEntity) {
                        potentSulfurEntity.resetCountdown();
                    }
                }

                return state.getValue(TYPE) == 3 ? state : state.setValue(TYPE, 2);
            }
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PotentSulfurBlockEntity(pos, state);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return validBlockState(state, level, pos);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(TYPE) > 0) {
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
        boolean client = level.isClientSide();
        BlockEntityType<PotentSulfurBlockEntity> type1 = ModItems.PotentSulfurEntity.get();
        BlockEntityTicker type2;
        switch (blockState.getValue(TYPE)) {
            case 1 -> type2 = client ? PotentSulfurBlockEntity.CLIENT_NOXIOUS_GAS_TICKER : PotentSulfurBlockEntity.SERVER_NAUSEA_EFFECT_TICKER;
            case 2 -> type2 = client ? PotentSulfurBlockEntity.CLIENT_NOXIOUS_GAS_TICKER : PotentSulfurBlockEntity.SERVER_WAITING_COUNTDOWN_TICKER;
            case 3 -> type2 = client ? PotentSulfurBlockEntity.CLIENT_GEYSER_PLUME_TICKER : PotentSulfurBlockEntity.SERVER_LAUNCH_ENTITY_TICKER;
            default -> type2 = null;
        }

        return createTickerHelper(type, type1, type2);
    }

    public MobEffect getEffect() {
        return effect.get();
    }
}

