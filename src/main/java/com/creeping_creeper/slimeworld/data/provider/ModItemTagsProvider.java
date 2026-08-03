package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;


import java.util.concurrent.CompletableFuture;

import static slimeknights.mantle.Mantle.commonResource;

public class ModItemTagsProvider extends ItemTagsProvider {
    private static final TagKey<Item> COOKED_EGGS = ItemTags.create(commonResource("cooked_eggs"));
    @SuppressWarnings("removal")
    private static final TagKey<Item> MAID_TAMED = ItemTags.create(new ResourceLocation("touhou_little_maid", "maid_tamed_item"));

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, SlimeWorld.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(@NotNull HolderLookup.Provider lookupProvider) {
        //vanilla
        this.copy(BlockTags.LEAVES, ItemTags.LEAVES);
        this.copy(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN);
        this.copy(BlockTags.SAPLINGS, ItemTags.SAPLINGS);
        this.copy(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS);
        this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
        this.copy(BlockTags.SLABS, ItemTags.SLABS);
        this.copy(BlockTags.WALLS, ItemTags.WALLS);
        //common
        this.tag(ModItems.Bronze.getIngotTag()).add(ModItems.Bronze.getIngot());
        this.tag(Tags.Items.INGOTS).addTag(ModItems.Bronze.getIngotTag());
        this.tag(ModItems.Bronze.getNuggetTag()).add(ModItems.Bronze.getNugget());
        this.tag(Tags.Items.NUGGETS).addTag(ModItems.Bronze.getNuggetTag());
        this.tag(ModTags.Items.RAW_BRONZE_NUGGET).add(ModItems.BronzeShard.get());
        this.tag(ModTags.Items.RAW_COPPER_NUGGET).add(ModItems.CopperShard.get());
        this.tag(ModTags.Items.RAW_IRON_NUGGET).add(ModItems.IronShard.get());
        this.tag(ModTags.Items.RAW_GOLD_NUGGET).add(ModItems.GoldShard.get());
        this.tag(ModTags.Items.RAW_NUGGET).addTags(ModTags.Items.RAW_BRONZE_NUGGET, ModTags.Items.RAW_COPPER_NUGGET, ModTags.Items.RAW_IRON_NUGGET, ModTags.Items.RAW_GOLD_NUGGET);
        this.tag(ModTags.Items.OCEAN_SLIME_BALL).add(ModItems.OceanSlimeBall.get());
        this.tag(Tags.Items.SLIMEBALLS).addTag(ModTags.Items.OCEAN_SLIME_BALL);
        this.tag(COOKED_EGGS).add(ModItems.FriedEgg.get());
        this.copy(ModTags.Blocks.GLOWSTONE_ORE, ModTags.Items.GLOWSTONE_ORE);
        this.copy(Tags.Blocks.ORES, Tags.Items.ORES);
        this.copy(ModItems.Bronze.getBlockTag(), ModItems.Bronze.getBlockItemTag());
        this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
        this.copy(ModTags.Blocks.STRIPPED_LOGS, ModTags.Items.STRIPPED_LOGS);
        //tconstruct
        this.copy(TinkerTags.Blocks.SLIME_BLOCK, TinkerTags.Items.SLIME_BLOCK);
        this.copy(TinkerTags.Blocks.CONGEALED_SLIME, TinkerTags.Items.CONGEALED_SLIME);
        //self
        this.tag(ModTags.Items.SULFUR_CUBE_SWALLOWABLE).addTags(ModTags.Items.ARCHETYPE_BOUNCY, ModTags.Items.ARCHETYPE_FAST_FLAT, ModTags.Items.ARCHETYPE_FAST_SLIDING, ModTags.Items.ARCHETYPE_HIGH_RESISTANCE,
                ModTags.Items.ARCHETYPE_LIGHT, ModTags.Items.ARCHETYPE_REGULAR, ModTags.Items.ARCHETYPE_SLOW_BOUNCY, ModTags.Items.ARCHETYPE_SLOW_FLAT, ModTags.Items.ARCHETYPE_SLOW_SLIDING, ModTags.Items.ARCHETYPE_STICKY,
                ModTags.Items.ARCHETYPE_EXPLOSIVE, ModTags.Items.ARCHETYPE_HOT);
        this.tag(ModTags.Items.ARCHETYPE_BOUNCY)
                .addTag(ItemTags.PLANKS)
                .add(Items.BAMBOO_MOSAIC)
                .addTag(ItemTags.LOGS)
                .addTag(ItemTags.BAMBOO_BLOCKS);
        this.tag(ModTags.Items.ARCHETYPE_FAST_FLAT)
                .add(Items.TUBE_CORAL_BLOCK, Items.BRAIN_CORAL_BLOCK, Items.BUBBLE_CORAL_BLOCK, Items.FIRE_CORAL_BLOCK, Items.HORN_CORAL_BLOCK)
                .add(Items.DEAD_TUBE_CORAL_BLOCK, Items.DEAD_BRAIN_CORAL_BLOCK, Items.DEAD_BUBBLE_CORAL_BLOCK, Items.DEAD_FIRE_CORAL_BLOCK, Items.DEAD_HORN_CORAL_BLOCK, Items.SPONGE)
                .add(Items.WET_SPONGE, Items.DRIED_KELP_BLOCK, Items.MOSS_BLOCK, Items.MELON, Items.HAY_BLOCK, Items.PUMPKIN)
                .add(Items.CARVED_PUMPKIN, Items.JACK_O_LANTERN, Items.OCHRE_FROGLIGHT, Items.PEARLESCENT_FROGLIGHT, Items.VERDANT_FROGLIGHT);
        this.tag(ModTags.Items.ARCHETYPE_FAST_SLIDING).add(Items.BLUE_ICE, Items.PACKED_ICE, Items.SNOW_BLOCK);
        this.tag(ModTags.Items.ARCHETYPE_HIGH_RESISTANCE).add(Items.SOUL_SAND, Items.SOUL_SOIL);
        this.tag(ModTags.Items.ARCHETYPE_LIGHT).addTag(ItemTags.WOOL);
        this.tag(ModTags.Items.ARCHETYPE_REGULAR).add(Items.WHITE_CONCRETE_POWDER, Items.ORANGE_CONCRETE_POWDER, Items.MAGENTA_CONCRETE_POWDER, Items.LIGHT_BLUE_CONCRETE_POWDER, Items.YELLOW_CONCRETE_POWDER)
                .add(Items.LIME_CONCRETE_POWDER, Items.PINK_CONCRETE_POWDER, Items.GRAY_CONCRETE_POWDER, Items.LIGHT_GRAY_CONCRETE_POWDER, Items.CYAN_CONCRETE_POWDER, Items.PURPLE_CONCRETE_POWDER)
                .add(Items.BLUE_CONCRETE_POWDER, Items.BROWN_CONCRETE_POWDER, Items.GREEN_CONCRETE_POWDER, Items.RED_CONCRETE_POWDER, Items.BLACK_CONCRETE_POWDER, Items.MUD)
                .add(Items.MUDDY_MANGROVE_ROOTS, Items.PACKED_MUD, Items.COAL_BLOCK, Items.DIRT, Items.COARSE_DIRT, Items.ROOTED_DIRT)
                .add(Items.PODZOL, Items.GRASS_BLOCK, Items.CLAY, Items.BONE_BLOCK, ModItems.SulfurMud.asItem());
        this.tag(ModTags.Items.ARCHETYPE_SLOW_FLAT).add(Items.IRON_BLOCK, Items.GOLD_BLOCK, Items.RAW_COPPER_BLOCK, Items.RAW_GOLD_BLOCK, Items.RAW_IRON_BLOCK)
                .addTag(ItemTags.GOLD_ORES).addTag(ItemTags.IRON_ORES).addTag(ItemTags.COPPER_ORES).add(Items.NETHERITE_BLOCK, Items.ANCIENT_DEBRIS, Items.COPPER_BLOCK)
                .add(Items.WAXED_COPPER_BLOCK, Items.EXPOSED_COPPER, Items.WAXED_EXPOSED_COPPER, Items.WEATHERED_COPPER, Items.WAXED_WEATHERED_COPPER, Items.OXIDIZED_COPPER)
                .add(Items.WAXED_OXIDIZED_COPPER, Items.CUT_COPPER, Items.WAXED_CUT_COPPER, Items.EXPOSED_CUT_COPPER, Items.WAXED_EXPOSED_CUT_COPPER, Items.WEATHERED_CUT_COPPER)
                .add(Items.WAXED_WEATHERED_CUT_COPPER, Items.OXIDIZED_CUT_COPPER, Items.WAXED_OXIDIZED_CUT_COPPER);
        this.tag(ModTags.Items.ARCHETYPE_SLOW_BOUNCY).add(Items.AMETHYST_BLOCK, Items.ANDESITE, Items.BASALT, Items.BLACKSTONE, Items.BRICKS)
                .add(Items.CALCITE, Items.CHISELED_DEEPSLATE, Items.CHISELED_NETHER_BRICKS, Items.CHISELED_POLISHED_BLACKSTONE, Items.CHISELED_QUARTZ_BLOCK, Items.CHISELED_RED_SANDSTONE)
                .add(Items.CHISELED_SANDSTONE, Items.CHISELED_STONE_BRICKS, Items.COBBLED_DEEPSLATE, Items.COBBLESTONE, Items.CRACKED_DEEPSLATE_BRICKS, Items.CRACKED_DEEPSLATE_TILES)
                .add(Items.CRACKED_NETHER_BRICKS, Items.CRACKED_POLISHED_BLACKSTONE_BRICKS, Items.CRACKED_STONE_BRICKS, Items.CRIMSON_NYLIUM, Items.CRYING_OBSIDIAN, Items.CUT_RED_SANDSTONE)
                .add(Items.CUT_SANDSTONE, Items.DARK_PRISMARINE, Items.DEEPSLATE, Items.DEEPSLATE_BRICKS, Items.DEEPSLATE_TILES, Items.DIAMOND_BLOCK)
                .add(Items.DIORITE, Items.DRIPSTONE_BLOCK, Items.EMERALD_BLOCK, Items.END_STONE, Items.END_STONE_BRICKS, Items.GILDED_BLACKSTONE)
                .add(Items.GLOWSTONE, Items.GRANITE, Items.LAPIS_BLOCK, Items.MOSSY_COBBLESTONE, Items.MOSSY_STONE_BRICKS, Items.MUD_BRICKS)
                .add(Items.NETHER_BRICKS, Items.NETHERRACK, Items.OBSERVER, Items.OBSIDIAN, Items.POLISHED_ANDESITE, Items.POLISHED_BASALT)
                .add(Items.POLISHED_BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICKS, Items.POLISHED_DEEPSLATE, Items.POLISHED_DIORITE, Items.POLISHED_GRANITE, ModItems.PolishedCinnabar.asItem(), ModItems.PolishedSulfur.asItem())
                .add(Items.PRISMARINE, Items.PRISMARINE_BRICKS, Items.PURPUR_BLOCK, Items.PURPUR_PILLAR, Items.QUARTZ_BLOCK, Items.QUARTZ_BRICKS)
                .add(Items.NETHER_QUARTZ_ORE, Items.QUARTZ_PILLAR, Items.RED_NETHER_BRICKS, Items.RED_SANDSTONE, Items.REDSTONE_LAMP, Items.SANDSTONE)
                .add(Items.SEA_LANTERN, Items.SMOOTH_BASALT, Items.SMOOTH_QUARTZ, Items.SMOOTH_RED_SANDSTONE, Items.SMOOTH_SANDSTONE, Items.SMOOTH_STONE)
                .add(Items.STONE, Items.STONE_BRICKS, ModItems.Cinnabar.asItem(), ModItems.CinnabarBricks.asItem(), ModItems.Sulfur.asItem(), ModItems.SulfurBricks.asItem(), Items.TUFF, Items.WARPED_NYLIUM)
                .add(Items.WHITE_CONCRETE, Items.ORANGE_CONCRETE, Items.MAGENTA_CONCRETE, Items.LIGHT_BLUE_CONCRETE, Items.YELLOW_CONCRETE, Items.LIME_CONCRETE)
                .add(Items.PINK_CONCRETE, Items.GRAY_CONCRETE, Items.LIGHT_GRAY_CONCRETE, Items.CYAN_CONCRETE, Items.PURPLE_CONCRETE, Items.BLUE_CONCRETE)
                .add(Items.BROWN_CONCRETE, Items.GREEN_CONCRETE, Items.RED_CONCRETE, Items.BLACK_CONCRETE).addTag(ItemTags.COAL_ORES).addTag(ItemTags.LAPIS_ORES)
                .addTag(ItemTags.REDSTONE_ORES).addTag(ItemTags.DIAMOND_ORES).addTag(ItemTags.EMERALD_ORES).add(Items.TERRACOTTA, Items.WHITE_TERRACOTTA)
                .add(Items.ORANGE_TERRACOTTA, Items.MAGENTA_TERRACOTTA, Items.LIGHT_BLUE_TERRACOTTA, Items.YELLOW_TERRACOTTA, Items.LIME_TERRACOTTA, Items.PINK_TERRACOTTA)
                .add(Items.GRAY_TERRACOTTA, Items.LIGHT_GRAY_TERRACOTTA, Items.CYAN_TERRACOTTA, Items.PURPLE_TERRACOTTA, Items.BLUE_TERRACOTTA, Items.BROWN_TERRACOTTA)
                .add(Items.GREEN_TERRACOTTA, Items.RED_TERRACOTTA, Items.BLACK_TERRACOTTA, Items.WHITE_GLAZED_TERRACOTTA, Items.ORANGE_GLAZED_TERRACOTTA, Items.MAGENTA_GLAZED_TERRACOTTA)
                .add(Items.LIGHT_BLUE_GLAZED_TERRACOTTA, Items.YELLOW_GLAZED_TERRACOTTA, Items.LIME_GLAZED_TERRACOTTA, Items.PINK_GLAZED_TERRACOTTA, Items.GRAY_GLAZED_TERRACOTTA)
                .add(Items.LIGHT_GRAY_GLAZED_TERRACOTTA, Items.CYAN_GLAZED_TERRACOTTA, Items.PURPLE_GLAZED_TERRACOTTA, Items.BLUE_GLAZED_TERRACOTTA, Items.BROWN_GLAZED_TERRACOTTA)
                .add(Items.GREEN_GLAZED_TERRACOTTA, Items.RED_GLAZED_TERRACOTTA, Items.BLACK_GLAZED_TERRACOTTA);
        this.tag(ModTags.Items.ARCHETYPE_SLOW_SLIDING).add(Items.BROWN_MUSHROOM_BLOCK, Items.RED_MUSHROOM_BLOCK, Items.MUSHROOM_STEM, Items.MYCELIUM).addTag(ItemTags.WART_BLOCKS).add(Items.SHROOMLIGHT);
        this.tag(ModTags.Items.ARCHETYPE_STICKY).add(Items.HONEYCOMB_BLOCK);
        this.tag(ModTags.Items.ARCHETYPE_EXPLOSIVE).add(Items.TNT);
        this.tag(ModTags.Items.ARCHETYPE_HOT).add(Items.MAGMA_BLOCK);
        //touhou little maid
        this.tag(MAID_TAMED).add(ModItems.TomatoPudding.get());
    }
}
