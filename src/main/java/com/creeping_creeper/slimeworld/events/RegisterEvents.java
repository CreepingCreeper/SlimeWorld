package com.creeping_creeper.slimeworld.events;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.ModTags;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.entity.IchorSlimeEntity;
import com.creeping_creeper.slimeworld.init.entity.OceanSlimeEntity;
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
        event.put(ModEntities.oceanSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.ichorSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.originSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.steelSlimeBossEntity.get(), Monster.createMonsterAttributes().build());
    }

    @SubscribeEvent
    static void registerSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.oceanSlimeEntity.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OceanSlimeEntity::canSpawnHere, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.ichorSlimeEntity.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, IchorSlimeEntity::canSpawnHere, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.originSlimeEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new SlimePlacementPredicate<>(BlockTags.SAND), SpawnPlacementRegisterEvent.Operation.OR);
        event.register(TinkerWorld.terracubeEntity.get(), null, null, new BiomeSlimePlacementPredicate<>(ModTags.Biomes.TerracubeSpawn, ModTags.Blocks.TerracubeSpawn, 8), SpawnPlacementRegisterEvent.Operation.OR);
    }
}
