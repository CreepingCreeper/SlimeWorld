package com.creeping_creeper.slimeworld.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class SlimeBossTeleportEvent extends EntityTeleportEvent.EnderEntity {
    /** Gets the slime that caused this teleport. If this is the same as {@link #getEntity()} then the slime is teleporting itself */
    private final Entity slime;

    public SlimeBossTeleportEvent(LivingEntity entity, double targetX, double targetY, double targetZ, Mob slime) {
        super(entity, targetX, targetY, targetZ);
        this.slime = slime;
    }

    /** Checks if the enderslime is teleporting itself */
    public boolean isTeleportingSelf() {
        return getEntity() == slime;
    }
}
