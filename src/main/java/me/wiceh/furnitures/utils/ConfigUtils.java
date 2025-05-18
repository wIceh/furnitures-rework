package me.wiceh.furnitures.utils;

import me.wiceh.furnitures.objects.FurnitureHitBox;
import me.wiceh.furnitures.objects.FurnitureItem;
import me.wiceh.furnitures.objects.FurnitureSettings;
import me.wiceh.utils.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.Vector;
import org.joml.Vector3f;

import java.util.*;
import java.util.stream.Collectors;

import static net.kyori.adventure.text.Component.text;

public class ConfigUtils {

    /**
     * Converte una stringa nel formato "x#y#z" in un {@link Vector}.
     *
     * @param string la stringa da parsare, con tre valori separati da '#'
     * @return un nuovo Vector con le coordinate x, y, z
     * @throws NumberFormatException se uno dei componenti non è un numero valido
     */
    public static Vector parseVector(String string) {
        String[] args = string.split("#");
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double z = Double.parseDouble(args[2]);
        return new Vector(x, y, z);
    }

    /**
     * Converte una stringa nel formato "x#y#z" in un {@link Vector3f}.
     *
     * @param string la stringa da parsare, con tre valori separati da '#'
     * @return un nuovo Vector3f con le coordinate x, y, z
     * @throws NumberFormatException se uno dei componenti non è un numero valido
     */
    public static Vector3f parseVector3f(String string) {
        String[] args = string.split("#");
        float x = Float.parseFloat(args[0]);
        float y = Float.parseFloat(args[1]);
        float z = Float.parseFloat(args[2]);
        return new Vector3f(x, y, z);
    }

    public static FurnitureSettings parseSettings(ConfigurationSection settings) {
        if (!settings.isSet("offset") || !settings.isSet("scale") || !settings.isSet("category"))
            throw new IllegalArgumentException("Missing keys in settings for furniture “" + settings.getName() + "”");

        return new FurnitureSettings(
                parseVector(settings.getString("offset", "0.0#0.0#0.0")),
                parseVector3f(settings.getString("scale", "1.0#0.0#0.0")),
                settings.getString("category")
        );
    }

    public static Optional<FurnitureItem> parseItem(ConfigurationSection itemSection) {
        String material = itemSection.getString("material", "BARRIER");
        int modelData = itemSection.getInt("model-data", 0);
        String displayName = itemSection.getString("display-name", "&r&7Furniture");
        List<String> lore = itemSection.getStringList("lore");

        return Optional.of(new FurnitureItem(
                Material.valueOf(material),
                modelData,
                text(ChatColor.translateAlternateColorCodes('&', displayName)),
                Utils.parseComponentList(lore)
        ));
    }

    public static Optional<FurnitureHitBox> parseHitBox(ConfigurationSection hitBoxSection) {
        boolean enabled = hitBoxSection.getBoolean("enabled");
        String offset = hitBoxSection.getString("offset", "0.0#0.0#0.0");
        float width = (float) hitBoxSection.getDouble("width", 1.0);
        float height = (float) hitBoxSection.getDouble("height", 1.0);

        return Optional.of(new FurnitureHitBox(
                enabled,
                parseVector(offset),
                width,
                height
        ));
    }

    public static Color parseColor(String rgb) {
        String[] rgbArgs = rgb.split("#");
        int r = Integer.parseInt(rgbArgs[0]);
        int g = Integer.parseInt(rgbArgs[1]);
        int b = Integer.parseInt(rgbArgs[2]);
        return Color.fromRGB(r, g, b);
    }

    public static List<Color> parseColors(List<String> list) {
        List<Color> colors = new ArrayList<>();
        for (String string : list) colors.add(parseColor(string));
        return colors;
    }

    public static Map<Vector, Material> parseBlocks(List<String> blocks) {
        Map<Vector, Material> map = new HashMap<>();

        for (String block : blocks) {
            String[] args = block.split(" ");

            String material = args[0];
            String offset = args[1];

            map.put(parseVector(offset), Material.valueOf(material));
        }

        return map;
    }

    public static List<Vector> parseSeats(List<String> list) {
        return list.stream()
                .map(ConfigUtils::parseVector)
                .collect(Collectors.toList());
    }
}
