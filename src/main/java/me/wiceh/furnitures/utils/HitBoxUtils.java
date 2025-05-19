package me.wiceh.furnitures.utils;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.UUID;

public class HitBoxUtils {

    public static final NamespacedKey HITBOX_KEY = new NamespacedKey("furnitures", "hitbox");

    public static Interaction spawnHitbox(Location location, float width, float height) {
        Interaction hitbox = (Interaction) location.getWorld().spawnEntity(location, EntityType.INTERACTION);

        hitbox.setPersistent(true);
        hitbox.setGravity(false);
        hitbox.setInvulnerable(true);
        hitbox.setResponsive(true);

        hitbox.setInteractionWidth(width);
        hitbox.setInteractionHeight(height);

        hitbox.setRotation(location.getYaw(), 0);

        return hitbox;
    }

    public static void setHitBoxEntity(Interaction hitBox, Entity entity) {
        hitBox.getPersistentDataContainer().set(HITBOX_KEY, PersistentDataType.STRING, entity.getUniqueId().toString());
    }

    public static boolean isHitBox(Interaction hitBox) {
        return hitBox.getPersistentDataContainer().has(HITBOX_KEY, PersistentDataType.STRING);
    }

    public static Optional<Entity> getEntityFromHitBox(Interaction hitBox) {
        String displayUuid = hitBox.getPersistentDataContainer().get(HITBOX_KEY, PersistentDataType.STRING);
        if (displayUuid == null) return Optional.empty();

        Entity entity = hitBox.getWorld().getEntity(UUID.fromString(displayUuid));
        return Optional.ofNullable(entity);
    }
}
