package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.data.key.ModModifierIds;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModModifiers;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.predicate.entity.LivingEntityPredicate;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.TinkerDamageTypes;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierProvider;
import slimeknights.tconstruct.library.json.variable.entity.ConditionalEntityVariable;
import slimeknights.tconstruct.library.json.variable.entity.EntityVariable;
import slimeknights.tconstruct.library.json.variable.stat.EntityConditionalStatVariable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.modifiers.modules.build.EnchantmentModule;
import slimeknights.tconstruct.library.modifiers.modules.build.SwappableToolTraitsModule;
import slimeknights.tconstruct.library.modifiers.modules.mining.ConditionalMiningSpeedModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierLevelDisplay;
import slimeknights.tconstruct.library.tools.definition.module.ToolHooks;
import slimeknights.tconstruct.shared.TinkerAttributes;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.tools.data.ModifierIds;
import slimeknights.tconstruct.tools.modules.interaction.FireballModule;

import static slimeknights.tconstruct.library.json.math.ModifierFormula.*;

public class ModModifierProvider extends AbstractModifierProvider {
    public ModModifierProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void addModifiers() {
        buildModifier(ModModifierIds.overwash);
        buildModifier(ModModifierIds.slimeBalance).levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(new SwappableToolTraitsModule(null, "", ToolHooks.TOOL_TRAITS))
                .addModule(new SwappableToolTraitsModule(null, "", ToolHooks.TOOL_TRAITS))
                .addModule(new SwappableToolTraitsModule(null, "", ToolHooks.TOOL_TRAITS));
        buildModifier(ModModifierIds.vanishingCurse).levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(EnchantmentModule.builder(Enchantments.VANISHING_CURSE).constant());
        buildModifier(ModModifierIds.undercurrent).levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(AttributeModule.builder(TinkerAttributes.CROUCH_DAMAGE_MULTIPLIER, AttributeModifier.Operation.MULTIPLY_BASE).tooltipStyle(AttributeModule.TooltipStyle.PERCENT).eachLevel(0.12F));
        buildModifier(ModModifierIds.waving)
                .addModule(ConditionalMiningSpeedModule.builder()
                        .customVariable("bonus", new EntityConditionalStatVariable(new ConditionalEntityVariable(
                                LivingEntityPredicate.FEET_IN_WATER,
                                new EntityVariable.Constant(0.3F),
                                new EntityVariable.Constant(0.0F)
                        ), 0.3F)).formula()
                        .variable(MULTIPLIER).customVariable("bonus").multiply()
                        .variable(LEVEL).multiply()
                        .variable(VALUE).add()
                        .build());

        buildModifier(ModifierIds.slimeball).levelDisplay(ModifierLevelDisplay.NO_LEVELS)
                .addModule(FireballModule.builder()
                        .damageType(TinkerDamageTypes.FLUID_IMPACT)
                        .sound(Sounds.SLIMY_BOUNCE.getSound())
                        .modifier(ModifierIds.bounce).damageMultiplier(1.5f)
                        .fireball(SlimeType.EARTH.getSlimeballTag()).modifier(new ModifierEntry(ModifierIds.drawback, 2)).damageMultiplier(0.67f).end()
                        .fireball(SlimeType.SKY.getSlimeballTag()  ).damageType(TinkerDamageTypes.FLUID_COLD).modifier(ModifierIds.freezing).end()
                        .fireball(SlimeType.ICHOR.getSlimeballTag()).damageType(TinkerDamageTypes.FLUID_FIRE).modifier(ModifierIds.fiery).end()
                        .fireball(SlimeType.ENDER.getSlimeballTag()).damageType(TinkerDamageTypes.FLUID_MAGIC).modifier(ModifierIds.enderclearance).end()
                        .fireball(Items.MAGMA_CREAM).damageType(TinkerDamageTypes.MOB_EXPLOSION).modifier(ModifierIds.explosive).end()
                        .fireball(ModItems.OceanSlimeBall).damageType(TinkerDamageTypes.WATER).modifier(ModModifiers.sputtering.getId()).end()
                        .build());
    }

    @Override
    public @NotNull String getName() {
        return "Slime World Modifier Provider";
    }
}
