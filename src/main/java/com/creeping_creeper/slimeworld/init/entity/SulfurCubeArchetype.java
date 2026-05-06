package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.data.ModTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SulfurCubeArchetype {
    public static final UUID KNOCKBACK_RESISTANCE = UUID.fromString("2737DE5E-7CE8-4030-940E-514C1F175601");

    private static final ArrayList<TagKey<Item>> Archetype = new ArrayList<>(List.of(ModTags.Items.ArchetypeBouncy, ModTags.Items.ArchetypeFastFlat, ModTags.Items.ArchetypeFastSliding, ModTags.Items.ArchetypeHighResistance,
            ModTags.Items.ArchetypeLight, ModTags.Items.ArchetypeRegular, ModTags.Items.ArchetypeSlowBouncy, ModTags.Items.ArchetypeSlowFlat, ModTags.Items.ArchetypeSlowSliding, ModTags.Items.ArchetypeSticky, ModTags.Items.ArchetypeExplosive, ModTags.Items.ArchetypeHot));

    public static int getIndex(ItemStack item){
        int index = 0;
        for (TagKey<Item> tag : Archetype){
         if (item.is(tag)){
             break;
         }
         index ++;
        }
        return index;
    }

    public static void getArchetype(SulfurCubeEntity entity){
        switch (getIndex(entity.getItemBySlot(EquipmentSlot.HEAD))){
            case 0 -> applyArchetype(entity, -2.0F, 0.8999999761581421F, -0.699999988079071F, -0.9900000002235174F, true);
            case 1 -> applyArchetype(entity, -2.0F, 0.20000000298023224F, -0.8999999985098839F, -0.9900000002235174F, false);
            case 2 -> applyArchetype(entity, 0.5F, 0.10000000149011612F, -0.9499999992549419F, -0.9900000002235174F, false);
            case 3 -> applyArchetype(entity, 0.699999988079071F, 0.20000000298023224F, 0.0F, -0.9900000002235174F, false);
            case 4 -> applyArchetype(entity, -1.0F, 1.0F, -0.699999988079071F, 0.7999999523162842F, true);
            case 5 -> applyArchetype(entity, -1.0F, 0.5F, -0.699999988079071F, -0.8999999985098839F, true);
            case 6 -> applyArchetype(entity, 0.4000000059604645F, 0.6000000238418579F, -0.699999988079071F,  -0.9499999992549419F, true);
            case 7 -> applyArchetype(entity, 0.699999988079071F, 0.20000000298023224F, -0.699999988079071F, -0.8999999985098839F, false);
            case 8 -> applyArchetype(entity, 0.800000011920929F, 0.10000000149011612F, -0.9499999992549419F, -0.9900000002235174F, true);
            case 9 -> applyArchetype(entity, -2.0F, 0.0F, 1.0F, -0.9900000002235174F, true);
            case 10 -> applyArchetype(entity, -1.0F, 0.5F, -0.699999988079071F, -0.699999988079071F, true, 120, -1.0F);
            case 11 -> applyArchetype(entity, -1.0F, 0.5F, -0.699999988079071F, -0.8999999985098839F, true, -1, 1.0F);
        }
    }

    private static void applyArchetype(SulfurCubeEntity entity, float knockback, float bounciness, float frictionModifier, float airDragModifier, boolean floatsInLiquids){
        applyArchetype(entity, knockback, bounciness, frictionModifier, airDragModifier, floatsInLiquids, -1, -1.0F);
    }

    private static void applyArchetype(SulfurCubeEntity entity, float knockback, float bounciness, float frictionModifier, float airDragModifier, boolean floatsInLiquids, int maxFuse, float damage){
        AttributeInstance attribute = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (attribute != null) {
            if (attribute.getModifier(KNOCKBACK_RESISTANCE) != null) {
                attribute.removeModifier(KNOCKBACK_RESISTANCE);
            }
            attribute.addPermanentModifier(new AttributeModifier(KNOCKBACK_RESISTANCE, "slimeworld.sulfur_cube.knockback_resistance", knockback, AttributeModifier.Operation.ADDITION));
        }
        entity.bounciness = bounciness;
        entity.frictionModifier = 1.0F + frictionModifier;
        entity.airDragModifier = 1.0F + airDragModifier;
        entity.floatsInLiquids = floatsInLiquids;
        entity.maxFuseFromArchetype = maxFuse;
        entity.damage = damage;
    }

    public static void resetArchetype(SulfurCubeEntity entity){
        AttributeInstance attribute = entity.getAttribute(Attributes.KNOCKBACK_RESISTANCE);
        if (attribute != null) {
            if (attribute.getModifier(KNOCKBACK_RESISTANCE) != null) {
                attribute.removeModifier(KNOCKBACK_RESISTANCE);
            }
        }
        entity.bounciness = 0.0F;
        entity.frictionModifier = 1.0F;
        entity.airDragModifier = 1.0F;
        entity.floatsInLiquids = false;
        entity.maxFuseFromArchetype = -1;
        entity.damage = -1.0F;
    }
}
