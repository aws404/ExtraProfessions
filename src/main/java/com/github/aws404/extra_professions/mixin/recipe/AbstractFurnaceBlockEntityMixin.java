package com.github.aws404.extra_professions.mixin.recipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.collection.DefaultedList;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {

    @Inject(method = "craftRecipe", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;increment(I)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void extra_professions_fixRecipeOutputIncrement(Recipe<?> recipe, DefaultedList<ItemStack> slots, int count, CallbackInfoReturnable<Boolean> cir, ItemStack input, ItemStack recipeOutput, ItemStack currentOutput) {
        currentOutput.increment(recipeOutput.getCount());
    }

    @Coerce
    @Redirect(method = "craftRecipe", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;increment(I)V"))
    private static void extra_professions_voidDefaultBehaviour(ItemStack stack, int amount) {
    }
}
