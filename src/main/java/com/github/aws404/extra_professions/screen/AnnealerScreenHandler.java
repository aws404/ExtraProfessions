package com.github.aws404.extra_professions.screen;

import com.github.aws404.extra_professions.ExtraProfessionsMod;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;

public class AnnealerScreenHandler extends AbstractFurnaceScreenHandler {
    public AnnealerScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ExtraScreenHandlers.LEHR_SCREEN_HANDLER, ExtraProfessionsMod.ANNEALING_RECIPE_TYPE, ExtraProfessionsMod.ANNEALING_RECIPE_BOOK_CATEGORY, syncId, playerInventory);
    }

    public AnnealerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(ExtraScreenHandlers.LEHR_SCREEN_HANDLER, ExtraProfessionsMod.ANNEALING_RECIPE_TYPE, ExtraProfessionsMod.ANNEALING_RECIPE_BOOK_CATEGORY, syncId, playerInventory, inventory, propertyDelegate);
    }
}
