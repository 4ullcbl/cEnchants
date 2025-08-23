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

public class Detection extends Enchantment<EntityDamageByEntityEvent>
{

    private static final Random random = new Random();

    private final CEnchants plugin;
    private final String key;

    private int chanceFirst;
    private int chanceSecond;
    private int chanceThird;

    private int durationFirst;
    private int durationSecond;
    private int durationThird;

    private int amplifierFirst;
    private int amplifierSecond;
    private int amplifierThird;

    public Detection(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    @EventHandler
    public void usage(EntityDamageByEntityEvent event)
    {
        if (!(event.getEntity() instanceof final Player target) || !(event.getDamager() instanceof Player source))
            return;

        if (!hasEnchant(source.getInventory().getItemInMainHand()))
            return;

        usageByLevel(target, getLevel(source.getInventory().getItemInMainHand(), this));
    }


    private void usageByLevel(LivingEntity target, int level)
    {
        if (level == 0) return;

        if (level == 1 && random.nextDouble() <= chanceFirst) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * durationFirst, amplifierFirst));
        } else if (level == 2 && random.nextDouble() <= chanceSecond) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * durationSecond, amplifierSecond));
        } else if (level > 2 && random.nextDouble() <= chanceThird) {
            target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * durationThird, amplifierThird));
        }
    }

    @Override
    public void loadConfig()
    {
        loadConfigPath();
        loadDefaultValue();

        chanceFirst = getConfig().getInt(getConfigPath() + "first.effect_chance");
        chanceSecond = getConfig().getInt(getConfigPath() + "second.effect_chance");
        chanceThird = getConfig().getInt(getConfigPath() + "third.effect_chance");

        durationFirst = getConfig().getInt(getConfigPath() + "first.effect_duration");
        durationSecond = getConfig().getInt(getConfigPath() + "second.effect_duration");
        durationThird = getConfig().getInt(getConfigPath() + "third.effect_duration");

        amplifierFirst = getConfig().getInt(getConfigPath() + "first.effect_amplifier") - 1;
        amplifierSecond = getConfig().getInt(getConfigPath() + "second.effect_amplifier") - 1;
        amplifierThird = getConfig().getInt(getConfigPath() + "third.effect_amplifier") - 1;
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
    public EnchantmentTarget getTarget()
    {
        return EnchantmentTarget.WEAPON;
    }
}

