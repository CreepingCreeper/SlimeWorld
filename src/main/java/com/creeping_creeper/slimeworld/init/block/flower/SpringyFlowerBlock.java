package com.creeping_creeper.slimeworld.init.block.flower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import slimeknights.tconstruct.common.network.TinkerNetwork;
import slimeknights.tconstruct.shared.TinkerEffects;

public class SpringyFlowerBlock extends BaseFlowerBlock {
    public SpringyFlowerBlock(Properties properties) {
        super(TinkerEffects.bouncy, 7, properties, ParticleTypes.SPORE_BLOSSOM_AIR);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity living && !living.hasEffect(TinkerEffects.bouncy.get())) {
            living.knockback(3, -Mth.sin((float) (entity.getYRot() * Math.PI / 180.0F)), Mth.cos((float) (entity.getYRot() * Math.PI / 180.0F)));
            if (living instanceof ServerPlayer playerMP) {
                TinkerNetwork.getInstance().sendVanillaPacket(new ClientboundSetEntityMotionPacket(living), playerMP);
            }
            TinkerEffects.bouncy.get().apply(living, 30, 0, true);
            RandomSource random = living.getRandom();
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SLIME_ATTACK, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F);
        }
    }
}
