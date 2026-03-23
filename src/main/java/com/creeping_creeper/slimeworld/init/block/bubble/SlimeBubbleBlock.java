package com.creeping_creeper.slimeworld.init.block.bubble;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.object.FlowingFluidObject;

public class SlimeBubbleBlock extends BubbleBlock {
    protected final FlowingFluidObject<? extends ForgeFlowingFluid> fluid;

    public SlimeBubbleBlock(Properties properties, FlowingFluidObject<? extends ForgeFlowingFluid> fluid) {
        super(properties, fluid::get);
        this.fluid = fluid;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        fluid.getBlock().entityInside(state, level , pos, entity);
    }
}
