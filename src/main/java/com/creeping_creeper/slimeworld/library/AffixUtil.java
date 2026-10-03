package com.creeping_creeper.slimeworld.library;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModModifierIds;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.library.modifiers.ModifierManager;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public interface AffixUtil {
   Component Prep = SlimeWorld.makeTranslation("modifier", "affix.prep");

   static void addAffix(ToolStack tool, ItemStack stack, boolean special) {
       for (AffixType type : AffixType.TYPES){
           if (stack.is(type.getToolTag())){
               int rarity = special ? 4 : stack.getRarity().ordinal();
               String level = AffixLevel.getLevel(rarity);
               if (level.isEmpty()){
                   return;
               }
               ModifierId modifierId = randomModifier(type.getModifierTag(level));
               tool.getPersistentData().putString(SlimeWorld.getResource("affix"), modifierId.toString());
               tool.addModifier(ModModifierIds.affix, 1);
               break;
           }
       }
   }

   static ModifierId randomModifier(TagKey<Modifier> tag) {
       List<Modifier> options = ModifierManager.getTagValues(tag);
       if (options.isEmpty()) {
           SlimeWorld.LOG.error("There is no modifier in tag{}", tag);
           return ModifierId.EMPTY;
       }
       return options.get(Modifier.RANDOM.nextInt(options.size())).getId();
   }

   enum AffixType {
       ARMOR(TinkerTags.Items.ARMOR, "armor/"),
       RANGED(TinkerTags.Items.RANGED, "ranged/"),
       MELEE(TinkerTags.Items.MELEE, "melee/"),
       GENERAL(null, "general/");

       private final TagKey<Item> toolTag;
       private final String type;

       private static final AffixType[] TYPES = {ARMOR, RANGED, MELEE};

       AffixType(TagKey<Item> toolTag, String type) {
           this.toolTag = toolTag;
           this.type = type;
       }

       public TagKey<Item> getToolTag() {
           return toolTag;
       }

       public TagKey<Modifier> getModifierTag(String level) {
           return ModifierManager.getTag((SlimeWorld.getResource(type).withSuffix(level)));
       }
   }

   enum AffixLevel {
       NONE(""),
       BAD("bad"),
       NEUTRAL("neutral"),
       NORMAL("normal"),
       GOOD("good"),
       BETTER("better");

       private final String level;
       private static final int[] COMMON_INDEX = {4, 6, 6, 9, 10, 10};
       private static final int[] UNCOMMON_INDEX = {3, 5, 6, 9, 10, 10};
       private static final int[] RARE_INDEX = {2, 4, 5, 8, 10, 10};
       private static final int[] EPIC_INDEX = {1, 2, 4, 7, 9, 10};
       private static final int[] SPECIAL_INDEX = {0, 0, 3, 6, 8, 10};

       AffixLevel(String level) {
           this.level = level;
       }

       private static String getLevel(int rarity) {
           int r = Modifier.RANDOM.nextInt(10);
           int[] index = switch (rarity) {
               case 0 -> COMMON_INDEX;
               case 1 -> UNCOMMON_INDEX;
               case 2 -> RARE_INDEX;
               case 3 -> EPIC_INDEX;
               default -> SPECIAL_INDEX;
           };
           for (int i = 0; i < 6; i++) {
               if (r < index[i]) {
                   return values()[i].level;
               }
           }
           return "";
       }
   }

}
