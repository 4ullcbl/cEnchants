package su.trident.cenchnats.context.blockbreak;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.api.BlockBreakableEnchant;
import su.trident.cenchnats.enchant.api.Enchant;
import su.trident.cenchnats.util.block.BlockUtil;
import su.trident.cenchnats.util.durability.DurabilityOptions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlockBreakListener implements Listener
{
    private final CEnchants plugin;

    public BlockBreakListener(CEnchants plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {

        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final ItemStack tool = player.getInventory().getItemInMainHand();

        if (tool.getItemMeta() == null || tool.getType() == Material.AIR || !tool.hasItemMeta() || tool.getType().isEmpty())
            return;

        final BlockBreakContext context = new BlockBreakContext(player, block, tool);
        saveDrops(context, tool);

        final List<Enchant<?>> enchants = plugin.getStorage().getAll(tool);

        enchants.sort(Comparator.comparingInt(Enchant::getPriority));

        for (Enchant<?> e : enchants) {
            if (e instanceof BlockBreakableEnchant enchant) {
                enchant.apply(context);
            }
        }
        int blockCount = context.getAffectedBlocks().size();

        DurabilityOptions.damageItem(player, tool, blockCount);

        event.setDropItems(false);
        event.setCancelled(true);

        breakAllBlocks(context);
        dropLoot(context);
    }

    private void saveDrops(BlockBreakContext context, ItemStack tool)
    {
        final List<ItemStack> dropToAdd = new ArrayList<>();

        for (Block b : context.getAffectedBlocks()) {
            dropToAdd.addAll(b.getDrops(tool));
        }

        context.getDrops().addAll(dropToAdd);
    }

    private void breakAllBlocks(BlockBreakContext context)
    {
        for (Block affected : context.getAffectedBlocks()) {
            if (BlockUtil.getUnbreakable().contains(affected.getType())) continue;

            BlockUtil.spawnBreakParticle(affected);
            affected.setType(Material.AIR);
        }
    }

    private void dropLoot(BlockBreakContext context)
    {
        final List<Item> toAdd = new ArrayList<>();

        for (ItemStack dropped : context.getDrops()) {
            Item item = context.getOriginBlock().getWorld().dropItem(BlockUtil.getCenter(context.getOriginBlock()), dropped);
            toAdd.add(item);
        }

        context.getDropped().addAll(toAdd);
    }
}
