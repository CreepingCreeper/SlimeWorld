package com.creeping_creeper.slimeworld.init.entity.boss;

import com.creeping_creeper.slimeworld.events.SlimeBossTeleportEvent;
import com.creeping_creeper.slimeworld.init.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.utils.TeleportHelper;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.entity.ArmoredSlimeEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class KnightSlimeBossEntity extends BaseBossSlimeEntity {
    private final TeleportHelper.ITeleportEventFactory teleportPredicate = (entity, x, y, z) -> new SlimeBossTeleportEvent(entity, x, y, z, this);
    private Vec3 bounce = Vec3.ZERO;
    private Vec3 chargeForwardDir;
    private boolean isChargingSprint;

    public KnightSlimeBossEntity(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
        this.isChargingSprint = false;
        this.chargeForwardDir = Vec3.ZERO;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        if (getTarget() != null && onGround() && !isCooling() && !canNotMove()) {
            TeleportHelper.randomNearbyTeleport(this, teleportPredicate);
            lookAt(getTarget(), 180F, 360F);
            this.chargeForwardDir = getLookAngle();
            this.setStunnedTick(this.getSize() / 2 * 5 + 10);
            setCooling(100 + random.nextInt(10) * 20);
        }

        if (!canNotMove() && !chargeForwardDir.equals(Vec3.ZERO)) {
            startSprint();
        }

        if (isChargingSprint && skillTick > 0) {
            skillTick--;
            if (skillTick <= 0) endSprint();
        }
    }

    @Override
    public void move(@NotNull MoverType type, @NotNull Vec3 pos) {
        super.move(type, pos);
        if (!isChargingSprint) return;

        double x = bounce.x, y = bounce.y, z = bounce.z;
        boolean shouldBounce = false;
        boolean hasCollision = false;
        if (horizontalCollision) {
            hasCollision = true;
            if (Math.abs(bounce.x) > 1.0E-7) {
                x *= -0.6;
                shouldBounce = true;
            }
            if (Math.abs(bounce.z) > 1.0E-7) {
                z *= -0.6;
                shouldBounce = true;
            }
        }
        if (this.verticalCollision && Math.abs(bounce.y) > 1.0E-7) {
            y *= -0.6;
            shouldBounce = true;
        }
        if (shouldBounce) {
            setDeltaMovement(new Vec3(x, y, z));
        } else if (hasCollision) {
            this.skillTick = 0;
            endSprint();
        }
        bounce = getDeltaMovement();
    }

    @Override
    protected void pushLiving(@NotNull LivingEntity living) {
        super.pushLiving(living);
        if (isChargingSprint) {
            strongKnockback(living);
        }
    }

    @Override
    public void doEnchantDamageEffects(@NotNull LivingEntity slime, @NotNull Entity target) {
        super.doEnchantDamageEffects(slime, target);
        if (isChargingSprint() && target instanceof LivingEntity living) {
            tryDropRandomItem(living);
            endSprint();
        }
    }

    @Override
    protected void actuallyHurt(@NotNull DamageSource damageSrc, float damageAmount) {
        float oldHealth = getHealth();
        super.actuallyHurt(damageSrc, damageAmount);
        if (isAlive() && getHealth() < oldHealth) {
            TeleportHelper.randomNearbyTeleport(this, teleportPredicate);
        }
    }

    @Override
    public boolean canDisableShield() {
        return isChargingSprint();
    }

    @Override
    protected boolean canNotMove(){
        return isChargingSprint || super.canNotMove();
    }

    private void startSprint() {
        this.isChargingSprint = true;
        this.skillTick = 20;
        int force = 8 - this.getSize() / 2;
        push(chargeForwardDir.x * force, 0.2D, chargeForwardDir.z * force);
    }

    private void endSprint(){
        isChargingSprint = false;
        chargeForwardDir = Vec3.ZERO;
    }

    private void tryDropRandomItem(LivingEntity living) {
        List<ItemStack> dropCandidates = new ArrayList<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = living.getItemBySlot(slot);
            if (!stack.isEmpty()) {
                dropCandidates.add(stack);
            }
        }

        if (dropCandidates.isEmpty()) return;
        ItemStack dropStack = dropCandidates.get(random.nextInt(dropCandidates.size()));
        ItemStack drop = dropStack.copyAndClear();
        ItemEntity item = new ItemEntity(living.level(), living.getX(), living.getY(), living.getZ(), drop);
        item.setPickUpDelay(100);
        living.level().addFreshEntity(item);
    }

    public boolean isChargingSprint() {
        return this.isAlive() && isChargingSprint;
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    protected @NotNull ParticleOptions getParticleType() {
        return ModParticles.KnightSlimeParticle.get();
    }

    @Override
    protected MaterialId getPlating() {
        return MaterialIds.knightslime;
    }

    @Override
    protected EntityType<? extends ArmoredSlimeEntity> getSummonedEntity() {
        return TinkerWorld.enderSlimeEntity.get();
    }

    @Override
    protected MaterialId getSummonedPlating() {
        return MaterialIds.knightmetal;
    }

    @Override
    protected MaterialId getTrimMaterial() {
        return MaterialIds.iron;
    }

    @Override
    protected ResourceKey<TrimPattern> getTrimPattern() {
        return TrimPatterns.SPIRE;
    }
}