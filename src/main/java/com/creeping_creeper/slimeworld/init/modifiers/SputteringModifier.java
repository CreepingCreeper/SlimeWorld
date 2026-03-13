package com.creeping_creeper.slimeworld.init.modifiers;

import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.library.ParticleUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.mantle.util.CombatHelper;
import slimeknights.tconstruct.common.TinkerDamageTypes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.entity.ProjectileWithPower;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.shared.TinkerEffects;

import javax.annotation.Nullable;
import java.util.List;

public class SputteringModifier extends Modifier implements ProjectileHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT);
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target, boolean notBlocked) {
        if (target != null) {
            Level level = projectile.level();
            int i = modifier.getLevel();
            ParticleUtil.slimeParticle(level, ModEntities.oceanSlimeParticle.get(), 12, i, projectile.getX(), projectile.getY() - 0.1, projectile.getZ());
            List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, projectile.getBoundingBox().inflate(i, 1, i));
            list.remove(target);
            list.remove(attacker);
            DamageSource source = CombatHelper.damageSource(TinkerEffects.needsEnderferenceOverride(target) ? TinkerDamageTypes.WATER.melee() : TinkerDamageTypes.WATER.ranged(), projectile, attacker);
            for (LivingEntity living : list) {
                living.hurt(source, ProjectileWithPower.getDamage(projectile));
            }
        }
        return false;
    }
}
