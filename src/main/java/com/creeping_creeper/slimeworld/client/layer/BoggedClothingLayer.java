package com.creeping_creeper.slimeworld.client.layer;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.client.ModModelLayers;
import com.creeping_creeper.slimeworld.init.entity.monster.BoggedEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BoggedClothingLayer<T extends BoggedEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation BOGGED_CLOTHES_LOCATION = SlimeWorld.getResource("textures/entity/bogged_overlay.png");
    private final SkeletonModel<T> layerModel;

    public BoggedClothingLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.layerModel = new SkeletonModel<>(modelSet.bakeLayer(ModModelLayers.BoggedOuterLayer));
    }

    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, @NotNull T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        coloredCutoutModelCopyLayerRender(this.getParentModel(), this.layerModel, BOGGED_CLOTHES_LOCATION, poseStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks, 1.0F, 1.0F, 1.0F);
    }
}
