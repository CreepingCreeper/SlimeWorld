package com.creeping_creeper.slimeworld.data;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModResourceKeys {
    public static final ResourceKey<ConfiguredFeature<?,?>> ichorSlimeFungus = key(Registries.CONFIGURED_FEATURE, "ichor_slime_fungus");

    protected static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String name) {
        return ResourceKey.create(registry, SlimeWorld.getResource(name));
    }
}
