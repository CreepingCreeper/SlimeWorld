package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.tconstruct.common.registration.FluidDeferredRegisterExtension;
import slimeknights.tconstruct.fluids.fluids.SlimeFluid;
import slimeknights.tconstruct.shared.TinkerEffects;

import static net.minecraft.world.level.material.MapColor.COLOR_BLUE;
import static net.minecraft.world.level.material.MapColor.COLOR_RED;
import static slimeknights.tconstruct.fluids.block.BurningLiquidBlock.createBurning;
import static slimeknights.tconstruct.fluids.block.MobEffectLiquidBlock.createEffect;

public class ModFluids {
    protected static final FluidDeferredRegisterExtension FLUIDS = new FluidDeferredRegisterExtension(SlimeWorld.MODID);

    public static final FlowingFluidObject<SlimeFluid> OceanSlime = FLUIDS.registerSlime("ocean_slime").type(slime("ocean_slime").temperature(370).lightLevel(1)).block(createEffect(COLOR_BLUE,1, () -> new MobEffectInstance(TinkerEffects.doubleJump.get(), 5*20))).bucket().flowing(SlimeFluid.Source::new, SlimeFluid.Flowing::new);
    public static final FlowingFluidObject<ForgeFlowingFluid> ResonanceSlime = FLUIDS.registerSlime("resonance_slime").type(hot("resonance_slime").temperature(1250).lightLevel(15)).block(createBurning(COLOR_RED,12, 10, 4f)).bucket().flowing();

    private static FluidType.Properties gas(String name) {
        return base(name).density(-2000)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY);
    }
    private static FluidType.Properties common(String name) {
        return base(name).density(2000)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .canExtinguish(true).canHydrate(true);
    }
    private static FluidType.Properties slime(String name) {
        return common(name).density(1600).viscosity(1600);
    }
    private static FluidType.Properties hot(String name) {
        return base(name).density(2000).viscosity(10000)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                .canSwim(false).canDrown(false)
                .pathType(BlockPathTypes.LAVA).adjacentPathType(null);
    }

    private static FluidType.Properties base(String name) {
        return FluidType.Properties.create()
                .descriptionId(SlimeWorld.makeDescriptionId("fluid", name))
                .motionScale(0.0023333333333333335D);
    }

    public static void registers(IEventBus bus) {
        FLUIDS.register(bus);
    }
}
