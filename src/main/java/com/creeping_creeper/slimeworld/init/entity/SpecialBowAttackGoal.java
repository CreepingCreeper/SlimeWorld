package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.init.entity.golem.RangeSlimeGolemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.EnumSet;

@SuppressWarnings("unused")
public class SpecialBowAttackGoal<T extends RangeSlimeGolemEntity> extends Goal {
    protected final T mob;
    private final double speedModifier;
    private int attackIntervalMin;
    private final float attackRadiusSqr;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public SpecialBowAttackGoal(T mob, double speedModifier, int attackIntervalMin, float attackRadiusSqr) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.attackIntervalMin = attackIntervalMin;
        this.attackRadiusSqr = attackRadiusSqr;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public void setMinAttackInterval(int attackIntervalMin) {
        this.attackIntervalMin = attackIntervalMin;
    }
    
    private ToolStack toolStack(){
        return ToolStack.from(this.mob.getMainHandItem());
    }

    public boolean canUse() {
        return this.mob.getTarget() != null && this.isHoldingBow();
    }

    protected boolean isHoldingBow() {
        return this.mob.isHolding(this.mob.canRangedAttack());
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.mob.getNavigation().isDone()) && this.isHoldingBow();
    }

    public void start() {
        super.start();
        this.mob.setAggressive(true);
    }

    public void stop() {
        super.stop();
        this.mob.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.mob.stopUsingItem();
    }
    

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            double d0 = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
            boolean flag = this.mob.getSensing().hasLineOfSight(target);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }

            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 20) {
                this.mob.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.mob.getNavigation().moveTo(target, this.speedModifier);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.mob.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }

                if ((double)this.mob.getRandom().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }

                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (d0 > (double)(this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double)(this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                Entity entity = this.mob.getControlledVehicle();
                if (entity instanceof Mob mob1) {
                    mob1.lookAt(target, 30.0F, 30.0F);
                }

                this.mob.lookAt(target, 30.0F, 30.0F);
            } else {
                this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }

            this.mob.shoot(this, flag, toolStack(), target);
        }
    }

    public boolean canSee(){
        return this.seeTime >= -60;
    }

    public boolean canDraw(){
        return --this.attackTime <= 0 && this.canSee();
    }

    public void resetAttackTime(){
        this.attackTime = this.attackIntervalMin;
    }
}
