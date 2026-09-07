package com.creeping_creeper.slimeworld.data.provider.tinkering;

import com.creeping_creeper.slimeworld.data.key.ModMaterialIds;
import com.creeping_creeper.slimeworld.data.key.ModModifierIds;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialTraitDataProvider;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.data.ModifierIds;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.stats.RepairStats;
import slimeknights.tconstruct.tools.stats.SlimeStats;

import static slimeknights.tconstruct.library.materials.MaterialRegistry.*;

public class ModTraitsProvider extends AbstractMaterialTraitDataProvider {
    public ModTraitsProvider(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }

    @Override
    protected void addMaterialTraits() {
        addTraits(ModMaterialIds.kelp, RANGED, ModModifierIds.waving);
        addTraits(ModMaterialIds.kelp, ARMOR, ModModifierIds.undercurrent);
        MaterialStatsId laces = RepairStats.LACES.getId();
        addTraits(ModMaterialIds.kelp, laces, ModModifierIds.undercurrent);
        addTraits(ModMaterialIds.oceanslime, AMMO, ModModifierIds.sputtering);
        addTraits(ModMaterialIds.oceanslime, SlimeStats.ID, ModModifierIds.undercurrent, ModifierIds.overslimeFriend);
        addTraits(ModMaterialIds.slimeBronze, MELEE_HARVEST, ModModifierIds.overwash, TinkerModifiers.overslime.getId());
        addTraits(ModMaterialIds.slimeBronze, RANGED, ModModifierIds.overload, TinkerModifiers.overslime.getId());
        addTraits(ModMaterialIds.slimeBronze, ARMOR, ModModifierIds.overload, TinkerModifiers.overslime.getId());
    }

    @Override
    public @NotNull String getName() {
        return "Slime World Material Traits";
    }
}
