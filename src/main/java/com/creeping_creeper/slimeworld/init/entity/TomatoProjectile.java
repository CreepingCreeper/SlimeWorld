package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.entity.ProjectileWithKnockback;
import slimeknights.tconstruct.library.modifiers.entity.ProjectileWithPower;

public class TomatoProjectile extends Projectile implements ProjectileWithKnockback, ProjectileWithPower, ItemSupplier {
  /** Projectile power determining how much fluid is used at most */
  private float power = 1;
  /** Amount of knockback for the projectile to cause, scaled like arrow knockback */
  private float knockback = 1;
  /** Position of the cannon that fired this projectile */

  public TomatoProjectile(EntityType<? extends TomatoProjectile> type, Level level) {
    super(type, level);
  }

  public TomatoProjectile(Level level) {
    this(ModEntities.TomatoProjectileEntity.get(), level);
  }

  public TomatoProjectile(Level level, LivingEntity owner, float power) {
    this(level);
    this.setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    this.setOwner(owner);
    this.setPower(power);
  }

    @Override
  public void addKnockback(float amount) {
    this.knockback += amount;
  }

  @Override
  public void tick() {
    super.tick();
    HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
    HitResult.Type hitType = hitResult.getType();
    if (hitType != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitResult)) {
      this.onHit(hitResult);
    }
    if (!this.isRemoved()) {
      this.updateRotation();
      Vec3 newLocation = position();
      Vec3 velocity = this.getDeltaMovement();
      // if we hit a block and are still alive, relocate ourselves to that position so we don't skip blocks
      if (hitType == HitResult.Type.BLOCK) {
        EntityDimensions dimensions = getType().getDimensions();
        float factor = 0.01f;
        if (((BlockHitResult)hitResult).getDirection().getAxis() == Axis.Y) {
          factor += dimensions.height;
        } else {
          factor += dimensions.width / 2;
        }
        newLocation = hitResult.getLocation().add(velocity.normalize().scale(factor));
      } else {
        newLocation = newLocation.add(velocity);
      }
      velocity = velocity.scale(this.isInWater() ? 0.6F : 0.99F);
      this.setDeltaMovement(velocity);
      this.setPos(newLocation);
    }

    if (getY() > level().getMaxBuildHeight() + 64) {
      this.discard();
    }
  }

  @Override
  protected void onHitEntity(EntityHitResult result) {
    Entity target = result.getEntity();
    // apply knockback to the entity regardless of fluid type
    if (knockback > 0) {
      Vec3 vec3 = this.getDeltaMovement().multiply(1, 0, 1).normalize().scale(knockback * 0.6);
      if (vec3.lengthSqr() > 0) {
        target.push(vec3.x, 0.1, vec3.z);
      }
    }
    Level level = level();
    if (!level.isClientSide) {
        this.discard();
    }
  }

  @Override
  protected void onHitBlock(@NotNull BlockHitResult hitResult) {
    super.onHitBlock(hitResult);

    // hit the block
    // handle the fluid
    Level level = level();
    if (!level.isClientSide) {
        this.discard();
    }
  }

    /* Network */
  private static final String KEY_POWER = "power";
  private static final String KEY_KNOCKBACK = "knockback";

  @Override
  protected void defineSynchedData() {
  }

  @Override
  public void recreateFromPacket(@NotNull ClientboundAddEntityPacket packet) {
    // copied from llama spit
    super.recreateFromPacket(packet);
    double x = packet.getXa();
    double y = packet.getYa();
    double z = packet.getZa();
    for(int i = 0; i < 7; i++) {
      double offset = 0.4D + 0.1D * i;
      this.level().addParticle(ParticleTypes.SPIT, this.getX(), this.getY(), this.getZ(), x * offset, y, z * offset);
    }
    this.setDeltaMovement(x, y, z);
  }

  @Override
  protected void addAdditionalSaveData(@NotNull CompoundTag nbt) {
    super.addAdditionalSaveData(nbt);
    nbt.putFloat(KEY_POWER, power);
    nbt.putFloat(KEY_KNOCKBACK, knockback);
  }

  @Override
  protected void readAdditionalSaveData(@NotNull CompoundTag nbt) {
    super.readAdditionalSaveData(nbt);
    this.power = nbt.getFloat(KEY_POWER);
    this.knockback = nbt.getFloat(KEY_KNOCKBACK);
  }

  @Override
  public float getPower() {
      return this.power;
  }

  @Override
  public void setPower(float power) {
      this.power = power;
  }

  @Override
  public @NotNull ItemStack getItem() {
      return ModItems.TomatoPudding.get().getDefaultInstance();
  }

}
