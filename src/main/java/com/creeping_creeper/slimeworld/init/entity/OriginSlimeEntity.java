package com.creeping_creeper.slimeworld.init.entity;

import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;
import slimeknights.tconstruct.world.TinkerWorld;
import slimeknights.tconstruct.world.entity.TravelersPlateSlimeEntity;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class OriginSlimeEntity extends TravelersPlateSlimeEntity{
    private static final List<Item> TRANSFORM_ITEMS = List.of(ModItems.SulfurMud.asItem(), TinkerWorld.earthGeode.asItem(), TinkerWorld.skyGeode.asItem(), TinkerWorld.ichorGeode.asItem(), TinkerWorld.enderGeode.asItem(), ModItems.OceanGeode.asItem(), Items.MUD, Items.MAGMA_BLOCK);
    protected int age;

    public OriginSlimeEntity(EntityType<? extends OriginSlimeEntity> type, Level worldIn) {
        super(type, worldIn);
        this.age = this.isHuge() ? 0 : -24000;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new OriginSlimeTemptGoal((itemStack) -> isHuge() ? TRANSFORM_ITEMS.contains(itemStack.getItem()) : isFood(itemStack) || TRANSFORM_ITEMS.contains(itemStack.getItem())));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && !this.isHuge() && this.isAlive()) {
            if (age == 0){
                this.setSize(this.getSize() * 2, true);
                if (this.getSize() == 2){
                    age = -24000;
                }
            }else age++;
        }
    }

    private boolean isFood(ItemStack stack){
        return stack.is(Tags.Items.SLIMEBALLS);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    private boolean isHuge(){
        return this.getSize() >= 4;
    }

    @Override
    public boolean isBaby(){
        return isTiny();
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack =  player.getItemInHand(hand);
        Level level = this.level();
        if (!level.isClientSide()) {
            int i = 0;
            if (!isHuge() && isFood(itemStack)){
                this.age -= (int) (this.age * 0.1);
                ServerLevel server = (ServerLevel)this.level();
                server.sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0F), this.getRandomY() + (double)0.5F, this.getRandomZ(1.0F), 0, 0.0F, 0.0F, 0.0F, 0);
                this.setPersistenceRequired();
            }else for (Item item : TRANSFORM_ITEMS){
                if (itemStack.is(item)){
                    Slime slime = switch (i){
                        case 0 -> ModEntities.SulfurCubeEntity.get().create(level);
                        case 1 -> EntityType.SLIME.create(level);
                        case 2 -> TinkerWorld.skySlimeEntity.get().create(level);
                        case 3 -> ModEntities.IchorSlimeEntity.get().create(level);
                        case 4 -> TinkerWorld.enderSlimeEntity.get().create(level);
                        case 5 -> ModEntities.OceanSlimeEntity.get().create(level);
                        case 6 -> TinkerWorld.terracubeEntity.get().create(level);
                        default -> EntityType.MAGMA_CUBE.create(level);
                    };
                    assert slime != null;
                    transform(slime);
                    this.discard();
                    break;
                }
                i++;
            }
            if (i < TRANSFORM_ITEMS.size()){
                 if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    private void transform(Slime slime){
        slime.setPersistenceRequired();
        slime.setCustomName(this.getCustomName());
        slime.setNoAi(this.isNoAi());
        slime.setInvulnerable(this.isInvulnerable());
        slime.setSize(this.getSize(), true);
        slime.setHealth(this.getHealth());
        slime.setItemSlot(EquipmentSlot.HEAD, getItemBySlot(EquipmentSlot.HEAD).copy());
        slime.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
        this.level().addFreshEntity(slime);
    }

    @Override
    protected boolean isDealsDamage() {
        return false;
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Age", age);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        age = compound.getInt("Age");
    }

    @Override
    protected @NotNull ParticleOptions getParticleType() {
        return ModParticles.OriginSlimeParticle.get();
    }

    @Override
    protected @NotNull MaterialId getPlating() {
        return MaterialIds.copper;
    }

    class OriginSlimeTemptGoal extends Goal {
        private final Predicate<ItemStack> items;
        private Player temptingPlayer;

        public OriginSlimeTemptGoal(Predicate<ItemStack> items) {
            this.items = items;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            this.temptingPlayer = OriginSlimeEntity.this.level().getNearestPlayer(OriginSlimeEntity.this.getX(), OriginSlimeEntity.this.getY(), OriginSlimeEntity.this.getZ(), 10.0D, this::isTemptingItem);
            return this.temptingPlayer != null;
        }

        @Override
        public void tick() {
            if (this.temptingPlayer != null) {
                OriginSlimeEntity.this.lookAt(this.temptingPlayer, 10.0F, 10.0F);
            }
            MoveControl var2 = OriginSlimeEntity.this.getMoveControl();
            if (var2 instanceof Slime.SlimeMoveControl cubeMoveControl) {
                cubeMoveControl.setDirection(OriginSlimeEntity.this.getYRot(), true);
            }

        }

        private boolean isTemptingItem(Entity entity) {
            return entity instanceof Player player && (this.items.test(player.getMainHandItem()) || this.items.test(player.getOffhandItem()));
        }
    }
}