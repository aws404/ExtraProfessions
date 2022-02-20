package com.github.aws404.extra_professions.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.client.gui.screen.recipebook.AbstractFurnaceRecipeBookScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Set;

@Environment(EnvType.CLIENT)
public class AnnealerScreen extends AbstractFurnaceScreen<AnnealerScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("textures/gui/container/furnace.png");

    public AnnealerScreen(AnnealerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, new RecipeBookScreen(), inventory, title, TEXTURE);
    }

    protected static class RecipeBookScreen extends AbstractFurnaceRecipeBookScreen {
        private static final Text TOGGLE_SMELTABLE_RECIPES_TEXT = new TranslatableText("gui.recipebook.toggleRecipes.anneal");

        @Override
        protected Text getToggleCraftableButtonText() {
            return TOGGLE_SMELTABLE_RECIPES_TEXT;
        }

        @Override
        protected Set<Item> getAllowedFuels() {
            return AbstractFurnaceBlockEntity.createFuelTimeMap().keySet();
        }

    }
}
