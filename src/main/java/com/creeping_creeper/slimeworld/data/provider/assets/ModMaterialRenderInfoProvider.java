package com.creeping_creeper.slimeworld.data.provider.assets;

import com.creeping_creeper.slimeworld.data.key.ModMaterialIds;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialRenderInfoProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

public class ModMaterialRenderInfoProvider extends AbstractMaterialRenderInfoProvider {
    public ModMaterialRenderInfoProvider(PackOutput packOutput, @Nullable AbstractMaterialSpriteProvider materialSprites, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, materialSprites, existingFileHelper);
    }

    @Override
    protected void addMaterialRenderInfo() {
        buildRenderInfo(ModMaterialIds.kelp).color(0xFF56CF4C);
        buildRenderInfo(ModMaterialIds.oceanslime);
        redirect(MaterialVariantId.create(MaterialIds.slimeball, "ocean"), ModMaterialIds.oceanslime);
        buildRenderInfo(ModMaterialIds.slimeBronze).color(0xFF9FC2A7).fallbacks("slime_metal", "metal");
    }

    @Override
    public @NotNull String getName() {
        return "Slime World Material Render Info Provider";
    }
}
