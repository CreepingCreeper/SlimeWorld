package com.creeping_creeper.slimeworld.init.block.grass;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.PlantType;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.world.TinkerWorld;

import javax.annotation.Nullable;
import java.util.List;

public class IchorTallGrassBlock extends BushBlock implements IForgeShearable {
  private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);

    public IchorTallGrassBlock(Properties properties) {
        super(properties);
    }

  @Deprecated
  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  /* Forge/MC callbacks */
  @Override
  public PlantType getPlantType(BlockGetter world, BlockPos pos) {
    return TinkerWorld.SLIME_PLANT_TYPE;
  }

  @Override
  public List<ItemStack> onSheared(@Nullable Player player, ItemStack item, Level world, BlockPos pos, int fortune) {
    return Lists.newArrayList(new ItemStack(this, 1));
  }

  @Override
  protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return state.is(TinkerTags.Blocks.SLIMY_SOIL);
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
      BlockPos blockpos = pos.above();
      return this.mayPlaceOn(level.getBlockState(blockpos), level, blockpos);
  }
}
