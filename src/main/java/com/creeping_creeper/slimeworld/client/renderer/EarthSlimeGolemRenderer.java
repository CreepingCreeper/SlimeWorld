package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.client.model.SlimeGolemModel;
import com.creeping_creeper.slimeworld.init.entity.golem.BaseSlimeGolemEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class EarthSlimeGolemRenderer extends SlimeGolemRenderer{
    private static final ResourceLocation SLIME_LOCATION = SlimeWorld.getResource("textures/entity/golem/earth_slime");

    public EarthSlimeGolemRenderer(EntityRendererProvider.Context context) {
        super(context, SLIME_LOCATION);
    }

    @Override
    public void render(@NotNull BaseSlimeGolemEntity entity, float p_117789_, float p_117790_, @NotNull PoseStack p_117791_, @NotNull MultiBufferSource p_117792_, int p_117793_) {
        this.setModelProperties(entity);
        super.render(entity, p_117789_, p_117790_, p_117791_, p_117792_, p_117793_);
    }

    private void setModelProperties(BaseSlimeGolemEntity entity) {
        SlimeGolemModel<BaseSlimeGolemEntity> model = this.getModel();
        HumanoidModel.ArmPose humanoidmodel$armpose = getArmPose(entity, InteractionHand.OFF_HAND);
        if (entity.getMainArm() == HumanoidArm.RIGHT) {
            model.leftArmPose = humanoidmodel$armpose;
        } else {
            model.rightArmPose = humanoidmodel$armpose;
        }
    }

    private static HumanoidModel.ArmPose getArmPose(Mob entity, InteractionHand hand) {
        ItemStack itemstack = entity.getItemInHand(hand);
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            UseAnim useanim = itemstack.getUseAnimation();
            if (useanim == UseAnim.BLOCK) {
                return HumanoidModel.ArmPose.BLOCK;
            }

            return HumanoidModel.ArmPose.ITEM;
        }
    }
}
