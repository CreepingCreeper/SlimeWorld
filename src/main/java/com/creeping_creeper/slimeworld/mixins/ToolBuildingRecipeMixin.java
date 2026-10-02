package com.creeping_creeper.slimeworld.mixins;

import com.creeping_creeper.slimeworld.SlimeWorld;
import com.creeping_creeper.slimeworld.data.key.ModModifierIds;
import net.minecraft.core.RegistryAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipe;
import slimeknights.tconstruct.library.tools.nbt.LazyToolStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin({ToolBuildingRecipe.class})
public class ToolBuildingRecipeMixin {

    @Inject(method = "getValidatedResult", at = @At(value = "RETURN"), remap = false)
    private void getValidatedResul(ITinkerStationContainer inv, RegistryAccess access, CallbackInfoReturnable<RecipeResult<LazyToolStack>> cir) {
        RecipeResult<LazyToolStack> stack = cir.getReturnValue();
        ToolStack tool = stack.getResult().getTool();
        tool.addModifier(ModModifierIds.affix, 1);
        tool.getPersistentData().putString(SlimeWorld.getResource("affix"), ModModifierIds.overwash.toString());
        tool.rebuildStats();
    }
}
