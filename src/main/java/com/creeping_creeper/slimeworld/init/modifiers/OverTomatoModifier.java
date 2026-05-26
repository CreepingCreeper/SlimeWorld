package com.creeping_creeper.slimeworld.init.modifiers;

import com.creeping_creeper.slimeworld.init.entity.TomatoProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.capacity.OverslimeModule;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.tools.entity.FluidEffectProjectile;

public class OverTomatoModifier extends Modifier implements MeleeHitModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, @NotNull ToolAttackContext context, float damageDealt) {
     if (OverslimeModule.getCapacity(tool) > 0 && RANDOM.nextFloat() > modifier.getLevel() * 0.15F){
         LivingEntity living = context.getAttacker();
         Level level = context.getLevel();
         int shots = 1 + 2 * modifier.getLevel();
         float startAngle = ModifiableLauncherItem.getAngleStart(shots);
         for (int shotIndex = 0; shotIndex < modifier.getLevel()*2+1; shotIndex++) {
             TomatoProjectile spit = new TomatoProjectile(level, living, modifier.getLevel());
             Vec3 upVector = living.getUpVector(1.0f);
             float angle = startAngle + (10 * shotIndex);
             Vector3f targetVector = living.getViewVector(1.0f).toVector3f().rotate((new Quaternionf()).setAngleAxis(angle * Math.PI / 180F, upVector.x, upVector.y, upVector.z));
             spit.shoot(targetVector.x(), targetVector.y(), targetVector.z(), 3, 0.1F);
             level.addFreshEntity(spit);
         }
     }
    }
}
