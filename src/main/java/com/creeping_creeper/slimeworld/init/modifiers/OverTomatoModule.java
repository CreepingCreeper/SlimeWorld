package com.creeping_creeper.slimeworld.init.modifiers;

import com.creeping_creeper.slimeworld.init.entity.TomatoProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.json.LevelingValue;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.modules.capacity.OverslimeModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public record OverTomatoModule(LevelingValue chance)implements ModifierModule, MeleeHitModifierHook {
    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<OverloadModule>defaultHooks(ModifierHooks.INVENTORY_TICK);

    public static final RecordLoadable<OverTomatoModule> LOADER = RecordLoadable.create(
            LevelingValue.LOADABLE.requiredField("chance", OverTomatoModule::chance),
           OverTomatoModule::new);

    @Override
    public @NotNull RecordLoadable<? extends ModifierModule> getLoader() {
        return LOADER;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    @Override
    public void afterMeleeHit(@NotNull IToolStackView tool, @NotNull ModifierEntry modifier, @NotNull ToolAttackContext context, float damageDealt) {
     if (OverslimeModule.getCapacity(tool) > 0 && Modifier.RANDOM.nextFloat() < this.chance.compute(modifier)){
         LivingEntity living = context.getAttacker();
         Level level = context.getLevel();
         int modifierLevel = modifier.getLevel();
         int shots = 1 + 2 * modifierLevel;
         float startAngle = ModifiableLauncherItem.getAngleStart(shots);
         for (int shotIndex = 0; shotIndex < shots; shotIndex++) {
             TomatoProjectile spit = new TomatoProjectile(level, living, modifierLevel);
             Vec3 upVector = living.getUpVector(1.0f);
             float angle = startAngle + (10 * shotIndex);
             Vector3f targetVector = living.getViewVector(1.0f).toVector3f().rotate((new Quaternionf()).setAngleAxis(angle * Math.PI / 180F, upVector.x, upVector.y, upVector.z));
             spit.shoot(targetVector.x(), targetVector.y(), targetVector.z(), 3, 0.1F);
             level.addFreshEntity(spit);
         }
     }
    }
}
