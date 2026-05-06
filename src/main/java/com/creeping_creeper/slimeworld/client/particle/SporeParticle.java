package com.creeping_creeper.slimeworld.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ParticleGroup;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class SporeParticle {

    @OnlyIn(Dist.CLIENT)
        public record WhiteSporeProvider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
                SuspendedParticle suspendedparticle = new SuspendedParticle(level, this.sprite, x, y, z, 0.0F, -0.8F, 0.0F) {
                    public @NotNull Optional<ParticleGroup> getParticleGroup() {
                        return Optional.of(ParticleGroup.SPORE_BLOSSOM);
                    }
                };
                suspendedparticle.setLifetime(Mth.randomBetweenInclusive(level.random, 500, 1000));
                suspendedparticle.gravity = 0.01F;
                suspendedparticle.setColor(0.9F, 0.9F, 0.8F);
                return suspendedparticle;
            }
        }

    @OnlyIn(Dist.CLIENT)
    public static class BlackSporeProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public BlackSporeProvider(SpriteSet sprites) {
            this.sprite = sprites;
        }

        public Particle createParticle(@NotNull SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            RandomSource randomsource = level.random;
            double d0 = randomsource.nextGaussian() * (double)1.0E-6F;
            double d1 = randomsource.nextGaussian() * (double)1.0E-4F;
            double d2 = randomsource.nextGaussian() * (double)1.0E-6F;
            SuspendedParticle sporeParticle = new SuspendedParticle(level, this.sprite, x, y, z, d0, d1, d2);
            sporeParticle.setColor(0.7F, 0.6F, 0.4F);
            return sporeParticle;
        }
    }

}
