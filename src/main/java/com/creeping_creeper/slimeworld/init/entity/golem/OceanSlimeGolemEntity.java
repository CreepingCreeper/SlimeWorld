package com.creeping_creeper.slimeworld.init.entity.golem;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.data.ModifierIds;

import java.util.function.Predicate;

public class OceanSlimeGolemEntity extends RangeSlimeGolemEntity {
     public OceanSlimeGolemEntity(EntityType<? extends RangeSlimeGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float distanceFactor) {

    }

    @Override
    public Predicate<ItemStack> canRangedAttack() {
        return itemStack -> itemStack.getItem() instanceof ModifiableItem && ToolStack.from(itemStack).getModifierLevel(ModifierIds.spitting) > 0 && !ToolStack.from(itemStack).isBroken();
    }

    @Override
    public void startDrawing(ToolStack tool){
        GeneralInteractionModifierHook.startUsingWithDrawtime(tool, ModifierIds.spitting, this, InteractionHand.MAIN_HAND, 1.5F);
    }

}
