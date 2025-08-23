package su.trident.cenchants.command.impl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.command.api.ArgumentExecutor;
import su.trident.cenchants.enchant.api.Enchantment;

public class GiveBook implements ArgumentExecutor
{
    private final CEnchants plugin;

    public GiveBook(CEnchants plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public String getName()
    {
        return "book";
    }

    @Override
    public String getUsage()
    {
        return "/ce book [enchant] [lvl]";
    }

    @Override
    public String getDesc()
    {
        return "Выдает книгу зачарования";
    }

    @Override
    public void execute(Player player, String[] args)
    {
        if (args.length <= 2) {
            sendHelp(player);
            return;
        }
        if (Enchantment.getByKey(args[1]) == null) {
            sendHelp(player);
            return;
        }

        try {
            final int level = Integer.parseInt(args[2]);
            final Enchantment<?> enchantment = Enchantment.getByKey(args[1]);

            giveBook(player, enchantment, level);

        } catch (NumberFormatException e) {
            sendHelp(player);
        }
    }

    private void giveBook(Player player, Enchantment<?> enchantment, int level)
    {
        player.getInventory().addItem(plugin.getStorage().book(enchantment, level));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&lcEnchants:&r&a успешно выдана книга " + enchantment.getName()));
    }

    private void sendHelp(Player player)
    {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9cEnchants:&c используйте: %s".formatted(this.getUsage())));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9cEnchants:&c &7описание: %s".formatted(this.getDesc())));
    }
}
