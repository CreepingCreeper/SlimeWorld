package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.entity.TravelersPlateSlimeEntity;

public class OriginSlimeEntity extends TravelersPlateSlimeEntity{
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Items.AMETHYST_SHARD, TinkerWorld.earthGeode.asItem(), TinkerWorld.skyGeode.asItem(), TinkerWorld.ichorGeode.asItem(), TinkerWorld.enderGeode.asItem(), ModItems.OceanGeode.asItem());

    public OriginSlimeEntity(EntityType<? extends OriginSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
        if (!worldIn.isClientSide) {
            tryAddAttribute(Attributes.ATTACK_DAMAGE, new AttributeModifier("slimeworld.attack_damage_bonus", -1, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    private void tryAddAttribute(Attribute attribute, AttributeModifier modifier) {
        AttributeInstance instance = getAttribute(attribute);
        if (instance != null) {
            instance.addTransientModifier(modifier);
        }
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        InteractionResult interactionresult = super.mobInteract(player, hand);
        ItemStack item =  player.getItemInHand(hand);
        Level level = this.level();
        if (!level.isClientSide && FOOD_ITEMS.test(item)) {
            for (int i = 0; i < 6; i++){
                if (item.getItem() == FOOD_ITEMS.getItems()[i].getItem()){
                    if (i==0){
                       if (this.getSize() > 3){
                           return interactionresult;
                       }
                       this.setSize(this.getSize() * 2, true);
                       break;
                    }
                    Slime slime = switch (i){
                        case 1 -> EntityType.SLIME.create(level);
                        case 2 -> TinkerWorld.skySlimeEntity.get().create(level);
                        case 3 -> ModEntities.ichorSlimeEntity.get().create(level);
                        case 4 -> TinkerWorld.enderSlimeEntity.get().create(level);
                        default -> ModEntities.oceanSlimeEntity.get().create(level);
                    };
                    assert slime != null;
                    transform(slime);
                }
            }
            level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.GENERIC_EAT, player.getSoundSource(), 1, 0.5f);
            if (!player.getAbilities().instabuild) {
                item.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return interactionresult;
    }

    private void transform(Slime slime){
        if (this.isPersistenceRequired()) {
            slime.setPersistenceRequired();
        }
        slime.setCustomName(this.getCustomName());
        slime.setNoAi(this.isNoAi());
        slime.setInvulnerable(this.isInvulnerable());
        slime.setSize(this.getSize(), true);
        slime.setHealth(this.getHealth());
        slime.setItemSlot(EquipmentSlot.HEAD, getItemBySlot(EquipmentSlot.HEAD).copy());
        slime.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        this.level().addFreshEntity(slime);
        this.discard();
    }

    @Override
    protected ParticleOptions getParticleType() {
        return ModEntities.originSlimeParticle.get();
    }

    @Override
    protected MaterialId getPlating() {
        return MaterialIds.copper;
    }
}