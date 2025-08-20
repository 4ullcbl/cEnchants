package su.trident.cenchnats.enchant.impl.enchant.projectilehit;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;
import su.trident.cenchnats.enchant.api.Enchant;
import su.trident.cenchnats.util.entity.EntityUtil;

public class Pulling extends Enchant<ProjectileHitEvent>
{

    private final String key;
    private final CEnchants plugin;

    public Pulling(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.plugin = plugin;
        this.key = key;
    }

    @Override
    @EventHandler
    public void usage(ProjectileHitEvent event)
    {
        if (!(event.getEntity() instanceof final Trident trident)) return;
        if (!hasEnchant(trident.getItem())) return;

        if (trident.getShooter() == null || event.getHitEntity() == null || !(trident.getShooter() instanceof LivingEntity shooter)) return;
        if (!(event.getHitEntity() instanceof LivingEntity target)) return;

        usageByLevel(shooter, target, getLevel(trident.getItem(), this));
    }

    private void usageByLevel(LivingEntity shooter, LivingEntity target, int level)
    {
        if (level == 0) return;

        if (level == 1) {
            EntityUtil.smoothTeleport(target, shooter, 3.5);
        } else {
            EntityUtil.smoothTeleport(target, shooter, 5.5);
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
        return "Притяжение";
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
        return EnchantTarget.TRIDENT;
    }

    @Override
    public int getChance()
    {
        return 6;
    }
}
