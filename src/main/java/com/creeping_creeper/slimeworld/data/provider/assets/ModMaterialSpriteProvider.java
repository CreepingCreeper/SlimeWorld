package com.creeping_creeper.slimeworld.data.provider.assets;

import com.creeping_creeper.slimeworld.data.key.ModMaterialIds;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.tools.stats.StatlessMaterialStats;

public class ModMaterialSpriteProvider extends AbstractMaterialSpriteProvider {

    @Override
    protected void addAllMaterials() {
        buildMaterial(ModMaterialIds.kelp)
                .statType(StatlessMaterialStats.BINDING, StatlessMaterialStats.BOWSTRING).cuirass()
                .fallbacks("cloth")
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(63, 0xFF229630)
                        .addARGB(102, 0xFF24A033)
                        .addARGB(140, 0xFF27AB37)
                        .addARGB(178, 0xFF3EBD42)
                        .addARGB(216, 0xFF56CF4C)
                        .addARGB(255, 0xFF82F86D)
                        .build());

        buildMaterial(ModMaterialIds.oceanslime).slime().arrowHead().fletching()
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(63, 0xFF0e4ecf)
                        .addARGB(102, 0xFF3753dc)
                        .addARGB(140, 0xFF2961d6)
                        .addARGB(178, 0xFF3f76e4)
                        .addARGB(216, 0xFF568bf5)
                        .addARGB(255, 0xFFadc9ff)
                        .build());

        buildMaterial(MaterialIds.slimesteel)
                .meleeHarvest().ranged().armor()
                .fallbacks("slime_metal", "metal")
                .colorMapper(GreyToColorMapping.builderFromBlack()
                        .addARGB(63, 0xFF053132)
                        .addARGB(102, 0xFF165B42)
                        .addARGB(140, 0xFF378450)
                        .addARGB(178, 0xFF68A972)
                        .addARGB(216, 0xFF9FC2A7)
                        .addARGB(255, 0xFFCACCCC)
                        .build());


    }

    @Override
    public @NotNull String getName() {
        return "Slime World Materials";
    }
}
