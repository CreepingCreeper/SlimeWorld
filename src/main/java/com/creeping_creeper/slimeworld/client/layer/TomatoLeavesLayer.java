package com.creeping_creeper.slimeworld.client.layer;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Axis;
import net.minecraft.world.entity.monster.Slime;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class TomatoLeavesLayer<T extends Slime, M extends HierarchicalModel<T>> extends RenderLayer<T, M> {
    private static final ResourceLocation LEAVES = SlimeWorld.getResource("textures/entity/tomato_leaves.png");
    private static final float QUAD_SIZE = 0.4F;
    private static final float HEAD_UP_OFFSET = 0.9F;

    public TomatoLeavesLayer(RenderLayerParent<T, M> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack matrices, @NotNull MultiBufferSource buffer, int packedLight, @NotNull T entity, float limbSwing, float swing, float partialTicks, float age, float headYaw, float headPitch) {
        matrices.pushPose();
        matrices.translate(0, HEAD_UP_OFFSET , 0);
        // 整体适配缩放
        matrices.scale(0.5F, 0.25F, 0.5F);

        // ========== 核心：渲染 4 个 45° 垂直纹理 ==========
        renderOneQuad(matrices, buffer, packedLight, 0);
        renderOneQuad(matrices, buffer, packedLight, 90);

        matrices.popPose();
    }

    private void renderOneQuad(PoseStack matrices, MultiBufferSource buffer, int light, int yRot) {
        matrices.pushPose();
        matrices.mulPose(Axis.YP.rotationDegrees(yRot + 45F));

        PoseStack.Pose pose = matrices.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(LEAVES));

        vertex(consumer, matrix4f, matrix3f, light, -QUAD_SIZE, -QUAD_SIZE, 0, 0);
        vertex(consumer, matrix4f, matrix3f, light,  QUAD_SIZE, -QUAD_SIZE, 1, 0);
        vertex(consumer, matrix4f, matrix3f, light,  QUAD_SIZE,  QUAD_SIZE, 1, 1);
        vertex(consumer, matrix4f, matrix3f, light, -QUAD_SIZE,  QUAD_SIZE, 0, 1);

        matrices.popPose();
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, Matrix3f normal, int lightmapUV, float x, float y, int u, int v) {
        consumer.vertex(pose, x, y, 0.0F)
                .color(255, 255, 255, 255)
                .uv((float) u, (float) v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(lightmapUV)
                .normal(normal, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }
}
