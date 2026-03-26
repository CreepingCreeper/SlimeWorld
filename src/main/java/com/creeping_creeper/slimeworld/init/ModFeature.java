package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.world.*;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.world.worldgen.trees.config.SlimeFungusConfig;

public class ModFeature {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, SlimeWorld.MODID);
    private static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, SlimeWorld.MODID);

    public static final RegistryObject<OceanLakeFeature> oceanLakeFeature = FEATURES.register("ocean_lake", () -> new OceanLakeFeature(OceanLakeFeature.Configuration.CODEC));
    public static final RegistryObject<InvertedLakeFeature> invertedLakeFeature = FEATURES.register("inverted_lake", () -> new InvertedLakeFeature(InvertedLakeFeature.Configuration.CODEC));
    public static final RegistryObject<IchorFungusFeature> ichorFungusFeature = FEATURES.register("ichor_fungus", () -> new IchorFungusFeature(SlimeFungusConfig.CODEC));

    public static final RegistryObject<Codec<? extends BiomeModifier>> MODIFY_SPAWNS =
            BIOME_MODIFIER_SERIALIZERS.register("modify_spawns", () -> ModifySpawnsBiomeModifier.CODEC);

    public static void registers(IEventBus bus) {
        FEATURES.register(bus);
        BIOME_MODIFIER_SERIALIZERS.register(bus);
    }
}
