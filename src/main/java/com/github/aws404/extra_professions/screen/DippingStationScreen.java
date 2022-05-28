package com.github.aws404.extra_professions.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import com.github.aws404.extra_professions.ExtraProfessionsMod;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class DippingStationScreen extends HandledScreen<DippingStationScreenHandler> {
    private static final Identifier TEXTURE = ExtraProfessionsMod.id("textures/gui/container/dipping_station.png");

    public DippingStationScreen(DippingStationScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        --this.titleY;
        this.backgroundHeight = 151;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.textRenderer.draw(matrices, Text.translatable("container.dipping_station.max_candles", this.handler.getMaxOutput()), this.x + DippingStationScreenHandler.WAX_SLOT_POS.getFirst() + 20, this.y + DippingStationScreenHandler.WAX_SLOT_POS.getSecond() + 8, 6710886);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
        super.drawMouseoverTooltip(matrices, x, y);

        Slot waxSlot = this.handler.getSlot(DippingStationScreenHandler.WAX_SLOT);

        if (this.focusedSlot == waxSlot && !waxSlot.hasStack() && this.handler.getCursorStack().isEmpty()) {
            this.renderTooltip(matrices, List.of(Text.translatable("container.dipping_station.wax_slot")), x, y);
        }
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        this.drawTexture(matrices, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);

        if (!this.handler.getSlot(DippingStationScreenHandler.WAX_SLOT).hasStack()) {
            this.drawTexture(matrices, this.x + DippingStationScreenHandler.WAX_SLOT_POS.getFirst(), this.y + DippingStationScreenHandler.WAX_SLOT_POS.getSecond(), 176, 0, 16, 16);
        }

        if (!this.handler.getSlot(DippingStationScreenHandler.WICK_SLOT).hasStack()) {
            this.drawTexture(matrices, this.x + DippingStationScreenHandler.WICK_SLOT_POS.getFirst(), this.y + DippingStationScreenHandler.WICK_SLOT_POS.getSecond(), 176, 16, 16, 16);
        }

        if (!this.handler.getSlot(DippingStationScreenHandler.DYE_SLOT).hasStack()) {
            this.drawTexture(matrices, this.x + DippingStationScreenHandler.DYE_SLOT_POS.getFirst(), this.y + DippingStationScreenHandler.DYE_SLOT_POS.getSecond(), 176, 32, 16, 16);
        }
    }
}
