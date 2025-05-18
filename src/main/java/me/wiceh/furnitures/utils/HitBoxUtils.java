package me.wiceh.furnitures.utils;

import me.wiceh.utils.utils.PersistentDataUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.UUID;

public class HitBoxUtils {

    public static final String HITBOX_KEY = "hitbox";

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
        PersistentDataUtils.set(hitBox, HITBOX_KEY, PersistentDataType.STRING, entity.getUniqueId().toString());
    }

    public static boolean isHitBox(Interaction hitBox) {
        return PersistentDataUtils.get(hitBox, HITBOX_KEY, PersistentDataType.STRING) != null;
    }

    public static Optional<Entity> getEntityFromHitBox(Interaction hitBox) {
        String displayUuid = PersistentDataUtils.get(hitBox, HITBOX_KEY, PersistentDataType.STRING);
        if (displayUuid == null) return Optional.empty();

        Entity entity = hitBox.getWorld().getEntity(UUID.fromString(displayUuid));
        return Optional.ofNullable(entity);
    }
}
