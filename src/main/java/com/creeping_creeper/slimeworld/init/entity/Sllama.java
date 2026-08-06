package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.init.ModEntities;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import slimeknights.mantle.fluid.FluidTransferHelper;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffectManager;
import slimeknights.tconstruct.library.modifiers.fluid.FluidEffects;
import slimeknights.tconstruct.tools.entity.FluidEffectProjectile;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Sllama extends Llama {
    private final FluidTank fluidTank;

    public Sllama(EntityType<? extends Llama> p_30750_, Level p_30751_) {
        super(p_30750_, p_30751_);
        fluidTank = new FluidTank(8000);
    }

    public boolean isSaddled() {
        return this.getSwag() != null;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.25D, Ingredient.of(TinkerTags.Items.CONGEALED_SLIME), false));
    }


    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (this.isTamed() && !player.isSecondaryUseActive()) {
            if (FluidTransferHelper.interactWithContainer(level(), this.getOnPos(), fluidTank, player, hand).didTransfer() || FluidTransferHelper.interactWithFilledBucket(level(), this.getOnPos(), fluidTank, player, hand, player.getDirection().getOpposite()).didTransfer()) {
                return InteractionResult.SUCCESS;
            }
        }

        return super.mobInteract(player, hand);
    }

//    @Override
//    public void openCustomInventoryScreen(Player p_218808_) {
//        if (!this.level().isClientSide && (!this.isVehicle() || this.hasPassenger(p_218808_)) && this.isTamed()) {
//            p_218808_.openHorseInventory(this, this.inventory);
//        }
//
//    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Mob mob) {
            return mob;
        } else {
            if (this.isSaddled()) {
                entity = this.getFirstPassenger();
                if (entity instanceof Player player) {
                    return player;
                }
            }

            return null;
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(Tags.Items.SLIMEBALLS) || itemStack.is(TinkerTags.Items.CONGEALED_SLIME);
    }

    @Override
    protected boolean handleEating(@NotNull Player player, ItemStack itemStack) {
        int i = 0;
        int j = 0;
        float f = 0.0F;
        boolean flag = false;
        if (itemStack.is(Tags.Items.SLIMEBALLS)) {
            i = 10;
            j = 3;
            f = 2.0F;
        } else if (itemStack.is(TinkerTags.Items.CONGEALED_SLIME)) {
            i = 90;
            j = 6;
            f = 10.0F;
            if (this.isTamed() && this.getAge() == 0 && this.canFallInLove()) {
                flag = true;
                this.setInLove(player);
            }
        }

        if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
            this.heal(f);
            flag = true;
        }

        if (this.isBaby() && i > 0) {
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0D), this.getRandomY() + 0.5D, this.getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
            if (!this.level().isClientSide) {
                this.ageUp(i);
            }

            flag = true;
        }

        if (j > 0 && (flag || !this.isTamed()) && this.getTemper() < this.getMaxTemper()) {
            flag = true;
            if (!this.level().isClientSide) {
                this.modifyTemper(j);
            }
        }

        if (flag && !this.isSilent()) {
            SoundEvent soundevent = this.getEatingSound();
            if (soundevent != null) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), this.getEatingSound(), this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }
        }

        return flag;
    }

    @Override
    public boolean canMate(@NotNull Animal p_30765_) {
        return p_30765_ != this && p_30765_ instanceof Sllama sLlama && this.canParent() && sLlama.canParent();
    }

    @Override
    @Nullable
    protected Sllama makeNewLlama() {
        return ModEntities.Sllama.get().create(this.level());
    }

    @Override
    protected void executeRidersJump(float p_251967_, @NotNull Vec3 p_275627_) {
        this.hasImpulse = true;
    }

    @Override
    public void handleStartJump(int jumpPower) {
        if (jumpPower < 25){
            return;
        }

        FluidEffectProjectile llamaSpit = getProjectile();
        if (llamaSpit == null){
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_ANGRY, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            return;
        }
        Vec3 upVector = this.getControllingPassenger().getUpVector(1.0f);
        Vector3f targetVector = this.getControllingPassenger().getViewVector(1.0f).toVector3f().rotate((new Quaternionf()).setAngleAxis(0F, upVector.x, upVector.y, upVector.z));
        llamaSpit.shoot(targetVector.x(), targetVector.y(), targetVector.z(), jumpPower / 50F, 1.0F);
        this.level().addFreshEntity(llamaSpit);

        if (!this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_SPIT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        }
    }

    @Override
    public void performRangedAttack(@Nonnull LivingEntity target, float p_30763_) {
        FluidEffectProjectile llamaSpit = getProjectile();

        if (llamaSpit == null){
            return;
        }

        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - llamaSpit.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2) * 0.2D;
        llamaSpit.shoot(d0, d1 + d3, d2, 1.5F, 10.0F);
        this.level().addFreshEntity(llamaSpit);

        if (!this.isSilent()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_SPIT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        }

        this.didSpit = true;
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

    private FluidEffectProjectile getProjectile(){
        if (fluidTank.isEmpty()) {
            return null;
        }

        FluidStack fluidStack = fluidTank.getFluid();
        Fluid fluid = fluidStack.getFluid();
        FluidEffects recipe = FluidEffectManager.INSTANCE.find(fluid);
        if (!recipe.hasEffects()) {
            return null;
        }

        int amount = Math.min(fluidStack.getAmount(), recipe.getAmount(fluid));
        FluidEffectProjectile llamaSpit = new FluidEffectProjectile(this.level(), this, new FluidStack(fluid, amount), 1);
        fluidTank.drain(amount, IFluidHandler.FluidAction.EXECUTE);

        return llamaSpit;
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
        compoundTag.put("sllamaTank", tankTag);
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("sllamaTank")) {
            fluidTank.readFromNBT(compoundTag.getCompound("sllamaTank"));
        }
    }

}