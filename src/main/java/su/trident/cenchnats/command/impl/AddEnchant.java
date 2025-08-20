package su.trident.cenchnats.command.impl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.command.api.ArgumentExecutor;
import su.trident.cenchnats.enchant.api.Enchant;

public class AddEnchant implements ArgumentExecutor
{
    private final CEnchants plugin;

    public AddEnchant(CEnchants plugin)
    {
        this.plugin = plugin;
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
        if (Enchant.getByKey(args[1]) == null) {
            sendHelp(player);
            return;
        }

        try {
            final int level = Integer.parseInt(args[2]);
            final Enchant<?> enchant = Enchant.getByKey(args[1]);

            if (targetTypeCheck(player, enchant)) return;
            addEnchant(player, enchant, level);

        } catch (NumberFormatException e) {
            sendHelp(player);
        }
    }

    private void addEnchant(Player player, Enchant<?> enchant, int level)
    {
        plugin.getStorage().addEnchantSave(player.getInventory().getItemInMainHand(), enchant, level);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&lcEnchants:&r&a успешно добавлен " + enchant.getName()));
    }

    private boolean targetTypeCheck(Player player, Enchant<?> enchant)
    {
        if (!enchant.getTarget().isType(player.getInventory().getItemInMainHand().getType())) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&lcEnchants:&r&c Вы не можете добавить " + enchant.getName() + " на этот предмет."));
            return true;
        }
        return false;
    }

    private void sendHelp(Player player)
    {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9cEnchants:&c используйте: %s".formatted(this.getUsage())));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9cEnchants:&c &7описание: %s".formatted(this.getDesc())));
    }
}
