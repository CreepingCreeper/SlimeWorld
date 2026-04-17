package com.creeping_creeper.slimeworld.init.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public record CompositeFeatureConfiguration(HolderSet<PlacedFeature> features) implements FeatureConfiguration {
    public static final Codec<CompositeFeatureConfiguration> CODEC = RecordCodecBuilder.create((i) -> i.group(ExtraCodecs.nonEmptyHolderSet(PlacedFeature.LIST_CODEC).fieldOf("features").forGetter(CompositeFeatureConfiguration::features)).apply(i, CompositeFeatureConfiguration::new));

    public @NotNull Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return this.features.stream().flatMap((f) -> f.value().getFeatures());
    }
}