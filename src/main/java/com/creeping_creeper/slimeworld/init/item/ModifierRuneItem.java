package com.creeping_creeper.slimeworld.init.item;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.fluid.FluidTransferHelper;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.ModifierManager;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.utils.Util;

import javax.annotation.Nullable;
import java.util.List;

public class ModifierRuneItem extends Item {
    private static final Component TOOLTIP_MISSING = TConstruct.makeTranslation("item", "modifier_crystal.missing").withStyle(ChatFormatting.GRAY);
    private static final Component TOOLTIP_APPLY = SlimeWorld.makeTranslation("item", "rune.tooltip").withStyle(ChatFormatting.GRAY);
    private static final String MODIFIER_KEY = TConstruct.makeTranslationKey("item", "modifier_crystal.modifier_id");
    private static final String MAX_LEVEL_KEY = SlimeWorld.makeTranslationKey("item", "rune.max_level");
    public static final String TAG_MODIFIER = "modifier";
    public static final String MAX_LEVEL = "max_level";
    private final TagKey<Item> tagKey;

    public ModifierRuneItem(TagKey<Item> tagKey, Properties props) {
        super(props);
        this.tagKey = tagKey;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack pStack) {
        return true;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        ModifierId modifier = getModifier(stack);
        if (modifier != null) {
            return Component.translatable(getDescriptionId(stack) + ".format", Component.translatable(Util.makeTranslationKey("modifier", modifier)));
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag advanced) {
        ModifierId id = getModifier(stack);
        if (id != null) {
            if (ModifierManager.INSTANCE.contains(id)) {
                tooltip.addAll(ModifierManager.INSTANCE.get(id).getDescriptionList());
            }
            tooltip.add(TOOLTIP_APPLY);
            tooltip.add((Component.translatable(MAX_LEVEL_KEY, getMaxLevel(stack))));
            if (advanced.isAdvanced()) {
                tooltip.add((Component.translatable(MODIFIER_KEY, id.toString())).withStyle(ChatFormatting.DARK_GRAY));
            }
        } else {
            tooltip.add(TOOLTIP_MISSING);
        }
    }

    @Nullable
    @Override
    public String getCreatorModId(ItemStack stack) {
        ModifierId modifier = getModifier(stack);
        if (modifier != null) {
            return modifier.getNamespace();
        }
        return null;
    }


    @Override
    public boolean overrideStackedOnOther(@NotNull ItemStack stack, @NotNull Slot slot, @NotNull ClickAction action, @NotNull Player player) {
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
                    if (tool.getModifierLevel(modifier) < getMaxLevel(stack)){
                        return false;
                    }
                    // add modifier
                    tool.addModifier(modifier, 1);
                    if (!player.getAbilities().instabuild){
                        stack.shrink(1);
                    }
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

    @Nullable
    public static ModifierId getModifier(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            return ModifierId.tryParse(tag.getString(TAG_MODIFIER));
        }
        return null;
    }

    public static int getMaxLevel(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            return tag.getInt(MAX_LEVEL);
        }
        return 1;
    }
}
