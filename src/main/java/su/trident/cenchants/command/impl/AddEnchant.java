package su.trident.cenchants.command.impl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import su.trident.cenchants.command.api.ArgumentExecutor;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.enchant.api.EnchantmentStorage;

public class AddEnchant extends ArgumentExecutor
{
    private final EnchantmentStorage storage;

    public AddEnchant(EnchantmentStorage storage)
    {
        this.storage = storage;
    }

    @Override
    public String getName()
    {
        return "add";
    }

    @Override
    public String getUsage()
    {
        return "/ce add [enchant] [lvl]";
    }

    @Override
    public String getDesc()
    {
        return "Добавляет указанное зачарование";
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

            if (targetTypeCheck(player, enchantment)) return;
            addEnchant(player, enchantment, level);

        } catch (NumberFormatException e) {
            sendHelp(player);
        }
    }

    private void addEnchant(Player player, Enchantment<?> enchantment, int level)
    {
        storage.addEnchantment(player.getInventory().getItemInMainHand(), enchantment, level);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&lcEnchants:&r&a успешно добавлен " + enchantment.getName()));
    }

    private boolean targetTypeCheck(Player player, Enchantment<?> enchantment)
    {
        if (!enchantment.getTarget().isTarget(player.getInventory().getItemInMainHand().getType())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&lcEnchants:&r&c Вы не можете добавить " + enchantment.getName() + " на этот предмет."));
            return true;
        }
        return false;
    }
}
