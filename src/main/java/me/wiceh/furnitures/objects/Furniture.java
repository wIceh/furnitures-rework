package me.wiceh.furnitures.objects;

import me.wiceh.furnitures.utils.FurnitureUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record Furniture(String id, FurnitureSettings settings, FurnitureItem item, FurnitureHitBox hitBox,
                        Color defaultColor,
                        List<Color> colors, Map<Vector, Material> blocks, List<Vector> seats) {

    public String getStringName() {
        String name = LegacyComponentSerializer.builder().useUnusualXRepeatedCharacterHexFormat().build().serialize(item.displayName());
        return ChatColor.stripColor(name);
    }

    public String getCategory() {
        String category = settings.category();
        return category != null && !category.isBlank() ? category : "default";
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = item.build();
        FurnitureUtils.setFurnitureItem(itemStack, this);
        return itemStack;
    }

    public ItemStack getGuiItem() {
        ItemStack itemStack = item.build();
        itemStack.editMeta((meta) -> {
            List<Component> lore = meta.lore() == null ? new ArrayList<>() : new ArrayList<>(meta.lore());
            lore.add(Component.empty());
            lore.add(Component.text("§fModel Data: §7" + this.item.modelData()));
            lore.add(Component.text("§fID: §7" + id));
            lore.add(Component.empty());
            lore.add(Component.text("§eᴄʟɪᴄᴄᴀ ᴘᴇʀ ᴏᴛᴛᴇɴᴇʀᴇ"));
            meta.lore(lore);
        });
        return itemStack;
    }
}
