package com.creeping_creeper.slimeworld.init.misc;

import com.creeping_creeper.slimeworld.init.ModMisc;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.common.IngredientLoadable;
import slimeknights.mantle.data.loadable.field.ContextKey;
import slimeknights.mantle.data.loadable.field.LoadableField;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.recipe.ICommonRecipe;
import slimeknights.mantle.recipe.container.ISingleStackContainer;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;

public class DryingRackRecipe implements ICommonRecipe<ISingleStackContainer> {
    protected static final LoadableField<Ingredient, DryingRackRecipe> INGREDIENT_FIELD = IngredientLoadable.DISALLOW_EMPTY.requiredField("ingredient", DryingRackRecipe::getIngredient);
    protected static final LoadableField<ItemOutput, DryingRackRecipe> RESULT_FIELD = ItemOutput.Loadable.REQUIRED_ITEM.requiredField("output", r -> r.result);
    protected static final LoadableField<Integer, DryingRackRecipe> DRYING_TIME_FIELD = IntLoadable.FROM_ONE.defaultField("drying_time",3000, DryingRackRecipe::getDryingTime);

    public static final RecordLoadable<DryingRackRecipe> LOADER = RecordLoadable.create(
            ContextKey.ID.requiredField(),
            LoadableRecipeSerializer.RECIPE_GROUP, INGREDIENT_FIELD, RESULT_FIELD, DRYING_TIME_FIELD,
            DryingRackRecipe::new);

    private final ResourceLocation id;
    private final Ingredient ingredient;
    protected final ItemOutput result;
    protected final int dryingTime;

    public DryingRackRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemOutput result, int dryingTime) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result;
        this.dryingTime = dryingTime;
    }


        @Override
    public boolean matches(@NotNull ISingleStackContainer inv, @NotNull Level worldIn) {
        return getIngredient().test(inv.getStack());
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess p_267052_) {
        return this.result.get();
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModMisc.DryingRecipeSerializer.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return ModMisc.DryingRecipeType.get();
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public int getDryingTime() {
        return this.dryingTime;
    }
}
