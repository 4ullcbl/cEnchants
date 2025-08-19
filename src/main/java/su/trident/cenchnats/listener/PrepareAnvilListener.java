package su.trident.cenchnats.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.api.Enchant;

import java.util.List;

public class PrepareAnvilListener implements Listener
{
    private final CEnchants plugin;

    public PrepareAnvilListener(CEnchants plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepare(PrepareAnvilEvent event)
    {
        final AnvilInventory inventory = event.getInventory();

        if (inventory.getFirstItem() == null || inventory.getSecondItem() == null) return;
        if (inventory.getSecondItem().getItemMeta() == null || !inventory.getSecondItem().hasItemMeta()) return;

        final ItemMeta secondMeta = inventory.getSecondItem().getItemMeta();
        final PersistentDataContainer pdc = secondMeta.getPersistentDataContainer();

        inventory.setRepairCost(Math.max(1, 10));
        if (pdc.isEmpty()) return;

        final List<Enchant<?>> enchantsOnSecondItem = this.plugin.getStorage().getAll(inventory.getSecondItem());

        if (enchantsOnSecondItem.isEmpty()) return;

        final ItemStack result = inventory.getFirstItem().clone();

        for (Enchant<?> e: enchantsOnSecondItem) {
            int level = this.plugin.getStorage().getLevelSave(inventory.getSecondItem(), e);

            if (level == 0) continue; // getLevelSave() возвращает 0 при отсутствии чара

            this.plugin.getStorage().addEnchantSave(result, e, level);
        }

        event.setResult(result);
    }
}
