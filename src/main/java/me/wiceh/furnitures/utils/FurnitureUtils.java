package me.wiceh.furnitures.utils;

import me.wiceh.furnitures.objects.Furniture;
import me.wiceh.utils.utils.PersistentDataUtils;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class FurnitureUtils {
    private static final String FURNITURE_KEY = "furniture";

    public static void setFurnitureEntity(Entity entity, Furniture furniture) {
        PersistentDataUtils.set(entity, FURNITURE_KEY, PersistentDataType.STRING, furniture.id());
    }

    public static void setFurnitureItem(ItemStack itemStack, Furniture furniture) {
        PersistentDataUtils.set(itemStack, FURNITURE_KEY, PersistentDataType.STRING, furniture.id());
    }

    public static String getFurnitureItemKey(ItemStack itemStack) {
        return PersistentDataUtils.get(itemStack, FURNITURE_KEY, PersistentDataType.STRING);
    }

    public static String getFurnitureEntityKey(Entity entity) {
        return PersistentDataUtils.get(entity, FURNITURE_KEY, PersistentDataType.STRING);
    }

    public static boolean isFurnitureEntity(Entity entity) {
        return getFurnitureEntityKey(entity) != null;
    }
}
