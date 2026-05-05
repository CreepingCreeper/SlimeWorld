package com.creeping_creeper.slimeworld.init.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.item.BurnableBlockItem;
import slimeknights.mantle.util.TranslationHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BurnableBlockTooltipItem extends BurnableBlockItem {
    public BurnableBlockTooltipItem(Block blockIn, Properties builder, int burnTime) {
        super(blockIn, builder, burnTime);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        TranslationHelper.addOptionalTooltip(stack, tooltip);
    }
}
