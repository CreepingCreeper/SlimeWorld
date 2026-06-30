package com.creeping_creeper.slimeworld.library;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class BiomeSlimePlacementPredicate <T extends Slime> implements SpawnPlacements.SpawnPredicate<T> {
    private TagKey<Biome> biomeTag;
    private TagKey<Block> blockTag;
    private int chance;

    @Override
    public boolean test(EntityType<T> entityType, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (reason == MobSpawnType.SPAWNER) {
            return true;
        }
        return world.getBiome(pos).is(biomeTag) && world.getBlockState(pos.below()).is(blockTag) && random.nextInt(chance) == 0;
    }
    public BiomeSlimePlacementPredicate(TagKey<Biome> biomeTag, TagKey<Block> blockTag, int chance) {
        this.biomeTag = biomeTag;
        this.blockTag = blockTag;
        this.chance = chance;
    }
}
