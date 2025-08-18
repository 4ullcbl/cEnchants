package su.trident.cenchnats.enchant.impl.enchant;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.context.blockbreak.BlockBreakContext;
import su.trident.cenchnats.enchant.api.BlockBreakableEnchant;
import su.trident.cenchnats.enchant.api.Enchant;
import su.trident.cenchnats.util.block.BlockUtil;

import java.util.ArrayList;
import java.util.List;


public class Bulldozer extends Enchant<BlockBreakEvent> implements BlockBreakableEnchant
{

    private final String key;
    private final CEnchants plugin;

    public Bulldozer(String key, CEnchants plugin)
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

        if (hasEnchant(tool, plugin.getRegister().getWeb()) && BlockUtil.getOres().contains(context.getOriginBlock().getType())) return;

        final List<Block> blocksToBreak = getBlocksToBreak(tool, context.getOriginBlock(), context.getPlayer());

        context.getAffectedBlocks().addAll(blocksToBreak);

        final List<ItemStack> dropToAdd = new ArrayList<>();

        for (Block block: context.getAffectedBlocks()) {
            if (block.equals(context.getOriginBlock())) continue;
            dropToAdd.addAll(block.getDrops(tool));
        }

        context.getDrops().addAll(dropToAdd);
    }

    @Override
    public void usage(BlockBreakEvent event)
    {
    }

    @Override
    public int getPriority()
    {
        return 10;
    }


    private List<Block> getBlocksToBreak(ItemStack item, Block block, Player player) {
        boolean isFirstMode = hasEnchant(item, 1);

        return isFirstMode
                ? bulldozerFirstBlockToBreak(block, player)
                : bulldozerSecondBlockToBreak(block);
    }

    private List<Block> bulldozerSecondBlockToBreak(Block center)
    {
        final List<Block> result = new ArrayList<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    Block relative = center.getRelative(dx, dy, dz);
                    result.add(relative);
                }
            }
        }
        return result;
    }

    private List<Block> bulldozerFirstBlockToBreak(Block center, Player player)
    {

        final List<Block> result = new ArrayList<>();
        Vector direction = player.getLocation().getDirection().normalize();

        boolean isLookingMostlyUpDown = Math.abs(direction.getY()) > 0.7;

        if (isLookingMostlyUpDown) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    final Block relative = center.getRelative(dx, 0, dz);
                    result.add(relative);
                }
            }
        } else {
            if (Math.abs(direction.getX()) > Math.abs(direction.getZ())) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        Block relative = center.getRelative(0, dy, dz);
                        result.add(relative);
                    }
                }
            } else {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        Block relative = center.getRelative(dx, dy, 0);
                        result.add(relative);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public String getName()
    {
        return "Bulldozer";
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
}
