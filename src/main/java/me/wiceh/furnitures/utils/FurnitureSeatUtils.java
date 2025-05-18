package me.wiceh.furnitures.utils;

import me.wiceh.utils.utils.PersistentDataUtils;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class FurnitureSeatUtils {
    private static final String FURNITURE_SEAT_KEY = "furniture-seat";

    public static void setFurnitureSeat(Interaction seatEntity, ItemDisplay display) {
        PersistentDataUtils.set(seatEntity, FURNITURE_SEAT_KEY, PersistentDataType.STRING, display.getUniqueId().toString());
    }

    public static boolean isFurnitureSeat(Interaction seatEntity) {
        return PersistentDataUtils.get(seatEntity, FURNITURE_SEAT_KEY, PersistentDataType.STRING) != null;
    }

    public static Optional<UUID> getFurnitureEntityFromSeat(Interaction seatEntity) {
        if (!isFurnitureSeat(seatEntity)) return Optional.empty();
        return Optional.of(UUID.fromString(Objects.requireNonNull(PersistentDataUtils.get(seatEntity, FURNITURE_SEAT_KEY,
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
