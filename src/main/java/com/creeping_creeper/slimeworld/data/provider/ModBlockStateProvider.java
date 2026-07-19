package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModItems;
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
        basicBlock(ModItems.OceanSlime.get());
        customBlock(ModItems.Bronze.get(), "block/bronze_block", TConstruct.getResource("block/storage/fallback_tconstruct_bronze"));
        String geode = "geode/";
        pathBlock(ModItems.OceanGeode.getBlock(), geode);
        pathBlock(ModItems.OceanGeode.getBudding(), geode);

        addWallBuildingBlock(ModItems.Sulfur, name(ModItems.Sulfur.get()), "", blockTexture(ModItems.Sulfur.get()));
        addWallBuildingBlock(ModItems.PolishedSulfur, name(ModItems.PolishedSulfur.get()), "", blockTexture(ModItems.PolishedSulfur.get()));
        addWallBuildingBlock(ModItems.SulfurBricks, name(ModItems.SulfurBricks.get()), "", blockTexture(ModItems.SulfurBricks.get()));
        basicBlock(ModItems.SulfurMud.get());
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

    public ModelFile customBlock(Block block, String location, ResourceLocation texture) {
        return basicBlock(block, models().cubeAll(location, texture));
    }

    public ModelFile basicBlock(Block block) {
        return basicBlock(block, models().cubeAll(name(block), new ResourceLocation(key(block).getNamespace(),  "block/" + name(block))));
    }

    public ModelFile basicBlock(Block block, String location, ResourceLocation texture) {
        return basicBlock(block, models().cubeAll(location, texture));
    }

    public ModelFile pathBlock(Block block, String path) {
        return basicBlock(block, models().cubeAll( "block/" + path + name(block), new ResourceLocation(key(block).getNamespace(), "block/" + path + name(block))));
    }

    protected void addWallBuildingBlock(WallBuildingBlockObject block, String folder, String name, ResourceLocation texture) {
        ModelFile blockModel = basicBlock(block.get(), folder + name, texture);
        slab(block.getSlab(), folder + "_slab", blockModel, texture, texture, texture);
        stairs(block.getStairs(), folder + "_stairs", texture, texture, texture);
        wall(block.getWall(), folder, texture);
    }

    public void slab(SlabBlock block, String location, ModelFile doubleModel, ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
        ModelFile slab = models().slab(location, sideTexture, bottomTexture, topTexture);
        slabBlock(block, slab, models().slabTop(location + "_top", sideTexture, bottomTexture, topTexture), doubleModel);
        simpleBlockItem(block, slab);
    }

    public void stairs(StairBlock block, String location, ResourceLocation sideTexture, ResourceLocation bottomTexture, ResourceLocation topTexture) {
        ModelFile stairs = models().stairs(location, sideTexture, bottomTexture, topTexture);
        stairsBlock(block, stairs, models().stairsInner(location + "_inner", sideTexture, bottomTexture, topTexture), models().stairsOuter(location + "_outer", sideTexture, bottomTexture, topTexture));
        simpleBlockItem(block, stairs);
    }

    public void wall(WallBlock block, String location, ResourceLocation texture){
        ModelFile wallInventory = models().wallInventory(location + "_wall_inventory", texture);
        wallBlock(block, location, texture);
        simpleBlockItem(block, wallInventory);
    }
}
