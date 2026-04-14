package com.creeping_creeper.slimeworld.client;

import com.creeping_creeper.slimeworld.SlimeWorld;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModModelLayers {
    public static final ModelLayerLocation InvertedSlimeInner = new ModelLayerLocation(SlimeWorld.getResource("inverted_slime_inner"), "main");
    public static final ModelLayerLocation Bogged = new ModelLayerLocation(SlimeWorld.getResource("bogged"), "main");
    public static final ModelLayerLocation BoggedInnerArmor = new ModelLayerLocation(SlimeWorld.getResource("bogged"), "inner_armor");
    public static final ModelLayerLocation BoggedOuterArmor = new ModelLayerLocation(SlimeWorld.getResource("bogged"), "outer_armor");
    public static final ModelLayerLocation BoggedOuterLayer = new ModelLayerLocation(SlimeWorld.getResource("bogged"), "outer");
    public static final ModelLayerLocation Parched = new ModelLayerLocation(SlimeWorld.getResource("parched"), "main");
    public static final ModelLayerLocation ParchedInnerArmor = new ModelLayerLocation(SlimeWorld.getResource("parched"), "inner_armor");
    public static final ModelLayerLocation ParchedOuterArmor = new ModelLayerLocation(SlimeWorld.getResource("parched"), "outer_armor");
    public static final ModelLayerLocation SulferCube = new ModelLayerLocation(SlimeWorld.getResource("sulfur_cube"), "main");
    public static final ModelLayerLocation SulferCubeInner = new ModelLayerLocation(SlimeWorld.getResource("sulfur_cube"), "inner");
}
