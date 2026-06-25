package com.pashacat2015.haha_mod.Screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pashacat2015.haha_mod.cat_mod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Клиентский экран GUI стола готовки.
 * Отображает текстуру фона, слоты и стрелку прогресса.
 */
public class FriteuseScreen extends AbstractContainerScreen<FriteuseMenu> {
    public FriteuseScreen(FriteuseMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation(cat_mod.MODID, "textures/gui/friteuse.png");
    private static final ResourceLocation FURNACE_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");

    @Override
    protected void init() {
        super.init();
        // Скрываем стандартные подписи — они уже нарисованы на текстуре
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    /** Отрисовка фона GUI и стрелки прогресса */
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth  * 2);
        int y = (height - imageHeight - 65);

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth + 50, imageHeight + 50);

        renderProgressArrow(guiGraphics, x, y);
    }

    /** Анимированная стрелка прогресса (как в печи) */
    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if (menu.isCrafting()) {
            int arrowWidth = menu.getScaledProgress();
            if (arrowWidth > 0) {
                guiGraphics.blit(FURNACE_TEXTURE, x + 104, y + 62, 176, 14, arrowWidth, 16);
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
