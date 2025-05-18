package me.wiceh.furnitures.inventories;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.wiceh.furnitures.Furnitures;
import me.wiceh.furnitures.objects.Furniture;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

import static net.kyori.adventure.text.Component.text;

public class SearchFurnituresInventory {

    private final Furnitures plugin;
    private final String query;

    public SearchFurnituresInventory(Furnitures plugin, String query) {
        this.plugin = plugin;
        this.query = query;
    }

    public void open(Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(text("Risultati Ricerca: " + query))
                .rows(4)
                .disableAllInteractions()
                .create();

        addNavigationItems(gui);

        List<Furniture> furnitures = plugin.getFurnituresManager().searchFurnitures(query);
        furnitures.forEach(furniture -> {
            gui.addItem(new GuiItem(furniture.getGuiItem(), event -> {
                if (furniture.item().material().name().contains("LEATHER") && !furniture.colors().isEmpty()) {
                    new FurnituresInventory(plugin).openFurnitureColor(player, furniture);
                    return;
                }

                player.getInventory().addItem(furniture.getItemStack());
            }));
        });

        gui.open(player);
    }

    private void addNavigationItems(PaginatedGui gui) {
        gui.setItem(4, 1, ItemBuilder.from(Material.PAPER).model(12)
                .name(text("ᴘᴀɢɪɴᴀ §lᴘʀᴇᴄᴇᴅᴇɴᴛᴇ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
                .asGuiItem(event -> gui.previous()));

        gui.setItem(4, 9, ItemBuilder.from(Material.PAPER).model(13)
                .name(text("ᴘᴀɢɪɴᴀ §lѕᴜᴄᴄᴇѕѕɪᴠᴀ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
                .asGuiItem(event -> gui.next()));

        int[] indexes = {28, 29, 30, 31, 32, 33, 34};
        for (int index : indexes) gui.setItem(index, new GuiItem(Material.GRAY_STAINED_GLASS_PANE));
    }
}
