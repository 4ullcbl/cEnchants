package su.trident.cenchants.command.impl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import su.trident.cenchants.command.api.ArgumentExecutor;
import su.trident.cenchants.enchant.api.Enchantment;

public class ConfigReload extends ArgumentExecutor
{
    private final JavaPlugin plugin;

    public ConfigReload(JavaPlugin plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public String getName()
    {
        return "reload";
    }

    @Override
    public String getUsage()
    {
        return "/ce reload";
    }

    @Override
    public String getDesc()
    {
        return "Перезагружает конфиг";
    }

    @Override
    public void execute(Player player, String[] args)
    {
        plugin.reloadConfig();

        Enchantment.keySet().forEach(e -> Enchantment.getByKey(e).loadConfig());
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&lcEnchants:&r&a Конфиг перезагружен"));
    }
}
