package com.creeping_creeper.slimeworld.init.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class EmptyMobBucketItem extends Item {
    private final Supplier<? extends EntityType<?>> entityTypeSupplier;
    private final Supplier<? extends SoundEvent> emptySoundSupplier;

    public EmptyMobBucketItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends SoundEvent> soundSupplier, Properties properties) {
        super(properties);
        this.emptySoundSupplier = soundSupplier;
        this.entityTypeSupplier = entitySupplier;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack itemStack = context.getItemInHand();
        if (level instanceof ServerLevel) {
            this.spawn((ServerLevel)level, itemStack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
            this.playEmptySound(player, level, pos);
            if (player != null) {
                player.setItemInHand(context.getHand(), getEmptySuccessItem(itemStack, player));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    private static ItemStack getEmptySuccessItem(ItemStack bucketStack, Player player) {
        return !player.getAbilities().instabuild ? new ItemStack(Items.BUCKET) : bucketStack;
    }

    private void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos) {
        level.playSound(player, pos, getEmptySound(), SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    private void spawn(ServerLevel serverLevel, ItemStack bucketedMobStack, BlockPos pos) {
        Entity entity = this.getFishType().spawn(serverLevel, bucketedMobStack, null, pos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof Bucketable bucketable) {
            bucketable.loadFromBucketTag(bucketedMobStack.getOrCreateTag());
            bucketable.setFromBucket(true);
        }

    }

    protected EntityType<?> getFishType() {
        return this.entityTypeSupplier.get();
    }

    protected SoundEvent getEmptySound() {
        return this.emptySoundSupplier.get();
    }
}
