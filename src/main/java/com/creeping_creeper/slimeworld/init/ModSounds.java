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

    public static final RegistryObject<SoundEvent> SULFUR_CUBE_HURT = registerSoundEvent("entity.sulfur_cube.hurt");
    public static final RegistryObject<SoundEvent> SULFUR_CUBE_DEATH = registerSoundEvent("entity.sulfur_cube.death");
    public static final RegistryObject<SoundEvent> SULFUR_CUBE_JUMP = registerSoundEvent("entity.sulfur_cube.jump");
    public static final RegistryObject<SoundEvent> SULFUR_CUBE_SQUISH = registerSoundEvent("entity.sulfur_cube.squish");
    public static final RegistryObject<SoundEvent> SULFUR_CUBE_BOUNCE = registerSoundEvent("entity.sulfur_cube.bounce");
    public static final RegistryObject<SoundEvent> SULFUR_CUBE_HIT = registerSoundEvent("entity.sulfur_cube.hit");
    public static final RegistryObject<SoundEvent> SULFUR_CUBE_PUSH = registerSoundEvent("entity.sulfur_cube.push");
    public static final RegistryObject<SoundEvent> SULFUR_CUBE_ABSORB = registerSoundEvent("entity.sulfur_cube.absorb");
    public static final RegistryObject<SoundEvent> SULFUR_CUBE_EJECT = registerSoundEvent("entity.sulfur_cube.eject");

    public static final RegistryObject<SoundEvent> SULFUR_CUBE_HURT_SMALL = registerSoundEvent("entity.small_sulfur_cube.hurt");
    public static final RegistryObject<SoundEvent> SULFUR_CUBE_DEATH_SMALL = registerSoundEvent("entity.small_sulfur_cube.death");
    public static final RegistryObject<SoundEvent> SULFUR_CUBE_JUMP_SMALL = registerSoundEvent("entity.small_sulfur_cube.jump");
    public static final RegistryObject<SoundEvent> SULFUR_CUBE_SQUISH_SMALL = registerSoundEvent("entity.small_sulfur_cube.squish");

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
