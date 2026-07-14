package com.creeping_creeper.slimeworld.init.entity.golem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.tools.item.IModifiableDisplay;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.data.ModifierIds;

public class EarthSlimeGolemEntity extends BaseSlimeGolemEntity {
    private int shieldTick = 0;
    public EarthSlimeGolemEntity(EntityType<? extends EarthSlimeGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void customServerAiStep() {
        if (this.shieldTick > -100) {
            if(this.shieldTick <= 60) {
                this.releaseUsingItem();
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

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        ItemStack itemStack = this.getOffhandItem();
        if (!source.is(DamageTypeTags.BYPASSES_SHIELD) && itemStack.getItem() instanceof ModifiableItem) {
            ToolStack tool = ToolStack.from(itemStack);
            if (tool.getModifierLevel(ModifierIds.blocking) > 0 && this.shieldTick <= 0 && random.nextInt(100) < -this.shieldTick) {
                GeneralInteractionModifierHook.startUsing(ToolStack.from(itemStack), ModifierIds.blocking, this, InteractionHand.OFF_HAND);
                this.shieldTick = 100;
            }
        }
        return super.hurt(source, amount);
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
        compound.putInt("shieldTick", this.shieldTick);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.reassessWeaponGoal();
        this.shieldTick = compound.getInt("shieldTick");
    }
}
