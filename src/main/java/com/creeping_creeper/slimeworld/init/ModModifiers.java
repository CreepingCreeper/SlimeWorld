package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.modifiers.*;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegisterEvent;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

import static com.creeping_creeper.slimeworld.SlimeWorld.getResource;

@SuppressWarnings("unused")
public class ModModifiers {
    private static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(SlimeWorld.MODID);
    public static final StaticModifier<FrugalModifier> frugal = MODIFIERS.register("frugal", FrugalModifier::new);
    public static final StaticModifier<FiendishModifier> fiendish = MODIFIERS.register("fiendish", FiendishModifier::new);
    public static final StaticModifier<MajesticModifier> majestic = MODIFIERS.register("majestic", MajesticModifier::new);

    @SubscribeEvent
    static void registerSerializers(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            ModifierModule.LOADER.register(getResource("sputtering"), SputteringModule.LOADER);
            ModifierModule.LOADER.register(getResource("overload"), OverloadModule.LOADER);
            ModifierModule.LOADER.register(getResource("overtomato"), OverTomatoModule.LOADER);
            ModifierModule.LOADER.register(getResource("steadfast"), SteadfastModule.LOADER);
            ModifierModule.LOADER.register(getResource("unyielding"), UnyieldingModule.LOADER);
            ModifierModule.LOADER.register(getResource("crit"), CritModule.INSTANCE.getLoader());
        }
    }

    public static void registers(IEventBus bus) {
        MODIFIERS.register(bus);
    }
}
