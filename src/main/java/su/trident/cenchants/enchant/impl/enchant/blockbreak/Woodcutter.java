package su.trident.cenchants.enchant.impl.enchant.blockbreak;

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

public class Woodcutter extends Enchantment<BlockBreakEvent> implements BlockBreakableEnchantment
{
    private final CEnchants plugin;
    private final String key;

    public Woodcutter(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.plugin = plugin;
        this.key = key;
    }

    @Override
    public void apply(BlockBreakContext context)
    {
        if (!hasEnchant(context.getTool()))
            return;

        if (!context.getOriginBlock().getType().toString().contains("_LOG") && !context.getOriginBlock().getType().toString().contains("_WOOD"))
            return;

        final List<Block> toBreak = getBlocksToBreak(context);

        if (toBreak.isEmpty() || toBreak.size() == 1) return;

        context.getAffectedBlocks().addAll(toBreak);

        final List<ItemStack> dropToAdd = new ArrayList<>();

        for (Block block : context.getAffectedBlocks()) {
            if (!this.plugin.getWorldGuardUtil().canBreakBlock(context.getPlayer(), block)) continue;

            if (block.equals(context.getOriginBlock())) continue;
            dropToAdd.addAll(block.getDrops(context.getTool()));
        }

        context.getDrops().addAll(dropToAdd);
    }

    private List<Block> getBlocksToBreak(BlockBreakContext context)
    {
        boolean isFirstMode = hasEnchant(context.getTool(), 1);

        return isFirstMode
                ? BlockUtil.findNearestBlock(context.getOriginBlock(), 1, 20)
                : BlockUtil.findNearestBlock(context.getOriginBlock(), 1, 40);
    }

    @Override
    public void usage(BlockBreakEvent event)
    {
    }

    @Override
    public int getPriority()
    {
        return 8;
    }

    @Override
    public int getStartLvl()
    {
        return 1;
    }

    @Override
    public int getMaxLvl()
    {
        return 2;
    }

    @Override
    public String getKey()
    {
        return this.key;
    }

    @Override
    public EnchantmentTarget getTarget()
    {
        return EnchantmentTarget.AXES;
    }
}
