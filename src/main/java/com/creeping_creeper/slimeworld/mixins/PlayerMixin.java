package com.creeping_creeper.slimeworld.mixins;

import com.creeping_creeper.slimeworld.init.ModEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({Player.class})
public abstract class PlayerMixin extends LivingEntity{
    //Unused
    public PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

        @Unique
    public boolean canStandOnFluid(FluidState fluidState){
        return (!this.isSteppingCarefully() && this.hasEffect(ModEffects.Floating.get())) || super.canStandOnFluid(fluidState);
    }
}
