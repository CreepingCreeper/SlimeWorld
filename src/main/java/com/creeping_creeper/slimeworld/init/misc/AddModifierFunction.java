package com.creeping_creeper.slimeworld.init.misc;

import com.creeping_creeper.slimeworld.init.ModMisc;
import com.creeping_creeper.slimeworld.init.item.ModifierRuneItem;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierId;

public class AddModifierFunction extends LootItemConditionalFunction {
     public static final AddModifierFunction.Serializer SERIALIZER = new Serializer();
    private final ModifierId modifier;
    private final int maxLevel;

    protected AddModifierFunction(LootItemCondition[] predicates, ModifierId modifier, int maxLevel) {
        super(predicates);
        this.modifier = modifier;
        this.maxLevel = maxLevel;
    }

    @Override
    protected @NotNull ItemStack run(ItemStack stack, @NotNull LootContext lootContext) {
        if (stack.getItem() instanceof ModifierRuneItem){
            return withModifier(stack, modifier, maxLevel);
        }
        return stack;
    }

    public static ItemStack withModifier(ItemStack stack, ModifierId modifier, int maxLevel) {
        ItemStack stack1 = stack.copy();
        stack1.getOrCreateTag().putString(ModifierRuneItem.TAG_MODIFIER, modifier.toString());
        stack1.getOrCreateTag().putInt(ModifierRuneItem.MAX_LEVEL, maxLevel);
        return stack1;
    }

    @Override
    public @NotNull LootItemFunctionType getType() {
        return ModMisc.AddModifier.get();
    }

    public static Builder addModifier(ModifierId modifier, int maxLevel) {
        return new AddModifierFunction.Builder(modifier, maxLevel);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<AddModifierFunction.Builder> {
        private final ModifierId modifier;
        private final int maxLevel;

        public Builder(ModifierId modifier, int maxLevel) {
            this.modifier = modifier;
            this.maxLevel = maxLevel;
        }

        protected AddModifierFunction.@NotNull Builder getThis() {
            return this;
        }

        public @NotNull LootItemFunction build() {
            return new AddModifierFunction(this.getConditions(), this.modifier, this.maxLevel);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<AddModifierFunction> {
        public void serialize(@NotNull JsonObject json, @NotNull AddModifierFunction addModifierFunction, @NotNull JsonSerializationContext serializationContext) {
            super.serialize(json, addModifierFunction, serializationContext);
            json.add("modifier", serializationContext.serialize(addModifierFunction.modifier));
            json.add("max_level", serializationContext.serialize(addModifierFunction.maxLevel));
        }

        public @NotNull AddModifierFunction deserialize(@NotNull JsonObject object, @NotNull JsonDeserializationContext deserializationContext, LootItemCondition @NotNull [] conditions) {
            String modifierId = GsonHelper.getAsString(object, "modifier");
            int maxLevel = GsonHelper.getAsInt(object, "max_level");
            return new AddModifierFunction(conditions, ModifierId.tryParse(modifierId), maxLevel);
        }
    }
}
