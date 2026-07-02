package com.creeping_creeper.slimeworld.data.provider;

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
public class ModEntityTypeTagProvider extends EntityTypeTagsProvider {
    private final ResourceLocation MAID = new ResourceLocation("touhou_little_maid", "maid");
    private final ResourceLocation FD_ROTTEN_TOMATO = new ResourceLocation("farmersdelight", "rotten_tomato");
    private final ResourceLocation FC_ROTTEN_TOMATO = new ResourceLocation("farm_and_charm", "rotten_tomato");

    public ModEntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider lookupProvider) {
        //vanilla
        this.tag(EntityTypeTags.FROG_FOOD).add(ModEntities.OceanSlimeEntity.get(), ModEntities.IchorSlimeEntity.get(), ModEntities.OriginSlimeEntity.get(), ModEntities.TomatoSlimeEntity.get(), ModEntities.SulfurCubeEntity.get());
        this.tag(EntityTypeTags.SKELETONS).add(ModEntities.BoggedEntity.get(), ModEntities.ParchedEntity.get());
        //common
        this.tag(ModTags.EntityTypes.SLIME).add(EntityType.MAGMA_CUBE, ModEntities.OceanSlimeEntity.get(), ModEntities.IchorSlimeEntity.get(), ModEntities.OriginSlimeEntity.get(), ModEntities.TomatoSlimeEntity.get(), ModEntities.SulfurCubeEntity.get(), ModEntities.SteelSlimeBossEntity.get(), ModEntities.KnightSlimeBossEntity.get());
        this.tag(Tags.EntityTypes.BOSSES).add(ModEntities.SteelSlimeBossEntity.get(), ModEntities.KnightSlimeBossEntity.get());
        //self
        this.tag(ModTags.EntityTypes.ORE_BERRY_BUSHES_IMMUNE).add(EntityType.FOX, EntityType.BEE).addTag(ModTags.EntityTypes.SLIME);
        this.tag(ModTags.EntityTypes.PLAY_SULFUR_CUBE).add(EntityType.PLAYER).addOptional(MAID);
        this.tag(ModTags.EntityTypes.SUMMON_TOMATO_SLIME).add(ModEntities.TomatoProjectileEntity.get()).addOptional(FD_ROTTEN_TOMATO).addOptional(FC_ROTTEN_TOMATO);
    }
}
