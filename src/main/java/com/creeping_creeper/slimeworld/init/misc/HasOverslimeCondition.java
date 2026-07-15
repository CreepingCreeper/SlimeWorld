package com.creeping_creeper.slimeworld.init.misc;

import com.creeping_creeper.slimeworld.init.ModOthers;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.modules.capacity.OverslimeModule;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class HasOverslimeCondition implements LootItemCondition {
    static final HasOverslimeCondition INSTANCE = new HasOverslimeCondition();

    @Override
    public @NotNull LootItemConditionType getType() {
        return ModOthers.hasModifierLootCondition.get();
    }

    @Override
    public boolean test(LootContext context) {
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        return tool != null && tool.is(TinkerTags.Items.MODIFIABLE) && OverslimeModule.INSTANCE.getAmount(ToolStack.from(tool)) > 0;
    }

    public static class ConditionSerializer implements Serializer<HasOverslimeCondition> {
        @Override
        public void serialize(@NotNull JsonObject json, @NotNull HasOverslimeCondition condition, @NotNull JsonSerializationContext context) {
        }

        @Override
        public @NotNull HasOverslimeCondition deserialize(@NotNull JsonObject json, @NotNull JsonDeserializationContext context) {
            return HasOverslimeCondition.INSTANCE;
        }
    }
}
