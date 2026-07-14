package com.creeping_creeper.slimeworld.init.entity.golem;

import com.creeping_creeper.slimeworld.init.entity.SpecialRangedMob;
import com.creeping_creeper.slimeworld.library.SpecialBowAttackGoal;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.data.ModifierIds;

import java.util.function.Predicate;

public class OceanSlimeGolemEntity extends BaseSlimeGolemEntity implements SpecialRangedMob {
    private final SpecialBowAttackGoal<OceanSlimeGolemEntity> bowGoal = new SpecialBowAttackGoal<>(this, 1.0D, 20, 15.0F);

    public OceanSlimeGolemEntity(EntityType<? extends BaseSlimeGolemEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void reassessWeaponGoal() {
        if (!this.level().isClientSide) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.bowGoal);
            if (this.getMainHandItem().getItem() instanceof ModifiableItem) {
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
        return itemStack -> itemStack.getItem() instanceof ModifiableItem && ToolStack.from(itemStack).getModifierLevel(ModifierIds.spitting) > 0 && !ToolStack.from(itemStack).isBroken();
    }

    @Override
    public Predicate<Item> canStartRangedAttack() {
        return item -> item instanceof ModifiableItem;
    }

    @Override
    public void startDrawing(ToolStack tool){
        GeneralInteractionModifierHook.startUsingWithDrawtime(tool, ModifierIds.spitting, this, InteractionHand.MAIN_HAND, 1.5F);
    }

}
