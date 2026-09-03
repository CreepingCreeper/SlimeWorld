package com.creeping_creeper.slimeworld.data.provider.tags;

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

public class ModBlockTagsProvider extends BlockTagsProvider {
    @SuppressWarnings("removal")
    private static final TagKey<Block> MAID_AVOID = BlockTags.create(new ResourceLocation("touhou_little_maid", "maid_avoid_block"));

    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags(@NotNull HolderLookup.Provider lookupProvider) {
        //vanilla
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModItems.Bronze.get(), ModItems.SlimeBronze.get(), ModItems.OceanGeode.getBlock(), ModItems.OceanGeode.getBud(GeodeItemObject.BudSize.SMALL), ModItems.OceanGeode.getBud(GeodeItemObject.BudSize.MEDIUM), ModItems.OceanGeode.getBud(GeodeItemObject.BudSize.LARGE),
                ModItems.OceanGeode.getBud(GeodeItemObject.BudSize.CLUSTER), ModItems.OceanGeode.getBudding(), ModItems.BronzeCluster.get(), ModItems.GlowstoneOre.get(), ModItems.DeepSlateGlowstoneOre.get(), ModItems.IsomericGlowstone.get(),
                ModItems.Cinnabar.get(), ModItems.Cinnabar.getStairs(), ModItems.Cinnabar.getSlab(), ModItems.Cinnabar.getWall(), ModItems.PolishedCinnabar.get(), ModItems.PolishedCinnabar.getStairs(), ModItems.ChiseledCinnabar.get(),
                ModItems.PolishedCinnabar.getSlab(), ModItems.PolishedCinnabar.getWall(), ModItems.CinnabarBricks.get(), ModItems.CinnabarBricks.getStairs(), ModItems.CinnabarBricks.getSlab(), ModItems.CinnabarBricks.getWall(),
                ModItems.Sulfur.get(), ModItems.Sulfur.getStairs(), ModItems.Sulfur.getSlab(), ModItems.Sulfur.getWall(), ModItems.PolishedSulfur.get(), ModItems.PolishedSulfur.getStairs(), ModItems.ChiseledSulfur.get(),
                ModItems.PolishedSulfur.getSlab(), ModItems.PolishedSulfur.getWall(), ModItems.SulfurBricks.get(), ModItems.SulfurBricks.getStairs(), ModItems.SulfurBricks.getSlab(), ModItems.SulfurBricks.getWall()
                , ModItems.SulfurSpike.get()).addTag(ModTags.Blocks.POTENT_SULFUR);
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(ModItems.OceanCongealedSlime.get(), ModItems.SlimeGravel.get(), ModItems.OceanSlimyEnderbarkRoots.get(), ModItems.IchorEarthSlimeNylium.get(), ModItems.IchorSkySlimeNylium.get(), ModItems.IchorIchorSlimeNylium.get(),
                ModItems.IchorEnderSlimeNylium.get(), ModItems.IchorVanillaSlimeNylium.get(), ModItems.SulfurMud.get());
        tag(BlockTags.NEEDS_IRON_TOOL).add(ModItems.Bronze.get(), ModItems.IchorEarthSlimeNylium.get(), ModItems.IchorSkySlimeNylium.get(), ModItems.IchorIchorSlimeNylium.get(), ModItems.IchorEnderSlimeNylium.get(),
                ModItems.IchorVanillaSlimeNylium.get());
        tag(BlockTags.NEEDS_STONE_TOOL).add(ModItems.GlowstoneOre.get(), ModItems.DeepSlateGlowstoneOre.get(), ModItems.SlimeBronze.get());
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(ModItems.OceanSlimyEnderbarkRoots.get());
        tag(BlockTags.SWORD_EFFICIENT).add(ModItems.IchorFern.get(), ModItems.IchorTallGrass.get());
        tag(BlockTags.REPLACEABLE).add(ModItems.IchorFern.get(), ModItems.IchorTallGrass.get());
        tag(BlockTags.REPLACEABLE_BY_TREES).add(ModItems.IchorFern.get(), ModItems.IchorTallGrass.get());
        tag(BlockTags.AZALEA_ROOT_REPLACEABLE).add(ModItems.IchorFern.get(), ModItems.IchorTallGrass.get());

        tag(BlockTags.DRAGON_IMMUNE).add(ModItems.UnknownTpSteel.get(), ModItems.UnknownTpBronze.get(), ModItems.UnknownTpCinder.get(), ModItems.UnknownTpQueen.get(), ModItems.UnknownTpKnight.get());
        tag(BlockTags.WITHER_IMMUNE).add(ModItems.UnknownTpSteel.get(), ModItems.UnknownTpBronze.get(), ModItems.UnknownTpCinder.get(), ModItems.UnknownTpQueen.get(), ModItems.UnknownTpKnight.get());
        tag(BlockTags.FALL_DAMAGE_RESETTING).add(ModItems.SlimeBerryBush.get(), ModItems.BerriperBush.get(), ModItems.CopperBerryBush.get(), ModItems.IronBerryBush.get(), ModItems.GoldBerryBush.get());
        tag(BlockTags.LEAVES).add(ModItems.Snowaveleaves.get(), ModItems.Magicbubbleleaves.get());
        tag(BlockTags.LOGS_THAT_BURN).add(ModItems.SnowaveLog.get(), ModItems.StrippedSnowaveLog.get(), ModItems.MagicbubbleLog.get(), ModItems.ActiveMagicbubbleLog.get());
        tag(BlockTags.MUSHROOM_GROW_BLOCK).addTag(TinkerTags.Blocks.SLIMY_NYLIUM);
        tag(BlockTags.SAPLINGS).add(ModItems.SnowaveSapling.get(), ModItems.MagicbubbleSapling.get());
        tag(BlockTags.SMALL_FLOWERS).add(ModItems.FieryFlower.get(), ModItems.PoisonFlower.get(), ModItems.SpringyFlower.get(), ModItems.ConsecratedFlower.get(), ModItems.GraveyardFlower.get());
        tag(BlockTags.STAIRS).add(ModItems.Cinnabar.getStairs(), ModItems.PolishedCinnabar.getStairs(), ModItems.CinnabarBricks.getStairs(), ModItems.Sulfur.getStairs(), ModItems.PolishedSulfur.getStairs(), ModItems.SulfurBricks.getStairs());
        tag(BlockTags.SLABS).add(ModItems.Cinnabar.getSlab(), ModItems.PolishedCinnabar.getSlab(), ModItems.CinnabarBricks.getSlab(), ModItems.Sulfur.getSlab(), ModItems.PolishedSulfur.getSlab(), ModItems.SulfurBricks.getSlab());
        tag(BlockTags.WALLS).add(ModItems.Cinnabar.getWall(), ModItems.PolishedCinnabar.getWall(), ModItems.CinnabarBricks.getWall(), ModItems.Sulfur.getWall(), ModItems.PolishedSulfur.getWall(), ModItems.SulfurBricks.getWall());
        //common
        tag(ModTags.Blocks.GLOWSTONE_ORE).add(ModItems.GlowstoneOre.get(), ModItems.DeepSlateGlowstoneOre.get());
        tag(Tags.Blocks.ORES).addTag(ModTags.Blocks.GLOWSTONE_ORE);
        tag(ModItems.Bronze.getBlockTag()).add(ModItems.Bronze.get());
        tag(ModItems.SlimeBronze.getBlockTag()).add(ModItems.SlimeBronze.get());
        tag(Tags.Blocks.STORAGE_BLOCKS).addTags(ModItems.Bronze.getBlockTag(), ModItems.SlimeBronze.getBlockTag());
        tag(ModTags.Blocks.STRIPPED_LOGS).add(ModItems.StrippedSnowaveLog.get());
        //tconstruct
        tag(TinkerTags.Blocks.HARVESTABLE_INTERACT).add(ModItems.SlimeBerryBush.get(), ModItems.BerriperBush.get(), ModItems.CopperBerryBush.get(), ModItems.IronBerryBush.get(), ModItems.GoldBerryBush.get());
        tag(FoliageType.ICHOR.getGrassBlockTag()).add(ModItems.IchorEarthSlimeNylium.get(), ModItems.IchorSkySlimeNylium.get(), ModItems.IchorIchorSlimeNylium.get(), ModItems.IchorEnderSlimeNylium.get(), ModItems.IchorVanillaSlimeNylium.get());
        tag(DirtType.ICHOR.getBlockTag()).add(ModItems.IchorEarthSlimeNylium.get(), ModItems.IchorSkySlimeNylium.get(), ModItems.IchorIchorSlimeNylium.get(), ModItems.IchorEnderSlimeNylium.get(), ModItems.IchorVanillaSlimeNylium.get());
        tag(TinkerTags.Blocks.SLIMY_SOIL).add(ModItems.OceanSlimyEnderbarkRoots.get(), ModItems.SulfurMud.get());
        tag(TinkerTags.Blocks.ENDERBARK_ROOTS).add(ModItems.OceanSlimyEnderbarkRoots.get());
        tag(TinkerTags.Blocks.SLIME_BLOCK).add(ModItems.OceanSlime.get());
        tag(TinkerTags.Blocks.CONGEALED_SLIME).add(ModItems.OceanCongealedSlime.get());
        tag(TinkerTags.Blocks.ANVIL_METAL).addTag(ModItems.SlimeBronze.getBlockTag());
        this.tag(TinkerTags.Blocks.SLIMY_FUNGUS_CAN_GROW_THROUGH).add(ModItems.IchorFern.get(), ModItems.IchorTallGrass.get());
        //self
        tag(ModTags.Blocks.ICHOR_CAVES_REPLACEABLE).addTag(BlockTags.BASE_STONE_OVERWORLD).add(Blocks.GRAVEL, TinkerWorld.slimeDirt.get(DirtType.ICHOR));
        tag(ModTags.Blocks.SULFUR_FEATURE_REPLACEABLE).add(ModItems.Sulfur.get(), ModItems.SulfurMud.get());
        tag(ModTags.Blocks.ANIMALS_SPAWN).addTag(TinkerTags.Blocks.SLIMY_GRASS);
        tag(ModTags.Blocks.ICHOR_SLIME_SPAWN).add(ModItems.IchorEarthSlimeNylium.get(), ModItems.IchorSkySlimeNylium.get(), ModItems.IchorIchorSlimeNylium.get(), ModItems.IchorEnderSlimeNylium.get(), ModItems.IchorVanillaSlimeNylium.get());
        tag(ModTags.Blocks.TERRACUBE_SPAWN).add(Blocks.CLAY);
        tag(ModTags.Blocks.NECROTIC_CLONABLE).add(Blocks.WITHER_ROSE, ModItems.FieryFlower.get(), ModItems.PoisonFlower.get(), ModItems.SpringyFlower.get(), ModItems.ConsecratedFlower.get(), ModItems.GraveyardFlower.get());
        tag(ModTags.Blocks.SLIMY).add(Blocks.SLIME_BLOCK, Blocks.HONEY_BLOCK, ModItems.OceanSlime.get());
        for (SlimeType type : SlimeType.values()) {
            tag(ModTags.Blocks.SLIMY).add(TinkerWorld.slime.get(type));
        }
        tag(ModTags.Blocks.CAUSES_CONTINUOUS_GEYSER_ERUPTIONS).add(Blocks.LAVA, TinkerFluids.blazingBlood.getBlock());
        tag(ModTags.Blocks.CAUSES_CONTINUOUS_GEYSER_ERUPTIONS).add(Blocks.MAGMA_BLOCK, TinkerFluids.magma.getBlock());
        tag(ModTags.Blocks.POTENT_SULFUR).add(ModItems.PotentSulfurNausea.get(), ModItems.PotentSulfurBlindness.get(), ModItems.PotentSulfurWeakness.get(), ModItems.PotentSulfurRegeneration.get(), ModItems.PotentSulfurStrength.get());

        //touhou little maid
        tag(MAID_AVOID).add(Blocks.WITHER_ROSE, ModItems.FieryFlower.get(), ModItems.PoisonFlower.get());
    }
}
