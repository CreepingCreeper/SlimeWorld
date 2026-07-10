package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
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
import slimeknights.tconstruct.common.data.tags.ItemTagProvider;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("removal, unchecked")
public class ModItemTagProvider extends ItemTagProvider {
    private static final TagKey<Item> MAID_TAMED = ItemTags.create(new ResourceLocation("touhou_little_maid", "maid_tamed_item"));

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, existingFileHelper);
    }

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

        this.copy(ModTags.Blocks.GLOWSTONE_ORE, ModTags.Items.GLOWSTONE_ORE);
        this.copy(Tags.Blocks.ORES, Tags.Items.ORES);
        this.copy(ModItems.Bronze.getBlockTag(), ModItems.Bronze.getBlockItemTag());
        this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
        this.copy(ModTags.Blocks.STRIPPED_LOGS, ModTags.Items.STRIPPED_LOGS);
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
                .add(Items.TUBE_CORAL_BLOCK).add(Items.BRAIN_CORAL_BLOCK).add(Items.BUBBLE_CORAL_BLOCK).add(Items.FIRE_CORAL_BLOCK).add(Items.HORN_CORAL_BLOCK)
                .add(Items.DEAD_TUBE_CORAL_BLOCK).add(Items.DEAD_BRAIN_CORAL_BLOCK).add(Items.DEAD_BUBBLE_CORAL_BLOCK).add(Items.DEAD_FIRE_CORAL_BLOCK).add(Items.DEAD_HORN_CORAL_BLOCK).add(Items.SPONGE)
                .add(Items.WET_SPONGE).add(Items.DRIED_KELP_BLOCK).add(Items.MOSS_BLOCK).add(Items.MELON).add(Items.HAY_BLOCK).add(Items.PUMPKIN)
                .add(Items.CARVED_PUMPKIN).add(Items.JACK_O_LANTERN).add(Items.OCHRE_FROGLIGHT).add(Items.PEARLESCENT_FROGLIGHT).add(Items.VERDANT_FROGLIGHT);
        this.tag(ModTags.Items.ARCHETYPE_FAST_SLIDING).add(Items.BLUE_ICE).add(Items.PACKED_ICE).add(Items.SNOW_BLOCK);
        this.tag(ModTags.Items.ARCHETYPE_HIGH_RESISTANCE).add(Items.SOUL_SAND).add(Items.SOUL_SOIL);
        this.tag(ModTags.Items.ARCHETYPE_LIGHT).addTag(ItemTags.WOOL);
        this.tag(ModTags.Items.ARCHETYPE_REGULAR).add(Items.WHITE_CONCRETE_POWDER).add(Items.ORANGE_CONCRETE_POWDER).add(Items.MAGENTA_CONCRETE_POWDER).add(Items.LIGHT_BLUE_CONCRETE_POWDER).add(Items.YELLOW_CONCRETE_POWDER)
                .add(Items.LIME_CONCRETE_POWDER).add(Items.PINK_CONCRETE_POWDER).add(Items.GRAY_CONCRETE_POWDER).add(Items.LIGHT_GRAY_CONCRETE_POWDER).add(Items.CYAN_CONCRETE_POWDER).add(Items.PURPLE_CONCRETE_POWDER)
                .add(Items.BLUE_CONCRETE_POWDER).add(Items.BROWN_CONCRETE_POWDER).add(Items.GREEN_CONCRETE_POWDER).add(Items.RED_CONCRETE_POWDER).add(Items.BLACK_CONCRETE_POWDER).add(Items.MUD)
                .add(Items.MUDDY_MANGROVE_ROOTS).add(Items.PACKED_MUD).add(Items.COAL_BLOCK).add(Items.DIRT).add(Items.COARSE_DIRT).add(Items.ROOTED_DIRT)
                .add(Items.PODZOL).add(Items.GRASS_BLOCK).add(Items.CLAY).add(Items.BONE_BLOCK).add(ModItems.SulfurMud.asItem());
        this.tag(ModTags.Items.ARCHETYPE_SLOW_FLAT).add(Items.IRON_BLOCK).add(Items.GOLD_BLOCK).add(Items.RAW_COPPER_BLOCK).add(Items.RAW_GOLD_BLOCK).add(Items.RAW_IRON_BLOCK)
                .addTag(ItemTags.GOLD_ORES).addTag(ItemTags.IRON_ORES).addTag(ItemTags.COPPER_ORES).add(Items.NETHERITE_BLOCK).add(Items.ANCIENT_DEBRIS).add(Items.COPPER_BLOCK)
                .add(Items.WAXED_COPPER_BLOCK).add(Items.EXPOSED_COPPER).add(Items.WAXED_EXPOSED_COPPER).add(Items.WEATHERED_COPPER).add(Items.WAXED_WEATHERED_COPPER).add(Items.OXIDIZED_COPPER)
                .add(Items.WAXED_OXIDIZED_COPPER).add(Items.CUT_COPPER).add(Items.WAXED_CUT_COPPER).add(Items.EXPOSED_CUT_COPPER).add(Items.WAXED_EXPOSED_CUT_COPPER).add(Items.WEATHERED_CUT_COPPER)
                .add(Items.WAXED_WEATHERED_CUT_COPPER).add(Items.OXIDIZED_CUT_COPPER).add(Items.WAXED_OXIDIZED_CUT_COPPER);
        this.tag(ModTags.Items.ARCHETYPE_SLOW_BOUNCY).add(Items.AMETHYST_BLOCK).add(Items.ANDESITE).add(Items.BASALT).add(Items.BLACKSTONE).add(Items.BRICKS)
                .add(Items.CALCITE).add(Items.CHISELED_DEEPSLATE).add(Items.CHISELED_NETHER_BRICKS).add(Items.CHISELED_POLISHED_BLACKSTONE).add(Items.CHISELED_QUARTZ_BLOCK).add(Items.CHISELED_RED_SANDSTONE)
                .add(Items.CHISELED_SANDSTONE).add(Items.CHISELED_STONE_BRICKS).add(Items.COBBLED_DEEPSLATE).add(Items.COBBLESTONE).add(Items.CRACKED_DEEPSLATE_BRICKS).add(Items.CRACKED_DEEPSLATE_TILES)
                .add(Items.CRACKED_NETHER_BRICKS).add(Items.CRACKED_POLISHED_BLACKSTONE_BRICKS).add(Items.CRACKED_STONE_BRICKS).add(Items.CRIMSON_NYLIUM).add(Items.CRYING_OBSIDIAN).add(Items.CUT_RED_SANDSTONE)
                .add(Items.CUT_SANDSTONE).add(Items.DARK_PRISMARINE).add(Items.DEEPSLATE).add(Items.DEEPSLATE_BRICKS).add(Items.DEEPSLATE_TILES).add(Items.DIAMOND_BLOCK)
                .add(Items.DIORITE).add(Items.DRIPSTONE_BLOCK).add(Items.EMERALD_BLOCK).add(Items.END_STONE).add(Items.END_STONE_BRICKS).add(Items.GILDED_BLACKSTONE)
                .add(Items.GLOWSTONE).add(Items.GRANITE).add(Items.LAPIS_BLOCK).add(Items.MOSSY_COBBLESTONE).add(Items.MOSSY_STONE_BRICKS).add(Items.MUD_BRICKS)
                .add(Items.NETHER_BRICKS).add(Items.NETHERRACK).add(Items.OBSERVER).add(Items.OBSIDIAN).add(Items.POLISHED_ANDESITE).add(Items.POLISHED_BASALT)
                .add(Items.POLISHED_BLACKSTONE).add(Items.POLISHED_BLACKSTONE_BRICKS).add(Items.POLISHED_DEEPSLATE).add(Items.POLISHED_DIORITE).add(Items.POLISHED_GRANITE).add(ModItems.PolishedSulfur.asItem())
                .add(Items.PRISMARINE).add(Items.PRISMARINE_BRICKS).add(Items.PURPUR_BLOCK).add(Items.PURPUR_PILLAR).add(Items.QUARTZ_BLOCK).add(Items.QUARTZ_BRICKS)
                .add(Items.NETHER_QUARTZ_ORE).add(Items.QUARTZ_PILLAR).add(Items.RED_NETHER_BRICKS).add(Items.RED_SANDSTONE).add(Items.REDSTONE_LAMP).add(Items.SANDSTONE)
                .add(Items.SEA_LANTERN).add(Items.SMOOTH_BASALT).add(Items.SMOOTH_QUARTZ).add(Items.SMOOTH_RED_SANDSTONE).add(Items.SMOOTH_SANDSTONE).add(Items.SMOOTH_STONE)
                .add(Items.STONE).add(Items.STONE_BRICKS).add(ModItems.Sulfur.asItem()).add(ModItems.SulfurBricks.asItem()).add(Items.TUFF).add(Items.WARPED_NYLIUM)
                .add(Items.WHITE_CONCRETE).add(Items.ORANGE_CONCRETE).add(Items.MAGENTA_CONCRETE).add(Items.LIGHT_BLUE_CONCRETE).add(Items.YELLOW_CONCRETE).add(Items.LIME_CONCRETE)
                .add(Items.PINK_CONCRETE).add(Items.GRAY_CONCRETE).add(Items.LIGHT_GRAY_CONCRETE).add(Items.CYAN_CONCRETE).add(Items.PURPLE_CONCRETE).add(Items.BLUE_CONCRETE)
                .add(Items.BROWN_CONCRETE).add(Items.GREEN_CONCRETE).add(Items.RED_CONCRETE).add(Items.BLACK_CONCRETE).addTag(ItemTags.COAL_ORES).addTag(ItemTags.LAPIS_ORES)
                .addTag(ItemTags.REDSTONE_ORES).addTag(ItemTags.DIAMOND_ORES).addTag(ItemTags.EMERALD_ORES).add(Items.TERRACOTTA).add(Items.WHITE_TERRACOTTA)
                .add(Items.ORANGE_TERRACOTTA).add(Items.MAGENTA_TERRACOTTA).add(Items.LIGHT_BLUE_TERRACOTTA).add(Items.YELLOW_TERRACOTTA).add(Items.LIME_TERRACOTTA).add(Items.PINK_TERRACOTTA)
                .add(Items.GRAY_TERRACOTTA).add(Items.LIGHT_GRAY_TERRACOTTA).add(Items.CYAN_TERRACOTTA).add(Items.PURPLE_TERRACOTTA).add(Items.BLUE_TERRACOTTA).add(Items.BROWN_TERRACOTTA)
                .add(Items.GREEN_TERRACOTTA).add(Items.RED_TERRACOTTA).add(Items.BLACK_TERRACOTTA).add(Items.WHITE_GLAZED_TERRACOTTA).add(Items.ORANGE_GLAZED_TERRACOTTA).add(Items.MAGENTA_GLAZED_TERRACOTTA)
                .add(Items.LIGHT_BLUE_GLAZED_TERRACOTTA).add(Items.YELLOW_GLAZED_TERRACOTTA).add(Items.LIME_GLAZED_TERRACOTTA).add(Items.PINK_GLAZED_TERRACOTTA).add(Items.GRAY_GLAZED_TERRACOTTA)
                .add(Items.LIGHT_GRAY_GLAZED_TERRACOTTA).add(Items.CYAN_GLAZED_TERRACOTTA).add(Items.PURPLE_GLAZED_TERRACOTTA).add(Items.BLUE_GLAZED_TERRACOTTA).add(Items.BROWN_GLAZED_TERRACOTTA)
                .add(Items.GREEN_GLAZED_TERRACOTTA).add(Items.RED_GLAZED_TERRACOTTA).add(Items.BLACK_GLAZED_TERRACOTTA);
        this.tag(ModTags.Items.ARCHETYPE_SLOW_SLIDING).add(Items.BROWN_MUSHROOM_BLOCK).add(Items.RED_MUSHROOM_BLOCK).add(Items.MUSHROOM_STEM).add(Items.MYCELIUM).addTag(ItemTags.WART_BLOCKS).add(Items.SHROOMLIGHT);
        this.tag(ModTags.Items.ARCHETYPE_STICKY).add(Items.HONEYCOMB_BLOCK);
        this.tag(ModTags.Items.ARCHETYPE_EXPLOSIVE).add(Items.TNT);
        this.tag(ModTags.Items.ARCHETYPE_HOT).add(Items.MAGMA_BLOCK);

        //touhou little maid
        this.tag(MAID_TAMED).add(ModItems.TomatoPudding.get());
    }
}
