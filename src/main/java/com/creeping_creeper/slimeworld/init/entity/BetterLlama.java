package com.creeping_creeper.slimeworld.init.entity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.fluid.FluidTransferHelper;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffectManager;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffects;
import slimeknights.tconstruct.tools.entity.FluidEffectProjectile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BetterLlama extends Llama {
    private final FluidTank fluidTank;

    public BetterLlama(EntityType<? extends Llama> p_30750_, Level p_30751_) {
        super(p_30750_, p_30751_);
        fluidTank = new FluidTank(8000);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        if (!player.isSecondaryUseActive() && !FluidTransferHelper.interactWithContainer(level(), this.getOnPos(), fluidTank, player, hand).didTransfer()) {
            FluidTransferHelper.interactWithFilledBucket(level(), this.getOnPos(), fluidTank, player, hand, player.getDirection().getOpposite());
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void performRangedAttack(@Nonnull LivingEntity target, float p_30763_) {
        if (fluidTank.isEmpty()) return;

        FluidStack fluidStack = fluidTank.getFluid();
        Fluid fluid = fluidStack.getFluid();
        FluidEffects recipe = FluidEffectManager.INSTANCE.find(fluid);
        if (!recipe.hasEffects()) return;

        int amount = Math.min(fluidStack.getAmount(), recipe.getAmount(fluid));
        FluidEffectProjectile llamaSpit = new FluidEffectProjectile(this.level(), this, new FluidStack(fluid, amount), 1);

        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - llamaSpit.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2) * 0.2D;
        llamaSpit.shoot(d0, d1 + d3, d2, getStrength() * 1F, 10.0F);

        if (!this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_SPIT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        }

        this.level().addFreshEntity(llamaSpit);
        this.didSpit = true;
        fluidTank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor p_30774_, @Nonnull DifficultyInstance p_30775_, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData p_30777_, @Nullable CompoundTag p_30778_) {
        SpawnGroupData spawnGroupData = super.finalizeSpawn(p_30774_, p_30775_, spawnType, p_30777_, p_30778_);
        int cap = 2000 * getStrength();
        fluidTank.setCapacity(cap);
        if (spawnType != MobSpawnType.BREEDING) {
            Fluid fluid = switch (this.getVariant()) {
                case CREAMY -> TinkerFluids.earthSlime.get();
                case WHITE -> TinkerFluids.skySlime.get();
                case BROWN -> TinkerFluids.ichor.get();
                case GRAY -> TinkerFluids.enderSlime.get();
            };
            fluidTank.setFluid(new FluidStack(fluid, cap));
        }
        return spawnGroupData;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == ForgeCapabilities.FLUID_HANDLER) {
            return LazyOptional.of(() -> fluidTank).cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        CompoundTag tankTag = new CompoundTag();
        fluidTank.writeToNBT(tankTag);
        compoundTag.put("LlamaTank", tankTag);
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("LlamaTank")) {
            fluidTank.readFromNBT(compoundTag.getCompound("LlamaTank"));
        }
    }

}