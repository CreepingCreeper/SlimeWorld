package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.block.bush.OreBerryBushBlock;
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
import slimeknights.tconstruct.shared.TinkerCommons;
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
        this.dropSelf(ModItems.OceanSlime.get());
        this.add(ModItems.OceanCongealedSlime.get(), block -> createSingleItemTableWithSilkTouch(block, ModItems.OceanCongealedSlime.get(), ConstantValue.exactly(4)));
        this.add(ModItems.OceanCake.get(), noDrop());
        this.dropGeode(ModItems.OceanGeode);
        this.dropSelf(ModItems.SlimeGravel.get());
        this.dropSelf(ModItems.IchorVent.get());
        this.add(ModItems.DryingRack.get(), block -> droppingWithFunctions(block, (builder) -> builder.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(RetexturedLootFunction::new)));

        this.dropBuildingLootTables(ModItems.Sulfur);
        this.dropBuildingLootTables(ModItems.PolishedSulfur);
        this.dropBuildingLootTables(ModItems.SulfurBricks);
        this.dropSelf(ModItems.SulfurMud.get());
        this.dropSelf(ModItems.SulfurSpike.get());
        this.dropSelf(ModItems.PotentSulfurNausea.get());
        this.dropSelf(ModItems.PotentSulfurBlindness.get());
        this.dropSelf(ModItems.PotentSulfurWeakness.get());
        this.dropSelf(ModItems.PotentSulfurRegeneration.get());
        this.dropSelf(ModItems.PotentSulfurStrength.get());


        this.add(ModItems.GlowstoneOre.get(), block -> createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(Items.GLOWSTONE_DUST).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))));
        this.add(ModItems.DeepSlateGlowstoneOre.get(), block -> createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(Items.GLOWSTONE_DUST).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)))));
        this.dropSelf(ModItems.IsomericGlowstone.get());
        this.add(ModItems.IsomericRedstoneBlock.get(), block -> createSilkTouchDispatchTable(block, this.applyExplosionDecay(block, LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE)).apply(LimitCount.limitCount(IntRange.range(1, 4))))));

        this.add(ModItems.IchorFern.get(), ModBlockLootTableProvider::onlyShears);
        this.add(ModItems.IchorTallGrass.get(), ModBlockLootTableProvider::onlyShears);
        this.dropSelf(ModItems.IchorSlimeSapling.get());
        this.add(ModItems.IchorEarthSlimeNylium.get(), block -> createSingleItemTableWithSilkTouch(block, TinkerWorld.slimeDirt.get(DirtType.ICHOR), ConstantValue.exactly(4)));
        this.add(ModItems.IchorSkySlimeNylium.get(), block -> createSingleItemTableWithSilkTouch(block, TinkerWorld.slimeDirt.get(DirtType.ICHOR), ConstantValue.exactly(4)));
        this.add(ModItems.IchorIchorSlimeNylium.get(), block -> createSingleItemTableWithSilkTouch(block, TinkerWorld.slimeDirt.get(DirtType.ICHOR), ConstantValue.exactly(4)));
        this.add(ModItems.IchorEnderSlimeNylium.get(), block -> createSingleItemTableWithSilkTouch(block, TinkerWorld.slimeDirt.get(DirtType.ICHOR), ConstantValue.exactly(4)));
        this.add(ModItems.IchorVanillaSlimeNylium.get(), block -> createSingleItemTableWithSilkTouch(block, TinkerWorld.slimeDirt.get(DirtType.ICHOR), ConstantValue.exactly(4)));

        this.dropSelf(ModItems.MagicbubbleSapling.get());
        this.dropSelf(ModItems.SnowaveSapling.get());
        this.dropSelf(ModItems.MagicbubbleLog.get());
        this.add(ModItems.Magicbubbleleaves.get(), block -> createLeavesDrops(block, ModItems.MagicbubbleSapling.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F));
        this.dropSelf(ModItems.ActiveMagicbubbleLog.get());
        this.add(ModItems.StrippedSnowaveLog.get(), noDrop());
        this.add(ModItems.Snowaveleaves.get(), block -> createLeavesDrops(block, ModItems.SnowaveSapling.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F));
        this.add(ModItems.SnowaveLog.get(), noDrop());

        this.dropSelf(ModItems.SlimeWeed.get());
        this.dropSelf(ModItems.StickPunjis.get());
        this.dropSelf(ModItems.FieryFlower.get());
        this.dropSelf(ModItems.PoisonFlower.get());
        this.dropSelf(ModItems.SpringyFlower.get());
        this.dropSelf(ModItems.ConsecratedFlower.get());
        this.dropSelf(ModItems.GraveyardFlower.get());
        //TODO: 修正黏液莓丛
        this.add(ModItems.SlimeBerryBush.get(), block -> createBerry(block, SweetBerryBushBlock.AGE, ModItems.Berriper));
        this.add(ModItems.BerriperBush.get(), block -> createBerry(block, SweetBerryBushBlock.AGE, ModItems.Berriper));
        this.dropCluster(ModItems.BronzeCluster.get(), ModItems.BronzeShard.get());
        this.dropSelf(ModItems.Bronze.get());
        this.add(ModItems.CopperBerryBush.get(), block -> createBerry(block, OreBerryBushBlock.AGE, ModItems.CopperShard));
        this.add(ModItems.IronBerryBush.get(), block -> createBerry(block, OreBerryBushBlock.AGE, ModItems.IronShard));
        this.add(ModItems.GoldBerryBush.get(), block -> createBerry(block, OreBerryBushBlock.AGE, ModItems.GoldShard));
        this.add(ModItems.CobaltBerryBush.get(), block -> createBerry(block, OreBerryBushBlock.AGE, TinkerWorld.cobaltShard));

        this.add(ModItems.WaterBubble.get(), noDrop());
        this.add(ModItems.LavaBubble.get(), noDrop());
        this.add(ModItems.EarthSlimeBubble.get(), noDrop());
        this.add(ModItems.SkySlimeBubble.get(), noDrop());
        this.add(ModItems.IchorSlimeBubble.get(), noDrop());
        this.add(ModItems.EnderSlimeBubble.get(), noDrop());
        this.add(ModItems.OceanSlimeBubble.get(), noDrop());
        this.add(ModItems.HoneyBubble.get(), noDrop());
        this.add(ModItems.VenomBubble.get(), noDrop());
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

    private void dropGeode(GeodeItemObject geode) {
        this.dropSelf(geode.getBlock());
        // cluster
        dropCluster(geode.getBud(GeodeItemObject.BudSize.CLUSTER), geode);
        // buds
        for (GeodeItemObject.BudSize size : GeodeItemObject.BudSize.SIZES) {
            this.dropWhenSilkTouch(geode.getBud(size));
        }
        this.add(geode.getBudding(), noDrop());
    }

    private void dropCluster(Block cluster, ItemLike drop) {
        this.add(cluster, block -> createSilkTouchDispatchTable(
                block, LootItem.lootTableItem(drop)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                        .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))
                        .otherwise(applyExplosionDecay(block, LootItem.lootTableItem(drop).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
    }

    private void dropBuildingLootTables(WallBuildingBlockObject object) {
        this.dropSelf(object.get());
        this.add(object.getSlab(), this::createSlabItemTable);
        this.dropSelf(object.getStairs());
        this.dropSelf(object.getWall());
    }
}
