package su.trident.cenchants.enchant.impl.enchant.blockexp;

import org.bukkit.event.block.BlockExpEvent;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.context.blockbreak.BlockBreakContext;
import su.trident.cenchants.enchant.EnchantmentTarget;
import su.trident.cenchants.enchant.api.BlockBreakableEnchantment;
import su.trident.cenchants.enchant.api.Enchantment;

public class Experienced extends Enchantment<BlockExpEvent> implements BlockBreakableEnchantment
{
    private final CEnchants plugin;
    private final String key;

    public Experienced(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.plugin = plugin;
        this.key = key;
    }

    @Override
    public void apply(BlockBreakContext context)
    {
        if (context.getExpToDrop() == 0) return;
        if (!hasEnchant(context.getTool())) return;

        getUsageByLevel(getLevel(context.getTool(), this), context);
    }

    private void getUsageByLevel(int level, BlockBreakContext context)
    {
        if (level == 0 || context.getExpToDrop() == 0) return;

        if (level == 1) {
            context.setExpDrop(true);
            context.setExpToDrop((int) (context.getExpToDrop() * 1.2));
        } else if (level == 2) {
            context.setExpDrop(true);
            context.setExpToDrop((int) (context.getExpToDrop() * 1.5));
            return;
        }

        context.setExpDrop(true);
        context.setExpToDrop(context.getExpToDrop() * 2);
    }

    @Override
    public void usage(BlockExpEvent event)
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
        return 3;
    }

    @Override
    public EnchantmentTarget getTarget()
    {
        return EnchantmentTarget.TOOLS;
    }

    @Override
    public String getKey()
    {
        return this.key;
    }
}
