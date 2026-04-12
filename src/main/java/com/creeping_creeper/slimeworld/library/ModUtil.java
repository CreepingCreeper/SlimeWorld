package com.creeping_creeper.slimeworld.library;

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
}
