package com.creeping_creeper.slimeworld.init.modifiers;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.json.LevelingValue;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.special.CapacityBarHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.modules.capacity.OverslimeModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public record OverloadModule(LevelingValue chance) implements ModifierModule, InventoryTickModifierHook {
    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<OverloadModule>defaultHooks(ModifierHooks.INVENTORY_TICK);
    
    public static final RecordLoadable<OverloadModule> LOADER = RecordLoadable.create(
            LevelingValue.LOADABLE.requiredField("chance", OverloadModule::chance),
            OverloadModule::new);
    
    @Override
    public  RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    private CapacityBarHook getBar(ModifierEntry modifier) {
        return OverslimeModule.INSTANCE;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        // update 1 times a second, but skip when active (messes with pulling bow back)
        if (!world.isClientSide && holder.tickCount % 20 == 0 && holder.getUseItem() != stack) {
            // has a chance of restoring each second per level
            CapacityBarHook bar = getBar(modifier);
            if (bar.getAmount(tool) > 0 && Modifier.RANDOM.nextFloat() < this.chance.compute(modifier)) {
                bar.removeAmount(tool, modifier, 1);
            }
        }
    }
    
}
