package com.creeping_creeper.slimeworld.events;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.entity.BossSlimeEntity;
import com.creeping_creeper.slimeworld.init.entity.IchorSlimeEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.shared.TinkerEffects;

@Mod.EventBusSubscriber(modid = SlimeWorld.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {
    @SubscribeEvent
    public static void onWorldJoin(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide && event.getEntity() instanceof IchorSlimeEntity slime) {
            if (slime.getSpawnType() != MobSpawnType.MOB_SUMMONED) {
                slime.addEffect(new MobEffectInstance(TinkerEffects.antigravity.get(), -1, 0, false, false));
            }
        }
    }

    @SubscribeEvent
    public void onLivingDying(LivingDeathEvent event){
        LivingEntity living = event.getEntity();
        if (living instanceof BossSlimeEntity boss && boss.getSize() > 2) {
            boss.setSize(boss.getSize() / 2, true);
            event.setCanceled(true);
        }
    }
}
