package com.creeping_creeper.slimeworld.data.provider.tinkering;

import com.creeping_creeper.slimeworld.data.key.ModMaterialIds;
import com.creeping_creeper.slimeworld.data.key.ModModifierIds;
import com.creeping_creeper.slimeworld.init.ModModifiers;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;

import static slimeknights.tconstruct.library.materials.MaterialRegistry.*;

public class ModTraitsProvider extends AbstractMaterialTraitDataProvider {
    public ModTraitsProvider(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }

    @Override
    protected void addMaterialTraits() {
        addTraits(ModMaterialIds.kelp, RANGED, ModModifierIds.waving);
        addTraits(ModMaterialIds.kelp, ARMOR, ModModifierIds.undercurrent);
        addTraits(ModMaterialIds.oceanslime, AMMO, ModModifiers.sputtering);
        addTraits(ModMaterialIds.slimeBronze, MELEE_HARVEST, ModModifierIds.overwash);
    }

    @Override
    public @NotNull String getName() {
        return "Slime World Material Traits";
    }
}
