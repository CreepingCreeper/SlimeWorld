package com.creeping_creeper.slimeworld.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public interface NecroticBonemealableBlock {
    boolean isValidBonemealTarget(LevelReader reader, BlockPos pos, BlockState state, boolean var);

    void performBonemeal(ServerLevel level, RandomSource source, BlockPos pos, BlockState state);
}
