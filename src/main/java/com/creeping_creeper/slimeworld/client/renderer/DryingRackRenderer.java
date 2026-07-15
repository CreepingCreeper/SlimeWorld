package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.init.block.DryingRackBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.render.RenderItem;
import slimeknights.mantle.client.render.RenderingHelper;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class DryingRackRenderer<T extends BlockEntity & Container> implements BlockEntityRenderer<T> {

    public DryingRackRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(T inventory, float partialTicks, @NotNull PoseStack matrices, @NotNull MultiBufferSource buffer, int light, int combinedOverlayIn) {
        // 类型强转为风干架BE
        DryingRackBlockEntity te = (DryingRackBlockEntity) inventory;
        ItemStack inputStack = te.getItem(DryingRackBlockEntity.INPUT);

        BlockState state = inventory.getBlockState();
        List<RenderItem> renderItems = RenderItem.STATE_REGISTRY.get(state, List.of());
        if (!renderItems.isEmpty()) {
            boolean isRotated = RenderingHelper.applyRotation(matrices, state);

            for (int i = 0; i < renderItems.size(); i++) {
                ItemStack stack = inventory.getItem(i);
                if(stack.isEmpty()) continue;

                // 核心规则：输入槽存在物品，跳过渲染输出槽
                if(i == DryingRackBlockEntity.OUTPUT && !inputStack.isEmpty()){
                    continue;
                }

                RenderingHelper.renderItem(matrices, buffer, stack, renderItems.get(i), light);
            }

            if (isRotated) {
                matrices.popPose();
            }
        }
    }

    @Override
    public boolean shouldRenderOffScreen(T tile) {
        return !tile.isEmpty();
    }
}