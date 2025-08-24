package su.trident.cenchants.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.enchant.api.EnchantmentStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PrepareAnvilListener implements Listener
{
    private final EnchantmentStorage storage;

    public PrepareAnvilListener(EnchantmentStorage storage)
    {
        this.storage = storage;
    }

    @EventHandler
    public void onPrepare(PrepareAnvilEvent event)
    {
        final AnvilInventory inventory = event.getInventory();
        if (!isCustomEnchantEvent(inventory)) return;

        final List<Enchantment<?>> enchantsOnSecondItem = storage.getEnchantmentList(inventory.getSecondItem());
        if (enchantsOnSecondItem.isEmpty()) return;

        final ItemStack result = Objects.requireNonNull(inventory.getFirstItem()).clone();
        addEnchantLogic(event, enchantsOnSecondItem, inventory, result);
    }

    private void addEnchantLogic(PrepareAnvilEvent event, List<Enchantment<?>> enchantsOnSecondItem, AnvilInventory inventory, ItemStack result)
    {
        int cost = 1;

        for (Enchantment<?> e: enchantsOnSecondItem) {
            int level = storage.getLevel(inventory.getSecondItem(), e);

            if (!e.getTarget().isTarget(result.getType())) {
                event.setResult(null);
                return;
            }

            if (level == 0) return; // getLevelSave() возвращает 0 при отсутствии чара

            storage.addEnchantment(result, e, level);
            cost += e.getAnvilCost() + level;
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
