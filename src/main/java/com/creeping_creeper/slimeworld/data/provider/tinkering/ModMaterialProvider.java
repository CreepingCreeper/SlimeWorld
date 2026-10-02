package com.creeping_creeper.slimeworld.data.provider.tinkering;

import com.creeping_creeper.slimeworld.data.key.ModMaterialIds;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;

public class ModMaterialProvider extends AbstractMaterialDataProvider {
    public ModMaterialProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addMaterials() {
        material(ModMaterialIds.kelp).tier(1).sort(ORDER_BINDING).craftable();
        material(ModMaterialIds.oceanslime).tier(2).sort(ORDER_REPAIR).craftable();
        material(ModMaterialIds.slimeBronze).tier(3).sort(ORDER_GENERAL).craftable();
    }

    @Override
    public @NotNull String getName() {
        return "Slime World Materials";
    }

}
