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
import java.util.List;

public class Web extends Enchant<BlockBreakEvent> implements BlockBreakableEnchant
{
    private final String key;
    private final CEnchants plugin;

    public Web(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    public void apply(BlockBreakContext context)
    {
        if (!hasEnchant(context.getTool())) return;
        if (!BlockUtil.getOres().contains(context.getOriginBlock().getType())) return;

        final List<Block> toBreak = BlockUtil.findNearestBlock(context.getOriginBlock(), 1, 30);

        if (toBreak == null) return;

        if (toBreak.size() <= 1) return;

        context.getAffectedBlocks().addAll(toBreak);

        final List<ItemStack> dropToAdd = new ArrayList<>();

        for (Block block : context.getAffectedBlocks()) {
            if (block.equals(context.getOriginBlock())) continue;
            dropToAdd.addAll(block.getDrops(context.getTool()));
        }

        context.getDrops().addAll(dropToAdd);
        visualWeb(context);
    }

    private void visualWeb(BlockBreakContext context)
    {
        for (Block block : context.getAffectedBlocks()) {
            if (block.getType() == Material.AIR || block.isEmpty() || BlockUtil.getUnbreakable().contains(block.getType()) || block.getType().isEmpty() || block.getType().isAir())
                continue;
            block.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, BlockUtil.getCenter(block), 1, 0.2, 0.03, 0.03, 0.03);
        }
    }

    @Override
    public void usage(BlockBreakEvent event)
    {
    }

    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return "Паутина";
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
}
