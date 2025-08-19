package su.trident.cenchnats.enchant.impl.enchant.attack;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;
import su.trident.cenchnats.enchant.api.Enchant;

import java.util.Random;

public class Poison extends Enchant<EntityDamageByEntityEvent>
{

    private static final Random random = new Random();

    private final CEnchants plugin;
    private final String key;

    public Poison(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    @EventHandler
    public void usage(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof final LivingEntity target) || !(event.getDamager() instanceof final Player source))
            return;

        if (!hasEnchant(source.getInventory().getItemInMainHand()))
            return;

        usageByLevel(target, getLevel(source.getInventory().getItemInMainHand(), this));
    }


    private void usageByLevel(LivingEntity target, int level)
    {
        if (level == 0) return;

        if (level == 1 && random.nextDouble() >= 0.5) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 2, 1));
        }
        else if (level == 2 && random.nextDouble() >= 0.5) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 3, 1));
        }
        else if (level > 2 && random.nextDouble() >= 0.5) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 20 * 4, 1));
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
        return "Яд";
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
        return EnchantTarget.WEAPON;
    }
}
