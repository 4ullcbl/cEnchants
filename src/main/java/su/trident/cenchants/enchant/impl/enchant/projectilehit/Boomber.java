package su.trident.cenchants.enchant.impl.enchant.projectilehit;

import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.enchant.EnchantmentTarget;
import su.trident.cenchants.enchant.api.Enchantment;

import java.util.Random;

public class Boomber extends Enchantment<ProjectileHitEvent>
{
    private static final Random random = new Random();

    private final CEnchants plugin;
    private final String key;

    public Boomber(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.plugin = plugin;
        this.key = key;
    }

    @Override
    @EventHandler
    public void usage(ProjectileHitEvent event)
    {
        if (!(event.getEntity() instanceof Trident trident)) return;
        if (!hasEnchant(trident.getItem())) return;

        if (random.nextDouble() <= 0.5) return;

        trident.getWorld().createExplosion(trident.getLocation(), 3f, false, false);
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
        return EnchantmentTarget.TRIDENT;
    }
}
