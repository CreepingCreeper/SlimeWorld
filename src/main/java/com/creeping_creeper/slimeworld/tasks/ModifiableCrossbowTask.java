//package com.creeping_creeper.slimeworld.tasks;
//
//import com.creeping_creeper.slimeworld.SlimeWorld;
//import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
//import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidAttackStrafingTask;
//import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidRangedWalkToTarget;
//import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidShootTargetTask;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
//import com.github.tartaricacid.touhoulittlemaid.util.SoundUtil;
//import com.google.common.collect.Lists;
//import com.mojang.datafixers.util.Pair;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.tags.ItemTags;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.behavior.BehaviorControl;
//import net.minecraft.world.entity.ai.behavior.StartAttacking;
//import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
//import net.minecraft.world.entity.projectile.AbstractArrow;
//import net.minecraft.world.entity.projectile.ProjectileUtil;
//import net.minecraft.world.item.ArrowItem;
//import net.minecraft.world.item.CrossbowItem;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import slimeknights.tconstruct.library.modifiers.hook.ranged.BowAmmoModifierHook;
//import slimeknights.tconstruct.library.tools.item.ranged.ModifiableCrossbowItem;
//import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
//import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
//import slimeknights.tconstruct.library.tools.nbt.ToolStack;
//import slimeknights.tconstruct.tools.TinkerTools;
//
//import java.util.List;
//
//import static slimeknights.tconstruct.library.tools.item.ranged.ModifiableCrossbowItem.KEY_CROSSBOW_AMMO;
//
//public class ModifiableCrossbowTask implements IRangedAttackTask {
//    public static final ResourceLocation UID = new ResourceLocation(SlimeWorld.MODID, "crossbow_attack");
//
//    @Override
//    public @NotNull ResourceLocation getUid() {
//        return UID;
//    }
//
//    @Override
//    public @NotNull ItemStack getIcon() {
//        return TinkerTools.crossbow.get().getRenderTool();
//    }
//
//    @Override
//    public SoundEvent getAmbientSound(@NotNull EntityMaid maid) {
//        return SoundUtil.attackSound(maid, InitSounds.MAID_RANGE_ATTACK.get(), 0.5F);
//    }
//
//    @Override
//    public @NotNull List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(@NotNull EntityMaid maid) {
//        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(e -> hasBow(e) && hasArrow(e), IRangedAttackTask::findFirstValidAttackTarget);
//        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create((target) -> !hasBow(maid) || !hasArrow(maid) || farAway(target, maid));
//        BehaviorControl<EntityMaid> moveToTargetTask = MaidRangedWalkToTarget.create(0.6f);
//        BehaviorControl<EntityMaid> maidAttackStrafingTask = new MaidAttackStrafingTask();
//        BehaviorControl<EntityMaid> shootTargetTask = new MaidShootTargetTask();
//
//        return Lists.newArrayList(
//                Pair.of(5, supplementedTask),
//                Pair.of(5, findTargetTask),
//                Pair.of(5, moveToTargetTask),
//                Pair.of(5, maidAttackStrafingTask),
//                Pair.of(5, shootTargetTask)
//        );
//    }
//
//    @Override
//    public @NotNull List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createRideBrainTasks(@NotNull EntityMaid maid) {
//        BehaviorControl<EntityMaid> supplementedTask = StartAttacking.create(e -> hasBow(e) && hasArrow(e), IRangedAttackTask::findFirstValidAttackTarget);
//        BehaviorControl<EntityMaid> findTargetTask = StopAttackingIfTargetInvalid.create((target) -> !hasBow(maid) || !hasArrow(maid) || farAway(target, maid));
//        BehaviorControl<EntityMaid> shootTargetTask = new MaidShootTargetTask();
//
//        return Lists.newArrayList(
//                Pair.of(5, supplementedTask),
//                Pair.of(5, findTargetTask),
//                Pair.of(5, shootTargetTask)
//        );
//    }
//
//    @Override
//    public void performRangedAttack(@NotNull EntityMaid entityMaid, @NotNull LivingEntity livingEntity, float v) {
//        InteractionHand interactionhand = ProjectileUtil.getWeaponHoldingHand(entityMaid, (item) -> item instanceof CrossbowItem);
//        ItemStack itemstack = entityMaid.getItemInHand(interactionhand);
//        if (entityMaid.isHolding((is) -> is.getItem() instanceof ModifiableCrossbowItem)) {
//            IToolStackView tool = ToolStack.from(itemstack);
//            ModDataNBT persistentData = tool.getPersistentData();
//            CompoundTag heldAmmo = persistentData.getCompound(KEY_CROSSBOW_AMMO);
//            ModifiableCrossbowItem.fireCrossbow(tool, entityMaid, false, InteractionHand.MAIN_HAND, heldAmmo);
//        }
//        entityMaid.onCrossbowAttackPerformed();
//    }
//
//    private boolean hasArrow(EntityMaid maid) {
//        return findAmmo(maid) != ItemStack.EMPTY;
//    }
//
//    private static ItemStack findAmmo(EntityMaid maid){
//        ItemStack bow = maid.getMainHandItem();
//        IToolStackView tool = ToolStack.from(bow);
//        return BowAmmoModifierHook.getAmmo(tool, bow, maid, stack -> stack.is(ItemTags.ARROWS));
//    }
//
//    @Override
//    public boolean isWeapon(EntityMaid maid, @NotNull ItemStack stack) {
//        ItemStack item = maid.getMainHandItem();
//        return item.getItem() instanceof ModifiableCrossbowItem && !ToolStack.from(item).isBroken();
//    }
//
//    private boolean hasBow(EntityMaid maid) {
//        ItemStack item = maid.getMainHandItem();
//        return item.getItem() instanceof ModifiableCrossbowItem && !ToolStack.from(item).isBroken();
//    }
//
//    @Nullable
//    private AbstractArrow getArrow(EntityMaid maid) {
//        ItemStack bow = maid.getMainHandItem();
//        IToolStackView tool = ToolStack.from(bow);
//        ItemStack ammo =  BowAmmoModifierHook.consumeAmmo(tool, bow, maid, null, stack -> stack.is(ItemTags.ARROWS));
//        if (ammo == ItemStack.EMPTY){
//            return null;
//        }
//        ArrowItem arrowItem = ammo.getItem() instanceof ArrowItem arrow ? arrow : (ArrowItem) Items.ARROW;
//        return arrowItem.createArrow(maid.level(), ammo, maid);
//    }
//
//    private boolean farAway(LivingEntity target, EntityMaid maid) {
//        return maid.distanceTo(target) > this.searchRadius(maid);
//    }
//}
