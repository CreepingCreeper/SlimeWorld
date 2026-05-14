package com.creeping_creeper.slimeworld.init.block;

import com.creeping_creeper.slimeworld.data.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SlimeGravelBlock extends FallingBlock {
    public SlimeGravelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (canFall(level, pos)) {
            super.tick(state, level, pos, random);
        }
    }

    private boolean canFall(BlockGetter level, BlockPos pos){
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos pos1 = pos.relative(direction, 1);
            if (isSlimy(level, pos1)){
                return false;
            }
        }
        return !isSlimy(level, pos.above());
    }

    private boolean isSlimy(BlockGetter level, BlockPos pos){
        BlockState blockstate = level.getBlockState(pos);
        return blockstate.is(this) || blockstate.is(ModTags.Blocks.SLIMY);
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter reader, BlockPos pos) {
        return 2732502;
    }
}
