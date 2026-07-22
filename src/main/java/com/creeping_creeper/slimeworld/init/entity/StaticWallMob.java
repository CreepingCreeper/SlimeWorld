package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.data.key.ModTags;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.FoliageType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StaticWallMob extends Mob {
    private static final Direction[] horizontalDirs = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private static final EntityDataAccessor<String> FOLIAGE_TYPE = SynchedEntityData.defineId(StaticWallMob.class, EntityDataSerializers.STRING);
    private boolean wasDay = true;
    private int moveTime = 0;

    public StaticWallMob(EntityType<? extends Mob> type, Level level) {
        super(type, level);
        this.setYRot(Direction.EAST.toYRot());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FOLIAGE_TYPE, FoliageType.EARTH.toString());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 10.0D)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide()) return;

        if (moveTime >= 0) {
            if (moveTime == 1){
                Vec3 center = this.blockPosition().getCenter();
                this.setPos(center.x, this.getY(), center.z);
                this.setDeltaMovement(Vec3.ZERO);
            }
            moveTime--;
        }

        tickDayNightMove();
    }

    @Override
    public void setDeltaMovement(@NotNull Vec3 vec3) {
        super.setDeltaMovement(this.moveTime >= 0 ? vec3 : new Vec3(0, vec3.y, 0));
    }

    private void tickDayNightMove() {
        boolean nowDay = level().isDay();

        if (nowDay != wasDay) {
            moveFixedDirection(Direction.EAST);
        }

        wasDay = nowDay;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        Direction direction = horizontalDirs[this.random.nextInt(4)];
        moveFixedDirection(direction);

        return super.hurt(source, amount);
    }

    protected void moveFixedDirection(Direction direction) {
        if (level().isClientSide()) return;
        moveTime = 60;
        Vec3 moveVec = Vec3.atLowerCornerOf(direction.getNormal());
        this.setDeltaMovement(moveVec);
    }

    @Override
    public boolean isPushable() {
        return false;
    }


    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    protected void jumpFromGround() {
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor level, @Nonnull DifficultyInstance p_30775_, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData p_30777_, @Nullable CompoundTag p_30778_) {
        SpawnGroupData spawnGroupData = super.finalizeSpawn(level, p_30775_, spawnType, p_30777_, p_30778_);
        this.setYRot(Direction.EAST.toYRot());
        Holder<Biome> holder = level.getBiome(this.blockPosition());
        if (holder.is(ModTags.Biomes.SKY_VARIANT_GRASS)) {
            this.entityData.set(FOLIAGE_TYPE, FoliageType.SKY.toString());
        } else if (holder.is(ModTags.Biomes.BLOOD_VARIANT_GRASS)) {
            this.entityData.set(FOLIAGE_TYPE, FoliageType.BLOOD.toString());
        } else if (holder.is(ModTags.Biomes.ENDER_VARIANT_GRASS)) {
            this.entityData.set(FOLIAGE_TYPE, FoliageType.ENDER.toString());
        }
        this.wasDay = level().isDay();
        return spawnGroupData;
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("WasDay", wasDay);
        compoundTag.putInt("MoveTime", moveTime);
        compoundTag.putString("FoliageType", this.entityData.get(FOLIAGE_TYPE));
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.wasDay = compoundTag.getBoolean("WasDay");
        this.moveTime = compoundTag.getInt("MoveTime");
        this.entityData.set(FOLIAGE_TYPE, compoundTag.getString("FoliageType"));
    }

    public BlockState getViewBlock(){
        return TinkerWorld.slimeTallGrass.get(FoliageType.valueOf(this.entityData.get(FOLIAGE_TYPE))).defaultBlockState();
    }
}