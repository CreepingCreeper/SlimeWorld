package com.creeping_creeper.slimeworld.data;

import com.creeping_creeper.slimeworld.SlimeWorld;
import slimeknights.tconstruct.library.modifiers.ModifierId;

public class ModModifierIds {
    public static final ModifierId vanishingCurse = id("vanishing_curse");
    public static final ModifierId slimeBalance = id("slime_balance");
    private static ModifierId id(String name) {
        return new ModifierId(SlimeWorld.MODID, name);
    }
}
