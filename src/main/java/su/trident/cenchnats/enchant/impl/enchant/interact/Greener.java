package su.trident.cenchnats.enchant.impl.enchant.interact;

import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import su.trident.cenchnats.CEnchants;
import su.trident.cenchnats.enchant.EnchantTarget;
import su.trident.cenchnats.enchant.api.Enchant;

public class Greener extends Enchant<PlayerInteractEvent>
{

    private final String key;
    private final CEnchants plugin;

    public Greener(String key, CEnchants plugin)
    {
        super(key, plugin);
        this.key = key;
        this.plugin = plugin;
    }

    @Override
    @EventHandler
    public void usage(PlayerInteractEvent event)
    {
        if (!hasEnchant(event.getPlayer().getInventory().getItemInMainHand()))
            return;

        if (event.getClickedBlock() == null || event.getClickedBlock().getType().isAir() || (!(event.getClickedBlock().getBlockData() instanceof Ageable ageable)))
            return;

        if (ageable.getAge() >= ageable.getMaximumAge())
            return;

        if (!plugin.getWorldGuardUtil().canPlaceBlock(event.getPlayer(), event.getClickedBlock()))
            return;

        ageable.setAge(ageable.getAge() + 1);
        event.getClickedBlock().setBlockData(ageable);

        greenerVisual(event.getPlayer(), event.getClickedBlock());
    }

    private void greenerVisual(Player player, Block block) {
        player.getWorld().spawnParticle(
                Particle.VILLAGER_HAPPY,
                block.getLocation().add(0.5, 0, 0.5),
                6,
                0.3, 0.5, 0.3,
                0.05
        );
        player.swingMainHand();
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
    public EnchantTarget getTarget()
    {
        return EnchantTarget.TOOLS;
    }
}
