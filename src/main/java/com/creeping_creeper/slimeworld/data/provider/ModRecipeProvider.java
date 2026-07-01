package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.ModFluids;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.advancements.CriterionTriggerInstance;
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
import slimeknights.mantle.recipe.ingredient.EntityIngredient;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.GeodeItemObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipe;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.world.TinkerWorld;

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

        String gadgets = "gadgets/";
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.NecroticBoneMeal, 3)
                .requires(TinkerMaterials.necroniumBone)
                .unlockedBy("has_item", RecipeProvider.has(TinkerMaterials.necroniumBone))
                .save(consumer, location(gadgets + id(ModItems.NecroticBoneMeal).getPath()));
        AlloyRecipeBuilder.alloy(ModFluids.ResonanceSlime, FluidValues.SLIMEBALL * 2)
                .addInput(TinkerTags.Fluids.SLIME, FluidValues.SLIMEBALL)
                .addInput(TinkerTags.Fluids.SLIME, FluidValues.SLIMEBALL)
                .addInput(TinkerFluids.moltenEnder.ingredient(FluidValues.SLIMEBALL))
                .save(consumer, prefix(ModFluids.ResonanceSlime, gadgets));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.PotentSulfurNausea)
                .define('#', ModItems.Sulfur)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_item", RecipeProvider.has(ModItems.Sulfur))
                .save(consumer, location(gadgets + id(ModItems.PotentSulfurNausea).getPath()));

        String material = "material/";
        smeltingRecipes(consumer, RecipeCategory.MISC, Ingredient.of(ModItems.GlowstoneOre, ModItems.DeepSlateGlowstoneOre), has(ModTags.Items.GLOWSTONE_ORE), Items.GLOWSTONE_DUST, material, 0.7F, 200, true);
        packingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, "block", ModItems.IsomericGlowstone, "dust", Items.GLOWSTONE_DUST, material);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.IsomericRedstoneBlock)
                .define('#', Items.REDSTONE)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_item", RecipeProvider.has(Items.REDSTONE))
                .save(consumer, wrap(id(ModItems.IsomericRedstoneBlock), material, String.format("_from_%ss", id(Items.REDSTONE).getPath())));

        String metal = material + "metal/";
        metalCrafting(consumer, ModItems.Bronze, metal);
        MeltingRecipeBuilder.melting(Ingredient.of(ModItems.BronzeCluster), TinkerFluids.moltenBronze, FluidValues.NUGGET * 4, 5/2f)
                .save(consumer, location(metal + id(ModItems.BronzeCluster).getPath() + "_melting"));
        smeltingRecipes(consumer, RecipeCategory.MISC, ModItems.BronzeShard, ModItems.Bronze.getNugget(), metal, 0.2F, 50, true);
        smeltingRecipes(consumer, RecipeCategory.MISC, ModItems.CopperShard, TinkerMaterials.copperNugget, metal, 0.2F, 50, true);
        smeltingRecipes(consumer, RecipeCategory.MISC, ModItems.IronShard, Items.IRON_NUGGET, metal, 0.2F, 50, true);
        smeltingRecipes(consumer, RecipeCategory.MISC, ModItems.GoldShard, Items.GOLD_NUGGET, metal, 0.2F, 50, true);
        MeltingRecipeBuilder.melting(Ingredient.of(ModItems.BronzeShard), TinkerFluids.moltenBronze, FluidValues.NUGGET, 1/2f)
                .save(consumer, location(metal + id(ModItems.BronzeShard).getPath() + "_melting"));
        MeltingRecipeBuilder.melting(Ingredient.of(ModItems.CopperShard), TinkerFluids.moltenCopper, FluidValues.NUGGET, 1/2f)
                .save(consumer, location(metal + id(ModItems.CopperShard).getPath() + "_melting"));
        MeltingRecipeBuilder.melting(Ingredient.of(ModItems.IronShard), TinkerFluids.moltenIron, FluidValues.NUGGET, 1/2f)
                .save(consumer, location(metal + id(ModItems.IronShard).getPath() + "_melting"));
        MeltingRecipeBuilder.melting(Ingredient.of(ModItems.GoldShard), TinkerFluids.moltenGold, FluidValues.NUGGET, 1/2f)
                .save(consumer, location(metal + id(ModItems.GoldShard).getPath() + "_melting"));

        String slime = material + "ocean_slime/";
        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.OceanCake)
                .define('M', ModFluids.OceanSlime.asItem())
                .define('S', Ingredient.of(Items.SUGAR))
                .define('E', Items.EGG)
                .define('W', Items.SEAGRASS)
                .pattern("MMM").
                pattern("SES").
                pattern("WWW")
                .unlockedBy("has_item", RecipeProvider.has(Items.SEAGRASS))
                .save(consumer, location(gadgets + id(ModItems.OceanCake).getPath()));
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

        String misc = "misc/";
        EntityMeltingRecipeBuilder.melting(EntityIngredient.of(ModEntities.IchorSlimeEntity.get()), TinkerFluids.ichor.result(FluidValues.SLIMEBALL / 10))
                .save(consumer, prefix(ModEntities.IchorSlimeEntity, misc));
        EntityMeltingRecipeBuilder.melting(EntityIngredient.of(ModEntities.OceanSlimeEntity.get()), ModFluids.OceanSlime.result(FluidValues.SLIMEBALL / 10))
                .save(consumer, prefix(ModEntities.OceanSlimeEntity, misc));
    }

    private void polishingRecipes(Consumer<FinishedRecipe> consumer, ItemLike before, ItemLike after, String folder, boolean addStonecutter) {
        ResourceLocation afterId = id(after);
        InventoryChangeTrigger.TriggerInstance hasBlock = RecipeProvider.has(before);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, after, 4)
                .define('B', before)
                .pattern("BB")
                .pattern("BB")
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

    private void smeltingRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike before, ItemLike after, String folder, float exp, int time, boolean addBlasting) {
        smeltingRecipes(consumer, category, Ingredient.of(before), has(before), after, folder, exp, time, addBlasting);
    }

    private void smeltingRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory category, Ingredient before, CriterionTriggerInstance instance, ItemLike after, String folder, float exp, int time, boolean addBlasting) {
        SimpleCookingRecipeBuilder.smelting(before, category, after, exp, time)
                .unlockedBy("has_item", instance)
                .save(consumer, wrap(id(after), folder, "_smelting"));
        if (addBlasting){
            SimpleCookingRecipeBuilder.blasting(before, category, after, exp, time / 2)
                    .unlockedBy("has_item", instance)
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
                    .save(consumer, location(folder + "bud_" + bud.getName() + "_melting"));
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