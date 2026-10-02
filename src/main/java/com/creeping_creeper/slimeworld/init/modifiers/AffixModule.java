package com.creeping_creeper.slimeworld.init.modifiers;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierTraitHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.Optional;

public enum AffixModule implements ModifierModule, ModifierTraitHook, ModifierRemovalHook {
    INSTANCE;

    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<AffixModule>defaultHooks(ModifierHooks.MODIFIER_TRAITS, ModifierHooks.REMOVE);

    private final SingletonLoader<AffixModule> LOADER = new SingletonLoader<>(this);

    @Override
    public void addTraits(IToolContext context, ModifierEntry modifier, TraitBuilder builder, boolean firstEncounter) {
        ModifierId id = getAffixModifier(context).orElse(ModifierId.EMPTY);
        builder.add(id, 1);
    }

    @Override
    public RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    @Override
    public List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(SlimeWorld.getResource("affix"));
        return null;
    }

    public static Optional<ModifierId> getAffixModifier(IToolContext context) {
        ResourceLocation affix = ResourceLocation.tryParse(context.getPersistentData().getString(SlimeWorld.getResource("affix")));
        if (affix != null){
            return Optional.ofNullable(ModifierId.tryBuild(affix.getNamespace(), affix.getPath()));
        }
        return Optional.empty();
    }
}
