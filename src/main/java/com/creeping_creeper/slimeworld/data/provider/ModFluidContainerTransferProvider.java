package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModFluids;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.datagen.MantleTags;
import slimeknights.mantle.fluid.transfer.AbstractFluidContainerTransferProvider;
import slimeknights.mantle.fluid.transfer.FillFluidContainerTransfer;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.recipe.FluidValues;

public class ModFluidContainerTransferProvider extends AbstractFluidContainerTransferProvider {
    public ModFluidContainerTransferProvider(PackOutput packOutput) {
        super(packOutput, SlimeWorld.MODID);
    }

    @Override
    protected void addTransfers() {
        addTransfer("ocean_slime_bottle_fill", new FillFluidContainerTransfer(Ingredient.of(Items.GLASS_BOTTLE), ItemOutput.fromItem(ModItems.OceanSlimeBottle), ModFluids.OceanSlime.ingredient(FluidValues.BOTTLE)));
        addFillEmpty("water_bubble_", ModItems.WaterBubble, ModItems.Bubble, Fluids.WATER, MantleTags.Fluids.WATER, 1000, false);
        addFillEmpty("lava_bubble_", ModItems.LavaBubble, ModItems.Bubble, Fluids.LAVA, MantleTags.Fluids.LAVA, 1000, false);
        addFillEmpty("earth_slime_bubble_", ModItems.EarthSlimeBubble, ModItems.Bubble, TinkerFluids.earthSlime, 1000, false);
        addFillEmpty("sky_slime_bubble_", ModItems.SkySlimeBubble, ModItems.Bubble, TinkerFluids.skySlime, 1000, false);
        addFillEmpty("ocean_slime_bubble_", ModItems.OceanSlimeBubble, ModItems.Bubble, ModFluids.OceanSlime, 1000, false);
        addFillEmpty("ichor_bubble_", ModItems.IchorBubble, ModItems.Bubble, TinkerFluids.ichor, 1000, false);
        addFillEmpty("ender_slime_bubble_", ModItems.EnderSlimeBubble, ModItems.Bubble, TinkerFluids.enderSlime, 1000, false);
        addFillEmpty("honey_bubble_", ModItems.HoneyBubble, ModItems.Bubble, TinkerFluids.honey, 1000, false);
        addFillEmpty("venom_bubble_", ModItems.VenomBubble, ModItems.Bubble, TinkerFluids.venom, 1000, false);

    }
    @Override
    public @NotNull String getName() {
        return "Slime World Fluid Container Transfer";
    }
}
