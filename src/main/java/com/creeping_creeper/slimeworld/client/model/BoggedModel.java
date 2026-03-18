package com.creeping_creeper.slimeworld.client.model;

import com.creeping_creeper.slimeworld.init.entity.BoggedEntity;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoggedModel<T extends BoggedEntity> extends SkeletonModel<T> {
    private final ModelPart mushrooms;

    public BoggedModel(ModelPart root) {
        super(root);
        this.mushrooms = root.getChild("head").getChild("mushrooms");
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.mushrooms.visible = !entity.isSheared();
    }
}
