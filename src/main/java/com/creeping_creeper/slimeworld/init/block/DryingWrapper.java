package com.creeping_creeper.slimeworld.init.block;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.recipe.container.ISingleStackContainer;

public record DryingWrapper(DryingRackBlockEntity tile) implements ISingleStackContainer {

    @Override
    public @NotNull ItemStack getStack() {
        return this.tile.getItem(0);
    }
}
