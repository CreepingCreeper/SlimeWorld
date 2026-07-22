package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModResourceKeys;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends BiomeTagsProvider {
    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider lookupProvider) {
        //vanilla
        //common
        //tconstruct
        //self
        this.tag(ModTags.Biomes.IS_SLIMEWORLD).add(ModResourceKeys.HoneyFields, ModResourceKeys.DeepForgottenOcean, ModResourceKeys.ForgottenOcean, ModResourceKeys.River, ModResourceKeys.RedBeach,
                ModResourceKeys.ClayWaste, ModResourceKeys.EarthPlains, ModResourceKeys.SkyHills, ModResourceKeys.BloodForest, ModResourceKeys.MudMeadow, ModResourceKeys.EnderSwamp,
                ModResourceKeys.LaceratedLand, ModResourceKeys.CrystalDesert, ModResourceKeys.SulfurSprings, ModResourceKeys.CinnabarCaves, ModResourceKeys.IchorCaves);
        this.tag(ModTags.Biomes.ICHOR_SLIME_SPAWN).add(ModResourceKeys.IchorCaves);
        this.tag(ModTags.Biomes.TERRACUBE_SPAWN).add(ModResourceKeys.ClayWaste);
        this.tag(ModTags.Biomes.SULFUR_CUBE_SPAWN).add(ModResourceKeys.SulfurSprings);
        this.tag(ModTags.Biomes.SKY_VARIANT_GRASS).add(ModResourceKeys.SkyHills);
        this.tag(ModTags.Biomes.BLOOD_VARIANT_GRASS).add(ModResourceKeys.BloodForest);
        this.tag(ModTags.Biomes.ENDER_VARIANT_GRASS).add(ModResourceKeys.EnderSwamp);
       }
}
