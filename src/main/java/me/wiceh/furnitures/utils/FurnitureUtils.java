package me.wiceh.furnitures.utils;

import me.wiceh.furnitures.objects.Furniture;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class FurnitureUtils {
    private static final NamespacedKey FURNITURE_KEY = new NamespacedKey("furnitures", "furniture");

    public static void setFurnitureEntity(Entity entity, Furniture furniture) {
        entity.getPersistentDataContainer().set(FURNITURE_KEY, PersistentDataType.STRING, furniture.id());
    }

    public static void setFurnitureItem(ItemStack itemStack, Furniture furniture) {
        itemStack.editMeta(meta -> {
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(FURNITURE_KEY, PersistentDataType.STRING, furniture.id());
        });
    }

    public static String getFurnitureItemKey(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null || !itemStack.hasItemMeta()) return null;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.get(FURNITURE_KEY, PersistentDataType.STRING);
    }

    public static String getFurnitureEntityKey(Entity entity) {
        return entity.getPersistentDataContainer().get(FURNITURE_KEY, PersistentDataType.STRING);
    }

    public static boolean isFurnitureEntity(Entity entity) {
        return getFurnitureEntityKey(entity) != null;
    }
}
