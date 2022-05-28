package com.github.aws404.extra_professions.recipe;

import com.github.aws404.extra_professions.block.ExtraBlocks;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SawmillRecipe extends CuttingRecipe {
    public SawmillRecipe(Identifier id, String group, Ingredient input, ItemStack output) {
        super(ExtraRecipeTypes.SAWMILL_RECIPE_TYPE, ExtraRecipeTypes.SAWMILL_RECIPE_SERIALISER, id, group, input, output);
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return this.input.test(inventory.getStack(0));
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ExtraBlocks.SAWMILL_ITEM);
    }

    public static class Serialiser extends CuttingRecipe.Serializer<SawmillRecipe> {
        public Serialiser() {
            super(SawmillRecipe::new);
        }
    }
}
