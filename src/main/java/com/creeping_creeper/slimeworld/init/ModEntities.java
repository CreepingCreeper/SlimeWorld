package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.entity.*;
import com.creeping_creeper.slimeworld.init.entity.boss.KnightSlimeBossEntity;
import com.creeping_creeper.slimeworld.init.entity.boss.SteelSlimeBossEntity;
import com.creeping_creeper.slimeworld.init.entity.golem.*;
import com.creeping_creeper.slimeworld.init.entity.monster.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;
import slimeknights.mantle.registration.object.EntityObject;

public class ModEntities {
    protected static final EntityTypeDeferredRegister ENTITIES = new EntityTypeDeferredRegister(SlimeWorld.MODID);

    public static final RegistryObject<EntityType<TomatoProjectile>> TomatoProjectileEntity = ENTITIES.register("tomato_projectile", () ->
            EntityType.Builder.<TomatoProjectile>of(TomatoProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).setShouldReceiveVelocityUpdates(false));
    public static final RegistryObject<EntityType<MagicbubbleEntity>> Magicbubble = ENTITIES.register("magicbubble", () ->
            EntityType.Builder.of(MagicbubbleEntity::new, MobCategory.MISC)
                    .sized(0.15f, 0.15f)
                    .setShouldReceiveVelocityUpdates(true)
                    .clientTrackingRange(16)
                    .updateInterval(1)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.Magicbubble.get().create(world)));

    public static final EntityObject<Sllama> Sllama = ENTITIES.registerWithEgg("sllama", () ->
            EntityType.Builder.of(Sllama::new, MobCategory.CREATURE)
                    .sized(0.9F, 1.87F).
                    clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.Sllama.get().create(world)), 0x4278e6, 0x2a60d7);
    public static final EntityObject<PlantLikeMob> Grass = ENTITIES.registerWithEgg("grass", () ->
            EntityType.Builder.of(PlantLikeMob::new, MobCategory.CREATURE)
                    .sized(0.9F, 1.87F).
                    clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.Grass.get().create(world)), 0x4278e6, 0x2a60d7);

    public static final EntityObject<OceanSlimeEntity> OceanSlimeEntity = ENTITIES.registerWithEgg("ocean_slime", () ->
            EntityType.Builder.of(OceanSlimeEntity::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(20)
                    .sized(2.04F, 2.04F)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.OceanSlimeEntity.get().create(world)), 0x4278e6, 0x2a60d7);
    public static final EntityObject<IchorSlimeEntity> IchorSlimeEntity = ENTITIES.registerWithEgg("ichor_slime", () ->
            EntityType.Builder.of(IchorSlimeEntity::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(20)
                    .sized(2.04F, 2.04F)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.IchorSlimeEntity.get().create(world)), 0xff8a2f, 0xeb5d00);
    public static final EntityObject<OriginSlimeEntity> OriginSlimeEntity = ENTITIES.registerWithEgg("origin_slime", () ->
            EntityType.Builder.of(OriginSlimeEntity::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(20)
                    .sized(2.04F, 2.04F)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.OriginSlimeEntity.get().create(world)), 0xb7b7b7, 0x8b8b8b);
    public static final EntityObject<TomatoSlimeEntity> TomatoSlimeEntity = ENTITIES.registerWithEgg("tomato_slime", () ->
            EntityType.Builder.of(TomatoSlimeEntity::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(20)
                    .sized(2.04F, 2.04F)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.TomatoSlimeEntity.get().create(world)), 0xa20000, 0x970000);
    public static final EntityObject<SulfurCubeEntity> SulfurCubeEntity = ENTITIES.registerWithEgg("sulfur_cube", () ->
            EntityType.Builder.of(SulfurCubeEntity::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(20)
                    .sized(1.96F, 1.96F).clientTrackingRange(10)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.SulfurCubeEntity.get().create(world)), 0xecf390, 0xd1af8a);

    public static final EntityObject<BoggedEntity> BoggedEntity = ENTITIES.registerWithEgg("bogged", () ->
            EntityType.Builder.of(BoggedEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.BoggedEntity.get().create(world)), 0x80916a, 0x18260d);
    public static final EntityObject<ParchedEntity> ParchedEntity = ENTITIES.registerWithEgg("parched", () ->
            EntityType.Builder.of(ParchedEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.ParchedEntity.get().create(world)), 0xddc38e, 0x4a473d);
    public static final EntityObject<EarthSlimeGolemEntity> EarthSlimeGolemEntity = ENTITIES.registerWithEgg("earth_slime_golem", () ->
            EntityType.Builder.of(EarthSlimeGolemEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.EarthSlimeGolemEntity.get().create(world)), 0x8CD782, 0x71AC63);
    public static final EntityObject<SkySlimeGolemEntity> SkySlimeGolemEntity = ENTITIES.registerWithEgg("sky_slime_golem", () ->
            EntityType.Builder.of(SkySlimeGolemEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.SkySlimeGolemEntity.get().create(world)), 0x82D7D5, 0x63ACAB);
    public static final EntityObject<OceanSlimeGolemEntity> OceanSlimeGolemEntity = ENTITIES.registerWithEgg("ocean_slime_golem", () ->
            EntityType.Builder.of(OceanSlimeGolemEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.OceanSlimeGolemEntity.get().create(world)), 0x568bf5, 0x3f76e4);
    public static final EntityObject<IchorGolemEntity> IchorGolemEntity = ENTITIES.registerWithEgg("ichor_golem", () ->
            EntityType.Builder.of(com.creeping_creeper.slimeworld.init.entity.golem.IchorGolemEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.IchorGolemEntity.get().create(world)), 0xFFB97C, 0xFF8324);
    public static final EntityObject<EnderSlimeGolemEntity> EnderSlimeGolemEntity = ENTITIES.registerWithEgg("ender_slime_golem", () ->
            EntityType.Builder.of(EnderSlimeGolemEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .clientTrackingRange(8)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.EnderSlimeGolemEntity.get().create(world)), 0xD37CFF, 0xA936ED);

    public static final RegistryObject<EntityType<SteelSlimeBossEntity>> SteelSlimeBossEntity = ENTITIES.register("steelslime_boss", () ->
            EntityType.Builder.of(SteelSlimeBossEntity::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(48)
                    .sized(2.04F, 2.04F)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.SteelSlimeBossEntity.get().create(world)));

    public static final RegistryObject<EntityType<KnightSlimeBossEntity>> KnightSlimeBossEntity = ENTITIES.register("knightslime_boss", () ->
            EntityType.Builder.of(KnightSlimeBossEntity::new, MobCategory.MONSTER)
                    .setShouldReceiveVelocityUpdates(true)
                    .setTrackingRange(48)
                    .sized(2.04F, 2.04F)
                    .setCustomClientFactory((spawnEntity, world) -> ModEntities.KnightSlimeBossEntity.get().create(world)));

    public static void registers(IEventBus bus) {
        ENTITIES.register(bus);
    }
}
