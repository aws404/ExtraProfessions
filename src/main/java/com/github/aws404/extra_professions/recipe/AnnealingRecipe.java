package com.github.aws404.extra_professions.recipe;

import com.github.aws404.extra_professions.ExtraProfessionsMod;
import com.github.aws404.extra_professions.block.ExtraBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;

public class AnnealingRecipe extends AbstractCookingRecipe {
    public AnnealingRecipe(Identifier id, String group, Ingredient input, ItemStack output, float experience, int cookTime) {
        super(ExtraProfessionsMod.ANNEALING_RECIPE_TYPE, id, group, input, output, experience, cookTime);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ExtraProfessionsMod.ANNEALING_RECIPE_SERIALISER;
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ExtraBlocks.ANNEALER_ITEM);
    }

}
