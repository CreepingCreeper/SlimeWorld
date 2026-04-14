package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.client.ModModelLayers;
import com.creeping_creeper.slimeworld.client.layer.BoggedClothingLayer;
import com.creeping_creeper.slimeworld.client.model.BoggedModel;
import com.creeping_creeper.slimeworld.init.entity.BoggedEntity;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoggedRenderer extends HumanoidMobRenderer<BoggedEntity, BoggedModel<BoggedEntity>> {
    private static final ResourceLocation BOGGED_SKELETON_LOCATION = SlimeWorld.getResource("textures/entity/bogged.png");

    public BoggedRenderer(EntityRendererProvider.Context context) {
        this(context, ModModelLayers.Bogged, ModModelLayers.BoggedInnerArmor, ModModelLayers.BoggedOuterArmor);
        this.addLayer(new BoggedClothingLayer<>(this, context.getModelSet()));
    }

    public BoggedRenderer(EntityRendererProvider.Context context, ModelLayerLocation skeletonLayer, ModelLayerLocation innerModelLayer, ModelLayerLocation outerModelLayer) {
        super(context, new BoggedModel<>(context.bakeLayer(skeletonLayer)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel<>(context.bakeLayer(innerModelLayer)), new SkeletonModel<>(context.bakeLayer(outerModelLayer)), context.getModelManager()));
    }

    @Override
    public ResourceLocation getTextureLocation(BoggedEntity entity) {
        return BOGGED_SKELETON_LOCATION;
    }

    @Override
    protected boolean isShaking(BoggedEntity entity) {
        return entity.isShaking();
    }
}