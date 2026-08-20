package com.creeping_creeper.slimeworld.data.provider.tags;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("removal")
public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
    private final ResourceLocation MAID = new ResourceLocation("touhou_little_maid", "maid");
    private final ResourceLocation FD_ROTTEN_TOMATO = new ResourceLocation("farmersdelight", "rotten_tomato");
    private final ResourceLocation FC_ROTTEN_TOMATO = new ResourceLocation("farm_and_charm", "rotten_tomato");

    public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider lookupProvider) {
        //vanilla
        tag(EntityTypeTags.FROG_FOOD).add(ModEntities.OceanSlimeEntity.get(), ModEntities.IchorSlimeEntity.get(), ModEntities.OriginSlimeEntity.get(), ModEntities.TomatoSlimeEntity.get(), ModEntities.SulfurCubeEntity.get());
        tag(EntityTypeTags.SKELETONS).add(ModEntities.BoggedEntity.get(), ModEntities.ParchedEntity.get());
        //common
        tag(ModTags.EntityTypes.SLIME).add(ModEntities.OceanSlimeEntity.get(), ModEntities.IchorSlimeEntity.get(), ModEntities.OriginSlimeEntity.get(), ModEntities.TomatoSlimeEntity.get(), ModEntities.SulfurCubeEntity.get(),
                ModEntities.SteelSlimeBossEntity.get(), ModEntities.KnightSlimeBossEntity.get());
        tag(Tags.EntityTypes.BOSSES).add(ModEntities.SteelSlimeBossEntity.get(), ModEntities.KnightSlimeBossEntity.get());
        //self
        tag(ModTags.EntityTypes.ANTIGRAVITY).add(ModEntities.IchorSlimeEntity.get());
        tag(ModTags.EntityTypes.SLIME_GOLEM).add(ModEntities.EarthSlimeGolemEntity.get(), ModEntities.SkySlimeGolemEntity.get(), ModEntities.OceanSlimeEntity.get(), ModEntities.IchorGolemEntity.get(), ModEntities.EnderSlimeGolemEntity.get());
        tag(ModTags.EntityTypes.ORE_BERRY_BUSHES_IMMUNE).add(EntityType.FOX, EntityType.BEE).addTag(ModTags.EntityTypes.SLIME).addTag(ModTags.EntityTypes.SLIME_GOLEM).addOptional(MAID);
        tag(ModTags.EntityTypes.PLAY_SULFUR_CUBE).add(EntityType.PLAYER).addOptional(MAID);
        tag(ModTags.EntityTypes.SUMMON_TOMATO_SLIME).add(ModEntities.TomatoProjectileEntity.get()).addOptional(FD_ROTTEN_TOMATO).addOptional(FC_ROTTEN_TOMATO);
    }
}
