package com.creeping_creeper.slimeworld.client;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.client.model.BoggedModel;
import com.creeping_creeper.slimeworld.client.model.SulfurCubeModel;
import com.creeping_creeper.slimeworld.client.model.SulfurCubeOuterModel;
import com.creeping_creeper.slimeworld.client.particle.*;
import com.creeping_creeper.slimeworld.client.renderer.*;
import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.ModFluids;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModParticles;
import com.creeping_creeper.slimeworld.init.block.DryingRackBlockEntity;
import com.creeping_creeper.slimeworld.init.item.ModifierRuneItem;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
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
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.client.ResourceColorManager;
import slimeknights.mantle.client.render.InventoryBlockEntityRenderer;
import slimeknights.mantle.registration.object.FlowingFluidObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.ClientEventBase;
import slimeknights.tconstruct.library.client.particle.SlimeParticle;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.utils.Util;
import slimeknights.tconstruct.shared.TinkerMaterials;
import slimeknights.tconstruct.shared.block.SlimeType;
import slimeknights.tconstruct.shared.block.entity.TableBlockEntity;
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
    public static final BossSlimeFactory STEELSLIME_FACTORY = new BossSlimeFactory(SlimeWorld.getResource("textures/entity/steelslime_boss.png"));
    public static final BossSlimeFactory KNIGHTSLIME_FACTORY = new BossSlimeFactory(SlimeWorld.getResource("textures/entity/knightslime_boss.png"));

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.TomatoProjectileEntity.get(), context -> new ThrownItemRenderer<>(context, 0.75f, true));
        event.registerEntityRenderer(ModEntities.Magicbubble.get(), FloatingWindRenderer::new);
        event.registerEntityRenderer(ModEntities.OceanSlimeEntity.get(), OCEAN_SLIME_FACTORY);
        event.registerEntityRenderer(ModEntities.IchorSlimeEntity.get(), ICHOR_SLIME_FACTORY);
        event.registerEntityRenderer(ModEntities.OriginSlimeEntity.get(), ORIGIN_SLIME_FACTORY);
        event.registerEntityRenderer(ModEntities.TomatoSlimeEntity.get(), TomatoSlimeRenderer::new);
        event.registerEntityRenderer(ModEntities.SulfurCubeEntity.get(), SulfurCubeRenderer::new);
        event.registerEntityRenderer(ModEntities.SteelSlimeBossEntity.get(), STEELSLIME_FACTORY);
        event.registerEntityRenderer(ModEntities.KnightSlimeBossEntity.get(), KNIGHTSLIME_FACTORY);
        event.registerEntityRenderer(ModEntities.BoggedEntity.get(), BoggedRenderer::new);
        event.registerEntityRenderer(ModEntities.ParchedEntity.get(), ParchedRenderer::new);
        event.registerEntityRenderer(ModEntities.EarthSlimeGolemEntity.get(), EarthSlimeGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.SkySlimeGolemEntity.get(), SlimeGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.OceanSlimeGolemEntity.get(), SlimeGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.IchorSlimeGolemEntity.get(), SlimeGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.EnderSlimeGolemEntity.get(), SlimeGolemRenderer::new);
        BlockEntityRendererProvider<DryingRackBlockEntity> tableRenderer = InventoryBlockEntityRenderer::new;
        event.registerBlockEntityRenderer(ModItems.DryingRackEntity.get(), tableRenderer);
    }

    @SubscribeEvent
    static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.MagicbubbleParticle.get(), sprite -> new StaticColorParticle.Provider(sprite, 0.95F, 0.38F, 0.73F));

        event.registerSpecial(ModParticles.OceanSlimeParticle.get(), new SlimeParticle.Factory(ModItems.OceanSlimeBall));
        event.registerSpecial(ModParticles.IchorSlimeParticle.get(), new IchorParticle.Factory(SlimeType.ICHOR));
        event.registerSpecial(ModParticles.OriginSlimeParticle.get(), new SlimeParticle.Factory(ModItems.IronShard));
        event.registerSpecial(ModParticles.TomatoSlimeParticle.get(), new SlimeParticle.Factory(ModItems.TomatoPudding));
        event.registerSpecial(ModParticles.SteelSlimeParticle.get(), new SlimeParticle.Factory(TinkerMaterials.slimesteel.getIngot()));
        event.registerSpecial(ModParticles.KnightSlimeParticle.get(), new SlimeParticle.Factory(TinkerMaterials.knightslime.getIngot()));
        event.registerSpecial(ModParticles.SulfurCubeGoo.get(), new SlimeParticle.Factory(ModItems.SulfurGoo));

        event.registerSpriteSet(ModParticles.SulfurBubbles.get(), SulfurBubbleParticle.Provider::new);
        event.registerSpriteSet(ModParticles.NoxiousGas.get(), NoxiousGasParticle.Provider::new);
        event.registerSpecial(ModParticles.NoxiousGasCloud.get(), new NoxiousGasCloudParticle.Provider());
        event.registerSpriteSet(ModParticles.Geyser.get(), GeyserEruptionParticle.Provider::new);
        event.registerSpriteSet(ModParticles.GeyserBase.get(), GeyserBaseParticle.Provider::new);
        event.registerSpriteSet(ModParticles.GeyserPoof.get(), GeyserBaseParticle.Provider::new);
        event.registerSpriteSet(ModParticles.GeyserPlume.get(), GeyserPlumeParticle.Provider::new);
        event.registerSpriteSet(ModParticles.WhiteSporeParticle.get(), SporeParticle.WhiteSporeProvider::new);
        event.registerSpriteSet(ModParticles.BlackSporeParticle.get(), SporeParticle.BlackSporeProvider::new);
    }

    @SubscribeEvent
    static void registerRenderers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.Bogged, BoggedModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BoggedInnerArmor, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(INNER_ARMOR_DEFORMATION), 64, 32));
        event.registerLayerDefinition(ModModelLayers.BoggedOuterArmor, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(OUTER_ARMOR_DEFORMATION), 64, 32));
        event.registerLayerDefinition(ModModelLayers.BoggedOuterLayer, () -> LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(0.25F), 0.0F), 64, 32));
        event.registerLayerDefinition(ModModelLayers.Parched, ParchedRenderer::createSingleModelDualBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ParchedInnerArmor, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(INNER_ARMOR_DEFORMATION), 64, 32));
        event.registerLayerDefinition(ModModelLayers.ParchedOuterArmor, () -> LayerDefinition.create(HumanoidArmorModel.createBodyLayer(OUTER_ARMOR_DEFORMATION), 64, 32));
        event.registerLayerDefinition(ModModelLayers.SulferCube, SulfurCubeOuterModel::createOuterBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SulferCubeInner, SulfurCubeModel::createInnerBodyLayer);
    }
    public record SlimeFactory(ResourceLocation slime, ResourceLocation metal) implements EntityRendererProvider<Slime> {
        @Override
        public @NotNull EntityRenderer<Slime> create(@NotNull Context context) {
            return new TinkerSlimeRenderer(context, slime, metal);
        }
    }

    public record InvertedSlimeFactory(ResourceLocation slime, ResourceLocation metal) implements EntityRendererProvider<Slime> {
        @Override
        public @NotNull EntityRenderer<Slime> create(@NotNull Context context) {return new InvertedSlimeRenderer(context, slime, metal);}}

    public record BossSlimeFactory(ResourceLocation slime) implements EntityRendererProvider<Slime> {
        @Override
        public @NotNull EntityRenderer<Slime> create(@NotNull Context context) {
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

        event.register((stack, index) -> {
            ModifierId modifier = ModifierRuneItem.getModifier(stack);
            if (modifier != null) {
                return ResourceColorManager.getColor(Util.makeTranslationKey("modifier", modifier));
            }
            return -1;
        }, ModItems.MeleeRune, ModItems.RangedRune, ModItems.ArmorRune);
    }

    private static int getSlimeColorByPos(@Nullable BlockPos pos, FoliageType type) {
        if (pos == null) {
            return type.getColor();
        }
        return SlimeColorizer.getColorForPos(pos, type);
    }
}
