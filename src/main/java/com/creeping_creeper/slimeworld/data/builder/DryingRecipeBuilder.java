package com.creeping_creeper.slimeworld.data.builder;

import com.creeping_creeper.slimeworld.init.ModOthers;
import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DryingRecipeBuilder implements RecipeBuilder {
    private final Ingredient input;
    private final ItemStack result;
    private int dryingTime = 3000;
    @Nullable
    private String group;

    private DryingRecipeBuilder(Ingredient input, ItemStack result) {
        this.input = input;
        this.result = result;
    }

    // ========== 静态入口 ==========
    public static DryingRecipeBuilder drying(ItemLike input, ItemLike output) {
        return drying(Ingredient.of(input), output);
    }

    public static DryingRecipeBuilder drying(Ingredient input, ItemLike output) {
        return new DryingRecipeBuilder(input, output.asItem().getDefaultInstance());
    }

    /** 设置干燥耗时 tick */
    public DryingRecipeBuilder time(int ticks) {
        this.dryingTime = ticks;
        return this;
    }

    @Override
    public @NotNull DryingRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return result.getItem();
    }

    @Override
    public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation recipeId) {
        FinishedDryingRecipe finished = new FinishedDryingRecipe(recipeId, this.group == null ? "" : this.group, this.input, this.result, this.dryingTime);
        consumer.accept(finished);
    }

    // ========== FinishedRecipe 实现 ==========
        public record FinishedDryingRecipe(ResourceLocation id, String group, Ingredient input, ItemStack output, int time) implements FinishedRecipe {

        @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                if (!group.isBlank()) {
                    json.addProperty("group", group);
                }
                json.add("input", input.toJson());

                JsonObject resultObj = new JsonObject();
                resultObj.addProperty("item", output.getItem().toString());
                if (output.getCount() > 1) {
                    resultObj.addProperty("count", output.getCount());
                }
                json.add("output", resultObj);
                if (time != 3000) {
                    json.addProperty("drying_time", time);
                }
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return id;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return ModOthers.DryingRecipeSerializer.get();
            }

            @Override
            public @Nullable JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String pCriterionName, net.minecraft.advancements.@NotNull CriterionTriggerInstance pCriterionTrigger) {
        throw new UnsupportedOperationException("DryingRecipe does not support advancement unlock");
    }
}