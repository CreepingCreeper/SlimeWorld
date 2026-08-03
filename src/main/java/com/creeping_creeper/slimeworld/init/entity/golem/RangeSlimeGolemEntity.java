package com.creeping_creeper.slimeworld.init.entity.golem;

import com.creeping_creeper.slimeworld.init.entity.SpecialBowAttackGoal;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Predicate;

public abstract class RangeSlimeGolemEntity extends BaseSlimeGolemEntity implements RangedAttackMob {
    protected final SpecialBowAttackGoal<? extends RangeSlimeGolemEntity> bowGoal = new SpecialBowAttackGoal<>(this, 1.0D, 20, 15.0F);

    public RangeSlimeGolemEntity(EntityType<? extends RangeSlimeGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            if (this.canRangedAttack().test(this.getMainHandItem())) {
                int i = 20;
                if (this.level().getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }

                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(4, this.bowGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }

        }
    }

    public void shoot(SpecialBowAttackGoal<? extends RangeSlimeGolemEntity> goal, boolean flag, ToolStack toolStack, LivingEntity target){
        if (this.isUsingItem()) {
            if (!flag && !goal.canSee()) {
                this.stopUsingItem();
            } else if (flag) {
                int i = this.getTicksUsingItem();
                float charge = GeneralInteractionModifierHook.getToolCharge(toolStack, i);
                if (charge == 1){
                    double d0 = target.getX() - this.getX();
                    double d1 = target.getY(0.3333333333333333D) - this.getEyeY() + 0.1;
                    double d2 = target.getZ() - this.getZ();
                    double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                    float f1 = (float)(-(Mth.atan2(d1, d3) * (double)(180F / (float)Math.PI)));
                    float x = this.getXRot();
                    this.setXRot(Mth.wrapDegrees(f1 - x) + x);
                    this.releaseUsingItem();
                    this.performRangedAttack(target, 1F);
                    goal.resetAttackTime();
                }
            }
        } else if (goal.canDraw()){
            this.startDrawing(toolStack);
        }
    }
    
    @Override
    public abstract void performRangedAttack(@NotNull LivingEntity target, float distanceFactor);
    
    public abstract Predicate<ItemStack> canRangedAttack();
    
    public abstract void startDrawing(ToolStack tool);

    @Override
    public void setItemSlot(@NotNull EquipmentSlot slot, @NotNull ItemStack itemStack) {
        super.setItemSlot(slot, itemStack);
        if (!this.level().isClientSide) {
            this.reassessWeaponGoal();
        }

    }

}
