package com.creeping_creeper.slimeworld.client.layer;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.monster.Slime;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.SkullBlock.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import slimeknights.tconstruct.world.client.SlimeArmorLayer;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class InvertedSlimeArmorLayer<T extends Slime, M extends HierarchicalModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T,M> {
    private final A armorModel;
    public final Map<Type, SkullModelBase> skullModels;

    public InvertedSlimeArmorLayer(RenderLayerParent<T,M> pRenderer, A armorModel, EntityModelSet modelSet) {
        super(pRenderer);
        this.armorModel = armorModel;
        this.skullModels = SkullBlockRenderer.createSkullRenderers(modelSet);
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource buffer, int packedLight, T entity, float pLimbSwing, float swing, float partialTicks, float age, float headYaw, float headPitch) {
        ItemStack helmet = entity.getItemBySlot(EquipmentSlot.HEAD);
        if (!helmet.isEmpty()) {
            matrices.pushPose();
            matrices.scale(0.9f, -0.9f, 0.9f);
            matrices.translate(0, -1.11f, 0);
            Item item = helmet.getItem();
            if (item instanceof ArmorItem armor && armor.getType() == ArmorItem.Type.HELMET) {
                this.getParentModel().copyPropertiesTo(armorModel);
                armorModel.setAllVisible(false);
                armorModel.head.visible = true;
                armorModel.hat.visible = true;

                Model model = ForgeHooksClient.getArmorModel(entity, helmet, EquipmentSlot.HEAD, armorModel);
                boolean enchanted = helmet.hasFoil();

                if (armor instanceof DyeableLeatherItem dyeable) {
                    int color = dyeable.getColor(helmet);
                    float red = (color >> 16 & 255) / 255.0F;
                    float green = (color >> 8 & 255) / 255.0F;
                    float blue = (color & 255) / 255.0F;
                    renderModel(matrices, buffer, packedLight, enchanted, model, red, green, blue, getArmorResource(entity, helmet, armor, ""));
                    renderModel(matrices, buffer, packedLight, enchanted, model, 1.0F, 1.0F, 1.0F, getArmorResource(entity, helmet, armor, "_overlay"));
                } else {
                    renderModel(matrices, buffer, packedLight, enchanted, model, 1.0F, 1.0F, 1.0F, getArmorResource(entity, helmet, armor, ""));
                }
            } else {
                if (item instanceof BlockItem block && block.getBlock() instanceof AbstractSkullBlock skullBlock) {
                    matrices.scale(1.1875F, -1.1875F, -1.1875F);
                    GameProfile gameprofile = null;
                    CompoundTag tag = helmet.getTag();
                    if (tag != null && tag.contains("SkullOwner", 10)) {
                        gameprofile = NbtUtils.readGameProfile(tag.getCompound("SkullOwner"));
                    }
                    matrices.translate(-0.5, 0.0, -0.5);
                    SkullBlock.Type type = skullBlock.getType();
                    SkullModelBase skullModel = this.skullModels.get(type);
                    RenderType renderType = SkullBlockRenderer.getRenderType(type, gameprofile);
                    SkullBlockRenderer.renderSkull(null, 180.0F, pLimbSwing, matrices, buffer, packedLight, skullModel, renderType);
                } else {
                    CustomHeadLayer.translateToHead(matrices, false);
                    Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().renderItem(entity, helmet, ItemDisplayContext.HEAD, false, matrices, buffer, packedLight);
                }
            }

            matrices.popPose();
        }
    }

    private static void renderModel(PoseStack matrices, MultiBufferSource buffer, int packedLight, boolean enchanted, Model model, float red, float green, float blue, ResourceLocation texture) {
        VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(texture), false, enchanted);
        model.renderToBuffer(matrices, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    public static ResourceLocation getArmorResource(Entity entity, ItemStack stack, ArmorItem armor, String type) {
        return SlimeArmorLayer.getArmorResource(entity, stack, armor, type);
    }
}