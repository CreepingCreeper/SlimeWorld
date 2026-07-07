package com.creeping_creeper.slimeworld.client.particle;

import com.creeping_creeper.slimeworld.init.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class GeyserEruptionParticle extends NoRenderParticle {
    private final int waterBlocks;
    private final double xa;
    private final double ya;
    private final double za;
    private final GeyserParticleOptions plumeParticle;
    private final GeyserBaseParticleOptions baseParticle;
    private final GeyserBaseParticleOptions poofParticle;

    protected GeyserEruptionParticle(final ClientLevel level, final double x, final double y, final double z, final double xAux, final double yAux, final double zAux, final GeyserParticleOptions options) {
        super(level, x, y, z);
        this.xa = xAux;
        this.ya = yAux;
        this.za = zAux;
        this.waterBlocks = options.waterBlocks();
        this.lifetime = 20;
        this.plumeParticle = new GeyserParticleOptions(ModParticles.GeyserPlume.get(), this.waterBlocks);
        this.baseParticle = new GeyserBaseParticleOptions(ModParticles.GeyserBase.get(), this.waterBlocks, 1.5F);
        this.poofParticle = new GeyserBaseParticleOptions(ModParticles.GeyserPoof.get(), this.waterBlocks, 2.0F);
    }

    public void tick() {
        super.tick();
        if (this.age % 2 == 0) {
            for(int i = 0; i < 2; ++i) {
                this.level.addParticle(this.baseParticle, this.x, this.y, this.z, this.xa, this.ya, this.za);
            }
        }

        for(int i = 0; i < this.waterBlocks + 2; ++i) {
            this.level.addParticle(this.plumeParticle, this.x, this.y, this.z, this.xa, this.ya, this.za);
        }

        if (this.age % 10 == 0) {
            for(int i = 0; i < 20; ++i) {
                this.level.addParticle(this.poofParticle, this.x, this.y, this.z, this.xa, this.ya, this.za);
            }
        }

    }

    public static class Provider implements ParticleProvider<GeyserParticleOptions> {
        public Provider(SpriteSet sprites) {}

        @Override
        public @Nullable Particle createParticle(@NotNull GeyserParticleOptions options, @NotNull ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
            return new GeyserEruptionParticle(level, x, y, z, xAux, yAux, zAux, options);
        }
    }
}

