package com.pashacat2015.haha_mod.quest;

import com.pashacat2015.haha_mod.cat_mod;
import com.pashacat2015.haha_mod.events.KillEvent;
import com.pashacat2015.haha_mod.init.itemMain;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.*;

public class QuestRegistry {

    private static final int BUTTON_SIZE = 28;
    private static final int TOOLTIP_W = 48;
    private static final int TOOLTIP_H = 48;

    private static final Map<String, Quest> QUESTS = new LinkedHashMap<>();

    static {
        register(new Quest(
                "quest_culith",
                1,
                Component.translatable("quest.haha_mod.culith.title"),
                Component.translatable("quest.haha_mod.culith.desc"),
                List.of("cubesugar_from_culith"),
                player -> player.getInventory().countItem(itemMain.EGG.get()) >= 64 && player.getInventory().countItem(itemMain.SANDWITH.get()) >= 64 ,
                texture("buttons/quest_culith.png"),
                texture("tooltips/quest_culith.png"),
                BUTTON_SIZE, BUTTON_SIZE, TOOLTIP_W, TOOLTIP_H
        ));
        register(new Quest(
                "quest_barbiqui",
                2,
                Component.translatable("quest.haha_mod.barbiqui.title"),
                Component.translatable("quest.haha_mod.barbiqui.desc"),
                List.of("barbequi_from_cookingtable"),
                player -> player.getInventory().countItem(itemMain.CORA.get()) >= 64 && player.getInventory().countItem(Items.COAL) >= 64 && player.getInventory().countItem(Items.BEEF) >= 64 ,
                texture("buttons/quest_barbiqui.png"),
                texture("tooltips/quest_barbiqui.png"),
                BUTTON_SIZE, BUTTON_SIZE, TOOLTIP_W, TOOLTIP_H
        ));
        register(new Quest(
                "quest_frenchfrice",
                1,
                Component.translatable("quest.haha_mod.frenchfrice.title"),
                Component.translatable("quest.haha_mod.frenchfrice.desc"),
                List.of("frenchfrice_from_friteuse"),
                player -> player.getInventory().countItem(itemMain.CUBESUGAR.get()) >= 64
                        && KillEvent.getMob(player, "minecraft:zombie") >= 50,
                texture("buttons/quest_frenchfrice.png"),
                texture("tooltips/quest_frenchfrice.png"),
                BUTTON_SIZE, BUTTON_SIZE, TOOLTIP_W, TOOLTIP_H
        ));
    }

    private static ResourceLocation texture(String path) {
        return new ResourceLocation(cat_mod.MODID, "textures/gui/cookingbook/" + path);
    }

    private static void register(Quest quest) {
        QUESTS.put(quest.id, quest);
    }

    public static Quest get(String id) {
        return QUESTS.get(id);
    }

    public static List<Quest> allSorted() {
        return QUESTS.values().stream()
                .sorted(Comparator.comparingInt(q -> q.priority))
                .toList();
    }
}
