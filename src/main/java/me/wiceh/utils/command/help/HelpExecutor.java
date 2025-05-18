package me.wiceh.utils.command.help;

import dev.jorel.commandapi.executors.CommandArguments;
import dev.jorel.commandapi.executors.CommandExecutor;
import me.wiceh.utils.command.CommandSupplier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class HelpExecutor implements CommandExecutor {
    private final CommandSupplier command;

    public HelpExecutor(CommandSupplier command) {
        this.command = command;
    }

    public void run(CommandSender sender, CommandArguments args) {
        List<HelpValue> helpList = this.command.getHelp();
        if (helpList.isEmpty()) {
            throw new IllegalStateException("Setup values first.");
        } else {
            sender.sendMessage(text("\n §6ʟɪѕᴛᴀ §lᴄᴏᴍᴀɴᴅɪ §7(/" + command.getName() + ")"));

            for (HelpValue value : helpList) {
                if (value.getPermission() == null || sender.hasPermission(value.getPermission())) {
                    TextComponent component = text(" §8▪ §e/" + command.getName() + " " + value.getCommand())
                            .hoverEvent(HoverEvent.showText(text("§e" + value.getDescription()).decorate(TextDecoration.ITALIC)));
                    sender.sendMessage(component.clickEvent(ClickEvent.clickEvent(
                            ClickEvent.Action.SUGGEST_COMMAND, "/" + command.getName() + " " + value.getCommand())));
                }
            }

            sender.sendMessage(Component.newline());
        }
    }
}
