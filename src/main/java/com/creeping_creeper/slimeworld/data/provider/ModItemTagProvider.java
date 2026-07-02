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
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.data.tags.ItemTagProvider;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("removal")
public class ModItemTagProvider extends ItemTagProvider {
    private static final TagKey<Item> MAID_TAMED = ItemTags.create(new ResourceLocation("touhou_little_maid", "maid_tamed_item"));

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, existingFileHelper);
    }

    @SuppressWarnings({"unchecked"})
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

        //touhou little maid
        this.tag(MAID_TAMED).add(ModItems.TomatoPudding.get());
    }
}
