package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IForgeShearable;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.FoliageType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class PlantLikeMob extends AgeableMob implements IForgeShearable {
    private final PanicGoal panicGoal = new PanicGoal(this, 1.2D);
    private final EastWestPathMoveGoal eastGoal = new EastWestPathMoveGoal(this, 1D, true);
    private final EastWestPathMoveGoal westGoal = new EastWestPathMoveGoal(this, 1D, false);
    private static final EntityDataAccessor<String> FOLIAGE_TYPE = SynchedEntityData.defineId(PlantLikeMob.class, EntityDataSerializers.STRING);
    private boolean wasDay = true;
    private int moveTime = 0;

    public PlantLikeMob(EntityType<? extends AgeableMob> type, Level level) {
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
                .add(Attributes.MAX_HEALTH, 5.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
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
                this.goalSelector.removeGoal(panicGoal);
                this.goalSelector.removeGoal(eastGoal);
                this.goalSelector.removeGoal(westGoal);
            }
            moveTime--;
        }else tickDayNightMove();
    }

    @Override
    public void setDeltaMovement(@NotNull Vec3 vec3) {
        super.setDeltaMovement(this.moveTime >= 0 ? vec3 : new Vec3(0, vec3.y, 0));
    }

    private void tickDayNightMove() {
        boolean nowDay = level().isDay();

        if (nowDay != wasDay) {
            if (nowDay){
                this.goalSelector.addGoal(2, eastGoal);
            }else this.goalSelector.addGoal(3, westGoal);
            moveTime = 120;
        }

        wasDay = nowDay;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        this.goalSelector.addGoal(1, panicGoal);
        moveTime = 60;
        return super.hurt(source, amount);
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


    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor level, @Nonnull DifficultyInstance p_30775_, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData p_30777_, @Nullable CompoundTag p_30778_) {
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
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel p_146743_, @NotNull AgeableMob p_146744_) {
        return null;
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("wasDay", wasDay);
        compoundTag.putInt("moveTime", moveTime);
        compoundTag.putString("foliageType", this.entityData.get(FOLIAGE_TYPE));
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.wasDay = compoundTag.getBoolean("wasDay");
        this.moveTime = compoundTag.getInt("moveTime");
        if (compoundTag.contains("foliageType")) {
            this.entityData.set(FOLIAGE_TYPE, compoundTag.getString("foliageType"));
        }
    }

    public Block getViewBlock(){
        String raw = this.entityData.get(FOLIAGE_TYPE);
        if (raw.isBlank()){
            return TinkerWorld.slimeTallGrass.get(FoliageType.EARTH);
        }
        return TinkerWorld.slimeTallGrass.get(FoliageType.valueOf(raw));
    }

    public boolean isShearable(@NotNull ItemStack item, Level level, BlockPos pos) {
        return !isBaby();
    }

    public @NotNull List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level level, BlockPos pos, int fortune) {
        level.playSound(null, this, ModSounds.BOGGED_SHEAR.get(), player == null ? SoundSource.BLOCKS : SoundSource.PLAYERS, 1.0F, 1.0F);
        this.gameEvent(GameEvent.SHEAR, player);
        if (!level.isClientSide()) {
            this.setBaby(true);
            List<ItemStack> items = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                Item item1 = this.random.nextBoolean() ? getViewBlock().asItem() : Items.STRING;
                items.add(item1.getDefaultInstance());
            }
            return items;
        }
        return Collections.emptyList();
    }

    public static class EastWestPathMoveGoal extends Goal {
        private final PathfinderMob mob;
        private final PathNavigation navigation;
        private final double speedModifier;
        public boolean goEast;

        private Path currentPath;
        private int recalcPathCooldown;

        public EastWestPathMoveGoal(PathfinderMob mob, double speedModifier, boolean goEast) {
            this.mob = mob;
            this.navigation = mob.getNavigation();
            this.speedModifier = speedModifier;
            this.goEast = goEast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (mob.isNoAi()) return false;
            if (recalcPathCooldown > 0) {
                recalcPathCooldown--;
                return false;
            }
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return !navigation.isStuck() && !mob.isNoAi() && currentPath != null && !currentPath.isDone();
        }

        @Override
        public void start() {
            recalculatePath();
        }

        @Override
        public void tick() {
            if (currentPath == null || currentPath.isDone()) {
                recalculatePath();
            }
        }

        private void recalculatePath() {
            recalcPathCooldown = 20;

            int range = 16;
            int dx = goEast ? range : -range;
            int targetX = mob.getBlockX() + dx;
            int targetY = mob.getBlockY();
            int targetZ = mob.getBlockZ();

            currentPath = navigation.createPath(targetX, targetY, targetZ, 0);
            if (currentPath != null && !currentPath.isDone()) {
                navigation.moveTo(currentPath, speedModifier);
                Direction dir = goEast ? Direction.EAST : Direction.WEST;
                mob.setYRot(dir.toYRot());
                mob.yBodyRot = mob.getYRot();
            }
        }

        @Override
        public void stop() {
            navigation.stop();
            currentPath = null;
        }
    }
}