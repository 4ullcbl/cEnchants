package su.trident.cenchnats.listener;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchnats.CEnchants;

public class RemoveCustomEnchant implements Listener
{
    private final CEnchants plugin;

    public RemoveCustomEnchant(CEnchants plugin)
    {
        this.plugin = plugin;
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
            this.plugin.getStorage().removeEnchantAll(result, true);
            event.setResult(result);

        }

        if (first == null && second != null) {
            result = second.clone();
            this.plugin.getStorage().removeEnchantAll(result, true);

            event.setResult(result);
            return;
        }

        if (first == null) return;

        result = first.clone();
        this.plugin.getStorage().removeEnchantAll(result, true);

        event.setResult(result);

    }
}
