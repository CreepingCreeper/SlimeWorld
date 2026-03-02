package com.creeping_creeper.slimeworld.init.block.grass;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FungusBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.DirtType;

/** Update of fungus that grows on slime soil instead */
public class IchorFungusBlock extends FungusBlock {
  public IchorFungusBlock(Properties properties, ResourceKey<ConfiguredFeature<?,?>> fungusFeature) {
    super(properties, fungusFeature, TinkerWorld.slimeDirt.get(DirtType.ICHOR));
  }

  @Override
  protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return state.is(TinkerTags.Blocks.SLIMY_SOIL);
  }

  @Override
  public boolean isValidBonemealTarget(LevelReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
    return worldIn.getBlockState(pos.above()).is(TinkerTags.Blocks.SLIMY_SOIL);
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      BlockPos blockpos = pos.above();
      return this.mayPlaceOn(level.getBlockState(blockpos), level, blockpos);
  }
}
