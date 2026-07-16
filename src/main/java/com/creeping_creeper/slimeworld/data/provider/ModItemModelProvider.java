package com.creeping_creeper.slimeworld.data.provider;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

@SuppressWarnings("UnusedReturnValue")
public class ModItemModelProvider extends ItemModelProvider {
    private final ModelFile.UncheckedModelFile GENERATED = new ModelFile.UncheckedModelFile("item/generated");

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.CopperShard.get());
        basicItem(ModItems.IronShard.get());
        basicItem(ModItems.GoldShard.get());
        basicItem(ModItems.BronzeShard.get());

        String food = "food/";
        pathItem(ModItems.OceanCake.get(), food);
        pathItem(ModItems.EarthSlimeBerry.get(), food);
        pathItem(ModItems.SkySlimeBerry.get(), food);
        pathItem(ModItems.BloodSlimeBerry.get(), food);
        pathItem(ModItems.EnderSlimeBerry.get(), food);
        pathItem(ModItems.Berriper.get(), food);
        pathItem(ModItems.BeefJerky.get(), food);
        pathItem(ModItems.PorkJerky.get(), food);
        pathItem(ModItems.MuttonJerky.get(), food);
        pathItem(ModItems.RabbitJerky.get(), food);
        pathItem(ModItems.ChickenJerky.get(), food);
        pathItem(ModItems.CodJerky.get(), food);
        pathItem(ModItems.SalmonJerky.get(), food);
        pathItem(ModItems.TropicalFishJerky.get(), food);
        pathItem(ModItems.PufferfishJerky.get(), food);
        pathItem(ModItems.RottenFleshJerky.get(), food);
        pathItem(ModItems.FriedEgg.get(), food);
        pathItem(ModItems.EarthSlimeDrop.get(), food);
        pathItem(ModItems.SkySlimeDrop.get(), food);
        pathItem(ModItems.OceanSlimeDrop.get(), food);
        pathItem(ModItems.MagmaSlimeDrop.get(), food);
        pathItem(ModItems.IchorSlimeDrop.get(), food);
        pathItem(ModItems.EnderSlimeDrop.get(), food);
    }

    @SuppressWarnings("deprecation") // no its not
    private ResourceLocation id(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    private ItemModelBuilder generated(ResourceLocation item, ResourceLocation texture) {
        return getBuilder(item.toString()).parent(GENERATED).texture("layer0", texture);
    }

    @SuppressWarnings("removal")
    private ItemModelBuilder generated(ResourceLocation item, String texture) {
        return generated(item, new ResourceLocation(item.getNamespace(), texture));
    }

    private ItemModelBuilder otherItem(ResourceLocation item, String texture) {
        return generated(item, "item/" + texture);
    }

    public ItemModelBuilder customItem(ItemLike item, String texture) {
        return otherItem(id(item), texture);
    }

    protected ItemModelBuilder pathItem(ItemLike item, String path) {
        return customItem(item, path + id(item).getPath());
    }
}
