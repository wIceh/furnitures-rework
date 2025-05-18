package me.wiceh.furnitures.managers;

import me.wiceh.furnitures.Furnitures;
import me.wiceh.furnitures.objects.Furniture;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.stream.Collectors;

import static me.wiceh.furnitures.utils.ConfigUtils.*;

public class FurnituresManager {
    private final FileConfiguration config;

    public FurnituresManager(Furnitures plugin) {
        this.config = plugin.getConfig();
    }

    public Set<Furniture> getFurnitures() {
        Set<Furniture> furnitures = new HashSet<>();

        ConfigurationSection furnituresSection = config.getConfigurationSection("furnitures");
        if (furnituresSection != null) {
            for (String furnitureId : furnituresSection.getKeys(false)) {
                ConfigurationSection furnitureSection = furnituresSection.getConfigurationSection(furnitureId);
                if (furnitureSection == null) continue;

                ConfigurationSection settingsSection = furnitureSection.getConfigurationSection("settings");
                ConfigurationSection itemSection = furnitureSection.getConfigurationSection("item");
                ConfigurationSection hitBoxSection = furnitureSection.getConfigurationSection("hit-box");
                ConfigurationSection colorSection = furnitureSection.getConfigurationSection("color");

                if (settingsSection == null || itemSection == null || hitBoxSection == null || colorSection == null)
                    continue;

                String defaultColor = colorSection.getString("default", "255#255#255");
                List<String> colors = colorSection.getStringList("list");

                List<String> blocks = furnitureSection.getStringList("blocks");
                List<String> seats = furnitureSection.getStringList("seats");

                furnitures.add(new Furniture(
                        furnitureId,
                        parseSettings(settingsSection),
                        parseItem(itemSection).orElse(null),
                        parseHitBox(hitBoxSection).orElse(null),
                        parseColor(defaultColor),
                        parseColors(colors),
                        parseBlocks(blocks),
                        parseSeats(seats)
                ));
            }
        }

        return furnitures;
    }

    public List<Furniture> getFurnituresSorted() {
        Set<Furniture> raw = getFurnitures();

        List<Furniture> sorted = new ArrayList<>(raw);
        sorted.sort(Comparator.comparingInt(f -> f.item().modelData()));

        return sorted;
    }

    public Set<Furniture> getFurnitures(String category) {
        return getFurnitures().stream()
                .filter(furniture -> furniture.getCategory().equals(category))
                .collect(Collectors.toSet());
    }

    public List<Furniture> getFurnituresSorted(String category) {
        Set<Furniture> raw = getFurnitures(category);

        List<Furniture> sorted = new ArrayList<>(raw);
        sorted.sort(Comparator.comparingInt(f -> f.item().modelData()));

        return sorted;
    }

    public Optional<Furniture> getFurniture(String id) {
        return getFurnitures().stream()
                .filter(furniture -> furniture.id().equals(id))
                .findFirst();
    }

    public List<Furniture> searchFurnitures(String query) {
        return getFurnituresSorted().stream()
                .filter(furniture -> furniture.getStringName().toLowerCase().contains(query) ||
                        furniture.id().toLowerCase().contains(query))
                .toList();
    }

    public TreeSet<String> getCategories() {
        TreeSet<String> categories = new TreeSet<>();

        Set<Furniture> furnitures = getFurnitures();
        for (Furniture furniture : furnitures) {
            String category = furniture.getCategory();
            categories.add(category);
        }

        return categories;
    }
}
