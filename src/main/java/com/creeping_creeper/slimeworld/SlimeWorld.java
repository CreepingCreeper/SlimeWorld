package com.creeping_creeper.slimeworld;

import com.creeping_creeper.slimeworld.data.provider.*;
import com.creeping_creeper.slimeworld.events.EntityEvents;
import com.creeping_creeper.slimeworld.events.WorldEvents;
import com.creeping_creeper.slimeworld.init.*;
import com.mojang.logging.LogUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.utils.Util;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("removal")
@Mod(SlimeWorld.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SlimeWorld {
    public static final String MODID = "slimeworld";
    public static final Logger LOG = LogUtils.getLogger();

    public SlimeWorld() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.registers(bus);
        ModFluids.registers(bus);
        ModModifiers.registers(bus);
        ModEffects.registers(bus);
        ModEntities.registers(bus);
        ModOthers.registers(bus);
        ModParticles.registers(bus);
        ModSounds.registers(bus);
    }

    @SubscribeEvent
    void commonSetup(final FMLCommonSetupEvent event) {
        ModEffects.init();
        WorldEvents.init();
        MinecraftForge.EVENT_BUS.register(new EntityEvents());
    }

    @SubscribeEvent
    static void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        boolean server = event.includeServer();
        boolean client = event.includeClient();
        RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder();
        DatapackBuiltinEntriesProvider datapackProvider = new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), registrySetBuilder, Set.of(MODID));
        generator.addProvider(server, datapackProvider);
        //recipes
        generator.addProvider(server, new ModRecipeProvider(output));
        //tags
        ModBlockTagProvider blockTags = new ModBlockTagProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(server, blockTags);
        generator.addProvider(server, new ModItemTagProvider(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        generator.addProvider(server, new ModFluidTagProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(server, new ModEntityTypeTagProvider(output, lookupProvider, existingFileHelper));
        //models
        generator.addProvider(client, new ModItemModelProvider(output, existingFileHelper));
        generator.addProvider(client, new ModBlockStateProvider(output, existingFileHelper));
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
        return new ResourceLocation(MODID, name);
    }

    public static <T> TinkerDataCapability.TinkerDataKey<T> createKey(String name) {
        return TinkerDataCapability.TinkerDataKey.of(getResource(name));
    }
}
