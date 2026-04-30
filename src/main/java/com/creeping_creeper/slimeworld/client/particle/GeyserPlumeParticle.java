package com.creeping_creeper.slimeworld.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class GeyserPlumeParticle extends SingleQuadParticle {
    protected TextureAtlasSprite sprite;
    private final SpriteSet sprites;
    private final double startY;
    private final double maxY;
    private final float initialPropulsion;
    private final float horizontalSprayX;
    private final float horizontalSprayZ;
    private final float minSize;
    private final float maxSize;
    private boolean done;

    private GeyserPlumeParticle(ClientLevel level, double x, double y, double z, double xa, double ya, double za, GeyserParticleOptions options, SpriteSet sprites) {
        super(level, x, y, z, xa, ya, za);
        int plumeHeight = 5 * Math.max(1, options.waterBlocks);
        this.hasPhysics = true;
        this.speedUpWhenYMotionIsBlocked = true;
        this.lifetime = plumeHeight * 5;
        this.yd = 0.0F;
        this.startY = y;
        this.maxY = this.startY + (double) plumeHeight - (double) 1.0F;
        this.horizontalSprayX = (level.getRandom().nextFloat() - 0.5F) * 0.2F;
        this.horizontalSprayZ = (level.getRandom().nextFloat() - 0.5F) * 0.2F;
        this.friction = 1.0F;
        this.initialPropulsion = (options.waterBlocks == 1 ? 1.5F : 1.0F) * (float) plumeHeight * 1.45F;
        this.gravity = -this.initialPropulsion;
        float initiallyRandomizedSize = this.quadSize * 0.75F;
        this.minSize = initiallyRandomizedSize * (2.0F + (float) plumeHeight / 8.0F);
        this.maxSize = initiallyRandomizedSize * (3.0F + (float) plumeHeight / 8.0F);
        this.quadSize = this.minSize;
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
    }

    public void tick() {
        super.tick();
        if (!this.done && (this.yd < (double) 0.0F || this.y > this.maxY || this.y == this.yo)) {
            this.lifetime = Math.min(this.lifetime, this.age + 5);
            this.friction = 0.0F;
            this.done = true;
        }

        double yProgressLinear = Mth.clamp((this.y - this.startY) / (this.maxY - this.startY), 0.0F, 1.0F);
        double yProgressExponential = Math.pow(yProgressLinear, 3.0F);
        this.gravity = this.initialPropulsion * (float) yProgressExponential * 0.12F;
        this.xd = yProgressLinear * (double) this.horizontalSprayX;
        this.zd = yProgressLinear * (double) this.horizontalSprayZ;
        this.setSpriteFromAge(this.sprites);
        this.quadSize = this.minSize + (float) (yProgressLinear * (double) (this.maxSize - this.minSize));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
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

    public void setSpriteFromAge(SpriteSet sprites) {
        if (!this.removed) {
            this.setSprite(sprites.get(this.age, this.lifetime));
        }
    }

    protected void setSprite(TextureAtlasSprite icon) {
        this.sprite = icon;
    }

    public static class Provider implements ParticleProvider<GeyserParticleOptions> {
        private final SpriteSet sprites;

        public Provider(final SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(GeyserParticleOptions options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux) {
            RandomSource random = level.getRandom();
            double randomX = x + (double) ((random.nextFloat() - 0.5F) * 0.2F);
            double randomY = y + (double) random.nextFloat();
            double randomZ = z + (double) ((random.nextFloat() - 0.5F) * 0.2F);
            return new GeyserPlumeParticle(level, randomX, randomY, randomZ, xAux, yAux, zAux, options, this.sprites);

        }
    }
}

