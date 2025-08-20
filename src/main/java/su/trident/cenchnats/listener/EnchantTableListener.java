package su.trident.cenchnats.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.api.Enchant;

import java.util.List;

public class EnchantTableListener implements Listener
{
    private final CEnchants plugin;

    public EnchantTableListener(CEnchants plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event)
    {
        if (event.getExpLevelCost() < 16) return;
        final List<Enchant<?>> toAdd = plugin.getStorage().getRandom();

        for (Enchant<?> e : toAdd) {
            plugin.getStorage().addEnchantSave(event.getItem(), e, plugin.getStorage().getRandomLevel(e));
        }
    }
}
