package com.creeping_creeper.slimeworld.data.provider.tags;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModResourceKeys;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends BiomeTagsProvider {
    public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider lookupProvider) {
        //vanilla
        tag(BiomeTags.HAS_CLOSER_WATER_FOG).add(ModResourceKeys.EnderSwamp, ModResourceKeys.MudMeadow);
        tag(BiomeTags.INCREASED_FIRE_BURNOUT).add(ModResourceKeys.EnderSwamp, ModResourceKeys.MudMeadow);
        tag(BiomeTags.IS_BEACH).add(ModResourceKeys.RedBeach);
        tag(BiomeTags.IS_DEEP_OCEAN).add(ModResourceKeys.DeepForgottenOcean);
        tag(BiomeTags.IS_FOREST).add(ModResourceKeys.BloodForest);
        tag(BiomeTags.IS_HILL).add(ModResourceKeys.SkyHills);
        tag(BiomeTags.IS_OCEAN).add(ModResourceKeys.ForgottenOcean);

        tag(BiomeTags.HAS_MINESHAFT).add(ModResourceKeys.River, ModResourceKeys.RedBeach, ModResourceKeys.ClayWaste, ModResourceKeys.EarthPlains, ModResourceKeys.SkyHills,
                ModResourceKeys.BloodForest, ModResourceKeys.MudMeadow, ModResourceKeys.EnderSwamp, ModResourceKeys.LaceratedLand, ModResourceKeys.CrystalDesert, ModResourceKeys.CinnabarCaves,
                ModResourceKeys.IchorCaves);
        tag(BiomeTags.HAS_MINESHAFT_MESA).add(ModResourceKeys.ClayWaste, ModResourceKeys.SulfurSprings);
        tag(BiomeTags.HAS_OCEAN_RUIN_COLD).add(ModResourceKeys.ForgottenOcean, ModResourceKeys.DeepForgottenOcean);
        tag(BiomeTags.HAS_RUINED_PORTAL_DESERT).add(ModResourceKeys.CrystalDesert);
        tag(BiomeTags.HAS_RUINED_PORTAL_SWAMP).add(ModResourceKeys.EnderSwamp, ModResourceKeys.MudMeadow);
        tag(BiomeTags.HAS_RUINED_PORTAL_STANDARD).add(ModResourceKeys.River, ModResourceKeys.RedBeach, ModResourceKeys.ClayWaste, ModResourceKeys.EarthPlains, ModResourceKeys.SkyHills,
                ModResourceKeys.BloodForest, ModResourceKeys.LaceratedLand, ModResourceKeys.SulfurSprings);

        //common
        tag(Tags.Biomes.IS_CAVE).add(ModResourceKeys.CinnabarCaves, ModResourceKeys.IchorCaves);
        tag(Tags.Biomes.IS_DESERT).add(ModResourceKeys.CrystalDesert);
        tag(Tags.Biomes.IS_SWAMP).add(ModResourceKeys.EnderSwamp, ModResourceKeys.MudMeadow);
        //tconstruct
        tag(TinkerTags.Biomes.EARTHSLIME_ISLANDS).remove(ModTags.Biomes.IS_SLIMEWORLD);
        tag(TinkerTags.Biomes.SKYSLIME_ISLANDS).remove(ModTags.Biomes.IS_SLIMEWORLD);
        //self
        tag(ModTags.Biomes.IS_SLIMEWORLD).add(ModResourceKeys.HoneyFields, ModResourceKeys.DeepForgottenOcean, ModResourceKeys.ForgottenOcean, ModResourceKeys.River, ModResourceKeys.RedBeach,
                ModResourceKeys.ClayWaste, ModResourceKeys.EarthPlains, ModResourceKeys.SkyHills, ModResourceKeys.BloodForest, ModResourceKeys.MudMeadow, ModResourceKeys.EnderSwamp,
                ModResourceKeys.LaceratedLand, ModResourceKeys.CrystalDesert, ModResourceKeys.SulfurSprings, ModResourceKeys.CinnabarCaves, ModResourceKeys.IchorCaves);
        tag(ModTags.Biomes.ICHOR_SLIME_SPAWN).add(ModResourceKeys.IchorCaves);
        tag(ModTags.Biomes.TERRACUBE_SPAWN).add(ModResourceKeys.ClayWaste);
        tag(ModTags.Biomes.SULFUR_CUBE_SPAWN).add(ModResourceKeys.SulfurSprings);
        tag(ModTags.Biomes.SKY_VARIANT_GRASS).add(ModResourceKeys.SkyHills);
        tag(ModTags.Biomes.BLOOD_VARIANT_GRASS).add(ModResourceKeys.BloodForest);
        tag(ModTags.Biomes.ENDER_VARIANT_GRASS).add(ModResourceKeys.EnderSwamp);

        tag(ModTags.Biomes.BAKERY).add(ModResourceKeys.HoneyFields, ModResourceKeys.EarthPlains, ModResourceKeys.SkyHills);
        tag(ModTags.Biomes.GREAT_WALL).add(ModResourceKeys.EarthPlains, ModResourceKeys.SkyHills, ModResourceKeys.BloodForest, ModResourceKeys.CrystalDesert, ModResourceKeys.SulfurSprings);
        tag(ModTags.Biomes.GROUT).add(ModResourceKeys.RedBeach, ModResourceKeys.ClayWaste, ModResourceKeys.EarthPlains, ModResourceKeys.SkyHills, ModResourceKeys.BloodForest, ModResourceKeys.MudMeadow);
        tag(ModTags.Biomes.MAGIC_TOWER).add(ModResourceKeys.EarthPlains, ModResourceKeys.SkyHills, ModResourceKeys.BloodForest, ModResourceKeys.EnderSwamp);
        tag(ModTags.Biomes.SMELTING_WORKSHOP).add(ModResourceKeys.SkyHills, ModResourceKeys.CrystalDesert, ModResourceKeys.SulfurSprings);

    }
}
