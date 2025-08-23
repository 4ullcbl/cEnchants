package su.trident.cenchants.context.blockbreak;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchants.enchant.api.BlockBreakableEnchantment;
import su.trident.cenchants.enchant.api.Enchantment;
import su.trident.cenchants.enchant.api.EnchantmentStorage;
import su.trident.cenchants.util.block.BlockUtil;
import su.trident.cenchants.util.durability.DurabilityOptions;
import su.trident.cenchants.util.worldguard.WorldGuardUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlockBreakListener implements Listener
{
    private final EnchantmentStorage storage;
    private final WorldGuardUtil util;

    public BlockBreakListener(EnchantmentStorage storage, WorldGuardUtil util)
    {
        this.storage = storage;
        this.util = util;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final ItemStack tool = player.getInventory().getItemInMainHand();

        if (event.isCancelled()) return;

        if (tool.getItemMeta() == null || tool.getType() == Material.AIR || !tool.hasItemMeta() || tool.getType().isEmpty())
            return;

        final BlockBreakContext context = new BlockBreakContext(util, player, block, tool, event.getExpToDrop());
        saveDrops(context, tool);

        final List<Enchantment<?>> enchantments = storage.getAll(tool);

        enchantments.sort(Comparator.comparingInt(Enchantment::getPriority));

        enchantments.stream()
                .filter(enchantment -> enchantment instanceof BlockBreakableEnchantment)
                .map(BlockBreakableEnchantment.class::cast)
                .forEach(enchantment -> enchantment.apply(context));

        int blockCount = context.getAffectedBlocks().size();

        DurabilityOptions.damageItem(player, tool, blockCount);

        event.setDropItems(false);
        event.setCancelled(true);

        breakAllBlocks(context);
        spawnExperienceOrb(event, context);
        dropLoot(context);
    }

    private void saveDrops(BlockBreakContext context, ItemStack tool)
    {
        final List<ItemStack> dropToAdd = new ArrayList<>();

        for (Block b : context.getAffectedBlocks()) {
            if (!util.canBreakBlock(context.getPlayer(), b)) continue;
            dropToAdd.addAll(b.getDrops(tool));
        }

        context.getDrops().addAll(dropToAdd);
    }

    private void breakAllBlocks(BlockBreakContext context)
    {
        for (Block affected : context.getAffectedBlocks()) {
            if (BlockUtil.getUnbreakable().contains(affected.getType())) continue;
            if (!util.canBreakBlock(context.getPlayer(), affected)) continue;

            BlockUtil.spawnBreakParticle(affected);
            affected.setType(Material.AIR);
        }
    }

    private void spawnExperienceOrb(BlockExpEvent event, BlockBreakContext context) {
        if (event.getExpToDrop() == 0 || !context.expDrop) return;

        final ExperienceOrb orb = (ExperienceOrb) event.getBlock().getWorld().spawnEntity(event.getBlock().getLocation(), EntityType.EXPERIENCE_ORB);

        orb.setExperience(event.getExpToDrop());
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
