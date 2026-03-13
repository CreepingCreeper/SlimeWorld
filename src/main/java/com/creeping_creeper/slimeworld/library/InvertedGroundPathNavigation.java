package com.creeping_creeper.slimeworld.library;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class InvertedGroundPathNavigation extends PathNavigation {
    public InvertedGroundPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new WalkNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    protected Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), this.getSurfaceY(), this.mob.getZ());
    }

    @Override
    protected boolean canUpdatePath() {
        return this.mob.onGround() || this.isInLiquid() || this.mob.isPassenger();
    }

    @Override
    public Path createPath(BlockPos pos, int accuracy) {
        if (this.level.getBlockState(pos).isAir()) {
            BlockPos blockpos;
            for(blockpos = pos.above(); blockpos.getY() < this.level.getMaxBuildHeight() && this.level.getBlockState(blockpos).isAir(); blockpos = blockpos.above()) {
            }

            if (blockpos.getY() < this.level.getMaxBuildHeight()) {
                return super.createPath(blockpos.above(), accuracy);
            }

            while(blockpos.getY() > this.level.getMinBuildHeight() && this.level.getBlockState(blockpos).isAir()) {
                blockpos = blockpos.below();
            }

            pos = blockpos;
        }

        if (!this.level.getBlockState(pos).isSolid()) {
            return super.createPath(pos, accuracy);
        } else {
            BlockPos blockpos1;
            for(blockpos1 = pos.below(); blockpos1.getY() > this.level.getMinBuildHeight() && this.level.getBlockState(blockpos1).isSolid(); blockpos1 = blockpos1.below()) {
            }

            return super.createPath(blockpos1, accuracy);
        }
    }

    @Override
    public Path createPath(Entity entity, int accuracy) {
        return this.createPath(entity.blockPosition(), accuracy);
    }

    private int getSurfaceY() {
        if (this.mob.isInWater() && this.canFloat()) {
            int i = this.mob.getBlockY();
            BlockState blockstate = this.level.getBlockState(BlockPos.containing(this.mob.getX(), i, this.mob.getZ()));
            int j = 0;

            while(blockstate.is(Blocks.WATER)) {
                ++i;
                blockstate = this.level.getBlockState(BlockPos.containing(this.mob.getX(), i, this.mob.getZ()));
                ++j;
                if (j > 16) {
                    return this.mob.getBlockY();
                }
            }

            return i;
        } else {
            return Mth.floor(this.mob.getBoundingBox().maxY - 0.5D);
        }
    }

    @Override
    protected void trimPath() {
        super.trimPath();
        if (canSeeSkyWithoutSolid(this.level, BlockPos.containing(this.mob.getX(), this.mob.getBoundingBox().maxY - 0.5D, this.mob.getZ()))) {
            return;
        }
        for(int i = 0; i < this.path.getNodeCount(); ++i) {
            Node node = this.path.getNode(i);
            if (canSeeSkyWithoutSolid(this.level, new BlockPos(node.x, node.y, node.z))) {
                this.path.truncateNodes(i);
                return;
            }
        }
    }

    private boolean canSeeSkyWithoutSolid(Level level, BlockPos pos){
        return level.canSeeSky(pos) && level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() <= pos.getY();
    }
}
