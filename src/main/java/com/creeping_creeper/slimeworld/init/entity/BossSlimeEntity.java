package com.creeping_creeper.slimeworld.init.entity;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.material.ToolMaterialHook;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.world.entity.ArmoredSlimeEntity;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BossSlimeEntity extends Slime {
    private static final EntityDataAccessor<Integer> COOLING = SynchedEntityData.defineId(BossSlimeEntity.class, EntityDataSerializers.INT);

    public BossSlimeEntity(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
        if (!level.isClientSide) {
            tryAddAttribute(Attributes.ARMOR, new AttributeModifier("tconstruct.small_armor_bonus", 3, AttributeModifier.Operation.MULTIPLY_TOTAL));
            tryAddAttribute(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("tconstruct.small_toughness_bonus", 3, AttributeModifier.Operation.MULTIPLY_TOTAL));
            tryAddAttribute(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier("tconstruct.small_resistence_bonus", 1, AttributeModifier.Operation.ADDITION));
        }
    }

    protected void tryAddAttribute(Attribute attribute, AttributeModifier modifier) {
        AttributeInstance instance = getAttribute(attribute);
        if (instance != null) {
            instance.addTransientModifier(modifier);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COOLING, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes();
    }

    protected void setCooling(int cooling) {
       this.entityData.set(COOLING, cooling);
    }

    protected int getCooling() {
       return this.entityData.get(COOLING);
    }

    protected boolean isCooling() {
        return this.getCooling() > 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && isCooling()){
            setCooling(this.getCooling() - 1);
        }
    }

    @Override
    @VisibleForTesting
    public void setSize(int size, boolean resetHealth) {
        super.setSize(size, false);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(200);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.4F + 0.1F * (float)size);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(32/size);
        if (resetHealth) {
            this.setHealth(this.getMaxHealth());
        }
    }

    private ToolStack tool(MaterialId id){
        IModifiable helmetItem = TinkerTools.plateArmor.get(ArmorItem.Type.HELMET);
        ToolDefinition definition = helmetItem.getToolDefinition();
        return ToolStack.createTool(
                helmetItem.asItem(), definition,
                RandomMaterial.build(ToolMaterialHook.stats(definition), List.of(RandomMaterial.fixed(id), RandomMaterial.fixed(id)), random));
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData spawnData = super.finalizeSpawn(level, difficulty, reason, pSpawnData, dataTag);
        this.setSize(8, true);
        this.setItemSlot(EquipmentSlot.HEAD, tool(getPlating()).createStack());
        this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
        return spawnData;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        super.actuallyHurt(damageSource, damageAmount);
        Level level = level();
        int count = this.random.nextInt(4 - this.getSize() / 4);
        float offset = this.getSize() / 4.0F;
        for(int i = 0; i < count; ++i) {
            float x = ((i % 2) - 0.5F) * offset;
            float z = ((i / 2) - 0.5F) * offset;
            ArmoredSlimeEntity slime = getSummonedEntity().create(level);
            assert slime != null;
            if (this.isPersistenceRequired()) {
                slime.setPersistenceRequired();
            }
            slime.setSize(this.random.nextInt(2) * 2 +2, true);
            slime.setItemSlot(EquipmentSlot.HEAD, tool(getSummonedPlating()).createStack());
            slime.moveTo(this.getX() + x, this.getY() + 0.5D, this.getZ() + z, this.random.nextFloat() * 360.0F, 0.0F);
            slime.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
            level.addFreshEntity(slime);
        }
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        MobEffect effect = effectInstance.getEffect();
        return effect.isBeneficial() || effect == MobEffects.GLOWING && super.addEffect(effectInstance, entity);
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        this.setRemoved(reason);
        if (reason == Entity.RemovalReason.KILLED) {
            this.gameEvent(GameEvent.ENTITY_DIE);
        }
        this.invalidateCaps();
    }

    protected abstract MaterialId getPlating();
    protected abstract EntityType<? extends ArmoredSlimeEntity> getSummonedEntity();
    protected abstract MaterialId getSummonedPlating();

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("cooling", this.getCooling());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setSize(compound.getInt("Size") + 1, false);
        super.readAdditionalSaveData(compound);
        this.setCooling(compound.getInt("cooling"));
    }
}
