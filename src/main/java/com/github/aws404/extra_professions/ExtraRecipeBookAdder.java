package com.github.aws404.extra_professions;

import com.github.aws404.booking_it.RecipeBookAdder;
import com.github.aws404.extra_professions.recipe.AnnealingRecipe;
import com.github.aws404.extra_professions.recipe.SawmillRecipe;

import java.util.List;

public class ExtraRecipeBookAdder implements RecipeBookAdder {
    @Override
    public List<RecipeCategoryOptions> getCategories() {
        return List.of(
                RecipeBookAdder.builder("ANNEALING")
                        .addGroup("ALL", recipe -> recipe instanceof AnnealingRecipe, "minecraft:glass")
                        .build(),
                RecipeBookAdder.builder("SAWING")
                        .addGroup("ALL", recipe -> recipe instanceof SawmillRecipe, "minecraft:oak_planks")
                        .build()
        );
    }
}
