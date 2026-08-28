package com.creeping_creeper.slimeworld.data.provider.tags;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModMaterialIds;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractMaterialTagProvider;

public class ModMaterialTagsProvider extends AbstractMaterialTagProvider {
    public ModMaterialTagsProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(TinkerTags.Materials.HARVEST).add(ModMaterialIds.slimeBronze);
        tag(TinkerTags.Materials.HEAVY).add(ModMaterialIds.slimeBronze);
    }

    @Override
    public @NotNull String getName() {
        return "Slime World Material Tag Provider";
    }
}
