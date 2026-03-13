package com.creeping_creeper.slimeworld.library;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

public interface ParticleUtil {
    static void slimeParticle(Level level, ParticleOptions particleType, int count, float r, double posX, double posY, double posZ){
        if (level instanceof ServerLevel server){
            for(int j = 0; j < count * r; ++j) {
                float f = level.random.nextFloat() * ((float)Math.PI * 2F);
                float f1 = level.random.nextFloat() * 0.5F + 0.5F;
                float f2 = Mth.sin(f) * r * f1;
                float f3 = Mth.cos(f) * r * f1;
                server.sendParticles(particleType, posX + f2, posY, posZ + f3, 0, 0.0F, 0.0F, 0.0F, 0);
            }
        }
    }
}
