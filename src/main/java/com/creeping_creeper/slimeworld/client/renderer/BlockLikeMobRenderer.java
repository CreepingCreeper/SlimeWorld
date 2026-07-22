package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.init.entity.StaticWallMob;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BlockLikeMobRenderer<T extends StaticWallMob> extends EntityRenderer<T> {
    private final BlockRenderDispatcher blockRenderer;

    public BlockLikeMobRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = context.getBlockRenderDispatcher();
        this.shadowRadius = 0F;
    }

    @Override
    public void render(@NotNull T entity, float entityYaw, float partialTicks, PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(-0.5F, 0, -0.5F);
        BlockState blockState = entity.getViewBlock();
        blockRenderer.renderSingleBlock(blockState, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.cutoutMipped());

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
