package com.creeping_creeper.slimeworld.library;

import net.minecraft.world.entity.LivingEntity;

public class ModUtil {
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
