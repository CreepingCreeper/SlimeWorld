package com.creeping_creeper.slimeworld.init.block;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class StrippableSnowaveLogBlock extends SnowaveLogBlock {
    private final Supplier<? extends Block> stripped;

    public StrippableSnowaveLogBlock(Supplier<? extends Block> stripped, Properties properties) {
        super(properties);
        this.stripped = stripped;
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (toolAction == ToolActions.AXE_STRIP) {
            return stripped.get().defaultBlockState().setValue(FACING, state.getValue(FACING));
        }
        return null;
    }
}
