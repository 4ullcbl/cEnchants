package su.trident.cenchants.enchant.impl.enchant.itembreak;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.config.SoundLoader;
import su.trident.cenchants.enchant.EnchantmentTarget;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.util.durability.DurabilityOptions;

public class Ping extends Enchantment<PlayerItemDamageEvent>
{
    private final String key;
    private final CEnchants plugin;
    private int percentToPing;
    private SoundLoader effects;

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

        if (DurabilityOptions.isLowDurability(event.getItem(), percentToPing)) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), effects.getSound(), effects.getYaw(), effects.getPitch());
        }
    }

    @Override
    public void loadConfig()
    {
        loadDefaultValue();
        effects = new SoundLoader(getConfig(), getConfigPath() + "sound", this.plugin);
        percentToPing = getConfig().getInt(getConfigPath() + "percent_to_ping");
    }

    @Override
    public int getPriority()
    {
        return 50;
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
    public EnchantmentTarget getTarget()
    {
        return EnchantmentTarget.ALL;
    }
}
