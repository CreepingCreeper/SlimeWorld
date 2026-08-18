package com.creeping_creeper.slimeworld.client.layer;

import com.creeping_creeper.slimeworld.client.ModModelLayers;
import com.creeping_creeper.slimeworld.client.model.SlimeGolemModel;
import com.creeping_creeper.slimeworld.init.entity.golem.BaseSlimeGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SlimeGolemClothingLayer<T extends BaseSlimeGolemEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final ResourceLocation slime;
    private final SlimeGolemModel<T> layerModel;

    public SlimeGolemClothingLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet, ResourceLocation slime) {
        super(renderer);
        this.slime = slime;
        this.layerModel = new SlimeGolemModel<>(modelSet.bakeLayer(ModModelLayers.BoggedOuterLayer));
    }

    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        coloredTranslucentModelCopyLayerRender(this.getParentModel(), this.layerModel, slime, poseStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks);
    }

    protected static <T extends LivingEntity> void coloredTranslucentModelCopyLayerRender(@NotNull EntityModel<T> p_117360_, @NotNull EntityModel<T> p_117361_, ResourceLocation p_117362_, PoseStack p_117363_, MultiBufferSource p_117364_, int p_117365_, T p_117366_, float p_117367_, float p_117368_, float p_117369_, float p_117370_, float p_117371_, float p_117372_) {
        if (!p_117366_.isInvisible()) {
            p_117360_.copyPropertiesTo(p_117361_);
            p_117361_.prepareMobModel(p_117366_, p_117367_, p_117368_, p_117372_);
            p_117361_.setupAnim(p_117366_, p_117367_, p_117368_, p_117369_, p_117370_, p_117371_);
            renderColoredTranslucentModel(p_117361_, p_117362_, p_117363_, p_117364_, p_117365_, p_117366_);
        }

    }

    protected static <T extends LivingEntity> void renderColoredTranslucentModel(EntityModel<T> p_117377_, @NotNull ResourceLocation p_117378_, PoseStack p_117379_, MultiBufferSource p_117380_, int p_117381_, T p_117382_) {
        VertexConsumer vertexconsumer = p_117380_.getBuffer(RenderType.entityTranslucent(p_117378_));
        p_117377_.renderToBuffer(p_117379_, vertexconsumer, p_117381_, LivingEntityRenderer.getOverlayCoords(p_117382_, 0.0F), (float) 1.0, (float) 1.0, (float) 1.0, 1.0F);
    }
}
