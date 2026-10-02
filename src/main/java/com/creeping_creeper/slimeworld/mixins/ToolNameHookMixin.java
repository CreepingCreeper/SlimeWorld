package com.creeping_creeper.slimeworld.mixins;

import com.creeping_creeper.slimeworld.init.modifiers.AffixModule;
import com.creeping_creeper.slimeworld.library.AffixUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.tools.definition.ToolDefinition;
import slimeknights.tconstruct.library.tools.definition.module.ToolHooks;
import slimeknights.tconstruct.library.tools.definition.module.display.ToolNameHook;
import slimeknights.tconstruct.library.tools.helper.TooltipUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(ToolNameHook.class)
public interface ToolNameHookMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    static Component getName(ToolDefinition definition, ItemStack stack, @Nullable IToolStackView tool) {
        String name = TooltipUtil.getDisplayName(stack);
        if (!name.isEmpty()) {
            return Component.literal(name);
        }

        IToolStackView tool1 = ToolNameHook.getTool(stack, tool);
        Component baseName = definition.getHook(ToolHooks.DISPLAY_NAME).getDisplayName(definition, stack, tool);
        Optional<ModifierId> id = AffixModule.getAffixModifier(tool1);
        if (id.isPresent()){
            String key = id.get().toLanguageKey("modifier");
            return Component.translatable(key).append(AffixUtil.Prep).append(baseName);
        }
        return baseName;
    }

}
