package com.creeping_creeper.slimeworld;

import com.creeping_creeper.slimeworld.events.EntityEvents;
import com.creeping_creeper.slimeworld.events.WorldEvents;
import com.creeping_creeper.slimeworld.init.*;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.utils.Util;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SlimeWorld.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SlimeWorld {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "slimeworld";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public SlimeWorld() {
      IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
      bus.addListener(this::commonSetup);
      ModItems.registers(bus);
      ModFluids.registers(bus);
      ModModifiers.registers(bus);
      ModPotions.registers(bus);
      ModEffects.registers(bus);
      ModEntities.registers(bus);
      ModFeature.registers(bus);
    }
    public void commonSetup(final FMLCommonSetupEvent event) {
        ModPotions.init();
        WorldEvents.init();
        MinecraftForge.EVENT_BUS.register(new EntityEvents());
    }

    public static String makeTranslationKey(String base, String name) {
        return Util.makeTranslationKey(base, getResource(name));
    }
    public static MutableComponent makeTranslation(String base, String name) {
        return Component.translatable(makeTranslationKey(base, name));
    }
    public static MutableComponent makeTranslation(String base, String name, Object... arguments) {
        return Component.translatable(makeTranslationKey(base, name), arguments);
    }
    public static String makeDescriptionId(String type, String name) {
        return type + "." + MODID + "." + name;
    }
    public static ResourceLocation getResource(String name) {
        return new ResourceLocation(MODID, name);
    }
    public static <T> TinkerDataCapability.TinkerDataKey<T> createKey(String name) {
        return TinkerDataCapability.TinkerDataKey.of(getResource(name));
    }
}
