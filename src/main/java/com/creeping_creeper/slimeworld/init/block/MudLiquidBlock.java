package com.creeping_creeper.slimeworld.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class MudLiquidBlock extends LiquidBlock {
    public MudLiquidBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
        super(supplier, properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!entity.fireImmune() && entity.getFluidTypeHeight(getFluid().getFluidType()) > 0) {
            entity.makeStuckInBlock(state, new Vec3(0.7F, 1.2F, 0.7F));
        }
    }

    public static Function<Supplier<? extends FlowingFluid>, LiquidBlock> createMud(MapColor color, int lightLevel) {
        return fluid -> new MudLiquidBlock(fluid, FluidDeferredRegister.createProperties(color, lightLevel));
    }
}
