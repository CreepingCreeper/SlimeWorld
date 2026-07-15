package com.creeping_creeper.slimeworld.init.misc;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModOthers;
import com.creeping_creeper.slimeworld.init.item.ModifierRuneItem;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.json.TinkerLoadables;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.ModifierManager;

import java.util.List;

public class RandomModifierFunction extends LootItemConditionalFunction {
    public static final ResourceLocation ID = SlimeWorld.getResource("random_modifier");
    public static final RandomModifierFunction.Serializer SERIALIZER = new Serializer();
    private final TagKey<Modifier> tag;
    private final int maxLevel;

    protected RandomModifierFunction(LootItemCondition[] predicates, TagKey<Modifier> tag, int maxLevel) {
        super(predicates);
        this.tag = tag;
        this.maxLevel = maxLevel;
    }

    @Override
    protected @NotNull ItemStack run(ItemStack stack, @NotNull LootContext lootContext) {
        if (stack.getItem() instanceof ModifierRuneItem){
            RandomSource random = lootContext.getRandom();
            List<Modifier> options = ModifierManager.getTagValues(tag);
            ModifierId modifier = options.get(random.nextInt(options.size())).getId();
            return withModifier(stack, modifier, maxLevel);
        }
        return stack;
    }

    private static ItemStack withModifier(ItemStack stack, ModifierId modifier, int maxLevel) {
        ItemStack stack1 = stack.copy();
        stack1.getOrCreateTag().putString(ModifierRuneItem.TAG_MODIFIER, modifier.toString());
        stack1.getOrCreateTag().putInt(ModifierRuneItem.MAX_LEVEL, maxLevel);
        return stack1;
    }

    @Override
    public @NotNull LootItemFunctionType getType() {
        return ModOthers.RandomModifier.get();
    }

    public static Builder randomModifier(TagKey<Modifier> tag, int maxLevel) {
        return new RandomModifierFunction.Builder(tag, maxLevel);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<RandomModifierFunction.Builder> {
        private final TagKey<Modifier> tag;
        private final int maxLevel;

        public Builder(TagKey<Modifier> tag, int maxLevel) {
            this.tag = tag;
            this.maxLevel = maxLevel;
        }

        protected RandomModifierFunction.@NotNull Builder getThis() {
            return this;
        }

        public @NotNull LootItemFunction build() {
            return new RandomModifierFunction(this.getConditions(), this.tag, this.maxLevel);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<RandomModifierFunction> {
        public void serialize(@NotNull JsonObject json, @NotNull RandomModifierFunction randomModifierFunction, @NotNull JsonSerializationContext serializationContext) {
            super.serialize(json, randomModifierFunction, serializationContext);
            json.add("tag", serializationContext.serialize(randomModifierFunction.tag));
            json.add("max_level", serializationContext.serialize(randomModifierFunction.maxLevel));
        }

        public @NotNull RandomModifierFunction deserialize(@NotNull JsonObject object, @NotNull JsonDeserializationContext deserializationContext, LootItemCondition @NotNull [] conditions) {
            int maxLevel = GsonHelper.getAsInt(object, "max_level");
            return new RandomModifierFunction(conditions, TinkerLoadables.MODIFIER_TAGS.getIfPresent(object, "tag"), maxLevel);
        }
    }
}
