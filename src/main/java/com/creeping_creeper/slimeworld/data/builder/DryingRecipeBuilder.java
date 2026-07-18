package com.creeping_creeper.slimeworld.data.builder;

import com.creeping_creeper.slimeworld.init.misc.DryingRackRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.recipe.data.AbstractRecipeBuilder;
import slimeknights.mantle.recipe.helper.ItemOutput;

public class DryingRecipeBuilder extends AbstractRecipeBuilder<DryingRecipeBuilder> {
    private final Ingredient input;
    private final ItemOutput result;
    private int dryingTime = 3000;
    @Nullable
    private String group;

    private DryingRecipeBuilder(Ingredient input, ItemOutput result) {
        this.input = input;
        this.result = result;
    }

    public static DryingRecipeBuilder drying(ItemLike input, ItemLike output) {
        return drying(Ingredient.of(input), output);
    }

    public static DryingRecipeBuilder drying(Ingredient input, ItemLike output) {
        return new DryingRecipeBuilder(input, ItemOutput.fromItem(output));
    }

    public DryingRecipeBuilder time(int ticks) {
        this.dryingTime = ticks;
        return this;
    }

    @Override
    public @NotNull DryingRecipeBuilder group(@Nullable String groupName) {
        this.group = groupName;
        return this;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void save(@NotNull Consumer<FinishedRecipe> consumerIn) {
        this.save(consumerIn, BuiltInRegistries.ITEM.getKey(this.result.get().getItem()));
    }

    @Override
    public void save(@NotNull Consumer<FinishedRecipe> consumer, @NotNull ResourceLocation id) {
        consumer.accept(new LoadableFinishedRecipe<>(new DryingRackRecipe(id, group, input, result, dryingTime), DryingRackRecipe.LOADER, null));
    }

}