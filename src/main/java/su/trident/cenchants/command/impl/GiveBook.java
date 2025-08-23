package su.trident.cenchants.command.impl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import su.trident.cenchants.command.api.ArgumentExecutor;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.enchant.api.EnchantmentStorage;

public class GiveBook extends ArgumentExecutor
{
    private final EnchantmentStorage storage;

    public GiveBook(EnchantmentStorage storage)
    {
        this.storage = storage;
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
        player.getInventory().addItem(storage.book(enchantment, level));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&lcEnchants:&r&a успешно выдана книга " + enchantment.getName()));
    }
}
