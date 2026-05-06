package com.creeping_creeper.slimeworld.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class StaticColorParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    StaticColorParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, 0, 0, 0);
        this.sprites = sprites;
        this.lifetime = 10;
        this.hasPhysics = false;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public int getLightColor(float partialTick) {
        return super.getLightColor(partialTick);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        this.setSpriteFromAge(this.sprites);
    }

    @OnlyIn(Dist.CLIENT)
        public record Provider(SpriteSet sprite, float red, float green,
                               float blue) implements ParticleProvider<SimpleParticleType> {

        @Override
            @NotNull
            public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
                StaticColorParticle particle = new StaticColorParticle(level, x, y, z, this.sprite);
                particle.setColor(this.red, this.green, this.blue);

                return particle;
            }
        }
}