package com.creeping_creeper.slimeworld.init.entity.golem;

import com.creeping_creeper.slimeworld.init.entity.SpecialRangedMob;
import com.creeping_creeper.slimeworld.library.SpecialBowAttackGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Predicate;

import static slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem.KEY_DRAWBACK_AMMO;

public class SkySlimeGolemEntity extends BaseSlimeGolemEntity implements SpecialRangedMob {
    private final SpecialBowAttackGoal<SkySlimeGolemEntity> bowGoal = new SpecialBowAttackGoal<>(this, 1.0D, 20, 15.0F);

    public SkySlimeGolemEntity(EntityType<? extends BaseSlimeGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            if (this.getMainHandItem().getItem() instanceof ModifiableBowItem) {
                int i = 20;
                if (this.level().getDifficulty() != Difficulty.HARD) {
                    i = 40;
                }

                this.bowGoal.setMinAttackInterval(i);
                this.goalSelector.addGoal(4, this.bowGoal);
            } else {
                this.goalSelector.addGoal(4, this.meleeGoal);
            }

        }
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float distanceFactor) {

    }

    @Override
    public Predicate<ItemStack> canRangedAttack() {
        return itemStack -> itemStack.getItem() instanceof ModifiableBowItem && !ToolStack.from(itemStack).isBroken();
    }

    @Override
    public Predicate<Item> canStartRangedAttack() {
        return item -> item instanceof ModifiableBowItem;
    }

    @Override
    public void startDrawing(ToolStack tool){
        this.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this, this.canStartRangedAttack()));
        GeneralInteractionModifierHook.startDrawing(tool, this, 1);
        ItemStack ammo = BowAmmoModifierHook.getAmmo(tool, tool.createStack(), this, ModifiableBowItem.ARROW_ONLY);
        tool.getPersistentData().put(KEY_DRAWBACK_AMMO, ammo.save(new CompoundTag()));
    }

    @Override
    public @NotNull ItemStack equipItemIfPossible(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ArrowItem && this.getOffhandItem().isEmpty()){
            this.setItemSlot(EquipmentSlot.OFFHAND, itemStack);
            return itemStack;
        }
        return super.equipItemIfPossible(itemStack);
    }

    @Override
    public boolean canFireProjectileWeapon(@NotNull ProjectileWeaponItem item) {
        return item instanceof ModifiableBowItem;
    }

}
