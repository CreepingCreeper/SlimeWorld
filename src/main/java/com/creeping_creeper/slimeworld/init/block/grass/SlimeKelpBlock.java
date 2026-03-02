package com.creeping_creeper.slimeworld.init.block.grass;

import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.KelpBlock;

public class SlimeKelpBlock extends KelpBlock {
    public SlimeKelpBlock(Properties p_54300_) {
        super(p_54300_);
    }

    @Override
    protected Block getBodyBlock() {
        return ModItems.SlimeKelpPlant.get();
    }
}
