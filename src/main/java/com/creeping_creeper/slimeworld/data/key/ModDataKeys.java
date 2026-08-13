package com.creeping_creeper.slimeworld.data.key;

import com.creeping_creeper.slimeworld.SlimeWorld;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;

import static slimeknights.tconstruct.library.tools.capability.TinkerDataKeys.INTEGER_REGISTRY;

public interface ModDataKeys {
    static void init() {}

    private static TinkerDataCapability.TinkerDataKey<Integer> intKey(String name) {
        return INTEGER_REGISTRY.register(SlimeWorld.createKey(name));
    }
}
