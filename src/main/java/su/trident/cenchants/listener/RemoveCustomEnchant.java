package su.trident.cenchants.listener;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchants.enchant.api.EnchantmentStorage;

public class RemoveCustomEnchant implements Listener
{
    private final EnchantmentStorage storage;

    public RemoveCustomEnchant(EnchantmentStorage storage)
    {
        this.storage = storage;
    }

    @EventHandler
    public void onRemoveEnchant(PrepareResultEvent event)
    {
        if (!(event.getInventory() instanceof final GrindstoneInventory inv)) return;

        final ItemStack first = inv.getUpperItem();
        final ItemStack second = inv.getLowerItem();
        ItemStack result;

        if (second != null && first != null && (first.getType() == second.getType())) {
            result = second.clone();
            storage.removeEnchantAll(result, true);
            event.setResult(result);
        }

        if (first == null && second != null) {
            result = second.clone();
            storage.removeEnchantAll(result, true);

            event.setResult(result);
            return;
        }

        if (first == null) return;

        result = first.clone();
        storage.removeEnchantAll(result, true);

        event.setResult(result);

    }
}
