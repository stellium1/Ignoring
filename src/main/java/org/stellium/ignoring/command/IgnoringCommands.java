package org.stellium.ignoring.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.stellium.ignoring.config.IgnoringConfig;

public class IgnoringCommands {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        dispatcher.register(ClientCommandManager.literal("!ignoring:togglerender")
            .executes(IgnoringCommands::toggleRender));

        dispatcher.register(ClientCommandManager.literal("!ignoring:togglechat")
            .executes(IgnoringCommands::toggleChat));

        dispatcher.register(ClientCommandManager.literal("!ignoring:toggletablist")
            .executes(IgnoringCommands::toggleTablist));

        dispatcher.register(ClientCommandManager.literal("!ignoring:toggleinteraction")
            .executes(IgnoringCommands::toggleInteraction));

        dispatcher.register(ClientCommandManager.literal("!ignoring:addignore")
            .then(ClientCommandManager.argument("player", StringArgumentType.string())
                .executes(IgnoringCommands::addIgnore)));

        dispatcher.register(ClientCommandManager.literal("!ignoring:removeignore")
            .then(ClientCommandManager.argument("player", StringArgumentType.string())
                .executes(IgnoringCommands::removeIgnore)));

        dispatcher.register(ClientCommandManager.literal("!ignoring:listignore")
            .executes(IgnoringCommands::listIgnore));

        dispatcher.register(ClientCommandManager.literal("!ignoring:transparency")
            .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(0, 255))
                .executes(IgnoringCommands::setTransparency)));

        dispatcher.register(ClientCommandManager.literal("!ignoring:reload")
            .executes(IgnoringCommands::reload));

        dispatcher.register(ClientCommandManager.literal("!ignoring:help")
            .executes(IgnoringCommands::help));
    }

    private static int toggleRender(CommandContext<FabricClientCommandSource> context) {
        IgnoringConfig config = IgnoringConfig.get();
        config.ignoreRender = !config.ignoreRender;
        saveConfig();

        context.getSource().sendFeedback(Text.translatable("text.ignoring.toggle.ignoreRender")
            .append(Text.literal(": "))
            .append(Text.translatable(config.ignoreRender ? "text.ignoring.status.enabled" : "text.ignoring.status.disabled")
                .formatted(config.ignoreRender ? Formatting.GREEN : Formatting.RED)));

        return 1;
    }

    private static int toggleChat(CommandContext<FabricClientCommandSource> context) {
        IgnoringConfig config = IgnoringConfig.get();
        config.ignoreChat = !config.ignoreChat;
        saveConfig();

        context.getSource().sendFeedback(Text.translatable("text.ignoring.toggle.ignoreChat")
            .append(Text.literal(": "))
            .append(Text.translatable(config.ignoreChat ? "text.ignoring.status.enabled" : "text.ignoring.status.disabled")
                .formatted(config.ignoreChat ? Formatting.GREEN : Formatting.RED)));

        return 1;
    }

    private static int toggleTablist(CommandContext<FabricClientCommandSource> context) {
        IgnoringConfig config = IgnoringConfig.get();
        config.ignoreTablist = !config.ignoreTablist;
        saveConfig();

        context.getSource().sendFeedback(Text.translatable("text.ignoring.toggle.ignoreTablist")
            .append(Text.literal(": "))
            .append(Text.translatable(config.ignoreTablist ? "text.ignoring.status.enabled" : "text.ignoring.status.disabled")
                .formatted(config.ignoreTablist ? Formatting.GREEN : Formatting.RED)));

        return 1;
    }

    private static int toggleInteraction(CommandContext<FabricClientCommandSource> context) {
        IgnoringConfig config = IgnoringConfig.get();
        config.interactionThroughIgnoredPlayer = !config.interactionThroughIgnoredPlayer;
        saveConfig();

        context.getSource().sendFeedback(Text.translatable("text.ignoring.toggle.interactionThroughIgnoredPlayer")
            .append(Text.literal(": "))
            .append(Text.translatable(config.interactionThroughIgnoredPlayer ? "text.ignoring.status.enabled" : "text.ignoring.status.disabled")
                .formatted(config.interactionThroughIgnoredPlayer ? Formatting.GREEN : Formatting.RED)));

        return 1;
    }

    private static int addIgnore(CommandContext<FabricClientCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "player");
        IgnoringConfig config = IgnoringConfig.get();

        if (config.ignoredPlayerList.contains(playerName)) {
            context.getSource().sendError(Text.translatable("text.ignoring.command.addignore.duplicate", playerName));
            return 0;
        }

        config.ignoredPlayerList.remove("Insert name");
        config.ignoredPlayerList.add(playerName);
        saveConfig();

        context.getSource().sendFeedback(Text.translatable("text.ignoring.command.addignore.success", playerName)
            .formatted(Formatting.GREEN));

        return 1;
    }

    private static int removeIgnore(CommandContext<FabricClientCommandSource> context) {
        String playerName = StringArgumentType.getString(context, "player");
        IgnoringConfig config = IgnoringConfig.get();

        if (!config.ignoredPlayerList.contains(playerName)) {
            context.getSource().sendError(Text.translatable("text.ignoring.command.removeignore.notfound", playerName));
            return 0;
        }

        config.ignoredPlayerList.remove(playerName);
        saveConfig();

        context.getSource().sendFeedback(Text.translatable("text.ignoring.command.removeignore.success", playerName)
            .formatted(Formatting.GREEN));

        return 1;
    }

    private static int listIgnore(CommandContext<FabricClientCommandSource> context) {
        IgnoringConfig config = IgnoringConfig.get();

        if (config.ignoredPlayerList.isEmpty() ||
            (config.ignoredPlayerList.size() == 1 && config.ignoredPlayerList.contains("Insert name"))) {
            context.getSource().sendFeedback(Text.translatable("text.ignoring.command.listignore.empty")
                .formatted(Formatting.YELLOW));
            return 1;
        }

        context.getSource().sendFeedback(Text.translatable("text.ignoring.command.listignore.header")
            .formatted(Formatting.GOLD, Formatting.BOLD));

        for (String player : config.ignoredPlayerList) {
            if (!player.equals("Insert name")) {
                context.getSource().sendFeedback(Text.literal("  - ")
                    .formatted(Formatting.GRAY)
                    .append(Text.literal(player).formatted(Formatting.WHITE)));
            }
        }

        return 1;
    }

    private static int setTransparency(CommandContext<FabricClientCommandSource> context) {
        int value = IntegerArgumentType.getInteger(context, "value");
        IgnoringConfig config = IgnoringConfig.get();
        config.transparency = value;
        saveConfig();

        context.getSource().sendFeedback(Text.translatable("text.ignoring.command.transparency.success", value)
            .formatted(Formatting.GREEN));

        return 1;
    }

    private static int reload(CommandContext<FabricClientCommandSource> context) {
        try {
            AutoConfig.getConfigHolder(IgnoringConfig.class).load();
            context.getSource().sendFeedback(Text.translatable("text.ignoring.command.reload.success")
                .formatted(Formatting.GREEN));
            return 1;
        } catch (Exception e) {
            context.getSource().sendError(Text.translatable("text.ignoring.command.reload.failed", e.getMessage()));
            return 0;
        }
    }

    private static int help(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.translatable("text.ignoring.command.help.header")
            .formatted(Formatting.GOLD, Formatting.BOLD));

        context.getSource().sendFeedback(Text.literal("!ignoring:togglerender")
            .formatted(Formatting.YELLOW)
            .append(Text.literal(" - "))
            .append(Text.translatable("text.ignoring.command.help.togglerender").formatted(Formatting.GRAY)));

        context.getSource().sendFeedback(Text.literal("!ignoring:togglechat")
            .formatted(Formatting.YELLOW)
            .append(Text.literal(" - "))
            .append(Text.translatable("text.ignoring.command.help.togglechat").formatted(Formatting.GRAY)));

        context.getSource().sendFeedback(Text.literal("!ignoring:toggletablist")
            .formatted(Formatting.YELLOW)
            .append(Text.literal(" - "))
            .append(Text.translatable("text.ignoring.command.help.toggletablist").formatted(Formatting.GRAY)));

        context.getSource().sendFeedback(Text.literal("!ignoring:toggleinteraction")
            .formatted(Formatting.YELLOW)
            .append(Text.literal(" - "))
            .append(Text.translatable("text.ignoring.command.help.toggleinteraction").formatted(Formatting.GRAY)));

        context.getSource().sendFeedback(Text.literal("!ignoring:addignore <player>")
            .formatted(Formatting.YELLOW)
            .append(Text.literal(" - "))
            .append(Text.translatable("text.ignoring.command.help.addignore").formatted(Formatting.GRAY)));

        context.getSource().sendFeedback(Text.literal("!ignoring:removeignore <player>")
            .formatted(Formatting.YELLOW)
            .append(Text.literal(" - "))
            .append(Text.translatable("text.ignoring.command.help.removeignore").formatted(Formatting.GRAY)));

        context.getSource().sendFeedback(Text.literal("!ignoring:listignore")
            .formatted(Formatting.YELLOW)
            .append(Text.literal(" - "))
            .append(Text.translatable("text.ignoring.command.help.listignore").formatted(Formatting.GRAY)));

        context.getSource().sendFeedback(Text.literal("!ignoring:transparency <0-255>")
            .formatted(Formatting.YELLOW)
            .append(Text.literal(" - "))
            .append(Text.translatable("text.ignoring.command.help.transparency").formatted(Formatting.GRAY)));

        context.getSource().sendFeedback(Text.literal("!ignoring:reload")
            .formatted(Formatting.YELLOW)
            .append(Text.literal(" - "))
            .append(Text.translatable("text.ignoring.command.help.reload").formatted(Formatting.GRAY)));

        context.getSource().sendFeedback(Text.literal("!ignoring:help")
            .formatted(Formatting.YELLOW)
            .append(Text.literal(" - "))
            .append(Text.translatable("text.ignoring.command.help.help").formatted(Formatting.GRAY)));

        return 1;
    }

    private static void saveConfig() {
        AutoConfig.getConfigHolder(IgnoringConfig.class).save();
    }
}
