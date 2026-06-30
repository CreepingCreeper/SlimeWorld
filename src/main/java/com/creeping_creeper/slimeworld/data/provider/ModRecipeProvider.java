package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.recipe.data.ICommonRecipeHelper;
import slimeknights.mantle.recipe.data.IRecipeHelper;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IRecipeHelper, ICommonRecipeHelper {
    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public @NotNull String getModId() {
        return SlimeWorld.MODID;
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        String building = "building/";
        stairSlabWallCrafting(consumer, ModItems.Sulfur, building, true);
        polishingRecipes(consumer, ModItems.Sulfur, ModItems.PolishedSulfur, building, true);
        stairSlabWallCrafting(consumer, ModItems.PolishedSulfur, building, true);
        polishingRecipes(consumer, ModItems.PolishedSulfur, ModItems.SulfurBricks, building, true);
        stairSlabWallCrafting(consumer, ModItems.SulfurBricks, building, true);
    }

    private void polishingRecipes(Consumer<FinishedRecipe> consumer, ItemLike before, ItemLike after, String folder, boolean addStonecutter) {
        ResourceLocation afterId = id(after);
        InventoryChangeTrigger.TriggerInstance hasBlock = RecipeProvider.has(before);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, after, 4)
                .define('B', before)
                .pattern("BBB")
                .pattern("BBB")
                .unlockedBy("has_item", hasBlock)
                .group(afterId.toString())
                .save(consumer, location(folder + afterId.getPath()));
        if (addStonecutter) {
            Ingredient ingredient = Ingredient.of(before);
            SingleItemRecipeBuilder.stonecutting(ingredient, RecipeCategory.BUILDING_BLOCKS, after)
                    .unlockedBy("has_item", hasBlock)
                    .save(consumer, wrap(afterId, folder, "_stonecutter"));
        }
    }
}