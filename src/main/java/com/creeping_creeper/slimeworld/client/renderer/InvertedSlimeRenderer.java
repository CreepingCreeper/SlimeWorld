package com.creeping_creeper.slimeworld.client.renderer;

import com.creeping_creeper.slimeworld.client.ModModelLayers;
import com.creeping_creeper.slimeworld.client.layer.InvertedSlimeArmorLayer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Slime;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.world.entity.ArmoredSlimeEntity;

@OnlyIn(Dist.CLIENT)
public class InvertedSlimeRenderer extends MobRenderer<Slime, SlimeModel<Slime>> {
    private final ResourceLocation slime, metal;

    public InvertedSlimeRenderer(EntityRendererProvider.Context context, ResourceLocation slime, ResourceLocation metal) {
        super(context, new SlimeModel<>(context.bakeLayer(ModModelLayers.InvertedSlimeInner)), 0.25F);
        this.addLayer(new SlimeOuterLayer<>(this, context.getModelSet()));
        this.slime = slime;
        this.metal = metal;
        addLayer(new InvertedSlimeArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelSet()));
    }

    @Override
    public void render(Slime entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        this.shadowRadius = 0.25F * (float)entity.getSize();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void scale(Slime livingEntity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.999F, 0.999F, 0.999F);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        float f1 = (float)livingEntity.getSize() * 0.5f;
        float f2 = Mth.lerp(partialTickTime, livingEntity.oSquish, livingEntity.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        poseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Slime entity) {
        if (slime != metal && ((ArmoredSlimeEntity) entity).isMetal()) {
            return metal;
        }
        return slime;
    }

    public static LayerDefinition createInnerBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("cube", CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, 17.0F, -3.0F, 6.0F, 6.0F, 6.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(32, 0).addBox(-3.25F, 20.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(32, 4).addBox(1.25F, 20.0F, -3.5F, 2.0F, 2.0F, 2.0F), PartPose.ZERO);
        partdefinition.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(32, 8).addBox(0.0F, 18.0F, -3.5F, 1.0F, 1.0F, 1.0F), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 64, 32);
    }
}
