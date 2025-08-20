package su.trident.cenchnats.enchant.impl.enchant.projectilehit;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;
import su.trident.cenchnats.enchant.api.Enchant;

public class Scout extends Enchant<ProjectileHitEvent>
{
    private final CEnchants plugin;
    private final String key;

    public Scout(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    @EventHandler
    public void usage(ProjectileHitEvent event)
    {
        if (!(event.getEntity() instanceof final Trident trident)) return;
        if (!hasEnchant(trident.getItem())) return;

        if (trident.getShooter() == null || event.getHitEntity() != null || !(trident.getShooter() instanceof LivingEntity shooter)) return;

        usageByLevel(shooter, getLevel(trident.getItem(), this));
    }

    private void usageByLevel(LivingEntity shooter, int level)
    {
        if (level == 0) return;

        if (level == 1) {
            scoutApply(shooter, 2.8);
        } else if (level == 2) {
            scoutApply(shooter, 3.5);
        } else {
            scoutApply(shooter, 4.5);
        }
    }

    private void scoutApply(LivingEntity player, double strenght)
    {
        final Vector vector = player.getLocation().getDirection().normalize();

        vector.multiply(strenght);
        vector.setY(0.6);

        player.setVelocity(vector);
    }

    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return "Скаут";
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
