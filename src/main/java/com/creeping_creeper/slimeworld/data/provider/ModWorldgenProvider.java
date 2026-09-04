package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.data.key.ModResourceKeys;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.ModFluids;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModMisc;
import com.creeping_creeper.slimeworld.init.world.ModifySpawnsBiomeModifier;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.*;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.holdersets.AndHolderSet;
import net.minecraftforge.registries.holdersets.OrHolderSet;
import slimeknights.tconstruct.common.registration.GeodeItemObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.world.TinkerWorld;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.minecraft.core.HolderSet.direct;

@SuppressWarnings({"ConstantConditions", "deprecation"})

public class ModWorldgenProvider {
    private ModWorldgenProvider() {}

    /** Registers this provider with the data generator */
    public static void register(RegistrySetBuilder builder) {
        builder.add(Registries.CONFIGURED_FEATURE, ModWorldgenProvider::registerConfiguredFeatures);
        builder.add(Registries.PLACED_FEATURE, ModWorldgenProvider::registerPlacedFeatures);
        builder.add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModWorldgenProvider::registerBiomeModifiers);
    }
    private static void registerConfiguredFeatures(BootstapContext<ConfiguredFeature<?,?>> context) {
        configureGeode(context, ModResourceKeys.OceanGeode, ModItems.OceanGeode, BlockStateProvider.simple(Blocks.CALCITE), BlockStateProvider.simple(Blocks.MUD), ModItems.BronzeCluster,
                new GeodeLayerSettings(1.7D, 2.2D, 3.2D, 5.2D), new GeodeCrackSettings(0.45, 1.0D, 2), UniformInt.of(4, 10), UniformInt.of(3, 4), UniformInt.of(1, 2), 16, 10000,
                true);

        register(context, ModResourceKeys.EarthSlimeLake, Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(TinkerFluids.earthSlime.getBlock()), BlockStateProvider.simple(TinkerWorld.congealedSlime.get(SlimeType.EARTH))));
        register(context, ModResourceKeys.SkySlimeLake, Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(TinkerFluids.skySlime.getBlock()), BlockStateProvider.simple(TinkerWorld.congealedSlime.get(SlimeType.SKY))));
        register(context, ModResourceKeys.OceanSlimeLake, ModMisc.OceanLake.get(), new LakeFeature.Configuration(BlockStateProvider.simple(ModFluids.OceanSlime.getBlock()), BlockStateProvider.simple(ModItems.OceanCongealedSlime.get())));
        register(context, ModResourceKeys.IchorLake, ModMisc.InvertedLake.get(), new LakeFeature.Configuration(BlockStateProvider.simple(TinkerFluids.ichor.getBlock()), BlockStateProvider.simple(TinkerWorld.congealedSlime.get(SlimeType.ICHOR))));
        register(context, ModResourceKeys.MagmaLake, Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(TinkerFluids.magma.getBlock()), BlockStateProvider.simple(Blocks.MAGMA_BLOCK)));
        register(context, ModResourceKeys.EnderSlimeLake, Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(TinkerFluids.enderSlime.getBlock()), BlockStateProvider.simple(TinkerWorld.congealedSlime.get(SlimeType.ENDER))));
        register(context, ModResourceKeys.HoneyLake, Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(TinkerFluids.honey.getBlock()), BlockStateProvider.simple(Blocks.HONEY_BLOCK)));
        register(context, ModResourceKeys.MudLake, Feature.LAKE, new LakeFeature.Configuration(BlockStateProvider.simple(ModFluids.LiquidMud.getBlock()), BlockStateProvider.simple(Blocks.MUD)));
        register(context, ModResourceKeys.SulfurPool, ModMisc.SulfurPool.get(), new LakeFeature.Configuration(BlockStateProvider.simple(Blocks.WATER), BlockStateProvider.simple(ModItems.Sulfur.get())));

        register(context, ModResourceKeys.ShallowSkySlimeLake, ModMisc.ShallowLake.get(), new LakeFeature.Configuration(BlockStateProvider.simple(TinkerFluids.skySlime.getBlock()), BlockStateProvider.simple(TinkerWorld.congealedSlime.get(SlimeType.SKY))));

    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?,?>> context, ResourceKey<ConfiguredFeature<?,?>> key, F feature, FC config) {
        context.register(key, new ConfiguredFeature<>(feature, config));
    }

    private static void configureGeode(BootstapContext<ConfiguredFeature<?,?>> context, ResourceKey<ConfiguredFeature<?,?>> key, GeodeItemObject geode,
                                       BlockStateProvider middleLayer, BlockStateProvider outerLayer, @Nullable Supplier<? extends Block> extraCluster, GeodeLayerSettings layerSettings, GeodeCrackSettings crackSettings,
                                       IntProvider outerWall, IntProvider distributionPoints, IntProvider pointOffset, int genOffset, int invalidBlocks, boolean waterLogged) {
        // allow adding in an extra cluster type to the geode
        Stream<BlockState> buds = Arrays.stream(GeodeItemObject.BudSize.values()).map(type -> geode.getBud(type).defaultBlockState().setValue(AmethystClusterBlock.WATERLOGGED, waterLogged));
        if (extraCluster != null) {
            buds = Stream.concat(buds, Stream.of(extraCluster.get().defaultBlockState().setValue(AmethystClusterBlock.WATERLOGGED, waterLogged)));
        }
        register(context, key, Feature.GEODE, new GeodeConfiguration(
                new GeodeBlockSettings(BlockStateProvider.simple(Blocks.AIR),
                        BlockStateProvider.simple(geode.getBlock()),
                        BlockStateProvider.simple(geode.getBudding()),
                        middleLayer, outerLayer,
                        buds.toList(),
                        BlockTags.FEATURES_CANNOT_REPLACE, BlockTags.GEODE_INVALID_BLOCKS),
                layerSettings, crackSettings, 0.335, 0.083, true, outerWall, distributionPoints, pointOffset, -genOffset, genOffset, 0.05D, invalidBlocks)
        );
    }

    private static void registerPlacedFeatures(BootstapContext<PlacedFeature> context) {
        // geodes
        register(context, ModResourceKeys.placedOceanGeode, ModResourceKeys.OceanGeode, RarityFilter.onAverageOnceEvery(128), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(80), VerticalAnchor.aboveBottom(112)), BiomeFilter.biome());
        // lakes
        register(context, ModResourceKeys.placedEarthSlimeLake, ModResourceKeys.EarthSlimeLake, RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome());
        register(context, ModResourceKeys.placedSkySlimeLake, ModResourceKeys.SkySlimeLake, RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome());
        register(context, ModResourceKeys.placedOceanSlimeLake, ModResourceKeys.OceanSlimeLake, RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome());
        register(context, ModResourceKeys.placedIchorLake, ModResourceKeys.IchorLake, RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(5), VerticalAnchor.aboveBottom(63)), EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.allOf(BlockPredicate.not(BlockPredicate.ONLY_IN_AIR_PREDICATE), BlockPredicate.insideWorld(new BlockPos(0, -5, 0))), 32), SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -5), BiomeFilter.biome());
        register(context, ModResourceKeys.placedMagmaLake, ModResourceKeys.MagmaLake, RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome());
        register(context, ModResourceKeys.placedEnderSlimeLake, ModResourceKeys.EnderSlimeLake, RarityFilter.onAverageOnceEvery(100), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome());
        register(context, ModResourceKeys.placedHoneyLake, ModResourceKeys.HoneyLake, RarityFilter.onAverageOnceEvery(20), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome());
        register(context, ModResourceKeys.placedMudLake, ModResourceKeys.MudLake, RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome());
        register(context, ModResourceKeys.placedSulfurPool, ModResourceKeys.SulfurPool, RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome());

    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?,?>> configured, PlacementModifier... placement) {
        context.register(key, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configured), List.of(placement)));
    }

    private static void registerBiomeModifiers(BootstapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placed = context.lookup(Registries.PLACED_FEATURE);
        // geodes
        context.register(ModResourceKeys.addOceanGeode, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(and(biomes.getOrThrow(BiomeTags.IS_OVERWORLD), biomes.getOrThrow(BiomeTags.IS_OCEAN)), direct(placed.getOrThrow(ModResourceKeys.placedOceanGeode)), GenerationStep.Decoration.LOCAL_MODIFICATIONS));
        // spawns
        context.register(ModResourceKeys.lessSkeletonDesert, new ModifySpawnsBiomeModifier(biomes.getOrThrow(Tags.Biomes.IS_DESERT), new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 4, 4, 50)));
        context.register(ModResourceKeys.lessSkeletonSwamp, new ModifySpawnsBiomeModifier(biomes.getOrThrow(Tags.Biomes.IS_SWAMP), new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 4, 4, 70)));
        context.register(ModResourceKeys.spawnBogged, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(biomes.getOrThrow(Tags.Biomes.IS_SWAMP), List.of(new MobSpawnSettings.SpawnerData(ModEntities.BoggedEntity.get(), 4, 4, 30))));
        context.register(ModResourceKeys.spawnParched, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(biomes.getOrThrow(Tags.Biomes.IS_DESERT), List.of(new MobSpawnSettings.SpawnerData(ModEntities.ParchedEntity.get(), 4, 4, 50))));
        context.register(ModResourceKeys.spawnNetherSlime, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(biomes.getOrThrow(BiomeTags.IS_NETHER), List.of(new MobSpawnSettings.SpawnerData(ModEntities.IchorSlimeEntity.get(), 200, 2, 4))));
    }

    @SafeVarargs
    private static <T> AndHolderSet<T> and(HolderSet<T>... sets) {
        return new AndHolderSet<>(List.of(sets));
    }

    @SafeVarargs
    private static <T> OrHolderSet<T> or(HolderSet<T>... sets) {
        return new OrHolderSet<>(List.of(sets));
    }
}
