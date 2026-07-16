package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
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

    public ModelFile pathBlock(Block block, String path) {
        return basicBlock(block, models().cubeAll( "block/" + path + name(block), new ResourceLocation(key(block).getNamespace(), "block/" + path + name(block))));
    }
}
