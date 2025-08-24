package su.trident.cenchants.command.impl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchants.command.api.ArgumentExecutor;
import su.trident.cenchants.enchant.api.EnchantmentStorage;

public class RemoveEnchant extends ArgumentExecutor
{
    private final EnchantmentStorage storage;

    public RemoveEnchant(EnchantmentStorage storage)
    {
        this.storage = storage;
    }

    @Override
    public String getName()
    {
        return "remove";
    }

    @Override
    public String getUsage()
    {
        return "/ce remove";
    }

    @Override
    public String getDesc()
    {
        return "Удаляет чары с предмета";
    }

    @Override
    public void execute(Player player, String[] args)
    {
        try {
            final ItemStack item = player.getInventory().getItemInMainHand();
            storage.removeEnchantmentAll(item, true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9&lcEnchants:&r&a Чар удален."));

        } catch (Exception e) {
           sendHelp(player);
        }
    }
}
