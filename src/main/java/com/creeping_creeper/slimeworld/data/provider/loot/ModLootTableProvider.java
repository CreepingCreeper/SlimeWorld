package com.creeping_creeper.slimeworld.data.provider.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class ModLootTableProvider extends LootTableProvider {
    private static final Set<ResourceLocation> REQUIRED_TABLES = Set.of();

    public ModLootTableProvider(PackOutput packOutput) {
        super(packOutput, REQUIRED_TABLES, List.of(
                new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(ModEntityLootTableProvider::new, LootContextParamSets.ENTITY)
        ));
    }

}
