package com.creeping_creeper.slimeworld.init.item;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import slimeknights.mantle.fluid.FluidTransferHelper;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.item.ModifierCrystalItem;

public class CrystalItem extends ModifierCrystalItem {
    private final TagKey<Item> tagKey;
    public CrystalItem(TagKey<Item> tagKey, Properties props) {
        super(props);
        this.tagKey = tagKey;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction action, Player player) {
        // stacking a crystal on a tool attempts to add it when in creative
        // see also - modifier adding command

        // must be op or in creative, right-clicking onto a modifiable slot
        if (action == ClickAction.SECONDARY && slot.allowModification(player)) {
            ModifierId modifier = getModifier(stack);
            ItemStack toolItem = slot.getItem();
            // slot must have a tool, NBT must be valid
            if (modifier != null && !toolItem.isEmpty() && toolItem.is(tagKey)) {
                if (!player.level().isClientSide || (player.isCreative() && player.containerMenu.menuType == null)) {
                    ToolStack tool = ToolStack.copyFrom(toolItem);

                    // add modifier
                    tool.addModifier(modifier, stack.getCount());
                    stack.shrink(1);
                    // ensure no modifier problems after adding
                    Component toolValidation = tool.tryValidate();
                    if (toolValidation != null) {
                        player.displayClientMessage(toolValidation, false);
                    } else {
                        tool.updateStack(toolItem);
                        FluidTransferHelper.playUISound(player, SoundEvents.ENCHANTMENT_TABLE_USE);
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
        return false;
    }
}
