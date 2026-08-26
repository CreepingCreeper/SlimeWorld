package com.creeping_creeper.slimeworld.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class UnknownTpBlock extends Block {
    public UnknownTpBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.isAlive() && !player.isPassenger() && !player.isVehicle()) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DungeonTeleporterBE teleporterBE && level instanceof ServerLevel serverLevel) {
                MinecraftServer server = serverLevel.getServer();
                ServerLevel dungeonLevel = server.getLevel(DUNGEONS);
                BlockPos dungeonPos = teleporterBE.findDungeons(dungeonLevel);

                if (!teleporterBE.generated) {
                    teleporterBE.generated = true;
                    JellyDungeonGenerator generator = new JellyDungeonGenerator();
                    generator.generate(dungeonLevel, dungeonPos.below());
                }
                //传送
                DimensionTransition destination = getDestination(level, dungeonPos, player);
                if (destination != null && player.canChangeDimensions(level, destination.newLevel())) {

                    player.changeDimension(destination);
                    return ItemInteractionResult.SUCCESS;
                }
            }

        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
