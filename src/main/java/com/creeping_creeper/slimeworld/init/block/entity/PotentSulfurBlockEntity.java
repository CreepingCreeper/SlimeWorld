package com.creeping_creeper.slimeworld.init.block.entity;

import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModParticles;
import com.creeping_creeper.slimeworld.init.block.PotentSulfurBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class PotentSulfurBlockEntity extends BlockEntity {
    private static final Predicate<Entity> EFFECT_PREDICATE = EntitySelector.NO_SPECTATORS.and(EntitySelector.ENTITY_STILL_ALIVE);

    public PotentSulfurBlockEntity(BlockPos pos, BlockState state) {
        super(ModItems.PotentSulfurEntity.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, PotentSulfurBlockEntity potentSulfur) {
        if (level.getGameTime() % 10L == 0L && isUnderwater(level, pos)) {
            BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
            if (sourceBlock != null) {
                for(LivingEntity entity : getNearbyLivingEntities(level, sourceBlock)) {
                    if (canBeReachedByNoxiousGas(level, sourceBlock, entity.getEyePosition())) {
                        applyEffect(entity, (PotentSulfurBlock) state.getBlock());
                    }
                }

            }
        }
    }

    private static void applyEffect(LivingEntity entity, PotentSulfurBlock block) {
        entity.addEffect(new MobEffectInstance(block.getEffect(), 80, 0, true, true));
    }

    private static List<LivingEntity> getNearbyLivingEntities(Level level, BlockPos pos) {
        AABB aabb = (new AABB(pos)).inflate(2.5F, 0.0F, 2.5F);
        return level.getEntitiesOfClass(LivingEntity.class, aabb, EFFECT_PREDICATE);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, PotentSulfurBlockEntity entity) {
        if (level.getGameTime() % 20L == 0L && isUnderwater(level, pos)) {
            BlockPos sourceBlock = findNoxiousGasSourceBlock(level, pos);
            if (sourceBlock != null) {
                spawnNoxiousGasCloudParticle(level, sourceBlock.getCenter());
            }

        }
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
}
