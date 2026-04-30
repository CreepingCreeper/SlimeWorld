package com.creeping_creeper.slimeworld.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class GeyserBaseParticle extends BaseAshSmokeParticle {
    private GeyserBaseParticle(ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, int waterBlocks, float burstImpulseBase, SpriteSet sprites) {
        super(level, x, y, z, burstImpulse(burstImpulseBase, waterBlocks), burstImpulse(burstImpulseBase, waterBlocks), burstImpulse(burstImpulseBase, waterBlocks), xAux, yAux, zAux, 3.0F + 0.125F * (float)waterBlocks, sprites, 0.0F, 0, 0.0F, true);
        this.friction = 0.725F;
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.yd = Math.abs(this.yd);
        float lifetimeFactor = 0.8F + 0.2F * level.getRandom().nextFloat();
        this.lifetime = (int)(25.0F * lifetimeFactor);
    }

    private static float burstImpulse(float burstImpulseBase, int waterBlocks){
       return burstImpulseBase + 0.25F * (float)waterBlocks;
    }

    public static class Provider implements ParticleProvider<GeyserBaseParticleOptions> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(GeyserBaseParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
            RandomSource random = level.getRandom();
            double randomX = x + (double)((random.nextFloat() - 0.5F) * 0.5F);
            double randomY = y + ((random.nextFloat() - 0.5F) * 0.5F) + (double)0.2F;
            double randomZ = z + (double)((random.nextFloat() - 0.5F) * 0.5F);
            return new GeyserBaseParticle(level, randomX, randomY, randomZ, xAux, yAux, zAux, options.waterBlocks, options.burstImpulseBase, this.sprites);
        }
    }
}
