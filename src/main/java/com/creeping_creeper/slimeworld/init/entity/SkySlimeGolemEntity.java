package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.library.SpecialBowAttackGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class SkySlimeGolemEntity extends BaseSlimeGolemEntity implements RangedAttackMob {
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
        AbstractArrow entityArrow = getArrow(this);
        ItemStack mainHandItem = this.getMainHandItem();
        IToolStackView tool = ToolStack.from(mainHandItem);
        if (tool.isBroken()) {
            return;
        }
        double x = target.getX() - this.getX();
        double y = target.getY(0.3333333333333333D) - entityArrow.getY();
        double z = target.getZ() - this.getZ();
        // 依据距离调整箭速
        float distance = this.distanceTo(target);
        float velocity = Mth.clamp(distance / 10f, 1.6f, 3.2f) * ConditionalStatModifierHook.getModifiedStat(tool, this, ToolStats.VELOCITY);
        //为了触发某些特性，箭必须暴击，为此需要降低基础伤害以平衡
        float multiplier = 0.67f;
        float baseArrowDamage = (float)(entityArrow.getBaseDamage() - 2 + tool.getStats().get(ToolStats.PROJECTILE_DAMAGE));
        entityArrow.setBaseDamage(ConditionalStatModifierHook.getModifiedStat(tool, this, ToolStats.PROJECTILE_DAMAGE, baseArrowDamage) * multiplier);
        entityArrow.shoot(x, y + Math.sqrt(x * x + z * z) * (double)0.2F, z, velocity, ModifierUtil.getInaccuracy(tool, this));
        ModifierNBT modifiers = tool.getModifiers();
        EntityModifierCapability.getCapability(entityArrow).addModifiers(modifiers);
        ModDataNBT arrowData = PersistentDataCapability.getOrWarn(entityArrow);
        entityArrow.setCritArrow(true);
        for (ModifierEntry entry : modifiers.getModifiers()) {
            entry.getHook(ModifierHooks.PROJECTILE_LAUNCH).onProjectileLaunch(tool, entry, this, findAmmo(this), entityArrow, entityArrow, arrowData, true);
        }
        ToolDamageUtil.damageAnimated(tool, 1, this, this.getUsedItemHand());
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(entityArrow);
    }

    private AbstractArrow getArrow(SkySlimeGolemEntity slime) {
        ItemStack bow = slime.getMainHandItem();
        IToolStackView tool = ToolStack.from(bow);
        ItemStack ammo = BowAmmoModifierHook.consumeAmmo(tool, bow, slime, null, stack -> stack.is(ItemTags.ARROWS));
        ArrowItem arrowItem = ammo.getItem() instanceof ArrowItem arrow ? arrow : (ArrowItem) Items.ARROW;
        return arrowItem.createArrow(slime.level(), ammo, slime);
    }

    private static ItemStack findAmmo(SkySlimeGolemEntity slime){
        ItemStack bow = slime.getMainHandItem();
        IToolStackView tool = ToolStack.from(bow);
        return BowAmmoModifierHook.getAmmo(tool, bow, slime, stack -> stack.is(ItemTags.ARROWS));
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

    @Override
    public void setItemSlot(@NotNull EquipmentSlot slot, @NotNull ItemStack itemStack) {
        super.setItemSlot(slot, itemStack);
        if (!this.level().isClientSide) {
            this.reassessWeaponGoal();
        }

    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.reassessWeaponGoal();
    }
}
