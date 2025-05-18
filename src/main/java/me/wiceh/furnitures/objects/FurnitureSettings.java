package me.wiceh.furnitures.objects;

import org.bukkit.util.Vector;
import org.joml.Vector3f;

public record FurnitureSettings(Vector offset, Vector3f scale, String category) {
}
