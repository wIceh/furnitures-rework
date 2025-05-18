package me.wiceh.utils.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static net.kyori.adventure.text.Component.text;

public class Utils {
    public static final LegacyComponentSerializer SERIALIZER;
    private static final SimpleDateFormat DATE_FORMAT;
    private static final NumberFormat CURRENCY_FORMAT;

    private Utils() {
    }

    public static Component parse(String data) {
        return parse((Component) SERIALIZER.deserialize(data));
    }

    public static Component parse(Component data) {
        return SERIALIZER.deserialize(SERIALIZER.serialize(MiniMessage.miniMessage().deserialize(SERIALIZER.serialize(data))));
    }

    public static void safeAddItem(Player player, ItemStack stack) {
        safeAddItem(player, stack);
    }

    public static void safeAddItem(Player player, ItemStack... stacks) {
        HashMap<Integer, ItemStack> exceedItems = player.getInventory().addItem(stacks);
        if (!exceedItems.isEmpty()) {
            player.sendMessage(text("\n §cɴᴏɴ ʜᴀɪ ѕᴘᴀᴢɪᴏ \n §7Non hai abbastanza spazio nell'inventario! L'oggetto verrà lanciato a terra.\n"));

            for (ItemStack drop : exceedItems.values()) {
                player.getWorld().dropItem(player.getLocation(), drop);
            }
        }

    }

    public static boolean chance(int value) {
        return value >= ThreadLocalRandom.current().nextInt(101);
    }


    public static <T extends Enum<T>> Optional<T> optionalEnum(Class<T> clazz, String string) {
        try {
            return Optional.of(Enum.valueOf(clazz, string.toUpperCase()));
        } catch (IllegalArgumentException var3) {
            return Optional.empty();
        }
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String formatCurrency(double amount) {
        return CURRENCY_FORMAT.format(amount);
    }

    public static String formatSeconds(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (long) day * 24L;
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60L;
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60L;
        StringBuilder stringBuilder = new StringBuilder();
        if (day > 0) {
            stringBuilder.append(day).append("d ");
        }

        if (hours > 0L) {
            stringBuilder.append(hours).append("h ");
        }

        if (minute > 0L) {
            stringBuilder.append(minute).append("m ");
        }

        if (second > 0L) {
            stringBuilder.append(second).append("s ");
        }

        return stringBuilder.toString().trim();
    }

    public static List<Component> parseComponentList(List<String> list) {
        List<Component> components = new ArrayList<>();
        for (String string : list) components.add(text(ChatColor.translateAlternateColorCodes('&', string)));
        return components;
    }

    public static TextColor parseTextColor(Color color) {
        return TextColor.color(color.getRed(), color.getGreen(), color.getBlue());
    }

    static {
        SERIALIZER = LegacyComponentSerializer.legacyAmpersand().toBuilder().hexColors().hexCharacter('#').extractUrls(Style.style(TextColor.fromHexString("#aade1d"), TextDecoration.UNDERLINED)).build();
        DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        CURRENCY_FORMAT = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        CURRENCY_FORMAT.setMaximumFractionDigits(1);
    }
}
