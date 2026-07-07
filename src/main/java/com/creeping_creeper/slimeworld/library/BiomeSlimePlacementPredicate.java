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
import org.jetbrains.annotations.NotNull;

public record BiomeSlimePlacementPredicate<T extends Slime>(TagKey<Biome> biomeTag, TagKey<Block> blockTag, int chance) implements SpawnPlacements.SpawnPredicate<T> {
    @Override
    public boolean test(@NotNull EntityType<T> entityType, ServerLevelAccessor world, @NotNull MobSpawnType reason, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (world.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }
        if (reason == MobSpawnType.SPAWNER) {
            return true;
        }
        return world.getBiome(pos).is(biomeTag) && world.getBlockState(pos.below()).is(blockTag) && random.nextInt(chance) == 0;
    }
}
