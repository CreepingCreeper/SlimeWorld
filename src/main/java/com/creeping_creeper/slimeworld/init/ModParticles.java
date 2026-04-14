package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;

public class ModParticles {
    protected static final SynchronizedDeferredRegister<ParticleType<?>> PARTICLE_TYPES = SynchronizedDeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SlimeWorld.MODID);

    public static final RegistryObject<SimpleParticleType> MagicbubbleParticle = PARTICLE_TYPES.register("magicbubble", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> OceanSlimeParticle = PARTICLE_TYPES.register("ocean_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> IchorSlimeParticle = PARTICLE_TYPES.register("ichor_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> OriginSlimeParticle = PARTICLE_TYPES.register("origin_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SteelSlimeParticle = PARTICLE_TYPES.register("steel_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SulfurCubeGoo = PARTICLE_TYPES.register("sulfur_cube_goo", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> SulfurBubbles = PARTICLE_TYPES.register("sulfur_bubbles", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> NoxiousGas = PARTICLE_TYPES.register("noxious_gas", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> NoxiousGasCloud = PARTICLE_TYPES.register("noxious_gas_cloud", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> WhiteSporeParticle = PARTICLE_TYPES.register("white_spore", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BlackSporeParticle = PARTICLE_TYPES.register("black_spore", () -> new SimpleParticleType(false));

    public static void registers(IEventBus bus) {
        PARTICLE_TYPES.register(bus);
    }
}
