package com.creeping_creeper.slimeworld.init.block;

import com.creeping_creeper.slimeworld.data.key.ModResourceKeys;
import com.creeping_creeper.slimeworld.init.entity.boss.BaseBossSlimeEntity;
import com.creeping_creeper.slimeworld.library.ModUtil;
import com.creeping_creeper.slimeworld.library.ParticleUtil;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

public class UnknownTpBlock extends Block {
    public static final BooleanProperty USED = BooleanProperty.create("used");
    private final Supplier<EntityType<?>> boss;
    private final BlockState block;
    private final ResourceKey<StructureTemplatePool> pool;
    private final TagKey<Item> item;

    public UnknownTpBlock(Properties properties, Supplier<EntityType<?>> boss, Block block, ResourceKey<StructureTemplatePool> pool, TagKey<Item> item) {
        super(properties);
        this.boss = boss;
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
        if (!level.isClientSide && player.canChangeDimensions()) {
            ItemStack itemStack = player.getItemInHand(hand);
            MinecraftServer server = level.getServer();
            if (server == null || !itemStack.is(item)) return InteractionResult.PASS;
            ServerLevel serverLevel = level.dimension() == ModResourceKeys.UNKNOWN_AREA ? server.getLevel(ModResourceKeys.SLIMEWORLD) : server.getLevel(ModResourceKeys.UNKNOWN_AREA);
            if (serverLevel != null) {
                if (!state.getValue(USED)){
                    generateProtectedPlatform(serverLevel, pos, block);
                    level.setBlock(pos, state.setValue(USED, Boolean.TRUE), 3);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
                    level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                ParticleUtil.slimeParticle(level, ParticleTypes.ITEM_SLIME, 12, 1, pos.getX(), pos.getY() + 1, pos.getZ());
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                player.teleportTo(serverLevel, pos.getX(), 3, pos.getZ(), ModUtil.DEFAULT_TELEPORT_FLAGS, player.getYRot(), player.getXRot());
                serverLevel.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        return InteractionResult.PASS;
    }

    private void generateProtectedPlatform(ServerLevel serverLevel, BlockPos centerPos, BlockState floorState) {
        centerPos = centerPos.atY(0);
        BlockState bedrock = Blocks.BEDROCK.defaultBlockState();
        BlockState barrier = Blocks.BARRIER.defaultBlockState();
        //floor
        BlockPos platformMin = centerPos.offset(-32, 0, -32).atY(0);
        BlockPos platformMax = centerPos.offset(32, 0, 32).atY(0);
        for (BlockPos pos : BlockPos.betweenClosed(platformMin, platformMax)) {
            setBlockFast(serverLevel, pos, bedrock);
            setBlockFast(serverLevel, pos.above(), floorState);
            setBlockFast(serverLevel, pos.above(2), floorState);
        }
        //ceiling
        BlockPos ceilingMin = centerPos.offset(-32, 0, -32).atY(64);
        BlockPos ceilingMax = centerPos.offset(32, 0, 32).atY(64);
        for (BlockPos pos : BlockPos.betweenClosed(ceilingMin, ceilingMax)) {
            setBlockFast(serverLevel, pos, barrier);
        }

        Registry<StructureTemplatePool> poolRegistry = serverLevel.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> poolHolder = poolRegistry.getHolder(pool).orElseThrow();
        for(Direction direction : Direction.Plane.HORIZONTAL) {
            Vec3i dir = direction.getNormal();
            Vec3i start = dir.offset(direction.getClockWise().getNormal());
            Vec3i end = dir.offset(direction.getCounterClockWise().getNormal());
            //wall
            BlockPos wallMin = centerPos.offset(start.multiply(32));
            BlockPos wallMax = centerPos.offset(end.multiply(32)).atY(64);
            for (BlockPos pos : BlockPos.betweenClosed(wallMin, wallMax)) {
                setBlockFast(serverLevel, pos, barrier);
            }
            //jigsaw
            RandomSource random = serverLevel.getRandom();
            int num = random.nextInt(16) + 16;
            BlockPos feature;
            for (int i = 0; i < num; i++){
                feature = centerPos.offset(random.nextInt(47) - 24, 3, random.nextInt(47) - 24);
                JigsawPlacement.generateJigsaw(serverLevel, poolHolder, ResourceLocation.withDefaultNamespace("bottom"), 7, feature, false);
            }
        }
        //boss
        BaseBossSlimeEntity boss = (BaseBossSlimeEntity) this.boss.get().create(serverLevel);
        if (boss != null) {
            boss.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(centerPos), MobSpawnType.STRUCTURE, null, null);
            boss.moveTo(centerPos.atY(24), 0, 0);
            serverLevel.addFreshEntity(boss);
        }
    }

    private static void setBlockFast(ServerLevel level, BlockPos pos, BlockState state) {
        //no block update
        ChunkAccess chunk = level.getChunk(pos);
        chunk.setBlockState(pos, state, false);
    }
}
