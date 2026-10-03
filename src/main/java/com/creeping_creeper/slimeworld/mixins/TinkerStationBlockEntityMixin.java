package com.creeping_creeper.slimeworld.mixins;

import com.creeping_creeper.slimeworld.data.key.ModModifierIds;
import com.creeping_creeper.slimeworld.library.AffixUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tables.block.entity.table.TinkerStationBlockEntity;

@Mixin({TinkerStationBlockEntity.class})
public class TinkerStationBlockEntityMixin {
    @Inject(method = "onCraft", at = @At(value = "INVOKE", target = "Lslimeknights/tconstruct/tables/block/entity/table/TinkerStationBlockEntity;playCraftSound(Lnet/minecraft/world/entity/player/Player;)V"), remap = false)
    private void onCraft(Player player, ItemStack resultItem, int amount, CallbackInfo ci) {
        ToolStack tool = ToolStack.from(resultItem);
        if (tool.getModifierLevel(ModModifierIds.affix) == 0){
            AffixUtil.addAffix(tool, resultItem);
        }
    }
}
