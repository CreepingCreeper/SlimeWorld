package com.creeping_creeper.slimeworld.init.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.HugeFungusConfiguration;
import net.minecraft.world.level.levelgen.feature.HugeFungusFeature;
import net.minecraft.world.level.levelgen.feature.WeepingVinesFeature;
import slimeknights.tconstruct.world.worldgen.trees.config.SlimeFungusConfig;

public class IchorFungusFeature extends HugeFungusFeature {
    public IchorFungusFeature(Codec<HugeFungusConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<HugeFungusConfiguration> context) {
        if (!(context.config() instanceof SlimeFungusConfig config)) {
            return super.place(context);
        }
        // must be on the right ground
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        if (!level.getBlockState(pos.above()).is(config.getGroundTag())) {
            return false;
        }
        // ensure not too tall
        RandomSource random = context.random();
        int height = Mth.nextInt(random, 4, 13);
        if (random.nextInt(12) == 0) {
            height *= 2;
        }
        if (!config.planted && pos.getY() - height - 1 <= context.chunkGenerator().getMinY()) {
            return false;
        }
        // actual generation
        boolean flag = !config.planted && random.nextFloat() < 0.06F;
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
        this.placeStem(level, random, config, pos, height, flag);
        this.placeHat(level, random, config, pos, height, flag);
        return true;
    }

    private void placeStem(WorldGenLevel level, RandomSource random, HugeFungusConfiguration config, BlockPos pos, int height, boolean huge) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockState blockstate = config.stemState;
        int i = huge ? 1 : 0;

        for(int j = -i; j <= i; ++j) {
            for(int k = -i; k <= i; ++k) {
                boolean flag = huge && Mth.abs(j) == i && Mth.abs(k) == i;

                for(int l = 0; l < height; ++l) {
                    blockpos$mutableblockpos.setWithOffset(pos, j, -l, k);
                    if (isReplaceable(level, blockpos$mutableblockpos, config, true)) {
                        if (config.planted) {
                            if (!level.getBlockState(blockpos$mutableblockpos.below()).isAir()) {
                                level.destroyBlock(blockpos$mutableblockpos, true);
                            }

                            level.setBlock(blockpos$mutableblockpos, blockstate, 3);
                        } else if (flag) {
                            if (random.nextFloat() < 0.1F) {
                                this.setBlock(level, blockpos$mutableblockpos, blockstate);
                            }
                        } else {
                            this.setBlock(level, blockpos$mutableblockpos, blockstate);
                        }
                    }
                }
            }
        }
    }

    private void placeHat(WorldGenLevel level, RandomSource random, HugeFungusConfiguration config, BlockPos pos, int height, boolean huge) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        boolean flag = config.hatState.is(Blocks.NETHER_WART_BLOCK);
        int i = Math.min(random.nextInt(1 + height / 3) + 5, height);
        int j = height - i;

        for(int k = j; k <= height; ++k) {
            int l = k < height - random.nextInt(3) ? 2 : 1;
            if (i > 8 && k < j + 4) {
                l = 3;
            }

            if (huge) {
                ++l;
            }

            for(int i1 = -l; i1 <= l; ++i1) {
                for(int j1 = -l; j1 <= l; ++j1) {
                    boolean flag1 = i1 == -l || i1 == l;
                    boolean flag2 = j1 == -l || j1 == l;
                    boolean flag3 = !flag1 && !flag2 && k != height;
                    boolean flag4 = flag1 && flag2;
                    boolean flag5 = k < j + 3;
                    blockpos$mutableblockpos.setWithOffset(pos, i1, -k, j1);
                    if (isReplaceable(level, blockpos$mutableblockpos, config, false)) {
                        if (config.planted && !level.getBlockState(blockpos$mutableblockpos.below()).isAir()) {
                            level.destroyBlock(blockpos$mutableblockpos, true);
                        }

                        if (flag5) {
                            if (!flag3) {
                                this.placeHatDropBlock(level, random, blockpos$mutableblockpos, config.hatState, flag);
                            }
                        } else if (flag3) {
                            this.placeHatBlock(level, random, config, blockpos$mutableblockpos, 0.1F, 0.2F, flag ? 0.1F : 0.0F);
                        } else if (flag4) {
                            this.placeHatBlock(level, random, config, blockpos$mutableblockpos, 0.01F, 0.7F, flag ? 0.083F : 0.0F);
                        } else {
                            this.placeHatBlock(level, random, config, blockpos$mutableblockpos, 5.0E-4F, 0.98F, flag ? 0.07F : 0.0F);
                        }
                    }
                }
            }
        }

    }

    private void placeHatDropBlock(LevelAccessor level, RandomSource random, BlockPos pos, BlockState state, boolean weepingVines) {
        if (level.getBlockState(pos.above()).is(state.getBlock())) {
            this.setBlock(level, pos, state);
        } else if ((double)random.nextFloat() < 0.15) {
            this.setBlock(level, pos, state);
            if (weepingVines && random.nextInt(11) == 0) {
                tryPlaceWeepingVines(pos, level, random);
            }
        }
    }

    private static void tryPlaceWeepingVines(BlockPos pos, LevelAccessor level, RandomSource random) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable().move(Direction.UP);
        if (level.isEmptyBlock(blockpos$mutableblockpos)) {
            int i = Mth.nextInt(random, 1, 5);
            if (random.nextInt(7) == 0) {
                i *= 2;
            }
            WeepingVinesFeature.placeWeepingVinesColumn(level, random, blockpos$mutableblockpos, i, 23, 25);
        }
    }
}
