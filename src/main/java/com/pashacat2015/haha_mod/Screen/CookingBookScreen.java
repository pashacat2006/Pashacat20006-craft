package com.pashacat2015.haha_mod.Screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pashacat2015.haha_mod.cat_mod;
import com.pashacat2015.haha_mod.network.QuestClaimPacket;
import com.pashacat2015.haha_mod.network.QuestRequestSyncPacket;
import com.pashacat2015.haha_mod.quest.Quest;
import com.pashacat2015.haha_mod.quest.QuestRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class CookingBookScreen extends AbstractContainerScreen<CookingBookMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(cat_mod.MODID, "textures/gui/cookingbook.png");

    private static final int BUTTON_X = 14;
    private static final int BUTTON_START_Y = 28;
    private static final int BUTTON_SPACING = 54;

    private final List<Quest> quests = QuestRegistry.allSorted();
    private final List<QuestImageButton> questButtons = new ArrayList<>();
    private Quest hoveredQuest = null;

    public CookingBookScreen(CookingBookMenu menu, Inventory inv, net.minecraft.network.chat.Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.inventoryLabelY = 100000;
        this.titleLabelY = 100000;
    }

    @Override
    protected void init() {
        super.init();
        questButtons.clear();
        clearWidgets();

        int y = topPos + BUTTON_START_Y;
        for (Quest quest : quests) {
            final String questId = quest.id;

            QuestImageButton button = new QuestImageButton(
                    leftPos + BUTTON_X,
                    y,
                    quest.buttonWidth,
                    quest.buttonHeight,
                    quest.buttonTexture,
                    quest.buttonWidth,
                    quest.buttonHeight,
                    b -> QuestClaimPacket.send(questId)
            );

            button.setLocked(menu.isQuestLocked(quest));
            button.setCompleted(menu.isQuestDone(quest));
            addRenderableWidget(button);
            questButtons.add(button);
            y += BUTTON_SPACING;
        }

        QuestRequestSyncPacket.send();
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        g.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        renderBackground(g);
        super.render(g, mouseX, mouseY, partialTick);

        hoveredQuest = null;
        for (int i = 0; i < questButtons.size(); i++) {
            if (questButtons.get(i).isHovered()) {
                hoveredQuest = quests.get(i);
                break;
            }
        }

        if (hoveredQuest != null) {
            renderImageTooltip(g, hoveredQuest, mouseX, mouseY);
        }
    }

    private void renderImageTooltip(GuiGraphics g, Quest quest, int mouseX, int mouseY) {
        int x = mouseX + 12;
        int y = mouseY - quest.tooltipHeight / 2;

        if (x + quest.tooltipWidth > width) {
            x = mouseX - quest.tooltipWidth - 12;
        }
        if (y < 4) {
            y = 4;
        }
        if (y + quest.tooltipHeight > height - 4) {
            y = height - quest.tooltipHeight - 4;
        }

        g.fill(x - 2, y - 2, x + quest.tooltipWidth + 2, y + quest.tooltipHeight + 2, 0xC0101010);
        g.fill(x - 1, y - 1, x + quest.tooltipWidth + 1, y + quest.tooltipHeight + 1, 0xFF2A2118);

        RenderSystem.enableBlend();
        g.blit(quest.tooltipTexture, x, y, 0, 0, quest.tooltipWidth, quest.tooltipHeight,
                quest.tooltipWidth, quest.tooltipHeight);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (QuestImageButton questButton : questButtons) {
            if (questButton.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        for (int i = 0; i < questButtons.size(); i++) {
            Quest quest = quests.get(i);
            questButtons.get(i).setLocked(menu.isQuestLocked(quest));
            questButtons.get(i).setCompleted(menu.isQuestDone(quest));
        }
    }
}
