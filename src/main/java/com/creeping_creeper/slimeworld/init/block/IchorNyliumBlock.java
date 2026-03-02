package com.creeping_creeper.slimeworld.init.block;

import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.DirtType;
import slimeknights.tconstruct.world.block.FoliageType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class IchorNyliumBlock extends Block implements BonemealableBlock {
    private final DirtType dirtType;
    private static final List<Supplier<IchorNyliumBlock>> NYLIUMS = new ArrayList<>(List.of(
            ModItems.IchorIchorSlimeNylium, ModItems.IchorEarthSlimeNylium, ModItems.IchorSkySlimeNylium, ModItems.IchorEnderSlimeNylium, ModItems.IchorVanillaSlimeNylium
    ));

    public IchorNyliumBlock(Properties properties, DirtType dirtType) {
        super(properties);
        this.dirtType = dirtType;
    }

    private static boolean isDarkEnough(BlockState state, LevelReader reader, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = reader.getBlockState(blockpos);
        int i = LightEngine.getLightBlockInto(reader, state, pos, blockstate, blockpos, Direction.DOWN, blockstate.getLightBlock(reader, blockpos));
        return i < reader.getMaxLightLevel();
    }

    @SuppressWarnings("deprecation") @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource random) {
        if (!isDarkEnough(state, worldIn, pos)) {
            worldIn.setBlockAndUpdate(pos, TinkerWorld.slimeDirt.get(dirtType).defaultBlockState());
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        return worldIn.getBlockState(pos.below()).isAir();
    }
    @Override
    public boolean isBonemealSuccess(Level worldIn, RandomSource rand, BlockPos pos, BlockState state) {
        return true;
  }
  @Override
  public void performBonemeal(ServerLevel world, RandomSource rand, BlockPos pos, BlockState state) {
        growGrass(world, rand, pos, TinkerTags.Blocks.SLIMY_NYLIUM, FoliageType.ICHOR, true, true);
    }

  public static void growGrass(ServerLevel world, RandomSource rand, BlockPos pos, TagKey<Block> validBase, FoliageType foliageType, boolean includeSapling, boolean spread) {
        // based on vanilla logic, reimplemented to switch plant types
        BlockPos down = pos.below();
        mainLoop:
        for (int i = 0; i < 128; i++) {
            // locate target
            BlockPos target = down;
            for (int j = 0; j < i / 16; j++) {
                target = target.offset(rand.nextInt(3) - 1, -(rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
                BlockPos up = target.above();
                BlockState upState = world.getBlockState(up);
                // stop if opaque below
                if (world.getBlockState(target).isCollisionShapeFullBlock(world, target)) {
                    continue mainLoop;
                }
                // spread if requested
                if (spread) {
                   for (Supplier<IchorNyliumBlock> block : NYLIUMS){
                       if (upState.is(TinkerWorld.allDirt.get(block.get().dirtType))) {
                           world.setBlockAndUpdate(up, block.get().defaultBlockState());
                           continue mainLoop;
                       }
                   }
                }
                // stop if not a valid base block
                if (!upState.is(validBase)) {
                    continue mainLoop;
                }
            }
            // grow the plants if empty
            if (world.isEmptyBlock(target)) {
                BlockState plantState;
                int plant = rand.nextInt(32);
                if (plant == 0 && includeSapling) {
                    plantState = ModItems.IchorSlimeSapling.get().defaultBlockState();
                } else if (plant < 6) {
                    plantState = ModItems.IchorFern.get().defaultBlockState();
                } else {
                    plantState = ModItems.IchorTallGrass.get().defaultBlockState();
                }
                if (plantState.canSurvive(world, target)) {
                    world.setBlock(target, plantState, 3);
                }
            }
        }
    }
}
