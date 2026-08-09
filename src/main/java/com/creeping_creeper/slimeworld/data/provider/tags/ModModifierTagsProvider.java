package com.creeping_creeper.slimeworld.data.provider.tags;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModModifierIds;
import com.creeping_creeper.slimeworld.init.ModModifiers;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierTagProvider;

public class ModModifierTagsProvider extends AbstractModifierTagProvider {
    public ModModifierTagsProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(TinkerTags.Modifiers.OVERSLIME_FRIEND).add(ModModifierIds.overwash, ModModifiers.overTomato.getId());
    }

    @Override
    public @NotNull String getName() {
        return "Slime World Modifier Tag Provider";
    }
}
