package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("removal")
public class ModBlockTagProvider extends BlockTagsProvider {
    private static final TagKey<Block> MAID_AVOID = BlockTags.create(new ResourceLocation("touhou_little_maid", "maid_avoid_block"));

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider lookupProvider) {
        //vanilla
        this.tag(BlockTags.FALL_DAMAGE_RESETTING).add(ModItems.SlimeBerryBush.get(), ModItems.BerriperBush.get(), ModItems.CopperBerryBush.get(), ModItems.IronBerryBush.get(), ModItems.GoldBerryBush.get());
        this.tag(BlockTags.LEAVES).add(ModItems.Snowaveleaves.get(), ModItems.Magicbubbleleaves.get());
        this.tag(BlockTags.LOGS_THAT_BURN).add(ModItems.SnowaveLog.get(), ModItems.StrippedSnowaveLog.get(), ModItems.MagicbubbleLog.get(), ModItems.ActiveMagicbubbleLog.get());
        this.tag(BlockTags.MUSHROOM_GROW_BLOCK).addOptionalTag(TinkerTags.Blocks.SLIMY_NYLIUM);
        this.tag(BlockTags.SAPLINGS).add(ModItems.SnowaveSapling.get(), ModItems.MagicbubbleSapling.get());
        this.tag(BlockTags.SMALL_FLOWERS).add(ModItems.FieryFlower.get(), ModItems.PoisonFlower.get(), ModItems.SpringyFlower.get(), ModItems.ConsecratedFlower.get(), ModItems.GraveyardFlower.get());
        this.tag(BlockTags.STAIRS).add(ModItems.Sulfur.getStairs(), ModItems.PolishedSulfur.getStairs(), ModItems.SulfurBricks.getStairs());
        this.tag(BlockTags.SLABS).add(ModItems.Sulfur.getSlab(), ModItems.PolishedSulfur.getSlab(), ModItems.SulfurBricks.getSlab());
        this.tag(BlockTags.WALLS).add(ModItems.Sulfur.getWall(), ModItems.PolishedSulfur.getWall(), ModItems.SulfurBricks.getWall());
        //common
        this.tag(ModTags.Blocks.GLOWSTONE_ORE).add(ModItems.GlowstoneOre.get(), ModItems.DeepSlateGlowstoneOre.get());
        this.tag(Tags.Blocks.ORES).addTag(ModTags.Blocks.GLOWSTONE_ORE);
        this.tag(ModItems.Bronze.getBlockTag()).add(ModItems.Bronze.get());
        this.tag(Tags.Blocks.STORAGE_BLOCKS).addTag(ModItems.Bronze.getBlockTag());
        this.tag(ModTags.Blocks.STRIPPED_LOGS).add(ModItems.StrippedSnowaveLog.get());
        //self

        //touhou little maid
        this.tag(MAID_AVOID).add(Blocks.WITHER_ROSE, ModItems.FieryFlower.get(), ModItems.PoisonFlower.get());
    }
}
