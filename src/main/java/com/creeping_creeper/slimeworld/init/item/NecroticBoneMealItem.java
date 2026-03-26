package com.creeping_creeper.slimeworld.init.item;

import com.creeping_creeper.slimeworld.data.ModTags;
import com.creeping_creeper.slimeworld.init.block.NecroticBonemealableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
import slimeknights.mantle.util.TranslationHelper;
import slimeknights.tconstruct.common.Sounds;
import slimeknights.tconstruct.common.TinkerDamageTypes;

import javax.annotation.Nullable;
import java.util.List;

public class NecroticBoneMealItem extends Item {
    public NecroticBoneMealItem(Properties properties) {
        super(properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        if (applyBonemeal(context.getItemInHand(), level, blockpos, context.getPlayer())) {
            if (!level.isClientSide) {
                level.levelEvent(1505, blockpos, 0);
                level.playLocalSound(blockpos,  Sounds.NECROTIC_HEAL.getSound(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } return InteractionResult.PASS;
    }

    public static boolean applyBonemeal(ItemStack item, Level level, BlockPos pos, Player player) {
        BlockState blockstate = level.getBlockState(pos);
        int hook = ForgeEventFactory.onApplyBonemeal(player, level, pos, blockstate, item);
        if (hook != 0) {
            return hook > 0;
        } else {
            if (blockstate.is(ModTags.Blocks.NecroticClonable)){
                if (level instanceof ServerLevel) {
                    player.hurt(TinkerDamageTypes.source(level.registryAccess(), TinkerDamageTypes.BLEEDING), 5);
                    ItemEntity itementity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(blockstate.getBlock().asItem()));
                    level.addFreshEntity(itementity);
                }
                item.shrink(1);
                return true;
            }else if (blockstate.getBlock() instanceof NecroticBonemealableBlock block) {
                if (block.isValidBonemealTarget(level, pos, blockstate, level.isClientSide)) {
                    if (level instanceof ServerLevel) {
                        player.hurt(TinkerDamageTypes.source(level.registryAccess(), TinkerDamageTypes.BLEEDING), 3);
                        block.performBonemeal((ServerLevel)level, level.random, pos, blockstate);
                    }
                    item.shrink(1);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        TranslationHelper.addOptionalTooltip(stack, tooltip);
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
