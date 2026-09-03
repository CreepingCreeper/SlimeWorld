package com.creeping_creeper.slimeworld.plugin;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModMisc;
import com.creeping_creeper.slimeworld.init.misc.DryingRackRecipe;
import com.creeping_creeper.slimeworld.plugin.jei.DryingRackRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    public static RecipeType<DryingRackRecipe> DryingRecipeType =
            new RecipeType<>(DryingRackRecipeCategory.UID, DryingRackRecipe.class);

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return SlimeWorld.getResource("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new DryingRackRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager rm = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();
        List<DryingRackRecipe> recipesDryingRack = rm.getAllRecipesFor(ModMisc.DryingRecipeType.get());
        registration.addRecipes(DryingRecipeType,recipesDryingRack);
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registry) {
        registry.addRecipeCatalyst(ModItems.DryingRack.asItem().getDefaultInstance(), DryingRecipeType);
    }
}