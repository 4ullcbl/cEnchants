package su.trident.cenchnats.enchant.impl.enchant.projectilehit;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;
import su.trident.cenchnats.enchant.api.Enchant;

public class Stupor extends Enchant<ProjectileHitEvent>
{

    private final String key;
    private final CEnchants plugin;

    public Stupor(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    @EventHandler
    public void usage(ProjectileHitEvent event)
    {
        if (!(event.getEntity() instanceof final Trident trident) || !(event.getHitEntity() instanceof final LivingEntity entity))
            return;

        final ItemStack tridentItem = trident.getItem();

        if (!hasEnchant(tridentItem))
            return;

        usageByLevel(entity, getLevel(tridentItem, this));
    }


    private void usageByLevel(LivingEntity target, int level)
    {
        if (level == 0) return;

        if (level == 1) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 2, 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 2, 0));
        } else if (level == 2) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 4, 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 4, 0));
        } else {
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 5, 1));
        }
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
