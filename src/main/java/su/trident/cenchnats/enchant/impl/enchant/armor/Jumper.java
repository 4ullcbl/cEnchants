package su.trident.cenchnats.enchant.impl.enchant.armor;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;
import su.trident.cenchnats.enchant.api.Enchant;

public class Jumper extends Enchant<PlayerArmorChangeEvent>
{

    private final CEnchants plugin;
    private final String key;

    public Jumper(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    @EventHandler
    public void usage(PlayerArmorChangeEvent event)
    {
        if (event.getSlotType() != PlayerArmorChangeEvent.SlotType.FEET) return;
        if (event.getNewItem() == null || event.getOldItem() == null) return;

        boolean isNewJumpBoots = hasEnchant(event.getNewItem());
        boolean isOldJumpBoots = hasEnchant(event.getOldItem());

        if (isOldJumpBoots && !isNewJumpBoots) {
            event.getPlayer().removePotionEffect(PotionEffectType.JUMP);
            return;
        }

        if (isNewJumpBoots) {
            event.getPlayer().addPotionEffect(
                    new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 0, false, false, false)
            );
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
        return "Попрыгун";
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
        return EnchantTarget.BOOTS;
    }

    @Override
    public int getChance()
    {
        return 10;
    }
}
