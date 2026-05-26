package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.modifiers.*;
import net.minecraftforge.eventbus.api.IEventBus;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class ModModifiers {
    private static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(SlimeWorld.MODID);
    public static final StaticModifier<SputteringModifier> sputtering = MODIFIERS.register("sputtering", SputteringModifier::new);
    public static final StaticModifier<FrugalModifier> frugal = MODIFIERS.register("frugal", FrugalModifier::new);
    public static final StaticModifier<SteadfastModifier> steadfast = MODIFIERS.register("steadfast", SteadfastModifier::new);
    public static final StaticModifier<FiendishModifier> fiendish = MODIFIERS.register("fiendish", FiendishModifier::new);
    public static final StaticModifier<MajesticModifier> majestic = MODIFIERS.register("majestic", MajesticModifier::new);
    public static final StaticModifier<OverTomatoModifier> overTomato = MODIFIERS.register("overtomato", OverTomatoModifier::new);

    public static void registers(IEventBus bus) {
        MODIFIERS.register(bus);
    }
}
