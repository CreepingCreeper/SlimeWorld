package com.creeping_creeper.slimeworld.init.block.bush;

import com.creeping_creeper.slimeworld.data.key.ModTags;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.block.NecroticBonemealableBlock;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IForgeShearable;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class OreBerryBushBlock extends BushBlock implements NecroticBonemealableBlock, IForgeShearable {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final VoxelShape  SAPLING_SHAPE = Block.box(3.0F, 0.0F, 3.0F, 13.0F, 8.0F, 13.0F);
    public static final VoxelShape MID_GROWTH_SHAPE = Block.box(1.0F, 0.0F, 1.0F, 15.0F, 16.0F, 15.0F);
    private final Supplier<Item> ITEM;

    public OreBerryBushBlock(Supplier<Item> item, Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
        this.ITEM = item;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if (state.getValue(AGE) == 0) {
            return SAPLING_SHAPE;
        } else {
            return state.getValue(AGE) < 3 ? MID_GROWTH_SHAPE : super.getShape(state, level, pos, context);
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.is(Tags.Blocks.STONE);
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nullable Player player, @NotNull ItemStack item, Level world, BlockPos pos, int fortune) {
        return Lists.newArrayList(new ItemStack(this, 1));
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader level, @NotNull BlockPos pos) {
        return level.getRawBrightness(pos, 0) < 10 && level.getBlockState(pos.below()).is(Tags.Blocks.STONE);
    }

    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(AGE) < 3;
    }

    public void randomTick(BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        int i = state.getValue(AGE);
        if (i < 3 && level.getRawBrightness(pos.above(), 0) < 10 && ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(8) == 0)) {
            BlockState blockstate = state.setValue(AGE, i + 1);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockstate));
            ForgeHooks.onCropsGrowPost(level, pos, state);
        }
    }

    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof LivingEntity && !entity.getType().is(ModTags.EntityTypes.ORE_BERRY_BUSHES_IMMUNE)) {
            entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75F, 0.8F));
            if (!level.isClientSide && state.getValue(AGE) > 0 && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
                double d0 = Math.abs(entity.getX() - entity.xOld);
                double d1 = Math.abs(entity.getZ() - entity.zOld);
                if (d0 >= 0.003F || d1 >= 0.003F) {
                    entity.hurt(level.damageSources().sweetBerryBush(), 2.0F);
                }
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        int i = state.getValue(AGE);
        boolean flag = i == 3;
        if (!flag && player.getItemInHand(hand).is(ModItems.NecroticBoneMeal.get())) {
            return InteractionResult.PASS;
        } else if (i > 1) {
            int j = 1 + level.random.nextInt(2);
            popResource(level, pos, new ItemStack(ITEM.get(), j + (flag ? 1 : 0)));
            level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            BlockState blockstate = state.setValue(AGE, 1);
            level.setBlock(pos, blockstate, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockstate));
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(state, level, pos, player, hand, hit);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader reader, BlockPos pos, BlockState state, boolean var) {
        return state.getValue(AGE) < 3;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource source, BlockPos pos, BlockState state) {
        level.setBlock(pos, state.setValue(AGE, Math.min(3, state.getValue(AGE) + 1)), 2);
    }

}
