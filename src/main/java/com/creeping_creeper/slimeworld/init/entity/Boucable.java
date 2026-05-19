package com.creeping_creeper.slimeworld.init.entity;

import net.minecraft.world.phys.Vec3;

public interface Boucable {
    float bounciness = 0;
    Vec3 bounce = Vec3.ZERO;
}
