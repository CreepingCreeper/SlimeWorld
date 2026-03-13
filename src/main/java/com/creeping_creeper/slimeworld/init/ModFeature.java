package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.world.IchorFungusFeature;
import com.creeping_creeper.slimeworld.init.world.InvertedLakeFeature;
import com.creeping_creeper.slimeworld.init.world.OceanLakeFeature;
import com.creeping_creeper.slimeworld.init.world.SlimeKelpFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.world.worldgen.trees.config.SlimeFungusConfig;

public class ModFeature {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, SlimeWorld.MODID);
    public static final RegistryObject<OceanLakeFeature> oceanLakeFeature = FEATURES.register("ocean_lake", () -> new OceanLakeFeature(OceanLakeFeature.Configuration.CODEC));
    public static final RegistryObject<InvertedLakeFeature> invertedLakeFeature = FEATURES.register("inverted_lake", () -> new InvertedLakeFeature(InvertedLakeFeature.Configuration.CODEC));
    public static final RegistryObject<IchorFungusFeature> ichorFungusFeature = FEATURES.register("ichor_fungus", () -> new IchorFungusFeature(SlimeFungusConfig.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> slimeKelpFeature = FEATURES.register("slime_kelp", () -> new SlimeKelpFeature(NoneFeatureConfiguration.CODEC));

    public static void registers(IEventBus bus) {
        FEATURES.register(bus);
    }
}
