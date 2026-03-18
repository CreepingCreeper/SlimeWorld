package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.client.ModLayers;
import com.creeping_creeper.slimeworld.client.layer.BoggedClothingLayer;
import com.creeping_creeper.slimeworld.client.model.BoggedModel;
import com.creeping_creeper.slimeworld.init.entity.BoggedEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
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
        this(context, ModLayers.Bogged, ModLayers.BoggedInnerArmor, ModLayers.BoggedOuterArmor);
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

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = meshdefinition.getRoot();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-2.0F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(2.0F, 12.0F, 0.0F));
        PartDefinition mushrooms = root.getChild("head").addOrReplaceChild("mushrooms", CubeListBuilder.create(), PartPose.ZERO);
        mushrooms.addOrReplaceChild("red_mushroom_1", CubeListBuilder.create().texOffs(50, 16).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 0.0F), PartPose.offsetAndRotation(3.0F, -8.0F, 3.0F, 0.0F, ((float)Math.PI / 4F), 0.0F));
        mushrooms.addOrReplaceChild("red_mushroom_2", CubeListBuilder.create().texOffs(50, 16).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 0.0F), PartPose.offsetAndRotation(3.0F, -8.0F, 3.0F, 0.0F, 2.3561945F, 0.0F));
        mushrooms.addOrReplaceChild("brown_mushroom_1", CubeListBuilder.create().texOffs(50, 22).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 0.0F), PartPose.offsetAndRotation(-3.0F, -8.0F, -3.0F, 0.0F, ((float)Math.PI / 4F), 0.0F));
        mushrooms.addOrReplaceChild("brown_mushroom_2", CubeListBuilder.create().texOffs(50, 22).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 4.0F, 0.0F), PartPose.offsetAndRotation(-3.0F, -8.0F, -3.0F, 0.0F, 2.3561945F, 0.0F));
        mushrooms.addOrReplaceChild("brown_mushroom_3", CubeListBuilder.create().texOffs(50, 28).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 4.0F, 0.0F), PartPose.offsetAndRotation(-2.0F, -1.0F, 4.0F, (-(float)Math.PI / 2F), 0.0F, ((float)Math.PI / 4F)));
        mushrooms.addOrReplaceChild("brown_mushroom_4", CubeListBuilder.create().texOffs(50, 28).addBox(-3.0F, -4.0F, 0.0F, 6.0F, 4.0F, 0.0F), PartPose.offsetAndRotation(-2.0F, -1.0F, 4.0F, (-(float)Math.PI / 2F), 0.0F, 2.3561945F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }
}