package com.creeping_creeper.slimeworld.library;

import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;

@SuppressWarnings("unChecked")
public class SpecialBowAttackGoal extends RangedBowAttackGoal {
    public SpecialBowAttackGoal(Monster p_25792_, double p_25793_, int p_25794_, float p_25795_) {
        super(p_25792_, p_25793_, p_25794_, p_25795_);
    }

    @Override
    protected boolean isHoldingBow() {
        return this.mob.isHolding(is -> is.getItem() instanceof ModifiableBowItem);
    }
}
