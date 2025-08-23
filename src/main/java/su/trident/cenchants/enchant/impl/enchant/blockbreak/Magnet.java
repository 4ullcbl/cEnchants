package su.trident.cenchants.enchant.impl.enchant.blockbreak;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.context.blockbreak.BlockBreakContext;
import su.trident.cenchants.enchant.EnchantmentTarget;
import su.trident.cenchants.enchant.api.BlockBreakableEnchantment;
import su.trident.cenchants.enchant.api.Enchantment;

import java.util.*;

public class Magnet extends Enchantment<BlockBreakEvent> implements BlockBreakableEnchantment
{

    protected static final List<Material> magnitude = new ArrayList<>();

    private final String key;
    private final CEnchants plugin;

    private Sound pickupSound;
    private float yaw;
    private float pitch;

    public Magnet(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    public void apply(BlockBreakContext context)
    {
        final ItemStack tool = context.getTool();

        if (!hasEnchant(tool)) return;

        final List<ItemStack> newDrop = new ArrayList<>();

        for (ItemStack i : context.getDrops()) {
            if (magnitude.contains(i.getType())) {
                context.getPlayer().getInventory().addItem(i);
            } else {
                newDrop.add(i);
            }
        }

        playMagnetEffects(context, newDrop);

        context.getDrops().clear();
        context.getDrops().addAll(newDrop);

    }

    private void playMagnetEffects(BlockBreakContext context, List<ItemStack> newDrop)
    {
        if (newDrop.size() != context.getDrops().size()) {
            context.getPlayer().playSound(context.getPlayer().getEyeLocation(), pickupSound, yaw, pitch);
        }
    }

    @Override
    public void loadConfig()
    {
        loadConfigPath();
        loadDefaultValue();

        pickupSound = Sound.valueOf(getConfig().getString(getConfigPath() + "sound.type"));
        yaw = (float) getConfig().getDouble(getConfigPath() + "sound.yaw");
        pitch = (float) getConfig().getDouble(getConfigPath() + "sound.pitch");
        clearMagnitude();
        initMagnitude();
    }

    private void initMagnitude()
    {
        final List<String> configMagnitude = getConfig().getStringList(getConfigPath() + "magnitude");

        configMagnitude.stream()
                .map(String::trim)
                .filter(str -> !str.isEmpty())
                .forEach(materialName -> {
                    try {
                        magnitude.add(Material.valueOf(materialName.toUpperCase()));
                        plugin.getLogger().info(ChatColor.DARK_GRAY + "DEBUG: magnet -> add value: " + materialName.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().info(ChatColor.RED + "DEBUG: magnet -> Неизвестный материал: " + materialName);
                    }
                });
    }

    @Override
    public void usage(BlockBreakEvent event)
    {
    }

    public static void clearMagnitude() {
        magnitude.clear();
    }

    @Override
    public int getPriority()
    {
        return 100;
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
        return key;
    }

    @Override
    public EnchantmentTarget getTarget()
    {
        return EnchantmentTarget.TOOLS;
    }

    public static List<Material> getMagnitude()
    {
        return magnitude;
    }
}
