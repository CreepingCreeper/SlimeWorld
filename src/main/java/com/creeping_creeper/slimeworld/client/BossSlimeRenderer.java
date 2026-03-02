package com.creeping_creeper.slimeworld.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;
import slimeknights.tconstruct.world.client.SlimeArmorLayer;

public class BossSlimeRenderer extends SlimeRenderer {
    private final ResourceLocation slime;

    public BossSlimeRenderer(EntityRendererProvider.Context context, ResourceLocation slime) {
        super(context);
        this.slime = slime;
        addLayer(new SlimeArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelSet(), false));
    }

    @Override
    public ResourceLocation getTextureLocation(Slime entity) {
        return slime;
    }
}
