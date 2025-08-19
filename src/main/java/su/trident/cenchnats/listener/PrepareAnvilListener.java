package su.trident.cenchnats.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.api.Enchant;

import java.util.List;
import java.util.Objects;

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
        if (!isCustomEnchantEvent(inventory)) return;

        final List<Enchant<?>> enchantsOnSecondItem = this.plugin.getStorage().getAll(inventory.getSecondItem());
        if (enchantsOnSecondItem.isEmpty()) return;

        final ItemStack result = Objects.requireNonNull(inventory.getFirstItem()).clone();
        addEnchantLogic(event, enchantsOnSecondItem, inventory, result);
    }

    private void addEnchantLogic(PrepareAnvilEvent event, List<Enchant<?>> enchantsOnSecondItem, AnvilInventory inventory, ItemStack result)
    {
        int cost = 1;

        for (Enchant<?> e: enchantsOnSecondItem) {
            int level = this.plugin.getStorage().getLevelSave(inventory.getSecondItem(), e);

            if (level == 0 || !e.getTarget().isType(result.getType())) continue; // getLevelSave() возвращает 0 при отсутствии чара

            this.plugin.getStorage().addEnchantSave(result, e, level);
            cost += level;
        }

        inventory.setRepairCost(Math.min(cost, 39));
        event.setResult(result);
    }

    private boolean isCustomEnchantEvent(AnvilInventory inventory)
    {
        if (inventory.getFirstItem() == null || inventory.getSecondItem() == null) return false;
        if (inventory.getSecondItem().getItemMeta() == null || !inventory.getSecondItem().hasItemMeta()) return false;

        final PersistentDataContainer pdc = inventory.getSecondItem().getItemMeta().getPersistentDataContainer();

        return !pdc.isEmpty();
    }
}
