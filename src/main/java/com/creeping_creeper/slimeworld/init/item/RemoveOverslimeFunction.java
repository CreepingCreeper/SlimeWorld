package com.creeping_creeper.slimeworld.init.item;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.ModModifierIds;
import com.creeping_creeper.slimeworld.init.ModOthers;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.modules.capacity.OverslimeModule;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class RemoveOverslimeFunction extends LootItemConditionalFunction {
    public static final ResourceLocation ID = SlimeWorld.getResource("remove_overslime");
    public static final RemoveOverslimeFunction.Serializer SERIALIZER = new Serializer();

    protected RemoveOverslimeFunction(LootItemCondition[] predicates) {
        super(predicates);
    }

    @Override
    protected @NotNull ItemStack run(@NotNull ItemStack stack, @NotNull LootContext lootContext) {
        ItemStack stack1 = lootContext.getParam(LootContextParams.TOOL);
        if (stack1.is(TinkerTags.Items.MODIFIABLE)){
            ToolStack tool = ToolStack.from(stack1);
            OverslimeModule.INSTANCE.removeAmount(tool, tool.getModifierLevel(ModModifierIds.overwash));
        }
        return stack;
    }

    @Override
    public @NotNull LootItemFunctionType getType() {
        return ModOthers.RemoveOverslime.get();
    }

    public static Builder removeOverslime() {
        return new RemoveOverslimeFunction.Builder();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<RemoveOverslimeFunction.Builder> {

        public Builder() {
        }

        protected RemoveOverslimeFunction.@NotNull Builder getThis() {
            return this;
        }

        public @NotNull LootItemFunction build() {
            return new RemoveOverslimeFunction(this.getConditions());
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RemoveOverslimeFunction> {
        public void serialize(@NotNull JsonObject json, @NotNull RemoveOverslimeFunction randomModifierFunction, @NotNull JsonSerializationContext serializationContext) {
            super.serialize(json, randomModifierFunction, serializationContext);
        }

        public @NotNull RemoveOverslimeFunction deserialize(@NotNull JsonObject object, @NotNull JsonDeserializationContext deserializationContext, LootItemCondition @NotNull [] conditions) {
            return new RemoveOverslimeFunction(conditions);
        }
    }
}
