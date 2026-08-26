package com.creeping_creeper.slimeworld.init.block.entity;

import net.minecraft.world.item.ItemStack;
import slimeknights.mantle.recipe.container.ISingleStackContainer;

public record DryingWrapper(DryingRackBlockEntity tile) implements ISingleStackContainer {

    @Override
    public ItemStack getStack() {
        return this.tile.getItem(0);
    }
}
