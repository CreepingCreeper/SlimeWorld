package com.creeping_creeper.slimeworld.data.key;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModResourceKeys {
    public static final ResourceLocation SLIMEWORLD_LOCATION = SlimeWorld.getResource("slimeworld");
    public static final ResourceKey<Level> SLIMEWORLD = ResourceKey.create(Registries.DIMENSION, SLIMEWORLD_LOCATION);

    public static final ResourceKey<ConfiguredFeature<?,?>> ichorSlimeFungus = key(Registries.CONFIGURED_FEATURE, "ichor_slime_fungus");
    public static final ResourceKey<ConfiguredFeature<?,?>> magicvubbleTree = key(Registries.CONFIGURED_FEATURE, "magicbubble_tree");

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

    protected static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String name) {
        return ResourceKey.create(registry, SlimeWorld.getResource(name));
    }
}
