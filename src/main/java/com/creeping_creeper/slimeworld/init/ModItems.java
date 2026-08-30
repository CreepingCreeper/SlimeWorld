package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModResourceKeys;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.block.*;
import com.creeping_creeper.slimeworld.init.block.bubble.BubbleBlock;
import com.creeping_creeper.slimeworld.init.block.bubble.SlimeBubbleBlock;
import com.creeping_creeper.slimeworld.init.block.bubble.WaterBubbleBlock;
import com.creeping_creeper.slimeworld.init.block.bush.CommonBerryBushBlock;
import com.creeping_creeper.slimeworld.init.block.bush.OreBerryBushBlock;
import com.creeping_creeper.slimeworld.init.block.bush.SlimeBerryBushBlock;
import com.creeping_creeper.slimeworld.init.block.entity.DryingRackBlockEntity;
import com.creeping_creeper.slimeworld.init.block.entity.PotentSulfurBlockEntity;
import com.creeping_creeper.slimeworld.init.block.flower.*;
import com.creeping_creeper.slimeworld.init.block.grass.*;
import com.creeping_creeper.slimeworld.init.item.*;
import com.creeping_creeper.slimeworld.init.world.MagicbubbleTreeGrower;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.item.BlockTooltipItem;
import slimeknights.mantle.item.BurnableBlockItem;
import slimeknights.mantle.registration.deferred.BlockEntityTypeDeferredRegister;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.mantle.registration.object.MetalItemObject;
import slimeknights.mantle.registration.object.WallBuildingBlockObject;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.common.registration.BlockDeferredRegisterExtension;
import slimeknights.tconstruct.common.registration.GeodeItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.fluids.item.ContainerFoodItem;
import slimeknights.tconstruct.gadgets.block.FoodCakeBlock;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.world.TinkerStructures;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.*;

import java.util.function.Function;

import static net.minecraft.world.level.block.SoundType.METAL;
import static net.minecraft.world.level.block.SweetBerryBushBlock.AGE;

public class ModItems {
    protected static final ItemDeferredRegisterExtension ITEMS = new ItemDeferredRegisterExtension(SlimeWorld.MODID);
    protected static final BlockDeferredRegisterExtension BLOCKS = new BlockDeferredRegisterExtension(SlimeWorld.MODID);
    protected static final BlockEntityTypeDeferredRegister BLOCK_ENTITIES = new BlockEntityTypeDeferredRegister(SlimeWorld.MODID);

    protected static final SynchronizedDeferredRegister<CreativeModeTab> CREATIVE_TABS = SynchronizedDeferredRegister.create(Registries.CREATIVE_MODE_TAB, SlimeWorld.MODID);

    public static final RegistryObject<CreativeModeTab> tab = CREATIVE_TABS.register(
            "", () -> CreativeModeTab.builder().title(SlimeWorld.makeTranslation("itemGroup", "common"))
                    .icon(() -> TinkerWorld.slimeGrass.get(DirtType.EARTH).get(FoliageType.EARTH).asItem().getDefaultInstance())
                    .displayItems(ModItems::addTabItems)
                    .withTabsBefore(TinkerWorld.tabWorld.getId())
                    .build());

    protected static final Item.Properties GENERAL_PROPS = new Item.Properties();
    protected static final Function<Block,? extends BlockItem> GENERAL_BLOCK_ITEM = (b) -> new BlockItem(b, GENERAL_PROPS);
    protected static final Function<Block,? extends BlockItem> TOOLTIP_BLOCK_ITEM = (b) -> new BlockTooltipItem(b, GENERAL_PROPS);
    protected static final Function<Block,? extends BlockItem> UNCOMMON_BLOCK_ITEM = (b) -> new BlockItem(b, new Item.Properties().rarity(Rarity.UNCOMMON));

    public static final ItemObject<Item> NecroticBoneMeal = ITEMS.register("necrotic_bone_meal", () -> new NecroticBoneMealItem(GENERAL_PROPS));
    public static final ItemObject<Item> SulfurGoo = ITEMS.register("sulfur_goo", GENERAL_PROPS);
    public static final ItemObject<Item> OceanSlimeBall = ITEMS.register("ocean_slime_ball", GENERAL_PROPS);
    public static final ItemObject<Block> OceanSlime = BLOCKS.register("ocean_slime", () -> new StickySlimeBlock(builder(MapColor.COLOR_BLUE).sound(SoundType.SLIME_BLOCK).strength(0.5F).friction(0.5F).lightLevel(s -> 3).noOcclusion(), (state, other) -> !other.is(ModTags.Blocks.SLIMY)), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> OceanCongealedSlime = BLOCKS.register("ocean_congealed_slime", () -> new CongealedSlimeBlock(builder(MapColor.COLOR_BLUE).sound(SoundType.SLIME_BLOCK).strength(0.5F).friction(0.5F).lightLevel(s -> 3)), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Item> OceanSlimeBottle = ITEMS.register("ocean_slime_bottle", () -> new ContainerFoodItem.FluidContainerFoodItem(new Item.Properties().food(ModFood.OCEAN_BOTTLE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), () -> new FluidStack(ModFluids.OceanSlime.get(), FluidValues.BOTTLE)));
    public static final ItemObject<Block> OceanCake = BLOCKS.register("ocean_cake", () -> new WaterFoodCakeBlock(Block.Properties.of().sound(SoundType.WOOL).forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY), ModFood.OCEAN_CAKE, FoodCakeBlock.EffectCombination.BLOCK), (b) -> new BlockItem(b, new Item.Properties().stacksTo(1)));
    public static final GeodeItemObject OceanGeode = BLOCKS.registerGeode("ocean_slime_crystal", MapColor.COLOR_BLUE, Sounds.ENDER_CRYSTAL, Sounds.ENDER_CRYSTAL_CHIME.getSound(), Sounds.ENDER_CRYSTAL_CLUSTER, 5, GENERAL_PROPS);
    public static final ItemObject<Block> SlimeGravel = BLOCKS.register("slime_gravel", () -> new SlimeGravelBlock(builder(MapColor.COLOR_BLUE).sound(SoundType.GRAVEL).instrument(NoteBlockInstrument.SNARE).strength(0.6F)), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> OceanSlimyEnderbarkRoots = BLOCKS.register("ocean_enderbark_roots", () -> new SlimeDirtBlock(builder(MapColor.COLOR_BLUE).strength(0.7F).sound(SoundType.MUDDY_MANGROVE_ROOTS).lightLevel(s -> 3)), GENERAL_BLOCK_ITEM);

    public static final ItemObject<Block> IchorVent = BLOCKS.register("ichor_vent", () -> new IchorVentBlock(builder(MapColor.STONE).sound(SoundType.STONE).strength(1F).requiresCorrectToolForDrops()), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> DryingRack = BLOCKS.register("drying_rack", () -> new DryingRackBlock(builder(MapColor.WOOD).sound(SoundType.WOOD).strength(0.5F)), GENERAL_BLOCK_ITEM);

    public static final WallBuildingBlockObject Cinnabar = BLOCKS.registerWallBuilding("cinnabar", buildingBuilder(MapColor.COLOR_RED).sound(ModSounds.CINNABAR).instrument(NoteBlockInstrument.BASEDRUM), GENERAL_BLOCK_ITEM);
    public static final WallBuildingBlockObject PolishedCinnabar = BLOCKS.registerWallBuilding("polished_cinnabar", buildingBuilder(MapColor.COLOR_RED).sound(ModSounds.CINNABAR).instrument(NoteBlockInstrument.BASEDRUM), GENERAL_BLOCK_ITEM);
    public static final WallBuildingBlockObject CinnabarBricks = BLOCKS.registerWallBuilding("cinnabar_bricks", buildingBuilder(MapColor.COLOR_RED).sound(ModSounds.CINNABAR).instrument(NoteBlockInstrument.BASEDRUM), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> ChiseledCinnabar = BLOCKS.register("chiseled_cinnabar", buildingBuilder(MapColor.COLOR_RED).sound(ModSounds.CINNABAR).instrument(NoteBlockInstrument.BASEDRUM), GENERAL_BLOCK_ITEM);
    public static final WallBuildingBlockObject Sulfur = BLOCKS.registerWallBuilding("sulfur", buildingBuilder(MapColor.COLOR_YELLOW).sound(ModSounds.SULFUR).instrument(NoteBlockInstrument.BASEDRUM), GENERAL_BLOCK_ITEM);
    public static final WallBuildingBlockObject PolishedSulfur = BLOCKS.registerWallBuilding("polished_sulfur", buildingBuilder(MapColor.COLOR_BROWN).sound(ModSounds.SULFUR).instrument(NoteBlockInstrument.BASEDRUM), GENERAL_BLOCK_ITEM);
    public static final WallBuildingBlockObject SulfurBricks = BLOCKS.registerWallBuilding("sulfur_bricks", buildingBuilder(MapColor.COLOR_BROWN).sound(ModSounds.SULFUR).instrument(NoteBlockInstrument.BASEDRUM), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> ChiseledSulfur = BLOCKS.register("chiseled_sulfur", buildingBuilder(MapColor.COLOR_BROWN).sound(ModSounds.SULFUR).instrument(NoteBlockInstrument.BASEDRUM), GENERAL_BLOCK_ITEM);

    public static final ItemObject<Block> SulfurMud = BLOCKS.register("sulfur_mud", buildingBuilder(MapColor.COLOR_BROWN).sound(SoundType.MUD), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> SulfurSpike = BLOCKS.register("sulfur_spike", () -> new SulfurSpikeBlock(builder(MapColor.TERRACOTTA_BROWN).forceSolidOn().instrument(NoteBlockInstrument.BASEDRUM).noOcclusion().sound(ModSounds.SULFUR).randomTicks().strength(1.5F, 3.0F).dynamicShape().offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY).isRedstoneConductor(Blocks::never).noOcclusion()), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> PotentSulfurNausea = BLOCKS.register("potent_sulfur_nausea", () -> new PotentSulfurBlock(() -> MobEffects.CONFUSION, BlockBehaviour.Properties.copy(Sulfur.get()).sound(ModSounds.POTENT_SULFUR)), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> PotentSulfurBlindness = BLOCKS.register("potent_sulfur_blindness", () -> new PotentSulfurBlock(() -> MobEffects.BLINDNESS, BlockBehaviour.Properties.copy(Sulfur.get()).sound(ModSounds.POTENT_SULFUR)), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> PotentSulfurWeakness = BLOCKS.register("potent_sulfur_weakness", () -> new PotentSulfurBlock(() -> MobEffects.WEAKNESS, BlockBehaviour.Properties.copy(Sulfur.get()).sound(ModSounds.POTENT_SULFUR)), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> PotentSulfurRegeneration = BLOCKS.register("potent_sulfur_regeneration", () -> new PotentSulfurBlock(() -> MobEffects.REGENERATION, BlockBehaviour.Properties.copy(Sulfur.get()).sound(ModSounds.POTENT_SULFUR)), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> PotentSulfurStrength = BLOCKS.register("potent_sulfur_strength", () -> new PotentSulfurBlock(() -> MobEffects.DAMAGE_BOOST, BlockBehaviour.Properties.copy(Sulfur.get()).sound(ModSounds.POTENT_SULFUR)), GENERAL_BLOCK_ITEM);

    public static final ItemObject<Block> GlowstoneOre = BLOCKS.register("glowstone_ore", builder(MapColor.STONE).sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(3.0F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 7), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> DeepSlateGlowstoneOre = BLOCKS.register("deepslate_glowstone_ore", builder(MapColor.DEEPSLATE).sound(SoundType.DEEPSLATE).strength(4.5F, 3.0F).requiresCorrectToolForDrops().lightLevel(state -> 7), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> IsomericGlowstone = BLOCKS.register("isomeric_glowstone", builder(MapColor.SAND).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.PLING).strength(5.0F, 6.0F).sound(METAL).lightLevel((p_50874_) -> 15).isRedstoneConductor(Blocks::never), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> IsomericRedstoneBlock = BLOCKS.register("isomeric_redstone_block", () -> new PoweredBlock(builder(MapColor.FIRE).strength(0.3F).sound(SoundType.GLASS).isRedstoneConductor(Blocks::never)), GENERAL_BLOCK_ITEM);

    public static final ItemObject<Block> IchorFern = BLOCKS.register("ichor_slime_fern", () -> new IchorTallGrassBlock(builder(MapColor.COLOR_ORANGE).sound(SoundType.ROOTS).offsetType(BlockBehaviour.OffsetType.XZ).replaceable().instabreak().noCollission().pushReaction(PushReaction.DESTROY)), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> IchorTallGrass = BLOCKS.register("ichor_slime_tall_grass", () -> new IchorTallGrassBlock(builder(MapColor.COLOR_ORANGE).sound(SoundType.ROOTS).offsetType(BlockBehaviour.OffsetType.XZ).replaceable().instabreak().noCollission().pushReaction(PushReaction.DESTROY)), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> IchorSlimeSapling = BLOCKS.register("ichor_slime_sapling", () -> new IchorFungusBlock(builder(MapColor.COLOR_ORANGE).sound(SoundType.FUNGUS).instabreak().noCollission().pushReaction(PushReaction.DESTROY), ModResourceKeys.ichorSlimeFungus), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<IchorNyliumBlock> IchorEarthSlimeNylium = BLOCKS.register("ichor_earth_slime_grass", () -> new IchorNyliumBlock(ichorNylium(), DirtType.EARTH), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<IchorNyliumBlock> IchorSkySlimeNylium = BLOCKS.register("ichor_sky_slime_grass", () -> new IchorNyliumBlock(ichorNylium(), DirtType.SKY), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<IchorNyliumBlock> IchorIchorSlimeNylium = BLOCKS.register("ichor_ichor_slime_grass", () -> new IchorNyliumBlock(ichorNylium(), DirtType.ICHOR), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<IchorNyliumBlock> IchorEnderSlimeNylium = BLOCKS.register("ichor_ender_slime_grass", () -> new IchorNyliumBlock(ichorNylium(), DirtType.ENDER), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<IchorNyliumBlock> IchorVanillaSlimeNylium = BLOCKS.register("ichor_vanilla_slime_grass", () -> new IchorNyliumBlock(ichorNylium(), DirtType.VANILLA), TOOLTIP_BLOCK_ITEM);

    public static final ItemObject<Block> MagicbubbleSapling = BLOCKS.register("magicbubble_sapling", () -> new SaplingBlock(new MagicbubbleTreeGrower(), builder(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> SnowaveSapling = BLOCKS.register("snowave_sapling", () -> new SaplingBlock(new MagicbubbleTreeGrower(), builder(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> MagicbubbleLog = BLOCKS.register("magicbubble_log", () -> new RotatedPillarBlock(builder(MapColor.WOOD).sound(SoundType.WOOD).ignitedByLava()), (b) -> new BurnableBlockTooltipItem(b, GENERAL_PROPS, 300));
    public static final ItemObject<Block> Magicbubbleleaves = BLOCKS.register("magicbubble_leaves", () -> new LeavesBlock(builder(MapColor.PLANT).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(Blocks::never)), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> ActiveMagicbubbleLog = BLOCKS.register("active_magicbubble_log", () -> new ActiveMagicbubbleLogBlock(builder(MapColor.WOOD).randomTicks().sound(SoundType.WOOD).ignitedByLava()), (b) -> new BurnableBlockTooltipItem(b, GENERAL_PROPS, 300));
    public static final ItemObject<Block> StrippedSnowaveLog = BLOCKS.register("stripped_snowave_log", () -> new SnowaveLogBlock(builder(MapColor.WOOD).sound(SoundType.WOOD).ignitedByLava()),(b) -> new BurnableBlockTooltipItem(b, GENERAL_PROPS, 300));
    public static final ItemObject<Block> Snowaveleaves = BLOCKS.register("snowave_leaves", () -> new LeavesBlock(builder(MapColor.PLANT).strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(Blocks::never)), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> SnowaveLog = BLOCKS.register("snowave_log", () -> new StrippableSnowaveLogBlock(StrippedSnowaveLog, builder(MapColor.WOOD).sound(SoundType.WOOD).ignitedByLava()), (b) -> new BurnableBlockItem(b, GENERAL_PROPS, 300));

    public static final ItemObject<Block> SlimeWeed = BLOCKS.register("slime_weed", () -> new GlowLichenBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WATER).noCollission().instabreak().sound(SoundType.WET_GRASS).pushReaction(PushReaction.DESTROY)), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> StickPunjis = BLOCKS.register("stick_punjis", () -> new StickPunjisBlock(grass().strength(3.0F).speedFactor(0.4F).noOcclusion()), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> FieryFlower = BLOCKS.register("fiery_flower", () -> new FieryFlowerBlock(grass().lightLevel(s -> 15)), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> PoisonFlower = BLOCKS.register("poison_flower", () -> new PoisonFlowerBlock(grass()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> SpringyFlower = BLOCKS.register("springy_flower", () -> new SpringyFlowerBlock(grass()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> ConsecratedFlower = BLOCKS.register("consecrated_flower", () -> new ConsecratedFlowerBlock(grass()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> GraveyardFlower = BLOCKS.register("graveyard_flower", () -> new GraveyardFlowerBlock(grass()), UNCOMMON_BLOCK_ITEM);
    public static final RegistryObject<Block> SlimeBerryBush = BLOCKS.registerNoItem("slime_berry_bush", () -> new SlimeBerryBushBlock(bush()));
    public static final RegistryObject<Block> BerriperBush = BLOCKS.registerNoItem("berriper_bush", () -> new CommonBerryBushBlock(bush()));

    public static final ItemObject<Item> EarthSlimeBerry = ITEMS.register("earth_slime_berries", () -> new BlockItem(SlimeBerryBush.get(),new Item.Properties().food(ModFood.EARTH_SLIME_BERRY)));
    public static final ItemObject<Item> SkySlimeBerry = ITEMS.register("sky_slime_berries", () -> new BlockItem(SlimeBerryBush.get(),new Item.Properties().food(ModFood.SKY_SLIME_BERRY)));
    public static final ItemObject<Item> EnderSlimeBerry = ITEMS.register("ender_slime_berries", () -> new BlockItem(SlimeBerryBush.get(),new Item.Properties().food(ModFood.ENDER_SLIME_BERRY)));
    public static final ItemObject<Item> BloodSlimeBerry = ITEMS.register("blood_slime_berries", () -> new BlockItem(SlimeBerryBush.get(),new Item.Properties().food(ModFood.BLOOD_SLIME_BERRY)));
    public static final ItemObject<Item> Berriper = ITEMS.register("berripers", () -> new BlockItem(BerriperBush.get(),new Item.Properties().food(ModFood.BERRIPER).rarity(Rarity.UNCOMMON)));
    public static final ItemObject<Item> TomatoPudding = ITEMS.register("tomato_pudding", new Item.Properties().food(ModFood.TOMATO_PUDDING));
    public static final ItemObject<Item> BeefJerky = ITEMS.register("beef_jerky", new Item.Properties().food(ModFood.BEEF_JERKY));
    public static final ItemObject<Item> PorkJerky = ITEMS.register("pork_jerky", new Item.Properties().food(ModFood.BEEF_JERKY));
    public static final ItemObject<Item> MuttonJerky = ITEMS.register("mutton_jerky", new Item.Properties().food(ModFood.MUTTON_JERKY));
    public static final ItemObject<Item> RabbitJerky = ITEMS.register("rabbit_jerky", new Item.Properties().food(ModFood.RABBIT_JERKY));
    public static final ItemObject<Item> ChickenJerky = ITEMS.register("chicken_jerky", new Item.Properties().food(ModFood.RABBIT_JERKY));
    public static final ItemObject<Item> CodJerky = ITEMS.register("cod_jerky", new Item.Properties().food(ModFood.RABBIT_JERKY));
    public static final ItemObject<Item> SalmonJerky = ITEMS.register("salmon_jerky", new Item.Properties().food(ModFood.MUTTON_JERKY));
    public static final ItemObject<Item> TropicalFishJerky = ITEMS.register("tropical_fish_jerky", new Item.Properties().food(ModFood.FISH_JERKY));
    public static final ItemObject<Item> PufferfishJerky = ITEMS.register("pufferfish_jerky", new Item.Properties().food(ModFood.FISH_JERKY));
    public static final ItemObject<Item> RottenFleshJerky = ITEMS.register("rotten_flesh_jerky", new Item.Properties().food(ModFood.ROTTEN_FLESH_JERKY));
    public static final ItemObject<Item> FriedEgg = ITEMS.register("fried_egg", new Item.Properties().food(ModFood.FRIED_EGG));
    public static final ItemObject<Item> EarthSlimeDrop = ITEMS.register("earth_slime_drop", new Item.Properties().food(ModFood.EARTH_SLIME_DROP));
    public static final ItemObject<Item> SkySlimeDrop = ITEMS.register("sky_slime_drop", new Item.Properties().food(ModFood.SKY_SLIME_DROP));
    public static final ItemObject<Item> OceanSlimeDrop = ITEMS.register("ocean_slime_drop", new Item.Properties().food(ModFood.OCEAN_SLIME_DROP));
    public static final ItemObject<Item> MagmaSlimeDrop = ITEMS.register("magma_slime_drop", new Item.Properties().food(ModFood.MAGMA_SLIME_DROP));
    public static final ItemObject<Item> IchorSlimeDrop = ITEMS.register("ichor_slime_drop", new Item.Properties().food(ModFood.ICHOR_SLIME_DROP));
    public static final ItemObject<Item> EnderSlimeDrop = ITEMS.register("ender_slime_drop", new Item.Properties().food(ModFood.ENDER_SLIME_DROP));

    public static final ItemObject<Item> CopperShard = ITEMS.register("copper_shard", GENERAL_PROPS);
    public static final ItemObject<Item> IronShard = ITEMS.register("iron_shard", GENERAL_PROPS);
    public static final ItemObject<Item> GoldShard = ITEMS.register("gold_shard", GENERAL_PROPS);
    public static final ItemObject<Item> BronzeShard = ITEMS.register("bronze_shard", GENERAL_PROPS);
    public static final ItemObject<Block> BronzeCluster = BLOCKS.register("bronze_cluster", () -> new CrystalClusterBlock(Sounds.ENDER_CRYSTAL_CHIME.getSound(), 7, 3, builder(MapColor.STONE).forceSolidOn().noOcclusion().randomTicks().strength(2.5f).requiresCorrectToolForDrops().pushReaction(PushReaction.DESTROY).lightLevel(state -> 5).sound(SoundType.METAL)), TOOLTIP_BLOCK_ITEM);
    public static final MetalItemObject Bronze = BLOCKS.registerMetal("bronze", metalBuilder(MapColor.COLOR_BROWN), GENERAL_BLOCK_ITEM, GENERAL_PROPS);
    public static final MetalItemObject SlimeBronze = BLOCKS.registerMetal("slime_bronze", metalBuilder(MapColor.COLOR_GREEN), GENERAL_BLOCK_ITEM, GENERAL_PROPS);

    public static final ItemObject<Block> CopperBerryBush = BLOCKS.register("copper_berry_bush", () -> new OreBerryBushBlock(CopperShard, oreBush()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> IronBerryBush = BLOCKS.register("iron_berry_bush", () -> new OreBerryBushBlock(IronShard, oreBush()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> GoldBerryBush = BLOCKS.register("gold_berry_bush", () -> new OreBerryBushBlock(GoldShard, oreBush()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> CobaltBerryBush = BLOCKS.register("cobalt_berry_bush", () -> new OreBerryBushBlock(TinkerWorld.cobaltShard, oreBush()), UNCOMMON_BLOCK_ITEM);

    public static final ItemObject<Item> MagicPot = ITEMS.register("magic_pot", GENERAL_PROPS);
    public static final ItemObject<Item> PlantPot = ITEMS.register("plant_pot", () -> new PotItem(GENERAL_PROPS));

    public static final ItemObject<Item> Bubble = ITEMS.register("bubble", GENERAL_PROPS);
    public static final ItemObject<Block> WaterBubble = BLOCKS.register("water_bubble", () -> new WaterBubbleBlock(bubble(MapColor.WATER)), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> LavaBubble = BLOCKS.register("lava_bubble", () -> new BubbleBlock(builder(MapColor.COLOR_RED).randomTicks().noCollission().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never), () -> Fluids.LAVA), (b) -> new BurnableBlockItem(b, new Item.Properties().craftRemainder(Bubble.asItem()), 2000));
    public static final ItemObject<Block> EarthSlimeBubble = BLOCKS.register("earth_slime_bubble", () -> new SlimeBubbleBlock(bubble(MapColor.COLOR_LIGHT_GREEN), TinkerFluids.earthSlime), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> SkySlimeBubble = BLOCKS.register("sky_slime_bubble", () -> new SlimeBubbleBlock((bubble(MapColor.COLOR_LIGHT_BLUE)), TinkerFluids.skySlime), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> IchorSlimeBubble = BLOCKS.register("ichor_bubble", () -> new SlimeBubbleBlock((bubble(MapColor.COLOR_ORANGE)), TinkerFluids.ichor), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> EnderSlimeBubble = BLOCKS.register("ender_slime_bubble", () -> new SlimeBubbleBlock((bubble(MapColor.COLOR_PURPLE)), TinkerFluids.enderSlime), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> OceanSlimeBubble = BLOCKS.register("ocean_slime_bubble", () -> new SlimeBubbleBlock((bubble(MapColor.COLOR_BLUE)), ModFluids.OceanSlime), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> HoneyBubble = BLOCKS.register("honey_bubble", () -> new SlimeBubbleBlock((bubble(MapColor.COLOR_YELLOW)), TinkerFluids.honey), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> VenomBubble = BLOCKS.register("venom_bubble", () -> new SlimeBubbleBlock((bubble(MapColor.COLOR_LIGHT_GRAY)), TinkerFluids.venom), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Item> SulfurCubeBucket = ITEMS.register("sulfur_cube_bucket", () -> new EmptyMobBucketItem(ModEntities.SulfurCubeEntity, ModSounds.BUCKET_EMPTY_SULFUR_CUBE, (new Item.Properties()).stacksTo(1)));

    public static final ItemObject<Block> UnknownTpSteel = BLOCKS.register("unknown_teleporter_steel", () -> new UnknownTpBlock(tp(MapColor.COLOR_LIGHT_BLUE), TinkerStructures.skySlimeIsland, TinkerMaterials.slimesteel.getIngotTag()), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> UnknownTpBronze = BLOCKS.register("unknown_teleporter_bronze", () -> new UnknownTpBlock(tp(MapColor.COLOR_BLUE), TinkerStructures.skySlimeIsland, SlimeBronze.getIngotTag()), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> UnknownTpCinder = BLOCKS.register("unknown_teleporter_cinder", () -> new UnknownTpBlock(tp(MapColor.COLOR_RED), TinkerStructures.bloodIsland, TinkerMaterials.cinderslime.getIngotTag()), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> UnknownTpQueen = BLOCKS.register("unknown_teleporter_queen", () -> new UnknownTpBlock(tp(MapColor.COLOR_ORANGE), TinkerStructures.bloodIsland, TinkerMaterials.queensSlime.getIngotTag()), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> UnknownTpKnight = BLOCKS.register("unknown_teleporter_knight", () -> new UnknownTpBlock(tp(MapColor.COLOR_PURPLE), TinkerStructures.endSlimeIsland, TinkerMaterials.knightslime.getIngotTag()), GENERAL_BLOCK_ITEM);

    public static final ItemObject<Item> MeleeRune = ITEMS.register("melee_rune", () -> new ModifierRuneItem(TinkerTags.Items.MELEE, (new Item.Properties()).stacksTo(16)));
    public static final ItemObject<Item> RangedRune = ITEMS.register("ranged_rune", () -> new ModifierRuneItem(TinkerTags.Items.RANGED, (new Item.Properties()).stacksTo(16)));
    public static final ItemObject<Item> ArmorRune = ITEMS.register("armor_rune", () -> new ModifierRuneItem(TinkerTags.Items.ARMOR, (new Item.Properties()).stacksTo(16)));

    public static final RegistryObject<BlockEntityType<PotentSulfurBlockEntity>> PotentSulfurEntity = BLOCK_ENTITIES.register("potent_sulfur", PotentSulfurBlockEntity::new, set -> set.add(PotentSulfurNausea.get(), PotentSulfurBlindness.get(), PotentSulfurWeakness.get(), PotentSulfurRegeneration.get(), PotentSulfurStrength.get()));
    public static final RegistryObject<BlockEntityType<DryingRackBlockEntity>> DryingRackEntity = BLOCK_ENTITIES.register("drying_rack", DryingRackBlockEntity::new, set -> set.add(DryingRack.get()));

    private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.accept(NecroticBoneMeal);
        output.accept(SulfurGoo);
        output.accept(DryingRack);
        output.accept(OceanSlimeBall);
        output.accept(OceanCongealedSlime);
        output.accept(OceanSlime);
        acceptGeode(output, OceanGeode);

        output.accept(OceanSlimeBottle);
        output.accept(OceanCake);
        output.accept(EarthSlimeBerry);
        output.accept(SkySlimeBerry);
        output.accept(EnderSlimeBerry);
        output.accept(BloodSlimeBerry);
        output.accept(Berriper);
        output.accept(TomatoPudding);
        output.accept(FriedEgg);
        output.accept(BeefJerky);
        output.accept(ChickenJerky);
        output.accept(PorkJerky);
        output.accept(MuttonJerky);
        output.accept(RabbitJerky);
        output.accept(RottenFleshJerky);
        output.accept(SalmonJerky);
        output.accept(CodJerky);
        output.accept(TropicalFishJerky);
        output.accept(PufferfishJerky);
        output.accept(EarthSlimeDrop);
        output.accept(SkySlimeDrop);
        output.accept(OceanSlimeDrop);
        output.accept(IchorSlimeDrop);
        output.accept(MagmaSlimeDrop);
        output.accept(EnderSlimeDrop);

        output.accept(CopperShard);
        output.accept(IronShard);
        output.accept(GoldShard);
        output.accept(BronzeShard);
        output.accept(BronzeCluster);
        accept(output, Bronze);
        accept(output, SlimeBronze);

        output.accept(SlimeGravel);
        output.accept(OceanSlimyEnderbarkRoots);
        output.accept(IchorVent);

        acceptWallBuilding(output, Cinnabar);
        acceptWallBuilding(output, PolishedCinnabar);
        acceptWallBuilding(output, CinnabarBricks);
        output.accept(ChiseledCinnabar);
        acceptWallBuilding(output, Sulfur);
        acceptWallBuilding(output, PolishedSulfur);
        acceptWallBuilding(output, SulfurBricks);
        output.accept(ChiseledSulfur);
        output.accept(SulfurMud);
        output.accept(PotentSulfurNausea);
        output.accept(PotentSulfurBlindness);
        output.accept(PotentSulfurWeakness);
        output.accept(PotentSulfurRegeneration);
        output.accept(PotentSulfurStrength);
        output.accept(SulfurSpike);

        output.accept(GlowstoneOre);
        output.accept(DeepSlateGlowstoneOre);
        output.accept(IsomericGlowstone);
        output.accept(IsomericRedstoneBlock);

        output.accept(IchorFern);
        output.accept(IchorTallGrass);
        output.accept(IchorSlimeSapling);
        output.accept(IchorEarthSlimeNylium);
        output.accept(IchorSkySlimeNylium);
        output.accept(IchorIchorSlimeNylium);
        output.accept(IchorEnderSlimeNylium);
        output.accept(IchorVanillaSlimeNylium);

        output.accept(MagicbubbleSapling);
        output.accept(SnowaveSapling);
        output.accept(Magicbubbleleaves);
        output.accept(MagicbubbleLog);
        output.accept(ActiveMagicbubbleLog);
        output.accept(SnowaveLog);
        output.accept(Snowaveleaves);
        output.accept(StrippedSnowaveLog);

        output.accept(SlimeWeed);
        output.accept(StickPunjis);
        output.accept(FieryFlower);
        output.accept(PoisonFlower);
        output.accept(SpringyFlower);
        output.accept(ConsecratedFlower);
        output.accept(GraveyardFlower);

        output.accept(CopperBerryBush);
        output.accept(IronBerryBush);
        output.accept(GoldBerryBush);
        output.accept(CobaltBerryBush);
        output.accept(ModFluids.OceanSlime);
        output.accept(ModFluids.ResonanceSlime);
        output.accept(ModFluids.LiquidMud);
        output.accept(ModFluids.MoltenSlimeBronze);

        output.accept(Bubble);
        output.accept(WaterBubble);
        output.accept(LavaBubble);
        output.accept(EarthSlimeBubble);
        output.accept(SkySlimeBubble);
        output.accept(IchorSlimeBubble);
        output.accept(EnderSlimeBubble);
        output.accept(OceanSlimeBubble);
        output.accept(HoneyBubble);
        output.accept(VenomBubble);

        output.accept(MagicPot);
        output.accept(PlantPot);

        output.accept(UnknownTpSteel);
        output.accept(UnknownTpBronze);
        output.accept(UnknownTpCinder);
        output.accept(UnknownTpQueen);
        output.accept(UnknownTpKnight);
        //output.accept(MeleeRune);

        output.accept(SulfurCubeBucket);
        output.accept(ModEntities.OceanSlimeEntity);
        output.accept(ModEntities.IchorSlimeEntity);
        output.accept(ModEntities.OriginSlimeEntity);
        output.accept(ModEntities.TomatoSlimeEntity);
        output.accept(ModEntities.SulfurCubeEntity);
        output.accept(ModEntities.BoggedEntity);
        output.accept(ModEntities.ParchedEntity);
        output.accept(ModEntities.EarthSlimeGolemEntity);
        output.accept(ModEntities.SkySlimeGolemEntity);
        output.accept(ModEntities.OceanSlimeGolemEntity);
        output.accept(ModEntities.IchorGolemEntity);
        output.accept(ModEntities.EnderSlimeGolemEntity);
    }

    protected static BlockBehaviour.Properties builder(MapColor color) {
        return Block.Properties.of().mapColor(color);
    }

    protected static BlockBehaviour.Properties metalBuilder(MapColor color) {
        return builder(color).sound(METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0F);
    }

    protected static BlockBehaviour.Properties buildingBuilder(MapColor color) {
        return builder(color).requiresCorrectToolForDrops().strength(1.5F,6);
    }

    protected static BlockBehaviour.Properties grass() {
        return plant().sound(SoundType.GRASS).instabreak().offsetType(BlockBehaviour.OffsetType.XZ);
    }

    protected static BlockBehaviour.Properties bush() {
        return plant().sound(SoundType.SWEET_BERRY_BUSH).randomTicks();
    }

    protected static BlockBehaviour.Properties oreBush() {
        return bush().lightLevel(state -> state.getValue(AGE) > 2 ? 3 : 0);
    }

    protected static BlockBehaviour.Properties plant() {
        return builder(MapColor.PLANT).noCollission().pushReaction(PushReaction.DESTROY);
    }

    protected static BlockBehaviour.Properties ichorNylium() {
        return builder(MapColor.COLOR_ORANGE).sound(SoundType.SLIME_BLOCK).strength(2.0f).requiresCorrectToolForDrops().randomTicks();
    }

    protected static BlockBehaviour.Properties bubble(MapColor color) {
        return builder(color).sound(SoundType.WOOL).noCollission().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).isViewBlocking(Blocks::never);
    }

    protected static BlockBehaviour.Properties tp(MapColor color) {
        return builder(color).sound(SoundType.TUFF).instrument(NoteBlockInstrument.BASEDRUM).strength(-1.0F, 3600000.0F).noLootTable().isValidSpawn(Blocks::never).lightLevel(s -> s.getValue(UnknownTpBlock.USED) ? 15 : 0);
    }

    private static void accept(CreativeModeTab.Output output, MetalItemObject metal) {
        output.accept(metal.getIngot());
        output.accept(metal.getNugget());
        output.accept(metal.get());
    }

    private static void acceptGeode(CreativeModeTab.Output output, GeodeItemObject geode) {
        output.accept(geode);
        output.accept(geode.getBlock());
        output.accept(geode.getBudding());
        for (GeodeItemObject.BudSize size : GeodeItemObject.BudSize.values()) {
            output.accept(geode.getBud(size));
        }
    }

    private static void acceptWallBuilding(CreativeModeTab.Output output, WallBuildingBlockObject building) {
        output.accept(building.get());
        output.accept(building.getStairs());
        output.accept(building.getSlab());
        output.accept(building.getWall());
    }

    public static void registers(IEventBus bus) {
        ITEMS.register(bus);
        BLOCKS.register(bus);
        BLOCK_ENTITIES.register(bus);
        CREATIVE_TABS.register(bus);
    }
}
