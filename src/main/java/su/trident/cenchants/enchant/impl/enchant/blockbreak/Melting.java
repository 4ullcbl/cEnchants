package su.trident.cenchants.enchant.impl.enchant.blockbreak;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.context.blockbreak.BlockBreakContext;
import su.trident.cenchants.enchant.EnchantmentTarget;
import su.trident.cenchants.enchant.api.BlockBreakableEnchantment;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.util.block.BlockUtil;

import java.util.*;

public class Melting extends Enchantment<BlockBreakEvent> implements BlockBreakableEnchantment
{
    private final CEnchants plugin;
    private final String key;

    private static final Map<Material, ItemStack> melted = new HashMap<>();

    private Particle particleType;
    private int count;
    private double speed;
    private double deltaX;
    private double deltaY;
    private double deltaZ;

    public Melting(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.plugin = plugin;
        this.key = key;
    }

    @Override
    public void apply(BlockBreakContext context)
    {
        final ItemStack tool = context.getTool();
        if (!hasEnchant(tool)) return;

        final List<ItemStack> toRemove = new ArrayList<>();
        final List<ItemStack> toAdd = new ArrayList<>();

        for (ItemStack item : context.getDrops()) {

            if (melted.get(item.getType()) != null) {
                final ItemStack melt = melted.get(item.getType());
                melt.setAmount(item.getAmount());

                toRemove.add(item);
                toAdd.add(melt);
            }
        }
        context.getDrops().removeAll(toRemove);
        context.getDrops().addAll(toAdd);
        visualMelt(context);
    }

    private void visualMelt(BlockBreakContext context)
    {
        for (Block block : context.getAffectedBlocks()) {
            if (block.getType() == Material.AIR || block.isEmpty() || BlockUtil.getUnbreakable().contains(block.getType()) || block.getType().isEmpty() || block.getType().isAir())
                continue;

            block.getWorld().spawnParticle(particleType, BlockUtil.getCenter(block), count, speed, deltaX, deltaY, deltaZ);
        }
    }

    @Override
    public void loadConfig()
    {
        loadConfigPath();
        loadDefaultValue();

        particleType = Particle.valueOf(getConfig().getString(getConfigPath() + "particle.type"));
        count = getConfig().getInt(getConfigPath() + "particle.count");
        speed = getConfig().getDouble(getConfigPath() + "particle.speed");
        deltaX = getConfig().getDouble(getConfigPath() + "particle.delta_x");
        deltaY = getConfig().getDouble(getConfigPath() + "particle.delta_y");
        deltaZ = getConfig().getDouble(getConfigPath() + "particle.delta_z");
        clearMelted();
        loadMelted();
    }

    private void loadMelted() {
        final ConfigurationSection section = getConfig().getConfigurationSection(getConfigPath() + "melted");

        if (section != null) {
            for (String keyName : section.getKeys(false)) {
                putToMelted(keyName, section);
                plugin.getLogger().info(ChatColor.DARK_GRAY + "DEBUG: melted put -> key: " + keyName + ", value: " + section.getString(keyName));
            }
        }
    }

    private void putToMelted(String keyName, ConfigurationSection section)
    {
        final String valueName = section.getString(keyName);

        if (valueName == null) return;

        final Material keyMaterial = Material.getMaterial(keyName.toUpperCase());
        final ItemStack value = new ItemStack(Objects.requireNonNull(Material.getMaterial(valueName.toUpperCase())));

        if (keyMaterial != null) {
            melted.put(keyMaterial, value);
        } else {
            plugin.getLogger().info(ChatColor.RED + "melted -> Неверный материал: " + keyName + " -> " + valueName);
        }
    }

    @Override
    public void usage(BlockBreakEvent event)
    {
    }

    public static void clearMelted() {
        melted.clear();
    }

    @Override
    public int getPriority()
    {
        return 70;
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
    public EnchantmentTarget getTarget()
    {
        return EnchantmentTarget.TOOLS;
    }

    public static Map<Material, ItemStack> getMelted()
    {
        return melted;
    }
}
