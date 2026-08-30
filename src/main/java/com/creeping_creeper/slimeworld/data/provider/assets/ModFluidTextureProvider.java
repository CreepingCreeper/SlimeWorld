package com.creeping_creeper.slimeworld.data.provider.assets;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModFluids;
import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.fluid.texture.AbstractFluidTextureProvider;
import slimeknights.mantle.fluid.texture.FluidTexture;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.TConstruct;

import static slimeknights.tconstruct.fluids.TinkerFluids.withoutMolten;

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
public class ModFluidTextureProvider extends AbstractFluidTextureProvider {
    public ModFluidTextureProvider(PackOutput packOutput) {
        super(packOutput, SlimeWorld.MODID);
    }

    @Override
    public void addTextures() {
        tinted(ModFluids.ResonanceSlime, "fluid/liquid/", 0xFF7cf6fc);
        slime(ModFluids.OceanSlime, "ocean");
        texture(ModFluids.LiquidMud).still(ResourceLocation.withDefaultNamespace("block/mud")).flowing(ResourceLocation.withDefaultNamespace("block/mud")).calculateFogColor(true).fog(FogShape.SPHERE, 0.25f, 8);
        alloy(ModFluids.MoltenSlimeBronze);
        tinted(ModFluids.Mercury, "fluid/liquid/", 0xFFFFFFFF);
        tinted(ModFluids.SulfuricAcid, "fluid/liquid/", 0xFFFFFFFF);
    }

    private FluidTexture.Builder root(FluidObject<?> fluid) {
        return texture(fluid).wrapId("fluid/", "/", false, false);
    }

    private FluidTexture.Builder named(FluidObject<?> fluid, String name) {
        return texture(fluid).root(SlimeWorld.getResource("fluid/" + name + "/"))
                .still().flowing().camera().calculateFogColor(true).fog(FogShape.SPHERE, 0.25f, 2);
    }

    private FluidTexture.Builder moltenFolder(FluidObject<?> fluid, String folder) {
        return named(fluid, "molten/" + folder + "/" + withoutMolten(fluid));
    }

    private FluidTexture.Builder slime(FluidObject<?> fluid, String name) {
        return named(fluid, "slime/" + name);
    }

    private FluidTexture.Builder alloy(FluidObject<?> fluid) {
        return moltenFolder(fluid, "alloy");
    }

    private FluidTexture.Builder tinted(FluidObject<?> fluid, String location, int color){
        return texture(fluid).root(TConstruct.getResource(location)).still().flowing().calculateFogColor(true).fog(FogShape.SPHERE, 0.25f, 2).color(color);

    }

    @Override
    public @NotNull String getName() {
        return "Slime World Fluid Texture Providers";
    }
}
