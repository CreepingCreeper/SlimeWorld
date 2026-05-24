package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.init.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.world.entity.TravelersPlateSlimeEntity;

import javax.annotation.Nullable;

public class TomatoSlimeEntity extends TravelersPlateSlimeEntity {
    public TomatoSlimeEntity(EntityType<? extends TomatoSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    protected @NotNull ParticleOptions getParticleType() {
        return ModParticles.OceanSlimeParticle.get();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {

        return super.finalizeSpawn(pLevel, difficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    protected @NotNull MaterialId getPlating() {
        return MaterialIds.bronze;
    }
}