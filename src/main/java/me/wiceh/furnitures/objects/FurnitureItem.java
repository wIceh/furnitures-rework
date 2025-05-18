package me.wiceh.furnitures.objects;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record FurnitureItem(Material material, int modelData, Component displayName, List<Component> lore) {

    public ItemStack build() {
        return ItemBuilder.from(material).model(modelData).name(displayName).lore(lore).build();
    }
}
