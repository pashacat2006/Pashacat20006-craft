package com.pashacat2015.haha_mod.events;

import com.pashacat2015.haha_mod.cat_mod;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = cat_mod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KillEvent {

    private static final Map<UUID, Map<String, Integer>> KILLS = new HashMap<>();

    public static final List<String> MOB_NAMES = List.of("zombie", "skeleton", "creeper", "spider");

    @SubscribeEvent
    public static void onMobKill(LivingDeathEvent event) {
        if (event.getEntity().level().isClientSide()) {
            return;
        }

        ServerPlayer player = getKillerPlayer(event);
        if (player == null) {
            return;
        }

        String mobId = ForgeRegistries.ENTITY_TYPES.getKey(event.getEntity().getType()).toString();
        addMobKill(player, mobId);
    }

    public static int getMob(ServerPlayer player, String mobId) {
        return getKillsMap(player).getOrDefault(mobId, 0);
    }

    public static void addMobKill(ServerPlayer player, String mobId) {
        Map<String, Integer> kills = getKillsMap(player);
        kills.put(mobId, kills.getOrDefault(mobId, 0) + 1);
    }

    private static Map<String, Integer> getKillsMap(ServerPlayer player) {
        return KILLS.computeIfAbsent(player.getUUID(), id -> new HashMap<>());
    }

    private static ServerPlayer getKillerPlayer(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer serverPlayer) {
            return serverPlayer;
        }
        if (event.getSource().getDirectEntity() instanceof Projectile projectile
                && projectile.getOwner() instanceof ServerPlayer serverPlayer) {
            return serverPlayer;
        }
        return null;
    }
}
