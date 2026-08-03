package com.creeping_creeper.slimeworld.init.block.flower;

import com.creeping_creeper.slimeworld.init.ModEffects;
import com.creeping_creeper.slimeworld.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class GraveyardFlowerBlock extends BaseFlowerBlock {
    public GraveyardFlowerBlock(Properties properties) {
        super(ModEffects.Curse, 15, properties);
    }

    @Override
    public void entityInside(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity living) {
            ModEffects.Curse.get().apply(living, 20, 0, true);
            if(living.getMobType() == MobType.UNDEAD && living.tickCount % 10 == 0){
                living.heal(1);
            }
        }
    }

    @Override
    public SimpleParticleType particleType(){
        return ModParticles.BlackSporeParticle.get();
    }
}
