package me.wiceh.utils.command.help;

public class HelpValue {
    private final String command;
    private final String description;
    private String permission;

    public HelpValue(String command, String description) {
        this.command = command;
        this.description = description;
        this.permission = null;
    }

    public HelpValue(String command, String description, String permission) {
        this(command, description);
        this.permission = permission;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCommand() {
        return this.command;
    }

    public String getPermission() {
        return this.permission;
    }
}
