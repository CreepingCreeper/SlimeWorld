package com.creeping_creeper.slimeworld.init.entity.golem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Predicate;

import static slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem.KEY_DRAWBACK_AMMO;

public class SkySlimeGolemEntity extends RangeSlimeGolemEntity {
    public SkySlimeGolemEntity(EntityType<? extends RangeSlimeGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float distanceFactor) {

    }

    @Override
    public Predicate<ItemStack> canRangedAttack() {
        return itemStack -> itemStack.getItem() instanceof ModifiableBowItem && !ToolStack.from(itemStack).isBroken();
    }

    @Override
    public void startDrawing(ToolStack tool){
        this.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof ModifiableBowItem));
        GeneralInteractionModifierHook.startDrawing(tool, this, 1);
        ItemStack ammo = BowAmmoModifierHook.getAmmo(tool, tool.createStack(), this, stack -> stack.is(ItemTags.ARROWS) || stack.is(TinkerTags.Items.BALLISTA_AMMO));
        tool.getPersistentData().put(KEY_DRAWBACK_AMMO, ammo.save(new CompoundTag()));
        level().playSound(null, this.getX(), this.getY(), this.getZ(), Sounds.LONGBOW_CHARGE.getSound(), SoundSource.HOSTILE, 0.75F, 1.0F);

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
