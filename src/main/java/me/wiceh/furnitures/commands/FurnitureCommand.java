package me.wiceh.furnitures.commands;

import dev.jorel.commandapi.CommandAPICommand;
import me.wiceh.furnitures.FurnituresPlugin;
import me.wiceh.furnitures.commands.subcommands.SearchCommand;
import me.wiceh.furnitures.inventories.FurnituresInventory;
import me.wiceh.furnitures.permission.FurnituresPermission;
import me.wiceh.utils.command.CommandSupplier;

public class FurnitureCommand extends CommandSupplier {

    private final FurnituresPlugin plugin;

    public FurnitureCommand(FurnituresPlugin plugin) {
        super("furniture");
        this.plugin = plugin;
    }

    @Override
    public CommandAPICommand[] get() {
        return new CommandAPICommand[]{
                create(name)
                        .withPermission(FurnituresPermission.FURNITURE.getPermission())
                        .withAliases("furnitures")
                        .withSubcommands(
                                new SearchCommand(plugin)
                        )
                        .executesPlayer((player, args) -> {
                    new FurnituresInventory(plugin).openCategories(player);
                })
        };
    }
}
