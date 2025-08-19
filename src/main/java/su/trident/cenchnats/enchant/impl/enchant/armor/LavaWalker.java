package su.trident.cenchnats.enchant.impl.enchant.armor;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.api.Enchant;

import java.util.*;

public class LavaWalker extends Enchant<PlayerArmorChangeEvent>
{
    private final String key;
    private final CEnchants plugin;

    private final Set<UUID> lavaWalkers = new HashSet<>();

    public LavaWalker(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    @EventHandler
    public void usage(PlayerArmorChangeEvent event)
    {
        if (event.getSlotType() != PlayerArmorChangeEvent.SlotType.FEET) return;
        if (event.getNewItem() == null || event.getOldItem() == null) return;

        boolean isNewJumpBoots = hasEnchant(event.getNewItem());
        boolean isOldJumpBoots = hasEnchant(event.getOldItem());

        if (isOldJumpBoots && !isNewJumpBoots) {
            lavaWalkers.remove(event.getPlayer().getUniqueId());
            return;
        }

        if (isNewJumpBoots) {
            lavaWalkers.add(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom() == event.getTo()) return;
        if (!lavaWalkers.contains(event.getPlayer().getUniqueId())) return;

        final Block blockUnderPlayer = event.getPlayer().getLocation().subtract(0, 1, 0).getBlock();
        final Player player = event.getPlayer();

        if (!blockUnderPlayer.getType().equals(Material.LAVA)) return;

        final List<Block> blocksToReplace = getBlocksToReplaceByLevel(event.getPlayer().getInventory().getBoots(), blockUnderPlayer, player);

        replaceLavaToObsidian(blocksToReplace, player);

        lavaWalkerVisual(player);
    }

    private void lavaWalkerVisual(Player player)
    {
        player.getWorld().playSound(
                player.getLocation(),
                Sound.BLOCK_FIRE_EXTINGUISH,
                1,
                1
        );
    }

    private void replaceLavaToObsidian(List<Block> blocksToReplace, Player player)
    {
        for (Block block : blocksToReplace) {
            if (block.getType() != Material.LAVA && player.getLocation().getBlock().getType() != Material.AIR) continue;

            if (block.getBlockData() instanceof Levelled levelled) {
                if (levelled.getLevel() == 0) {
                    block.setType(Material.OBSIDIAN);
                } else {
                    block.setType(Material.BASALT);
                }
            }
        }
    }
    private List<Block> getBlocksToReplaceByLevel(ItemStack item, Block block, Player player) {
        boolean isFirstMode = hasEnchant(item, 1);

        return isFirstMode
                ? getBlocksToReplace(block, 1)
                : getBlocksToReplace(block, 2);
    }

    private List<Block> getBlocksToReplace(Block underPlayer, int radius)
    {
        final List<Block> result = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                final Block relative = underPlayer.getRelative(dx, 0, dz);
                result.add(relative);
            }
        }

        return result;
    }

    @Override
    public int getPriority()
    {
        return 0;
    }

    @Override
    public String getName()
    {
        return "Лаваход";
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
