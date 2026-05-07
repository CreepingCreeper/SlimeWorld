package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.client.ModModelLayers;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ParchedRenderer extends HumanoidMobRenderer<AbstractSkeleton, SkeletonModel<AbstractSkeleton>> {
    private static final ResourceLocation PARCHED_SKELETON_LOCATION = SlimeWorld.getResource("textures/entity/parched.png");

    public ParchedRenderer(EntityRendererProvider.Context context) {
        this(context, ModModelLayers.Parched, ModModelLayers.ParchedInnerArmor, ModModelLayers.ParchedOuterArmor);
    }

    public ParchedRenderer(EntityRendererProvider.Context context, ModelLayerLocation skeletonLayer, ModelLayerLocation innerModelLayer, ModelLayerLocation outerModelLayer) {
        super(context, new SkeletonModel<>(context.bakeLayer(skeletonLayer)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new SkeletonModel(context.bakeLayer(innerModelLayer)), new SkeletonModel(context.bakeLayer(outerModelLayer)), context.getModelManager()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull AbstractSkeleton entity) {
        return PARCHED_SKELETON_LOCATION;
    }

    @Override
    protected boolean isShaking(AbstractSkeleton entity) {
        return entity.isShaking();
    }

    public static LayerDefinition createSingleModelDualBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition root = meshdefinition.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F).texOffs(28, 0).addBox(-4.0F, 10.0F, -2.0F, 8.0F, 1.0F, 4.0F).texOffs(16, 48).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F).texOffs(0, 32).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, 0.0F)).addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(42, 33).addBox(-1.55F, -2.025F, -1.5F, 3.0F, 12.0F, 3.0F), PartPose.offset(-5.5F, 2.0F, 0.0F));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(56, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(40, 48).addBox(-1.45F, -2.025F, -1.5F, 3.0F, 12.0F, 3.0F), PartPose.offset(5.5F, 2.0F, 0.0F));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(0, 49).addBox(-1.5F, -0.0F, -1.5F, 3.0F, 12.0F, 3.0F), PartPose.offset(-2.0F, 12.0F, 0.0F));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F).texOffs(4, 49).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F), PartPose.offset(2.0F, 12.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }
}
