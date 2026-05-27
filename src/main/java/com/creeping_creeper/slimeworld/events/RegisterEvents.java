package com.creeping_creeper.slimeworld.events;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.ModTags;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.entity.*;
import com.creeping_creeper.slimeworld.library.BiomeSlimePlacementPredicate;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.entity.SlimePlacementPredicate;

@Mod.EventBusSubscriber(modid = SlimeWorld.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class RegisterEvents {
    @SubscribeEvent
    static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.OceanSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.IchorSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.OriginSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.TomatoSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.SulfurCubeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.BoggedEntity.get(), BoggedEntity.createAttributes().build());
        event.put(ModEntities.ParchedEntity.get(), ParchedEntity.createAttributes().build());
        event.put(ModEntities.SlimeGolemEntity.get(), SlimeGolemEntity.createAttributes().build());
        event.put(ModEntities.SteelSlimeBossEntity.get(), BossSlimeEntity.createAttributes().build());
        event.put(ModEntities.KnightSlimeBossEntity.get(), BossSlimeEntity.createAttributes().build());
    }

    @SubscribeEvent
    static void registerSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.OceanSlimeEntity.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OceanSlimeEntity::canSpawnHere, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.IchorSlimeEntity.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, IchorSlimeEntity::canSpawnHere, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.OriginSlimeEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new SlimePlacementPredicate<>(BlockTags.SAND), SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.TomatoSlimeEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new SlimePlacementPredicate<>(BlockTags.DIRT), SpawnPlacementRegisterEvent.Operation.OR);
        event.register(TinkerWorld.terracubeEntity.get(), null, null, new BiomeSlimePlacementPredicate<>(ModTags.Biomes.TerracubeSpawn, ModTags.Blocks.TERRACUBE_SPAWN, 8), SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.BoggedEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.ParchedEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
    }
}
