package com.creeping_creeper.slimeworld.init.block;

import com.creeping_creeper.slimeworld.data.key.ModResourceKeys;
import com.creeping_creeper.slimeworld.library.ModUtil;
import com.creeping_creeper.slimeworld.library.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.phys.BlockHitResult;

public class UnknownTpBlock extends Block {
    public static final BooleanProperty USED = BooleanProperty.create("used");
    private final BlockState block;
    private final ResourceKey<StructureTemplatePool> pool;
    private final TagKey<Item> item;

    public UnknownTpBlock(Properties properties, Block block, ResourceKey<StructureTemplatePool> pool, TagKey<Item> item) {
        super(properties);
        this.block = block.defaultBlockState();
        this.pool = pool;
        this.item = item;
        this.registerDefaultState(this.stateDefinition.any().setValue(USED, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(USED);
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide && player.canChangeDimensions() && player.getItemInHand(hand).is(item)) {
            MinecraftServer server = level.getServer();
            if (server == null) return InteractionResult.PASS;
            ServerLevel serverLevel = level.dimension() == ModResourceKeys.UNKNOWN_AREA ? server.getLevel(ModResourceKeys.SLIMEWORLD) : server.getLevel(ModResourceKeys.UNKNOWN_AREA);
            if (serverLevel != null) {
                ParticleUtil.slimeParticle(level, ParticleTypes.ITEM_SLIME, 12, 1, pos.getX(), pos.getY() + 1, pos.getZ());
                if (!state.getValue(USED)){
                    generateProtectedPlatform(serverLevel, pos);
                    serverLevel.setBlock(pos.atY(1), state.setValue(USED, Boolean.TRUE), 3);
                    Registry<StructureTemplatePool> poolRegistry = level.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
                    Holder<StructureTemplatePool> poolHolder = poolRegistry.getHolder(pool).orElseThrow();
                    JigsawPlacement.generateJigsaw(serverLevel, poolHolder, ResourceLocation.withDefaultNamespace("bottom"), 7, pos.atY(3), false);
                    level.setBlock(pos, state.setValue(USED, Boolean.TRUE), 3);
                }
                player.getItemInHand(hand).shrink(1);
                player.teleportTo(serverLevel, pos.getX(), pos.getY() + 2, pos.getZ(), ModUtil.DEFAULT_TELEPORT_FLAGS, player.getYRot(), player.getXRot());
            }
        }
        return InteractionResult.PASS;
    }

    private void generateProtectedPlatform(ServerLevel serverLevel, BlockPos centerPos) {
        centerPos = centerPos.atY(0);
        final int halfSize = 64;
        final int platformY = 0;
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState barrier = Blocks.BARRIER.defaultBlockState();

        BlockPos platformMin = centerPos.offset(-halfSize, 0, -halfSize).atY(platformY);
        BlockPos platformMax = centerPos.offset(halfSize - 1, 0, halfSize - 1).atY(platformY);

        for (BlockPos pos : BlockPos.betweenClosed(platformMin, platformMax)) {
            setBlockFast(serverLevel, pos, bedrock);
            setBlockFast(serverLevel, pos.above(), block);
        }

        int wallEndY = 127;

        BlockPos wallLeftMin  = centerPos.offset(-halfSize, 0, -halfSize).atY(platformY);
        BlockPos wallLeftMax  = centerPos.offset(-halfSize, 0, halfSize).atY(wallEndY);
        for (BlockPos pos : BlockPos.betweenClosed(wallLeftMin, wallLeftMax)) {
            setBlockFast(serverLevel, pos, barrier);
        }

        BlockPos wallRightMin = centerPos.offset(halfSize, 0, -halfSize).atY(platformY);
        BlockPos wallRightMax = centerPos.offset(halfSize, 0, halfSize).atY(wallEndY);
        for (BlockPos pos : BlockPos.betweenClosed(wallRightMin, wallRightMax)) {
            setBlockFast(serverLevel, pos, barrier);
        }

        BlockPos wallFrontMin = centerPos.offset(-halfSize + 1, 0, -halfSize).atY(platformY);
        BlockPos wallFrontMax = centerPos.offset(halfSize - 1, 0, -halfSize).atY(wallEndY);
        for (BlockPos pos : BlockPos.betweenClosed(wallFrontMin, wallFrontMax)) {
            setBlockFast(serverLevel, pos, barrier);
        }

        BlockPos wallBackMin  = centerPos.offset(-halfSize + 1, 0, halfSize).atY(platformY);
        BlockPos wallBackMax  = centerPos.offset(halfSize - 1, 0, halfSize).atY(wallEndY);
        for (BlockPos pos : BlockPos.betweenClosed(wallBackMin, wallBackMax)) {
            setBlockFast(serverLevel, pos, barrier);
        }

        BlockPos ceilingMin = centerPos.offset(-halfSize, 0, -halfSize).atY(wallEndY);
        BlockPos ceilingMax = centerPos.offset(halfSize - 1, 0, halfSize - 1).atY(wallEndY);
        for (BlockPos pos : BlockPos.betweenClosed(ceilingMin, ceilingMax)) {
            setBlockFast(serverLevel, pos, barrier);
        }
    }

    private void setBlockFast(ServerLevel level, BlockPos pos, BlockState state) {
        ChunkAccess chunk = level.getChunk(pos);
        chunk.setBlockState(pos, state, false);
    }
}
