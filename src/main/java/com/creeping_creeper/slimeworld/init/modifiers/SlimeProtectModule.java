package com.creeping_creeper.slimeworld.init.modifiers;

import com.creeping_creeper.slimeworld.data.key.ModTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nullable;
import java.util.List;

public enum SlimeProtectModule implements ModifierModule, ProjectileHitModifierHook {
    INSTANCE;

    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<SlimeProtectModule>defaultHooks(ModifierHooks.PROJECTILE_HIT);

    private final SingletonLoader<SlimeProtectModule> loader = new SingletonLoader<>(this);

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return loader;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Override
    public Integer getPriority() {
        return 250;
    }

    @Override
    public  boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target, boolean notBlocked) {
        if (target != null) {
            EntityType<?> type = target.getType();
            return type.is(ModTags.EntityTypes.SLIME) || type.is(ModTags.EntityTypes.SLIME_GOLEM);
        }
        return false;
    }
}
