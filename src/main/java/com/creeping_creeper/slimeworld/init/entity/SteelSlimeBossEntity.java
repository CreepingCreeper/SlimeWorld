package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.init.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.entity.ArmoredSlimeEntity;
import slimeknights.tconstruct.world.entity.SkySlimeEntity;

import javax.annotation.Nullable;
import java.util.List;

public class SteelSlimeBossEntity extends BossSlimeEntity {
    private boolean isImmune;

    public SteelSlimeBossEntity(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide) {
            tryAddAttribute(Attributes.ARMOR, new AttributeModifier("slimeworld.small_armor_bonus", 4, AttributeModifier.Operation.ADDITION));
            tryAddAttribute(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("slimeworld.small_toughness_bonus", 3, AttributeModifier.Operation.ADDITION));
          }
        this.isImmune = false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        // 满足条件：有目标+地面+无冷却+无眩晕+不在冲刺
        if (getTarget() != null && onGround() && !isCooling() && isImmobile() && !isImmune) {
            this.setStunnedTick(40);
            startImmune();
            addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, false, false));
        }


        // 冲刺计时衰减
        if (isImmune && skillTick > 0) {
            skillTick--;
            if (skillTick <= 0) tryEndImmune();
        }
    }

    @Override
    public void push(@NotNull Entity entity) {
        super.push(entity);
        if (entity instanceof LivingEntity living) {
            strongKnockback(living);
        }
    }

    private void startImmune() {
        this.isImmune = true;
        this.skillTick = 40;
        List<SkySlimeEntity> list = level().getEntitiesOfClass(SkySlimeEntity.class, this.getBoundingBox().inflate(32, 3, 32));
        for (SkySlimeEntity slime : list){
            slime.addEffect(new MobEffectInstance(MobEffects.GLOWING, -1));
        }
    }

    private void tryEndImmune(){
        List<SkySlimeEntity> list = level().getEntitiesOfClass(SkySlimeEntity.class, this.getBoundingBox().inflate(32, 3, 32));
        if (list.isEmpty()){
            isImmune = false;
            this.skillTick = 0;
            setCooling(400 + random.nextInt(10) * 20);
            this.setStunnedTick(40);
        }else  this.skillTick = 20;
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return this.isImmune || super.isInvulnerableTo(source);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    protected @NotNull ParticleOptions getParticleType() {
        return ModParticles.SteelSlimeParticle.get();
    }

    @Override
    protected MaterialId getPlating() {
        return MaterialIds.slimesteel;
    }

    @Override
    protected EntityType<? extends ArmoredSlimeEntity> getSummonedEntity() {
        return TinkerWorld.skySlimeEntity.get();
    }

    @Override
    protected MaterialId getSummonedPlating() {
        return MaterialIds.steel;
    }

    @Override
    protected ResourceKey<TrimPattern> getTrimPattern() {
        return TrimPatterns.SENTRY;
    }
}