package com.creeping_creeper.slimeworld.init.block.grass;

import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.KelpPlantBlock;

public class SlimeKelpPlantBlock extends KelpPlantBlock {
    public SlimeKelpPlantBlock(Properties p_54300_) {
        super(p_54300_);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) ModItems.SlimeKelp.get();
    }
}
