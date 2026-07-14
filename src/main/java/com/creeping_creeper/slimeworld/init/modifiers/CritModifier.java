package com.creeping_creeper.slimeworld.init.modifiers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nullable;

public class CritModifier extends Modifier implements ProjectileLaunchModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_LAUNCH);
    }

    @Override
    public void onProjectileLaunch(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, @NotNull LivingEntity shooter, @NotNull Projectile projectile, @Nullable AbstractArrow arrow, @NotNull ModDataNBT persistentData, boolean primary) {
        if (arrow != null) {
            arrow.setCritArrow(true);
        }
    }
}
