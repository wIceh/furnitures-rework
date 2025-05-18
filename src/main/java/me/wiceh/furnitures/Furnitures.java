package me.wiceh.furnitures;

import dev.jorel.commandapi.CommandAPICommand;
import me.wiceh.furnitures.commands.FurnitureCommand;
import me.wiceh.furnitures.listeners.FurnitureListener;
import me.wiceh.furnitures.managers.FurnituresManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Furnitures extends JavaPlugin {

    private FurnituresManager furnituresManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.furnituresManager = new FurnituresManager(this);

        registerCommands();
        registerListeners();
    }

    private void registerCommands() {
        CommandAPICommand[] furnitureCommands = new FurnitureCommand(this).get();
        for (CommandAPICommand command : furnitureCommands) {
            command.register();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new FurnitureListener(this), this);
    }

    public FurnituresManager getFurnituresManager() {
        return furnituresManager;
    }
}
