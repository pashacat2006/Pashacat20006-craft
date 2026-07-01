package com.pashacat2015.haha_mod.quest;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.function.Predicate;

public class Quest {

    public final String id;
    public final int priority;
    public final Component title;
    public final Component description;
    public final List<String> rewardRecipeIds;
    public final Predicate<ServerPlayer> condition;
    public final ResourceLocation buttonTexture;
    public final ResourceLocation tooltipTexture;
    public final int buttonWidth;
    public final int buttonHeight;
    public final int tooltipWidth;
    public final int tooltipHeight;

    public Quest(String id, int priority, Component title, Component description,
                 List<String> rewardRecipeIds, Predicate<ServerPlayer> condition,
                 ResourceLocation buttonTexture, ResourceLocation tooltipTexture,
                 int buttonWidth, int buttonHeight, int tooltipWidth, int tooltipHeight) {
        this.id = id;
        this.priority = priority;
        this.title = title;
        this.description = description;
        this.rewardRecipeIds = rewardRecipeIds;
        this.condition = condition;
        this.buttonTexture = buttonTexture;
        this.tooltipTexture = tooltipTexture;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
        this.tooltipWidth = tooltipWidth;
        this.tooltipHeight = tooltipHeight;
    }
}
