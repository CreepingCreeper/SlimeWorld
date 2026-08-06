package com.creeping_creeper.slimeworld.events;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.entity.PlantLikeMob;
import com.creeping_creeper.slimeworld.init.entity.boss.BaseBossSlimeEntity;
import com.creeping_creeper.slimeworld.init.entity.golem.BaseSlimeGolemEntity;
import com.creeping_creeper.slimeworld.init.entity.monster.BoggedEntity;
import com.creeping_creeper.slimeworld.init.entity.monster.IchorSlimeEntity;
import com.creeping_creeper.slimeworld.init.entity.monster.OceanSlimeEntity;
import com.creeping_creeper.slimeworld.init.entity.monster.ParchedEntity;
import com.creeping_creeper.slimeworld.library.BiomePlacementPredicate;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.horse.Llama;
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
        event.put(ModEntities.Sllama.get(), Llama.createAttributes().build());
        event.put(ModEntities.Grass.get(), PlantLikeMob.createAttributes().build());

        event.put(ModEntities.OceanSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.IchorSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.OriginSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.TomatoSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.SulfurCubeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.BoggedEntity.get(), BoggedEntity.createAttributes().build());
        event.put(ModEntities.ParchedEntity.get(), ParchedEntity.createAttributes().build());
        event.put(ModEntities.EarthSlimeGolemEntity.get(), BaseSlimeGolemEntity.createAttributes().build());
        event.put(ModEntities.SkySlimeGolemEntity.get(), BaseSlimeGolemEntity.createAttributes().build());
        event.put(ModEntities.OceanSlimeGolemEntity.get(), BaseSlimeGolemEntity.createAttributes().build());
        event.put(ModEntities.IchorSlimeGolemEntity.get(), BaseSlimeGolemEntity.createAttributes().build());
        event.put(ModEntities.EnderSlimeGolemEntity.get(), BaseSlimeGolemEntity.createAttributes().build());

        event.put(ModEntities.SteelSlimeBossEntity.get(), BaseBossSlimeEntity.createAttributes().build());
        event.put(ModEntities.KnightSlimeBossEntity.get(), BaseBossSlimeEntity.createAttributes().build());
    }

    @SubscribeEvent
    static void registerSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.Sllama.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BiomePlacementPredicate<>(null, ModTags.Blocks.ANIMALS_SPAWNABLE, 1), SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.Grass.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BiomePlacementPredicate<>(null, ModTags.Blocks.ANIMALS_SPAWNABLE, 1), SpawnPlacementRegisterEvent.Operation.OR);

        event.register(ModEntities.OceanSlimeEntity.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OceanSlimeEntity::canSpawnHere, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.IchorSlimeEntity.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, IchorSlimeEntity::canSpawnHere, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.OriginSlimeEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new SlimePlacementPredicate<>(BlockTags.SAND), SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.TomatoSlimeEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new SlimePlacementPredicate<>(BlockTags.DIRT), SpawnPlacementRegisterEvent.Operation.OR);
        event.register(TinkerWorld.terracubeEntity.get(), null, null, new BiomePlacementPredicate<>(ModTags.Biomes.TERRACUBE_SPAWN, ModTags.Blocks.TERRACUBE_SPAWN, 8), SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.SulfurCubeEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BiomePlacementPredicate<>(ModTags.Biomes.SULFUR_CUBE_SPAWN, ModTags.Blocks.SULFUR_FEATURE_BASE, 1), SpawnPlacementRegisterEvent.Operation.OR);

        event.register(ModEntities.BoggedEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.ParchedEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
    }
}
