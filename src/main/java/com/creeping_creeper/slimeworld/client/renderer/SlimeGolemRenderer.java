package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.client.layer.SlimeGolemClothingLayer;
import com.creeping_creeper.slimeworld.client.model.SlimeGolemModel;
import com.creeping_creeper.slimeworld.init.entity.golem.BaseSlimeGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SlimeGolemRenderer extends HumanoidMobRenderer<BaseSlimeGolemEntity, SlimeGolemModel<BaseSlimeGolemEntity>>{
    private ResourceLocation slime;

    public SlimeGolemRenderer(EntityRendererProvider.Context context, ResourceLocation slime) {
        this(context, ModelLayers.SKELETON, ModelLayers.SKELETON_INNER_ARMOR, ModelLayers.SKELETON_OUTER_ARMOR);
        this.slime = slime.withSuffix(".png");
        this.addLayer(new SlimeGolemClothingLayer<>(this, context.getModelSet(), slime.withSuffix("_cloth.png")));
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
        return slime;
    }

    public record SlimeGolemFactory(ResourceLocation slime) implements EntityRendererProvider<BaseSlimeGolemEntity> {
        @Override
        public @NotNull EntityRenderer<BaseSlimeGolemEntity> create(@NotNull Context context) {
            return new SlimeGolemRenderer(context, slime);
        }
    }
}
