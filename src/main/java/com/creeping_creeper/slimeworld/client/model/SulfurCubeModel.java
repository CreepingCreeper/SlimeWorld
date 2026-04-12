package com.creeping_creeper.slimeworld.client.model;

import com.creeping_creeper.slimeworld.init.entity.SulfurCubeEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SulfurCubeModel<T extends SulfurCubeEntity> extends HierarchicalModel<T> {
    private final ModelPart root;
    private final ModelPart block;
    public SulfurCubeModel(ModelPart root) {
        super(RenderType::entityTranslucent);
        this.root = root;
        this.block = root.getChild("block");
    }

    public static LayerDefinition createInnerBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("block", CubeListBuilder.create().texOffs(0, 36).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.block.visible = !entity.hasBodyItem();
    }

    @Override
    public ModelPart root() {
        return this.root;
    }
}
