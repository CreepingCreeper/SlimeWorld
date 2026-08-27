package com.creeping_creeper.slimeworld;

import com.creeping_creeper.slimeworld.data.key.ModDataKeys;
import com.creeping_creeper.slimeworld.data.provider.*;
import com.creeping_creeper.slimeworld.data.provider.assets.ModBlockStateProvider;
import com.creeping_creeper.slimeworld.data.provider.assets.ModFluidTextureProvider;
import com.creeping_creeper.slimeworld.data.provider.assets.ModItemModelProvider;
import com.creeping_creeper.slimeworld.data.provider.loot.ModGlobalLootModifiersProvider;
import com.creeping_creeper.slimeworld.data.provider.loot.ModLootTableProvider;
import com.creeping_creeper.slimeworld.data.provider.tags.*;
import com.creeping_creeper.slimeworld.data.provider.tinkering.*;
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
import slimeknights.mantle.fluid.texture.FluidTextureCameraProvider;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.fluids.data.FluidBlockstateModelProvider;
import slimeknights.tconstruct.fluids.data.FluidBucketModelProvider;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.utils.Util;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
        ModOthers.registers(bus);
        ModParticles.registers(bus);
        ModSounds.registers(bus);
        ModDataKeys.init();
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
        //registers
        RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder();
        ModWorldgenProvider.register(registrySetBuilder);
        //resource pack
        generator.addProvider(client, new ModItemModelProvider(output, existingFileHelper));
        generator.addProvider(client, new ModBlockStateProvider(output, existingFileHelper));
        ModFluidTextureProvider textureProvider = new ModFluidTextureProvider(output);
        generator.addProvider(client, textureProvider);
        generator.addProvider(client, new FluidTextureCameraProvider(output, event.getExistingFileHelper(), textureProvider));
        generator.addProvider(client, new FluidBucketModelProvider(output, SlimeWorld.MODID));
        generator.addProvider(client, new FluidBlockstateModelProvider(output, SlimeWorld.MODID));
        //data pack
        DatapackBuiltinEntriesProvider datapackProvider = new DatapackBuiltinEntriesProvider(output, event.getLookupProvider(), registrySetBuilder, Set.of(MODID));
        generator.addProvider(server, datapackProvider);
        //tags
        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(output, lookupProvider, existingFileHelper);
        generator.addProvider(server, blockTags);
        generator.addProvider(server, new ModItemTagsProvider(output, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        generator.addProvider(server, new ModFluidTagProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(server, new ModEntityTypeTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(server, new ModBiomeTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(server, new ModDamageTypeTagsProvider(output, lookupProvider, existingFileHelper));
        generator.addProvider(server, new ModModifierTagsProvider(output, existingFileHelper));
        //loots
        generator.addProvider(server, new ModLootTableProvider(output));
        generator.addProvider(server, new ModGlobalLootModifiersProvider(output));
        //others
        generator.addProvider(server, new ModRecipeProvider(output));
        ModMaterialProvider materials = new ModMaterialProvider(output);
        generator.addProvider(server, materials);
        generator.addProvider(server, new ModStatsProvider(output, materials));
        generator.addProvider(server, new ModTraitsProvider(output, materials));
        generator.addProvider(server, new ModFluidEffectProvider(output));
        generator.addProvider(server, new ModModifierProvider(output));
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

    @SuppressWarnings("removal")
    public static ResourceLocation getResource(String name) {
        return new ResourceLocation(MODID, name);
    }

    public static <T> TinkerDataCapability.TinkerDataKey<T> createKey(String name) {
        return TinkerDataCapability.TinkerDataKey.of(getResource(name));
    }
}
