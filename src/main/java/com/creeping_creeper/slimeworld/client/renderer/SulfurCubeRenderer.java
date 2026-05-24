package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.client.ModModelLayers;
import com.creeping_creeper.slimeworld.client.layer.SulfurCubeBlockLayer;
import com.creeping_creeper.slimeworld.client.layer.SulfurCubeOuterLayer;
import com.creeping_creeper.slimeworld.client.model.SulfurCubeModel;
import com.creeping_creeper.slimeworld.init.entity.SulfurCubeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SulfurCubeRenderer extends MobRenderer<SulfurCubeEntity, SulfurCubeModel<SulfurCubeEntity>> {
    private static final ResourceLocation SULFUR_CUBE_LOCATION = SlimeWorld.getResource("textures/entity/sulfur_cube.png");

    public SulfurCubeRenderer(EntityRendererProvider.Context context) {
        super(context, new SulfurCubeModel<>(context.bakeLayer(ModModelLayers.SulferCubeInner)), 0.25F);
        addLayer(new SulfurCubeBlockLayer<>(this, context.getBlockRenderDispatcher()));
        addLayer(new SulfurCubeOuterLayer<>(this, context.getModelSet()));
    }

    @Override
    public void render(SulfurCubeEntity entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.shadowRadius = 0.25F * (float)entity.getSize();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void scale(@NotNull SulfurCubeEntity entity, @NotNull PoseStack poseStack, float partialTicks) {
        float size = (float) entity.getSize();
        float ss = Mth.lerp(partialTicks, entity.oSquish, entity.squish) / (size * 0.5F + 1.0F);
        float w = 1.0F / (ss + 1.0F);
        poseStack.scale(w * size * 0.999F, 1.0F / w * size * 0.999F, w * size * 0.999F);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        super.scale(entity, poseStack, partialTicks);

        float fuse = entity.isPrimed() ? (float)entity.getFuse() - partialTicks + 1.0F : 0.0F;
        if (fuse < 10.0F && fuse > 0.0F) {
            float s = 1.0F + getSwellAmount(fuse);
            poseStack.scale(s, s, s);
        }

        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(-0.0F, 0.98F, -0.0F);
    }

    private static float getSwellAmount(float fuse) {
        float g = 1.0F - fuse / 10.0F;
        g = Mth.clamp(g, 0.0F, 1.0F);
        g *= g;
        g *= g;
        return g * 0.3F;
    }

    @Override
    protected float getWhiteOverlayProgress(SulfurCubeEntity entity, float partialTicks) {
        return entity.getFuse() / 5 % 2 == 0 ? (float) (1 / 15.0) : 0;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SulfurCubeEntity sulfurCubeEntity) {
        return SULFUR_CUBE_LOCATION;
    }
}

