package com.creeping_creeper.slimeworld.data.key;

import com.creeping_creeper.slimeworld.SlimeWorld;
import slimeknights.tconstruct.library.modifiers.ModifierId;

public class ModModifierIds {
    public static final ModifierId undercurrent = id("undercurrent");
    public static final ModifierId waving = id("waving");
    public static final ModifierId sputtering = id("sputtering");
    public static final ModifierId overwash = id("overwash");
    public static final ModifierId overload = id("overload");
    public static final ModifierId overtomato = id("overtomato");
    public static final ModifierId steadfast = id("steadfast");

    public static final ModifierId crit = id("crit");
    public static final ModifierId vanishingCurse = id("vanishing_curse");
    public static final ModifierId slimeBalance = id("slime_balance");
    private static ModifierId id(String name) {
        return new ModifierId(SlimeWorld.MODID, name);
    }
}
