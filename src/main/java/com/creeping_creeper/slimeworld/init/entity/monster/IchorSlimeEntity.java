package com.creeping_creeper.slimeworld.init.entity.monster;

import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModParticles;
import com.creeping_creeper.slimeworld.library.InvertedGroundPathNavigation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.shared.TinkerEffects;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.world.entity.TravelersPlateSlimeEntity;

import javax.annotation.Nullable;

public class IchorSlimeEntity extends TravelersPlateSlimeEntity {
    private double bounceAmount = 0f;

    public IchorSlimeEntity(EntityType<? extends TravelersPlateSlimeEntity> type, Level world) {
        super(type, world);
    }

    public static boolean canSpawnHere(EntityType<? extends Slime> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (reason == MobSpawnType.SPAWNER) {
            return true;
        }
        return !world.canSeeSky(pos) && (world.getBiome(pos).is(ModTags.Biomes.ICHOR_SLIME_SPAWN) || world.getBlockState(pos.above(3)).is(ModTags.Blocks.ICHOR_SLIME_SPAWN));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new InvertedGroundPathNavigation(this, level);
    }

    @Override
    public void setOnGroundWithKnownMovement(boolean onGround, @NotNull Vec3 movement) {
        boolean underGround = this.verticalCollision && this.getDeltaMovement().y > (double)0.0F;
        this.onGround = underGround;
        this.checkSupportingBlock(underGround, movement);
    }

    @Override
    protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions size) {
        return 0.375F * size.height;
    }

    @Override
    protected void jumpFromGround() {
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x, -this.getJumpPower(), vec3.z);
        this.hasImpulse = true;
    }

    @Override
    protected @NotNull ParticleOptions getParticleType() {
        return ModParticles.IchorSlimeParticle.get();
    }

    @Override
    protected boolean spawnCustomParticles() {
        int i = this.getSize();
        for (int j = 0; j < i * 8; ++j) {
            float f = this.random.nextFloat() * ((float) Math.PI * 2F);
            float f1 = this.random.nextFloat() * 0.5F + 0.5F;
            float f2 = Mth.sin(f) * (float) i * 0.5F * f1;
            float f3 = Mth.cos(f) * (float) i * 0.5F * f1;
            this.level().addParticle(this.getParticleType(), this.getX() + (double) f2, this.getBoundingBox().maxY, this.getZ() + (double) f3, 0.0F, 0.0F, 0.0F);
        }
        return true;
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
        if (bounceAmount < 0) {
            Vec3 motion = getDeltaMovement();
            setDeltaMovement(motion.x, bounceAmount, motion.z);
            bounceAmount = 0;
        }
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag dataTag) {
        SpawnGroupData spawnData = super.finalizeSpawn(level, difficulty, reason, pSpawnData, dataTag);
        this.addEffect(new MobEffectInstance(TinkerEffects.antigravity.get(), -1, 0, true, false));
        return spawnData;
    }

    @Override
    protected @NotNull MaterialId getPlating() {
        return MaterialIds.cobalt;
    }
}
