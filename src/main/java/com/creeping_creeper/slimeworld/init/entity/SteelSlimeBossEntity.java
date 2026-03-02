package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.init.ModEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.utils.SlimeBounceHandler;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.entity.ArmoredSlimeEntity;

public class SteelSlimeBossEntity extends BossSlimeEntity {
    public SteelSlimeBossEntity(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide) {
            tryAddAttribute(Attributes.ARMOR, new AttributeModifier("tconstruct.small_armor_bonus", 4, AttributeModifier.Operation.ADDITION));
            tryAddAttribute(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("tconstruct.small_toughness_bonus", 3, AttributeModifier.Operation.ADDITION));
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.getTarget() != null && this.onGround() && !isCooling()) {
            int force = 8 / this.getSize();
            this.lookAt(this.getTarget(), 10.0F, 10.0F);
            Vec3 look = this.getLookAngle();
            this.push(force * look.x, 0.02,force * look.z);
            SlimeBounceHandler.addBounceHandler(this);
            this.setCooling(200);
            this.setNoGravity(true);
            this.addEffect(new MobEffectInstance(MobEffects.GLOWING, 200));
        }else super.travel(travelVector);
    }

    @Override
    public void setOnGroundWithKnownMovement(boolean onGround, Vec3 movement) {
        if (this.isNoGravity() && onGround){
            this.setNoGravity(false);
        }
        super.setOnGroundWithKnownMovement(onGround, movement);
    }

    @Override
    protected ParticleOptions getParticleType() {
        return ModEntities.steelSlimeParticle.get();
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
}
