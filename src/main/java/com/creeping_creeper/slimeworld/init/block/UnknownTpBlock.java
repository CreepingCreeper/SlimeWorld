package com.creeping_creeper.slimeworld.init.block;

import com.creeping_creeper.slimeworld.data.key.ModResourceKeys;
import com.creeping_creeper.slimeworld.library.ModUtil;
import com.creeping_creeper.slimeworld.library.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.phys.BlockHitResult;

public class UnknownTpBlock extends Block {
    public static final BooleanProperty USED = BooleanProperty.create("used");
    private final ResourceKey<Structure> structure;
    private final TagKey<Item> item;

    public UnknownTpBlock(Properties properties, ResourceKey<Structure> structure, TagKey<Item> item) {
        super(properties);
        this.structure = structure;
        this.item = item;
        this.registerDefaultState(this.stateDefinition.any().setValue(USED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(USED);
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (player.canChangeDimensions() && player.getItemInHand(hand).is(item)) {
            MinecraftServer server = level.getServer();
            if (server == null) return InteractionResult.PASS;
            ServerLevel serverLevel = level.dimension() == ModResourceKeys.UNKNOWN_AREA ? server.getLevel(ModResourceKeys.SLIMEWORLD) : server.getLevel(ModResourceKeys.UNKNOWN_AREA);
            if (serverLevel != null) {
                ParticleUtil.slimeParticle(level, ParticleTypes.ITEM_SLIME, 12, 1, pos.getX(), pos.getY() + 1, pos.getZ());
                if (!state.getValue(USED)){
                    level.setBlock(pos, state.setValue(USED, Boolean.TRUE), 3);
                }
                player.teleportTo(serverLevel, pos.getX(), pos.getY() + 1, pos.getZ(), ModUtil.DEFAULT_TELEPORT_FLAGS, player.getYRot(), player.getXRot());
            }
        }
        return InteractionResult.PASS;
    }
}
