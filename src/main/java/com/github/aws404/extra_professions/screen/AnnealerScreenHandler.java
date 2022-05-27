package com.github.aws404.extra_professions.screen;

import com.github.aws404.booking_it.BookingIt;
import com.github.aws404.extra_professions.recipe.ExtraRecipeTypes;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class AnnealerScreenHandler extends AbstractFurnaceScreenHandler {
    private static final RecipeBookCategory ANNEALING_RECIPE_BOOK_CATEGORY = BookingIt.getCategory("ANNEALING");

    public AnnealerScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ExtraScreenHandlers.ANNEALER, ExtraRecipeTypes.ANNEALING_RECIPE_TYPE, ANNEALING_RECIPE_BOOK_CATEGORY, syncId, playerInventory);
    }

    public AnnealerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ExtraScreenHandlers.ANNEALER, ExtraRecipeTypes.ANNEALING_RECIPE_TYPE, ANNEALING_RECIPE_BOOK_CATEGORY, syncId, playerInventory, inventory, propertyDelegate);
    }
}
