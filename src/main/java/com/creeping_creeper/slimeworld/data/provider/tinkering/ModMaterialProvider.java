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
        addMaterial(ModMaterialIds.kelp, 1, ORDER_BINDING, true);
        addMaterial(ModMaterialIds.oceanslime, 2, ORDER_REPAIR, true);
        addMaterial(ModMaterialIds.slimeBronze, 3, ORDER_GENERAL, false);
    }

    @Override
    public @NotNull String getName() {
        return "Slime World Materials";
    }

}
