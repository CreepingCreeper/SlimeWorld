//package com.creeping_creeper.slimeworld.mixins;
//
//import com.creeping_creeper.slimeworld.library.AffixUtil;
//import net.minecraft.core.RegistryAccess;
//import net.minecraft.world.item.ItemStack;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//import slimeknights.tconstruct.library.recipe.RecipeResult;
//import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
//import slimeknights.tconstruct.library.recipe.tinkerstation.building.ToolBuildingRecipe;
//import slimeknights.tconstruct.library.tools.nbt.LazyToolStack;
//import slimeknights.tconstruct.library.tools.nbt.ToolStack;
//
//@Mixin({ToolBuildingRecipe.class})
//public class ToolBuildingRecipeMixin {
//
//    @Inject(method = "getValidatedResult", at = @At(value = "RETURN"), remap = false)
//    private void getValidatedResult(ITinkerStationContainer inv, RegistryAccess access, CallbackInfoReturnable<RecipeResult<LazyToolStack>> cir) {
//        LazyToolStack lazyStack = cir.getReturnValue().getResult();
//        ItemStack stack = lazyStack.getStack();
//        ToolStack tool = lazyStack.getTool();
//        AffixUtil.addAffix(tool, stack, false);
//    }
//}
