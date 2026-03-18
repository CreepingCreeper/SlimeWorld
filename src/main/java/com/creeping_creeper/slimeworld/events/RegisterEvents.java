package com.creeping_creeper.slimeworld.events;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.ModTags;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.entity.*;
import com.creeping_creeper.slimeworld.library.BiomeSlimePlacementPredicate;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.MobSpawnSettingsBuilder;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.entity.SlimePlacementPredicate;

import java.util.List;

@Mod.EventBusSubscriber(modid = SlimeWorld.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class RegisterEvents {
    @SubscribeEvent
    static void entityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.oceanSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.ichorSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.originSlimeEntity.get(), Monster.createMonsterAttributes().build());
        event.put(ModEntities.boggedEntity.get(), BoggedEntity.createAttributes().build());
        event.put(ModEntities.parchedEntity.get(), ParchedEntity.createAttributes().build());
        event.put(ModEntities.steelSlimeBossEntity.get(), BossSlimeEntity.createAttributes().build());
    }

    @SubscribeEvent
    static void registerSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.oceanSlimeEntity.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, OceanSlimeEntity::canSpawnHere, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.ichorSlimeEntity.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, IchorSlimeEntity::canSpawnHere, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.originSlimeEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new SlimePlacementPredicate<>(BlockTags.SAND), SpawnPlacementRegisterEvent.Operation.OR);
        event.register(TinkerWorld.terracubeEntity.get(), null, null, new BiomeSlimePlacementPredicate<>(ModTags.Biomes.TerracubeSpawn, ModTags.Blocks.TerracubeSpawn, 8), SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.boggedEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(ModEntities.parchedEntity.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
    }

    public record RemoveSpawnsBiomeModifier(HolderSet<Biome> biomes, HolderSet<EntityType<?>> entityTypes) implements BiomeModifier {
        public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (phase == Phase.MODIFY) {
                if (biome.is(Tags.Biomes.IS_SWAMP)) {
                    MobSpawnSettingsBuilder spawnBuilder = builder.getMobSpawnSettings();
                    for (MobCategory category : MobCategory.values()) {
                        List<MobSpawnSettings.SpawnerData> spawns = spawnBuilder.getSpawner(category);
                        spawns.removeIf((spawnerData) -> this.entityTypes.contains(ForgeRegistries.ENTITY_TYPES.getHolder(EntityType.SKELETON).get()));
                        spawns.add(new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 70, 4, 4));
                    }
                } else if (biome.is(Tags.Biomes.IS_DESERT)) {
                    MobSpawnSettingsBuilder spawnBuilder = builder.getMobSpawnSettings();
                    for (MobCategory category : MobCategory.values()) {
                        List<MobSpawnSettings.SpawnerData> spawns = spawnBuilder.getSpawner(category);
                        spawns.removeIf((spawnerData) -> this.entityTypes.contains(ForgeRegistries.ENTITY_TYPES.getHolder(EntityType.SKELETON).get()));
                        spawns.add(new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 50, 4, 4));
                    }
                }
            }
        }

        public Codec<? extends BiomeModifier> codec() {
            return (Codec) ForgeMod.REMOVE_SPAWNS_BIOME_MODIFIER_TYPE.get();
        }
    }
}
