package com.creeping_creeper.slimeworld.tasks;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidAttackStrafingTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidRangedWalkToTarget;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidShootTargetTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
import slimeknights.tconstruct.library.tools.capability.EntityModifierCapability;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.helper.ToolDamageUtil;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;
import slimeknights.tconstruct.tools.TinkerTools;

import javax.annotation.Nullable;
import java.util.List;

public class ModifiableBowTask implements IRangedAttackTask {
    public static final ResourceLocation UID = new ResourceLocation(SlimeWorld.MODID, "ranged_attack");

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return TinkerTools.longbow.get().getRenderTool();
    }

    @Override
    public SoundEvent getAmbientSound(EntityMaid maid) {
        return SoundUtil.attackSound(maid, InitSounds.MAID_RANGE_ATTACK.get(), 0.5F);
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(e -> hasBow(e) && hasArrow(e), IRangedAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create((target) -> !hasBow(maid) || !hasArrow(maid) || farAway(target, maid));
        BehaviorControl<EntityMaid> moveToTargetTask = MaidRangedWalkToTarget.create(0.6f);
        BehaviorControl<EntityMaid> maidAttackStrafingTask = new MaidAttackStrafingTask();
        BehaviorControl<EntityMaid> shootTargetTask = new MaidShootTargetTask();

        return Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, moveToTargetTask),
                Pair.of(5, maidAttackStrafingTask),
                Pair.of(5, shootTargetTask)
        );
    }

    @Override
    public List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createRideBrainTasks(EntityMaid maid) {
        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(e -> hasBow(e) && hasArrow(e), IRangedAttackTask::findFirstValidAttackTarget);
        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create((target) -> !hasBow(maid) || !hasArrow(maid) || farAway(target, maid));
        BehaviorControl<EntityMaid> shootTargetTask = new MaidShootTargetTask();

        return Lists.newArrayList(
                Pair.of(5, supplementedTask),
                Pair.of(5, findTargetTask),
                Pair.of(5, shootTargetTask)
        );
    }

    @Override
    public void performRangedAttack(EntityMaid shooter, LivingEntity target, float distanceFactor) {
        AbstractArrow entityArrow = getArrow(shooter);
        if (entityArrow != null) {
            ItemStack mainHandItem = shooter.getMainHandItem();
            if (mainHandItem.getItem() instanceof ModifiableBowItem) {
                IToolStackView tool = ToolStack.from(mainHandItem);
                if (tool.isBroken()) {
                    return;
                }
                double x = target.getX() - shooter.getX();
                double y = target.getEyeY() - shooter.getEyeY();
                double z = target.getZ() - shooter.getZ();
                // 依据距离调整箭速和不准确度
                float distance = shooter.distanceTo(target);
                float velocity = Mth.clamp(distance / 10f, 1.6f, 3.2f) * ConditionalStatModifierHook.getModifiedStat(tool, shooter, ToolStats.VELOCITY);
                float inaccuracy = 1 - Mth.clamp(distance / 100f, 0, 0.9f);
                // 箭伤害也和好感度挂钩
                AttributeInstance attackDamage = shooter.getAttribute(Attributes.ATTACK_DAMAGE);
                //为了触发某些特性，箭必须暴击，为此需要降低基础伤害以平衡
                float multiplier = 0.67f;
                if (attackDamage != null) {
                    multiplier = (float) attackDamage.getBaseValue() * 3 / 4.0f;
                }
                float baseArrowDamage = (float)(entityArrow.getBaseDamage() - 2 + tool.getStats().get(ToolStats.PROJECTILE_DAMAGE));
                entityArrow.setBaseDamage(ConditionalStatModifierHook.getModifiedStat(tool, shooter, ToolStats.PROJECTILE_DAMAGE, baseArrowDamage) * multiplier);
                // 射出的箭忽略重力，从而能让女仆百发百中
                entityArrow.setNoGravity(true);
                entityArrow.shoot(x, y, z, velocity * 3.0F, inaccuracy);
                mainHandItem.hurtAndBreak(1, shooter, (maid) -> maid.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                ModifierNBT modifiers = tool.getModifiers();
                EntityModifierCapability.getCapability(entityArrow).addModifiers(modifiers);
                ModDataNBT arrowData = PersistentDataCapability.getOrWarn(entityArrow);
                entityArrow.setCritArrow(true);
                for (ModifierEntry entry : modifiers.getModifiers()) {
                    entry.getHook(ModifierHooks.PROJECTILE_LAUNCH).onProjectileLaunch(tool, entry, shooter, findAmmo(shooter), entityArrow, entityArrow, arrowData, true);
                }
                ToolDamageUtil.damageAnimated(tool, 1, shooter, shooter.getUsedItemHand());
                shooter.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (shooter.getRandom().nextFloat() * 0.4F + 0.8F));
                shooter.level().addFreshEntity(entityArrow);
            }
        }
    }

    private boolean hasArrow(EntityMaid maid) {
        return findAmmo(maid) != ItemStack.EMPTY;
    }

    private static ItemStack findAmmo(EntityMaid maid){
        ItemStack bow = maid.getMainHandItem();
        IToolStackView tool = ToolStack.from(bow);
        return BowAmmoModifierHook.getAmmo(tool, bow, maid, stack -> stack.is(ItemTags.ARROWS));
    }

    @Override
    public boolean isWeapon(EntityMaid maid, @NotNull ItemStack stack) {
        ItemStack item = maid.getMainHandItem();
        return item.getItem() instanceof ModifiableBowItem && !ToolStack.from(item).isBroken();
    }

    private boolean hasBow(EntityMaid maid) {
        ItemStack item = maid.getMainHandItem();
        return item.getItem() instanceof ModifiableBowItem && !ToolStack.from(item).isBroken();
    }

    @Nullable
    private AbstractArrow getArrow(EntityMaid maid) {
        ItemStack bow = maid.getMainHandItem();
        IToolStackView tool = ToolStack.from(bow);
        ItemStack ammo =  BowAmmoModifierHook.consumeAmmo(tool, bow, maid, null, stack -> stack.is(ItemTags.ARROWS));
        if (ammo == ItemStack.EMPTY){
            return null;
        }
        ArrowItem arrowItem = ammo.getItem() instanceof ArrowItem arrow ? arrow : (ArrowItem)Items.ARROW;
        return arrowItem.createArrow(maid.level(), ammo, maid);
    }

    private boolean farAway(LivingEntity target, EntityMaid maid) {
        return maid.distanceTo(target) > this.searchRadius(maid);
    }
}
