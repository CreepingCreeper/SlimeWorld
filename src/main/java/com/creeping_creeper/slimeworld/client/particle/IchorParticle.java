package com.creeping_creeper.slimeworld.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.block.SlimeType;

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

    public record Factory(ItemLike slime) implements ParticleProvider<SimpleParticleType> {
            public Factory(SlimeType type) {
                this(TinkerCommons.slimeball.get(type));
            }

            @Override
            public @NotNull Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
                return new IchorParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, new ItemStack(slime));
            }
        }
}
