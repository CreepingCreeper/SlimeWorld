package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.init.entity.FloatingWindEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FloatingWindRenderer extends EntityRenderer<FloatingWindEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon_fireball.png");

    public FloatingWindRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    public ResourceLocation getTextureLocation(FloatingWindEntity entity) {
        return TEXTURE_LOCATION;
    }
}

