package com.creeping_creeper.slimeworld.init.world;

import com.creeping_creeper.slimeworld.data.key.ModResourceKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class MagicbubbleTreeGrower extends AbstractTreeGrower {
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource source, boolean hasFlowers) {
        return ModResourceKeys.magicvubbleTree;
    }
}
