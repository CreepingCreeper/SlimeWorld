package com.creeping_creeper.slimeworld.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.tconstruct.fluids.fluids.SlimeFluid;

public class SlimeBubbleBlock extends BubbleBlock{
    protected final FlowingFluidObject<SlimeFluid> fluid;

    public SlimeBubbleBlock(Properties properties, FlowingFluidObject<SlimeFluid> fluid) {
        super(properties, fluid::get);
        this.fluid = fluid;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        fluid.getBlock().entityInside(state, level , pos, entity);
    }
}
