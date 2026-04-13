package com.creeping_creeper.slimeworld.init.entity;


import com.creeping_creeper.slimeworld.data.ModTags;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModSounds;
import com.creeping_creeper.slimeworld.library.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class SulfurCubeEntity extends Slime implements IForgeShearable, Bucketable {
    private static final UUID SLOW_FALLING_ID = UUID.fromString("A5B6CF2A-2F7C-31EF-9022-7C3E7D5E6ABA");
    private static final AttributeModifier SLOW_FALLING = new AttributeModifier(SLOW_FALLING_ID, "Slow falling acceleration reduction", -0.07, AttributeModifier.Operation.ADDITION);
    private static final Predicate<ItemEntity> ALLOWED_ITEMS = (e) -> !e.hasPickUpDelay() && e.isAlive() && isSwallowableItem(e.getItem());
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SulfurCubeEntity.class, EntityDataSerializers.BOOLEAN);

    protected int age;
    private int pickupTimer = 0;
    public float bounciness = 0;
    private Vec3 bounce = Vec3.ZERO;
    public float frictionModifier = 1.0F;
    public float airDragModifier = 1.0F;
    public boolean floatsInLiquids = false;

    public SulfurCubeEntity(EntityType<? extends SulfurCubeEntity> type, Level worldIn) {
        super(type, worldIn);
        this.setCanPickUpLoot(true);
        this.age = this.isTiny() ? -24000 : 0;
        this.moveControl = new SulfurCubeMoveControl<>(this);
        this.lookControl = new SulfurCubeLookControl(this);
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
        if(resetHealth){
            this.setHealth(this.getMaxHealth());
        }
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new SulfurCubeTemptGoal((itemStack) -> isTiny() ? isFoodItem(itemStack) : isSwallowableItem(itemStack)));
        this.goalSelector.addGoal(3, new SulfurCubeSearchForItemsGoal());

    }

    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        CompoundTag compound = stack.getOrCreateTag();
        compound.putInt("Size", this.getSize());
        compound.putInt("Age", this.age);
        compound.putInt("PickupTimer", this.pickupTimer);
        if (hasBodyItem()){
            CompoundTag compoundtag = new CompoundTag();
            this.getItemBySlot(EquipmentSlot.HEAD).save(compoundtag);
            compound.put("BodyItems", compoundtag);
        }
    }

    @Override
    public void loadFromBucketTag(CompoundTag compound) {
        Bucketable.loadDefaultDataFromBucketTag(this, compound);
        if (compound.contains("Size")) {
            this.setSize(compound.getInt("Size"), false);
        }
        if (compound.contains("Age")) {
            this.age = compound.getInt("Age");
        }
        if (compound.contains("PickupTimer")) {
            this.pickupTimer = compound.getInt("PickupTimer");
        }
        if (compound.contains("BodyItems", Tag.TAG_COMPOUND)) {
            CompoundTag compoundtag = compound.getCompound("BodyItems");
            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.of(compoundtag));
        }
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.SulfurCubeBucket);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    public boolean canBreatheUnderwater() {
        return hasBodyItem() || super.canBreatheUnderwater();
    }

    @Override
    public void move(MoverType type, Vec3 pos) {
        super.move(type, pos);
        if (bounciness > 0){
            double x = bounce.x, y = bounce.y, z = bounce.z;
            boolean shouldBounce = false;
            if ((this.horizontalCollision)) {
                  if (Math.abs(bounce.x) > 1.0E-7){
                       x *= -bounciness;
                      shouldBounce = true;
                  }
                   if (Math.abs(bounce.z) > 1.0E-7){
                      z *= -bounciness;
                       shouldBounce = true;
                 }
              }
            if (this.verticalCollision && Math.abs(bounce.y) > 1.0E-7) {
                y *= -bounciness;
                shouldBounce = true;
            }
            if (shouldBounce){
                this.setDeltaMovement(new Vec3(x, y, z));
            }
            bounce = this.getDeltaMovement();
        }
    }
    @Override
    public void travel(Vec3 travelVector) {
        if (this.isControlledByLocalInstance()) {
            double d0;
            AttributeInstance gravity = this.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
            boolean flag = this.getDeltaMovement().y <= (double)0.0F;
            if (flag && this.hasEffect(MobEffects.SLOW_FALLING)) {
                if (!gravity.hasModifier(SLOW_FALLING)) {
                    gravity.addTransientModifier(SLOW_FALLING);
                }
            } else if (gravity.hasModifier(SLOW_FALLING)) {
                gravity.removeModifier(SLOW_FALLING);
            }

            d0 = gravity.getValue();
            FluidState fluidstate = this.level().getFluidState(this.blockPosition());
            if (this.hasBodyItem() && this.floatsInLiquids) {
                float vibeAmount = 0.2F * Mth.sin((float) this.tickCount * 0.4F);
                double immersion = this.getFluidHeight(this.isInWater() ? FluidTags.WATER : FluidTags.LAVA) - this.getFluidJumpThreshold() + (double) vibeAmount;
                if (immersion > (double) 0.0F) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0F, Math.min(1.0F, immersion) * (double) 0.04F, 0.0F));
                }

            }
            if ((this.isInWater() || this.isInFluidType(fluidstate) && fluidstate.getFluidType() != ForgeMod.LAVA_TYPE.get()) && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate)) {
                if (this.isInWater() || this.isInFluidType(fluidstate) && !this.moveInFluid(fluidstate, travelVector, d0)) {
                    double d9 = this.getY();
                    float f4 = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
                    float f5 = 0.02F;
                    float f6 = (float) EnchantmentHelper.getDepthStrider(this);
                    if (f6 > 3.0F) {
                        f6 = 3.0F;
                    }

                    if (!this.onGround()) {
                        f6 *= 0.5F;
                    }

                    if (f6 > 0.0F) {
                        f4 += (0.54600006F - f4) * f6 / 3.0F;
                        f5 += (this.getSpeed() - f5) * f6 / 3.0F;
                    }

                    if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
                        f4 = 0.96F;
                    }

                    f5 *= (float)this.getAttribute(ForgeMod.SWIM_SPEED.get()).getValue();
                    this.moveRelative(f5, travelVector);
                    this.move(MoverType.SELF, this.getDeltaMovement());
                    Vec3 vec36 = this.getDeltaMovement();
                    if (this.horizontalCollision && this.onClimbable()) {
                        vec36 = new Vec3(vec36.x, 0.2, vec36.z);
                    }

                    this.setDeltaMovement(vec36.multiply(f4, 0.8F, f4));
                    Vec3 vec32 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
                    this.setDeltaMovement(vec32);
                    if (this.horizontalCollision && this.isFree(vec32.x, vec32.y + (double)0.6F - this.getY() + d9, vec32.z)) {
                        this.setDeltaMovement(vec32.x, 0.3F, vec32.z);
                    }
                }
            } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate)) {
                double d8 = this.getY();
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.5F, 0.8F, 0.5F));
                    Vec3 vec33 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
                    this.setDeltaMovement(vec33);
                } else {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.5F));
                }

                if (!this.isNoGravity()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0.0F, -d0 / (double)4.0F, 0.0F));
                }

                Vec3 vec34 = this.getDeltaMovement();
                if (this.horizontalCollision && this.isFree(vec34.x, vec34.y + (double)0.6F - this.getY() + d8, vec34.z)) {
                    this.setDeltaMovement(vec34.x, 0.3F, vec34.z);
                }
            } else if (this.isFallFlying()) {
                this.checkSlowFallDistance();
                Vec3 vec3 = this.getDeltaMovement();
                Vec3 vec31 = this.getLookAngle();
                float f = this.getXRot() * ((float)Math.PI / 180F);
                double d1 = Math.sqrt(vec31.x * vec31.x + vec31.z * vec31.z);
                double d3 = vec3.horizontalDistance();
                double d4 = vec31.length();
                double d5 = Math.cos(f);
                d5 = d5 * d5 * Math.min(1.0F, d4 / 0.4);
                vec3 = this.getDeltaMovement().add(0.0F, d0 * ((double)-1.0F + d5 * (double)0.75F), 0.0F);
                if (vec3.y < (double)0.0F && d1 > (double)0.0F) {
                    double d6 = vec3.y * -0.1 * d5;
                    vec3 = vec3.add(vec31.x * d6 / d1, d6, vec31.z * d6 / d1);
                }

                if (f < 0.0F && d1 > (double)0.0F) {
                    double d10 = d3 * (double)(-Mth.sin(f)) * 0.04;
                    vec3 = vec3.add(-vec31.x * d10 / d1, d10 * 3.2, -vec31.z * d10 / d1);
                }

                if (d1 > (double)0.0F) {
                    vec3 = vec3.add((vec31.x / d1 * d3 - vec3.x) * 0.1, 0.0F, (vec31.z / d1 * d3 - vec3.z) * 0.1);
                }

                this.setDeltaMovement(vec3.multiply(0.99F, 0.98F, 0.99F));
                this.move(MoverType.SELF, this.getDeltaMovement());
                if (this.horizontalCollision && !this.level().isClientSide) {
                    double d11 = this.getDeltaMovement().horizontalDistance();
                    double d7 = d3 - d11;
                    float f1 = (float)(d7 * (double)10.0F - (double)3.0F);
                    if (f1 > 0.0F) {
                        this.playSound(this.getFallDamageSound((int)f1), 1.0F, 1.0F);
                        this.hurt(this.damageSources().flyIntoWall(), f1);
                    }
                }

                if (this.onGround() && !this.level().isClientSide) {
                    this.setSharedFlag(7, false);
                }
            } else {
                BlockPos blockpos = this.getBlockPosBelowThatAffectsMyMovement();
                float blockFriction = this.onGround() ? computeModifiedFriction(this.level().getBlockState(blockpos).getBlock().getFriction(), this.frictionModifier) : 1.0F;
                Vec3 vec35 = this.handleRelativeFrictionAndCalculateMovement(travelVector, blockFriction);
                double d2 = vec35.y;
                if (this.hasEffect(MobEffects.LEVITATION)) {
                    d2 += (0.05 * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec35.y) * 0.2;
                } else if (this.level().isClientSide && !this.level().hasChunkAt(blockpos)) {
                    if (this.getY() > (double)this.level().getMinBuildHeight()) {
                        d2 = -0.1;
                    } else {
                        d2 = 0.0F;
                    }
                } else if (!this.isNoGravity()) {
                    d2 -= d0;
                }

                if (this.shouldDiscardFriction()) {
                    this.setDeltaMovement(vec35.x, d2, vec35.z);
                } else {
                    float entityAirDragModifier = this.airDragModifier;
                    float airDrag = computeModifiedFriction(0.91F, entityAirDragModifier);
                    double friction = (double) blockFriction * airDrag;
                    this.setDeltaMovement(vec35.x * friction, d2 * friction, vec35.z * friction);

                }
            }
        }
    }

    private static float computeModifiedFriction(final float friction, final float modifier) {
        return Mth.clamp(1.0F - (1.0F - friction) * modifier, 0.0F, 1.0F);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return 0.175F * size.height;
    }

    @Override
    protected boolean isDealsDamage() {
        return false;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.hasBodyItem() && source.is(ModTags.DamageTypes.SULFUR_CUBE_IMMUNE)) {
            Entity var5 = source.getEntity();
            if (var5 instanceof LivingEntity player && var5.getType().is(ModTags.EntityTypes.PlaySulfurCube)) {
                this.playerHit(player, amount);
                return false;
            } else {
                return true;
            }
        } else {
            return super.hurt(source, amount);
        }
    }

    private void playerHit(LivingEntity player, float damage) {
        Vec3 playerEyePosition = player.getEyePosition();
        Vec3 cubePosition = this.getBoundingBox().getCenter();
        Vec3 playerToCubeDirectionEye = cubePosition.subtract(playerEyePosition);
        Vec3 playerAimDirection = player.getLookAngle().scale(playerToCubeDirectionEye.length());
        double hitScale = (double)1.0F / (double)((float)this.getSize() * this.getScale());
        Vec3 hitVector = playerToCubeDirectionEye.subtract(playerAimDirection).scale(hitScale);
        hitVector = hitVector.add(cubePosition.subtract(player.position()).normalize().scale(hitScale)).scale(0.5F);
        this.playSound(this.getHitSound());
        this.applyKnockback(damage, hitVector);
    }


    private void applyKnockback(final float damage, final Vec3 hitVector) {
        double damageMultiplier = Mth.sqrt(damage);
        damageMultiplier *= Math.max(0.0F, (double)1.0F - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
        Vec3 deltaVector = hitVector.scale(damageMultiplier * (double)0.6F);
        this.addDeltaMovement(deltaVector);
    }

    public boolean hasBodyItem() {
        return !this.getItemBySlot(EquipmentSlot.HEAD).isEmpty();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (pickupTimer > 0){
                pickupTimer--;
            }else if (this.isTiny() && this.isAlive()){
                if (age == 0){
                    this.setSize(2, true);
                }else age++;
           }
        }
    }

    @Override
    public boolean isBaby() {
        return this.isTiny();
    }

    @Override
    public float getStepHeight() {
        return hasBodyItem() ? 0.0F : super.getStepHeight();
    }

    private static boolean isSwallowableItem(ItemStack stack) {
        return stack.is(ModTags.Items.SulfurCubeSwallowable);
    }

    private static boolean isFoodItem(ItemStack stack) {
        return stack.is(Tags.Items.SLIMEBALLS);
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return !isTiny() && !hasBodyItem() && this.pickupTimer <= 0 && isSwallowableItem(stack);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.hasBodyItem() || this.fromBucket();
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return !this.isLeashed();
    }

    @Override
    protected void tickLeash() {
        super.tickLeash();
        Entity entity = this.getLeashHolder();
        if (entity != null && entity.level() == this.level()) {
            this.restrictTo(entity.blockPosition(), 5);
            float f = this.distanceTo(entity);
            if (f > 12.0F) {
                this.dropLeash(true, true);
                this.goalSelector.disableControlFlag(Goal.Flag.MOVE);
            } else if (f > 6.0F) {
                double d0 = (entity.getX() - this.getX()) / (double)f;
                double d1 = (entity.getY() - this.getY()) / (double)f;
                double d2 = (entity.getZ() - this.getZ()) / (double)f;
                this.setDeltaMovement(this.getDeltaMovement().add(Math.copySign(d0 * d0 * 0.4, d0), Math.copySign(d1 * d1 * 0.4, d1), Math.copySign(d2 * d2 * 0.4, d2)));
                this.checkSlowFallDistance();
            } else {
                this.goalSelector.enableControlFlag(Goal.Flag.MOVE);
                Vec3 vec3 = (new Vec3(entity.getX() - this.getX(), entity.getY() - this.getY(), entity.getZ() - this.getZ())).normalize().scale(Math.max(f - 2.0F, 0.0F));
                this.getNavigation().moveTo(this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z, 1.0F);
            }
        }

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
            this.playSound(this.getAbsorbSound());
            itemEntity.discard();
            this.pickupTimer = 100;
        }
    }

    @Override
    public boolean equipmentHasChanged(ItemStack oldItem, ItemStack newItem) {
         if (super.equipmentHasChanged(oldItem, newItem)) {
            if (!newItem.isEmpty()) {
                this.setSpeed(0.0F);
                SulfurCubeArchetype.getArchetype(this);
            }else SulfurCubeArchetype.resetArchetype(this);
            return true;
        }

        return false;
    }

    @Override
    public float maxUpStep() {
        return this.hasBodyItem() ? 0.0F : super.maxUpStep();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level().isClientSide()){
            if (!isTiny()) {
             if (isSwallowableItem(stack)){
                 ItemStack oldStack = this.getItemBySlot(EquipmentSlot.HEAD);
                 if (!oldStack.isEmpty()) {
                     this.spawnAtLocation(oldStack.getItem().getDefaultInstance(), 1.7F);
                     this.pickupTimer = 100;
                 }
                 this.setItemSlot(EquipmentSlot.HEAD, stack.getItem().getDefaultInstance());
                 stack.shrink(1);
                 this.setPersistenceRequired();
                 this.playSound(this.getAbsorbSound());
                 return InteractionResult.SUCCESS;
             }
            }else if (isFoodItem(stack)) {
                this.age -= (int) (this.age * 0.1);
                ServerLevel server = (ServerLevel)this.level();
                server.sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0F), this.getRandomY() + (double)0.5F, this.getRandomZ(1.0F), 0, 0.0F, 0.0F, 0.0F, 0);
                stack.shrink(1);
                this.setPersistenceRequired();
                return InteractionResult.SUCCESS;
            }
        }
        return bucketMobPickup(player, hand).orElse(super.mobInteract(player, hand));
    }

    private Optional<InteractionResult> bucketMobPickup(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getItem() == Items.BUCKET && this.isAlive()) {
            ItemStack itemstack1 = this.getBucketItemStack();
            this.saveToBucketTag(itemstack1);
            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, player, itemstack1, false);
            player.setItemInHand(hand, itemstack2);
            this.discard();
            return Optional.of(InteractionResult.sidedSuccess(level().isClientSide));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Age", age);
        compound.putInt("PickupTimer", pickupTimer);
        compound.putFloat("Bounciness", bounciness);
        compound.putFloat("FrictionModifier", frictionModifier);
        compound.putFloat("AirDragModifier", airDragModifier);
        compound.putBoolean("FloatsInLiquids", floatsInLiquids);
        compound.putBoolean("FromBucket", fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        age = compound.getInt("Age");
        pickupTimer = compound.getInt("PickupTimer");
        bounciness = compound.getFloat("Bounciness");
        frictionModifier = compound.getFloat("FrictionModifier");
        airDragModifier = compound.getFloat("AirDragModifier");
        floatsInLiquids = compound.getBoolean("FloatsInLiquids");
        setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    public void playerTouch(Player player) {
       playerPush(player);
    }

    private void playerPush(Player player) {
        if (this.hasBodyItem()) {
            Vec3 cubeToPlayer = this.position().subtract(player.position());
            if (cubeToPlayer.horizontalDistance() < (double)1.3F && player.getY() <= this.getY() + (double)this.getBbHeight()) {
                double knockback = Math.max(0.0F, (double)1.0F - this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
                Vec3 pushDirection =  new Vec3(cubeToPlayer.x, 0.0F, cubeToPlayer.z).normalize().scale(knockback);
                double playerSpeed = player.getDeltaMovement().length() * (double)2.0F * (double)0.3F;
                playerSpeed = Mth.clamp(playerSpeed, 0.0F, 0.5F);
                Vec3 pushVelocity = (new Vec3(pushDirection.x, this.onGround() ? knockback * (double)0.3F : (double)0.0F, pushDirection.z)).scale(playerSpeed);
                if (pushVelocity.lengthSqr() > (double)0.25F) {
                    this.playSound(this.getPushSound());
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
       if (reason == MobSpawnType.BUCKET){
           return spawnData;
       }
       SpawnGroupData data = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
       this.setSize(Math.min(2, this.getSize()), true);
       this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 1.0F;
       return data;
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
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

        this.setRemoved(reason);
        if (reason == Entity.RemovalReason.KILLED) {
            this.gameEvent(GameEvent.ENTITY_DIE);
        }
        this.invalidateCaps();
    }

    @Override
    public int getExperienceReward() {
        return this.isTiny() ? 0 : 1 + this.random.nextInt(2);
    }

    private SoundEvent getFallDamageSound(int height) {
        return height > 4 ? this.getFallSounds().big() : this.getFallSounds().small();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return this.isTiny() ? ModSounds.SULFUR_CUBE_HURT_SMALL.get() : ModSounds.SULFUR_CUBE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isTiny() ? ModSounds.SULFUR_CUBE_DEATH_SMALL.get() : ModSounds.SULFUR_CUBE_DEATH.get();
    }

    @Override
    protected SoundEvent getJumpSound() {
        return this.isTiny() ? ModSounds.SULFUR_CUBE_JUMP_SMALL.get() : ModSounds.SULFUR_CUBE_JUMP.get();
    }

    @Override
    protected SoundEvent getSquishSound() {
        if (this.isTiny()) {
            return ModSounds.SULFUR_CUBE_SQUISH_SMALL.get();
        }
        return this.hasBodyItem() ? ModSounds.SULFUR_CUBE_BOUNCE.get() : ModSounds.SULFUR_CUBE_SQUISH.get();
    }

    protected SoundEvent getHitSound() {
        return ModSounds.SULFUR_CUBE_HIT.get();
    }

    private SoundEvent getPushSound() {
        return ModSounds.SULFUR_CUBE_PUSH.get();
    }

    private SoundEvent getAbsorbSound() {
        return ModSounds.SULFUR_CUBE_ABSORB.get();
    }

    private SoundEvent getEjectSound() {
        return ModSounds.SULFUR_CUBE_EJECT.get();
    }

    @Override
    public boolean isShearable(@NotNull ItemStack item, Level world, BlockPos pos) {
        return hasBodyItem() && this.isAlive();
    }

    @Override
    public @NotNull List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level world, BlockPos pos, int fortune) {
        world.playSound(null, this, this.getEjectSound(), player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
        this.gameEvent(GameEvent.SHEAR, player);
        if (!world.isClientSide()) {
            List<ItemStack> items = new ArrayList<>();
            items.add(this.getItemBySlot(EquipmentSlot.HEAD));
            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
            this.pickupTimer = 100;
            return items;
        }
        return Collections.emptyList();
    }

    static class SulfurCubeMoveControl<T extends SulfurCubeEntity> extends Slime.SlimeMoveControl {
        public SulfurCubeMoveControl(final T cubeMob) {
            super(cubeMob);
        }

        public void tick() {
            if (!((SulfurCubeEntity)this.mob).hasBodyItem()) {
                super.tick();
            }

        }
    }

    class SulfurCubeLookControl extends LookControl {
        private SulfurCubeLookControl(SulfurCubeEntity entity) {
            super(entity);
        }

        public void tick() {
            if (!SulfurCubeEntity.this.hasBodyItem()) {
                super.tick();
            } else {
                float closeAngle = ModUtil.wrapDegrees90(SulfurCubeEntity.this.getYRot());
                SulfurCubeEntity.this.setYRot(SulfurCubeEntity.this.getYRot() - closeAngle);
                SulfurCubeEntity.this.setYHeadRot(SulfurCubeEntity.this.getYRot());
            }
        }
    }

    class SulfurCubeTemptGoal extends Goal {
        private final Predicate<ItemStack> items;
        private Player temptingPlayer;

        public SulfurCubeTemptGoal(Predicate<ItemStack> items) {
            this.items = items;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.temptingPlayer = SulfurCubeEntity.this.level().getNearestPlayer(SulfurCubeEntity.this.getX(), SulfurCubeEntity.this.getY(), SulfurCubeEntity.this.getZ(), 10.0D, this::isTemptingItem);
            return this.temptingPlayer != null;
        }

        @Override
        public void tick() {
            if (this.temptingPlayer != null) {
                SulfurCubeEntity.this.lookAt(this.temptingPlayer, 10.0F, 10.0F);
            }
             MoveControl var2 = SulfurCubeEntity.this.getMoveControl();
            if (var2 instanceof SulfurCubeMoveControl cubeMoveControl) {
                cubeMoveControl.setDirection(SulfurCubeEntity.this.getYRot(), true);
            }

        }

        private boolean isTemptingItem(Entity entity) {
            return entity instanceof Player player && (this.items.test(player.getMainHandItem()) || this.items.test(player.getOffhandItem()));
        }

    }

    class SulfurCubeSearchForItemsGoal extends Goal {
        @Nullable ItemEntity targetItem;

        public SulfurCubeSearchForItemsGoal() {
            super();
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            if (!SulfurCubeEntity.this.isTiny() && SulfurCubeEntity.this.pickupTimer <= 0) {
                List<ItemEntity> list = SulfurCubeEntity.this.level().getEntitiesOfClass(ItemEntity.class, SulfurCubeEntity.this.getBoundingBox().inflate(8.0F, 8.0F, 8.0F), ALLOWED_ITEMS);
                this.targetItem = list.isEmpty() ? null : list.get(0);
                return this.targetItem != null;
            } else {
                return false;
            }
        }

        public void tick() {
            if (this.targetItem != null) {
                SulfurCubeEntity.this.lookAt(this.targetItem, 10.0F, 10.0F);
            }
            MoveControl var2 = SulfurCubeEntity.this.getMoveControl();
            if (var2 instanceof SulfurCubeMoveControl cubeMoveControl) {
                cubeMoveControl.setDirection(SulfurCubeEntity.this.getYRot(), true);
            }

        }
    }

}
