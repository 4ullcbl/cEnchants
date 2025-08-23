package su.trident.cenchants.enchant.impl.enchant.blockbreak;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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

public class MegaBulldozer extends Enchantment<BlockBreakEvent> implements BlockBreakableEnchantment
{

    private final CEnchants plugin;
    private final String key;

    public MegaBulldozer(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.plugin = plugin;
        this.key = key;
    }

    @Override
    public void apply(BlockBreakContext context)
    {
        if (!hasEnchant(context.getTool())) return;

        if ((hasEnchant(context.getTool(), plugin.getRegister().getWeb()) && BlockUtil.getOres().contains(context.getOriginBlock().getType()))
                || (hasEnchant(context.getTool(), plugin.getRegister().getWoodCutter()) && context.getOriginBlock().getType().toString().contains("_LOG")))
            return;

        final List<Block> blocksToBreak = getBlocksToBreak(context.getOriginBlock(), context.getPlayer());

        context.getAffectedBlocks().addAll(blocksToBreak);

        final List<ItemStack> dropToAdd = new ArrayList<>();

        for (Block block: context.getAffectedBlocks()) {

            if (!this.plugin.getWorldGuardUtil().canBreakBlock(context.getPlayer(), block)) continue;

            if (block.equals(context.getOriginBlock())) continue;
            dropToAdd.addAll(block.getDrops(context.getTool()));
        }

        context.getDrops().addAll(dropToAdd);
    }

    private List<Block> getBlocksToBreak(Block start, Player player)
    {
        final List<Block> result = new ArrayList<>();

        for (int dx = -2; dx <= 2; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -2; dz <= 2; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    final Block relative = start.getRelative(dx, dy, dz);
                    if (!this.plugin.getWorldGuardUtil().canBreakBlock(player, relative)) continue;
                    result.add(relative);
                }
            }
        }
        return result;
    }

    @Override
    public void usage(BlockBreakEvent event)
    {
    }

    @Override
    public int getPriority()
    {
        return 9;
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
