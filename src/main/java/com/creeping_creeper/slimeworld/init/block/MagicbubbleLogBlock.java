package com.creeping_creeper.slimeworld.init.block;

import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.entity.MagicbubbleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MagicbubbleLogBlock extends RotatedPillarBlock {
    public MagicbubbleLogBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isRaining() || level.isThundering()) return;
        MagicbubbleEntity entity = new MagicbubbleEntity(ModEntities.magicbubble.get(), level);
        entity.setPos(pos.getX() + getPos(random), pos.getY() + 0.5, pos.getZ() + getPos(random));
        level.addFreshEntity(entity);
    }

    private double getPos(RandomSource random){
        return random.nextBoolean() ? -random.nextDouble() : 1 + random.nextDouble();
    }
}