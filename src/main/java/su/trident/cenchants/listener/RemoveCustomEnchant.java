package su.trident.cenchants.listener;

import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.enchant.api.EnchantmentStorage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

            storage.removeEnchantmentAll(result, true);

            transferCurses(first, result);
            event.setResult(result);
        }

        if (first == null && second != null) {
            result = second.clone();
            storage.removeEnchantmentAll(result, true);

            event.setResult(result);
            return;
        }

        if (first == null) return;

        result = first.clone();
        storage.removeEnchantmentAll(result, true);

        event.setResult(result);
    }

    private void transferCurses(ItemStack toTransfer, ItemStack result)
    {
        final Map<Enchantment<?>, Integer> curses = getCurseEnchantment(toTransfer);
        if (!curses.isEmpty()) {
            curses.forEach((e, level) -> {
                storage.addEnchantment(result, e, level);
            });
        }
    }

    /*private List<Enchantment<?>> getCurseEnchantmentList(ItemStack stack)
    {
        return storage.getEnchantmentList(stack).stream()
                .filter(Enchantment::isCurse).toList();
    }*/

    private Map<Enchantment<?>, Integer> getCurseEnchantment(ItemStack stack)
    {
        return storage.getAll(stack).entrySet().stream()
                .filter(entry -> entry.getKey().isCurse())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }
}
