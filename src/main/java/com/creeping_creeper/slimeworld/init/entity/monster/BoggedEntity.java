package com.creeping_creeper.slimeworld.init.entity.monster;

import com.creeping_creeper.slimeworld.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.IForgeShearable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoggedEntity extends AbstractSkeleton implements IForgeShearable {
    private static final EntityDataAccessor<Boolean> DATA_SHEARED = SynchedEntityData.defineId(BoggedEntity.class, EntityDataSerializers.BOOLEAN);

    public BoggedEntity(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
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
        return ModSounds.BOGGED_AMBIENT.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.BOGGED_HURT.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSounds.BOGGED_DEATH.get();
    }

    @Override
    protected @NotNull SoundEvent getStepSound() {
        return ModSounds.BOGGED_STEP.get();
    }

    @Override
    protected @NotNull AbstractArrow getArrow(@NotNull ItemStack arrowStack, float distanceFactor) {
        AbstractArrow abstractarrow = super.getArrow(arrowStack, distanceFactor);
        if (abstractarrow instanceof Arrow) {
            ((Arrow)abstractarrow).addEffect(new MobEffectInstance(MobEffects.POISON, 80));
        }

        return abstractarrow;
    }

    public boolean isSheared() {
        return this.entityData.get(DATA_SHEARED);
    }

    public void setSheared(final boolean sheared) {
        this.entityData.set(DATA_SHEARED, sheared);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_SHEARED, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("sheared", this.isSheared());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSheared(compound.getBoolean("sheared"));
    }

    @Override
    protected @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        player.getItemInHand(hand);
        return InteractionResult.PASS;
    }

    @Override
    public boolean isShearable(@NotNull ItemStack item, Level world, BlockPos pos) {
        return !this.isSheared() && this.isAlive();
    }

    @Override
    public @NotNull List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level world, BlockPos pos, int fortune) {
        world.playSound(null, this, ModSounds.BOGGED_SHEAR.get(), player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
        this.gameEvent(GameEvent.SHEAR, player);
        if (!world.isClientSide()) {
            this.setSheared(true);
            List<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                Item item1 = this.random.nextBoolean() ? Items.RED_MUSHROOM : Items.BROWN_MUSHROOM;
                items.add(item1.getDefaultInstance());
            }
            return items;
        }
        return Collections.emptyList();
    }
}
