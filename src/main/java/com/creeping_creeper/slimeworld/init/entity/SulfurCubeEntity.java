package com.creeping_creeper.slimeworld.init.entity;


import com.creeping_creeper.slimeworld.data.ModTags;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IForgeShearable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SulfurCubeEntity extends Slime implements IForgeShearable {
    private static final double PUSH_DISTANCE_THRESHOLD = 1.3F;
    private static final double MAX_PLAYER_PUSH_SPEED = 0.5F;
    private static final float PLAYER_PUSH_SPEED_SCALE_MULTIPLIER = 0.3F;
    private static final float VERTICAL_PUSH_MULTIPLIER = 0.3F;
    private static final float DAMAGE_MULTIPLIER_SCALE = 0.6F;
    private static final float PUSH_SOUND_THRESHOLD = 0.5F;
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SulfurCubeEntity.class, EntityDataSerializers.BOOLEAN);

    private int pickupTimer = 0;
    private boolean floatsInLiquids = false;

    public SulfurCubeEntity(EntityType<? extends SulfurCubeEntity> type, Level worldIn) {
        super(type, worldIn);
        this.setCanPickUpLoot(true);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FROM_BUCKET, false);
    }

    @Override
    public void setSize(int size, boolean resetHealth) {
        super.setSize(size, resetHealth);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.getSize() * 5.0 - 1);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(8.0);
        this.setHealth(this.getMaxHealth());
    }

    // AI 目标
//    @Override
//    protected void registerGoals() {
//        super.registerGoals();
//        this.goalSelector.addGoal(2, new SulfurCubeTemptGoal(this, 1.0D, stack -> isBaby() ? stack.is(ItemTags.SULFUR_CUBE_FOOD) : isSwallowableItem(stack), false, 1.0D));
//        this.goalSelector.addGoal(3, new SulfurCubeSearchForItemsGoal(this, this));
//    }

    // 桶数据
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    //@Override
    public void saveToBucketTag(ItemStack itemStack) {
        Bucketable.saveDefaultDataToBucketTag(this, itemStack);
       // CompoundTag tag = itemStack.getOrCreateChildTag("BucketEntityData");
       // tag.putInt("Age", this.getAge());
    }

    //@Override
    public void loadFromBucketTag(CompoundTag compoundTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, compoundTag);
        //this.setAge(tag.getInt("Age"));
    }

//    @Override
//    public ItemStack getBucketItemStack() {
//        return new ItemStack(Items.SULFUR_CUBE_BUCKET);
//    }

    // Bucketable 实现
    //@Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_EMPTY;
    }

    // 水下呼吸
    @Override
    public boolean canBreatheUnderwater() {
        return hasBodyItem() || super.canBreatheUnderwater();
    }

    @Override
    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return 0.175F * size.height;
    }

    @Override
    protected boolean isDealsDamage() {
        return false;
    }

    // 受伤逻辑（免疫）
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (hasBodyItem() && source.is(ModTags.DamageTypes.SULFUR_CUBE_IMMUNE)) {
            if (source.getEntity() instanceof Player player) {
                Vec3 playerEyePosition = player.getEyePosition();
                Vec3 cubePosition = this.getBoundingBox().getCenter();
                Vec3 playerToCubeDirectionEye = cubePosition.subtract(playerEyePosition);
                Vec3 playerAimDirection = player.getLookAngle().scale(playerToCubeDirectionEye.length());
                double hitScale = (double)1.0F / (double)((float)this.getSize() * this.getScale());
                Vec3 hitVector = playerToCubeDirectionEye.subtract(playerAimDirection).scale(hitScale);
                hitVector = hitVector.add(cubePosition.subtract(player.position()).normalize().scale(hitScale)).scale(0.5F);
                this.playSound(this.getHurtSound(source));
                this.applyKnockback(amount, hitVector);
                return false;
            }
        }
        return super.hurt(source, amount);
    }

    private void applyKnockback(final float damage, final Vec3 hitVector) {
        double damageMultiplier = Mth.sqrt(damage);
        damageMultiplier *= Math.max(0.0F, (double)1.0F - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        Vec3 deltaVector = hitVector.scale(damageMultiplier * (double)0.6F);
        this.addDeltaMovement(deltaVector);
    }

    public boolean hasBodyItem() {
        return !isTiny() && !this.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && pickupTimer > 0) {
            pickupTimer--;
        }
    }

    @Override
    public float getStepHeight() {
        return hasBodyItem() ? 0.0F : super.getStepHeight();
    }

    private static boolean isSwallowableItem(ItemStack stack) {
        return stack.is(ModTags.Items.SulfurCubeSwallowable);
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return !hasBodyItem() && this.pickupTimer <= 0 && isSwallowableItem(stack);
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        ItemStack itemstack = itemEntity.getItem();
        if (this.canHoldItem(itemstack)) {
            int i = itemstack.getCount();
            if (i > 1) {
                ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), itemstack.split(i - 1));
                this.level().addFreshEntity(itementity);
            }
            this.onItemPickup(itemEntity);
            this.setItemSlot(EquipmentSlot.HEAD, itemstack.split(1));
            this.take(itemEntity, itemstack.getCount());
            itemEntity.discard();
            this.pickupTimer = 100;
            this.setNoAi(true);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level().isClientSide() && !isTiny() && stack.is(ModTags.Items.SulfurCubeSwallowable)){
            ItemStack oldStack = this.getItemBySlot(EquipmentSlot.HEAD);
            if (!oldStack.isEmpty()){
                this.spawnAtLocation(oldStack.getItem().getDefaultInstance(), 1.7F);
                this.pickupTimer = 100;
            }
            this.setItemSlot(EquipmentSlot.HEAD, stack.getItem().getDefaultInstance());
            this.setNoAi(true);
            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

        // NBT 存储
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("PickupTimer", pickupTimer);
        compound.putBoolean("FromBucket", fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        pickupTimer = compound.getInt("PickupTimer");
        setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    public void playerTouch(Player player) {
       playerPush(player);
    }

    private Vec3 horizontal(Vec3 vec3) {
        return new Vec3(vec3.x, 0.0F, vec3.z);
    }

    private void playerPush(Player player) {
        if (this.hasBodyItem()) {
            Vec3 cubeToPlayer = this.position().subtract(player.position());
            if (cubeToPlayer.horizontalDistance() < (double)1.3F && player.getY() <= this.getY() + (double)this.getBbHeight()) {
                double knockback = Math.max(0.0F, (double)1.0F - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                Vec3 pushDirection = horizontal(cubeToPlayer).normalize().scale(knockback);
                double playerSpeed = player.getDeltaMovement().length() * (double)2.0F * (double)0.3F;
                playerSpeed = Mth.clamp(playerSpeed, 0.0F, 0.5F);
                Vec3 pushVelocity = (new Vec3(pushDirection.x, this.onGround() ? knockback * (double)0.3F : (double)0.0F, pushDirection.z)).scale(playerSpeed);
                if (pushVelocity.lengthSqr() > (double)0.25F) {
                    this.playSound(this.getSquishSound());
                }

                this.addDeltaMovement(pushVelocity);
            }

        }
    }

    @Override
    protected ParticleOptions getParticleType() {
        return ModEntities.sulfurCubeGoo.get();
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        this.setSize(Math.min(2, this.getSize()), true);
        this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 1.0F;
        return data;
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        // on death, split into multiple slimes, and let them inherit armor if it did not drop
        Level level = level();
        if (!level.isClientSide && this.getSize() > 1 && this.isDeadOrDying()) {
            Component name = this.getCustomName();
            boolean noAi = this.isNoAi();
            boolean invulnerable = this.isInvulnerable();
            for(int i = 0; i < 2; ++i) {
                float x = ((i % 2) - 0.5F) * 0.5F;
                SulfurCubeEntity slime =  ModEntities.sulfurCubeEntity.get().create(level);
                assert slime != null;
                if (this.isPersistenceRequired()) {
                    slime.setPersistenceRequired();
                }
                slime.setCustomName(name);
                slime.setNoAi(noAi);
                slime.setInvulnerable(invulnerable);
                slime.setSize(1, true);
                slime.moveTo(this.getX() + x, this.getY() + 0.5D, this.getZ() -0.25F, this.random.nextFloat() * 360.0F, 0.0F);
                level.addFreshEntity(slime);
            }
        }

        // calling supper does the split reason again, but we need to transfer armor
        this.setRemoved(reason);
        if (reason == Entity.RemovalReason.KILLED) {
            this.gameEvent(GameEvent.ENTITY_DIE);
        }
        this.invalidateCaps();
    }

    @Override
    public boolean isShearable(@NotNull ItemStack item, Level world, BlockPos pos) {
        return hasBodyItem() && this.isAlive();
    }

    @Override
    public @NotNull List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level world, BlockPos pos, int fortune) {
        world.playSound(null, this, ModSounds.BOGGED_SHEAR.get(), player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
        this.gameEvent(GameEvent.SHEAR, player);
        if (!world.isClientSide()) {
            List<ItemStack> items = new ArrayList();
            items.add(this.getItemBySlot(EquipmentSlot.HEAD));
            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
            this.setNoAi(false);
            this.pickupTimer = 100;
            return items;
        }
        return Collections.emptyList();
    }
}
