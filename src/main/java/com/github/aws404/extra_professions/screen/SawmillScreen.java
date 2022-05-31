package com.github.aws404.extra_professions.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import com.github.aws404.extra_professions.ExtraProfessionsMod;
import com.github.aws404.extra_professions.recipe.SawmillRecipe;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

@Environment(EnvType.CLIENT)
public class SawmillScreen extends HandledScreen<SawmillScreenHandler> {
    private static final Identifier TEXTURE = ExtraProfessionsMod.id("textures/gui/container/sawmill.png");
    private static final int SCROLLBAR_WIDTH = 12;
    private static final int SCROLLBAR_HEIGHT = 15;
    private static final int RECIPE_LIST_COLUMNS = 4;
    private static final int RECIPE_LIST_ROWS = 3;
    private static final int RECIPE_ENTRY_WIDTH = 16;
    private static final int RECIPE_ENTRY_HEIGHT = 18;
    private static final int SCROLLBAR_AREA_HEIGHT = RECIPE_ENTRY_HEIGHT * RECIPE_LIST_ROWS;
    private static final int RECIPE_LIST_OFFSET_X = 52;
    private static final int RECIPE_LIST_OFFSET_Y = 14;

    private float scrollAmount;
    private boolean mouseClicked;
    private int scrollOffset;
    private boolean canCraft;

    public SawmillScreen(SawmillScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        handler.setContentsChangedListener(this::onInventoryChange);
        --this.titleY;
        this.backgroundHeight = 178;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.renderBackground(matrices);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        this.drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        int scrollStart = (int) (41.0f * this.scrollAmount);
        this.drawTexture(matrices, this.x + 119, this.y + SCROLLBAR_HEIGHT + scrollStart, 176 + (this.shouldScroll() ? 0 : SCROLLBAR_WIDTH), 0, SCROLLBAR_WIDTH, SCROLLBAR_HEIGHT);

        int recipeIconsX = this.x + RECIPE_LIST_OFFSET_X;
        int recipeIconsY = this.y + RECIPE_LIST_OFFSET_Y;
        int scrollOffset = this.scrollOffset + SCROLLBAR_WIDTH;
        this.renderRecipeBackground(matrices, mouseX, mouseY, recipeIconsX, recipeIconsY, scrollOffset);
        this.renderRecipeIcons(recipeIconsX, recipeIconsY, scrollOffset);
    }

    @Override
    protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
        super.drawMouseoverTooltip(matrices, x, y);
        if (this.canCraft) {
            int startX = this.x + RECIPE_LIST_OFFSET_X;
            int startY = this.y + RECIPE_LIST_OFFSET_Y;
            int scrollOffset = this.scrollOffset + SCROLLBAR_WIDTH;

            List<SawmillRecipe> list = this.handler.getAvailableRecipes();
            
            for (int i = this.scrollOffset; i < scrollOffset && i < this.handler.getAvailableRecipeCount(); ++i) {
                int posIndex = i - this.scrollOffset;
                int recipeX = startX + posIndex % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
                int recipeY = startY + posIndex / RECIPE_LIST_COLUMNS * RECIPE_ENTRY_HEIGHT + 2;
                if (x < recipeX || x >= recipeX + RECIPE_ENTRY_WIDTH || y < recipeY || y >= recipeY + RECIPE_ENTRY_HEIGHT) continue;
                this.renderTooltip(matrices, list.get(i).getOutput(), x, y);
            }
        }
    }

    private void renderRecipeBackground(MatrixStack matrices, int mouseX, int mouseY, int x, int y, int scrollOffset) {
        for (int i = this.scrollOffset; i < scrollOffset && i < this.handler.getAvailableRecipeCount(); ++i) {
            int posIndex = i - this.scrollOffset;
            int recipeX = x + posIndex % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
            int recipeY = y + posIndex / RECIPE_LIST_COLUMNS * RECIPE_ENTRY_HEIGHT + 2;

            int n = 0;
            if (i == this.handler.getSelectedRecipe()) {
                n += RECIPE_ENTRY_HEIGHT;
            } else if (mouseX >= recipeX && mouseY >= recipeY && mouseX < recipeX + RECIPE_ENTRY_WIDTH && mouseY < recipeY + RECIPE_ENTRY_HEIGHT) {
                n += 36;
            }

            this.drawTexture(matrices, recipeX, recipeY - 1, 200, n, RECIPE_ENTRY_WIDTH, RECIPE_ENTRY_HEIGHT);
        }
    }

    private void renderRecipeIcons(int x, int y, int scrollOffset) {
        List<SawmillRecipe> list = this.handler.getAvailableRecipes();
        for (int i = this.scrollOffset; i < scrollOffset && i < this.handler.getAvailableRecipeCount(); ++i) {
            int posIndex = i - this.scrollOffset;
            int recipeX = x + posIndex % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH;
            int recipeY = y + posIndex / RECIPE_LIST_COLUMNS * RECIPE_ENTRY_HEIGHT + 2;
            this.client.getItemRenderer().renderInGuiWithOverrides(list.get(i).getOutput(), recipeX, recipeY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.mouseClicked = false;
        if (this.canCraft) {
            int recipeListX = this.x + RECIPE_LIST_OFFSET_X;
            int recipeListY = this.y + RECIPE_LIST_OFFSET_Y;
            int k = this.scrollOffset + SCROLLBAR_WIDTH;
            for (int i = this.scrollOffset; i < k; ++i) {
                int posIndex = i - this.scrollOffset;
                double distanceX = mouseX - (double)(recipeListX + posIndex % RECIPE_LIST_COLUMNS * RECIPE_ENTRY_WIDTH);
                double distanceY = mouseY - (double)(recipeListY + posIndex / RECIPE_LIST_COLUMNS * RECIPE_ENTRY_HEIGHT);

                if (!(distanceX >= 0.0) || !(distanceY >= 0.0) || !(distanceX < RECIPE_ENTRY_WIDTH) || !(distanceY < RECIPE_ENTRY_HEIGHT) || !this.handler.onButtonClick(this.client.player, i)) continue;

                MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0f));
                this.client.interactionManager.clickButton(this.handler.syncId, i);
                return true;
            }

            int scrollBarX = this.x + 119;
            int scrollBarY = this.y + 9;
            if (mouseX >= (double)scrollBarX && mouseX < (double)(scrollBarX + SCROLLBAR_WIDTH) && mouseY >= (double)scrollBarY && mouseY < (double)(scrollBarY + SCROLLBAR_AREA_HEIGHT)) {
                this.mouseClicked = true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.mouseClicked && this.shouldScroll()) {
            int startPos = this.y + RECIPE_LIST_OFFSET_Y;
            int endPos = startPos + SCROLLBAR_AREA_HEIGHT;
            this.scrollAmount = (float) (mouseY - startPos - 7.5f) / ((endPos - startPos) - 15.0f);
            this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0f, 1.0f);
            this.scrollOffset = (int) ((this.scrollAmount * this.getMaxScroll()) + 0.5) * RECIPE_LIST_COLUMNS;
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.shouldScroll()) {
            int maxScroll = this.getMaxScroll();
            this.scrollAmount = (float) (this.scrollAmount - (amount / maxScroll));
            this.scrollAmount = MathHelper.clamp(this.scrollAmount, 0.0f, 1.0f);
            this.scrollOffset = (int) ((this.scrollAmount * maxScroll) + 0.5) * RECIPE_LIST_COLUMNS;
        }
        return true;
    }

    private boolean shouldScroll() {
        return this.canCraft && this.handler.getAvailableRecipeCount() > 12;
    }

    protected int getMaxScroll() {
        return (this.handler.getAvailableRecipeCount() + RECIPE_LIST_COLUMNS - 1) / RECIPE_LIST_COLUMNS - RECIPE_LIST_ROWS;
    }

    private void onInventoryChange() {
        this.canCraft = this.handler.canCraft();
        if (!this.canCraft) {
            this.scrollAmount = 0.0f;
            this.scrollOffset = 0;
        }
    }
}
