package com.creeping_creeper.slimeworld.data.provider.recipes;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.builder.DryingRecipeBuilder;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModFluids;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.recipe.crafting.ShapedRetexturedRecipeBuilder;
import slimeknights.mantle.recipe.data.ICommonRecipeHelper;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.common.registration.GeodeItemObject;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.melting.IMeltingContainer;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.shared.block.SlimeType;

import java.util.function.Consumer;

public class ModCommonRecipeProvider extends RecipeProvider implements ICommonRecipeHelper {
    public ModCommonRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        String building = "building/";
        stairSlabWallCrafting(consumer, ModItems.Cinnabar, building, true);
        polishingRecipes(consumer, ModItems.Cinnabar, ModItems.PolishedCinnabar, building, true);
        stairSlabWallCrafting(consumer, ModItems.PolishedCinnabar, building, true);
        polishingRecipes(consumer, ModItems.PolishedCinnabar, ModItems.CinnabarBricks, building, true);
        stairSlabWallCrafting(consumer, ModItems.CinnabarBricks, building, true);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,  ModItems.ChiseledCinnabar)
                .define('s',  ModItems.Cinnabar.getSlab())
                .pattern("s")
                .pattern("s")
                .unlockedBy("has_item", has(ModItems.Cinnabar.getSlab()))
                .save(consumer, location(building + id(ModItems.ChiseledCinnabar).getPath()));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModItems.Cinnabar), RecipeCategory.BUILDING_BLOCKS, ModItems.ChiseledCinnabar)
                .unlockedBy("has_item", has(ModItems.Cinnabar))
                .save(consumer, location(building + id(ModItems.ChiseledCinnabar).getPath() + "_stonecutter"));
        stairSlabWallCrafting(consumer, ModItems.Sulfur, building, true);
        polishingRecipes(consumer, ModItems.Sulfur, ModItems.PolishedSulfur, building, true);
        stairSlabWallCrafting(consumer, ModItems.PolishedSulfur, building, true);
        polishingRecipes(consumer, ModItems.PolishedSulfur, ModItems.SulfurBricks, building, true);
        stairSlabWallCrafting(consumer, ModItems.SulfurBricks, building, true);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS,  ModItems.ChiseledSulfur)
                .define('s',  ModItems.Sulfur.getSlab())
                .pattern("s")
                .pattern("s")
                .unlockedBy("has_item", has(ModItems.Cinnabar.getSlab()))
                .save(consumer, location(building + id(ModItems.ChiseledSulfur).getPath()));
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModItems.Sulfur), RecipeCategory.BUILDING_BLOCKS, ModItems.ChiseledSulfur)
                .unlockedBy("has_item", has(ModItems.Sulfur))
                .save(consumer, location(building + id(ModItems.ChiseledSulfur).getPath() + "_stonecutter"));

        String gadgets = "gadgets/";
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.NecroticBoneMeal, 3)
                .requires(TinkerMaterials.necroticBone)
                .unlockedBy("has_item", RecipeProvider.has(TinkerMaterials.necroticBone))
                .save(consumer, location(gadgets + id(ModItems.NecroticBoneMeal).getPath()));
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.PotentSulfurNausea)
                .define('#', ModItems.Sulfur)
                .pattern("###")
                .pattern("###")
                .pattern("###")
                .unlockedBy("has_item", RecipeProvider.has(ModItems.Sulfur))
                .save(consumer, location(gadgets + id(ModItems.PotentSulfurNausea).getPath()));

        ShapedRetexturedRecipeBuilder.fromShaped(
                        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.DryingRack)
                                .define('w', ItemTags.WOODEN_SLABS)
                                .pattern("www")
                                .unlockedBy("has_item", has(ItemTags.WOODEN_SLABS)))
                .setSource('w')
                .setMatchAll()
                .build(consumer, prefix(ModItems.DryingRack, gadgets));
        String food = gadgets + "food/";

        DryingRecipeBuilder.drying(Items.BEEF, ModItems.BeefJerky).time(1200).save(consumer, location(food + id(ModItems.BeefJerky).getPath()));
        DryingRecipeBuilder.drying(Items.CHICKEN, ModItems.ChickenJerky).time(1200).save(consumer, location(food + id(ModItems.ChickenJerky).getPath()));
        DryingRecipeBuilder.drying(Items.PORKCHOP, ModItems.PorkJerky).time(1200).save(consumer, location(food + id(ModItems.PorkJerky).getPath()));
        DryingRecipeBuilder.drying(Items.MUTTON, ModItems.MuttonJerky).time(1200).save(consumer, location(food + id(ModItems.MuttonJerky).getPath()));
        DryingRecipeBuilder.drying(Items.RABBIT, ModItems.RabbitJerky).time(1200).save(consumer, location(food + id(ModItems.RabbitJerky).getPath()));
        DryingRecipeBuilder.drying(Items.ROTTEN_FLESH, ModItems.RottenFleshJerky).time(1200).save(consumer, location(food + id(ModItems.RottenFleshJerky).getPath()));
        DryingRecipeBuilder.drying(Items.SALMON, ModItems.SalmonJerky).time(1200).save(consumer, location(food + id(ModItems.SalmonJerky).getPath()));
        DryingRecipeBuilder.drying(Items.COD, ModItems.CodJerky).time(1200).save(consumer, location(food + id(ModItems.CodJerky).getPath()));
        DryingRecipeBuilder.drying(Items.TROPICAL_FISH, ModItems.TropicalFishJerky).time(1200).save(consumer, location(food + id(ModItems.TropicalFishJerky).getPath()));
        DryingRecipeBuilder.drying(Items.PUFFERFISH, ModItems.PufferfishJerky).time(1200).save(consumer, location(food + id(ModItems.PufferfishJerky).getPath()));
        DryingRecipeBuilder.drying(Items.SLIME_BALL, ModItems.EarthSlimeDrop).save(consumer, location(food + id(ModItems.EarthSlimeDrop).getPath()));
        DryingRecipeBuilder.drying(TinkerCommons.slimeball.get(SlimeType.SKY), ModItems.SkySlimeDrop).save(consumer, location(food + id(ModItems.SkySlimeDrop).getPath()));
        DryingRecipeBuilder.drying(ModItems.OceanSlimeBall, ModItems.OceanSlimeDrop).save(consumer, location(food + id(ModItems.OceanSlimeDrop).getPath()));
        DryingRecipeBuilder.drying(Items.MAGMA_CREAM, ModItems.MagmaSlimeDrop).save(consumer, location(food + id(ModItems.MagmaSlimeDrop).getPath()));
        DryingRecipeBuilder.drying(TinkerCommons.slimeball.get(SlimeType.ICHOR), ModItems.IchorSlimeDrop).save(consumer, location(food + id(ModItems.IchorSlimeDrop).getPath()));
        DryingRecipeBuilder.drying(TinkerCommons.slimeball.get(SlimeType.ENDER), ModItems.EnderSlimeDrop).save(consumer, location(food + id(ModItems.EnderSlimeDrop).getPath()));
        smeltingRecipes(consumer, RecipeCategory.FOOD, Ingredient.of(Tags.Items.EGGS), has(Tags.Items.EGGS), ModItems.FriedEgg, food, 0.7F, 200, 2);

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

        String material = "material/";
        smeltingRecipes(consumer, RecipeCategory.MISC, Ingredient.of(ModItems.GlowstoneOre, ModItems.DeepSlateGlowstoneOre), has(ModTags.Items.GLOWSTONE_ORE), Items.GLOWSTONE_DUST, material, 0.7F, 200, 1);
        packingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, "block", ModItems.IsomericGlowstone, "dust", Items.GLOWSTONE_DUST, material);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModItems.IsomericRedstoneBlock)
                .define('#', Items.REDSTONE)
                .pattern("##")
                .pattern("##")
                .unlockedBy("has_item", RecipeProvider.has(Items.REDSTONE))
                .save(consumer, wrap(id(ModItems.IsomericRedstoneBlock), material, String.format("_from_%ss", id(Items.REDSTONE).getPath())));

        String metal = material + "metal/";
        metalCrafting(consumer, ModItems.Bronze, metal);
        metalCrafting(consumer, ModItems.SlimeBronze, metal);
        smeltingRecipes(consumer, RecipeCategory.MISC, ModItems.BronzeShard, ModItems.Bronze.getNugget(), metal, 0.2F, 50, 1);
        smeltingRecipes(consumer, RecipeCategory.MISC, ModItems.CopperShard, TinkerMaterials.copperNugget, metal, 0.2F, 50, 1);
        smeltingRecipes(consumer, RecipeCategory.MISC, ModItems.IronShard, Items.IRON_NUGGET, metal, 0.2F, 50, 1);
        smeltingRecipes(consumer, RecipeCategory.MISC, ModItems.GoldShard, Items.GOLD_NUGGET, metal, 0.2F, 50, 1);

        String slime = material + "ocean_slime/";
        geodeRecipes(consumer, ModItems.OceanGeode, ModItems.OceanSlimeBall, ModItems.SlimeGravel, ModFluids.OceanSlime, slime);
        packingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, "block", ModItems.OceanSlime, "ball", ModItems.OceanSlimeBall, slime);
        smallPackingRecipe(consumer, RecipeCategory.BUILDING_BLOCKS, "congealed", ModItems.OceanCongealedSlime, "ball", ModItems.OceanSlimeBall, slime);

        String misc = "misc/";
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModFluids.LiquidMud)
                .requires(Ingredient.of(Items.WATER_BUCKET, Items.MUD))
                .unlockedBy("has_item", RecipeProvider.has(Items.MUD))
                .save(consumer, location(misc + id(ModFluids.LiquidMud).getPath()));
        DryingRecipeBuilder.drying(Items.WET_SPONGE, Items.SPONGE).time(100).save(consumer, location(misc + id(Items.SPONGE).getPath()));
        DryingRecipeBuilder.drying(Items.MUD, Items.CLAY).save(consumer, location(misc + id(Items.CLAY).getPath()));
        DryingRecipeBuilder.drying(Ingredient.of(ItemTags.SAPLINGS), Items.DEAD_BUSH).save(consumer, location(misc + id(Items.DEAD_BUSH).getPath()));
        DryingRecipeBuilder.drying(Items.CRYING_OBSIDIAN, Items.OBSIDIAN).save(consumer, location(misc + id(Items.OBSIDIAN).getPath()));

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

    private void smeltingRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike before, ItemLike after, String folder, float exp, int time, int type) {
        smeltingRecipes(consumer, category, Ingredient.of(before), has(before), after, folder, exp, time, type);
    }

    private void smeltingRecipes(Consumer<FinishedRecipe> consumer, RecipeCategory category, Ingredient before, CriterionTriggerInstance instance, ItemLike after, String folder, float exp, int time, int type) {
       switch (type){
           case 1 : {
               SimpleCookingRecipeBuilder.blasting(before, category, after, exp, time / 2)
                       .unlockedBy("has_item", instance)
                       .save(consumer, wrap(id(after), folder, "_blasting"));
           }
           case 2 : {
               SimpleCookingRecipeBuilder.smoking(before, category, after, exp, time / 2)
                   .unlockedBy("has_item", instance)
                   .save(consumer, wrap(id(after), folder, "_smoking"));
               SimpleCookingRecipeBuilder.campfireCooking(before, category, after, exp, time * 3)
                       .unlockedBy("has_item", instance)
                       .save(consumer, wrap(id(after), folder, "_campfire"));
           }
           default : {
               SimpleCookingRecipeBuilder.smelting(before, category, after, exp, time)
                       .unlockedBy("has_item", instance)
                       .save(consumer, wrap(id(after), folder, "_smelting"));
           }
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

    @Override
    public @NotNull String getModId() {
        return SlimeWorld.MODID;
    }

    @Override
    public @NotNull String getName(){
        return "Slime World Common Provider";
    }
}