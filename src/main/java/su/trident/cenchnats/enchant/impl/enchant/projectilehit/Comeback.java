package su.trident.cenchnats.enchant.impl.enchant.projectilehit;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;
import su.trident.cenchnats.enchant.api.Enchant;

import java.util.Random;

public class Comeback extends Enchant<ProjectileHitEvent>
{
    private final static Random random = new Random();

    private final CEnchants plugin;
    private final String key;

    public Comeback(String key, CEnchants plugin)
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

        if (trident.getShooter() == null || !(trident.getShooter() instanceof Player shooter)) return;
        final ItemStack tridentItem = trident.getItem();

        if (random.nextDouble() <= 0.5) return;
        trident.remove();
        shooter.getInventory().addItem(tridentItem);
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
    public EnchantTarget getTarget()
    {
        return EnchantTarget.TRIDENT;
    }
}
