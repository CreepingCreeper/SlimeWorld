package com.creeping_creeper.slimeworld.data.provider.recipes;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModMaterialIds;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModFluids;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

import java.util.function.Consumer;

public class ModMaterialRecipeProvider extends RecipeProvider implements IMaterialRecipeHelper {
    public ModMaterialRecipeProvider(PackOutput generator) {
        super(generator);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        String tool = "tools/";
        String material = tool + "materials/";
        // tier 1
        materialRecipe(consumer, ModMaterialIds.kelp, Ingredient.of(Items.KELP), 1, 4, material + "kelp");
        // tier 3
        metalMaterialRecipe(consumer, ModMaterialIds.slimeBronze, material, "slime_bronze", false);
        materialMeltingCasting(consumer, ModMaterialIds.slimeBronze, ModFluids.MoltenSlimeBronze, material);

        // ammo
        materialRecipe(consumer, ModMaterialIds.oceanslime, Ingredient.of(ModItems.OceanGeode),1, 1, material + "oceanslime");
        materialRecipe(consumer, MaterialVariantId.create(MaterialIds.slimeball, "ocean"), Ingredient.of(ModTags.Items.OCEAN_SLIME_BALL), 1, 1, material + "slimeball/ocean");
        // slimeskin
        String slimeskin = material + "slimeskin/";
        materialComposite(consumer, MaterialIds.leather, ModMaterialIds.oceanSlimeskin, ModFluids.OceanSlime, FluidValues.SLIMEBALL, slimeskin, "ocean");
        materialComposite(consumer, ModMaterialIds.oceanSlimeskin, MaterialIds.leather, ModFluids.OceanSlime, FluidValues.SIP, slimeskin, "ocean_cleaning");
    }


    @Override
    public @NotNull String getModId() {
        return SlimeWorld.MODID;
    }

    @Override
    public @NotNull String getName(){
        return "Slime World Material Recipe Provider";
    }
}
