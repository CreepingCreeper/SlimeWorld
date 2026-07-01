package com.creeping_creeper.slimeworld.data.key;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModResourceKeys {
    public static final ResourceLocation SLIMEWORLD_LOCATION = SlimeWorld.getResource("slimeworld");
    public static final ResourceKey<Level> SLIMEWORLD = ResourceKey.create(Registries.DIMENSION, SLIMEWORLD_LOCATION);

    public static final ResourceKey<ConfiguredFeature<?,?>> ichorSlimeFungus = key(Registries.CONFIGURED_FEATURE, "ichor_slime_fungus");
    public static final ResourceKey<ConfiguredFeature<?,?>> magicvubbleTree = key(Registries.CONFIGURED_FEATURE, "magicbubble_tree");

    protected static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String name) {
        return ResourceKey.create(registry, SlimeWorld.getResource(name));
    }
}
