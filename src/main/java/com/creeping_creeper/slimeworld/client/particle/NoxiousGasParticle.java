package com.creeping_creeper.slimeworld.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class NoxiousGasParticle extends BaseAshSmokeParticle {
    private final float fadeOutStartingPoint;

    protected NoxiousGasParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, float scale, SpriteSet sprites) {
        super(level, x, y, z, 0.1F, 0.1F, 0.1F, xa, ya, za, scale, sprites, 0.3F, 5, -0.02F, true);
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.lifetime = (int)((double)6.0F / ((double)this.random.nextFloat() * (double)0.5F + (double)0.5F) * (double)scale);
        this.fadeOutStartingPoint = (float)this.lifetime / 2.0F;
    }

    public void tick() {
        super.tick();
        if ((float)this.age > this.fadeOutStartingPoint) {
            float framesSinceFadeOutStart = (float)this.age - this.fadeOutStartingPoint;
            this.setAlpha(((float)this.lifetime - framesSinceFadeOutStart) / (float)this.lifetime);
        }

    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
        public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {

        @Override
            public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
                return new NoxiousGasParticle(level, x, y, z, xAux, yAux, zAux, 3.0F, this.sprites);
            }
        }
}

