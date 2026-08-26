package com.creeping_creeper.slimeworld.init.block;

import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModParticles;
import com.creeping_creeper.slimeworld.init.ModSounds;
import com.creeping_creeper.slimeworld.init.block.entity.PotentSulfurBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
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

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PotentSulfurBlock extends BaseEntityBlock {
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 4);
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
            if (belowState.is(ModTags.Blocks.CAUSES_CONTINUOUS_GEYSER_ERUPTIONS)) {
                return state.setValue(TYPE, 4);
            } else {
                if (!belowState.is(ModTags.Blocks.CAUSES_PERIODIC_GEYSER_ERUPTIONS)) {
                    return state.setValue(TYPE, 1);
                } else if (state.getValue(TYPE) < 2) {
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
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return validBlockState(state, level, pos);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (state.getValue(TYPE) > 2) {
            level.blockEvent(pos, this, 0, 0);
            level.playSound((Entity)null, pos, ModSounds.GEYSER_ERUPTION_START.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }

    }

    @Override
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
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return switch (state.getValue(TYPE)) {
                case 1, 2 -> createTickerHelper(type, ModItems.PotentSulfurEntity.get(), PotentSulfurBlockEntity.CLIENT_NOXIOUS_GAS_TICKER);
                case 3, 4 -> createTickerHelper(type, ModItems.PotentSulfurEntity.get(), PotentSulfurBlockEntity.CLIENT_GEYSER_PLUME_TICKER);
                default -> null;
            };
        } else {
            return createTickerHelper(type, ModItems.PotentSulfurEntity.get(), PotentSulfurBlockEntity.SERVER_TICKER);
        }
    }

    public MobEffect getEffect() {
        return effect.get();
    }
}

