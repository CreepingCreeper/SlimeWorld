package com.creeping_creeper.slimeworld.init.modifiers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nullable;
import java.util.List;

public enum CritModule implements ModifierModule, ProjectileLaunchModifierHook {
    INSTANCE;

    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<CritModule>defaultHooks(ModifierHooks.PROJECTILE_LAUNCH);

    private final SingletonLoader<CritModule> loader = new SingletonLoader<>(this);

    @Override
    public @NotNull RecordLoadable<? extends ModifierModule> getLoader() {
        return loader;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Override
    public Integer getPriority() {
        return 250;
    }

    @Override
    public void onProjectileLaunch(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, @NotNull LivingEntity shooter, @NotNull Projectile projectile, @Nullable AbstractArrow arrow, @NotNull ModDataNBT persistentData, boolean primary) {
        if (arrow != null) {
            arrow.setCritArrow(true);
        }
    }
}
