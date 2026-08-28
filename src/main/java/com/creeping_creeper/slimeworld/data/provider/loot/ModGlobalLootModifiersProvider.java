package com.creeping_creeper.slimeworld.data.provider.loot;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModModifierIds;
import com.creeping_creeper.slimeworld.init.misc.HasOverslimeCondition;
import com.creeping_creeper.slimeworld.init.misc.RemoveOverslimeFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.mantle.loot.AddEntryLootModifier;
import slimeknights.mantle.loot.condition.BlockTagLootCondition;
import slimeknights.mantle.loot.condition.ContainsItemModifierLootCondition;
import slimeknights.mantle.loot.entry.TagPreferenceLootEntry;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.tconstruct.tools.modifiers.loot.HasModifierLootCondition;
import slimeknights.tconstruct.tools.modifiers.loot.ModifierBonusLootFunction;

import static slimeknights.mantle.Mantle.commonResource;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
  public ModGlobalLootModifiersProvider(PackOutput output) {
    super(output, SlimeWorld.MODID);
  }

  @Override
  protected void start() {
      //overwash implementation
      //vanilla
      addOverwash("glowstone", false);
      addOverwash("redstone", false);
      addOverwash("coal", true);
      addOverwash("lapis", true);
      addOverwash("quartz", true);
      addOverwash("emerald", true);
      addOverwash("diamond", true);
      addOverwash("amethyst", ResourceLocation.withDefaultNamespace("amethyst_cluster"));
      //thermal
      addOverwash("apatite", true);
      addOverwash("cinnabar", true);
      addOverwash("niter", true);
      addOverwash("sulfur", true);
      //mek
      addOverwash("fluorite", true);
      //ae2
      addOverwash("certus_quartz", ResourceLocation.fromNamespaceAndPath("ae2", "certus_cluster"));
      //ae2cs
      addOverwash("certus_quartz_2", true);
      //miniutilities
      addOverwash("ender", true);
      //croptopia
      addOverwash("salt", true);
  }

  /** Adds lustrous for an ore */
  private void addOverwash(String name, boolean optional) {
      ResourceLocation ores = commonResource("ores/" + name);
      LootItemCondition condition = new BlockTagLootCondition(TagKey.create(Registries.BLOCK, ores));
      addOverwash(name, name, condition, optional);
  }

    private void addOverwash(String name, ResourceLocation orelike) {
      Block block = ForgeRegistries.BLOCKS.getValue(orelike);
      LootItemCondition condition = null;
      if (block != null) {
          condition = LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).build();
      }
      addOverwash(name + "_cluster", name, condition, true);
    }

    private void addOverwash(String ore, String name, LootItemCondition condition, boolean optional) {
        TagKey<Item> dusts = TagKey.create(Registries.ITEM, commonResource("dusts/" + name));
        ResourceLocation ores = commonResource("ores/" + name);
        AddEntryLootModifier.Builder builder = AddEntryLootModifier.builder(TagPreferenceLootEntry.tagPreference(dusts));
        builder.addCondition(condition)
                .addCondition(new ContainsItemModifierLootCondition(Ingredient.of(TagKey.create(Registries.ITEM, ores))).inverted());
        if (optional) {
            builder.addCondition(new TagFilledCondition<>(dusts));
        }
        add("overwash/" + ore, builder.addCondition(new HasModifierLootCondition(ModModifierIds.overwash))
                .addCondition(new HasOverslimeCondition())
                .addFunction(SetItemCountFunction.setCount(UniformGenerator.between(3, 5)).build())
                .addFunction(ModifierBonusLootFunction.oreDrops(ModModifierIds.overwash, false).build())
                .addFunction(RemoveOverslimeFunction.removeOverslime().build())
                .addFunction(ApplyExplosionDecay.explosionDecay().build())
                .build());
    }
}
