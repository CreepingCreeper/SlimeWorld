package com.creeping_creeper.slimeworld.library;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;

import java.util.EnumSet;
import java.util.Set;

public class ModUtil {
    public static final Set<RelativeMovement> DEFAULT_TELEPORT_FLAGS = EnumSet.of(
            RelativeMovement.X,
            RelativeMovement.Y,
            RelativeMovement.Z,
            RelativeMovement.Y_ROT,
            RelativeMovement.X_ROT
    );

    public static float wrapDegrees90(final float angle) {
        float normalizedAngle = angle % 90.0F;
        if (normalizedAngle >= 45.0F) {
            normalizedAngle -= 90.0F;
        }

        if (normalizedAngle < -45.0F) {
            normalizedAngle += 90.0F;
        }

        return normalizedAngle;
    }

    public static void addAbsorption(LivingEntity living, float value){
        living.setAbsorptionAmount(living.getAbsorptionAmount() + value);
    }

    public static void addAbsorption(LivingEntity living, float value, float max){
        living.setAbsorptionAmount(Math.max(living.getAbsorptionAmount() + value, max));
    }

}
