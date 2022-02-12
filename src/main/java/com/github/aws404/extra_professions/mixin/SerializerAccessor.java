package com.github.aws404.extra_professions.mixin;

import net.minecraft.recipe.CuttingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CuttingRecipe.Serializer.class)
public interface SerializerAccessor {
    @Invoker("<init>")
    static <T extends CuttingRecipe> CuttingRecipe.Serializer<T> createSerializer(CuttingRecipe.Serializer.RecipeFactory<T> recipeFactory) {
        throw new UnsupportedOperationException();
    }
}
