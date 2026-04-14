package com.creeping_creeper.slimeworld.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SulfurBubbleParticle extends SingleQuadParticle {
    private final TextureAtlasSprite sprite;
    private final double yStart;
    private final double yEnd;
    private final float sizeStart;
    private double yPrev;

    private SulfurBubbleParticle(ClientLevel level, double x, double y, double z, double xa, double za, TextureAtlasSprite sprite) {
        super(level, x, y, z);
        this.sprite = sprite;
        this.gravity = -0.04F;
        this.friction = 0.85F;
        this.setSize(0.02F, 0.02F);
        this.xd = xa * (double)0.2F + (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.02F);
        this.zd = za * (double)0.2F + (double)((this.random.nextFloat() * 2.0F - 1.0F) * 0.02F);
        this.sizeStart = 0.02F + 0.02F * this.random.nextFloat();
        this.quadSize = this.sizeStart;
        this.lifetime = Integer.MAX_VALUE;
        this.yStart = this.yo;
        this.yEnd = this.yo + (double)4.0F - (double)1.0F;
        this.yPrev = y;
    }

    public void tick() {
        super.tick();
        if (!this.removed && !this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).isSourceOfType(Fluids.WATER)) {
            this.remove();
        }

        if (!this.removed && this.y >= this.yEnd) {
            this.remove();
        }

        if (!this.removed && this.y <= this.yPrev) {
            this.remove();
        }

        this.xd += this.randomHorizontalWiggling();
        this.zd += this.randomHorizontalWiggling();
        this.move(this.xd, 0.0F, this.zd);
        float travelProgress = (float)((this.y - this.yStart) / (this.yEnd - this.yStart));
        this.quadSize = this.sizeStart + travelProgress * (0.15F - this.sizeStart);
        this.yPrev = this.y;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    private double randomHorizontalWiggling() {
        return (double)(this.random.nextFloat() * 0.003F * (float)(this.random.nextBoolean() ? 1 : -1)) * (double)0.5F;
    }

    @Override
    protected float getU0() {
        return this.sprite.getU0();
    }

    @Override
    protected float getU1() {
        return this.sprite.getU1();
    }

    @Override
    protected float getV0() {
        return this.sprite.getV0();
    }

    @Override
    protected float getV1() {
        return this.sprite.getV1();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
            return new SulfurBubbleParticle(level, x, y, z, xAux, yAux, this.sprite.get(level.random));
        }

    }
}

