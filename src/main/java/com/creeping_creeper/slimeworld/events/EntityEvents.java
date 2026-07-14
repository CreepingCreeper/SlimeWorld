package com.creeping_creeper.slimeworld.events;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.entity.boss.BaseBossSlimeEntity;
import com.creeping_creeper.slimeworld.init.entity.monster.BoggedEntity;
import com.creeping_creeper.slimeworld.init.entity.monster.IchorSlimeEntity;
import com.creeping_creeper.slimeworld.init.entity.monster.ParchedEntity;
import com.creeping_creeper.slimeworld.init.entity.monster.TomatoSlimeEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.shared.TinkerEffects;
import slimeknights.tconstruct.world.TinkerHeadType;
import slimeknights.tconstruct.world.TinkerWorld;

@Mod.EventBusSubscriber(modid = SlimeWorld.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {
    @SubscribeEvent
    public static void onWorldJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide) {
            Entity entity = event.getEntity();
            if (entity instanceof Mob slime && slime.getType().is(ModTags.EntityTypes.ANTIGRAVITY) && slime.getSpawnType() != MobSpawnType.MOB_SUMMONED) {
                slime.addEffect(new MobEffectInstance(TinkerEffects.antigravity.get(), -1, 0, false, false));
            }
        }
    }

    @SubscribeEvent
    public void onLivingDying(LivingDeathEvent event){
        LivingEntity living = event.getEntity();
        if (living instanceof BaseBossSlimeEntity boss && boss.getSize() > 2) {
            boss.setSize(boss.getSize() / 2, true);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    static void livingVisibility(LivingEvent.LivingVisibilityEvent event) {
        Entity lookingEntity = event.getLookingEntity();
        if (lookingEntity == null) {
            return;
        }
        ItemStack helmet = event.getEntity().getItemBySlot(EquipmentSlot.HEAD);
        Item item = helmet.getItem();
        if (item != Items.AIR && TinkerWorld.headItems.contains(item)) {
            TinkerHeadType headType = ((TinkerHeadType)((SkullBlock)((BlockItem)item).getBlock()).getType());
            if((lookingEntity instanceof BoggedEntity && headType == TinkerHeadType.VENOMBONE) || (lookingEntity instanceof ParchedEntity && headType == TinkerHeadType.BLAZING_BONE)){
                event.modifyVisibility(0.5f);
            }
        }
    }

    @SubscribeEvent
    static void creeperKill(LivingDropsEvent event) {
        DamageSource source = event.getSource();
        if (source != null) {
            Entity entity = source.getEntity();
            if (entity instanceof Creeper creeper) {
                if (creeper.canDropMobsSkull()) {
                    LivingEntity dying = event.getEntity();
                    if(dying instanceof BoggedEntity){
                        creeper.increaseDroppedSkulls();
                        event.getDrops().add(dying.spawnAtLocation(TinkerWorld.heads.get(TinkerHeadType.VENOMBONE)));
                    }else if(dying instanceof ParchedEntity){
                        creeper.increaseDroppedSkulls();
                        event.getDrops().add(dying.spawnAtLocation(TinkerWorld.heads.get(TinkerHeadType.BLAZING_BONE)));
                    }
                }
            }
        }
    }

    @SubscribeEvent
    static void projectileHit(ProjectileImpactEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level();
        if (entity.getType().is(ModTags.EntityTypes.SUMMON_TOMATO_SLIME) && level.random.nextInt(10) == 0){
            TomatoSlimeEntity slime = ModEntities.TomatoSlimeEntity.get().create(level);
            if (slime != null) {
                slime.moveTo(entity.getX(), entity.getY(), entity.getZ(), entity.getYRot(), entity.getXRot());
                level.addFreshEntity(slime);
            }
        }
    }
}
