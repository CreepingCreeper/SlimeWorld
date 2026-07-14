package com.creeping_creeper.slimeworld.init.entity.golem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableCrossbowItem;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Predicate;

import static slimeknights.tconstruct.library.tools.item.ranged.ModifiableCrossbowItem.KEY_CROSSBOW_AMMO;
import static slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem.KEY_DRAWBACK_AMMO;

public class IchorSlimeGolemEntity extends RangeSlimeGolemEntity {
     public IchorSlimeGolemEntity(EntityType<? extends RangeSlimeGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float distanceFactor) {
        ItemStack bow = this.getItemInHand(InteractionHand.MAIN_HAND);
        ToolStack tool = ToolStack.from(bow);
        ModDataNBT persistentData = tool.getPersistentData();
        CompoundTag heldAmmo = persistentData.getCompound(KEY_CROSSBOW_AMMO);
        ModifiableCrossbowItem.fireCrossbow(tool, this, false, InteractionHand.MAIN_HAND, heldAmmo);
    }

    @Override
    public Predicate<ItemStack> canRangedAttack() {
        return itemStack -> itemStack.getItem() instanceof ModifiableCrossbowItem && !ToolStack.from(itemStack).isBroken();
    }

    @Override
    public void startDrawing(ToolStack tool){
        this.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof ModifiableCrossbowItem));
        GeneralInteractionModifierHook.startDrawing(tool, this, 1);
        ItemStack ammo = BowAmmoModifierHook.getAmmo(tool, tool.createStack(), this, ModifiableCrossbowItem.ARROW_OR_FIREWORK);
        tool.getPersistentData().put(KEY_DRAWBACK_AMMO, ammo.save(new CompoundTag()));
        if (!level().isClientSide) {
            level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.CROSSBOW_QUICK_CHARGE_1, SoundSource.PLAYERS, 0.75F, 1.0F);
        }
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
        return item instanceof ModifiableCrossbowItem;
    }

}
