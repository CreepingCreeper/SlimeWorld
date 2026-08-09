package com.creeping_creeper.slimeworld.data.provider.tinkering;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModEffects;
import com.creeping_creeper.slimeworld.init.ModFluids;
import net.minecraft.data.PackOutput;
import net.minecraft.world.effect.MobEffects;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.tinkering.AbstractFluidEffectProvider;
import slimeknights.tconstruct.library.modifiers.fluid.FluidMobEffect;
import slimeknights.tconstruct.library.modifiers.fluid.TimeAction;
import slimeknights.tconstruct.library.modifiers.fluid.block.BreakBlockFluidEffect;

public class ModFluidEffectProvider extends AbstractFluidEffectProvider {
    public ModFluidEffectProvider(PackOutput packOutput) {
        super(packOutput, SlimeWorld.MODID);
    }

    @Override
    protected void addFluids() {
        addSlime(ModFluids.OceanSlime)
                .addEntityEffects(FluidMobEffect.builder().effect(ModEffects.Floating.get(), 20 * 7).buildEntity(TimeAction.ADD))
                .addBlockEffect(new BreakBlockFluidEffect(2));
        addSlime(ModFluids.ResonanceSlime)
                .addEffect(FluidMobEffect.builder().effect(ModEffects.SlimeResonance.get(), 20 * 4), TimeAction.SET);
        addSlime(ModFluids.LiquidMud)
                .addEffect(FluidMobEffect.builder().effect(MobEffects.MOVEMENT_SLOWDOWN, 20, 4), TimeAction.SET);

    }

    @Override
    public @NotNull String getName() {
        return "Slime World Fluid Provider";
    }
}
