package com.creeping_creeper.slimeworld.init.block.grass;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.gadgets.block.PunjiBlock;

public class StickPunjisBlock extends PunjiBlock {
    public StickPunjisBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState block = level.getBlockState(pos.relative(state.getValue(FACING)));
        return block.is(BlockTags.BAMBOO_PLANTABLE_ON) || block.is(TinkerTags.Blocks.SLIMY_SOIL);
    }
}
