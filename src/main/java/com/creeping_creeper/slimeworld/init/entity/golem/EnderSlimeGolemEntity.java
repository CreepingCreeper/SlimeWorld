package com.creeping_creeper.slimeworld.init.entity.golem;

import com.creeping_creeper.slimeworld.events.MobTeleportEvent;
import com.creeping_creeper.slimeworld.init.entity.SpecialBowAttackGoal;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.tools.item.ModifiableShurikenItem;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.library.utils.TeleportHelper;
import slimeknights.tconstruct.tools.entity.ThrownShuriken;

import java.util.function.Predicate;

public class EnderSlimeGolemEntity extends RangeSlimeGolemEntity {
    private final TeleportHelper.ITeleportEventFactory teleportPredicate = (entity, x, y, z) -> new MobTeleportEvent(entity, x, y, z, this);

    public EnderSlimeGolemEntity(EntityType<? extends RangeSlimeGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            if (this.canRangedAttack().test(this.getOffhandItem())) {
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
        ItemStack stack = this.getItemInHand(InteractionHand.OFF_HAND);
        level().playSound(null, this.getX(), this.getY(), this.getZ(), Sounds.SHURIKEN_THROW.getSound(), SoundSource.HOSTILE, 0.5F, 0.4F / (level().getRandom().nextFloat() * 0.4F + 0.8F));
        ThrownShuriken shuriken = new ThrownShuriken(level(), this);
        IToolStackView tool = shuriken.onCreate(stack, this);
        float velocity = ConditionalStatModifierHook.getModifiedStat(tool, this, ToolStats.VELOCITY);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - shuriken.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        shuriken.shoot(d0, d1 + d3 * 0.2D, d2, velocity, 1F);
        level().addFreshEntity(shuriken);
        TeleportHelper.randomNearbyTeleport(this, teleportPredicate);
        this.reassessWeaponGoal();
    }

    @Override
    public Predicate<ItemStack> canRangedAttack() {
        return itemStack -> itemStack.getItem() instanceof ModifiableShurikenItem;
    }

    @Override
    public void startDrawing(ToolStack tool){
    }

    @Override
    public void shoot(SpecialBowAttackGoal<? extends RangeSlimeGolemEntity> goal, boolean flag, ToolStack toolStack, LivingEntity target){
        if (( flag || goal.canSee()) && goal.canDraw()) {
            this.performRangedAttack(target, 1F);
            goal.resetAttackTime();
        }
    }

    @Override
    public @NotNull ItemStack equipItemIfPossible(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ModifiableShurikenItem && this.getOffhandItem().isEmpty()){
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
