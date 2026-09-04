package com.creeping_creeper.slimeworld.mixins;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import slimeknights.mantle.fluid.TextureFluidType;

@Mixin({Slime.class})
public class SlimeMixin extends Mob {
    //Unused
    protected SlimeMixin(EntityType<? extends Mob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }

    @Unique
    public boolean isInWater() {
        return super.isInWater() || this.isInFluidType(((fluidType, aDouble) -> fluidType instanceof TextureFluidType));
    }
}
