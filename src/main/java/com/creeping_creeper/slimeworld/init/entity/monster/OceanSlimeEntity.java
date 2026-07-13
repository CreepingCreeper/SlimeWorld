package com.creeping_creeper.slimeworld.init.entity.monster;

import com.creeping_creeper.slimeworld.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.world.entity.TravelersPlateSlimeEntity;

import javax.annotation.Nullable;

public class OceanSlimeEntity extends TravelersPlateSlimeEntity {
    public OceanSlimeEntity(EntityType<? extends OceanSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    public static boolean canSpawnHere(EntityType<? extends Slime> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (reason == MobSpawnType.SPAWNER) {
            return true;
        }
        return world.getFluidState(pos).is(FluidTags.WATER) && pos.getY() > world.getSeaLevel() - 6 && random.nextInt(200) == 0;
    }

    public @NotNull MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new AmphibiousPathNavigation(this, level);
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        LivingEntity target = this.getTarget();
        if (this.isEffectiveAi() && target != null) {
            double length = 1D + this.getSize();
            boolean shouldDive = target.isInWaterOrBubble()
                    && target.getEyeY() < this.getY()
                    && Math.abs(target.getX() - this.getX()) < length
                    && Math.abs(target.getZ() - this.getZ()) < length;
            if (shouldDive) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.1D * this.getSize(), 0.0D));
                this.move(MoverType.SELF, this.getDeltaMovement());
            }
        }
        super.travel(travelVector);
        if (this.getFirstPassenger() instanceof AbstractFish fish && !fish.isInWater()){
            fish.setAirSupply(300);
        }
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {return level.isUnobstructed(this);}

    @Override
    public boolean isPushedByFluid() {return false;}

    @Override
    public boolean canBreatheUnderwater() {return true;}

    @Override
    protected @NotNull ParticleOptions getParticleType() {
        return ModParticles.OceanSlimeParticle.get();
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof AbstractFish ? null : super.getControllingPassenger();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        Level level = pLevel.getLevel();
        if (this.random.nextInt(5) == 0) {
            int random = this.random.nextInt(3);
            AbstractFish fish =  switch (random){
                case 0 -> EntityType.COD.create(level);
                case 1 -> EntityType.SALMON.create(level);
                default -> EntityType.PUFFERFISH.create(level);
            };
               if (fish != null) {
                   this.spawnJockey(pLevel, difficulty, fish);
               }
        }
        return super.finalizeSpawn(pLevel, difficulty, pReason, pSpawnData, pDataTag);
    }

    private void spawnJockey(ServerLevelAccessor serverLevel, DifficultyInstance difficulty, Mob jockey) {
        jockey.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        jockey.finalizeSpawn(serverLevel, difficulty, MobSpawnType.JOCKEY, null, null);
        jockey.startRiding(this, true);
    }

    @Override
    protected @NotNull MaterialId getPlating() {
        return MaterialIds.bronze;
    }
}