package com.creeping_creeper.slimeworld.data.provider.tags;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerDamageTypes;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider lookupProvider) {
        //self
        tag(ModTags.DamageTypes.SULFUR_CUBE_IMMUNE).add(DamageTypes.ARROW, DamageTypes.CACTUS, DamageTypes.DRY_OUT, DamageTypes.FALL, DamageTypes.FALLING_ANVIL,
                DamageTypes.FALLING_BLOCK, DamageTypes.FALLING_STALACTITE, DamageTypes.FREEZE, DamageTypes.HOT_FLOOR, DamageTypes.MOB_ATTACK, DamageTypes.MOB_ATTACK_NO_AGGRO,
                DamageTypes.MOB_PROJECTILE, DamageTypes.PLAYER_ATTACK, DamageTypes.STALAGMITE, DamageTypes.STING, DamageTypes.SWEET_BERRY_BUSH,
                DamageTypes.THROWN, DamageTypes.TRIDENT, TinkerDamageTypes.KNIGHTMETAL, TinkerDamageTypes.THROWN_TOOL, TinkerDamageTypes.FISHING_HOOK)
                .addTag(DamageTypeTags.IS_EXPLOSION);
    }
}
