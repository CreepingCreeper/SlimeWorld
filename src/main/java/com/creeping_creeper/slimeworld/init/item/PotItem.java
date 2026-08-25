package com.creeping_creeper.slimeworld.init.item;

import com.creeping_creeper.slimeworld.init.ModEntities;
import com.creeping_creeper.slimeworld.init.ModItems;
import com.creeping_creeper.slimeworld.init.entity.PlantLikeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;

public class PotItem extends Item {
    public static final String PLANT_DATA = "PlantData";
    public static final String Type = "type";
    public PotItem(Properties properties) {
        super(properties);
    }

    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        if (!blockstate.is(TinkerTags.Blocks.SLIMY_SOIL)) {
            return InteractionResult.FAIL;
        } else {
            ItemStack itemStack = context.getItemInHand();
            if (!level.isClientSide) {
                PlantLikeEntity plant = new PlantLikeEntity(ModEntities.Grass.get(), level);
                if (itemStack.getTag() != null) {
                    CompoundTag tag = itemStack.getTag().getCompound(PLANT_DATA);
                    plant.load(tag);
                    plant.moveTo(blockpos.above(), 0, 0);
                }
                plant.playSound(SoundEvents.GRASS_PLACE, 1.0F, 1.0F);
                level.addFreshEntity(plant);
            }
            if (context.getPlayer() != null) {
                Player player = context.getPlayer();
                itemStack.shrink(1);
                ItemStack itemStack1 = ModItems.MagicPot.get().getDefaultInstance();
                player.setItemInHand(context.getHand(), itemStack1);
            }
            level.gameEvent(GameEvent.ENTITY_PLACE, blockpos, GameEvent.Context.of(context.getPlayer(), level.getBlockState(blockpos.below())));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

}
