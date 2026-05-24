package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.client.layer.TomatoLeavesLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import slimeknights.tconstruct.world.client.TinkerSlimeRenderer;

@OnlyIn(Dist.CLIENT)
public class TomatoSlimeRenderer extends TinkerSlimeRenderer {
    private static final ResourceLocation SLIME = SlimeWorld.getResource("textures/entity/tomato_slime.png");

    public TomatoSlimeRenderer(EntityRendererProvider.Context context) {
        super(context, SLIME, SLIME);
        addLayer(new TomatoLeavesLayer<>(this));
    }
}
