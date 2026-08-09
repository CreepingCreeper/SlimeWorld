package com.creeping_creeper.slimeworld.data.key;

import com.creeping_creeper.slimeworld.SlimeWorld;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public class ModMaterialIds {
    public static final MaterialId kelp = id("kelp");
    public static final MaterialId oceanslime = id("oceanslime");
    public static final MaterialId slimeBronze = id("slime_bronze");

    private static MaterialId id(String name) {
        return new MaterialId(SlimeWorld.MODID, name);
    }
}
