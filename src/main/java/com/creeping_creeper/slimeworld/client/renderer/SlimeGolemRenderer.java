package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.client.model.SlimeGolemModel;
import com.creeping_creeper.slimeworld.init.entity.BaseSlimeGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("removal")
public class SlimeGolemRenderer extends HumanoidMobRenderer<BaseSlimeGolemEntity, SlimeGolemModel<BaseSlimeGolemEntity>>{
    private static final ResourceLocation SKELETON_LOCATION = new ResourceLocation("textures/entity/skeleton/skeleton.png");

    public SlimeGolemRenderer(EntityRendererProvider.Context p_174380_) {
        this(p_174380_, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
    }

    public SlimeGolemRenderer(EntityRendererProvider.Context context, ModelLayerLocation skeletonLayer, ModelLayerLocation innerModelLayer, ModelLayerLocation outerModelLayer) {
        super(context, new SlimeGolemModel<>(context.bakeLayer(skeletonLayer)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new SlimeGolemModel<>(context.bakeLayer(innerModelLayer)), new SlimeGolemModel<>(context.bakeLayer(outerModelLayer)), context.getModelManager()));
    }

    @Override
    public void render(@NotNull BaseSlimeGolemEntity entity, float p_117789_, float p_117790_, @NotNull PoseStack p_117791_, @NotNull MultiBufferSource p_117792_, int p_117793_) {
        super.render(entity, p_117789_, p_117790_, p_117791_, p_117792_, p_117793_);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull BaseSlimeGolemEntity entity) {
        return SKELETON_LOCATION;
    }
}
