package me.wiceh.furnitures.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class FurnitureSeatUtils {
    private static final NamespacedKey FURNITURE_SEAT_KEY = new NamespacedKey("furnitures", "furniture-seat");

    public static void setFurnitureSeat(Interaction seatEntity, ItemDisplay display) {
        seatEntity.getPersistentDataContainer().set(FURNITURE_SEAT_KEY, PersistentDataType.STRING, display.getUniqueId().toString());
    }

    public static boolean isFurnitureSeat(Interaction seatEntity) {
        return seatEntity.getPersistentDataContainer().has(FURNITURE_SEAT_KEY, PersistentDataType.STRING);
    }

    public static Optional<UUID> getFurnitureEntityFromSeat(Interaction seatEntity) {
        if (!isFurnitureSeat(seatEntity)) return Optional.empty();
        PersistentDataContainer pdc = seatEntity.getPersistentDataContainer();
        return Optional.of(UUID.fromString(Objects.requireNonNull(pdc.get(FURNITURE_SEAT_KEY,
                PersistentDataType.STRING))));
    }

    public static Set<Interaction> getSeatsByFurniture(ItemDisplay display) {
        UUID parentUuid = display.getUniqueId();
        Set<Interaction> seats = new HashSet<>();

        for (Interaction seatEntity : display.getWorld().getEntitiesByClass(Interaction.class)) {
            if (!isFurnitureSeat(seatEntity)) continue;

            UUID uuid = getFurnitureEntityFromSeat(seatEntity).orElse(null);
            if (uuid != null && parentUuid.toString().equals(uuid.toString())) {
                seats.add(seatEntity);
            }
        }

        return seats;
    }
}
