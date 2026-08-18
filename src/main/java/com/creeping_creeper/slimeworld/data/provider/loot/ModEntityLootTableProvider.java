package com.creeping_creeper.slimeworld.data.provider.loot;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.shared.TinkerCommons;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.entity.ArmoredSlimeEntity;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Stream;

public class ModEntityLootTableProvider extends EntityLootSubProvider {
    protected ModEntityLootTableProvider() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return ForgeRegistries.ENTITY_TYPES.getEntries().stream()
                .filter(entry -> SlimeWorld.MODID.equals(entry.getKey().location().getNamespace()))
                .map(Map.Entry::getValue);
    }

    @Override
    public void generate() {
        add(ModEntities.Sllama.get(), LootTable.lootTable());
        add(ModEntities.Grass.get(), LootTable.lootTable());


        add(ModEntities.BoggedEntity.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)).setLimit(1)).apply(SetPotionFunction.setPotion(Potions.POISON))).when(LootItemKilledByPlayerCondition.killedByPlayer())));
        add(ModEntities.ParchedEntity.get(), LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.TIPPED_ARROW).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F))).apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)).setLimit(1)).apply(SetPotionFunction.setPotion(Potions.WEAKNESS))).when(LootItemKilledByPlayerCondition.killedByPlayer())));

        add(ModEntities.OceanSlimeEntity.get(), dropSlimeballs(ModItems.OceanSlimeBall.get(), ModItems.BronzeShard.get()));
        add(ModEntities.OriginSlimeEntity.get(), LootTable.lootTable());
        add(ModEntities.IchorSlimeEntity.get(), dropSlimeballs(TinkerCommons.slimeball.get(SlimeType.ICHOR), TinkerWorld.cobaltShard.get()));
        add(ModEntities.SulfurCubeEntity.get(), dropSlimeballs(ModItems.SulfurGoo.get(), null));
        add(ModEntities.TomatoSlimeEntity.get(), dropSlimeballs(ModItems.TomatoPudding.get(), null));

        add(ModEntities.EarthSlimeGolemEntity.get(), LootTable.lootTable());
        add(ModEntities.SkySlimeGolemEntity.get(), LootTable.lootTable());
        add(ModEntities.OceanSlimeGolemEntity.get(), LootTable.lootTable());
        add(ModEntities.IchorGolemEntity.get(), LootTable.lootTable());
        add(ModEntities.EnderSlimeGolemEntity.get(), LootTable.lootTable());

        add(ModEntities.SteelSlimeBossEntity.get(), LootTable.lootTable());
        add(ModEntities.KnightSlimeBossEntity.get(), LootTable.lootTable());
    }

    private LootTable.Builder dropSlimeballs(Item slimeball, @Nullable Item nugget) {
        LootItemCondition.Builder small = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().subPredicate(SlimePredicate.sized(MinMaxBounds.Ints.exactly(1))));
        LootItemCondition.Builder killedByFrog = killedByFrog();
        LootPool.Builder noFrog = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(killedByFrog.invert()).when(small);
        LootPool.Builder frog = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(killedByFrog).when(small);
        // if given a nugget, add that drop when metal
        if (nugget != null) {
            CompoundTag isMetal = new CompoundTag();
            isMetal.putBoolean(ArmoredSlimeEntity.TAG_METAL, true);
            LootItemCondition.Builder predicate = LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().nbt(new NbtPredicate(isMetal)));
            noFrog.add(slimeball(slimeball).when(predicate.invert()));
            noFrog.add(slimeball(nugget).when(predicate));
            frog.add(frogball(slimeball).when(predicate.invert()));
            frog.add(frogball(nugget).when(predicate));
        } else {
            noFrog.add(slimeball(slimeball));
            frog.add(frogball(slimeball));
        }
        return LootTable.lootTable().withPool(noFrog).withPool(frog);
    }

    private static LootPoolEntryContainer.Builder<?> slimeball(Item item) {
        return LootItem.lootTableItem(item)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)));
    }

    /** Drops a frog slimeball */
    private static LootPoolEntryContainer.Builder<?> frogball(Item item) {
        return LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)));
    }
}
