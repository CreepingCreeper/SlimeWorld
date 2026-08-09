package com.creeping_creeper.slimeworld.data.provider.tinkering;

import com.creeping_creeper.slimeworld.data.key.ModMaterialIds;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.data.material.AbstractMaterialDataProvider;
import slimeknights.tconstruct.library.data.material.AbstractMaterialStatsDataProvider;
import slimeknights.tconstruct.tools.stats.*;

import static net.minecraft.world.item.Tiers.DIAMOND;

public class ModStatsProvider extends AbstractMaterialStatsDataProvider {
    public ModStatsProvider(PackOutput packOutput, AbstractMaterialDataProvider materials) {
        super(packOutput, materials);
    }

    @Override
    protected void addMaterialStats() {
        addMeleeHarvest();
        addRanged();
        addAmmo();
        addArmor();
        addSlimesuit();
        addMisc();
    }
    private void addMeleeHarvest() {
        addMaterialStats(ModMaterialIds.slimeBronze,
                new HeadMaterialStats(800, 6.5f, DIAMOND, 2.25f),
                HandleMaterialStats.multipliers().durability(1.05f).attackSpeed(1.05f).miningSpeed(1.05f).build(),
                StatlessMaterialStats.BINDING);
    }

    private void addRanged() {
        addMaterialStats(ModMaterialIds.kelp, StatlessMaterialStats.BOWSTRING);
        addMaterialStats(ModMaterialIds.slimeBronze,
                new LimbMaterialStats(800, 0.5f, 0.5f, 0.05f),
                new GripMaterialStats(0.05f, 0.05f, 2.25f));
    }

    private void addAmmo() {
        addMaterialStats(ModMaterialIds.oceanslime, StatlessMaterialStats.ARROW_HEAD);
    }

    private void addArmor() {
        addMaterialStats(ModMaterialIds.kelp, StatlessMaterialStats.MAILLE, StatlessMaterialStats.CUIRASS);
        addArmorShieldStats(ModMaterialIds.slimeBronze, PlatingMaterialStats.builder().durabilityFactor(25).armor(2, 5, 7, 2).toughness(1).knockbackResistance(0.05f), StatlessMaterialStats.MAILLE);

    }

    private void addSlimesuit() {
        addMaterialStats(ModMaterialIds.oceanslime, new SlimeStats( 75, 125)); // 200
        addMaterialStats(ModMaterialIds.kelp, RepairStats.laces(100));
    }

    private void addMisc() {
        addMaterialStats(ModMaterialIds.kelp, StatlessMaterialStats.REPAIR_KIT);
    }

    @Override
    public @NotNull String getName() {
        return "Slime World Material Stats";
    }
}
