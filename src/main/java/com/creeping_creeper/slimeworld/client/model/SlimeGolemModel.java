package com.creeping_creeper.slimeworld.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class SlimeGolemModel<T extends Mob > extends HumanoidModel<T> {
    public SlimeGolemModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(-2.0F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F), PartPose.offset(2.0F, 12.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        ItemStack itemstack = entity.getMainHandItem();
        if (entity.isAggressive() && (itemstack.isEmpty() || !itemstack.is(Items.BOW))) {
            float f = Mth.sin(this.attackTime * (float)Math.PI);
            float f1 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * (float)Math.PI);
            this.rightArm.zRot = 0.0F;
            this.leftArm.zRot = 0.0F;
            this.rightArm.yRot = -(0.1F - f * 0.6F);
            this.leftArm.yRot = 0.1F - f * 0.6F;
            this.rightArm.xRot = (-(float)Math.PI / 2F);
            this.leftArm.xRot = (-(float)Math.PI / 2F);
            ModelPart var10000 = this.rightArm;
            var10000.xRot -= f * 1.2F - f1 * 0.4F;
            var10000 = this.leftArm;
            var10000.xRot -= f * 1.2F - f1 * 0.4F;
            AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
        }

    }

    public void translateToHand(@NotNull HumanoidArm side, @NotNull PoseStack poseStack) {
        float f = side == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        ModelPart modelpart = this.getArm(side);
        modelpart.x += f;
        modelpart.translateAndRotate(poseStack);
        modelpart.x -= f;
    }
}