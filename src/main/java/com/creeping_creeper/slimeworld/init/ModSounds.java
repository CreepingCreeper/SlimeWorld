package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SlimeWorld.MODID);

    public static final RegistryObject<SoundEvent> BOGGED_AMBIENT = registerSoundEvent("entity.bogged.ambient");
    public static final RegistryObject<SoundEvent> BOGGED_HURT = registerSoundEvent("entity.bogged.hurt");
    public static final RegistryObject<SoundEvent> BOGGED_DEATH = registerSoundEvent("entity.bogged.death");
    public static final RegistryObject<SoundEvent> BOGGED_SHEAR = registerSoundEvent("entity.bogged.shear");
    public static final RegistryObject<SoundEvent> BOGGED_STEP = registerSoundEvent("entity.bogged.step");

    public static final RegistryObject<SoundEvent> PARCHED_AMBIENT = registerSoundEvent("entity.parched.ambient");
    public static final RegistryObject<SoundEvent> PARCHED_HURT = registerSoundEvent("entity.parched.hurt");
    public static final RegistryObject<SoundEvent> PARCHED_DEATH = registerSoundEvent("entity.parched.death");
    public static final RegistryObject<SoundEvent> PARCHED_STEP = registerSoundEvent("entity.parched.step");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = SlimeWorld.getResource(name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void registers(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
