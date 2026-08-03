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
import slimeknights.tconstruct.common.registration.GeodeItemObject;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.block.DirtType;
import slimeknights.tconstruct.world.block.FoliageType;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("removal")
public class ModBlockTagsProvider extends BlockTagsProvider {
    private static final TagKey<Block> MAID_AVOID = BlockTags.create(new ResourceLocation("touhou_little_maid", "maid_avoid_block"));

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider lookupProvider) {
        //vanilla
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModItems.Bronze.get()).add(ModItems.OceanGeode.getBlock()).add(ModItems.OceanGeode.getBud(GeodeItemObject.BudSize.SMALL)).add(ModItems.OceanGeode.getBud(GeodeItemObject.BudSize.MEDIUM)).add(ModItems.OceanGeode.getBud(GeodeItemObject.BudSize.LARGE))
                .add(ModItems.OceanGeode.getBud(GeodeItemObject.BudSize.CLUSTER)).add(ModItems.OceanGeode.getBudding()).add(ModItems.BronzeCluster.get()).add(ModItems.GlowstoneOre.get()).add(ModItems.DeepSlateGlowstoneOre.get()).add(ModItems.IsomericGlowstone.get())
                .add(ModItems.Cinnabar.get()).add(ModItems.Cinnabar.getStairs()).add(ModItems.Cinnabar.getSlab()).add(ModItems.Cinnabar.getWall()).add(ModItems.PolishedCinnabar.get()).add(ModItems.PolishedCinnabar.getStairs())
                .add(ModItems.PolishedCinnabar.getSlab()).add(ModItems.PolishedCinnabar.getWall()).add(ModItems.CinnabarBricks.get()).add(ModItems.CinnabarBricks.getStairs()).add(ModItems.CinnabarBricks.getSlab()).add(ModItems.CinnabarBricks.getWall())
                .add(ModItems.Sulfur.get()).add(ModItems.Sulfur.getStairs()).add(ModItems.Sulfur.getSlab()).add(ModItems.Sulfur.getWall()).add(ModItems.PolishedSulfur.get()).add(ModItems.PolishedSulfur.getStairs())
                .add(ModItems.PolishedSulfur.getSlab()).add(ModItems.PolishedSulfur.getWall()).add(ModItems.SulfurBricks.get()).add(ModItems.SulfurBricks.getStairs()).add(ModItems.SulfurBricks.getSlab()).add(ModItems.SulfurBricks.getWall())
                .add(ModItems.PotentSulfurNausea.get()).add(ModItems.PotentSulfurBlindness.get()).add(ModItems.PotentSulfurWeakness.get()).add(ModItems.PotentSulfurRegeneration.get()).add(ModItems.PotentSulfurStrength.get()).add(ModItems.SulfurSpike.get());
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModItems.OceanCongealedSlime.get()).add(ModItems.SlimeGravel.get()).add(ModItems.IchorEarthSlimeNylium.get()).add(ModItems.IchorSkySlimeNylium.get()).add(ModItems.IchorIchorSlimeNylium.get())
                .add(ModItems.IchorEnderSlimeNylium.get()).add(ModItems.IchorVanillaSlimeNylium.get()).add(ModItems.SulfurMud.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(ModItems.Bronze.get()).add(ModItems.IchorEarthSlimeNylium.get()).add(ModItems.IchorSkySlimeNylium.get()).add(ModItems.IchorIchorSlimeNylium.get()).add(ModItems.IchorEnderSlimeNylium.get())
                .add(ModItems.IchorVanillaSlimeNylium.get());
        this.tag(BlockTags.NEEDS_STONE_TOOL).add(ModItems.GlowstoneOre.get()).add(ModItems.DeepSlateGlowstoneOre.get());
        
        this.tag(BlockTags.FALL_DAMAGE_RESETTING).add(ModItems.SlimeBerryBush.get(), ModItems.BerriperBush.get(), ModItems.CopperBerryBush.get(), ModItems.IronBerryBush.get(), ModItems.GoldBerryBush.get());
        this.tag(BlockTags.LEAVES).add(ModItems.Snowaveleaves.get(), ModItems.Magicbubbleleaves.get());
        this.tag(BlockTags.LOGS_THAT_BURN).add(ModItems.SnowaveLog.get(), ModItems.StrippedSnowaveLog.get(), ModItems.MagicbubbleLog.get(), ModItems.ActiveMagicbubbleLog.get());
        this.tag(BlockTags.MUSHROOM_GROW_BLOCK).addTag(TinkerTags.Blocks.SLIMY_NYLIUM);
        this.tag(BlockTags.SAPLINGS).add(ModItems.SnowaveSapling.get(), ModItems.MagicbubbleSapling.get());
        this.tag(BlockTags.SMALL_FLOWERS).add(ModItems.FieryFlower.get(), ModItems.PoisonFlower.get(), ModItems.SpringyFlower.get(), ModItems.ConsecratedFlower.get(), ModItems.GraveyardFlower.get());
        this.tag(BlockTags.STAIRS).add(ModItems.Cinnabar.getStairs(), ModItems.PolishedCinnabar.getStairs(), ModItems.CinnabarBricks.getStairs(), ModItems.Sulfur.getStairs(), ModItems.PolishedSulfur.getStairs(), ModItems.SulfurBricks.getStairs());
        this.tag(BlockTags.SLABS).add(ModItems.Cinnabar.getSlab(), ModItems.PolishedCinnabar.getSlab(), ModItems.CinnabarBricks.getSlab(), ModItems.Sulfur.getSlab(), ModItems.PolishedSulfur.getSlab(), ModItems.SulfurBricks.getSlab());
        this.tag(BlockTags.WALLS).add(ModItems.Cinnabar.getWall(), ModItems.PolishedCinnabar.getWall(), ModItems.CinnabarBricks.getWall(), ModItems.Sulfur.getWall(), ModItems.PolishedSulfur.getWall(), ModItems.SulfurBricks.getWall());
        //common
        this.tag(ModTags.Blocks.GLOWSTONE_ORE).add(ModItems.GlowstoneOre.get(), ModItems.DeepSlateGlowstoneOre.get());
        this.tag(Tags.Blocks.ORES).addTag(ModTags.Blocks.GLOWSTONE_ORE);
        this.tag(ModItems.Bronze.getBlockTag()).add(ModItems.Bronze.get());
        this.tag(Tags.Blocks.STORAGE_BLOCKS).addTag(ModItems.Bronze.getBlockTag());
        this.tag(ModTags.Blocks.STRIPPED_LOGS).add(ModItems.StrippedSnowaveLog.get());
        //tconstruct
        this.tag(TinkerTags.Blocks.HARVESTABLE_INTERACT).add(ModItems.SlimeBerryBush.get(), ModItems.BerriperBush.get(), ModItems.CopperBerryBush.get(), ModItems.IronBerryBush.get(), ModItems.GoldBerryBush.get());
        this.tag(FoliageType.ICHOR.getGrassBlockTag()).add(ModItems.IchorEarthSlimeNylium.get(), ModItems.IchorSkySlimeNylium.get(), ModItems.IchorIchorSlimeNylium.get(), ModItems.IchorEnderSlimeNylium.get(), ModItems.IchorVanillaSlimeNylium.get());
        this.tag(DirtType.ICHOR.getBlockTag()).add(ModItems.IchorEarthSlimeNylium.get(), ModItems.IchorSkySlimeNylium.get(), ModItems.IchorIchorSlimeNylium.get(), ModItems.IchorEnderSlimeNylium.get(), ModItems.IchorVanillaSlimeNylium.get());
        this.tag(TinkerTags.Blocks.SLIMY_SOIL).add(ModItems.SulfurMud.get());
        this.tag(TinkerTags.Blocks.SLIME_BLOCK).add(ModItems.OceanSlime.get());
        this.tag(TinkerTags.Blocks.CONGEALED_SLIME).add(ModItems.OceanCongealedSlime.get());
        //self
        this.tag(ModTags.Blocks.CAUSES_CONTINUOUS_GEYSER_ERUPTIONS).add(Blocks.LAVA, TinkerFluids.blazingBlood.getBlock());
        this.tag(ModTags.Blocks.CAUSES_CONTINUOUS_GEYSER_ERUPTIONS).add(Blocks.MAGMA_BLOCK, TinkerFluids.magma.getBlock());
        this.tag(ModTags.Blocks.ICHOR_SLIME_SPAWN).addTag(BlockTags.MOSS_REPLACEABLE).add(TinkerWorld.slimeDirt.get(DirtType.ICHOR));
        this.tag(ModTags.Blocks.ICHOR_SLIME_SPAWN).add(ModItems.IchorEarthSlimeNylium.get(), ModItems.IchorSkySlimeNylium.get(), ModItems.IchorIchorSlimeNylium.get(), ModItems.IchorEnderSlimeNylium.get(), ModItems.IchorVanillaSlimeNylium.get());
        this.tag(ModTags.Blocks.NECROTIC_CLONABLE).add(Blocks.WITHER_ROSE, ModItems.FieryFlower.get(), ModItems.PoisonFlower.get(), ModItems.SpringyFlower.get(), ModItems.ConsecratedFlower.get(), ModItems.GraveyardFlower.get());
        this.tag(ModTags.Blocks.SLIMY).add(Blocks.SLIME_BLOCK, Blocks.HONEY_BLOCK, ModItems.OceanSlime.get());
        for (SlimeType type : SlimeType.values()) {
            this.tag(ModTags.Blocks.SLIMY).add(TinkerWorld.slime.get(type));
        }
        this.tag(ModTags.Blocks.SULFUR_FEATURE_BASE).add(ModItems.Sulfur.get(), ModItems.SulfurMud.get());
        this.tag(ModTags.Blocks.TERRACUBE_SPAWN).add(Blocks.CLAY);
        //touhou little maid
        this.tag(MAID_AVOID).add(Blocks.WITHER_ROSE, ModItems.FieryFlower.get(), ModItems.PoisonFlower.get());
    }
}
