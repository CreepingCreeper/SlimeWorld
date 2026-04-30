package com.creeping_creeper.slimeworld.init.modifiers;

import net.minecraft.network.chat.Component;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.ToolHooks;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;

public class SmithingModifier extends Modifier implements ModifierTraitHook, ModifierRemovalHook {

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, TraitBuilder builder, boolean firstEncounter) {
      if ("traits".equals(context.getPersistentData().getString(modifier.getModifier().getId()))) {
            ToolDefinition definition = context.getDefinition();
            definition.getHook(ToolHooks.REBALANCED_TRAIT).addTraits(definition, context.getMaterials(), builder);
        }
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(modifier.getId());
        return null;
    }
}
