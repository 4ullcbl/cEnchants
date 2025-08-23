package su.trident.cenchants.enchant.impl.enchant.blockbreak;

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

    private static final List<Material> magnitude = new ArrayList<>();

    static {
        magnitude.add(Material.IRON_INGOT);
        magnitude.add(Material.GOLD_INGOT);
        magnitude.add(Material.IRON_ORE);
        magnitude.add(Material.GOLD_ORE);
        magnitude.add(Material.DIAMOND);
        magnitude.add(Material.SAND);
        magnitude.add(Material.DIAMOND_ORE);
        magnitude.add(Material.LAPIS_ORE);
        magnitude.add(Material.LAPIS_LAZULI);
        magnitude.add(Material.COAL);
        magnitude.add(Material.COAL_ORE);
        magnitude.add(Material.ANCIENT_DEBRIS);
        magnitude.add(Material.GLASS);
        magnitude.add(Material.REDSTONE_ORE);
        magnitude.add(Material.NETHER_GOLD_ORE);
        magnitude.add(Material.EMERALD_ORE);
        magnitude.add(Material.OAK_LOG);
        magnitude.add(Material.SPRUCE_LOG);
        magnitude.add(Material.BIRCH_LOG);
        magnitude.add(Material.JUNGLE_LOG);
        magnitude.add(Material.ACACIA_LOG);
        magnitude.add(Material.DARK_OAK_LOG);
        magnitude.add(Material.CHARCOAL);
    }

    private final String key;
    private final CEnchants plugin;

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

        if (newDrop.size() != context.getDrops().size()) {
            context.getPlayer().playSound(context.getPlayer().getEyeLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
        }

        context.getDrops().clear();
        context.getDrops().addAll(newDrop);

    }

    @Override
    public void usage(BlockBreakEvent event)
    {
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
