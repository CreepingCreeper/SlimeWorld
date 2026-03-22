package com.creeping_creeper.slimeworld.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

import java.util.Optional;
import java.util.function.Supplier;

import static net.minecraft.world.level.material.FlowingFluid.FALLING;

public class BubbleBlock extends Block implements BucketPickup, LiquidBlockContainer {
    private final Supplier<Fluid> fluid;

    public BubbleBlock(Properties properties, Supplier<Fluid> fluid) {
        super(properties);
        this.fluid = fluid;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return this.fluid.get().defaultFluidState().setValue(FALLING,false);
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
        if (!level.isClientSide()) {
            level.levelEvent(2001, pos, Block.getId(state));
        }

        return new ItemStack(this.fluid.get().getBucket());
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return this.fluid.get().getPickupSound();
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        return false;
    }
}
