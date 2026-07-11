package com.creeping_creeper.slimeworld.init.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;

public class SlimeGolemEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_SHIELD = SynchedEntityData.defineId(SlimeGolemEntity.class, EntityDataSerializers.BOOLEAN);

    private int hurtTick = 0;
    private int shieldTick = 0;
    public final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, 1.2, false) {
        public void stop() {
            super.stop();
            SlimeGolemEntity.this.setAggressive(false);
        }

        public void start() {
            super.start();
            SlimeGolemEntity.this.setAggressive(true);
        }
    };

    public SlimeGolemEntity(EntityType<? extends SlimeGolemEntity> entityType, Level level) {
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_SHIELD, false);
    }

    public boolean isUsingShield() {
        return this.entityData.get(DATA_SHIELD);
    }

    public void setUsingShield(boolean using) {
        this.entityData.set(DATA_SHIELD, using);
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
        if (this.shieldTick > -100) {
            if(this.shieldTick <= 60) {
                this.stopUsingItem();
                setUsingShield(false);
            }
            this.shieldTick --;
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

    private void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.addGoal(4, this.meleeGoal);
        }

    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (amount > 0) this.hurtTick = 200;
        if (!source.is(DamageTypeTags.BYPASSES_SHIELD) && this.getOffhandItem().canPerformAction(ToolActions.SHIELD_BLOCK)) {
            if (this.shieldTick <= 0 && random.nextInt(100) < -this.shieldTick) {
                this.startUsingItem(InteractionHand.OFF_HAND);
                setUsingShield(true);
                this.shieldTick = 100;
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void startUsingItem(@NotNull InteractionHand p_21159_) {
        ItemStack itemstack = this.getItemInHand(p_21159_);
        if (!itemstack.isEmpty() && !this.isUsingItem()) {
            //很遗憾地告诉大家，匠魂工具的getUseDuration()和UseAnim()只能在玩家使用时受到特性的影响，所以此处需要特判
            int duration = ForgeEventFactory.onItemUseStart(this, itemstack, itemstack.getItem() instanceof IModifiableDisplay ? 72000 : itemstack.getUseDuration());
            if (duration < ForgeConfig.SERVER.getUseItemDuration()) return;
            this.useItem = itemstack;
            this.useItemRemaining = duration;
            if (!this.level().isClientSide) {
                this.setLivingEntityFlag(1, true);
                this.setLivingEntityFlag(2, p_21159_ == InteractionHand.OFF_HAND);
                this.gameEvent(GameEvent.ITEM_INTERACT_START);
            }

        }
    }

    @Override
    public boolean isBlocking() {
        //取消格挡前摇时间
        return this.isUsingItem() && !this.useItem.isEmpty() && this.useItem.canPerformAction(ToolActions.SHIELD_BLOCK);
    }

    @Override
    protected void blockUsingShield(@NotNull LivingEntity attacker) {
        super.blockUsingShield(attacker);
        if (attacker.getMainHandItem().canDisableShield(this.useItem, this, attacker)) {
            this.stopUsingItem();
            this.level().broadcastEntityEvent(this, EntityEvent.SHIELD_DISABLED);
        }
    }


    @Override
    protected void hurtCurrentlyUsedShield(float damage) {
        if (this.useItem.canPerformAction(ToolActions.SHIELD_BLOCK) && damage >= 3.0F ) {
            int damageAmount = 1 + Mth.floor(damage);
            InteractionHand interactionhand = this.getUsedItemHand();
            this.useItem.hurtAndBreak(damageAmount, this, slime -> {
                slime.broadcastBreakEvent(interactionhand);
                slime.stopUsingItem();
            });
            if (this.useItem.isEmpty()) {
                if (interactionhand == InteractionHand.OFF_HAND && !(this.getOffhandItem().getItem() instanceof IModifiableDisplay)) {
                    this.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                this.useItem = ItemStack.EMPTY;
                this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level().random.nextFloat() * 0.4F);
            } else {
                this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 1.0F);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("hurtTick", this.hurtTick);
        compound.putInt("shieldTick", this.shieldTick);
        compound.putBoolean("usingShield", this.isUsingShield());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.reassessWeaponGoal();
        this.hurtTick = compound.getInt("hurtTick");
        this.shieldTick = compound.getInt("shieldTick");
        this.setUsingShield(compound.getBoolean("usingShield"));
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
