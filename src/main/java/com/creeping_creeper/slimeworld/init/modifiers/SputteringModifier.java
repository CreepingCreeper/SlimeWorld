package com.creeping_creeper.slimeworld.init.modifiers;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.common.TinkerDamageTypes;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.entity.ProjectileWithPower;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

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
            Level level = target.level();
            List<LivingEntity> list = target.level().getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(modifier.getLevel()));
            DamageSource source = TinkerDamageTypes.source(level.registryAccess(), DamageTypes.ARROW, attacker);
            float damage = 0;
            if (projectile instanceof ProjectileWithPower withPower) {
                source = TinkerDamageTypes.source(level.registryAccess(), DamageTypes.ARROW, attacker);
                damage = withPower.getDamage();
            }
            for (LivingEntity living : list) {
                ToolAttackUtil.attackEntitySecondary(source, damage, living, living, true);
            }
        }
        return false;
    }
}
