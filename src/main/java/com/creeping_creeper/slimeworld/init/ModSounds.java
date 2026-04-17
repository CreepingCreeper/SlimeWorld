package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SlimeWorld.MODID);

    public static final RegistryObject<SoundEvent> SULFUR_BREAK = registerSoundEvent("block.sulfur.break");
    public static final RegistryObject<SoundEvent> SULFUR_STEP = registerSoundEvent("block.sulfur.step");
    public static final RegistryObject<SoundEvent> SULFUR_PLACE = registerSoundEvent("block.sulfur.place");
    public static final RegistryObject<SoundEvent> SULFUR_HIT = registerSoundEvent("block.sulfur.hit");
    public static final RegistryObject<SoundEvent> SULFUR_FALL = registerSoundEvent("block.sulfur.fall");

    public static final RegistryObject<SoundEvent> POTENT_SULFUR_BREAK = registerSoundEvent("block.potent_sulfur.break");
    public static final RegistryObject<SoundEvent> POTENT_SULFUR_STEP = registerSoundEvent("block.potent_sulfur.step");
    public static final RegistryObject<SoundEvent> POTENT_SULFUR_PLACE = registerSoundEvent("block.potent_sulfur.place");
    public static final RegistryObject<SoundEvent> POTENT_SULFUR_HIT = registerSoundEvent("block.potent_sulfur.hit");
    public static final RegistryObject<SoundEvent> POTENT_SULFUR_FALL = registerSoundEvent("block.potent_sulfur.fall");
    public static final RegistryObject<SoundEvent> NOXIOUS_GAS = registerSoundEvent("block.potent_sulfur.noxious_gas");

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

    public static final SoundType SULFUR = new ForgeSoundType(1.0F, 1.0F, SULFUR_BREAK, SULFUR_STEP, SULFUR_PLACE, SULFUR_HIT, SULFUR_FALL);
    public static final SoundType POTENT_SULFUR = new ForgeSoundType(1.0F, 1.0F, SULFUR_BREAK, SULFUR_STEP, SULFUR_PLACE, SULFUR_HIT, SULFUR_FALL);

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(SlimeWorld.getResource(name)));
    }

    public static void registers(IEventBus bus) {
        SOUND_EVENTS.register(bus);
    }
}
