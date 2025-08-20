package su.trident.cenchnats.enchant.impl.enchant.projectilehit;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;
import su.trident.cenchnats.enchant.api.Enchant;

public class Sniper extends Enchant<EntityShootBowEvent>
{
    private final String key;
    private final CEnchants plugin;

    public Sniper(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.plugin = plugin;
        this.key = key;
    }

    @Override
    @EventHandler
    public void usage(EntityShootBowEvent event)
    {
        if (!hasEnchant(event.getBow())) return;

        final Arrow arrow = (Arrow) event.getProjectile();

        usageByLevel(arrow, getLevel(event.getBow(), this));
    }

    private void usageByLevel(Arrow arrow, int level)
    {
        if (level == 0) return;

        if (level == 1) {
            arrow.setVelocity(arrow.getVelocity().multiply(1.5));
        } else {
            arrow.setVelocity(arrow.getVelocity().multiply(2.3));
        }
    }

    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return "Снайпер";
    }

    @Override
    public int getStartLvl()
    {
        return 1;
    }

    @Override
    public int getMaxLvl()
    {
        return 2;
    }

    @Override
    public String getKey()
    {
        return this.key;
    }

    @Override
    public EnchantTarget getTarget()
    {
        return EnchantTarget.BOW;
    }

    @Override
    public int getChance()
    {
        return 10;
    }
}
