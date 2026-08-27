package com.creeping_creeper.slimeworld.data.provider.loot;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.block.bush.OreBerryBushBlock;
import com.creeping_creeper.slimeworld.init.block.bush.SlimeBerryBushBlock;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.loot.CanToolPerformAction;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.loot.function.RetexturedLootFunction;
import slimeknights.mantle.registration.object.WallBuildingBlockObject;
import slimeknights.tconstruct.common.registration.GeodeItemObject;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.DirtType;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    @SuppressWarnings("deprecation")  // the vanilla registry is perfectly fine for our uses, will make migration away from forge registries easier
    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.stream()
                .filter(block -> SlimeWorld.MODID.equals(BuiltInRegistries.BLOCK.getKey(block).getNamespace()))
                .collect(Collectors.toList());
    }

    protected ModBlockLootTableProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(ModItems.OceanSlime.get());
        add(ModItems.OceanCongealedSlime.get(), block -> createSingleItemTableWithSilkTouch(block, ModItems.OceanCongealedSlime.get(), ConstantValue.exactly(4)));
        add(ModItems.OceanCake.get(), noDrop());
        dropGeode(ModItems.OceanGeode);
        dropSelf(ModItems.SlimeGravel.get());
        dropSelf(ModItems.IchorVent.get());
        add(ModItems.DryingRack.get(), block -> droppingWithFunctions(block, (builder) -> builder.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(RetexturedLootFunction::new)));

        dropBuildingLootTables(ModItems.Cinnabar);
        dropBuildingLootTables(ModItems.PolishedCinnabar);
        dropBuildingLootTables(ModItems.CinnabarBricks);
        dropSelf(ModItems.ChiseledCinnabar.get());
        dropBuildingLootTables(ModItems.Sulfur);
        dropBuildingLootTables(ModItems.PolishedSulfur);
        dropBuildingLootTables(ModItems.SulfurBricks);
        dropSelf(ModItems.ChiseledSulfur.get());
        dropSelf(ModItems.SulfurMud.get());
        dropSelf(ModItems.SulfurSpike.get());
        dropSelf(ModItems.PotentSulfurNausea.get());
        dropSelf(ModItems.PotentSulfurBlindness.get());
        dropSelf(ModItems.PotentSulfurWeakness.get());
        dropSelf(ModItems.PotentSulfurRegeneration.get());
        dropSelf(ModItems.PotentSulfurStrength.get());


        add(ModItems.GlowstoneOre.get(), block -> createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(Items.GLOWSTONE_DUST).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))));
        add(ModItems.DeepSlateGlowstoneOre.get(), block -> createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(Items.GLOWSTONE_DUST).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))));
        dropSelf(ModItems.IsomericGlowstone.get());
        add(ModItems.IsomericRedstoneBlock.get(), block -> createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)).apply(LimitCount.limitCount(IntRange.range(1, 4))))));

        add(ModItems.IchorFern.get(), ModBlockLootTableProvider::onlyShears);
        add(ModItems.IchorTallGrass.get(), ModBlockLootTableProvider::onlyShears);
        dropSelf(ModItems.IchorSlimeSapling.get());
        add(ModItems.IchorEarthSlimeNylium.get(), block -> createSingleItemTableWithSilkTouch(block, TinkerWorld.slimeDirt.get(DirtType.ICHOR), ConstantValue.exactly(4)));
        add(ModItems.IchorSkySlimeNylium.get(), block -> createSingleItemTableWithSilkTouch(block, TinkerWorld.slimeDirt.get(DirtType.ICHOR), ConstantValue.exactly(4)));
        add(ModItems.IchorIchorSlimeNylium.get(), block -> createSingleItemTableWithSilkTouch(block, TinkerWorld.slimeDirt.get(DirtType.ICHOR), ConstantValue.exactly(4)));
        add(ModItems.IchorEnderSlimeNylium.get(), block -> createSingleItemTableWithSilkTouch(block, TinkerWorld.slimeDirt.get(DirtType.ICHOR), ConstantValue.exactly(4)));
        add(ModItems.IchorVanillaSlimeNylium.get(), block -> createSingleItemTableWithSilkTouch(block, TinkerWorld.slimeDirt.get(DirtType.ICHOR), ConstantValue.exactly(4)));

        dropSelf(ModItems.MagicbubbleSapling.get());
        dropSelf(ModItems.SnowaveSapling.get());
        dropSelf(ModItems.MagicbubbleLog.get());
        add(ModItems.Magicbubbleleaves.get(), block -> createLeavesDrops(block, ModItems.MagicbubbleSapling.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F));
        dropSelf(ModItems.ActiveMagicbubbleLog.get());
        add(ModItems.StrippedSnowaveLog.get(), noDrop());
        add(ModItems.Snowaveleaves.get(), block -> createLeavesDrops(block, ModItems.SnowaveSapling.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F));
        add(ModItems.SnowaveLog.get(), noDrop());

        dropSelf(ModItems.SlimeWeed.get());
        dropSelf(ModItems.StickPunjis.get());
        dropSelf(ModItems.FieryFlower.get());
        dropSelf(ModItems.PoisonFlower.get());
        dropSelf(ModItems.SpringyFlower.get());
        dropSelf(ModItems.ConsecratedFlower.get());
        dropSelf(ModItems.GraveyardFlower.get());
        add(ModItems.SlimeBerryBush.get(), this::slimeBerryBushLoot);
        add(ModItems.BerriperBush.get(), noDrop());
        add(ModItems.BerriperBush.get(), block -> createBerry(block, SweetBerryBushBlock.AGE, ModItems.Berriper));
        dropCluster(ModItems.BronzeCluster.get(), ModItems.BronzeShard.get());
        dropSelf(ModItems.Bronze.get());
        dropSelf(ModItems.SlimeBronze.get());
        add(ModItems.CopperBerryBush.get(), block -> createBerry(block, OreBerryBushBlock.AGE, ModItems.CopperShard));
        add(ModItems.IronBerryBush.get(), block -> createBerry(block, OreBerryBushBlock.AGE, ModItems.IronShard));
        add(ModItems.GoldBerryBush.get(), block -> createBerry(block, OreBerryBushBlock.AGE, ModItems.GoldShard));
        add(ModItems.CobaltBerryBush.get(), block -> createBerry(block, OreBerryBushBlock.AGE, TinkerWorld.cobaltShard));

        add(ModItems.WaterBubble.get(), noDrop());
        add(ModItems.LavaBubble.get(), noDrop());
        add(ModItems.EarthSlimeBubble.get(), noDrop());
        add(ModItems.SkySlimeBubble.get(), noDrop());
        add(ModItems.IchorSlimeBubble.get(), noDrop());
        add(ModItems.EnderSlimeBubble.get(), noDrop());
        add(ModItems.OceanSlimeBubble.get(), noDrop());
        add(ModItems.HoneyBubble.get(), noDrop());
        add(ModItems.VenomBubble.get(), noDrop());
    }

    private static final LootItemCondition.Builder SHEARS = CanToolPerformAction.canToolPerformAction(ToolActions.SHEARS_DIG);

    protected static LootTable.Builder onlyShears(ItemLike item) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(SHEARS).add(LootItem.lootTableItem(item)));
    }

    private LootTable.Builder droppingWithFunctions(Block block, Function<LootItem.Builder<?>, LootItem.Builder<?>> mapping) {
        return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(mapping.apply(LootItem.lootTableItem(block)))));
    }

    private LootTable.Builder createBerry(Block block, Property<Integer> property, ItemLike item) {
        return applyExplosionDecay(block, LootTable.lootTable().withPool(LootPool.lootPool().when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, 3))).add(LootItem.lootTableItem(item)).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))).withPool(LootPool.lootPool().when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, 2))).add(LootItem.lootTableItem(item)).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    private LootTable.Builder slimeBerryBushLoot(Block bushBlock) {
        LootPool.Builder poolAge3 = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(bushBlock)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(SlimeBerryBushBlock.AGE, 3))
                )
                .add(AlternativesEntry.alternatives(
                        LootItem.lootTableItem(ModItems.EarthSlimeBerry.get())
                                .when(LootItemBlockStatePropertyCondition
                                        .hasBlockStateProperties(bushBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(SlimeBerryBushBlock.BERRY, 1))
                                ),
                        LootItem.lootTableItem(ModItems.SkySlimeBerry.get())
                                .when(LootItemBlockStatePropertyCondition
                                        .hasBlockStateProperties(bushBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(SlimeBerryBushBlock.BERRY, 2))
                                ),
                        LootItem.lootTableItem(ModItems.EnderSlimeBerry.get())
                                .when(LootItemBlockStatePropertyCondition
                                        .hasBlockStateProperties(bushBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(SlimeBerryBushBlock.BERRY, 4))
                                ),
                        LootItem.lootTableItem(ModItems.BloodSlimeBerry.get())
                                .when(LootItemBlockStatePropertyCondition
                                        .hasBlockStateProperties(bushBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(SlimeBerryBushBlock.BERRY, 5))
                                ),
                        LootItem.lootTableItem(Items.SUGAR)
                ))
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE));

        LootPool.Builder poolAge2 = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemBlockStatePropertyCondition
                        .hasBlockStateProperties(bushBlock)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                .hasProperty(SweetBerryBushBlock.AGE, 2))
                )
                .add(AlternativesEntry.alternatives(
                        LootItem.lootTableItem(ModItems.EarthSlimeBerry.get())
                                .when(LootItemBlockStatePropertyCondition
                                        .hasBlockStateProperties(bushBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(SlimeBerryBushBlock.BERRY, 1))
                                ),
                        LootItem.lootTableItem(ModItems.SkySlimeBerry.get())
                                .when(LootItemBlockStatePropertyCondition
                                        .hasBlockStateProperties(bushBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(SlimeBerryBushBlock.BERRY, 2))
                                ),
                        LootItem.lootTableItem(ModItems.EnderSlimeBerry.get())
                                .when(LootItemBlockStatePropertyCondition
                                        .hasBlockStateProperties(bushBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(SlimeBerryBushBlock.BERRY, 4))
                                ),
                        LootItem.lootTableItem(ModItems.BloodSlimeBerry.get())
                                .when(LootItemBlockStatePropertyCondition
                                        .hasBlockStateProperties(bushBlock)
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(SlimeBerryBushBlock.BERRY, 5))
                                ),
                        LootItem.lootTableItem(Items.SUGAR)
                ))
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE));

        return applyExplosionDecay(bushBlock, LootTable.lootTable().withPool(poolAge3).withPool(poolAge2));
    }


    private void dropGeode(GeodeItemObject geode) {
        dropSelf(geode.getBlock());
        // cluster
        dropCluster(geode.getBud(GeodeItemObject.BudSize.CLUSTER), geode);
        // buds
        for (GeodeItemObject.BudSize size : GeodeItemObject.BudSize.SIZES) {
            dropWhenSilkTouch(geode.getBud(size));
        }
        add(geode.getBudding(), noDrop());
    }

    private void dropCluster(Block cluster, ItemLike drop) {
        add(cluster, block -> createSilkTouchDispatchTable(
                block, LootItem.lootTableItem(drop)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                        .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
                        .otherwise(applyExplosionDecay(block, LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
    }

    private void dropBuildingLootTables(WallBuildingBlockObject object) {
        dropSelf(object.get());
        add(object.getSlab(), this::createSlabItemTable);
        dropSelf(object.getStairs());
        dropSelf(object.getWall());
    }
}
