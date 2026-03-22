package com.creeping_creeper.slimeworld.init;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.ModResourceKeys;
import com.creeping_creeper.slimeworld.data.ModTags;
import com.creeping_creeper.slimeworld.init.block.*;
import com.creeping_creeper.slimeworld.init.block.bush.CommonBerryBushBlock;
import com.creeping_creeper.slimeworld.init.block.bush.OreBerryBushBlock;
import com.creeping_creeper.slimeworld.init.block.bush.SlimeBerryBushBlock;
import com.creeping_creeper.slimeworld.init.block.flower.*;
import com.creeping_creeper.slimeworld.init.block.grass.*;
import com.creeping_creeper.slimeworld.init.item.ModFood;
import com.creeping_creeper.slimeworld.init.item.NecroticBoneMealItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.item.BlockTooltipItem;
import slimeknights.mantle.item.TooltipItem;
import slimeknights.mantle.registration.deferred.SynchronizedDeferredRegister;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.mantle.registration.object.MetalItemObject;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.registration.BlockDeferredRegisterExtension;
import slimeknights.tconstruct.common.registration.GeodeItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.fluids.item.ContainerFoodItem;
import slimeknights.tconstruct.gadgets.block.FoodCakeBlock;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.*;

import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraft.world.level.block.SoundType.METAL;
import static net.minecraft.world.level.block.SweetBerryBushBlock.AGE;

public class ModItems {
    protected static final ItemDeferredRegisterExtension ITEMS = new ItemDeferredRegisterExtension(SlimeWorld.MODID);
    protected static final BlockDeferredRegisterExtension BLOCKS = new BlockDeferredRegisterExtension(SlimeWorld.MODID);
    protected static final SynchronizedDeferredRegister<CreativeModeTab> CREATIVE_TABS = SynchronizedDeferredRegister.create(Registries.CREATIVE_MODE_TAB, SlimeWorld.MODID);

    public static final RegistryObject<CreativeModeTab> tab = CREATIVE_TABS.register(
            "", () -> CreativeModeTab.builder().title(SlimeWorld.makeTranslation("itemGroup", "common"))
                    .icon(() -> TinkerWorld.slimeGrass.get(DirtType.EARTH).get(FoliageType.EARTH).asItem().getDefaultInstance())
                    .displayItems(ModItems::addTabItems)
                    .withTabsBefore(TinkerWorld.tabWorld.getId())
                    .build());

    protected static final Item.Properties GENERAL_PROPS = new Item.Properties();
    protected static final Supplier<Item> TOOLTIP_ITEM = () -> new TooltipItem(GENERAL_PROPS);
    protected static final Function<Block,? extends BlockItem> GENERAL_BLOCK_ITEM = (b) -> new BlockItem(b, GENERAL_PROPS);
    protected static final Function<Block,? extends BlockItem> TOOLTIP_BLOCK_ITEM = (b) -> new BlockTooltipItem(b, GENERAL_PROPS);
    protected static final Function<Block,? extends BlockItem> UNCOMMON_BLOCK_ITEM = (b) -> new BlockItem(b, new Item.Properties().rarity(Rarity.UNCOMMON));

    protected static BlockBehaviour.Properties metalBuilder(MapColor color) {
        return builder(color).sound(METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE).requiresCorrectToolForDrops().strength(5.0f);
    }

    public static final ItemObject<Item> NecroticBoneMeal = ITEMS.register("necrotic_bone_meal", () -> new NecroticBoneMealItem(GENERAL_PROPS));
    public static final ItemObject<Item> OceanSlimeBall = ITEMS.register("ocean_slime_ball", GENERAL_PROPS);
    public static final ItemObject<Block> OceanSlime = BLOCKS.register("ocean_slime", () -> new StickySlimeBlock(builder(MapColor.COLOR_BLUE).sound(SoundType.SLIME_BLOCK).strength(0.5F).friction(0.5F).lightLevel(s -> 3).noOcclusion(), (state, other) -> !other.is(ModTags.Blocks.Slimy)), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> OceanCongealedSlime = BLOCKS.register("ocean_congealed_slime", () -> new CongealedSlimeBlock(builder(MapColor.COLOR_BLUE).sound(SoundType.SLIME_BLOCK).strength(0.5F).friction(0.5F).lightLevel(s -> 3)), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Item> OceanSlimeBottle = ITEMS.register("ocean_slime_bottle", () -> new ContainerFoodItem.FluidContainerFoodItem(new Item.Properties().food(ModFood.OCEAN_BOTTLE).stacksTo(16).craftRemainder(Items.GLASS_BOTTLE), () -> new FluidStack(ModFluids.OceanSlime.get(), FluidValues.BOTTLE)));
    public static final ItemObject<Block> OceanCake = BLOCKS.register("ocean_cake", () -> new WaterFoodCakeBlock(Block.Properties.of().sound(SoundType.WOOL).forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY), ModFood.OCEAN_CAKE, FoodCakeBlock.EffectCombination.BLOCK), (b) -> new BlockItem(b, new Item.Properties().stacksTo(1)));
    public static final GeodeItemObject OceanGeode = BLOCKS.registerGeode("ocean_slime_crystal", MapColor.COLOR_BLUE, Sounds.ENDER_CRYSTAL, Sounds.ENDER_CRYSTAL_CHIME.getSound(), Sounds.ENDER_CRYSTAL_CLUSTER, 5, GENERAL_PROPS);
    public static final ItemObject<Block> SlimeGravel = BLOCKS.register("slime_gravel", () -> new SlimeGravelBlock(builder(MapColor.COLOR_BLUE).sound(SoundType.GRAVEL).instrument(NoteBlockInstrument.SNARE).strength(0.6F)), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> IchorVent = BLOCKS.register("ichor_vent", () -> new IchorVentBlock(builder(MapColor.STONE).sound(SoundType.STONE).strength(1F).requiresCorrectToolForDrops()), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> GlowstoneOre = BLOCKS.register("glowstone_ore", () -> new Block(builder(MapColor.STONE).sound(SoundType.STONE).strength(5F).requiresCorrectToolForDrops().lightLevel(state -> 7)), GENERAL_BLOCK_ITEM);

    public static final ItemObject<Block> IchorFern = BLOCKS.register("ichor_slime_fern", () -> new IchorTallGrassBlock(builder(MapColor.COLOR_ORANGE).sound(SoundType.ROOTS).offsetType(BlockBehaviour.OffsetType.XZ).replaceable().instabreak().noCollission().pushReaction(PushReaction.DESTROY), FoliageType.ICHOR), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> IchorTallGrass = BLOCKS.register("ichor_slime_tall_grass", () -> new IchorTallGrassBlock(builder(MapColor.COLOR_ORANGE).sound(SoundType.ROOTS).offsetType(BlockBehaviour.OffsetType.XZ).replaceable().instabreak().noCollission().pushReaction(PushReaction.DESTROY), FoliageType.ICHOR), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> IchorSlimeSapling = BLOCKS.register("ichor_slime_sapling", () -> new IchorFungusBlock(builder(MapColor.COLOR_ORANGE).sound(SoundType.FUNGUS).instabreak().noCollission().pushReaction(PushReaction.DESTROY), ModResourceKeys.ichorSlimeFungus), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<IchorNyliumBlock> IchorEarthSlimeNylium = BLOCKS.register("ichor_earth_slime_grass", () -> new IchorNyliumBlock(ichorNylium(), DirtType.EARTH), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<IchorNyliumBlock> IchorSkySlimeNylium = BLOCKS.register("ichor_sky_slime_grass", () -> new IchorNyliumBlock(ichorNylium(), DirtType.SKY), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<IchorNyliumBlock> IchorIchorSlimeNylium = BLOCKS.register("ichor_ichor_slime_grass", () -> new IchorNyliumBlock(ichorNylium(), DirtType.ICHOR), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<IchorNyliumBlock> IchorEnderSlimeNylium = BLOCKS.register("ichor_ender_slime_grass", () -> new IchorNyliumBlock(ichorNylium(), DirtType.ENDER), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<IchorNyliumBlock> IchorVanillaSlimeNylium = BLOCKS.register("ichor_vanilla_slime_grass", () -> new IchorNyliumBlock(ichorNylium(), DirtType.VANILLA), TOOLTIP_BLOCK_ITEM);

    public static final ItemObject<Block> SlimeWeed = BLOCKS.register("slime_weed", () -> new GlowLichenBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WATER).noCollission().instabreak().sound(SoundType.WET_GRASS).pushReaction(PushReaction.DESTROY)), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> SlimeKelp = BLOCKS.register("slime_kelp", () -> new SlimeKelpBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WATER).noCollission().randomTicks().instabreak().sound(SoundType.WET_GRASS).pushReaction(PushReaction.DESTROY)), GENERAL_BLOCK_ITEM);
    public static final RegistryObject<Block> SlimeKelpPlant = BLOCKS.registerNoItem("slime_kelp_plant", () -> new SlimeKelpPlantBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WATER).noCollission().instabreak().sound(SoundType.WET_GRASS).pushReaction(PushReaction.DESTROY)));
    public static final ItemObject<Block> StickPunjis = BLOCKS.register("stick_punjis", () -> new StickPunjisBlock(grass().strength(3.0F).speedFactor(0.4F).noOcclusion()), GENERAL_BLOCK_ITEM);
    public static final ItemObject<Block> FieryFlower = BLOCKS.register("fiery_flower", () -> new FieryFlowerBlock(grass()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> PoisonFlower = BLOCKS.register("poison_flower", () -> new PoisonFlowerBlock(grass()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> SpringyFlower = BLOCKS.register("springy_flower", () -> new SpringyFlowerBlock(grass()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> ConsecratedFlower = BLOCKS.register("consecrated_flower", () -> new ConsecratedFlowerBlock(grass()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> GraveyardFlower = BLOCKS.register("graveyard_flower", () -> new GraveyardFlowerBlock(grass()), UNCOMMON_BLOCK_ITEM);
    public static final RegistryObject<Block> SlimeBerryBush = BLOCKS.registerNoItem("slime_berry_bush", () -> new SlimeBerryBushBlock(bush()));
    public static final RegistryObject<Block> BerriperBush = BLOCKS.registerNoItem("berriper_bush", () -> new CommonBerryBushBlock(bush()));

    public static final ItemObject<Item> DriedSlimeKelp = ITEMS.register("dried_slime_kelp", () -> new Item(new Item.Properties().food(ModFood.DRIED_SLIME_KELP)));
    public static final ItemObject<Item> EarthSlimeBerry = ITEMS.register("earth_slime_berries", () -> new BlockItem(SlimeBerryBush.get(),new Item.Properties().food(ModFood.EARTH_SLIME_BERRY)));
    public static final ItemObject<Item> SkySlimeBerry = ITEMS.register("sky_slime_berries", () -> new BlockItem(SlimeBerryBush.get(),new Item.Properties().food(ModFood.SKY_SLIME_BERRY)));
    public static final ItemObject<Item> EnderSlimeBerry = ITEMS.register("ender_slime_berries", () -> new BlockItem(SlimeBerryBush.get(),new Item.Properties().food(ModFood.ENDER_SLIME_BERRY)));
    public static final ItemObject<Item> BloodSlimeBerry = ITEMS.register("blood_slime_berries", () -> new BlockItem(SlimeBerryBush.get(),new Item.Properties().food(ModFood.BLOOD_SLIME_BERRY)));
    public static final ItemObject<Item> Berriper = ITEMS.register("berripers", () -> new BlockItem(BerriperBush.get(),new Item.Properties().food(ModFood.BERRIPER).rarity(Rarity.UNCOMMON)));

    public static final ItemObject<Item> CopperShard = ITEMS.register("copper_shard", GENERAL_PROPS);
    public static final ItemObject<Item> IronShard = ITEMS.register("iron_shard", GENERAL_PROPS);
    public static final ItemObject<Item> GoldShard = ITEMS.register("gold_shard", GENERAL_PROPS);
    public static final ItemObject<Item> BronzeShard = ITEMS.register("bronze_shard", GENERAL_PROPS);
    public static final ItemObject<Block> BronzeCluster = BLOCKS.register("bronze_cluster", () -> new CrystalClusterBlock(Sounds.ENDER_CRYSTAL_CHIME.getSound(), 7, 3, builder(MapColor.STONE).forceSolidOn().noOcclusion().randomTicks().strength(2.5f).requiresCorrectToolForDrops().pushReaction(PushReaction.DESTROY).lightLevel(state -> 5).sound(SoundType.METAL)), TOOLTIP_BLOCK_ITEM);
    public static final MetalItemObject Bronze = BLOCKS.registerMetal("bronze", metalBuilder(MapColor.COLOR_BROWN), GENERAL_BLOCK_ITEM, GENERAL_PROPS);

    public static final ItemObject<Block> CopperBerryBush = BLOCKS.register("copper_berry_bush", () -> new OreBerryBushBlock(CopperShard, oreBush()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> IronBerryBush = BLOCKS.register("iron_berry_bush", () -> new OreBerryBushBlock(IronShard, oreBush()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> GoldBerryBush = BLOCKS.register("gold_berry_bush", () -> new OreBerryBushBlock(GoldShard, oreBush()), UNCOMMON_BLOCK_ITEM);
    public static final ItemObject<Block> CobaltBerryBush = BLOCKS.register("cobalt_berry_bush", () -> new OreBerryBushBlock(TinkerWorld.cobaltShard, oreBush()), UNCOMMON_BLOCK_ITEM);

    public static final ItemObject<Block> WindSculpture = BLOCKS.register("wind_sculpture", () -> new WindSculptureBlock(builder(MapColor.WOOD).randomTicks().sound(SoundType.WOOD).pushReaction(PushReaction.DESTROY)), TOOLTIP_BLOCK_ITEM);

    public static final ItemObject<Block> WaterBubble = BLOCKS.register("water_bubble", () -> new WaterBubbleBlock(builder(MapColor.WATER).sound(SoundType.WOOL).noCollission().noOcclusion().isValidSpawn(Blocks::never).isSuffocating(Blocks::never).pushReaction(PushReaction.DESTROY)), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> LavaBubble = BLOCKS.register("lava_bubble", () -> new BubbleBlock(builder(MapColor.COLOR_RED).randomTicks().sound(SoundType.WOOL).noCollission().noOcclusion().isValidSpawn(Blocks::never).isSuffocating(Blocks::never).pushReaction(PushReaction.DESTROY), () -> Fluids.LAVA), TOOLTIP_BLOCK_ITEM);
    public static final ItemObject<Block> EarthSlimeBubble = BLOCKS.register("earth_slime_bubble", () -> new SlimeBubbleBlock(builder(MapColor.COLOR_RED).sound(SoundType.WOOL).noCollission().noOcclusion().isValidSpawn(Blocks::never).isRedstoneConductor(Blocks::never).isSuffocating(Blocks::never).pushReaction(PushReaction.DESTROY), TinkerFluids.earthSlime), TOOLTIP_BLOCK_ITEM);

    private static void addTabItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
        output.accept(NecroticBoneMeal);
        output.accept(OceanSlimeBall);
        output.accept(OceanCongealedSlime);
        output.accept(OceanSlime);
        output.accept(OceanSlimeBottle);
        output.accept(OceanCake);
        acceptGeode(output, OceanGeode);

        output.accept(DriedSlimeKelp);
        output.accept(EarthSlimeBerry);
        output.accept(SkySlimeBerry);
        output.accept(EnderSlimeBerry);
        output.accept(BloodSlimeBerry);
        output.accept(Berriper);

        output.accept(CopperShard);
        output.accept(IronShard);
        output.accept(GoldShard);
        output.accept(BronzeShard);
        output.accept(BronzeCluster);
        accept(output, Bronze);

        output.accept(SlimeGravel);
        output.accept(IchorVent);
        output.accept(GlowstoneOre);
        output.accept(IchorFern);
        output.accept(IchorTallGrass);
        output.accept(IchorSlimeSapling);
        output.accept(IchorEarthSlimeNylium);
        output.accept(IchorSkySlimeNylium);
        output.accept(IchorIchorSlimeNylium);
        output.accept(IchorEnderSlimeNylium);
        output.accept(IchorVanillaSlimeNylium);

        output.accept(SlimeWeed);
        output.accept(SlimeKelp);
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
        output.accept(ModEntities.oceanSlimeEntity);
        output.accept(ModEntities.ichorSlimeEntity);
        output.accept(ModEntities.originSlimeEntity);
        output.accept(ModEntities.boggedEntity);
        output.accept(ModEntities.parchedEntity);

        output.accept(WindSculpture);
        output.accept(WaterBubble);
        output.accept(LavaBubble);
        output.accept(EarthSlimeBubble);
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
    protected static BlockBehaviour.Properties builder(MapColor color) {
        return Block.Properties.of().mapColor(color);
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

    public static void registers(IEventBus bus) {
        ITEMS.register(bus);
        BLOCKS.register(bus);
        CREATIVE_TABS.register(bus);
    }
}
