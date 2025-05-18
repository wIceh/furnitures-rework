package me.wiceh.furnitures.listeners;

import me.wiceh.furnitures.Furnitures;
import me.wiceh.furnitures.objects.Furniture;
import me.wiceh.furnitures.objects.FurnitureHitBox;
import me.wiceh.furnitures.objects.FurnitureSettings;
import me.wiceh.furnitures.utils.FurnitureSeatUtils;
import me.wiceh.furnitures.utils.FurnitureUtils;
import me.wiceh.furnitures.utils.HitBoxUtils;
import me.wiceh.furnitures.utils.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class FurnitureListener implements Listener {
    private final Furnitures plugin;

    public FurnitureListener(Furnitures plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFurniturePlace(PlayerInteractEvent event) {
        if (!isPlaceAction(event)) return;

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        String id = FurnitureUtils.getFurnitureItemKey(item);
        if (id == null) return;

        Furniture furniture = plugin.getFurnituresManager().getFurniture(id).orElse(null);
        if (furniture == null) return;

        event.setCancelled(true);

        Block block = event.getClickedBlock();
        if (block == null) return;
        BlockFace face = event.getBlockFace();

        Location clicked = block.getLocation().add(0.5, 1, 0.5);
        clicked.setYaw(LocationUtils.getSnappedYaw(player.getLocation()));

        if (face == BlockFace.DOWN) { // soffitto
            clicked.subtract(0, 2, 0);
        } else { // muro
            if (face != BlockFace.UP) {
                Vector offset = new Vector(0, -0.5, -0.5);
                clicked = LocationUtils.rotateOffset(clicked, offset);
            }
        }

        ItemStack clone = item.clone();
        clone.setAmount(1);

        spawnFurniture(clicked, clone, furniture, furniture.hitBox().enabled());
        consumeItem(player, item);
    }

    @EventHandler
    public void onFurnitureBreak(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player) ||
                !(event.getEntity() instanceof Interaction hit)) return;

        boolean isHitBox = HitBoxUtils.isHitBox(hit);
        handleFurnitureRemoval(player, hit, isHitBox);
    }

    @EventHandler
    public void onSeatInteract(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof Interaction seat) ||
                !FurnitureSeatUtils.isFurnitureSeat(seat) ||
                !seat.getPassengers().isEmpty()) return;

        seat.addPassenger(event.getPlayer());
    }

    private boolean isPlaceAction(PlayerInteractEvent e) {
        return e.getAction() == Action.RIGHT_CLICK_BLOCK
                && e.getHand() == EquipmentSlot.HAND
                && e.getClickedBlock() != null;
    }

    private void consumeItem(Player player, ItemStack item) {
        if (player.getGameMode() != GameMode.CREATIVE) {
            item.setAmount(item.getAmount() - 1);
        }
    }

    private void spawnFurniture(Location loc, ItemStack item, Furniture furniture, boolean spawnHitBox) {
        FurnitureSettings settings = furniture.settings();
        Quaternionf noRot = new Quaternionf();

        // Display
        Location dispLoc = LocationUtils.rotateOffset(loc, settings.offset());
        ItemDisplay display = (ItemDisplay) loc.getWorld().spawnEntity(dispLoc, EntityType.ITEM_DISPLAY);
        display.setItemStack(item);
        display.setRotation(loc.getYaw() + 180, 0);
        // Apply scale
        display.setTransformation(new Transformation(
                display.getTransformation().getTranslation(),
                noRot,
                settings.scale(),
                noRot
        ));
        FurnitureUtils.setFurnitureEntity(display, furniture);

        if (spawnHitBox) createHitBox(loc, display, furniture.hitBox());
        placeBlocks(loc, furniture);
        createSeats(display, furniture);
    }

    private void createHitBox(Location base, ItemDisplay display, FurnitureHitBox hitBox) {
        Location offsetLoc = LocationUtils.rotateOffset(base, hitBox.offset());
        Interaction box = HitBoxUtils.spawnHitbox(offsetLoc, hitBox.width(), hitBox.height());
        HitBoxUtils.setHitBoxEntity(box, display);
    }

    private void placeBlocks(Location base, Furniture furniture) {
        furniture.blocks().forEach((offset, mat) -> {
            Location bLoc = base.clone().add(offset);
            Block b = new Location(bLoc.getWorld(), bLoc.getBlockX(), bLoc.getBlockY(), bLoc.getBlockZ()).getBlock();
            if (b.getType() == Material.AIR) b.setType(mat);
        });
    }

    private void createSeats(ItemDisplay display, Furniture furniture) {
        List<org.bukkit.util.Vector> seats = furniture.seats();
        if (seats.isEmpty()) return;

        seats.forEach(off -> {
            Location seatLoc = LocationUtils.rotateOffset(display.getLocation(), off);
            Interaction seat = (Interaction) seatLoc.getWorld().spawnEntity(seatLoc, EntityType.INTERACTION);
            seat.setInteractionWidth(0.5f);
            seat.setInteractionHeight(0.5f);
            seat.setInvulnerable(true);
            seat.setRotation(seatLoc.getYaw(), 0);
            FurnitureSeatUtils.setFurnitureSeat(seat, display);
        });
    }

    private void handleFurnitureRemoval(Player player, Interaction interaction, boolean byHitBox) {
        ItemDisplay display = resolveDisplay(interaction, byHitBox);
        if (display == null) return;

        String id = FurnitureUtils.getFurnitureEntityKey(display);
        Furniture furniture = plugin.getFurnituresManager().getFurniture(id).orElse(null);

        // Drop or return the item
        if (furniture != null) {
            ItemStack drop = display.getItemStack().clone();
            if (player.getGameMode() == GameMode.CREATIVE) {
                player.getInventory().addItem(drop);
            } else {
                player.getWorld().dropItem(display.getLocation(), drop);
            }
            removeBlocks(display, furniture);
        }
        removeSeats(display);
    }

    private ItemDisplay resolveDisplay(Interaction inter, boolean hitBox) {
        if (hitBox) {
            Entity parent = HitBoxUtils.getEntityFromHitBox(inter).orElse(null);
            if (parent instanceof ItemDisplay && FurnitureUtils.isFurnitureEntity(parent)) {
                inter.remove();
                parent.remove();
                return (ItemDisplay) parent;
            }
        } else {
            UUID id = FurnitureSeatUtils.getFurnitureEntityFromSeat(inter).orElse(null);
            Entity ent = (id != null) ? Bukkit.getEntity(id) : null;
            if (ent instanceof ItemDisplay && FurnitureUtils.isFurnitureEntity(ent)) {
                ent.remove();
                return (ItemDisplay) ent;
            }
        }
        return null;
    }

    private void removeBlocks(ItemDisplay display, Furniture furniture) {
        furniture.blocks().keySet().forEach(offset -> {
            Location loc = display.getLocation().clone()
                    .subtract(furniture.settings().offset())
                    .add(offset);
            Block b = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).getBlock();
            if (b.getType() == furniture.blocks().get(offset)) {
                b.setType(Material.AIR);
            }
        });
    }

    private void removeSeats(ItemDisplay display) {
        Set<Interaction> seats = FurnitureSeatUtils.getSeatsByFurniture(display);
        seats.forEach(Entity::remove);
    }
}
