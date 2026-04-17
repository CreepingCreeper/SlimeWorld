package com.creeping_creeper.slimeworld.init.world;

import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.block.SulfurSpikeBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.function.Consumer;

public class SulfurSpikeFeature extends Feature<NoneFeatureConfiguration> {

    public SulfurSpikeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        LevelAccessor levelaccessor = context.level();
        BlockPos blockpos = context.origin();
        RandomSource randomsource = context.random();
        if (isSulfurSpikeBase(levelaccessor, blockpos)) {
            int i = randomsource.nextFloat() < 0.2 && DripstoneUtils.isEmptyOrWater(levelaccessor.getBlockState(blockpos.relative(Direction.UP))) ? 2 : 1;
            growSulfurSpike(levelaccessor, blockpos, i);
            return true;
        }
        return false;
    }

    protected static void growSulfurSpike(LevelAccessor level, BlockPos pos, int height) {
        if (isSulfurSpikeBase(level, pos)) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();
            buildBaseToTipColumn(Direction.UP, height, false, (p_277326_) -> {
                if (p_277326_.is(ModItems.SulfurSpike.get())) {
                    p_277326_ = p_277326_.setValue(SulfurSpikeBlock.WATERLOGGED, level.isWaterAt(blockpos$mutableblockpos));
                }

                level.setBlock(blockpos$mutableblockpos, p_277326_, 2);
                blockpos$mutableblockpos.move(Direction.UP);
            });
        }

    }

    protected static void buildBaseToTipColumn(Direction direction, int height, boolean mergeTip, Consumer<BlockState> blockSetter) {
        if (height >= 3) {
            blockSetter.accept(SulfurSpike(direction, DripstoneThickness.BASE));

            for(int i = 0; i < height - 3; ++i) {
                blockSetter.accept(SulfurSpike(direction, DripstoneThickness.MIDDLE));
            }
        }

        if (height >= 2) {
            blockSetter.accept(SulfurSpike(direction, DripstoneThickness.FRUSTUM));
        }

        if (height >= 1) {
            blockSetter.accept(SulfurSpike(direction, mergeTip ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP));
        }

    }
    
    private static BlockState SulfurSpike(Direction direction, DripstoneThickness dripstoneThickness) {
        return ModItems.SulfurSpike.get().defaultBlockState().setValue(SulfurSpikeBlock.TIP_DIRECTION, direction).setValue(SulfurSpikeBlock.THICKNESS, dripstoneThickness);
    }

    public static boolean isSulfurSpikeBase(LevelAccessor level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(ModItems.Sulfur.get()) && level.getBlockState(pos).isAir();
    }
}

