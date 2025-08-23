package su.trident.cenchants.command.api;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public abstract class ArgumentExecutor
{
    public abstract String getName();

    public abstract String getUsage();

    public abstract String getDesc();

    public abstract void execute(Player player, String[] args);

    public void sendHelp(Player player)
    {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9cEnchants:&c используйте: %s".formatted(this.getUsage())));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9cEnchants:&c &7описание: %s".formatted(this.getDesc())));
    }
}
