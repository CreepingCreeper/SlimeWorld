package com.creeping_creeper.slimeworld.client.model;

import com.creeping_creeper.slimeworld.init.entity.monster.BoggedEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class BoggedModel<T extends BoggedEntity> extends SkeletonModel<T> {
    private final ModelPart mushrooms;

    public BoggedModel(ModelPart root) {
        super(root);
        this.mushrooms = root.getChild("head").getChild("mushrooms");
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.mushrooms.visible = !entity.isSheared();
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
