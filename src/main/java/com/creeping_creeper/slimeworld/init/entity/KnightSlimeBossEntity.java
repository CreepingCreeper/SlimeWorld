package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.events.SlimeBossTeleportEvent;
import com.creeping_creeper.slimeworld.init.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ItemStack;
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

public class KnightSlimeBossEntity extends BossSlimeEntity {
    private final TeleportHelper.ITeleportEventFactory teleportPredicate = (entity, x, y, z) -> new SlimeBossTeleportEvent(entity, x, y, z, this);
    private Vec3 bounce = Vec3.ZERO;
    private int sprintTick;
    // 蓄力锁定的冲刺朝向
    private Vec3 chargeForwardDir;
    // 冲刺是否正在进行标记
    private boolean isChargingSprint;

    public KnightSlimeBossEntity(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide) {
            tryAddAttribute(Attributes.MOVEMENT_SPEED, new AttributeModifier("slimeworld.speed_bonus", 0.1, AttributeModifier.Operation.ADDITION));
        }
        this.isChargingSprint = false;
        this.chargeForwardDir = Vec3.ZERO;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        // 满足条件：有目标+地面+无冷却+无眩晕+不在冲刺
        if (getTarget() != null && onGround() && !isCooling() && !isImmobile() && !isChargingSprint) {
            TeleportHelper.randomNearbyTeleport(this, teleportPredicate);
            // 1. 朝向玩家
            lookAt(getTarget(), 180F, 180F);
            // 2. 锁定当前前方朝向，冲刺全程不再更改
            this.chargeForwardDir = getLookAngle();
            // 3. 眩晕1秒 = 20tick 蓄力
            this.setStunnedTick(30);
            addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, false, false));
            setCooling(100 + random.nextInt(10) * 20);
        }

        // 眩晕结束 触发直线冲刺
        if (!isImmobile() && !chargeForwardDir.equals(Vec3.ZERO) && !isChargingSprint) {
            startSprint();
        }

        // 冲刺计时衰减
        if (isChargingSprint && sprintTick > 0) {
            sprintTick--;
            if (sprintTick <= 0) endSprint();
        }
    }

    // 冲刺移动+撞墙反弹保留
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
        }

        if (shouldBounce) {
            sprintTick = 40;
            setDeltaMovement(new Vec3(x, y, z));
        } else if (hasCollision) {
            endSprint();
        }
        bounce = getDeltaMovement();
    }

    @Override
    public void push(@NotNull Entity entity) {
        super.push(entity);
        if (entity instanceof LivingEntity living) {
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

    // 冲刺可破盾
    @Override
    public boolean canDisableShield() {
        return isChargingSprint();
    }

    @Override
    protected boolean isImmobile() {
        return isChargingSprint || super.isImmobile();
    }

    private void startSprint() {
        this.isChargingSprint = true;
        this.sprintTick = 40;
        int force = 36 / this.getSize();
        push(chargeForwardDir.x * force, 0.2D, chargeForwardDir.z * force);
    }

    private void endSprint(){
        isChargingSprint = false;
        this.sprintTick = 0;
        chargeForwardDir = Vec3.ZERO;
    }

    /**
     * 玩家随机丢弃一件 盔甲/主手/副手 物品
     */
    private void tryDropRandomItem(LivingEntity living) {
        List<ItemStack> dropCandidates = new ArrayList<>();
        // 收集全身盔甲
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = living.getItemBySlot(slot);
            if (!stack.isEmpty()) {
                dropCandidates.add(stack);
            }
        }
        // 收集手持物品

        if (dropCandidates.isEmpty()) return;
        // 随机选一个丢弃
        ItemStack dropStack = dropCandidates.get(random.nextInt(dropCandidates.size()));
        ItemStack drop = dropStack.split(1);
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
}