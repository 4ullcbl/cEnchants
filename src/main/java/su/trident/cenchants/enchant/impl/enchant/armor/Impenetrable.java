package su.trident.cenchants.enchant.impl.enchant.armor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.config.SoundLoader;
import su.trident.cenchants.enchant.EnchantmentTarget;
import su.trident.cenchants.enchant.api.Enchantment;

import java.util.Random;

public class Impenetrable extends Enchantment<EntityDamageEvent>
{

    private static final Random random = new Random();

    private final CEnchants plugin;
    private final String key;

    private double protectionPerPiece;
    private double maxProtection;
    private double chanceToProtection;
    private SoundLoader effects;

    public Impenetrable(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.plugin = plugin;
        this.key = key;
    }

    @Override
    @EventHandler
    public void usage(EntityDamageEvent event)
    {
        if (!(event.getEntity() instanceof Player player)) return;
        if (event.isCancelled()) return;

        final int armorPieces = updateArmorCount(player);

        if (armorPieces > 0 && random.nextDouble() <= chanceToProtection) {
            final double damage = event.getDamage();
            double reduction = calculateDamageReduction(armorPieces);
            double newDamage = damage * (1 - reduction);

            event.setDamage(newDamage);
            player.playSound(player.getLocation(), effects.getSound(), effects.getYaw(), effects.getPitch());

            plugin.getLogger().info(String.format(
                    "Игрок %s получил урон: %.1f -> %.1f (редукция: %.0f%%, части брони: %d)",
                    player.getName(), damage, newDamage, reduction * 100, armorPieces
            ));
        }
    }

    private int updateArmorCount(Player player)
    {
        final ItemStack[] armor = player.getInventory().getArmorContents();
        int count = 0;

        for (ItemStack piece : armor) {
            if (piece != null && piece.getType() != Material.AIR && isArmor(piece.getType()) && hasEnchant(piece)) {
                count++;
            }
        }

        return count;
    }

    private boolean isArmor(Material material)
    {
        return material.name().endsWith("_HELMET") ||
                material.name().endsWith("_CHESTPLATE") ||
                material.name().endsWith("_LEGGINGS") ||
                material.name().endsWith("_BOOTS");
    }

    private double calculateDamageReduction(int armorPieces)
    {
        return Math.min(armorPieces * protectionPerPiece, maxProtection);
    }

    @Override
    public void loadConfig()
    {
        loadDefaultValue();
        protectionPerPiece = getConfig().getDouble(getConfigPath() + "protection_piece");
        maxProtection = getConfig().getDouble(getConfigPath() + "max_protection");
        chanceToProtection = getConfig().getDouble(getConfigPath() + "chance_protection");
        effects = new SoundLoader(getConfig(), getConfigPath() + "sound", plugin);
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
    public EnchantmentTarget getTarget()
    {
        return EnchantmentTarget.ARMOR;
    }

    @Override
    public String getKey()
    {
        return this.key;
    }
}
