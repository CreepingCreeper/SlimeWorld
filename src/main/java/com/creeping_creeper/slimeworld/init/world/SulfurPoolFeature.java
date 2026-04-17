package com.creeping_creeper.slimeworld.init.world;

import com.creeping_creeper.slimeworld.init.ModItems;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.LakeFeature;

public class SulfurPoolFeature extends LakeFeature {
    public SulfurPoolFeature(Codec<Configuration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Configuration> context) {
        boolean place = super.place(context);
        findSolidUnderLake(context.level(), context.origin().offset(8, 0, 8));
        return place;
    }

    private void findSolidUnderLake(WorldGenLevel level, BlockPos center) {
        int maxHorizontalRadius = 4;
        int maxDownSearch = 8;

        for (int r = 0; r <= maxHorizontalRadius; r++) {
            for (BlockPos pos : BlockPos.betweenClosed(center.offset(-r, 0, -r), center.offset(r, 0, r))) {
                if (distSq(pos.getX() - center.getX(), pos.getZ() - center.getZ()) != r * r)
                    continue;
                for (int down = 0; down < maxDownSearch; down++) {
                    if (!level.getFluidState(pos.below(down)).isEmpty()) {
                        if (level.getBlockState(pos.below(down + 1)).isSolid()) {
                             level.setBlock(pos.below(down + 1), getPotentSulfur(level).defaultBlockState(), 2);
                             return;
                        }
                    }
                }
            }
        }
    }

    private Block getPotentSulfur(WorldGenLevel level){
        return switch (level.getRandom().nextInt(5)){
            case 0 -> ModItems.PotentSulfurNausea.get();
            case 1 -> ModItems.PotentSulfurBlindness.get();
            case 2 -> ModItems.PotentSulfurWeakness.get();
            case 3 -> ModItems.PotentSulfurRegeneration.get();
            default -> ModItems.PotentSulfurStrength.get();
        };
    }

    private int distSq(int x, int z) {
        return x * x + z * z;
    }
}
