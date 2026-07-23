package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.misc.AddModifierFunction;
import com.creeping_creeper.slimeworld.init.misc.HasOverslimeCondition;
import com.creeping_creeper.slimeworld.init.misc.RandomModifierFunction;
import com.creeping_creeper.slimeworld.init.misc.RemoveOverslimeFunction;
import com.creeping_creeper.slimeworld.init.world.*;
import com.creeping_creeper.slimeworld.init.misc.DryingRackRecipe;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FillLayerFeature;
import net.minecraft.world.level.levelgen.feature.configurations.LayerConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.recipe.helper.LoadableRecipeSerializer;
import slimeknights.tconstruct.world.worldgen.trees.config.SlimeFungusConfig;

@SuppressWarnings("unused")
public class ModOthers {
    protected static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, SlimeWorld.MODID);
    protected static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, SlimeWorld.MODID);
    protected static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, SlimeWorld.MODID);
    protected static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, SlimeWorld.MODID);
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, SlimeWorld.MODID);
    protected static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SlimeWorld.MODID);

    public static final RegistryObject<BetterFillLayerFeature> BetterFillLayer = FEATURES.register("better_fill_layer", () -> new BetterFillLayerFeature(LayerConfiguration.CODEC));
    public static final RegistryObject<OceanLakeFeature> OceanLake = FEATURES.register("ocean_lake", () -> new OceanLakeFeature(OceanLakeFeature.Configuration.CODEC));
    public static final RegistryObject<InvertedLakeFeature> InvertedLake = FEATURES.register("inverted_lake", () -> new InvertedLakeFeature(InvertedLakeFeature.Configuration.CODEC));
    public static final RegistryObject<SulfurPoolFeature> SulfurPool = FEATURES.register("sulfur_pool", () -> new SulfurPoolFeature(SulfurPoolFeature.Configuration.CODEC));

    public static final RegistryObject<IchorFungusFeature> IchorFungus = FEATURES.register("ichor_fungus", () -> new IchorFungusFeature(SlimeFungusConfig.CODEC));
    public static final RegistryObject<HugeSulfurSpikeFeature> HugeSulfurSpike = FEATURES.register("huge_sulfur_spike", () -> new HugeSulfurSpikeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<SulfurSpikeFeature> SulfurSpike = FEATURES.register("sulfur_spike", () -> new SulfurSpikeFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<Codec<? extends BiomeModifier>> MODIFY_SPAWNS =
            BIOME_MODIFIER_SERIALIZERS.register("modify_spawns", () -> ModifySpawnsBiomeModifier.CODEC);

    public static final RegistryObject<LootItemConditionType> hasModifierLootCondition = LOOT_CONDITIONS.register("has_overslime", () -> new LootItemConditionType(new HasOverslimeCondition.ConditionSerializer()));


    public static final RegistryObject<LootItemFunctionType> AddModifier = LOOT_FUNCTIONS.register("add_modifier", () -> new LootItemFunctionType(AddModifierFunction.SERIALIZER));
    public static final RegistryObject<LootItemFunctionType> RandomModifier = LOOT_FUNCTIONS.register("random_modifier", () -> new LootItemFunctionType(RandomModifierFunction.SERIALIZER));
    public static final RegistryObject<LootItemFunctionType> RemoveOverslime = LOOT_FUNCTIONS.register("remove_overslime", () -> new LootItemFunctionType(RemoveOverslimeFunction.SERIALIZER));


    public static final RegistryObject<RecipeType<DryingRackRecipe>> DryingRecipeType = RECIPE_TYPES.register("drying_rack", () -> new RecipeType<>() {
        @Override
        public String toString() {
            return SlimeWorld.MODID + ":" + "drying_rack";
        }
    });
    public static final RegistryObject<RecipeSerializer<DryingRackRecipe>> DryingRecipeSerializer = RECIPE_SERIALIZERS.register("drying_rack", () -> LoadableRecipeSerializer.of(DryingRackRecipe.LOADER));

    public static void registers(IEventBus bus) {
        FEATURES.register(bus);
        BIOME_MODIFIER_SERIALIZERS.register(bus);
        LOOT_CONDITIONS.register(bus);
        LOOT_FUNCTIONS.register(bus);
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }
}
