package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.init.entity.golem.BaseSlimeGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class InvertedSlimeGolemRenderer extends SlimeGolemRenderer{
    public InvertedSlimeGolemRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void setupRotations(@NotNull BaseSlimeGolemEntity entity, @NotNull PoseStack poseStack, float ageInTicks, float yBodyRot, float partialTick) {
        // 先执行父类原版所有旋转逻辑
        super.setupRotations(entity, poseStack, ageInTicks, yBodyRot, partialTick);
        poseStack.translate(0.0F, entity.getBbHeight() + 0.1F, 0.0F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
    }
}
