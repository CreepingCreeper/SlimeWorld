package com.creeping_creeper.slimeworld.data.provider.tags;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModModifierIds;
import com.creeping_creeper.slimeworld.library.AffixUtil;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractModifierTagProvider;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.data.ModifierIds;

import static com.creeping_creeper.slimeworld.library.AffixUtil.AffixLevel.*;
import static com.creeping_creeper.slimeworld.library.AffixUtil.AffixType.*;

public class ModModifierTagsProvider extends AbstractModifierTagProvider {
    public ModModifierTagsProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, SlimeWorld.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(TinkerTags.Modifiers.OVERSLIME_FRIEND).add(ModModifierIds.overwash, ModModifierIds.overload, ModModifierIds.overtomato);

        tag(MELEE.getModifierTag(BAD.getLevel())).add(TinkerModifiers.insatiable.getId());
        tag(MELEE.getModifierTag(NEUTRAL.getLevel())).add(TinkerModifiers.insatiable.getId());
        tag(MELEE.getModifierTag(NORMAL.getLevel())).add(ModifierIds.unburdened);
        tag(MELEE.getModifierTag(GOOD.getLevel())).add(ModifierIds.lightweight);
        tag(MELEE.getModifierTag(BETTER.getLevel())).add(TinkerModifiers.insatiable.getId());

        tag(RANGED.getModifierTag(BAD.getLevel())).add(ModifierIds.erratic);
        tag(RANGED.getModifierTag(NEUTRAL.getLevel())).add(ModifierIds.spiny);
        tag(RANGED.getModifierTag(NORMAL.getLevel())).add(ModifierIds.unburdened);
        tag(RANGED.getModifierTag(GOOD.getLevel())).add(ModifierIds.holy, ModifierIds.supercharged, ModifierIds.keen, ModifierIds.lightweight);
        tag(RANGED.getModifierTag(BETTER.getLevel())).add(TinkerModifiers.insatiable.getId());

        tag(ARMOR.getModifierTag(BAD.getLevel())).add(TinkerModifiers.insatiable.getId());
        tag(ARMOR.getModifierTag(NEUTRAL.getLevel())).add(TinkerModifiers.insatiable.getId());
        tag(ARMOR.getModifierTag(NORMAL.getLevel())).add(TinkerModifiers.insatiable.getId());
        tag(ARMOR.getModifierTag(GOOD.getLevel())).add(TinkerModifiers.insatiable.getId());
        tag(ARMOR.getModifierTag(BETTER.getLevel())).add(TinkerModifiers.insatiable.getId());

        tag(GENERAL.getModifierTag(BAD.getLevel())).add(ModModifierIds.broken, ModModifierIds.damaged);
        tag(GENERAL.getModifierTag(NEUTRAL.getLevel())).add(ModifierIds.dense);
        tag(GENERAL.getModifierTag(NORMAL.getLevel())).add(ModModifierIds.hard);
        tag(GENERAL.getModifierTag(GOOD.getLevel())).add(ModModifierIds.superior);
        tag(GENERAL.getModifierTag(BETTER.getLevel())).add(ModModifierIds.authoritative);

        for (AffixUtil.AffixType type : AffixUtil.AffixType.TYPES) {
            for (AffixUtil.AffixLevel level : AffixUtil.AffixLevel.TYPES) {
                String sLevel = level.getLevel();
                tag(type.getModifierTag(sLevel)).addTag(GENERAL.getModifierTag(sLevel));
            }
        }
    }

    @Override
    public @NotNull String getName() {
        return "Slime World Modifier Tag Provider";
    }
}
