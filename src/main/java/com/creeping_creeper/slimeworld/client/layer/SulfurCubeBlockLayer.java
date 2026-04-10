package com.creeping_creeper.slimeworld.client.layer;

import com.creeping_creeper.slimeworld.init.entity.SulfurCubeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;

@OnlyIn(Dist.CLIENT)
public class SulfurCubeBlockLayer<T extends SulfurCubeEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
    private final BlockRenderDispatcher blockRenderer;

    public SulfurCubeBlockLayer(RenderLayerParent<T, M> renderer, EntityModelSet modelSet, BlockRenderDispatcher blockRenderer) {
        super(renderer);
        this.blockRenderer = blockRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        BlockState blockState = Block.byItem(livingEntity.getItemBySlot(EquipmentSlot.HEAD).getItem()).defaultBlockState();
        if (!blockState.isAir()) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            poseStack.translate(-0.5F, -0.518F, -0.5F);
            this.blockRenderer.renderSingleBlock(blockState, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.translucent());
            poseStack.popPose();
        }
    }
}

