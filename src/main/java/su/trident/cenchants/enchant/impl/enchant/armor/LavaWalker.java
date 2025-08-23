package su.trident.cenchants.enchant.impl.enchant.armor;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import su.trident.cenchants.CEnchants;
import su.trident.cenchants.enchant.EnchantmentTarget;
import su.trident.cenchants.enchant.api.Enchantment;

import java.util.*;

public class LavaWalker extends Enchantment<PlayerArmorChangeEvent>
{
    private final String key;
    private final CEnchants plugin;
    private final Set<UUID> lavaWalkers = new HashSet<>();

    private Material mainTypeToReplace;
    private Material secondaryTypeToReplace;

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

        if (blocksToReplace.size() < 3) return;

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

    @Override
    public void loadConfig()
    {
        loadConfigPath();
        loadDefaultValue();

        mainTypeToReplace = Material.valueOf(getConfig().getString(getConfigPath() + "main_block"));
        secondaryTypeToReplace = Material.valueOf(getConfig().getString(getConfigPath() + "second_block"));
    }

    private void replaceLavaToObsidian(List<Block> blocksToReplace, Player player)
    {
        for (Block block : blocksToReplace) {
            if (block.getType() != Material.LAVA && player.getLocation().getBlock().getType() != Material.AIR) continue;

            if (!this.plugin.getWorldGuardUtil().canPlaceBlock(player, block)) continue;

            if (block.getBlockData() instanceof Levelled levelled) {
                if (levelled.getLevel() == 0) {
                    block.setType(mainTypeToReplace);
                } else {
                    block.setType(secondaryTypeToReplace);
                }
            }
        }
    }
    private List<Block> getBlocksToReplaceByLevel(ItemStack item, Block block, Player player) {
        boolean isFirstMode = hasEnchant(item, 1);

        return isFirstMode
                ? getBlocksToReplace(player, block, 1)
                : getBlocksToReplace(player, block, 2);
    }

    private List<Block> getBlocksToReplace(Player player, Block underPlayer, int radius)
    {
        final List<Block> result = new ArrayList<>();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                final Block relative = underPlayer.getRelative(dx, 0, dz);
                if (!this.plugin.getWorldGuardUtil().canPlaceBlock(player, relative)) continue;
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
        return EnchantmentTarget.BOOTS;
    }
}
