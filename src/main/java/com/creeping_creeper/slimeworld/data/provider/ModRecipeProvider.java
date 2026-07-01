package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModFluids;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.recipe.data.ICommonRecipeHelper;
import slimeknights.mantle.recipe.data.IRecipeHelper;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.common.registration.GeodeItemObject;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;
import slimeknights.tconstruct.shared.TinkerMaterials;

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

        String metal = "material/metal/";
        metalCrafting(consumer, ModItems.Bronze, metal);
        smeltingRecipes(consumer, RecipeCategory.MISC, ModItems.BronzeShard, ModItems.Bronze.getNugget(), metal, 0.2F, 50, true);
        smeltingRecipes(consumer, RecipeCategory.MISC, ModItems.CopperShard, TinkerMaterials.copperNugget, metal, 0.2F, 50, true);
        smeltingRecipes(consumer, RecipeCategory.MISC, ModItems.IronShard, Items.IRON_NUGGET, metal, 0.2F, 50, true);
        smeltingRecipes(consumer, RecipeCategory.MISC, ModItems.GoldShard, Items.GOLD_NUGGET, metal, 0.2F, 50, true);

        String slime = "material/ocean_slime/";
        geodeRecipes(consumer, ModItems.OceanGeode, ModItems.OceanSlimeBall, ModItems.SlimeGravel, ModFluids.OceanSlime, slime);
        packingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, "block", ModItems.OceanSlime, "ball", ModItems.OceanSlimeBall, slime);
        smallPackingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, "congealed", ModItems.OceanCongealedSlime, "ball", ModItems.OceanSlimeBall, slime);
        MeltingRecipeBuilder.melting(Ingredient.of(ModTags.Items.OCEAN_SLIME_BALL), ModFluids.OceanSlime, FluidValues.SLIMEBALL, 1.0f)
                .save(consumer, location(slime + "ball_melting"));
        MeltingRecipeBuilder.melting(Ingredient.of(ModItems.OceanCongealedSlime), ModFluids.OceanSlime, FluidValues.SLIME_CONGEALED, 2.0f)
                .save(consumer, location(slime + "congealed_melting"));
        MeltingRecipeBuilder.melting(Ingredient.of(ModItems.OceanSlime), ModFluids.OceanSlime, FluidValues.SLIME_BLOCK, 3.0f)
                .save(consumer, location(slime + "block_melting"));
        ItemCastingRecipeBuilder.basinRecipe(ModItems.OceanCongealedSlime)
                .setFluidAndTime(ModFluids.OceanSlime, FluidValues.SLIME_CONGEALED)
                .save(consumer, location(slime + "congealed_casting"));
//        ItemCastingRecipeBuilder.basinRecipe(TinkerWorld.slimyEnderbarkRoots.get(slimeType))
//                .setFluidAndTime(ModFluids.OceanSlime, FluidValues.SLIME_CONGEALED)
//                .setCast(TinkerWorld.enderbarkRoots, true)
//                .save(consumer, location(slime + "roots_casting"));
        ItemCastingRecipeBuilder.basinRecipe(ModItems.OceanSlime)
                .setFluidAndTime(ModFluids.OceanSlime, FluidValues.SLIME_BLOCK - FluidValues.SLIME_CONGEALED)
                .setCast(ModItems.OceanCongealedSlime, true)
                .save(consumer, location(slime + "block_casting"));
        ItemCastingRecipeBuilder.tableRecipe(ModItems.OceanSlimeBall)
                .setFluidAndTime(ModFluids.OceanSlime, FluidValues.SLIMEBALL)
                .save(consumer, location(slime + "slimeball_casting"));
        ItemCastingRecipeBuilder.tableRecipe(ModItems.OceanSlimeBottle)
                .setFluid(ModFluids.OceanSlime.ingredient(FluidValues.SLIMEBALL))
                .setCoolingTime(1)
                .setCast(Items.GLASS_BOTTLE, true)
                .save(consumer, location(slime + "bottle_casting"));
        ItemCastingRecipeBuilder.basinRecipe(ModItems.SlimeGravel)
                .setFluidAndTime(ModFluids.OceanSlime, FluidValues.SLIMEBALL * 2)
                .setCast(Blocks.GRAVEL, true)
                .save(consumer, location(slime + "gravel_casting"));
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

    private void smeltingRecipes(Consumer<FinishedRecipe> consumer,RecipeCategory category, ItemLike before, ItemLike after, String folder, float exp, int time, boolean addBlasting) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(before), category, after, exp, time)
                .unlockedBy("has_item", has(before))
                .save(consumer, wrap(id(after), folder, "_smelting"));
        if (addBlasting){
            SimpleCookingRecipeBuilder.blasting(Ingredient.of(before), category, after, exp, time / 2)
                    .unlockedBy("has_item", has(before))
                    .save(consumer, wrap(id(after), folder, "_blasting"));
        }
    }

        private void geodeRecipes(Consumer<FinishedRecipe> consumer, GeodeItemObject geode, ItemLike slimeBall, ItemLike slimeDirt, FluidObject<?> fluid, String folder) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, geode.getBlock())
                .define('#', geode.asItem())
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_item", has(geode.asItem()))
                .group("tconstruct:slime_crystal_block")
                .save(consumer, location(folder + "crystal_block"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(geode), RecipeCategory.MISC, slimeBall, 0.2f, 200)
                .unlockedBy("has_crystal", has(geode))
                .group("tconstruct:slime_crystal")
                .save(consumer, location(folder + "crystal_smelting"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(slimeDirt), RecipeCategory.MISC, geode, 0.2f, 400)
                .unlockedBy("has_dirt", has(slimeDirt))
                .group("tconstruct:slime_dirt")
                .save(consumer, location(folder + "crystal_growing"));
        MeltingRecipeBuilder.melting(Ingredient.of(geode), fluid, FluidValues.SLIMEBALL, 1.0f).save(consumer, location(folder + "crystal_melting"));
        MeltingRecipeBuilder.melting(Ingredient.of(geode.getBlock()), fluid, FluidValues.SLIMEBALL * 4, 2.0f).save(consumer, location(folder + "crystal_block_melting"));
        for (GeodeItemObject.BudSize bud : GeodeItemObject.BudSize.values()) {
            int size = bud.getSize();
            MeltingRecipeBuilder.melting(Ingredient.of(geode.getBud(bud)), fluid, FluidValues.SLIMEBALL * size, (size + 1) / 2f)
                    .setOre(IMeltingContainer.OreRateType.GEM)
                    .save(consumer, location(folder + "bud_" + bud.getName()));
        }
    }

    private void smallPackingRecipe(Consumer<FinishedRecipe> consumer, RecipeCategory category, String largeName, ItemLike large, String smallName, ItemLike small, String folder) {
        // four to one
        ResourceLocation largeId = id(large);
        ShapedRecipeBuilder.shaped(category, large)
                .define('#', small)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_item", RecipeProvider.has(small))
                .group(largeId.toString())
                .save(consumer, wrap(largeId, folder, String.format("_from_%ss", smallName)));
        // one to four
        ResourceLocation smallId = id(small);
        ShapelessRecipeBuilder.shapeless(category, small, 4)
                .requires(large)
                .unlockedBy("has_item", RecipeProvider.has(large))
                .group(smallId.toString())
                .save(consumer, wrap(smallId, folder, String.format("_from_%s", largeName)));
    }
}