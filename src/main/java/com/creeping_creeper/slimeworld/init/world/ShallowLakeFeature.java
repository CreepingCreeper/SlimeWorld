package com.creeping_creeper.slimeworld.init.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.LakeFeature;

public class ShallowLakeFeature extends LakeFeature {

    public ShallowLakeFeature(Codec<Configuration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Configuration> context) {
        BlockPos blockpos = context.origin().below();
        WorldGenLevel worldgenlevel = context.level();
        RandomSource randomsource = context.random();
        Configuration lakefeature$configuration = context.config();

        boolean[] aboolean = new boolean[256];
        int i = randomsource.nextInt(4) + 4;

        for(int j = 0; j < i; ++j) {
            double d0 = randomsource.nextDouble() * 6.0D + 3.0D;
            double d2 = randomsource.nextDouble() * 6.0D + 3.0D;

            double d3 = randomsource.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
            double d5 = randomsource.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

            // 移除纵向(Y)循环！只生成单层湖面
            for(int l = 1; l < 15; ++l) {
                for(int i1 = 1; i1 < 15; ++i1) {
                    // 2D椭圆方程，删掉原先高度d7项，池子永远1格深
                    double d6 = ((double)l - d3) / (d0 / 2.0D);
                    double d8 = ((double)i1 - d5) / (d2 / 2.0D);
                    double d9 = d6 * d6 + d8 * d8;
                    if (d9 < 1.0D) {
                        aboolean[l * 16 + i1] = true;
                    }
                }
            }
        }

        BlockState blockstate1 = lakefeature$configuration.fluid().getState(randomsource, blockpos);

        // ========== 放置水体 单层1格深度 ==========
        for(int l1 = 0; l1 < 16; ++l1) {
            for(int i2 = 0; i2 < 16; ++i2) {
                if (aboolean[l1 * 16 + i2]) {
                    BlockPos blockpos1 = blockpos.offset(l1, 0, i2);
                    if (this.canReplaceBlock(worldgenlevel.getBlockState(blockpos1))) {
                        // 不再区分>=4高度，全部放置流体方块，水池只有一格深
                        worldgenlevel.setBlock(blockpos1, blockstate1, 2);
                    }
                }
            }
        }

        // ========== 湖边屏障方块生成（适配单层） ==========
        BlockState blockstate2 = lakefeature$configuration.barrier().getState(randomsource, blockpos);
        if (!blockstate2.isAir()) {
            for(int j2 = 0; j2 < 16; ++j2) {
                for(int j3 = 0; j3 < 16; ++j3) {
                    boolean flag2 = !aboolean[j2 * 16 + j3] && (
                            j2 < 15 && aboolean[(j2 + 1) * 16 + j3] ||
                                    j2 > 0 && aboolean[(j2 - 1) * 16 + j3] ||
                                    j3 < 15 && aboolean[j2 * 16 + (j3 + 1)] ||
                                    j3 > 0 && aboolean[j2 * 16 + (j3 - 1)]
                    );
                    if (flag2 ) {
                        if (randomsource.nextInt(2) != 0) {
                            BlockPos blockpos3 = blockpos.offset(j2, 0, j3);
                            BlockState blockstate = worldgenlevel.getBlockState(blockpos3);
                            if (!blockstate.isAir() && !blockstate.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
                                worldgenlevel.setBlock(blockpos3, blockstate2, 2);
                                this.markAboveForPostProcessing(worldgenlevel, blockpos3);
                            }
                        }
                        BlockPos bottomPos = blockpos.offset(j2, -1, j3);
                        BlockState bottomState = worldgenlevel.getBlockState(bottomPos);
                        if (!bottomState.isAir() && !bottomState.is(BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
                            worldgenlevel.setBlock(bottomPos, blockstate2, 2);
                            this.markAboveForPostProcessing(worldgenlevel, bottomPos);
                        }
                    }
                }
            }
        }
        return true;
    }


    private boolean canReplaceBlock(BlockState state) {
        return !state.is(BlockTags.FEATURES_CANNOT_REPLACE);
    }

}
