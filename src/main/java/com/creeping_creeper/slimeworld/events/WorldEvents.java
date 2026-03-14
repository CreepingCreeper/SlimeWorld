package com.creeping_creeper.slimeworld.events;

import com.creeping_creeper.slimeworld.init.ModFluids;
import com.creeping_creeper.slimeworld.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.DispensibleContainerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;

public class WorldEvents {
    public static void init() {
        ComposterBlock.add(0.3f, ModItems.EarthSlimeBerry);
        ComposterBlock.add(0.3f, ModItems.SkySlimeBerry);
        ComposterBlock.add(0.3f, ModItems.EnderSlimeBerry);
        ComposterBlock.add(0.3f, ModItems.BloodSlimeBerry);
        ComposterBlock.add(0.3f, ModItems.Berriper);
        ComposterBlock.add(0.3f, ModItems.SlimeKelp);
        ComposterBlock.add(0.3f, ModItems.DriedSlimeKelp);
        ComposterBlock.add(0.65f, ModItems.IchorFern);
        ComposterBlock.add(0.35f, ModItems.IchorTallGrass);
        ComposterBlock.add(0.35f, ModItems.IchorSlimeSapling);

        DispenseItemBehavior dispenseBucket = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public ItemStack execute(BlockSource source, ItemStack stack) {
                DispensibleContainerItem container = (DispensibleContainerItem)stack.getItem();
                BlockPos blockpos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
                Level level = source.getLevel();
                if (container.emptyContents(null, level, blockpos, null, stack)) {
                    container.checkExtraContent(null, level, stack, blockpos);
                    return new ItemStack(Items.BUCKET);
                } else {
                    return this.defaultDispenseItemBehavior.dispense(source, stack);
                }
            }
        };

        DispenserBlock.registerBehavior(ModFluids.OceanSlime, dispenseBucket);
        DispenserBlock.registerBehavior(ModFluids.ResonanceSlime, dispenseBucket);
        
    }
}
