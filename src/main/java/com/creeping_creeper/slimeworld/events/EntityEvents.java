package com.creeping_creeper.slimeworld.events;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.entity.monster.TomatoSlimeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SlimeWorld.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents {
    @SubscribeEvent
    static void projectileHit(ProjectileImpactEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level();
        if (entity.getType().is(ModTags.EntityTypes.SUMMON_TOMATO_SLIME) && level.random.nextInt(10) == 0){
            TomatoSlimeEntity slime = ModEntities.TomatoSlimeEntity.get().create(level);
            BlockPos pos = event.getProjectile().getOnPos();
            if (slime != null) {
                slime.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);
                slime.moveTo(pos.above(), entity.getYRot(), entity.getXRot());
                level.addFreshEntity(slime);
            }
        }
    }
}
