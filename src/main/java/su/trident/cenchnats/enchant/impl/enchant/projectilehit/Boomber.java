package su.trident.cenchnats.enchant.impl.enchant.projectilehit;

import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;
import su.trident.cenchnats.enchant.api.Enchant;

import java.util.Random;

public class Boomber extends Enchant<ProjectileHitEvent>
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
    public String getName()
    {
        return "Подрывник";
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
        return EnchantTarget.TRIDENT;
    }
}
