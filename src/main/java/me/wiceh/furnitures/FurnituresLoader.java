package me.wiceh.furnitures;

import org.bukkit.plugin.java.JavaPlugin;

public class FurnituresLoader extends JavaPlugin {
    private final FurnituresPlugin plugin;

    public FurnituresLoader() {
        this.plugin = new FurnituresPlugin(this);
    }

    @Override
    public void onEnable() {
        this.plugin.enable();
    }

    @Override
    public void onDisable() {
        this.plugin.disable();
    }
}
