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
import net.minecraft.world.level.block.*;
import org.jetbrains.annotations.NotNull;

public class WorldEvents {
    public static void init() {
        ComposterBlock.add(0.3f, ModItems.MagicbubbleSapling);
        ComposterBlock.add(0.3f, ModItems.SnowaveSapling);
        ComposterBlock.add(0.3f, ModItems.Magicbubbleleaves);
        ComposterBlock.add(0.3f, ModItems.Snowaveleaves);
        ComposterBlock.add(0.3f, ModItems.EarthSlimeBerry);
        ComposterBlock.add(0.3f, ModItems.SkySlimeBerry);
        ComposterBlock.add(0.3f, ModItems.EnderSlimeBerry);
        ComposterBlock.add(0.3f, ModItems.BloodSlimeBerry);
        ComposterBlock.add(0.3f, ModItems.Berriper);
        ComposterBlock.add(0.3f, ModItems.SlimeWeed);
        ComposterBlock.add(0.65f, ModItems.IchorFern);
        ComposterBlock.add(0.35f, ModItems.IchorTallGrass);
        ComposterBlock.add(0.35f, ModItems.IchorSlimeSapling);
        ComposterBlock.add(0.35f, ModItems.OceanCake);

        DispenseItemBehavior dispenseBucket = new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public @NotNull ItemStack execute(BlockSource source, ItemStack stack) {
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
        DispenserBlock.registerBehavior(ModFluids.LiquidMud, dispenseBucket);
        DispenserBlock.registerBehavior(ModFluids.MoltenSlimeBronze, dispenseBucket);
        DispenserBlock.registerBehavior(ModFluids.Mercury, dispenseBucket);
        DispenserBlock.registerBehavior(ModFluids.SulfuricAcid, dispenseBucket);
        DispenserBlock.registerBehavior(ModItems.SulfurCubeBucket, dispenseBucket);

        FireBlock fireblock = (FireBlock) Blocks.FIRE;
        fireblock.setFlammable(ModItems.Magicbubbleleaves.get(), 30, 60);
        fireblock.setFlammable(ModItems.Snowaveleaves.get(), 30, 60);
        fireblock.setFlammable(ModItems.MagicbubbleLog.get(), 5, 5);
        fireblock.setFlammable(ModItems.ActiveMagicbubbleLog.get(), 5, 5);
        fireblock.setFlammable(ModItems.SnowaveLog.get(), 5, 5);
        fireblock.setFlammable(ModItems.StrippedSnowaveLog.get(), 5, 5);
    }
}
