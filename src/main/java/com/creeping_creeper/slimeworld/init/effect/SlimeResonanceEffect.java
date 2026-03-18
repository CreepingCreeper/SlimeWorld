package com.creeping_creeper.slimeworld.init.effect;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.library.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.common.TinkerEffect;

import java.util.EnumSet;
import java.util.Set;

public class SlimeResonanceEffect extends TinkerEffect {
    public SlimeResonanceEffect(MobEffectCategory typeIn, int color, boolean show) {
        super(typeIn, color, show);
    }

    private static final Set<RelativeMovement> DEFAULT_TELEPORT_FLAGS = EnumSet.of(
            RelativeMovement.X,
            RelativeMovement.Y,
            RelativeMovement.Z,
            RelativeMovement.Y_ROT,
            RelativeMovement.X_ROT
    );
    @Override
    public boolean isDurationEffectTick(int tick, int amplifier) {
        return tick == 1;
    }
    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (living.canChangeDimensions()) {
            Level level = living.level();
            MinecraftServer server = level.getServer();
            if (server == null) return;
            ServerLevel serverLevel = level.dimension() == SlimeWorld.SLIMEWORLD ? server.getLevel(Level.OVERWORLD) : server.getLevel(SlimeWorld.SLIMEWORLD);
            if (serverLevel != null) {
                ParticleUtil.slimeParticle(level,ParticleTypes.ITEM_SLIME, 12, 1, living.getX(), living.getY() + 0.1, living.getZ());
                living.teleportTo(serverLevel, living.getX(), 256 ,living.getZ(), DEFAULT_TELEPORT_FLAGS, living.getYRot(), living.getXRot());
                living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 4));
                living.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 400));
            }
        }
    }
}