package me.wiceh.utils.utils;

import com.google.common.collect.Maps;
import me.wiceh.furnitures.Furnitures;
import org.bukkit.NamespacedKey;
import org.bukkit.block.TileState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class PersistentDataUtils {
    private static final Map<String, NamespacedKey> KEYS = Maps.newHashMap();
    private static final Plugin PLUGIN;

    private PersistentDataUtils() {
    }

    public static NamespacedKey getOrCreateKey(String key) {
        return KEYS.computeIfAbsent(key, (k) -> new NamespacedKey(PLUGIN, k));
    }

    public static <T, Z> void set(PersistentDataHolder dataHolder, NamespacedKey key, PersistentDataType<T, Z> dataType, Z value) {
        if (dataHolder == null) {
            PLUGIN.getLogger().warning(() -> "Tried to set " + key + " -> " + value + " to a null entity!");
        } else {
            dataHolder.getPersistentDataContainer().set(key, dataType, value);
            if (dataHolder instanceof TileState) {
                TileState tileState = (TileState) dataHolder;
                tileState.update();
            }

        }
    }

    public static <T, Z> void transfer(PersistentDataHolder from, PersistentDataHolder to, NamespacedKey key, PersistentDataType<T, Z> dataType) {
        if (from != null && to != null) {
            Z value = get(from, key, dataType);
            set(to, key, dataType, value);
        } else {
            PLUGIN.getLogger().warning(() -> "Tried to transfer " + key + " from " + from + " to " + to + "!");
        }
    }

    public static <T, Z> void set(ItemStack itemStack, NamespacedKey key, PersistentDataType<T, Z> dataType, Z value) {
        if (itemStack == null) {
            PLUGIN.getLogger().warning(() -> "Tried to set " + key + " -> " + value + " to a null item!");
        } else {
            ItemMeta meta = itemStack.getItemMeta();
            set(meta, key, dataType, value);
            itemStack.setItemMeta(meta);
        }
    }

    public static <T, Z> void set(ItemStack itemStack, String key, PersistentDataType<T, Z> dataType, Z value) {
        set(itemStack, getOrCreateKey(key), dataType, value);
    }

    public static <T, Z> void set(PersistentDataHolder dataHolder, String key, PersistentDataType<T, Z> dataType, Z value) {
        set(dataHolder, getOrCreateKey(key), dataType, value);
    }

    public static void remove(PersistentDataHolder dataHolder, NamespacedKey key) {
        if (dataHolder != null) {
            dataHolder.getPersistentDataContainer().remove(key);
            if (dataHolder instanceof TileState) {
                TileState tileState = (TileState) dataHolder;
                tileState.update();
            }

        }
    }

    public static void remove(PersistentDataHolder dataHolder, String key) {
        remove(dataHolder, getOrCreateKey(key));
    }

    public static void remove(ItemStack itemStack, NamespacedKey key) {
        if (itemStack != null) {
            ItemMeta meta = itemStack.getItemMeta();
            remove(meta, key);
            itemStack.setItemMeta(meta);
        }
    }

    public static void remove(ItemStack itemStack, String key) {
        remove(itemStack, getOrCreateKey(key));
    }

    public static <T, Z> @Nullable Z get(ItemStack itemStack, NamespacedKey key, PersistentDataType<T, Z> dataType) {
        return itemStack == null ? null : get(itemStack.getItemMeta(), key, dataType);
    }

    public static <T, Z> @Nullable Z get(PersistentDataHolder dataHolder, NamespacedKey key, PersistentDataType<T, Z> dataType) {
        return dataHolder == null ? null : dataHolder.getPersistentDataContainer().get(key, dataType);
    }

    public static <T, Z> @Nullable Z get(ItemStack itemStack, String key, PersistentDataType<T, Z> dataType) {
        return get(itemStack, getOrCreateKey(key), dataType);
    }

    public static <T, Z> @Nullable Z get(PersistentDataHolder dataHolder, String key, PersistentDataType<T, Z> dataType) {
        return get(dataHolder, getOrCreateKey(key), dataType);
    }

    static {
        PLUGIN = JavaPlugin.getPlugin(Furnitures.class);
    }
}
