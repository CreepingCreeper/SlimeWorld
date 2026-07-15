package com.creeping_creeper.slimeworld.plugin.jei;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.misc.DryingRackRecipe;
import com.creeping_creeper.slimeworld.plugin.JEIPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class DryingRackRecipeCategory implements IRecipeCategory<DryingRackRecipe> {
    public static final ResourceLocation UID = SlimeWorld.getResource("drying_rack");
    public static final ResourceLocation TEXTURE = SlimeWorld.getResource("textures/gui/drying_rack_gui.png");
    private static final String TIME = SlimeWorld.makeTranslationKey("jei", "drying_time");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable arrow;

    public DryingRackRecipeCategory(IGuiHelper helper){
        this.background  = helper.createDrawable(TEXTURE,0,0,90,42);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ModItems.DryingRack.asItem().getDefaultInstance());
        this.arrow = helper.drawableBuilder(TEXTURE,90,0,22,16).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }
    @Override
    public @NotNull RecipeType<DryingRackRecipe> getRecipeType() {
        return JEIPlugin.DryingRecipeType;
    }

    @Override
    public @NotNull Component getTitle() {
        return SlimeWorld.makeTranslation("jei", "drying_rack");
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return this.icon;
    }
    @Override
    public int getWidth() {
        return background.getWidth();
    }
    @Override
    public int getHeight() {
        return background.getHeight();
    }
    @Override
    public void draw(@NotNull DryingRackRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics, 0, 0);
        this.arrow.draw(guiGraphics, 34, 13);
        String coolingString = I18n.get(TIME, recipe.getDryingTime() / 20);
        Font fontRenderer = Minecraft.getInstance().font;
        guiGraphics.drawString(fontRenderer, coolingString, 72 - fontRenderer.width(coolingString) / 2, 2, Color.GRAY.getRGB(), false);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DryingRackRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,9,13).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 65, 13).addItemStack(recipe.getResultItem(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY)));
    }
}
