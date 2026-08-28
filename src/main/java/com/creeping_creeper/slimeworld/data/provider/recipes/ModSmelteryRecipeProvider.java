package com.creeping_creeper.slimeworld.data.provider.recipes;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.ModFluids;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.recipe.ingredient.EntityIngredient;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.entitymelting.EntityMeltingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;

import java.util.function.Consumer;

public class ModSmelteryRecipeProvider extends RecipeProvider implements ISmelteryRecipeHelper {
    public ModSmelteryRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public @NotNull String getModId() {
        return SlimeWorld.MODID;
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        String gadgets = "gadgets/";
        AlloyRecipeBuilder.alloy(ModFluids.ResonanceSlime, FluidValues.SLIMEBALL * 2)
                .addInput(TinkerTags.Fluids.SLIME, FluidValues.SLIMEBALL)
                .addInput(TinkerTags.Fluids.SLIME, FluidValues.SLIMEBALL)
                .addInput(TinkerFluids.moltenEnder.ingredient(FluidValues.SLIMEBALL))
                .save(consumer, prefix(ModFluids.ResonanceSlime, gadgets));
        AlloyRecipeBuilder.alloy(ModFluids.ResonanceSlime, FluidValues.INGOT * 2)
                .addInput(ModFluids.OceanSlime.getTag(), FluidValues.SLIMEBALL)
                .addInput(TinkerFluids.moltenCopper.getTag(), FluidValues.INGOT)
                .addInput(TinkerFluids.moltenQuartz.ingredient(FluidValues.GEM))
                .save(consumer, prefix(ModFluids.MoltenSlimeBronze, gadgets));

        String material = "material/";
        String metal = material + "metal/";
        MeltingRecipeBuilder.melting(Ingredient.of(ModItems.BronzeCluster), TinkerFluids.moltenBronze, FluidValues.NUGGET * 4, 5/2f)
                .save(consumer, location(metal + id(ModItems.BronzeCluster).getPath() + "_melting"));
        String melting = metal + "melting/";
        String casting = metal + "casting/";
        MeltingRecipeBuilder.melting(Ingredient.of(ModTags.Items.RAW_BRONZE_NUGGET), TinkerFluids.moltenBronze, FluidValues.NUGGET, 1/2f)
                .save(consumer, location(melting + id(ModItems.BronzeShard).getPath()));
        MeltingRecipeBuilder.melting(Ingredient.of(ModTags.Items.RAW_COPPER_NUGGET), TinkerFluids.moltenCopper, FluidValues.NUGGET, 1/2f)
                .save(consumer, location(melting + id(ModItems.CopperShard).getPath()));
        MeltingRecipeBuilder.melting(Ingredient.of(ModTags.Items.RAW_IRON_NUGGET), TinkerFluids.moltenIron, FluidValues.NUGGET, 1/2f)
                .save(consumer, location(melting + id(ModItems.IronShard).getPath()));
        MeltingRecipeBuilder.melting(Ingredient.of(ModTags.Items.RAW_GOLD_NUGGET), TinkerFluids.moltenGold, FluidValues.NUGGET, 1/2f)
                .save(consumer, location(melting + id(ModItems.GoldShard).getPath()));
        molten(consumer, ModFluids.MoltenSlimeBronze).castingFolder(casting).meltingFolder(melting).metal();

        String slime = material + "ocean_slime/";
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
        ItemCastingRecipeBuilder.basinRecipe(Items.MUD)
                .setFluidAndTime(ModFluids.LiquidMud, FluidValues.BRICK * 4)
                .save(consumer, location(misc + "mud_casting"));

        EntityMeltingRecipeBuilder.melting(EntityIngredient.of(ModEntities.IchorSlimeEntity.get()), TinkerFluids.ichor.result(FluidValues.SLIMEBALL / 10))
                .save(consumer, prefix(ModEntities.IchorSlimeEntity, misc));
        EntityMeltingRecipeBuilder.melting(EntityIngredient.of(ModEntities.OceanSlimeEntity.get()), ModFluids.OceanSlime.result(FluidValues.SLIMEBALL / 10))
                .save(consumer, prefix(ModEntities.OceanSlimeEntity, misc));
    }

    @Override
    public @NotNull String getName(){
        return "Slime World Smeltery Recipe Provider";
    }
}