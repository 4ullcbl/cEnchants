package su.trident.cenchnats.enchant.impl.enchant.blockbreak;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.context.blockbreak.BlockBreakContext;
import su.trident.cenchnats.enchant.api.BlockBreakableEnchant;
import su.trident.cenchnats.enchant.api.Enchant;
import su.trident.cenchnats.util.block.BlockUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Melting extends Enchant<BlockBreakEvent> implements BlockBreakableEnchant
{
    private final CEnchants plugin;
    private final String key;

    private static final Map<Material, ItemStack> melted = new HashMap<>();

    static {
        melted.put(Material.IRON_ORE, new ItemStack(Material.IRON_INGOT));
        melted.put(Material.GOLD_ORE, new ItemStack(Material.GOLD_INGOT));
        melted.put(Material.SAND, new ItemStack(Material.GLASS));
        melted.put(Material.COBBLESTONE, new ItemStack(Material.STONE));
        melted.put(Material.STONE, new ItemStack(Material.STONE));
        melted.put(Material.OAK_LOG, new ItemStack(Material.CHARCOAL));
    }

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

            block.getWorld().spawnParticle(Particle.FLAME, BlockUtil.getCenter(block), 5, 0.028, 0.045, 0.045, 0.045);
        }
    }

    @Override
    public void usage(BlockBreakEvent event)
    {
    }

    @Override
    public int getPriority()
    {
        return 70;
    }

    @Override
    public String getName()
    {
        return "Auto-Melting";
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

    public static Map<Material, ItemStack> getMelted()
    {
        return melted;
    }
}
