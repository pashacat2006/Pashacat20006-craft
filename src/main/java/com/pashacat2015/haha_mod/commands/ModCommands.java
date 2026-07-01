package com.pashacat2015.haha_mod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.pashacat2015.haha_mod.cat_mod;
import com.pashacat2015.haha_mod.events.KillEvent;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = cat_mod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommands {

    private static final SuggestionProvider<CommandSourceStack> MOB_SUGGESTIONS = (ctx, builder) -> {
        for (String mob : KillEvent.MOB_NAMES) {
            if (mob.startsWith(builder.getRemaining().toLowerCase())) {
                builder.suggest(mob);
            }
        }
        return builder.buildFuture();
    };
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commands = event.getDispatcher();

        commands.register(
                Commands.literal("kills")
                        .executes(ctx -> {
                            ServerPlayer player = ctx.getSource().getPlayerOrException();
                            int count = KillEvent.getMob(player, "minecraft:zombie");
                            ctx.getSource().sendSuccess(
                                    () -> Component.literal("Зомби: " + count),
                                    false
                            );
                            return 1;
                        })
                        .then(Commands.argument("mobname", StringArgumentType.word())
                                .suggests(MOB_SUGGESTIONS)
                                .executes(ctx -> {
                                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                                    String mobArg = StringArgumentType.getString(ctx, "mobname");
                                    final String mobId = mobArg.contains(":") ? mobArg : "minecraft:" + mobArg;
                                    final int count = KillEvent.getMob(player, mobId);
                                    ctx.getSource().sendSuccess(
                                            () -> Component.literal(mobId + ": " + count),
                                            false
                                    );
                                    return 1;
                                })
                        )
        );
    }
}
