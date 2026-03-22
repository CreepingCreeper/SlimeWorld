package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;

public class FloatingWindEntity extends Entity {
    private int age = 0;

    public FloatingWindEntity(EntityType<? extends Entity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return this.isRemoved() || !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }


    @Override
    public void tick() {
        super.tick();
        this.age++;
        int maxAge = 160;
        if (this.age >= maxAge) {
            this.discard();
        }
        if (!level().isClientSide) {
            double motionX = random.nextDouble() - 0.5;
            double motionY = (random.nextDouble() - 0.8) * 0.1;
            double motionZ = random.nextDouble() - 0.5;
            this.setDeltaMovement(motionX, motionY, motionZ);
            this.move(MoverType.SELF, this.getDeltaMovement());

            AABB box = getBoundingBox();
            if (level().getEntitiesOfClass(LivingEntity.class, box.inflate(0.15D)).isEmpty()) {
                this.discard();
            }
            if (!level().getBlockState(BlockPos.containing(this.getX(), this.getY(), this.getZ())).isAir()){
                this.age+=19;
            }
        }else level().addParticle(ModEntities.windParticle.get(), this.getX(), this.getY(), this.getZ(), 0, 0 ,0);
    }

    @Override
    protected void defineSynchedData() {}
    @Override
    protected void readAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}
    @Override
    protected void addAdditionalSaveData(net.minecraft.nbt.CompoundTag tag) {}

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }
}