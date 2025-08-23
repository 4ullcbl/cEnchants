package su.trident.cenchants.enchant.impl.enchant.blockbreak;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.context.blockbreak.BlockBreakContext;
import su.trident.cenchants.enchant.EnchantmentTarget;
import su.trident.cenchants.enchant.api.BlockBreakableEnchantment;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.util.block.BlockUtil;

import java.util.ArrayList;
import java.util.List;

public class Web extends Enchantment<BlockBreakEvent> implements BlockBreakableEnchantment
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
            if (!this.plugin.getWorldGuardUtil().canBreakBlock(context.getPlayer(), block)) continue;

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
}
