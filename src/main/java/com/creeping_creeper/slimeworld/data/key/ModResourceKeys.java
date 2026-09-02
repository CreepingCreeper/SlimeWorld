package com.creeping_creeper.slimeworld.data.key;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class ModResourceKeys {
    public static final ResourceLocation SLIMEWORLD_LOCATION = SlimeWorld.getResource("slimeworld");
    public static final ResourceLocation UNKNOWN_AREA_LOCATION = SlimeWorld.getResource("unknown_area");

    //level
    public static final ResourceKey<Level> SLIMEWORLD = ResourceKey.create(Registries.DIMENSION, SLIMEWORLD_LOCATION);
    public static final ResourceKey<Level> UNKNOWN_AREA = ResourceKey.create(Registries.DIMENSION, UNKNOWN_AREA_LOCATION);
    //configured feature
    public static final ResourceKey<ConfiguredFeature<?,?>> ichorSlimeFungus = key(Registries.CONFIGURED_FEATURE, "ichor_slime_fungus");
    public static final ResourceKey<ConfiguredFeature<?,?>> magicvubbleTree = key(Registries.CONFIGURED_FEATURE, "magicbubble_tree");
    public static final ResourceKey<ConfiguredFeature<?,?>> oceanGeode = key(Registries.CONFIGURED_FEATURE, "ocean_geode");
    //placed feature
    public static final ResourceKey<PlacedFeature> placedOceanGeode = key(Registries.PLACED_FEATURE, "ocean_geode");
    //structure
    public static final ResourceKey<Structure> bakery = key(Registries.STRUCTURE, "bakery");
    //pool
    public static final ResourceKey<StructureTemplatePool> bakeryPool = key(Registries.TEMPLATE_POOL, "bakery");
    public static final ResourceKey<StructureTemplatePool> startPool = key(Registries.TEMPLATE_POOL, "great_wall/start");

    //biome
    public static final ResourceKey<Biome> HoneyFields = key(Registries.BIOME, "honey_fields");
    public static final ResourceKey<Biome> DeepForgottenOcean = key(Registries.BIOME, "deep_forgotten_ocean");
    public static final ResourceKey<Biome> ForgottenOcean = key(Registries.BIOME, "forgotten_ocean");
    public static final ResourceKey<Biome> River = key(Registries.BIOME, "river");
    public static final ResourceKey<Biome> RedBeach = key(Registries.BIOME, "red_beach");
    public static final ResourceKey<Biome> ClayWaste = key(Registries.BIOME, "clay_waste");
    public static final ResourceKey<Biome> EarthPlains = key(Registries.BIOME, "earth_plains");
    public static final ResourceKey<Biome> SkyHills = key(Registries.BIOME, "sky_hills");
    public static final ResourceKey<Biome> BloodForest = key(Registries.BIOME, "blood_forest");
    public static final ResourceKey<Biome> MudMeadow = key(Registries.BIOME, "mud_meadow");
    public static final ResourceKey<Biome> EnderSwamp = key(Registries.BIOME, "ender_swamp");
    public static final ResourceKey<Biome> LaceratedLand = key(Registries.BIOME, "lacerated_land");
    public static final ResourceKey<Biome> CrystalDesert = key(Registries.BIOME, "crystal_desert");
    public static final ResourceKey<Biome> SulfurSprings = key(Registries.BIOME, "sulfur_springs");
    public static final ResourceKey<Biome> CinnabarCaves = key(Registries.BIOME, "cinnabar_caves");
    public static final ResourceKey<Biome> IchorCaves = key(Registries.BIOME, "ichor_caves");
    //biome modifier
    public static final ResourceKey<BiomeModifier> lessSkeletonDesert = key(ForgeRegistries.Keys.BIOME_MODIFIERS, "less_skeleton_desert");
    public static final ResourceKey<BiomeModifier> lessSkeletonSwamp = key(ForgeRegistries.Keys.BIOME_MODIFIERS, "less_skeleton_swamp");
    public static final ResourceKey<BiomeModifier> addOceanGeode = key(ForgeRegistries.Keys.BIOME_MODIFIERS, "ocean_geode");
    public static final ResourceKey<BiomeModifier> spawnBogged = key(ForgeRegistries.Keys.BIOME_MODIFIERS, "spawn_bogged");
    public static final ResourceKey<BiomeModifier> spawnParched = key(ForgeRegistries.Keys.BIOME_MODIFIERS, "spawn_parched");
    public static final ResourceKey<BiomeModifier> spawnNetherSlime = key(ForgeRegistries.Keys.BIOME_MODIFIERS, "spawn_nether_slime");

    protected static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String name) {
        return ResourceKey.create(registry, SlimeWorld.getResource(name));
    }
}
