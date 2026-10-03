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
    public static final ModifierId unyielding = id("unyielding");

    public static final ModifierId affix = id("affix");

    public static final ModifierId authoritative = id("authoritative");
    public static final ModifierId superior = id("superior");
    public static final ModifierId forceful = id("forceful");
    public static final ModifierId hard = id("hard");
    public static final ModifierId broken = id("broken");
    public static final ModifierId damaged = id("damaged");
    public static final ModifierId shoddy = id("shoddy");
    public static final ModifierId hurtful = id("hurtful");
    public static final ModifierId unpleasant = id("unpleasant");
    public static final ModifierId weak = id("weak");
    public static final ModifierId ruthless = id("ruthless");
    public static final ModifierId godly = id("godly");
    public static final ModifierId demonic = id("demonic");
    public static final ModifierId zealous = id("zealous");

    public static final ModifierId crit = id("crit");
    public static final ModifierId slimeProtect = id("slime_protect");
    public static final ModifierId vanishingCurse = id("vanishing_curse");
    public static final ModifierId slimeBalance = id("slime_balance");
    private static ModifierId id(String name) {
        return new ModifierId(SlimeWorld.MODID, name);
    }
}
