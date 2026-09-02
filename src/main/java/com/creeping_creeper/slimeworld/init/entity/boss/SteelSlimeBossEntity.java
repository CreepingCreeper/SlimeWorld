package com.creeping_creeper.slimeworld.init.entity.boss;

import com.creeping_creeper.slimeworld.init.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.armortrim.TrimPatterns;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.entity.ArmoredSlimeEntity;
import slimeknights.tconstruct.world.entity.SkySlimeEntity;

import java.util.List;

public class SteelSlimeBossEntity extends BaseBossSlimeEntity {
    private double bounceAmount = 0f;
    private boolean isImmune;

    public SteelSlimeBossEntity(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide) {
            tryAddAttribute(Attributes.ARMOR, new AttributeModifier("slimeworld.steel_slime_barmor_bonus", 4, AttributeModifier.Operation.ADDITION));
            tryAddAttribute(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("slimeworld.steel_slime_toughness_bonus", 3, AttributeModifier.Operation.ADDITION));
          }
        this.isImmune = false;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) return;

        if (getTarget() != null && onGround() && !isCooling() && isImmobile() && !isImmune) {
            this.setStunnedTick(40);
            startImmune();
            addEffect(new MobEffectInstance(MobEffects.GLOWING, 200, 0, false, false));
        }


        if (isImmune && skillTick > 0) {
            skillTick--;
            if (skillTick <= 0) tryEndImmune();
        }
    }

    private void startImmune() {
        this.isImmune = true;
        this.skillTick = 40;
        List<SkySlimeEntity> list = level().getEntitiesOfClass(SkySlimeEntity.class, this.getBoundingBox().inflate(64, 32, 64));
        for (SkySlimeEntity slime : list){
            slime.addEffect(new MobEffectInstance(MobEffects.GLOWING, -1));
        }
    }

    private void tryEndImmune(){
        List<SkySlimeEntity> list = level().getEntitiesOfClass(SkySlimeEntity.class, this.getBoundingBox().inflate(64, 32, 64));
        if (list.isEmpty()){
            isImmune = false;
            this.skillTick = 0;
            setCooling(400 + random.nextInt(10) * 20);
            this.setStunnedTick(40);
        }else this.skillTick = 20;
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        return this.isImmune || super.isInvulnerableTo(source);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, @NotNull DamageSource source) {
        if (isSuppressingBounce()) {
            return super.causeFallDamage(distance, damageMultiplier * 0.2f, source);
        }
        float[] ret = ForgeHooks.onLivingFall(this, distance, damageMultiplier);
        if (ret == null) {
            return false;
        }
        distance = ret[0];
        if (distance > 2) {
            // invert Y motion, boost X and Z slightly
            Vec3 motion = getDeltaMovement();
            setDeltaMovement(motion.x / 0.95f, motion.y * -0.9, motion.z / 0.95f);
            bounceAmount = getDeltaMovement().y;
            fallDistance = 0f;
            hasImpulse = true;
            setOnGround(false);
            playSound(Sounds.SLIMY_BOUNCE.getSound(), 1f, 1f);
        }
        return false;
    }

    @Override
    public void move(@NotNull MoverType typeIn, @NotNull Vec3 pos) {
        super.move(typeIn, pos);
        if (bounceAmount > 0) {
            Vec3 motion = getDeltaMovement();
            setDeltaMovement(motion.x, bounceAmount, motion.z);
            bounceAmount = 0;
        }
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
    protected MaterialId getTrimMaterial() {
        return MaterialIds.iron;
    }

    @Override
    protected ResourceKey<TrimPattern> getTrimPattern() {
        return TrimPatterns.SENTRY;
    }

}