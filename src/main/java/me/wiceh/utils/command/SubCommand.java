package me.wiceh.utils.command;

import com.google.common.collect.Maps;
import dev.jorel.commandapi.CommandAPICommand;
import me.wiceh.utils.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

public abstract class SubCommand<T extends JavaPlugin> extends CommandAPICommand {
    protected final int coolDown;
    private final Map<UUID, Long> cooldownMap;
    protected T plugin;

    protected SubCommand(T plugin, String name, int coolDown) {
        super(name);
        this.cooldownMap = Maps.newHashMap();
        this.plugin = plugin;
        this.coolDown = coolDown;
        this.init();
        this.setup();
    }

    protected SubCommand(T plugin, String name) {
        this(plugin, name, -1);
    }

    protected void init() {
    }

    protected abstract void setup();

    protected boolean coolDownCheck(Player player) {
        UUID playerUUID = player.getUniqueId();
        long currentMillis = System.currentTimeMillis();
        Long coolDownMillis = this.cooldownMap.get(playerUUID);
        if (coolDownMillis == null) {
            this.cooldownMap.put(playerUUID, currentMillis + (long) this.coolDown * 1000L);
            return false;
        } else if (currentMillis > coolDownMillis) {
            player.sendMessage(text("\n §cᴄᴏᴏʟᴅᴏᴡɴ \n §7Aspetta §f" + Utils.formatSeconds(coolDownMillis - currentMillis) + " §7prima di eseguire di nuovo il comando.\n"));
            return true;
        } else {
            this.cooldownMap.remove(playerUUID);
            return false;
        }
    }
}
