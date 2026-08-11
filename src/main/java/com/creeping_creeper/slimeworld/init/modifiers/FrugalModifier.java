package com.creeping_creeper.slimeworld.init.modifiers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.items.ItemHandlerHelper;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.tools.entity.ModifiableArrow;

import javax.annotation.Nullable;

public class FrugalModifier extends Modifier implements ProjectileLaunchModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_LAUNCH);
    }

    @Override
    public void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT persistentData, boolean primary) {
        if (arrow instanceof ModifiableArrow arrow1 && shooter instanceof Player player && arrow.pickup == AbstractArrow.Pickup.ALLOWED && RANDOM.nextFloat() < modifier.getLevel() * 0.2F){
            ItemHandlerHelper.giveItemToPlayer(player, arrow1.getPickupItem());
            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
    }
}
