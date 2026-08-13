package com.creeping_creeper.slimeworld.init.modifiers;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.json.LevelingValue;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public record UnyieldingModule(LevelingValue amount) implements ModifierModule, ModifyDamageModifierHook {
    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<UnyieldingModule>defaultHooks(ModifierHooks.MODIFY_HURT);

    public static final RecordLoadable<UnyieldingModule> LOADER = RecordLoadable.create(
            LevelingValue.LOADABLE.requiredField("amount", UnyieldingModule::amount),
            UnyieldingModule::new);

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (context.getEntity().invulnerableTime >= 10) {
            context.getEntity().invulnerableTime += (int) this.amount.compute(modifier);
        }
        return amount;
    }
}
