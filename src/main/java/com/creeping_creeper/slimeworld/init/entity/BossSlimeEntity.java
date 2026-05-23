package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.data.ModModifierIds;
import com.google.common.annotations.VisibleForTesting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.materials.RandomMaterial;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.material.ToolMaterialHook;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.TinkerTools;
import slimeknights.tconstruct.tools.modules.cosmetic.TrimModule;
import slimeknights.tconstruct.world.entity.ArmoredSlimeEntity;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BossSlimeEntity extends Slime {
    private static final EntityDataAccessor<Integer> COOLING = SynchedEntityData.defineId(BossSlimeEntity.class, EntityDataSerializers.INT);
    private int stunnedTick;
    protected int skillTick;
    private final ServerBossEvent bossEvent;

    public BossSlimeEntity(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
        this.bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
        if (!level.isClientSide) {
            tryAddAttribute(Attributes.ARMOR, new AttributeModifier("tconstruct.small_armor_bonus", 3, AttributeModifier.Operation.MULTIPLY_TOTAL));
            tryAddAttribute(Attributes.ARMOR_TOUGHNESS, new AttributeModifier("tconstruct.small_toughness_bonus", 3, AttributeModifier.Operation.MULTIPLY_TOTAL));
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
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 48.0F).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
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
        if (!this.level().isClientSide){
            if (isCooling()) setCooling(this.getCooling() - 1);
            if (this.stunnedTick > 0) if (--this.stunnedTick == 0) {
                this.goalSelector.enableControlFlag(Goal.Flag.MOVE);
            }
        }
    }
//
//    @Override
//    public void travel(@NotNull Vec3 travelVector) {
//        if (isImmobile()) {
//            // 蓄力眩晕完全禁移；冲刺阶段只走锁定方向，禁止玩家/AI操控转向移动
//            if (stunnedTick > 1) {
//                this.goalSelector.disableControlFlag(Goal.Flag.LOOK);
//                setDeltaMovement(getDeltaMovement().multiply(0, 1, 0));
//                return;
//            }
//        }
//        super.travel(travelVector);
//    }

    protected void setStunnedTick(int tick){
        this.stunnedTick = tick;
        this.goalSelector.disableControlFlag(Goal.Flag.MOVE);
    }

    @Override
    public float getJumpPower() {
        return this.canNotMove() ? 0.0F : super.getJumpPower();
    }

    @Override
    protected boolean isImmobile() {
        return this.stunnedTick > 0 || super.isImmobile();
    }

    public boolean hasLineOfSight(@NotNull Entity entity) {
        return !isImmobile() && super.hasLineOfSight(entity) ;
    }
    
    protected boolean canNotMove(){
        return this.isImmobile();
    }

    @Override
    @VisibleForTesting
    public void setSize(int size, boolean resetHealth) {
        super.setSize(size, false);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(200);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1.4F - 0.1F * (float)size);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(32.0F / size);
        if (resetHealth) {
            this.setHealth(this.getMaxHealth());
        }
        this.resetBarColor();
    }

    private ToolStack tool(MaterialId id){
        IModifiable helmetItem = TinkerTools.plateArmor.get(ArmorItem.Type.HELMET);
        ToolDefinition definition = helmetItem.getToolDefinition();
        ToolStack tool = ToolStack.createTool(
                helmetItem.asItem(), definition,
                RandomMaterial.build(ToolMaterialHook.stats(definition), List.of(RandomMaterial.fixed(id), RandomMaterial.fixed(id)), random));
        tool.addModifier(ModModifierIds.vanishingCurse, 1);
        tool.addModifier(ModModifierIds.slimeBalance, 1);
        ModDataNBT persistentData = tool.getPersistentData();
        ModifierId trimId = TinkerModifiers.trim.getId();
        persistentData.putString(TrimModule.materialKey(trimId), getTrimMaterial().toString());
        persistentData.putString(TrimModule.patternKey(trimId), getTrimPattern().location().toString());
        tool.addModifier(TinkerModifiers.trim.getId(), 1);
        if (!getModifier().isEmpty()){
            int t = 0;
            for (Modifier modifier : getModifier()){
                tool.addModifier(modifier.getId(), getModifierLevel().get(t));
                t++;
            }
        }
        return tool;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData spawnData = super.finalizeSpawn(level, difficulty, reason, pSpawnData, dataTag);
        this.setSize(8, true);
        this.setItemSlot(EquipmentSlot.HEAD, tool(getPlating()).createStack());
        return spawnData;
    }

    protected void pushLiving(@NotNull LivingEntity living){
        if (!living.getType().is(TinkerTags.EntityTypes.SLIMES) && this.isDealsDamage()){
            this.dealDamage(living);
        }
    }

    @Override
    public void push(@NotNull Entity entity) {
        super.push(entity);
        if (entity instanceof LivingEntity living){
            this.pushLiving(living);
        }
    }

    @Override
    protected void actuallyHurt(@NotNull DamageSource damageSource, float damageAmount) {
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
            level.addFreshEntity(slime);
        }
    }

    @Override
    public boolean addEffect(MobEffectInstance effectInstance, @Nullable Entity entity) {
        MobEffect effect = effectInstance.getEffect();
        return effect.isBeneficial() || effect == MobEffects.GLOWING && super.addEffect(effectInstance, entity);
    }

    @Override
    public void remove(Entity.@NotNull RemovalReason reason) {
        this.setRemoved(reason);
        if (reason == Entity.RemovalReason.KILLED) {
            this.gameEvent(GameEvent.ENTITY_DIE);
        }
        this.invalidateCaps();
    }

    protected abstract MaterialId getPlating();
    protected abstract EntityType<? extends ArmoredSlimeEntity> getSummonedEntity();
    protected abstract MaterialId getSummonedPlating();
    protected abstract MaterialId getTrimMaterial();
    protected List<Modifier> getModifier(){
        return List.of();
    }
    protected List<Integer> getModifierLevel(){
        return List.of();
    }
    protected abstract ResourceKey<TrimPattern> getTrimPattern();

    protected void strongKnockback(Entity entity) {
        double d0 = entity.getX() - this.getX();
        double d1 = entity.getZ() - this.getZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
        entity.push(d0 / d2 * 4.0D, 0.4D, d1 / d2 * 4.0D);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("cooling", this.getCooling());
        compound.putInt("stunTick", this.stunnedTick);
        compound.putInt("skillTick", this.skillTick);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setSize(compound.getInt("Size") + 1, false);
        super.readAdditionalSaveData(compound);
        this.setCooling(compound.getInt("cooling"));
        this.setStunnedTick(compound.getInt("stunnedTick"));
        this.skillTick = (compound.getInt("skillTick"));
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    protected void customServerAiStep() {
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    private void resetBarColor(){
        BossEvent.BossBarColor color =  switch (getSize() / 2){
            case 4 -> BossEvent.BossBarColor.RED;
            case 2 -> BossEvent.BossBarColor.YELLOW;
            default -> BossEvent.BossBarColor.BLUE;
        };
        this.bossEvent.setColor(color);
    }
}
