package su.trident.cenchnats.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.trident.cenchnats.CEnchants;

public class GiveEnchant implements CommandExecutor
{
    private final CEnchants plugin;

    public GiveEnchant(CEnchants plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        final Player player = (Player) sender;

        final ItemStack item = player.getInventory().getItemInMainHand();

        if (args[0].equals("BULLDOZER"))
            plugin.getStorage().addEnchantSave(item, plugin.getRegister().getBulldozer(), 1);
        if (args[0].equals("MAGNET"))
            plugin.getStorage().addEnchantSave(item, plugin.getRegister().getMagnet(), 1);
        if (args[0].equals("MELTING"))
            plugin.getStorage().addEnchantSave(item, plugin.getRegister().getMelting(), 1);
        if (args[0].equals("PING"))
            plugin.getStorage().addEnchantSave(item, plugin.getRegister().getPinger(), 1);
        if (args[0].equals("WEB"))
            plugin.getStorage().addEnchantSave(item, plugin.getRegister().getWeb(), 1);
        return true;
    }
}
