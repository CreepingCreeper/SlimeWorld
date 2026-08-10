package com.creeping_creeper.slimeworld.init.modifiers;

import com.creeping_creeper.slimeworld.init.ModParticles;
import com.creeping_creeper.slimeworld.library.ParticleUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.util.CombatHelper;
import slimeknights.tconstruct.common.TinkerDamageTypes;
import slimeknights.tconstruct.library.json.LevelingValue;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.entity.ProjectileWithPower;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.shared.TinkerEffects;

import javax.annotation.Nullable;
import java.util.List;

public record SputteringModule(LevelingValue radius) implements ModifierModule, ProjectileHitModifierHook {
    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<SputteringModule>defaultHooks(ModifierHooks.PROJECTILE_HIT);
    
    public static final RecordLoadable<SputteringModule> LOADER = RecordLoadable.create(
            LevelingValue.LOADABLE.requiredField("radius", SputteringModule::radius),
            SputteringModule::new);
    
    @Override
    public @NotNull RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Override
    public boolean onProjectileHitEntity(@NotNull ModifierNBT modifiers, @NotNull ModDataNBT persistentData, @NotNull ModifierEntry modifier, @NotNull Projectile projectile, @NotNull EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target, boolean notBlocked) {
        if (target != null) {
            Level level = projectile.level();
            int i = modifier.getLevel();
            ParticleUtil.slimeParticle(level, ModParticles.OceanSlimeParticle.get(), 12, i, projectile.getX(), projectile.getY() - 0.1, projectile.getZ());
            float radius = this.radius.compute(modifier);
            List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, projectile.getBoundingBox().inflate(radius, 1, radius));
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
