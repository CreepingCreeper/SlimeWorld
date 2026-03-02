package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.entity.IchorSlimeEntity;
import com.creeping_creeper.slimeworld.init.entity.OceanSlimeEntity;
import com.creeping_creeper.slimeworld.init.entity.OriginSlimeEntity;
import com.creeping_creeper.slimeworld.init.entity.SteelSlimeBossEntity;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.mantle.registration.object.EntityObject;

public class ModEntities {
    protected static final EntityTypeDeferredRegister ENTITIES = new EntityTypeDeferredRegister(SlimeWorld.MODID);
    protected static final SynchronizedDeferredRegister<ParticleType<?>> PARTICLE_TYPES = SynchronizedDeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SlimeWorld.MODID);

    public static final EntityObject<OceanSlimeEntity> oceanSlimeEntity = ENTITIES.registerWithEgg("ocean_slime", () ->
            EntityType.Builder.of(OceanSlimeEntity::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(20)
                    .sized(2.04F, 2.04F)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.oceanSlimeEntity.get().create(world)), 0x4278e6, 0x2a60d7);
    public static final EntityObject<IchorSlimeEntity> ichorSlimeEntity = ENTITIES.registerWithEgg("ichor_slime", () ->
            EntityType.Builder.of(IchorSlimeEntity::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(20)
                    .sized(2.04F, 2.04F)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.ichorSlimeEntity.get().create(world)), 0xff8a2f, 0xeb5d00);

    public static final EntityObject<OriginSlimeEntity> originSlimeEntity = ENTITIES.registerWithEgg("origin_slime", () ->
            EntityType.Builder.of(OriginSlimeEntity::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(20)
                    .sized(2.04F, 2.04F)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.originSlimeEntity.get().create(world)), 0xb7b7b7, 0x8b8b8b);

    public static final EntityObject<SteelSlimeBossEntity> steelSlimeBossEntity = ENTITIES.registerWithEgg("steel_slime_boss", () ->
            EntityType.Builder.of(SteelSlimeBossEntity::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(20)
                    .sized(2.04F, 2.04F)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.steelSlimeBossEntity.get().create(world)), 0xb7b7b7, 0x8b8b8b);

    public static final RegistryObject<SimpleParticleType> oceanSlimeParticle = PARTICLE_TYPES.register("ocean_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> ichorSlimeParticle = PARTICLE_TYPES.register("ichor_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> originSlimeParticle = PARTICLE_TYPES.register("origin_slime", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> steelSlimeParticle = PARTICLE_TYPES.register("steel_slime", () -> new SimpleParticleType(false));

    public static void registers(IEventBus bus) {
        ENTITIES.register(bus);
        PARTICLE_TYPES.register(bus);
    }
}
