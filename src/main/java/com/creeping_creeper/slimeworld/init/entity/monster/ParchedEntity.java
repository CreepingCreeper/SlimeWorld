package com.creeping_creeper.slimeworld.init.entity.monster;

import com.creeping_creeper.slimeworld.init.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ParchedEntity extends AbstractSkeleton {
    public ParchedEntity(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes().add(Attributes.MAX_HEALTH, 16.0F);
    }

    @Override
    public void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, (item) -> item instanceof BowItem));
            if (itemstack.is(Items.BOW)) {
                int i = 50;
                if (this.level().getDifficulty() != Difficulty.HARD) {
                    i = 70;
                }

                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(4, this.bowGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }
        }

    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.PARCHED_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.PARCHED_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.PARCHED_DEATH.get();
    }

    @Override
    protected SoundEvent getStepSound() {
        return ModSounds.PARCHED_STEP.get();
    }

    @Override
    protected AbstractArrow getArrow(ItemStack arrowStack, float distanceFactor) {
        AbstractArrow abstractarrow = super.getArrow(arrowStack, distanceFactor);
        if (abstractarrow instanceof Arrow) {
            ((Arrow)abstractarrow).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
        }

        return abstractarrow;
    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }

    @Override
    public boolean canBeAffected(final MobEffectInstance newEffect) {
        return newEffect.getEffect() != MobEffects.WEAKNESS && super.canBeAffected(newEffect);
    }
}
