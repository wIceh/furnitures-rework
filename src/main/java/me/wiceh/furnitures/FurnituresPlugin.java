package me.wiceh.furnitures;

import dev.jorel.commandapi.CommandAPICommand;
import me.wiceh.furnitures.commands.FurnitureCommand;
import me.wiceh.furnitures.listeners.FurnitureListener;
import me.wiceh.furnitures.managers.FurnituresManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class FurnituresPlugin {

    private final JavaPlugin plugin;
    private final FurnituresManager furnituresManager;

    public FurnituresPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
        this.furnituresManager = new FurnituresManager(this);
    }

    public void enable() {
        plugin.saveDefaultConfig();

        registerCommands();
        registerListeners();
    }

    public void disable() {
    }

    private void registerCommands() {
        CommandAPICommand[] furnitureCommands = new FurnitureCommand(this).get();
        for (CommandAPICommand command : furnitureCommands) {
            command.register();
        }
    }

    private void registerListeners() {
        plugin.getServer().getPluginManager().registerEvents(new FurnitureListener(this), plugin);
    }

    public FurnituresManager getFurnituresManager() {
        return furnituresManager;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
