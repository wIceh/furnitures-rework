package me.wiceh.furnitures.commands.subcommands;

import dev.jorel.commandapi.arguments.StringArgument;
import me.wiceh.furnitures.FurnituresPlugin;
import me.wiceh.furnitures.inventories.SearchFurnituresInventory;
import me.wiceh.furnitures.permission.FurnituresPermission;
import me.wiceh.utils.command.SubCommand;

public class SearchCommand extends SubCommand<FurnituresPlugin> {
    public SearchCommand(FurnituresPlugin plugin) {
        super(plugin, "cerca");
    }

    @Override
    protected void setup() {
        withPermission(FurnituresPermission.SEARCH.getPermission());
        withArguments(new StringArgument("query"));
        executesPlayer((player, args) -> {
            String query = (String) args.get(0);
            new SearchFurnituresInventory(plugin, query.toLowerCase()).open(player);
        });
    }
}
