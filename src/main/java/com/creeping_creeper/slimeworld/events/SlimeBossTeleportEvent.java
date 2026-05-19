package com.creeping_creeper.slimeworld.events;

import com.creeping_creeper.slimeworld.init.entity.KnightSlimeBossEntity;
import lombok.Getter;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class SlimeBossTeleportEvent extends EntityTeleportEvent.EnderEntity {
    /** Gets the slime that caused this teleport. If this is the same as {@link #getEntity()} then the slime is teleporting itself */
    @Getter
    private final KnightSlimeBossEntity slime;

    public SlimeBossTeleportEvent(LivingEntity entity, double targetX, double targetY, double targetZ, KnightSlimeBossEntity slime) {
        super(entity, targetX, targetY, targetZ);
        this.slime = slime;
    }

    /** Checks if the enderslime is teleporting itself */
    public boolean isTeleportingSelf() {
        return getEntity() == slime;
    }
}
