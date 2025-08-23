package su.trident.cenchants.enchant.impl.enchant.itembreak;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.enchant.EnchantmentTarget;
import su.trident.cenchants.enchant.api.Enchantment;

public class NotStable extends Enchantment<PlayerItemDamageEvent>
{
    private final CEnchants plugin;
    private final String key;

    public NotStable(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    @EventHandler
    public void usage(PlayerItemDamageEvent event)
    {
        final ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();

        if (!hasEnchant(itemInMainHand))
            return;

        event.setDamage(multiplyDamageByLevel(event.getDamage(), getLevel(itemInMainHand, this)));
    }

    private int multiplyDamageByLevel(int currentDamage, int level)
    {
        if (level == 0) return currentDamage;

        switch (level) {
            case 1, 2 -> {
                return currentDamage * 2;
            }
            case 3 -> {
                return currentDamage * 3;
            }
        }

        return currentDamage;
    }

    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public int getStartLvl()
    {
        return 1;
    }

    @Override
    public int getMaxLvl()
    {
        return 3;
    }

    @Override
    public EnchantmentTarget getTarget()
    {
        return EnchantmentTarget.ALL;
    }

    @Override
    public boolean isCurse()
    {
        return true;
    }

    @Override
    public String getKey()
    {
        return this.key;
    }
}
