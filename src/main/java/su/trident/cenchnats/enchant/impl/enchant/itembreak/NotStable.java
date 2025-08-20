package su.trident.cenchnats.enchant.impl.enchant.itembreak;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;
import su.trident.cenchnats.enchant.api.Enchant;

public class NotStable extends Enchant<PlayerItemDamageEvent>
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
    public String getName()
    {
        return "Нестабильный";
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
    public EnchantTarget getTarget()
    {
        return EnchantTarget.ALL;
    }

    @Override
    public int getChance()
    {
        return 5;
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
