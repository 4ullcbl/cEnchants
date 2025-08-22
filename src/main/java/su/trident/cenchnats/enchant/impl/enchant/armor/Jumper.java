package su.trident.cenchnats.enchant.impl.enchant.armor;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;
import su.trident.cenchnats.enchant.api.Enchant;

import java.util.Objects;

public class Jumper extends Enchant<PlayerArmorChangeEvent>
{
    private final CEnchants plugin;
    private final String key;

    private PotionEffect effect;

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
        if (event.getSlotType() != PlayerArmorChangeEvent.SlotType.FEET || event.getNewItem() == null || event.getOldItem() == null) return;

        boolean isNewJumpBoots = hasEnchant(event.getNewItem());
        boolean isOldJumpBoots = hasEnchant(event.getOldItem());

        if (isOldJumpBoots && !isNewJumpBoots) {
            event.getPlayer().removePotionEffect(effect.getType());
            return;
        }

        if (isNewJumpBoots) {
            event.getPlayer().addPotionEffect(effect);
        }
    }

    @Override
    public void loadConfig()
    {
        loadConfigPath();
        loadDefaultValue();
        effect = new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(getConfig().getString(getConfigPath() + "potion_effect", "JUMP"))),Integer.MAX_VALUE, getConfig().getInt(getConfigPath() + "effect_amplifier") - 1, false, false, false);
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
        return EnchantTarget.BOOTS;
    }
}
