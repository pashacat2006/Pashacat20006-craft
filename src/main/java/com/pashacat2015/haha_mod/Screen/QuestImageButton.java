package com.pashacat2015.haha_mod.Screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class QuestImageButton extends Button {

    private final ResourceLocation texture;
    private final int texWidth;
    private final int texHeight;
    private boolean locked;
    private boolean completed;

    public QuestImageButton(int x, int y, int width, int height, ResourceLocation texture,
                            int texWidth, int texHeight, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.texture = texture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.active = true;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
        this.active = true;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.active = true;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (!visible) {
            return;
        }

        RenderSystem.enableBlend();
        if (completed) {
            graphics.setColor(0.55F, 1.0F, 0.55F, 0.9F);
        } else if (locked) {
            graphics.setColor(0.45F, 0.45F, 0.45F, 0.85F);
        } else if (isHovered()) {
            graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            graphics.setColor(0.92F, 0.92F, 0.92F, 1.0F);
        }

        graphics.blit(texture, getX(), getY(), 0, 0, width, height, texWidth, texHeight);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!visible || button != 0) {
            return false;
        }
        if (isMouseOver(mouseX, mouseY)) {
            onPress(mouseX, mouseY);
            return true;
        }
        return false;
    }

    protected void onPress(double mouseX, double mouseY) {
        playDownSound(Minecraft.getInstance().getSoundManager());
        this.onPress.onPress(this);
    }
}
