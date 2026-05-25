package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.client.particle.GeyserBaseParticleOptions;
import com.creeping_creeper.slimeworld.client.particle.GeyserParticleOptions;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ModParticles {
    protected static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SlimeWorld.MODID);

    public static final RegistryObject<SimpleParticleType> MagicbubbleParticle = PARTICLE_TYPES.register("magicbubble", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> OceanSlimeParticle = PARTICLE_TYPES.register("ocean_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> IchorSlimeParticle = PARTICLE_TYPES.register("ichor_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> OriginSlimeParticle = PARTICLE_TYPES.register("origin_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> TomatoSlimeParticle = PARTICLE_TYPES.register("tomato_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SteelSlimeParticle = PARTICLE_TYPES.register("steel_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> KnightSlimeParticle = PARTICLE_TYPES.register("knight_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> SulfurCubeGoo = PARTICLE_TYPES.register("sulfur_cube_goo", () -> new SimpleParticleType(false));

    public static final RegistryObject<SimpleParticleType> SulfurBubbles = PARTICLE_TYPES.register("sulfur_bubbles", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> NoxiousGas = PARTICLE_TYPES.register("noxious_gas", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> NoxiousGasCloud = PARTICLE_TYPES.register("noxious_gas_cloud", () -> new SimpleParticleType(false));
    public static final RegistryObject<ParticleType<GeyserParticleOptions>> Geyser = PARTICLE_TYPES.register("geyser", () -> new ParticleType<>(true, GeyserParticleOptions.DESERIALIZER) {
        @Override
        public @NotNull Codec<GeyserParticleOptions> codec() {
            return GeyserParticleOptions.codec(this);
        }
    });
    public static final RegistryObject<ParticleType<GeyserBaseParticleOptions>> GeyserBase = PARTICLE_TYPES.register("geyser_base", () -> new ParticleType<>(true, GeyserBaseParticleOptions.DESERIALIZER) {
        @Override
        public @NotNull Codec<GeyserBaseParticleOptions> codec() {
            return GeyserBaseParticleOptions.codec(this);
        }
    });
    public static final RegistryObject<ParticleType<GeyserBaseParticleOptions>> GeyserPoof = PARTICLE_TYPES.register("geyser_poof", () -> new ParticleType<>(true, GeyserBaseParticleOptions.DESERIALIZER) {
        @Override
        public @NotNull Codec<GeyserBaseParticleOptions> codec() {
            return GeyserBaseParticleOptions.codec(this);
        }
    });
    public static final RegistryObject<ParticleType<GeyserParticleOptions>> GeyserPlume = PARTICLE_TYPES.register("geyser_plume", () -> new ParticleType<>(true, GeyserParticleOptions.DESERIALIZER) {
        @Override
        public @NotNull Codec<GeyserParticleOptions> codec() {
            return GeyserParticleOptions.codec(this);
        }
    });
    public static final RegistryObject<SimpleParticleType> WhiteSporeParticle = PARTICLE_TYPES.register("white_spore", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> BlackSporeParticle = PARTICLE_TYPES.register("black_spore", () -> new SimpleParticleType(false));

    public static void registers(IEventBus bus) {
        PARTICLE_TYPES.register(bus);
    }
}
