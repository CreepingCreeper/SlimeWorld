package com.creeping_creeper.slimeworld.client;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.client.renderer.*;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.ModFluids;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.ClientEventBase;
import slimeknights.tconstruct.library.client.particle.SlimeParticle;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.world.block.FoliageType;
import slimeknights.tconstruct.world.client.SlimeColorizer;
import slimeknights.tconstruct.world.client.TinkerSlimeRenderer;

import javax.annotation.Nullable;

import static net.minecraft.client.model.geom.LayerDefinitions.INNER_ARMOR_DEFORMATION;
import static net.minecraft.client.model.geom.LayerDefinitions.OUTER_ARMOR_DEFORMATION;

@Mod.EventBusSubscriber(modid = SlimeWorld.MODID, value= Dist.CLIENT, bus= Mod.EventBusSubscriber.Bus.MOD)
public class ClientEvent extends ClientEventBase {
    public static final SlimeFactory OCEAN_SLIME_FACTORY = new SlimeFactory(SlimeWorld.getResource("textures/entity/ocean_slime.png"), SlimeWorld.getResource("textures/entity/bronze_slime.png"));
    public static final InvertedSlimeFactory ICHOR_SLIME_FACTORY = new InvertedSlimeFactory(SlimeWorld.getResource("textures/entity/ichor_slime.png"), SlimeWorld.getResource("textures/entity/cobalt_slime.png"));
    public static final SlimeFactory ORIGIN_SLIME_FACTORY = new SlimeFactory(TConstruct.getResource("textures/entity/slime.png"), TConstruct.getResource("textures/entity/slime.png"));
    public static final BossSlimeFactory STEEL_SLIME_FACTORY = new BossSlimeFactory(SlimeWorld.getResource("textures/entity/steel_slime_boss.png"));

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.magicbubble.get(), FloatingWindRenderer::new);
        event.registerEntityRenderer(ModEntities.oceanSlimeEntity.get(), OCEAN_SLIME_FACTORY);
        event.registerEntityRenderer(ModEntities.ichorSlimeEntity.get(), ICHOR_SLIME_FACTORY);
        event.registerEntityRenderer(ModEntities.originSlimeEntity.get(), ORIGIN_SLIME_FACTORY);
        event.registerEntityRenderer(ModEntities.steelSlimeBossEntity.get(), STEEL_SLIME_FACTORY);
        event.registerEntityRenderer(ModEntities.boggedEntity.get(), BoggedRenderer::new);
        event.registerEntityRenderer(ModEntities.parchedEntity.get(), ParchedRenderer::new);
    }

    @SubscribeEvent
    static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModEntities.magicbubbleParticle.get(), sprite -> new StaticColorParticle.Provider(sprite, 0.95F, 0.38F, 0.73F));
        event.registerSpecial(ModEntities.oceanSlimeParticle.get(), new SlimeParticle.Factory(ModItems.OceanSlimeBall));
        event.registerSpecial(ModEntities.ichorSlimeParticle.get(), new IchorParticle.Factory(SlimeType.ICHOR));
        event.registerSpecial(ModEntities.originSlimeParticle.get(), new SlimeParticle.Factory(ModItems.IronShard));
        event.registerSpecial(ModEntities.steelSlimeParticle.get(), new SlimeParticle.Factory(TinkerMaterials.slimesteel.getIngot()));
        event.registerSpriteSet(ModEntities.whiteSporeParticle.get(), SporeParticle.WhiteSporeProvider::new);
        event.registerSpriteSet(ModEntities.blackSporeParticle.get(), SporeParticle.BlackSporeProvider::new);
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModLayers.InvertedSlimeInner, InvertedSlimeRenderer::createInnerBodyLayer);
        event.registerLayerDefinition(ModLayers.Bogged, BoggedRenderer::createBodyLayer);
        event.registerLayerDefinition(ModLayers.BoggedInnerArmor, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(INNER_ARMOR_DEFORMATION), 64, 32));
        event.registerLayerDefinition(ModLayers.BoggedOuterArmor, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(OUTER_ARMOR_DEFORMATION), 64, 32));
        event.registerLayerDefinition(ModLayers.BoggedOuterLayer, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.0F), 64, 32));
        event.registerLayerDefinition(ModLayers.Parched, ParchedRenderer::createSingleModelDualBodyLayer);
        event.registerLayerDefinition(ModLayers.ParchedInnerArmor, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(INNER_ARMOR_DEFORMATION), 64, 32));
        event.registerLayerDefinition(ModLayers.ParchedOuterArmor, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(OUTER_ARMOR_DEFORMATION), 64, 32));

    }
    public record SlimeFactory(ResourceLocation slime, ResourceLocation metal) implements EntityRendererProvider<Slime> {
        @Override
        public EntityRenderer<Slime> create(Context context) {
            return new TinkerSlimeRenderer(context, slime, metal);
        }
    }

    public record InvertedSlimeFactory(ResourceLocation slime, ResourceLocation metal) implements EntityRendererProvider<Slime> {
        @Override
        public EntityRenderer<Slime> create(Context context) {
            return new InvertedSlimeRenderer(context, slime, metal);
        }
    }

    public record BossSlimeFactory(ResourceLocation slime) implements EntityRendererProvider<Slime> {
        @Override
        public EntityRenderer<Slime> create(Context context) {
            return new BossSlimeRenderer(context, slime);
        }
    }

    @SubscribeEvent
    static void clientSetup(final FMLClientSetupEvent event) {
        setTranslucent(ModFluids.OceanSlime);
    }

    private static void setTranslucent(FlowingFluidObject<?> fluid) {
        ItemBlockRenderTypes.setRenderLayer(fluid.getStill(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(fluid.getFlowing(), RenderType.translucent());
    }

    @SubscribeEvent
    static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        event.register(
                (state, reader, pos, index) -> getSlimeColorByPos(pos, FoliageType.EARTH), ModItems.CopperBerryBush.get()
        );
        event.register(
                (state, reader, pos, index) -> getSlimeColorByPos(pos, FoliageType.SKY), ModItems.IronBerryBush.get()
        );
        event.register(
                (state, reader, pos, index) -> getSlimeColorByPos(pos, FoliageType.BLOOD), ModItems.GoldBerryBush.get()
        );
        event.register(
                (state, reader, pos, index) -> getSlimeColorByPos(pos, FoliageType.ICHOR), ModItems.CobaltBerryBush.get(),
                ModItems.IchorFern.get(), ModItems.IchorTallGrass.get(),
                ModItems.IchorEarthSlimeNylium.get(), ModItems.IchorSkySlimeNylium.get(), ModItems.IchorIchorSlimeNylium.get(), ModItems.IchorEnderSlimeNylium.get(), ModItems.IchorVanillaSlimeNylium.get()
        );
    }

    @SubscribeEvent
    static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        BlockColors blockColors = event.getBlockColors();
        ItemColors itemColors = event.getItemColors();
        registerBlockItemColorAlias(blockColors, itemColors, ModItems.IchorFern);
        registerBlockItemColorAlias(blockColors, itemColors, ModItems.IchorTallGrass);
        registerBlockItemColorAlias(blockColors, itemColors, ModItems.IchorEarthSlimeNylium);
        registerBlockItemColorAlias(blockColors, itemColors, ModItems.IchorSkySlimeNylium);
        registerBlockItemColorAlias(blockColors, itemColors, ModItems.IchorIchorSlimeNylium);
        registerBlockItemColorAlias(blockColors, itemColors, ModItems.IchorEnderSlimeNylium);
        registerBlockItemColorAlias(blockColors, itemColors, ModItems.IchorVanillaSlimeNylium);
        registerBlockItemColorAlias(blockColors, itemColors, ModItems.CopperBerryBush);
        registerBlockItemColorAlias(blockColors, itemColors, ModItems.IronBerryBush);
        registerBlockItemColorAlias(blockColors, itemColors, ModItems.GoldBerryBush);
        registerBlockItemColorAlias(blockColors, itemColors, ModItems.CobaltBerryBush);
    }

    private static int getSlimeColorByPos(@Nullable BlockPos pos, FoliageType type) {
        if (pos == null) {
            return type.getColor();
        }
        return SlimeColorizer.getColorForPos(pos, type);
    }
}
