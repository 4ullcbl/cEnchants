package su.trident.cenchnats.enchant.impl.enchant;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;
import su.trident.cenchnats.enchant.api.Enchant;
import su.trident.cenchnats.util.durability.DurabilityOptions;

public class Ping extends Enchant<PlayerItemDamageEvent>
{

    private final String key;
    private final CEnchants plugin;

    public Ping(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @EventHandler
    @Override
    public void usage(PlayerItemDamageEvent event)
    {
        final ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();

        if (!hasEnchant(itemInMainHand))
            return;

        if (DurabilityOptions.isLowDurability(event.getItem(), 25)) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
        }
    }

    @Override
    public int getPriority()
    {
        return 50;
    }

    @Override
    public String getName()
    {
        return "Pinger";
    }

    @Override
    public int getStartLvl()
    {
        return 1;
    }

    @Override
    public int getMaxLvl()
    {
        return 1;
    }

    @Override
    public String getKey()
    {
        return this.key;
    }

    @Override
    public EnchantTarget getTarget()
    {
        return EnchantTarget.ALL;
    }
}
