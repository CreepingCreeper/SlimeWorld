package com.creeping_creeper.slimeworld.init.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.*;

import java.util.List;

public record ModifySpawnsBiomeModifier(HolderSet<Biome> biomes, MobSpawnSettings.SpawnerData spawners) implements BiomeModifier {

    public void modify(Holder<Biome> biome, BiomeModifier.Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.MODIFY && this.biomes.contains(biome)) {
            MobSpawnSettingsBuilder spawns = builder.getMobSpawnSettings();
            EntityType<?> spawnerType = spawners.type;
            for(MobCategory category : MobCategory.values()) {
                List<MobSpawnSettings.SpawnerData> spawnlist = spawns.getSpawner(category);
                spawnlist.removeIf((spawnerData) -> spawnerData.type == spawnerType);
            }
            spawns.addSpawn(spawnerType.getCategory(), spawners);
        }

    }

    public Codec<? extends BiomeModifier> codec() {
        return CODEC;
    }

    public static final Codec<ModifySpawnsBiomeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(ModifySpawnsBiomeModifier::biomes),
            MobSpawnSettings.SpawnerData.CODEC.fieldOf("spawners").forGetter(ModifySpawnsBiomeModifier::spawners)
    ).apply(instance, ModifySpawnsBiomeModifier::new));
}
