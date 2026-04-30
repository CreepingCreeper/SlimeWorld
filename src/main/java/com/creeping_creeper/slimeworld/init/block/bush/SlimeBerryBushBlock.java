package com.creeping_creeper.slimeworld.init.block.bush;

import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import slimeknights.tconstruct.world.block.FoliageType;
import slimeknights.tconstruct.world.block.SlimeGrassBlock;
import slimeknights.tconstruct.world.block.SlimeNyliumBlock;

public class SlimeBerryBushBlock extends CommonBerryBushBlock{
    public static final IntegerProperty BERRY = IntegerProperty.create("berry", 0, 5);

    public SlimeBerryBushBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BERRY, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BERRY);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int i = 0;
        Block dirt = context.getLevel().getBlockState(context.getClickedPos().below()).getBlock();
        if (dirt instanceof SlimeGrassBlock grass){
            for (FoliageType type : FoliageType.values()) {
                i++;
                if (type == grass.getFoliageType()) {
                    break;
                }
            }
        } else if (dirt instanceof SlimeNyliumBlock){
            i = 5;
        }
        return this.defaultBlockState().setValue(BERRY, i);
    }

    @Override
    public Item getBerry(BlockState state){
        return switch (state.getValue(BERRY)){
            case 1 -> ModItems.EarthSlimeBerry.asItem();
            case 2 -> ModItems.SkySlimeBerry.asItem();
            case 4 -> ModItems.EnderSlimeBerry.asItem();
            case 5 -> ModItems.BloodSlimeBerry.asItem();
            default -> Items.SUGAR;
        };
    }
}
