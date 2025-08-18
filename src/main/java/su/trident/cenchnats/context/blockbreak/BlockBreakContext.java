package su.trident.cenchnats.context.blockbreak;

import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockBreakContext
{


    protected final Player player;
    protected final ItemStack tool;
    protected final Block originBlock;

    protected final Set<Block> affectedBlocks = new HashSet<>();
    protected final List<ItemStack> drops = new ArrayList<>();
    protected final List<Item> dropped = new ArrayList<>();

    protected int blockCount;

    public BlockBreakContext(Player player, Block originBlock, ItemStack tool)
    {
        this.player = player;
        this.tool = tool;
        this.originBlock = originBlock;
        this.affectedBlocks.add(originBlock);
        blockCount = affectedBlocks.size();
    }

    public Player getPlayer()
    {
        return player;
    }

    public ItemStack getTool()
    {
        return tool;
    }

    public Block getOriginBlock()
    {
        return originBlock;
    }

    public Set<Block> getAffectedBlocks()
    {
        return affectedBlocks;
    }

    public List<ItemStack> getDrops()
    {
        return drops;
    }

    public List<Item> getDropped()
    {
        return dropped;
    }

    public int getBlockCount()
    {
        return blockCount;
    }
}
