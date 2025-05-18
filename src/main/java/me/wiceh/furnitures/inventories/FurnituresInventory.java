package me.wiceh.furnitures.inventories;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import me.wiceh.furnitures.Furnitures;
import me.wiceh.furnitures.objects.Furniture;
import me.wiceh.utils.utils.Utils;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.text;

public class FurnituresInventory {

    private final Furnitures plugin;

    public FurnituresInventory(Furnitures plugin) {
        this.plugin = plugin;
    }

    public void openCategories(Player player) {
        PaginatedGui gui = Gui.paginated()
                .title(text("Categorie"))
                .rows(4)
                .disableAllInteractions()
                .create();

        addNavigationItems(gui, false);

        TreeSet<String> categories = plugin.getFurnituresManager().getCategories();

        categories.forEach(category -> {
            gui.addItem(ItemBuilder.from(Material.CONDUIT)
                    .name(text("§aᴄᴀᴛᴇɢᴏʀɪᴀ §7#" + category).decoration(TextDecoration.ITALIC, false))
                    .lore(List.of(
                            empty(),
                            text("§eꜰᴜʀɴɪᴛᴜʀᴇѕ ᴅɪѕᴘᴏɴɪʙɪʟɪ: §f" + plugin.getFurnituresManager().getFurnitures(category).size())
                    ))
                    .asGuiItem(event -> openFurnitures(player, category)));
        });

        gui.open(player);
    }

    public void openFurnitures(Player player, String category) {
        PaginatedGui gui = Gui.paginated()
                .title(text("Furnitures #" + category))
                .rows(4)
                .disableAllInteractions()
                .create();

        addNavigationItems(gui, true);

        List<Furniture> furnitures = plugin.getFurnituresManager().getFurnituresSorted(category);
        furnitures.forEach(furniture -> {
            gui.addItem(new GuiItem(furniture.getGuiItem(), event -> {
                if (furniture.item().material().name().contains("LEATHER") && !furniture.colors().isEmpty()) {
                    openFurnitureColor(player, furniture);
                    return;
                }

                player.getInventory().addItem(furniture.getItemStack());
            }));
        });

        gui.open(player);
    }

    public void openFurnitureColor(Player player, Furniture furniture) {
        PaginatedGui gui = Gui.paginated()
                .title(text("Scegli un colore"))
                .rows(4)
                .disableAllInteractions()
                .create();

        addNavigationItems(gui, true);

        List<Color> colors = furniture.colors();
        for (Color color : colors) {
            GuiItem item = ItemBuilder.from(furniture.getGuiItem().clone())
                    .name(text(furniture.getStringName(), Utils.parseTextColor(color)))
                    .asGuiItem(event -> {
                        ItemStack stack = furniture.getItemStack().clone();
                        stack.editMeta(meta -> {
                            LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
                            armorMeta.setColor(color);
                        });

                        player.getInventory().addItem(stack);
                    });

            item.getItemStack().editMeta(meta -> {
                LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;
                armorMeta.setColor(color);
            });

            gui.addItem(item);
        }

        gui.open(player);
    }

    private void addNavigationItems(PaginatedGui gui, boolean homeButton) {
        gui.setItem(4, 1, ItemBuilder.from(Material.PAPER).model(12)
                .name(text("ᴘᴀɢɪɴᴀ §lᴘʀᴇᴄᴇᴅᴇɴᴛᴇ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
                .asGuiItem(event -> gui.previous()));

        if (homeButton) {
            gui.setItem(4, 2, ItemBuilder.from(Material.PAPER).model(38)
                    .name(text("ʜᴏᴍᴇ", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false))
                    .asGuiItem(event -> openCategories((Player) event.getWhoClicked())));
        }

        gui.setItem(4, 9, ItemBuilder.from(Material.PAPER).model(13)
                .name(text("ᴘᴀɢɪɴᴀ §lѕᴜᴄᴄᴇѕѕɪᴠᴀ", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false))
                .asGuiItem(event -> gui.next()));

        int[] indexes = new int[]{28, 29, 30, 31, 32, 33, 34};
        if (homeButton) {
            indexes = new int[]{29, 30, 31, 32, 33, 34};
        }
        for (int index : indexes) gui.setItem(index, new GuiItem(Material.GRAY_STAINED_GLASS_PANE));
    }
}
