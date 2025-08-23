package su.trident.cenchants.command.api;

import org.bukkit.entity.Player;

public interface ArgumentExecutor
{
    String getName();

    String getUsage();

    String getDesc();

    void execute(Player player, String[] args);
}
