package com.creeping_creeper.slimeworld.init.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Predicate;

public interface SpecialRangedMob extends RangedAttackMob {
    Predicate<ItemStack> canRangedAttack();

    Predicate<Item> canStartRangedAttack();

    void startDrawing(ToolStack tool);
}
