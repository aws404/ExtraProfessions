package com.github.aws404.extra_professions.recipe;

import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.github.aws404.extra_professions.ExtraProfessionsMod.id;

public class ExtraRecipeTypes {
    public static final RecipeType<SawmillRecipe> SAWMILL_RECIPE_TYPE = registerRecipeType(id("sawmill"));
    public static final RecipeType<AnnealingRecipe> ANNEALING_RECIPE_TYPE = registerRecipeType(id("annealing"));

    public static final RecipeSerializer<SawmillRecipe> SAWMILL_RECIPE_SERIALISER = Registry.register(Registry.RECIPE_SERIALIZER, id("sawmill"), new SawmillRecipe.Serialiser());
    public static final RecipeSerializer<AnnealingRecipe> ANNEALING_RECIPE_SERIALISER = Registry.register(Registry.RECIPE_SERIALIZER, id("annealing"), new CookingRecipeSerializer<>(AnnealingRecipe::new, 100));

    public static void init() {
    }

    private static <T extends Recipe<?>> RecipeType<T> registerRecipeType(Identifier id) {
        String sId = id.toString();
        return Registry.register(Registry.RECIPE_TYPE, id, new RecipeType<>() {
            @Override
            public String toString() {
                return sId;
            }
        });
    }
}
