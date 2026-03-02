package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.effect.SlimeResonanceEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EnumDeferredRegister;
import slimeknights.tconstruct.common.TinkerEffect;
import slimeknights.tconstruct.shared.TinkerAttributes;

public class ModEffects {
    protected static final EnumDeferredRegister<MobEffect> MOB_EFFECTS = new EnumDeferredRegister<>(Registries.MOB_EFFECT, SlimeWorld.MODID);

    public static final RegistryObject<MobEffect> SlimeResonance = MOB_EFFECTS.register("slime_resonance",() -> new SlimeResonanceEffect(MobEffectCategory.NEUTRAL, 0x8CD782,true));
    public static final RegistryObject<TinkerEffect> Blessing = MOB_EFFECTS.register("blessing",() -> new TinkerEffect(MobEffectCategory.BENEFICIAL, 0xFFFFFF,true)
            .addAttributeModifier(TinkerAttributes.GOOD_EFFECT_DURATION.get(),"2602DE5E-7CE8-4241-940E-647C1F170001",0.25,AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(TinkerAttributes.BAD_EFFECT_DURATION.get(),"2602DE5E-7CE8-4241-940E-647C1F170002",-0.25,AttributeModifier.Operation.MULTIPLY_BASE));
    public static final RegistryObject<TinkerEffect> Curse = MOB_EFFECTS.register("curse",() -> new TinkerEffect(MobEffectCategory.HARMFUL, 0x000000,true)
            .addAttributeModifier(TinkerAttributes.GOOD_EFFECT_DURATION.get(),"2602DE5E-7CE8-4241-940E-647C1F170003",-0.25, AttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(TinkerAttributes.BAD_EFFECT_DURATION.get(),"2602DE5E-7CE8-4241-940E-647C1F170004",0.25, AttributeModifier.Operation.MULTIPLY_BASE));

    public static void registers(IEventBus bus) {
        MOB_EFFECTS.register(bus);
    }
}
