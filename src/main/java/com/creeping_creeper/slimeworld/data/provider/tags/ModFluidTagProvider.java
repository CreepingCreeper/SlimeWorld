package com.creeping_creeper.slimeworld.data.provider.tags;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.tconstruct.common.TinkerTags;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("unchecked")
public class ModFluidTagProvider extends FluidTagsProvider {
     public ModFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider lookupProvider) {
        //common
        //tconstruct
        tag(TinkerTags.Fluids.SLIME_TOOLTIPS).addTags(ModFluids.OceanSlime.getTag(), ModFluids.ResonanceSlime.getTag());
        tag(TinkerTags.Fluids.SLIME).addTags(ModFluids.OceanSlime.getTag(), ModFluids.ResonanceSlime.getTag());
        tag(TinkerTags.Fluids.CLAY_TOOLTIPS).addTag(ModFluids.LiquidMud.getTag());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addTags(ModFluids.MoltenSlimeBronze.getTag(), ModFluids.Mercury.getTag());
        //self
        fluidTag(ModFluids.OceanSlime);
        fluidTag(ModFluids.ResonanceSlime);
        fluidTag(ModFluids.LiquidMud);
        fluidTag(ModFluids.MoltenSlimeBronze);
        fluidTag(ModFluids.Mercury);
    }

    private void fluidTag(FlowingFluidObject<?> fluid) {
        tag(fluid.getLocalTag()).add(fluid.getStill(), fluid.getFlowing());
        TagKey<Fluid> tag = fluid.getCommonTag();
        if (tag != null) {
            tag(tag).addTag(fluid.getLocalTag());
        }
    }
}
