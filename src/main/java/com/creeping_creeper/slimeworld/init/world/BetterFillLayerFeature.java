package com.creeping_creeper.slimeworld.init.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;

public class BetterFillLayerFeature extends Feature<LayerConfiguration> {
    public BetterFillLayerFeature(Codec<LayerConfiguration> p_65818_) {
        super(p_65818_);
    }

    public boolean place(FeaturePlaceContext<LayerConfiguration> p_159780_) {
        BlockPos blockpos = p_159780_.origin();
        LayerConfiguration layerconfiguration = p_159780_.config();
        WorldGenLevel worldgenlevel = p_159780_.level();
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {
                int k = blockpos.getX() + i;
                int l = blockpos.getZ() + j;
                int i1 = worldgenlevel.getMinBuildHeight() + layerconfiguration.height;
                blockpos$mutableblockpos.set(k, i1, l);
                worldgenlevel.setBlock(blockpos$mutableblockpos, layerconfiguration.state, 2);
            }
        }

        return true;
    }
}
