package com.creeping_creeper.slimeworld.init.block.flower;

import com.creeping_creeper.slimeworld.init.ModEffects;
import com.creeping_creeper.slimeworld.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ConsecratedFlowerBlock extends BaseFlowerBlock {
    public ConsecratedFlowerBlock(Properties properties) {
        super(ModEffects.Blessing, 15, properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity living) {
            ModEffects.Blessing.get().apply(living, 20, 0, true);
            if(living.getMobType() == MobType.UNDEAD){
                living.hurt(living.damageSources().magic(),1);
            }
        }
    }

    @Override
    public SimpleParticleType particleType(){
        return ModEntities.whiteSporeParticle.get();
    }
}
