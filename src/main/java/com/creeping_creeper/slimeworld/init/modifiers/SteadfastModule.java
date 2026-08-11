package com.creeping_creeper.slimeworld.init.modifiers;

import com.creeping_creeper.slimeworld.library.ModUtil;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.json.LevelingValue;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public record SteadfastModule(LevelingValue amount) implements ModifierModule, EquipmentChangeModifierHook {
    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<SteadfastModule>defaultHooks(ModifierHooks.EQUIPMENT_CHANGE);

    public static final RecordLoadable<SteadfastModule> LOADER = RecordLoadable.create(
            LevelingValue.LOADABLE.requiredField("amount", SteadfastModule::amount),
            SteadfastModule::new);

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if (tool.getDamage() == 0){
            ModUtil.addAbsorption(context.getEntity(), amount.compute(modifier));
        }
    }

    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        ModUtil.addAbsorption(context.getEntity(), -amount.compute(modifier));
    }
}
