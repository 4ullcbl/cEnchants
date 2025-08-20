package su.trident.cenchnats.command;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.command.api.ArgumentExecutor;
import su.trident.cenchnats.command.impl.AddEnchant;
import su.trident.cenchnats.command.impl.GiveBook;
import su.trident.cenchnats.enchant.api.Enchant;

import java.util.*;

public class GiveEnchant implements CommandExecutor, TabExecutor
{
    private final static Map<String, ArgumentExecutor> arguments = new HashMap<>();

    public GiveEnchant(CEnchants plugin)
    {
        arguments.put("add", new AddEnchant(plugin));
        arguments.put("book", new GiveBook(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        final Player player = (Player) sender;

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        final String argument = args[0].toLowerCase();
        final ArgumentExecutor executor = arguments.get(argument);

        if (executor == null) {
            sendHelp(player);
            return true;
        }

        executor.execute(player, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        final List<String> tab = new ArrayList<>();

        if (args.length == 1) {
            tab.addAll(arguments.keySet());
        } else if (args.length == 2) {
            tab.addAll(Enchant.keySet());
        }

        return tab;
    }

    private void sendHelp(Player player)
    {
        for (ArgumentExecutor argument : arguments.values()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9cEnchants:&c используйте: %s".formatted(argument.getUsage())));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9cEnchants:&c &7описание: %s".formatted(argument.getDesc())));        }
    }
}
