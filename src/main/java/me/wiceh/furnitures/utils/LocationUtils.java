package me.wiceh.furnitures.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class LocationUtils {

    public static float getSnappedYaw(Location loc) {
        float yaw = loc.getYaw();
        yaw = (yaw % 360 + 360) % 360; // Normalizza tra 0–359
        int snapped = Math.round(yaw / 45f) * 45; // Arrotonda al multiplo di 45
        return snapped % 360;
    }

    public static Location rotateOffset(Location location, Vector offset) {
        float yaw = location.getYaw();
        double yawRad = Math.toRadians(yaw);

        double cos = Math.cos(yawRad);
        double sin = Math.sin(yawRad);
        double offsetX = offset.getX() * cos - offset.getZ() * sin;
        double offsetZ = offset.getX() * sin + offset.getZ() * cos;
        double offsetY = offset.getY();

        return location.clone().add(offsetX, offsetY, offsetZ);
    }
}
