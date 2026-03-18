package com.creeping_creeper.slimeworld.client;

import lombok.RequiredArgsConstructor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.block.SlimeType;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class IchorParticle extends BreakingItemParticle {
    public IchorParticle(ClientLevel level, double x, double y, double z, ItemStack stack) {
        super(level, x, y, z, stack);
        this.gravity = -1;
    }

    public IchorParticle(ClientLevel worldIn, double posXIn, double posYIn, double posZIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, ItemStack stack) {
        super(worldIn, posXIn, posYIn, posZIn, xSpeedIn, ySpeedIn, zSpeedIn, stack);
        this.gravity = -1;
    }

    @RequiredArgsConstructor
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final ItemLike slime;

        public Factory(SlimeType type) {
            this.slime = TinkerCommons.slimeball.get(type);
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new IchorParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, new ItemStack(slime));
        }
    }
}
