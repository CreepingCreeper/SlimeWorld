package com.creeping_creeper.slimeworld.init.block.entity;

import com.creeping_creeper.slimeworld.client.particle.GeyserParticleOptions;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModParticles;
import com.creeping_creeper.slimeworld.init.block.PotentSulfurBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class PotentSulfurBlockEntity extends BlockEntity {
    public int waitingCountdown = -1;
    public int geyserEruptionTime = -1;
    public int dormantGeyserTime = -1;
    public long eruptionTick = -1L;
    private static final Predicate<Entity> EFFECT_PREDICATE = EntitySelector.NO_SPECTATORS.and(EntitySelector.ENTITY_STILL_ALIVE);
    public static BlockEntityTicker<PotentSulfurBlockEntity> SERVER_NAUSEA_EFFECT_TICKER = (level, pos, state, potentSulfur) -> {
        if (level.getGameTime() % 10L == 0L) {
            BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
            if (sourceBlock != null) {
                for(LivingEntity entity : getNearbyLivingEntities(level, sourceBlock)) {
                    if (canBeReachedByNoxiousGas(level, sourceBlock, entity.getEyePosition())) {
                        applyEffect(entity, (PotentSulfurBlock) state.getBlock());
                    }
                }

            }
        }
    };

    public static BlockEntityTicker<PotentSulfurBlockEntity> CLIENT_NOXIOUS_GAS_TICKER = (level, pos, state, entity) -> {
        if (level.getGameTime() % 20L == 0L) {
            BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
            if (sourceBlock != null) {
                spawnNoxiousGasCloudParticle(level, sourceBlock.getCenter());
            }

        }
    };

    public static BlockEntityTicker<PotentSulfurBlockEntity> CLIENT_GEYSER_PLUME_TICKER = (level, pos, state, entity) -> {
        BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
        if (sourceBlock != null) {
            if ((level.getGameTime() - entity.eruptionTick) % 20L == 0L) {
                spawnGeyserParticle(level, pos.getCenter(), getBottomCenter(sourceBlock));
            }

        }
    };

    public static BlockEntityTicker<PotentSulfurBlockEntity> SERVER_WAITING_COUNTDOWN_TICKER = (level, pos, state, entity) -> {
        if (level.getGameTime() % 20L == 0L) {
            BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
            if (sourceBlock != null) {
                if (entity.waitingCountdown <= 0) {
                    int waterBlocks = (int)Math.floor(getBottomCenter(sourceBlock).y - pos.getCenter().y);
                    if (state.getValue(PotentSulfurBlock.TYPE) == 2) {
                        entity.waitingCountdown = 10 * (waterBlocks - 1) + entity.dormantGeyserTime;
                    } else {
                        entity.waitingCountdown = waterBlocks - 1 + entity.geyserEruptionTime;
                    }
                }

                if (entity.waitingCountdown > 0) {
                    --entity.waitingCountdown;
                }

                if (entity.waitingCountdown == 0) {
                    level.setBlock(pos, state.setValue(PotentSulfurBlock.TYPE, state.getValue(PotentSulfurBlock.TYPE) == 2 ? 3 : 2), 3);
                }

            }
        }
        if (state.getValue(PotentSulfurBlock.TYPE) == 2){
            SERVER_NAUSEA_EFFECT_TICKER.tick(level, pos, state, entity);
        }
    };

    public static BlockEntityTicker<PotentSulfurBlockEntity> SERVER_LAUNCH_ENTITY_TICKER = (level, pos, state, entity) -> {
        BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
        if (sourceBlock != null) {
            int waterBlocks = (int)Math.floor(getBottomCenter(sourceBlock).y - pos.getCenter().y);
            AABB aabb = (new AABB(pos)).inflate(0.0F, waterBlocks * 5, 0.0F).move(0.0F, waterBlocks, 0.0F);

            for(Entity entityToBeLaunched : level.getEntitiesOfClass(Entity.class, aabb, EFFECT_PREDICATE)) {
                Vec3 entityVelocity = entityToBeLaunched.getDeltaMovement();
                if (entityVelocity.y < (double)0.3F + (double)waterBlocks * 0.1 && haveLineOfSight(level, sourceBlock.below().getCenter(), entityToBeLaunched.getEyePosition())) {
                    entityToBeLaunched.addDeltaMovement(new Vec3(0.0F, 0.2F, 0.0F));
                    entityToBeLaunched.hurtMarked = true;
                  }
            }

            if (level.getGameTime() % 20L == 0L) {
                //level.playSound((Entity)null, pos, SoundEvents.GEYSER_ERUPTION_ACTIVE, SoundSource.BLOCKS, 1.0F * (float)waterBlocks, 1.0F);
            }

        }
        if (state.getValue(PotentSulfurBlock.TYPE) == 3){
            SERVER_WAITING_COUNTDOWN_TICKER.tick(level, pos, state, entity);
        }
    };

    public PotentSulfurBlockEntity(BlockPos pos, BlockState state) {
        super(ModItems.PotentSulfurEntity.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("dormant_time", this.dormantGeyserTime);
        tag.putInt("eruption_time", this.geyserEruptionTime);
        tag.putInt("countdown", this.waitingCountdown);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.dormantGeyserTime = tag.getInt("dormant_time");
        this.geyserEruptionTime = tag.getInt("eruption_time");
        this.waitingCountdown = tag.getInt("countdown");
    }

    public void resetCountdown() {
        this.waitingCountdown = -1;
    }

    private static void applyEffect(LivingEntity entity, PotentSulfurBlock block) {
        entity.addEffect(new MobEffectInstance(block.getEffect(), 80, 0, true, true));
    }

    private static List<LivingEntity> getNearbyLivingEntities(Level level, BlockPos pos) {
        AABB aabb = (new AABB(pos)).inflate(2.5F, 0.0F, 2.5F);
        return level.getEntitiesOfClass(LivingEntity.class, aabb, EFFECT_PREDICATE);
    }

    private static void spawnGeyserParticle(Level level, Vec3 sulfurPos, Vec3 sourcePos) {
        int waterBlocks = (int)Math.floor(sourcePos.y - sulfurPos.y);
        level.addParticle(new GeyserParticleOptions(ModParticles.Geyser.get(), waterBlocks), sourcePos.x, sourcePos.y, sourcePos.z, 0.0F, 0.0F, 0.0F);
    }

    private static void spawnNoxiousGasCloudParticle(Level level, Vec3 pos) {
        level.addParticle(ModParticles.NoxiousGasCloud.get(), pos.x, pos.y, pos.z, 0.0F, 0.0F, 0.0F);
    }

    private static @Nullable BlockPos findNoxiousGasSourceBlock(Level level, BlockPos origin) {
        int maxY = origin.getY() + 4 + 1;
        BlockPos.MutableBlockPos pos = origin.above(2).mutable();

        while(pos.getY() <= maxY) {
            if (!level.getFluidState(pos).isSourceOfType(Fluids.WATER)) {
                if (level.getBlockState(pos).isAir()) {
                    return pos.immutable();
                }
                break;
            }

            pos.move(Direction.UP);
        }

        return null;
    }

    public static boolean canBeReachedByNoxiousGas(Level level, BlockPos sourceBlock, Vec3 pos) {
        if (!isAir(level, pos)) {
            return false;
        } else if (pos.distanceToSqr(sourceBlock.getCenter()) > (double)9.0F) {
            return false;
        } else {
            Vec3 belowSource = sourceBlock.below().getCenter();
            Vec3 belowPos = pos.with(Direction.Axis.Y, pos.y - (double)1.0F);
            return isWater(level, belowPos) && haveLineOfSight(level, belowSource, belowPos);
        }
    }

    private static boolean haveLineOfSight(Level level, Vec3 a, Vec3 b) {
        HitResult hitResult = level.clip(new ClipContext(a, b, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, null));
        return hitResult.getType() != HitResult.Type.BLOCK;
    }

    private static boolean isUnderwater(Level level, BlockPos pos) {
        return level.getFluidState(pos.above()).isSourceOfType(Fluids.WATER);
    }

    private static boolean isWater(Level level, Vec3 pos) {
        return level.getFluidState(BlockPos.containing(pos)).isSourceOfType(Fluids.WATER);
    }

    private static boolean isAir(Level level, Vec3 pos) {
        return level.getBlockState(BlockPos.containing(pos)).isAir();
    }

    private static Vec3 getBottomCenter(BlockPos pos) {
        return Vec3.atBottomCenterOf(pos);
    }
}
