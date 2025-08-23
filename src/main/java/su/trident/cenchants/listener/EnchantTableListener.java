package su.trident.cenchants.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.enchant.api.EnchantmentStorage;

import java.util.List;

public class EnchantTableListener implements Listener
{
    private final EnchantmentStorage storage;

    public EnchantTableListener(EnchantmentStorage storage)
    {
        this.storage = storage;
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event)
    {
        final List<Enchantment<?>> toAdd = storage.getRandom();

        for (Enchantment<?> e : toAdd) {
            if (event.getExpLevelCost() < e.getMinTableLevel()) continue;

            storage.addEnchantSave(event.getItem(), e, storage.getRandomLevel(e));
        }
    }
}
