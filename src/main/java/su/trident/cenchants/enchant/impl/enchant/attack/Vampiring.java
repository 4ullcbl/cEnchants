package su.trident.cenchants.enchant.impl.enchant.attack;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.enchant.EnchantmentTarget;
import su.trident.cenchants.enchant.api.Enchantment;

import java.util.Random;

public class Vampiring extends Enchantment<EntityDamageByEntityEvent>
{

    private static final Random random = new Random();

    private final CEnchants plugin;
    private final String key;

    public Vampiring(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    @EventHandler
    public void usage(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof final Player source))
            return;

        if (!hasEnchant(source.getInventory().getItemInMainHand()))
            return;

        usageByLevel(source, getLevel(source.getInventory().getItemInMainHand(), this));
    }


    private void usageByLevel(LivingEntity source, int level)
    {
        if (level == 0) return;

        if (level == 1 && random.nextDouble() >= 0.5) {
            source.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 2, 1));
        }
        else if (level == 2 && random.nextDouble() >= 0.5) {
            source.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 3, 1));
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
        return 2;
    }

    @Override
    public String getKey()
    {
        return this.key;
    }

    @Override
    public EnchantmentTarget getTarget()
    {
        return EnchantmentTarget.WEAPON;
    }
}
