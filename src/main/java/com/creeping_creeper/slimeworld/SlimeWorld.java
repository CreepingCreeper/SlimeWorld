package com.creeping_creeper.slimeworld;

import com.creeping_creeper.slimeworld.data.key.ModDataKeys;
import com.creeping_creeper.slimeworld.data.provider.*;
import com.creeping_creeper.slimeworld.data.provider.assets.*;
import com.creeping_creeper.slimeworld.data.provider.tags.*;
import com.creeping_creeper.slimeworld.data.provider.tinkering.*;
import com.creeping_creeper.slimeworld.events.EntityEvents;
import com.creeping_creeper.slimeworld.events.WorldEvents;
import com.creeping_creeper.slimeworld.init.*;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.utils.Util;

@Mod(SlimeWorld.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SlimeWorld {
    public static final String MODID = "slimeworld";
    public static final Logger LOG = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public SlimeWorld() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.registers(bus);
        ModFluids.registers(bus);
        ModModifiers.registers(bus);
        bus.register(ModModifiers.class);
        ModEffects.registers(bus);
        ModEntities.registers(bus);
        ModMisc.registers(bus);
        ModParticles.registers(bus);
        ModSounds.registers(bus);
        ModDataKeys.init();
    }

    @SubscribeEvent
    static void commonSetup(final FMLCommonSetupEvent event) {
        ModEffects.init();
        WorldEvents.init();
        MinecraftForge.EVENT_BUS.register(EntityEvents.class);
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

    public static <T> ResourceKey<T> key(ResourceKey<? extends Registry<T>> registry, String name) {
        return ResourceKey.create(registry, TConstruct.getResource(name));
    }

    public static String makeDescriptionId(String type, String name) {
        return type + "." + MODID + "." + name;
    }

    public static ResourceLocation getResource(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name);
    }

    public static <T> TinkerDataCapability.TinkerDataKey<T> createKey(String name) {
        return TinkerDataCapability.TinkerDataKey.of(getResource(name));
    }
}
