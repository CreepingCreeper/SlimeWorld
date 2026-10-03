package com.creeping_creeper.slimeworld.mixins;

import com.creeping_creeper.slimeworld.library.AffixUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import slimeknights.tconstruct.library.recipe.casting.ICastingContainer;
import slimeknights.tconstruct.library.recipe.casting.material.ToolCastingRecipe;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mixin({ToolCastingRecipe.class})
public class ToolCastingRecipeMixin {

    @Inject(method = "assemble(Lslimeknights/tconstruct/library/recipe/casting/ICastingContainer;Lnet/minecraft/core/RegistryAccess;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "RETURN", ordinal = 1), remap = false)
    private void getValidatedResult(ICastingContainer inv, RegistryAccess access, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack stack = cir.getReturnValue();
        ToolStack tool = ToolStack.from(stack);
        AffixUtil.addAffix(tool, stack);
    }
}
