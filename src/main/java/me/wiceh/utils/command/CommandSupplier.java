package me.wiceh.utils.command;

import dev.jorel.commandapi.CommandAPICommand;
import me.wiceh.utils.command.help.Helpable;

import java.util.function.Supplier;

public abstract class CommandSupplier implements Supplier<CommandAPICommand[]>, Helpable {
    protected final String name;

    public CommandSupplier(String name) {
        this.name = name;
    }

    protected CommandAPICommand create(String command) {
        return new CommandAPICommand(command);
    }

    public String getName() {
        return this.name;
    }
}
