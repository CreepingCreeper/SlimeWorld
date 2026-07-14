package com.creeping_creeper.slimeworld.init.entity.golem;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

public class BaseSlimeGolemEntity extends Monster {
    private int hurtTick = 0;
    protected final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2, false) {
        public void stop() {
            super.stop();
            BaseSlimeGolemEntity.this.setAggressive(false);
        }

        public void start() {
            super.start();
            BaseSlimeGolemEntity.this.setAggressive(true);
        }
    };

    public BaseSlimeGolemEntity(EntityType<? extends BaseSlimeGolemEntity> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(true);
        this.reassessWeaponGoal();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
     }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25F).add(ForgeMod.ENTITY_REACH.get(), 1.0F);
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState block) {
       this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }


    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.SLIME_HURT;
    }

    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SLIME_SQUISH;
    }

    @Override
    public void customServerAiStep() {
        if (this.tickCount % 10 == 0){
            if (this.hurtTick > 0) this.hurtTick -= 10;
            else this.heal(1F);
        }
    }

    @Override
    public void rideTick() {
        super.rideTick();
        Entity entity = this.getControlledVehicle();
        if (entity instanceof PathfinderMob pathfindermob) {
            this.yBodyRot = pathfindermob.yBodyRot;
        }

    }

    protected void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.addGoal(4, this.meleeGoal);
        }

    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (amount > 0) this.hurtTick = 200;
        return super.hurt(source, amount);
    }


    @Override
    public void setItemSlot(@NotNull EquipmentSlot slot, @NotNull ItemStack itemStack) {
        super.setItemSlot(slot, itemStack);
        if (!this.level().isClientSide) {
            this.reassessWeaponGoal();
        }

    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("hurtTick", this.hurtTick);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.reassessWeaponGoal();
        this.hurtTick = compound.getInt("hurtTick");
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions size) {
        return 1.74F;
    }

    @Override
    public double getMyRidingOffset() {
        return -0.6;
    }

}
