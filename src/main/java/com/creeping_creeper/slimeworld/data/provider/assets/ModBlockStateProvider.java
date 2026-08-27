package com.creeping_creeper.slimeworld.data.provider.assets;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.block.UnknownTpBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.mantle.registration.object.WallBuildingBlockObject;
import slimeknights.tconstruct.TConstruct;

@SuppressWarnings({"UnusedReturnValue", "SameParameterValue", "removal"})
public class ModBlockStateProvider extends BlockStateProvider {
    private final ModelFile.UncheckedModelFile GENERATED = new ModelFile.UncheckedModelFile("item/generated");

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        String geode = "geode/";
        pathBlock(ModItems.OceanGeode.getBlock(), geode);
        pathBlock(ModItems.OceanGeode.getBudding(), geode);
        basicBlock(ModItems.OceanSlime.get());
        basicBlock(ModItems.OceanCongealedSlime.get());
        basicBlock(ModItems.SlimeGravel.get());
        basicBlock(ModItems.Bronze.get());
        basicBlock(ModItems.SlimeBronze.get());

        addWallBuildingBlock(ModItems.Cinnabar, name(ModItems.Cinnabar.get()), "", blockTexture(ModItems.Cinnabar.get()));
        addWallBuildingBlock(ModItems.PolishedCinnabar, name(ModItems.PolishedCinnabar.get()), "", blockTexture(ModItems.PolishedCinnabar.get()));
        addWallBuildingBlock(ModItems.CinnabarBricks, name(ModItems.CinnabarBricks.get()), "", blockTexture(ModItems.CinnabarBricks.get()));
        basicBlock(ModItems.ChiseledCinnabar.get());
        addWallBuildingBlock(ModItems.Sulfur, name(ModItems.Sulfur.get()), "", blockTexture(ModItems.Sulfur.get()));
        addWallBuildingBlock(ModItems.PolishedSulfur, name(ModItems.PolishedSulfur.get()), "", blockTexture(ModItems.PolishedSulfur.get()));
        addWallBuildingBlock(ModItems.SulfurBricks, name(ModItems.SulfurBricks.get()), "", blockTexture(ModItems.SulfurBricks.get()));
        basicBlock(ModItems.ChiseledSulfur.get());
        basicBlock(ModItems.SulfurMud.get());

        customBlock(ModItems.PotentSulfurNausea.get(), "block/potent_sulfur", SlimeWorld.getResource("block/potent_sulfur"));
        customBlock(ModItems.PotentSulfurBlindness.get(), "block/potent_sulfur", SlimeWorld.getResource("block/potent_sulfur"));
        customBlock(ModItems.PotentSulfurWeakness.get(), "block/potent_sulfur", SlimeWorld.getResource("block/potent_sulfur"));
        customBlock(ModItems.PotentSulfurRegeneration.get(), "block/potent_sulfur", SlimeWorld.getResource("block/potent_sulfur"));
        customBlock(ModItems.PotentSulfurStrength.get(), "block/potent_sulfur", SlimeWorld.getResource("block/potent_sulfur"));

        tp(ModItems.UnknownTpSteel.get(), "steel");
        tp(ModItems.UnknownTpBronze.get(), "bronze");
        tp(ModItems.UnknownTpCinder.get(), "cinder");
        tp(ModItems.UnknownTpQueen.get(), "queen");
        tp(ModItems.UnknownTpKnight.get(), "knight");

    }

    @SuppressWarnings("deprecation")
    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    /** Gets the resource path for a block */
    private String name(Block block) {
        return key(block).getPath();
    }

    public ModelFile basicBlock(Block block, ModelFile model) {
        simpleBlock(block, model);
        simpleBlockItem(block, model);
        return model;
    }

    private ModelFile customBlock(Block block, String location, ResourceLocation texture) {
        return basicBlock(block, models().cubeAll(location, texture));
    }

    private ModelFile basicBlock(Block block) {
        return basicBlock(block, models().cubeAll(name(block), new ResourceLocation(key(block).getNamespace(),  "block/" + name(block))));
    }

    private ModelFile basicBlock(Block block, String location, ResourceLocation texture) {
        return basicBlock(block, models().cubeAll(location, texture));
    }

    private ModelFile pathBlock(Block block, String path) {
        return basicBlock(block, models().cubeAll( "block/" + path + name(block), new ResourceLocation(key(block).getNamespace(), "block/" + path + name(block))));
    }

    private void addWallBuildingBlock(WallBuildingBlockObject block, String folder, String name, ResourceLocation texture) {
        ModelFile blockModel = basicBlock(block.get(), folder + name, texture);
        slab(block.getSlab(), folder + "_slab", blockModel, texture, texture, texture);
        stairs(block.getStairs(), folder + "_stairs", texture, texture, texture);
        wall(block.getWall(), folder, texture);
    }

    private void slab(SlabBlock block, String location, ModelFile doubleModel, ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
        ModelFile slab = models().slab(location, sideTexture, bottomTexture, topTexture);
        slabBlock(block, slab, models().slabTop(location + "_top", sideTexture, bottomTexture, topTexture), doubleModel);
        simpleBlockItem(block, slab);
    }

    private void stairs(StairBlock block, String location, ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
        ModelFile stairs = models().stairs(location, sideTexture, bottomTexture, topTexture);
        stairsBlock(block, stairs, models().stairsInner(location + "_inner", sideTexture, bottomTexture, topTexture), models().stairsOuter(location + "_outer", sideTexture, bottomTexture, topTexture));
        simpleBlockItem(block, stairs);
    }

    private void wall(WallBlock block, String location, ResourceLocation texture){
        ModelFile wallInventory = models().wallInventory(location + "_wall_inventory", texture);
        wallBlock(block, location, texture);
        simpleBlockItem(block, wallInventory);
    }

    private void tp(Block block, String variant){
        String location = "block/unknown_teleporter_" + variant;
        ResourceLocation texture = SlimeWorld.getResource("block/unknown_teleporter/" + variant + "/");
        ModelFile tp = models().cubeBottomTop(location, texture.withSuffix("side"), texture.withSuffix("bottom"), texture.withSuffix("top"));
        ModelFile tpUsed = models().cubeBottomTop(location + "_used", texture.withSuffix("side_used"), texture.withSuffix("bottom_used"), texture.withSuffix("top_used"));
        getVariantBuilder(block)
                .partialState().with(UnknownTpBlock.USED, false).modelForState().modelFile(tp).addModel()
                .partialState().with(UnknownTpBlock.USED, true).modelForState().modelFile(tpUsed).addModel();
        simpleBlockItem(block, tp);
    }
}
